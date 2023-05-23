package propensi.project.water.model.User;

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import propensi.project.water.model.Donasi.DonasiModel;
import propensi.project.water.model.PembelianSampah.PenawaranSampahModel;
//import propensi.project.water.model.PembelianSampah.PenawaranSampahModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "partner")
@PrimaryKeyJoinColumn(name="username")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PartnerModel extends UserModel {
    @NotNull
    @Column(name= "nama_pic", nullable=false)
    private String namaPic;

    @NotNull
    @Column(name= "alamat", nullable=false)
    private String alamat;

    // relasi dengan penawaran sampah
    @OneToMany(mappedBy = "partner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PenawaranSampahModel> listPenawaranSampah;

    private String kelurahan;
    private String kecamatan;
    private String kota;
    private String provinsi;
    private String kodePos;
}
