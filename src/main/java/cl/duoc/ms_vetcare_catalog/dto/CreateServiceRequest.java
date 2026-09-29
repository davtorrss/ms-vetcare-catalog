package cl.duoc.ms_vetcare_catalog.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CreateServiceRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio no puede ser negativo")
    private BigDecimal price;

    @NotNull(message = "Los cupos disponibles son obligatorios")
    @Min(value = 0, message = "Los cupos no pueden ser negativos")
    private Integer availableSlots;

    public CreateServiceRequest() {
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