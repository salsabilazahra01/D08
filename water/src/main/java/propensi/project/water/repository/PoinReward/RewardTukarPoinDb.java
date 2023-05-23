package propensi.project.water.repository.PoinReward;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import propensi.project.water.model.PoinReward.RewardTukarPoinModel;
import propensi.project.water.model.PoinReward.TukarPoinModel;

@Repository
public interface RewardTukarPoinDb extends JpaRepository<RewardTukarPoinModel, String> {
}
