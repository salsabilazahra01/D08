package propensi.project.water.model.PoinReward;

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.val;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.GenericGenerator;

import propensi.project.water.dto.RewardDTO;
import propensi.project.water.model.PembelianSampah.ItemPenawaranSampahModel;
import propensi.project.water.model.StringPrefixedSequenceIdGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "reward")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RewardModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reward_seq")
    @GenericGenerator(name = "reward_seq",
            strategy = "propensi.project.water.model.StringPrefixedSequenceIdGenerator",
            parameters = {
                    @Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "RWD-")
            }
    )
    @Column(name = "id_reward", nullable = false)
    private String idReward;

    @NotNull
    @Column(name = "jenis", nullable = false, unique = true)
    private String jenisReward; // id reward

    @NotNull
    @Column(name = "poin", nullable = false)
    private Integer poin;

    @NotNull
    @Column(name = "harga", nullable = false)
    private Integer harga;


    public String jenisReward() {
        val dto = RewardDTO.fromModel(this);
        if (dto.getJenis().equals("Uang"))
            return dto.getJenis() + " Rp. " + dto.getJumlah();
        return dto.getJenis() + " " + dto.getNama() + " " + dto.getJumlah() + " " + dto.getSatuan();
    }

    public String jenis() {
        val dto = RewardDTO.fromModel(this);
        return dto.getJenis();
    }

    //relasi dengan item reward
    @OneToMany(mappedBy = "idTukarPoin", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RewardTukarPoinModel> listTukarPoin;
}
