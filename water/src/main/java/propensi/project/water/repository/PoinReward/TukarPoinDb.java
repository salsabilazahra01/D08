package propensi.project.water.repository.PoinReward;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import propensi.project.water.model.PoinReward.TukarPoinModel;
import propensi.project.water.model.User.DonaturModel;

import java.util.List;

@Repository
public interface TukarPoinDb extends JpaRepository<TukarPoinModel, String> {

    Page<TukarPoinModel> findAllByIdTukarPoinIsNotNullOrderByTanggalDibuat(Pageable pageable);

    Page<TukarPoinModel> findAllByStatusOrderByTanggalDibuat(Integer status, Pageable pageable);

    Page<TukarPoinModel> findAllByDonaturAndIdTukarPoinIsNotNullOrderByTanggalDibuat(DonaturModel donatur, Pageable page);

    Page<TukarPoinModel> findAllByDonaturAndStatusOrderByTanggalDibuat(DonaturModel customer, Integer status, Pageable page);

}
