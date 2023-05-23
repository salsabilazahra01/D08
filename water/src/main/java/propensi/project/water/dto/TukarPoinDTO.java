package propensi.project.water.dto;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import propensi.project.water.model.PoinReward.RewardModel;


@Slf4j
@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TukarPoinDTO {
    @Builder.Default
    String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    @Builder.Default
    String jenis = "Uang";
    RewardModel reward;
    
    String bank;
    String noRek;
    String namaRek;
}
