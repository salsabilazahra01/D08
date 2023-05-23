package propensi.project.water.service;

import propensi.project.water.model.Warehouse.JenisItemModel;

import java.util.List;
import java.util.Optional;

public interface JenisItemService {
    List<JenisItemModel> getAllJenisItem();
    Optional<JenisItemModel> getById(String id);
    void add(JenisItemModel jenisItem);
    void update(JenisItemModel jenisItem);
    void deleteById(String id);
}
