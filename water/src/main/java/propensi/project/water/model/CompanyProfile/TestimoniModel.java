package propensi.project.water.model.CompanyProfile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import propensi.project.water.model.StringPrefixedSequenceIdGenerator;
import propensi.project.water.model.User.UserModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "testimoni")
public class TestimoniModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    @GenericGenerator(name = "testimoni_seq",
//            strategy = "propensi.project.water.model.StringPrefixedSequenceIdGenerator",
//            parameters = {
//                    @org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
//                    @org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "TST-")
//            }
//    )
    @Column(name = "id_testimoni", nullable = false)
    private Integer idTestimoni;

    @NotNull
    @Column(name = "review", nullable = false)
    private String review;

    @NotNull
    @Column(name = "role", nullable = false)
    private String role;

    @NotNull
    @Column(name = "nama_pembuat_testimoni", nullable = false)
    private String namaPembuatTestimoni;

    // Relasi dengan CompanyProfileModel
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_company_profile", referencedColumnName = "id_company_profile", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CompanyProfileModel companyProfile;
}
