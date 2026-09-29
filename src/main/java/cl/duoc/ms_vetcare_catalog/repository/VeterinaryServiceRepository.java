package cl.duoc.ms_vetcare_catalog.repository;

import cl.duoc.ms_vetcare_catalog.entity.VeterinaryService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VeterinaryServiceRepository extends JpaRepository<VeterinaryService, Long> {
}