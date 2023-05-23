package propensi.project.water.model.PenjualanHasilOlahan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import propensi.project.water.model.PembelianSampah.PenawaranSampahModel;
import propensi.project.water.model.Warehouse.WarehouseModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "item_penawaran_olahan")
@IdClass(ItemPenawaranOlahanIdModel.class)
public class ItemPenawaranOlahanModel {

    // relasi dengan item
    @Id
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "item", referencedColumnName = "id_item", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private WarehouseModel idItem;

    // relasi dengan penawaran olahan
    @Id
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "penawaran_olahan", referencedColumnName = "id_penawaran_olahan", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PenawaranOlahanModel idPenawaranOlahan;

    @NotNull
    @Column(name = "kuantitas", nullable = false)
    private Integer kuantitas;

    @Column(name = "harga", columnDefinition = "int default 0")
    private Integer harga;
}
