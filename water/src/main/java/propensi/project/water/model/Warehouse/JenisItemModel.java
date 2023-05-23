package propensi.project.water.model.Warehouse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import propensi.project.water.model.PenjualanHasilOlahan.PenawaranOlahanModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "jenis_item")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JenisItemModel implements Serializable {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy="uuid")
    @Column(name = "id_jenis_item", nullable = false)
    private Long idJenis;

    @NotNull
    @Size(max=50)
    @Column(name = "nama_jenis_item", nullable = false)
    private String namaJenis;

    // relasi dengan warehouse
    @OneToMany(mappedBy = "jenisItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<WarehouseModel> listNamaItem;
}
