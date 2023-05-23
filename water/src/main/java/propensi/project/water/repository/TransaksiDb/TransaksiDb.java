package propensi.project.water.repository.TransaksiDb;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import propensi.project.water.model.Transaksi.TransaksiModel;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransaksiDb extends JpaRepository<TransaksiModel, String> {
    Page<TransaksiModel> findAllByIdTransaksiIsNotNullOrderByTanggalDibuat(Pageable pageable);
    Page<TransaksiModel> findAllByJenisTransaksiOrderByTanggalDibuat(Boolean jenis, Pageable pageable);
    Page<TransaksiModel> findByIdTransaksiContainingIgnoreCaseOrderByTanggalDibuat(String keyword, Pageable pageable);
    Page<TransaksiModel> findByIdTransaksiContainingIgnoreCaseAndJenisTransaksiOrderByTanggalDibuat(Boolean jenis, String keyword, Pageable pageable);

    @Query(value = "SELECT * , 0 AS clazz_ FROM transaksi " +
            "LEFT OUTER JOIN proses_lain\n" +
            "ON transaksi.id_transaksi = proses_lain.id_transaksi\n" +
            "WHERE CAST(transaksi.tanggal_transaksi AS DATE) >= :startDate AND CAST(transaksi.tanggal_transaksi AS DATE) <= :endDate",
            nativeQuery = true)
    List<TransaksiModel> findListByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
