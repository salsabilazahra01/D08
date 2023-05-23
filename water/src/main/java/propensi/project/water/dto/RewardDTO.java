package propensi.project.water.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import propensi.project.water.model.PoinReward.RewardModel;


@Slf4j
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RewardDTO {
    @Builder.Default
    String jenis = "Uang";   // Jenis Reward
    @Builder.Default
    String nama = "";
    int poin;       // Ammount of points needed
    int jumlah;     // Qty Barang or ammount of money
    String satuan;  // Satuan Barang or Rp.
    Integer harga;

    // JENIS|NAMA|JUMLAH|SATUAN
    private static final String FORMAT_JENIS = "%s|%s|%s|%d";

    public static RewardDTO fromModel(RewardModel model) {
        String[] jenis = model.getJenisReward().split("\\|");
        log.error(jenis.toString());
        return RewardDTO.builder()
                .jenis(jenis[0])
                .nama(jenis[1])
                .satuan(jenis[2])
                .jumlah(Integer.parseInt(jenis[3]))
                .poin(model.getPoin())
                .harga(model.getHarga())
                .build();
    }

    private static String formatJenis(RewardDTO dto) {
        return formatJenis(dto.getJenis(), dto.getNama(), dto.getSatuan(), dto.getJumlah());
    }

    private static String formatJenis(String jenis, String nama, String satuan, int jumlah) {
        return String.format(FORMAT_JENIS, jenis, nama, satuan, jumlah);
    }

    public RewardModel toModel() {
        return RewardModel.builder()
                .jenisReward(formatJenis(this))
                .poin(this.getPoin())
                .harga(this.getHarga())
                .build();
    }

    public RewardModel toModel(RewardModel model) {
        model.setJenisReward(formatJenis(this));
        model.setPoin(this.getPoin());
        model.setHarga(this.getHarga());
        return model;
    }

    public static RewardModel toModel(RewardDTO dto) {
        return RewardModel.builder()
                .jenisReward(formatJenis(dto))
                .poin(dto.getPoin())
                .harga(dto.getHarga())
                .build();
    }

    public static RewardModel toModel(RewardDTO dto, RewardModel model) {
        model.setJenisReward(formatJenis(dto));
        model.setPoin(dto.getPoin());
        model.setHarga(dto.getHarga());
        return model;
    }
}
