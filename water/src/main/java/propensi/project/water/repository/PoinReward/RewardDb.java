package propensi.project.water.repository.PoinReward;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import propensi.project.water.model.PoinReward.RewardModel;

@Repository
public interface RewardDb extends JpaRepository<RewardModel, String> {
    Optional<RewardModel> findById(String id);
    List<RewardModel> findAll();
    void delete(RewardModel reward);
}
