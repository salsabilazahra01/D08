package propensi.project.water.model.Donasi;

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
public class ItemDonasiIdModel implements Serializable {

    // relasi dengan item
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "item", referencedColumnName = "id_item", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private WarehouseModel idItem;

    // relasi dengan donasi
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "donasi", referencedColumnName = "id_donasi", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private DonasiModel idDonasi;
}
