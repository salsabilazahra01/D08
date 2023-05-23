package propensi.project.water.model.Donasi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import propensi.project.water.model.Warehouse.WarehouseModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "item_donasi")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@IdClass(ItemDonasiIdModel.class)
public class ItemDonasiModel {

    // relasi dengan item
    @Id
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "item", referencedColumnName = "id_item", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private WarehouseModel idItem;

    // relasi dengan donasi
    @Id
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "donasi", referencedColumnName = "id_donasi", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private DonasiModel idDonasi;

    @NotNull
    @Column(name = "kuantitas", nullable = false, columnDefinition = "int default 0")
    private Integer kuantitas;
}
