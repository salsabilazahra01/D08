package propensi.project.water.repository.TransaksiDb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import propensi.project.water.model.PenjualanHasilOlahan.PenawaranOlahanModel;
import propensi.project.water.model.Transaksi.ProsesPenawaranOlahanModel;

import java.util.Optional;

@Repository
public interface ProsesPenawaranOlahanDb extends JpaRepository<ProsesPenawaranOlahanModel, String> {
    Optional<ProsesPenawaranOlahanModel> findProsesPenawaranOlahanModelByIdTransaksi(String id);
    Optional<ProsesPenawaranOlahanModel> findProsesPenawaranOlahanModelByPenawaranOlahan(PenawaranOlahanModel penawaranOlahanModel);
}
