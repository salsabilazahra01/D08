package propensi.project.water.repository.Warehouse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import propensi.project.water.model.Warehouse.BatchModel;

@Repository
public interface BatchDb extends JpaRepository<BatchModel, String> {
    BatchModel findByIdBatch(String idBatch);
}