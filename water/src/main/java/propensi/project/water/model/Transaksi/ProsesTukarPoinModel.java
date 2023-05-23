package propensi.project.water.model.Transaksi;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import propensi.project.water.model.PoinReward.TukarPoinModel;

import javax.persistence.*;

@Entity
@Table(name = "proses_tukar_poin")
@PrimaryKeyJoinColumn(name="id_transaksi")
@Setter
@Getter
@NoArgsConstructor
public class ProsesTukarPoinModel extends TransaksiModel{

    // relasi dengan penawaran sampah
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_tukar_poin", referencedColumnName = "id_tukar_poin")
    private TukarPoinModel tukarPoin;
}