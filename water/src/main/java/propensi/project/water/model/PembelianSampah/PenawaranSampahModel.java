package propensi.project.water.model.PembelianSampah;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;
import propensi.project.water.model.StringPrefixedSequenceIdGenerator;
import propensi.project.water.model.Transaksi.ProsesPenawaranSampahModel;
import propensi.project.water.model.User.PartnerModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "penawaran_sampah")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PenawaranSampahModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "penawaran_sampah_seq")
    @GenericGenerator(name = "penawaran_sampah_seq",
            strategy = "propensi.project.water.model.StringPrefixedSequenceIdGenerator",
            parameters = {
                    @Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "PWS-")
            }
    )
    @Column(name = "id_penawaran_sampah", nullable = false)
    private String idPenawaranSampah;

    @NotNull
    @Size(max = 50)
    @Column(name = "nama_pic", nullable = false)
    private String namaPic;

    @NotNull
    @Column(name = "kontak_pic", nullable = false)
    private String kontakPic;

    @NotNull
    @Column(name = "bank", nullable = false)
    private String bank;

    @NotNull
    @Column(name = "noRekening", nullable = false)
    private Integer noRekening;

    @NotNull
    @Column(name = "alamat_pic", nullable = false)
    private String alamatPic;

    @NotNull
    @Column(name = "is_picked_up", nullable = false)
    private Boolean isPickedUp;

    @NotNull
    @Column(name = "berat", nullable = false)
    private Integer berat;

    @NotNull
    @Column(name = "tanggal_dibuat", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime tanggalDibuat;

    @NotNull
    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "keterangan", nullable = false)
    private String keterangan;

    @NotNull
    @Column(name = "harga", nullable = false)
    private Integer harga;


    // relasi dengan partner
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username_partner", referencedColumnName = "username")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PartnerModel partner;

    // relasi dengan transaksi
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_transaksi", referencedColumnName = "id_transaksi")
    private ProsesPenawaranSampahModel transaksiSampah;

    // relasi dengan item penawaran sampah
    @OneToMany(mappedBy = "idPenawaranSampah", fetch = FetchType.LAZY,  cascade = CascadeType.ALL, orphanRemoval=true)
    private List<ItemPenawaranSampahModel> listItemPenawaranSampah;
}
