package propensi.project.water.model.PoinReward;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "tukar_poin_billing")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TukarPoinBillingModel implements Serializable {
    // Id Auto Increment Long
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String namaRekening;

    @NotNull
    @Column(nullable = false)
    private String nomorRekening;

    @NotNull
    @Column(nullable = false)
    private String namaBank;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tukar_poin", referencedColumnName = "id_tukar_poin", nullable = false)
    private TukarPoinModel tukarPoin;
}
