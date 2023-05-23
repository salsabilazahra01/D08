package propensi.project.water.repository.Donasi;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import propensi.project.water.model.Donasi.DonasiModel;
import propensi.project.water.model.User.DonaturModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonasiDb extends JpaRepository<DonasiModel, String> {

    // case: userSession=internal, status=semua
    Page<DonasiModel> findAllByIdDonasiIsNotNullOrderByTanggalDibuat(Pageable pageable);

    // case: userSession=internal, status=semua
    Page<DonasiModel> findAllByStatusOrderByTanggalDibuat(Integer status, Pageable pageable);

    //case: userSession=donatur, status=semua
    Page<DonasiModel> findAllByDonaturOrderByTanggalDibuat(DonaturModel donatur, Pageable pageable);

    //case: userSession=donatur, status=fragmentStatus
    Page<DonasiModel> findAllByDonaturAndStatusOrderByTanggalDibuat(DonaturModel donatur, Integer status, Pageable pageable);

    Optional<DonasiModel> findByIdDonasi(String idDonasi);
}
