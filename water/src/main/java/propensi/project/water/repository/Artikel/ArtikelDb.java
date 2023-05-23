package propensi.project.water.repository.Artikel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import propensi.project.water.model.artikel.ArtikelModel;

@Repository
public interface ArtikelDb extends JpaRepository<ArtikelModel, String> {

}
