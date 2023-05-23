package propensi.project.water.model.Transaksi;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import propensi.project.water.model.PenjualanHasilOlahan.PenawaranOlahanModel;

import javax.persistence.*;

@Entity
@Table(name = "proses_penawaran_olahan")
@PrimaryKeyJoinColumn(name="id_transaksi")
@Setter
@Getter
@NoArgsConstructor
public class ProsesPenawaranOlahanModel extends TransaksiModel{

    // relasi dengan penawaran olahan
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_penawaran_olahan", referencedColumnName = "id_penawaran_olahan")
    private PenawaranOlahanModel penawaranOlahan;

}
