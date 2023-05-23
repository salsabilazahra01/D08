package propensi.project.water.repository.Donasi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import propensi.project.water.model.Donasi.DonasiModel;
import propensi.project.water.model.Donasi.ItemDonasiModel;
import propensi.project.water.model.Warehouse.WarehouseModel;

@Repository
public interface ItemDonasiDb extends JpaRepository<ItemDonasiModel, String> {
    void deleteAllByIdDonasi(DonasiModel donasi);
}
