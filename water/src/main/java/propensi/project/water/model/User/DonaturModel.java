package propensi.project.water.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import propensi.project.water.model.Donasi.DonasiModel;
import propensi.project.water.model.PoinReward.TukarPoinModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "donatur")
@PrimaryKeyJoinColumn(name="username")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DonaturModel extends UserModel {
    @NotNull
    @Column(name="poin", nullable=false, columnDefinition = "int default 0")
    private Integer poin;

    @NotNull
    @Column(name="alamat", nullable=false)
    private String alamat;

    // relasi dengan donasi
    @OneToMany(mappedBy = "donatur", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DonasiModel> listDonasi;

    // relasi dengan tukar poin
    @OneToMany(mappedBy = "donatur", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TukarPoinModel> listTukarPoin;

    private String kelurahan;
    private String kecamatan;
    private String kota;
    private String provinsi;
    private String kodePos;
}
