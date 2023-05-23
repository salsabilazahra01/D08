package propensi.project.water.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import propensi.project.water.model.Warehouse.BatchModel;
import propensi.project.water.repository.Warehouse.BatchDb;

@Service
public class BatchServiceImpl implements BatchService {

    @Autowired
    private BatchDb repo;

    @Override
    public List<BatchModel> getAll() {
        return repo.findAll();
    }

    @Override
    public void add(BatchModel model) {
        repo.save(model);
    }

    @Override
    public Optional<BatchModel> getById(String id) {
        return repo.findById(id);
    }

    @Override
    public void deleteById(String id) {
        repo.deleteById(id);
    }

    @Override
    public void update(BatchModel model) {
        repo.save(model);
    }

    @Override
    public void create(String warehouseId, Integer kuantitasBahanBaku) {
        // BatchModel batch = new BatchModel();
        // batch.setWarehouseId(warehouseId);
        // batch.setKuantitasBahanBaku(kuantitasBahanBaku);

        // // Save the batch to the database using a repository
        // this.save(batch);
    }
}
