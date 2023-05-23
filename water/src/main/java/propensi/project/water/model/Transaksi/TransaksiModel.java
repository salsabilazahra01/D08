package propensi.project.water.model.Transaksi;

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import propensi.project.water.model.StringPrefixedSequenceIdGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaksi")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class TransaksiModel implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaksi_seq")
    @GenericGenerator(name = "transaksi_seq",
            strategy = "propensi.project.water.model.StringPrefixedSequenceIdGenerator",
            parameters = {
                    @Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "TRN-")
            }
    )
    @Column(name = "id_transaksi", nullable = false)
    private String idTransaksi;

    @NotNull
    @Column(name = "jenis_transaksi", nullable = false)
    private Boolean jenisTransaksi; // pendapatan atau pengeluaran

    @NotNull
    @Column(name = "nominal", nullable = false)
    private Integer nominal;

    @NotNull
    @Column(name = "tanggal_transaksi", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime tanggalTransaksi;

    @Column(name = "keterangan")
    private String keterangan;

    @NotNull
    @Column(name = "proses", nullable = false)
    private Integer proses;

    @NotNull
    @Column(name= "tanggal_dibuat", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime tanggalDibuat;

    @NotNull
    @Column(name = "bukti", nullable = false)
    private String bukti;

    @Transient
    public String getBuktiImagePath() {
        if (bukti == null || idTransaksi == null) return null;
        return "/images/" + idTransaksi + '-' + bukti + '/' + bukti;
    }

    public String getJenisString(){
        if(jenisTransaksi){
            return "Pengeluaran";
        } else {
            return "Pendapatan";
        }
    }

    public String getProsesString() {
        if(proses == 0){
            return "Pembelian Sampah";
        } else if(proses == 1){
            return "Penjualan Hasil Olahan";
        } else if(proses == 2){
            return "Sumber Lain";
        } else {
            return "Reward Penukaran Poin";
        }
    }
}
