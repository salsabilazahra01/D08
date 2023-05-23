package propensi.project.water.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.val;
import propensi.project.water.dto.RewardDTO;
import propensi.project.water.exceptions.donasi.RewardDuplicateJenisException;
import propensi.project.water.exceptions.donasi.RewardNotFoundException;
import propensi.project.water.model.PoinReward.RewardModel;
import propensi.project.water.repository.PoinReward.RewardDb;
import propensi.project.water.service.RewardService;

@Service
@AllArgsConstructor
public class RewardServiceImpl implements RewardService {

    @Autowired
    private final RewardDb rewardDb;

    @Override
    public List<RewardModel> findAll() {
        return rewardDb.findAll();
    }

    @Override
    public Page<RewardModel> findAll(Pageable paging) {
        return rewardDb.findAll(paging);
    }

    @Override
    public RewardModel findById(String id) {
        return rewardDb.findById(id)
                .orElseThrow(() -> new RewardNotFoundException(id));
    }

    @Override
    public RewardModel findByJenis(String jenis) {
        return rewardDb.findAll(
                Example.of(RewardModel.builder().jenisReward(jenis).build())
        ).stream().findFirst().orElse(null);

    }

    @Override
    public RewardModel add(RewardDTO dto) {
        return rewardDb.save(dto.toModel());
    }

    @Override
    public RewardModel update(RewardDTO dto, RewardModel model) {
        val lookup = findByJenis(model.getJenisReward());
        model = dto.toModel(model);
        if (lookup != null && !lookup.getIdReward().equals(model.getIdReward()))
            throw new RewardDuplicateJenisException(model);
        return rewardDb.save(model);
    }

    @Override
    public RewardModel delete(RewardModel model) {
        rewardDb.delete(model);
        return model;
    }

}
