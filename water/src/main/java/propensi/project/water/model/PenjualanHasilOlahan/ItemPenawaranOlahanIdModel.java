package propensi.project.water.model.PenjualanHasilOlahan;

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
public class ItemPenawaranOlahanIdModel implements Serializable {

    // relasi dengan item
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "item", referencedColumnName = "id_item", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private WarehouseModel idItem;

    // relasi dengan penawaran sampah
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "penawaran_olahan", referencedColumnName = "id_penawaran_olahan", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PenawaranOlahanModel idPenawaranOlahan;

}
