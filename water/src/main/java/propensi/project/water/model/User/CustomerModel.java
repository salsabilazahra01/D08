package propensi.project.water.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import propensi.project.water.model.PembelianSampah.PenawaranSampahModel;
import propensi.project.water.model.PenjualanHasilOlahan.PenawaranOlahanModel;
//import propensi.project.water.model.PenjualanHasilOlahan.PenawaranOlahanModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "customer")
@PrimaryKeyJoinColumn(name="username")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerModel extends UserModel {
    @NotNull
    @Column(name="nama_pic", nullable=false)
    private String namaPic;

    @NotNull
    @Column(name="alamat", nullable=false)
    private String alamat;

    @Column(name = "bank")
    private String bank;

    @Column(name = "noRekening")
    private Integer noRekening;

    @Column(name = "namaRekening")
    private String namaRekening;

    private String kelurahan;
    private String kecamatan;
    private String kota;
    private String provinsi;
    private String kodePos;

    // relasi dengan penawaran olahan
//    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private List<PenawaranOlahanModel> listPenawaranOlahan;
}
