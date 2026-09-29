package cl.duoc.ms_vetcare_catalog.service;

import cl.duoc.ms_vetcare_catalog.dto.CreateServiceRequest;
import cl.duoc.ms_vetcare_catalog.dto.ServiceResponse;
import cl.duoc.ms_vetcare_catalog.dto.UpdateServiceRequest;
import cl.duoc.ms_vetcare_catalog.entity.VeterinaryService;
import cl.duoc.ms_vetcare_catalog.exception.ResourceNotFoundException;
import cl.duoc.ms_vetcare_catalog.repository.VeterinaryServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VeterinaryServiceService {

    private final VeterinaryServiceRepository repository;

    public VeterinaryServiceService(VeterinaryServiceRepository repository) {
        this.repository = repository;
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

        return ServiceResponse.fromEntity(saved);
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

        return ServiceResponse.fromEntity(updated);
    }
}