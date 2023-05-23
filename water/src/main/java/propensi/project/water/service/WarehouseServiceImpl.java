package propensi.project.water.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import propensi.project.water.model.Warehouse.JenisItemModel;
import propensi.project.water.model.Warehouse.WarehouseModel;
import propensi.project.water.repository.Warehouse.JenisItemDb;
import propensi.project.water.repository.Warehouse.WarehouseDb;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    JenisItemDb jenisItemDb;
    @Autowired
    WarehouseDb warehouseDb;

    @Override
    public List<JenisItemModel> getListJenisItem(){
        return jenisItemDb.findAll();
    }
    @Override
    public List<WarehouseModel> getListItem(){
        return warehouseDb.findAll();
    }

    @Override
    public WarehouseModel updateItem(WarehouseModel item){
        WarehouseModel itemLama = getItemById(item.getIdItem());
        itemLama.setJenisItem(item.getJenisItem());
        itemLama.setNamaItem(item.getNamaItem());
        itemLama.setHargaBeli(item.getHargaBeli());
        itemLama.setKuantitasOlahan(item.getKuantitasOlahan());
        itemLama.setKuantitasSampah(item.getKuantitasSampah());

        return warehouseDb.save(itemLama);
    }
    @Override
    public void addItem(WarehouseModel item){
        warehouseDb.save(item);
    }

    @Override
    public void deleteItem(WarehouseModel item){
        warehouseDb.delete(item);
    }


    @Override
    public WarehouseModel getItemById(String idItem) {
        Optional<WarehouseModel> item = warehouseDb.findByIdItem(idItem);
        return item.orElse(null);
    }

    @Override
    public WarehouseModel getItemByNamaItem(String namaItem) {
        Optional<WarehouseModel> item = warehouseDb.findByNamaItem(namaItem);
        return item.orElse(null);
    }

    @Override
    public List<WarehouseModel> getListItemWarehouse() {
        return warehouseDb.findAll();
    }

    @Override
    public WarehouseModel updateKuantitas(WarehouseModel item){
        WarehouseModel itemLama = getItemById(item.getIdItem());
        itemLama.setKuantitasSampah(item.getKuantitasSampah());
        return warehouseDb.save(itemLama);
    }
}
