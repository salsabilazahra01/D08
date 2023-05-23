package propensi.project.water.model.PoinReward;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import propensi.project.water.model.PenjualanHasilOlahan.PenawaranOlahanModel;
import propensi.project.water.model.Warehouse.WarehouseModel;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class RewardTukarPoinIdModel implements Serializable {

    // relasi dengan reward
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "reward", referencedColumnName = "id_reward", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private RewardModel idReward;

    // relasi dengan tukar poin model
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "tukar_poin", referencedColumnName = "id_tukar_poin", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TukarPoinModel idTukarPoin;

}
