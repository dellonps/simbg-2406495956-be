package apap.ti._6.simbg_2406495956_be.repository;

import apap.ti._6.simbg_2406495956_be.model.ClaimItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClaimItemRepository extends JpaRepository<ClaimItem, String> {

    List<ClaimItem> findByClaimId(String claimId);

    @Query("select coalesce(sum(i.amount), 0L) from ClaimItem i where i.budgetComponent.id = :componentId")
    Long sumAmountByComponentId(@Param("componentId") String componentId);
}
