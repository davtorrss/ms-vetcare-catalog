package cl.duoc.ms_vetcare_catalog.messaging;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class CatalogEventConsumer {

    private static final int MAX_RETRIES = 3;
    private static final long INITIAL_BACKOFF_MS = 1000;

    @RabbitListener(queues = RabbitNames.CATALOG_QUEUE)
    public void consume(Message message, Channel channel) throws IOException {

        long deliveryTag =
                message.getMessageProperties().getDeliveryTag();

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {

            try {

                processMessage(message);

                // ACK manual:
                // el mensaje se procesó correctamente.
                channel.basicAck(
                        deliveryTag,
                        false
                );

                System.out.println(
                        "ACK enviado. Mensaje Catalog procesado correctamente."
                );

                return;

            } catch (Exception e) {

                System.err.println(
                        "Error procesando mensaje Catalog. Intento "
                                + attempt
                                + "/"
                                + MAX_RETRIES
                                + " - "
                                + e.getMessage()
                );

                if (attempt < MAX_RETRIES) {

                    long backoff =
                            INITIAL_BACKOFF_MS
                                    * (1L << (attempt - 1));

                    System.out.println(
                            "Reintentando en "
                                    + backoff
                                    + " ms..."
                    );

                    try {

                        Thread.sleep(backoff);

                    } catch (InterruptedException interruptedException) {

                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }

        System.err.println(
                "Se agotaron los reintentos. "
                        + "Enviando mensaje a DLQ."
        );

        // NACK manual:
        // false = afecta solo este mensaje.
        // false = no reencolar.
        // RabbitMQ lo enviará al DLX/DLQ configurado.
        channel.basicNack(
                deliveryTag,
                false,
                false
        );
    }

    private void processMessage(Message message) {

        String body = new String(
                message.getBody(),
                StandardCharsets.UTF_8
        );

        if (body.isBlank()) {

            throw new IllegalArgumentException(
                    "El mensaje recibido está vacío"
            );
        }

        System.out.println(
                "Mensaje recibido en Catalog:"
        );

        System.out.println(body);
    }
}