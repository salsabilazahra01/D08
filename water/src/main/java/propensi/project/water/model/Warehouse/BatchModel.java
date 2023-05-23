package propensi.project.water.model.Warehouse;

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;
import propensi.project.water.model.StringPrefixedSequenceIdGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "batch")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BatchModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "batch_seq")
    @GenericGenerator(name = "batch_seq",
            strategy = "propensi.project.water.model.StringPrefixedSequenceIdGenerator",
            parameters = {
                    @Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "BCH-")
            }
    )
    @Column(name = "id_batch", nullable = false)
    private String idBatch;

    @NotNull
    @Column(name = "kuantitas_bahan_baku", nullable = false)
    private Integer kuantitasBahanBaku;

    @NotNull
    @Column(name = "kuantitas_hasil", nullable = false, columnDefinition = "int default 0")
    private Integer kuantitasHasil;

    @NotNull
    @Column(name = "tanggal_dibuat", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime tanggalDibuat;

    @NotNull
    @Column(name = "status", nullable = false)
    private Integer status;

    // relasi dengan warehouse
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "nama_item", referencedColumnName = "nama", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private WarehouseModel warehouse;
}
