package propensi.project.water.dto;


import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileDTO {
    String username;
    String fname;
    String email;
    String namaPic;

    String alamat;
    String kelurahan;
    String kecamatan;
    String kota;
    String provinsi;
    String kodePos;

}
