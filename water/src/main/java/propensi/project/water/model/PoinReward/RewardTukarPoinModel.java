package propensi.project.water.model.PoinReward;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import propensi.project.water.model.PenjualanHasilOlahan.ItemPenawaranOlahanIdModel;
import propensi.project.water.model.PenjualanHasilOlahan.PenawaranOlahanModel;
import propensi.project.water.model.Warehouse.WarehouseModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reward_tukar_poin")
@IdClass(RewardTukarPoinIdModel.class)
public class RewardTukarPoinModel {

    // relasi dengan reward
    @Id
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "reward", referencedColumnName = "id_reward", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private RewardModel idReward;

    // relasi dengan tukar poin
    @Id
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "tukar_poin", referencedColumnName = "id_tukar_poin", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TukarPoinModel idTukarPoin;

    @NotNull
    @Column(name = "kuantitas", nullable = false)
    private Integer kuantitas;
}
