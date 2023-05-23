package propensi.project.water.service;

import propensi.project.water.model.Warehouse.BatchModel;

import java.util.List;
import java.util.Optional;

public interface BatchService {
    List<BatchModel> getAll();
    Optional<BatchModel> getById(String id);
    void add(BatchModel model);
    void update(BatchModel model);
    void deleteById(String id);
    void create(String warehouseId, Integer kuantitasBahanBaku);
}
