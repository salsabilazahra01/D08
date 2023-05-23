package propensi.project.water.exceptions.donasi;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import propensi.project.water.model.PoinReward.RewardModel;

public class RewardDuplicateJenisException extends EntityExistsException {
    public RewardDuplicateJenisException(RewardModel model) {
        super("Reward " + model.jenisReward() + " sudah ada!");
    }
}
