package cl.duoc.ms_vetcare_catalog.dto;

import cl.duoc.ms_vetcare_catalog.entity.VeterinaryService;

import java.math.BigDecimal;

public class ServiceResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private Integer availableSlots;

    public ServiceResponse() {
    }

    public ServiceResponse(
            Long id,
            String name,
            BigDecimal price,
            Integer availableSlots
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.availableSlots = availableSlots;
    }

    public static ServiceResponse fromEntity(VeterinaryService service) {
        return new ServiceResponse(
                service.getId(),
                service.getName(),
                service.getPrice(),
                service.getAvailableSlots()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getAvailableSlots() {
        return availableSlots;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setAvailableSlots(Integer availableSlots) {
        this.availableSlots = availableSlots;
    }
}