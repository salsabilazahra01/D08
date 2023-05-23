package propensi.project.water.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "supervisor")
@PrimaryKeyJoinColumn(name="username")
@Setter
@Getter
@NoArgsConstructor
public class SupervisorModel extends UserModel {
}
