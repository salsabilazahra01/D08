package propensi.project.water.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import propensi.project.water.dto.RewardDTO;
import propensi.project.water.model.PoinReward.RewardModel;

public interface RewardService {
    List<RewardModel> findAll();
    Page<RewardModel> findAll(Pageable paging);
    RewardModel findById(String id);
    RewardModel findByJenis(String jenis);
    RewardModel add(RewardDTO dto);
    RewardModel update(RewardDTO dto, RewardModel model);
    RewardModel delete(RewardModel model);

}
