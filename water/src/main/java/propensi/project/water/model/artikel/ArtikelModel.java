package propensi.project.water.model.artikel;

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import propensi.project.water.model.StringPrefixedSequenceIdGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "artikel")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArtikelModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "artikel_seq")
    @GenericGenerator(name = "artikel_seq",
            strategy = "propensi.project.water.model.StringPrefixedSequenceIdGenerator",
            parameters = {
                    @Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "ARTK-")
            }
    )
    @Column(name = "id_artikel", nullable = false)
    private String idArtikel;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "subtitle", nullable = false)
    private String subtitle;

    @NotNull
    @Column(name = "imageTitle", nullable = false)
    private String imageTitle;

    @NotNull
    @Column(columnDefinition="LONGTEXT", name = "content", nullable = false)
    private String content;

}
