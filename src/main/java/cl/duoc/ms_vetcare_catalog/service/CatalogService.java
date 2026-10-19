package cl.duoc.ms_vetcare_catalog.service;

import cl.duoc.ms_vetcare_catalog.dto.CreateServiceRequest;
import cl.duoc.ms_vetcare_catalog.dto.ServiceResponse;
import cl.duoc.ms_vetcare_catalog.dto.UpdateServiceRequest;
import cl.duoc.ms_vetcare_catalog.entity.VeterinaryService;
import cl.duoc.ms_vetcare_catalog.exception.ResourceNotFoundException;
import cl.duoc.ms_vetcare_catalog.messaging.CatalogEventPublisher;
import cl.duoc.ms_vetcare_catalog.repository.VeterinaryServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogService {

    private final VeterinaryServiceRepository repository;
    private final CatalogEventPublisher eventPublisher;

    public CatalogService(
            VeterinaryServiceRepository repository,
            CatalogEventPublisher eventPublisher
    ) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    public List<ServiceResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(ServiceResponse::fromEntity)
                .toList();
    }

    public ServiceResponse create(CreateServiceRequest request) {

        VeterinaryService service = new VeterinaryService(
                request.getName(),
                request.getPrice(),
                request.getAvailableSlots()
        );

        VeterinaryService saved = repository.save(service);

        ServiceResponse response = ServiceResponse.fromEntity(saved);

        eventPublisher.publishCreated(response);

        return response;
    }

    public ServiceResponse update(
            Long id,
            UpdateServiceRequest request
    ) {

        VeterinaryService service = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Servicio no encontrado con id: " + id
                        )
                );

        service.setPrice(request.getPrice());
        service.setAvailableSlots(request.getAvailableSlots());

        VeterinaryService updated = repository.save(service);

        ServiceResponse response = ServiceResponse.fromEntity(updated);

        eventPublisher.publishUpdated(response);

        return response;
    }
}