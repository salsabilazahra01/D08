package propensi.project.water.repository.Warehouse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import propensi.project.water.model.Warehouse.WarehouseModel;

import java.util.Optional;

@Repository
public interface WarehouseDb extends JpaRepository<WarehouseModel, String> {
    Optional<WarehouseModel> findByIdItem(String idItem);
    Optional<WarehouseModel> findByNamaItem(String namaItem);

}
