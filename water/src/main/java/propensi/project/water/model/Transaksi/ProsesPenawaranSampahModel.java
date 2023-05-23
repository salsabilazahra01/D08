package propensi.project.water.model.Transaksi;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import propensi.project.water.model.PembelianSampah.PenawaranSampahModel;

import javax.persistence.*;

@Entity
@Table(name = "proses_penawaran_sampah")
@PrimaryKeyJoinColumn(name="id_transaksi")
@Setter
@Getter
@NoArgsConstructor
public class ProsesPenawaranSampahModel extends TransaksiModel{

    // relasi dengan penawaran sampah
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_penawaran_sampah", referencedColumnName = "id_penawaran_sampah")
    private PenawaranSampahModel penawaranSampah;
}
