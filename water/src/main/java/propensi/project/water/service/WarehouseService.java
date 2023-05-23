package propensi.project.water.service;

import propensi.project.water.model.Warehouse.JenisItemModel;
import propensi.project.water.model.Warehouse.WarehouseModel;

import java.util.List;
public interface WarehouseService {
    List<JenisItemModel> getListJenisItem();
    List<WarehouseModel> getListItem();
    WarehouseModel updateItem(WarehouseModel item);
    void addItem(WarehouseModel item);
    void deleteItem(WarehouseModel item);
    WarehouseModel getItemById(String idItem);
    WarehouseModel getItemByNamaItem(String namaItem);

    List<WarehouseModel> getListItemWarehouse();
    WarehouseModel updateKuantitas(WarehouseModel item);
}
