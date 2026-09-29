package cl.duoc.ms_vetcare_catalog.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "veterinary_services")
public class VeterinaryService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer availableSlots;

    public VeterinaryService() {
    }

    public VeterinaryService(String name, BigDecimal price, Integer availableSlots) {
        this.name = name;
        this.price = price;
        this.availableSlots = availableSlots;
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