package propensi.project.water.repository.Warehouse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import propensi.project.water.model.Warehouse.JenisItemModel;

@Repository
public interface JenisItemDb extends JpaRepository<JenisItemModel, Long> {

}
