package propensi.project.water.repository.PenawaranSampah;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import propensi.project.water.model.PembelianSampah.ItemPenawaranSampahModel;

import java.util.Optional;


@Repository
public interface ItemPenawaranSampahDb extends JpaRepository<ItemPenawaranSampahModel, String> {
    Optional<ItemPenawaranSampahModel> findAllByIdPenawaranSampah_IdPenawaranSampah(String idPenawaran);

}
