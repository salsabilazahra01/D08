package propensi.project.water.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import propensi.project.water.model.PenjualanHasilOlahan.PenawaranOlahanModel;
import propensi.project.water.model.PoinReward.RewardTukarPoinModel;
import propensi.project.water.model.PoinReward.TukarPoinModel;
import propensi.project.water.model.User.DonaturModel;

public interface TukarPoinService {
    List<TukarPoinModel> findAll();
    Page<TukarPoinModel> findAll(Pageable paging);
    Page<TukarPoinModel> findAllByDonatur(Pageable paging, DonaturModel donatur, Integer status) ;
    TukarPoinModel findById(String id);
    TukarPoinModel add(TukarPoinModel tukarPoin);
    TukarPoinModel setDonaturInfo(DonaturModel donatur, TukarPoinModel tukarPoin);
    List<RewardTukarPoinModel> getListRewardById(String idTukarPoin);
    void delete(TukarPoinModel updatedTukarPoin);
    TukarPoinModel update(TukarPoinModel tukarPoinEx);
}
