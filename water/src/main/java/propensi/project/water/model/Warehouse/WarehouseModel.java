package propensi.project.water.model.Warehouse;

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import propensi.project.water.model.Donasi.ItemDonasiModel;
import propensi.project.water.model.PembelianSampah.ItemPenawaranSampahModel;
import propensi.project.water.model.PenjualanHasilOlahan.ItemPenawaranOlahanModel;
import propensi.project.water.model.StringPrefixedSequenceIdGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "warehouse")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq")
    @GenericGenerator(name = "item_seq",
            strategy = "propensi.project.water.model.StringPrefixedSequenceIdGenerator",
            parameters = {
                    @Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "ITM-")
            }
    )
    @Column(name = "id_item", nullable = false)
    private String idItem;

    @NotNull
    @Size(max=50)
    @Column(name = "nama", nullable = false)
    private String namaItem;

    @NotNull
    @Column(name = "kuantitas_sampah", nullable = false, columnDefinition = "int default 0")
    private Integer kuantitasSampah;

    @NotNull
    @Column(name = "kuantitas_olahan", nullable = false, columnDefinition = "int default 0" )
    private Integer kuantitasOlahan;

    @NotNull
    @Column(name = "harga_beli", nullable = false)
    private Integer hargaBeli;

    // relasi dengan jenis item
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_jenis", referencedColumnName = "id_jenis_item", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private JenisItemModel jenisItem;

    // relasi dengan batch
    @OneToMany(mappedBy = "warehouse", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BatchModel> listBatch;

    //relasi dengan item donasi
    @OneToMany(mappedBy = "idDonasi", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ItemDonasiModel> listItemDonasi;

    //relasi dengan item penawaran sampah
    @OneToMany(mappedBy = "idPenawaranSampah", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ItemPenawaranSampahModel> listItemPenawaranSampah;

    //relasi dengan item penawaran olahan
    @OneToMany(mappedBy = "idPenawaranOlahan", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ItemPenawaranOlahanModel> listItemPenawaranOlahan;
}
