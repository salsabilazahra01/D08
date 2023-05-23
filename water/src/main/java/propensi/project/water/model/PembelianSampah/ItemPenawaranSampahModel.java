package propensi.project.water.model.PembelianSampah;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import propensi.project.water.model.Warehouse.WarehouseModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "item_penawaran_sampah")
@IdClass(ItemPenawaranSampahIdModel.class)
public class ItemPenawaranSampahModel {

    // relasi dengan item
    @Id
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "item", referencedColumnName = "id_item", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private WarehouseModel idItem;

    // relasi dengan penawaran sampah
    @Id
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "penawaran_sampah", referencedColumnName = "id_penawaran_sampah", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PenawaranSampahModel idPenawaranSampah;

    @Column(name = "kuantitas", columnDefinition = "int default 0")
    private Integer kuantitas;
}
