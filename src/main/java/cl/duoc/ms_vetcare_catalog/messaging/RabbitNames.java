package cl.duoc.ms_vetcare_catalog.messaging;

public final class RabbitNames {

    private RabbitNames() {
    }

    // Exchanges
    public static final String DIRECT_EXCHANGE = "vetcare.direct.exchange";
    public static final String TOPIC_EXCHANGE = "vetcare.topic.exchange";
    public static final String DLX_EXCHANGE = "vetcare.dlx.exchange";

    // Catalog Queue
    public static final String CATALOG_QUEUE = "vetcare.catalog.queue";
    public static final String CATALOG_DLQ = "vetcare.catalog.dlq";

    // Routing keys Catalog
    public static final String CATALOG_CREATED = "catalog.service.created";
    public static final String CATALOG_UPDATED = "catalog.service.updated";
    public static final String CATALOG_DELETED = "catalog.service.deleted";

    // Routing key DLQ
    public static final String CATALOG_DLQ_ROUTING_KEY = "catalog.dlq";
}