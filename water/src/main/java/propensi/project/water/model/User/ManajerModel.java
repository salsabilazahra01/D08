package propensi.project.water.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "manajer")
@PrimaryKeyJoinColumn(name="username")
@Setter
@Getter
@NoArgsConstructor
public class ManajerModel extends UserModel {
}
