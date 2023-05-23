package propensi.project.water.exceptions.donasi;

import javax.persistence.EntityNotFoundException;

public class RewardNotFoundException extends EntityNotFoundException {
    public RewardNotFoundException(String id) {
        super("Reward dengan id " + id + " tidak ditemukan");
    }
}
