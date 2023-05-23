package propensi.project.water.model.Transaksi;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "proses_lain")
@PrimaryKeyJoinColumn(name="id_transaksi")
@Setter
@Getter
@NoArgsConstructor
public class ProsesLainModel extends TransaksiModel{

    @NotNull
    @Column(name= "person", nullable=false)
    private String person;

    @NotNull
    @Column(name= "nama_proses", nullable=false)
    private String namaProses;
}
