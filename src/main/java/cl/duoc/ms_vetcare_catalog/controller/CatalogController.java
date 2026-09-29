package cl.duoc.ms_vetcare_catalog.controller;

import cl.duoc.ms_vetcare_catalog.dto.CreateServiceRequest;
import cl.duoc.ms_vetcare_catalog.dto.ServiceResponse;
import cl.duoc.ms_vetcare_catalog.dto.UpdateServiceRequest;
import cl.duoc.ms_vetcare_catalog.service.VeterinaryServiceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/services")
public class CatalogController {

    private final VeterinaryServiceService service;

    public CatalogController(VeterinaryServiceService service) {
        this.service = service;
    }

    @GetMapping
    public List<ServiceResponse> getServices() {
        return service.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceResponse createService(
            @Valid @RequestBody CreateServiceRequest request
    ) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public ServiceResponse updateService(
            @PathVariable Long id,
            @Valid @RequestBody UpdateServiceRequest request
    ) {
        return service.update(id, request);
    }
}