package propensi.project.water.repository.PenawaranSampah;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import propensi.project.water.model.PembelianSampah.PenawaranSampahModel;
import propensi.project.water.model.User.PartnerModel;

import java.util.Optional;

@Repository
public interface PenawaranSampahDb extends JpaRepository<PenawaranSampahModel, String> {
    //Optional<PenawaranSampahModel> findProsesPenawaranSampahModelByIdTransaksi(String id);
    // case: userSession=internal, status=semua
    Page<PenawaranSampahModel> findAllByIdPenawaranSampahIsNotNullOrderByTanggalDibuat(Pageable pageable);

    // case: userSession=internal, status=semua
    Page<PenawaranSampahModel> findAllByStatusOrderByTanggalDibuat(Integer status, Pageable pageable);

    //case: userSession=partner, status=semua
    Page<PenawaranSampahModel> findAllByPartnerOrderByTanggalDibuat(PartnerModel partner, Pageable pageable);

    //case: userSession=partner, status=fragmentStatus
    Page<PenawaranSampahModel> findAllByPartnerAndStatusOrderByTanggalDibuat(PartnerModel partner, Integer status, Pageable pageable);

    Optional<PenawaranSampahModel> findByIdPenawaranSampah(String idPenawaranSampah);

}
