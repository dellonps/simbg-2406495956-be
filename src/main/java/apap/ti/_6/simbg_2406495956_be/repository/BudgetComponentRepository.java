package apap.ti._6.simbg_2406495956_be.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import apap.ti._6.simbg_2406495956_be.model.BudgetComponent;

public interface BudgetComponentRepository extends JpaRepository<BudgetComponent, String> {

    List<BudgetComponent> findByAnggaranId(String anggaranId);

    @Query("select coalesce(sum(b.allocatedAmount), 0L) from BudgetComponent b where b.anggaran.id = :anggaranId")
    Long sumAllocatedByAnggaranId(@Param("anggaranId") String anggaranId);

    
}