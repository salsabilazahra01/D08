package propensi.project.water.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import propensi.project.water.model.PenjualanHasilOlahan.PenawaranOlahanModel;
import propensi.project.water.model.PoinReward.RewardTukarPoinModel;
import propensi.project.water.model.PoinReward.TukarPoinModel;
import propensi.project.water.model.User.DonaturModel;
import propensi.project.water.repository.PoinReward.RewardTukarPoinDb;
import propensi.project.water.repository.PoinReward.TukarPoinDb;

@Service
@AllArgsConstructor
public class TukarPoinServiceImpl implements TukarPoinService {

    @Autowired
    private final TukarPoinDb tukarPoinDb;

    @Autowired
    private final RewardTukarPoinDb rewardTukarPoinDb;

    @Override
    public List<TukarPoinModel> findAll() {
        return tukarPoinDb.findAll();
    }

    @Override
    public Page<TukarPoinModel> findAll(Pageable paging) {
        return tukarPoinDb.findAll(paging);
    }

    @Override
    public TukarPoinModel findById(String id) {
        Optional<TukarPoinModel> tukarPoin = tukarPoinDb.findById(id);
        if (tukarPoin.isPresent()){
            return tukarPoin.get();
        }
        return null;
    }

    @Override
    public TukarPoinModel add(TukarPoinModel tukarPoin) {
        tukarPoin.setTanggalDibuat(LocalDateTime.now());
        tukarPoin.setStatus(0);

        for(RewardTukarPoinModel reward : tukarPoin.getListReward()){
            reward.setIdTukarPoin(tukarPoin);
        }
        tukarPoin.setListReward(tukarPoin.getListReward());

        return tukarPoinDb.save(tukarPoin);
    }

    @Override
    public void delete(TukarPoinModel updatedTukarPoin){
        TukarPoinModel tukarPoin = findById(updatedTukarPoin.getIdTukarPoin());
        tukarPoin.setStatus(4);
        tukarPoin.setKeteranganTolak("Penukaran poin dibatalkan oleh Donatur");
        tukarPoinDb.save(tukarPoin);
    }

    @Override
    public Page<TukarPoinModel> findAllByDonatur(Pageable paging, DonaturModel donatur, Integer status) {
        if(donatur == null){
            if(status == -1){
                return tukarPoinDb.findAllByIdTukarPoinIsNotNullOrderByTanggalDibuat(paging);
            }
            return tukarPoinDb.findAllByStatusOrderByTanggalDibuat(status, paging);
        } else {
            if(status == -1){
                return tukarPoinDb.findAllByDonaturAndIdTukarPoinIsNotNullOrderByTanggalDibuat(donatur,paging);
            }
            return tukarPoinDb.findAllByDonaturAndStatusOrderByTanggalDibuat(donatur,status, paging);
        }
    }

    @Override
    public TukarPoinModel setDonaturInfo(DonaturModel donatur, TukarPoinModel tukarPoin){
        tukarPoin.setDonatur(donatur);
        tukarPoin.setNamaDonatur(donatur.getNama());
        if(donatur.getEmail() != null){
            tukarPoin.setEmail(donatur.getEmail());
        }
        if(donatur.getHp() != null){
            tukarPoin.setHp(donatur.getHp());
        }
        tukarPoin.setAlamatDonatur(donatur.getAlamat());

        return tukarPoin;
    }

    @Override
    public List<RewardTukarPoinModel> getListRewardById(String idTukarPoin){
        List<RewardTukarPoinModel> listAll = rewardTukarPoinDb.findAll();
        List<RewardTukarPoinModel> listById = new ArrayList<>();

        for(RewardTukarPoinModel reward:listAll){
            if(reward.getIdTukarPoin().getIdTukarPoin().equals(idTukarPoin)){
                listById.add(reward);
            }
        }

        return listById;
    }

    @Override
    public TukarPoinModel update(TukarPoinModel updatedTukarPoin) {
        return tukarPoinDb.save(updatedTukarPoin);
    }

}
