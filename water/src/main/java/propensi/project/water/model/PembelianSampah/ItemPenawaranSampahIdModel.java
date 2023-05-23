package propensi.project.water.model.PembelianSampah;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import propensi.project.water.model.Warehouse.WarehouseModel;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ItemPenawaranSampahIdModel implements Serializable {

    // relasi dengan item
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "item", referencedColumnName = "id_item", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private WarehouseModel idItem;

    // relasi dengan penawaran sampah
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "penawaran_sampah", referencedColumnName = "id_penawaran_sampah", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PenawaranSampahModel idPenawaranSampah;
}
