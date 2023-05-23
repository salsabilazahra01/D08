package propensi.project.water.model.CompanyProfile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import propensi.project.water.model.StringPrefixedSequenceIdGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "company_profile")
public class CompanyProfileModel implements Serializable {
    @Id
    @Column(name = "id_company_profile", nullable = false)
    private String idCompanyProfile;

    @NotNull
    @Size(max = 50)
    @Column(name = "nama_perusahaan", nullable = false)
    private String namaPerusahaan;

    @NotNull
    @Column(name = "deskripsi_perusahaan", nullable = false)
    private String deskripsiPerusahaan;

    // foto perusahaan

    @NotNull
    @Column(name = "visi_perusahaan", nullable = false)
    private String visiPerusahaan;

    @NotNull
    @Column(name = "misi_perusahaan", nullable = false)
    private String misiPerusahaan;

    // foto sampah dan proses pengolahan

    @NotNull
    @Column(name = "email_perusahaan", nullable = false)
    private String emailPerusahaan;

    @NotNull
    @Column(name = "hp_perusahaan", nullable = false)
    private Integer hpPerusahaan;

    @NotNull
    @Column(name = "alamat_perusahaan", nullable = false)
    private String alamatPerusahaan;

    // Relasi dengan TestimoniModel
    @OneToMany(mappedBy = "companyProfile", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TestimoniModel> listTestimoni;
}
