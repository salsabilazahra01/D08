package propensi.project.water.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import propensi.project.water.model.Donasi.DonasiModel;
import propensi.project.water.model.Donasi.ItemDonasiModel;
import propensi.project.water.model.Transaksi.TransaksiModel;
import propensi.project.water.model.User.DonaturModel;
import propensi.project.water.model.User.UserModel;
import propensi.project.water.model.Warehouse.WarehouseModel;
import propensi.project.water.repository.Donasi.DonasiDb;
import propensi.project.water.repository.Donasi.ItemDonasiDb;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DonasiServiceImpl implements DonasiService {

    @Autowired
    private DonasiDb donasiDb;

    @Autowired
    private ItemDonasiDb itemDonasiDb;

    @Qualifier("warehouseServiceImpl")
    @Autowired
    private WarehouseService warehouseService;

    @Override
    public void addDonasi(DonasiModel donasi) {
        donasiDb.save(donasi);
    }

    @Override
    public void setDefaultDonatur(DonasiModel donasi, UserModel user) {
        donasi.setDonatur((DonaturModel) user);
        donasi.setNamaPic(user.getNama());
        donasi.setKontakPic(user.getHp());
        donasi.setAlamatPic(((DonaturModel) user).getAlamat());
    }

    @Override
    public List<DonasiModel> findAll() {
        return donasiDb.findAll();
    }

    @Override
    public List<DonasiModel> findAll(UserModel user) {
        List<DonasiModel> listDonasiUser = donasiDb.findAll();
        for(DonasiModel donasi:listDonasiUser) {
            if (donasi.getDonatur().getUsername() != user.getUsername()) {
                listDonasiUser.remove(donasi);
            }
        }
        return listDonasiUser;
    }

    @Override
    public Page<DonasiModel> retrievePage(Integer fragmentStatus, Pageable paging){
        if (fragmentStatus == 88){
            return donasiDb.findAllByIdDonasiIsNotNullOrderByTanggalDibuat(paging);
        }
        return donasiDb.findAllByStatusOrderByTanggalDibuat(fragmentStatus, paging);
    }

    @Override
    public Page<DonasiModel> retrievePage(DonaturModel donatur, Integer fragmentStatus, Pageable paging){
        if (fragmentStatus == 88){
            return donasiDb.findAllByDonaturOrderByTanggalDibuat(donatur, paging);
        }
        return donasiDb.findAllByDonaturAndStatusOrderByTanggalDibuat(donatur, fragmentStatus, paging);
    }

    @Override
    public DonasiModel findByIdDonasi(String idDonasi) { return donasiDb.findByIdDonasi(idDonasi).orElse(null);}

    @Override
    public void deleteDonasi(DonasiModel donasi) {
        donasi.setKeterangan("Dibatalkan oleh donatur");
        donasi.setStatus(-1);
        updateStatus(donasi);
    }

    @Override
    public void updateDonasi(DonasiModel updatedDonasi) {
        DonasiModel donasi = findByIdDonasi(updatedDonasi.getIdDonasi());

        donasi.setNamaPic(updatedDonasi.getNamaPic());
        donasi.setAlamatPic(updatedDonasi.getAlamatPic());
        donasi.setKontakPic(updatedDonasi.getKontakPic());
        donasi.setIsPickedUp(updatedDonasi.getIsPickedUp());
        donasi.setBeratSebelum(updatedDonasi.getBeratSebelum());
        donasi.setBeratSetelah(updatedDonasi.getBeratSetelah());
        donasi.setKeterangan(updatedDonasi.getKeterangan());

        for (ItemDonasiModel item: updatedDonasi.getListItemDonasi()) {
            item.setIdDonasi(donasi);
        }

        donasi.setListItemDonasi(updatedDonasi.getListItemDonasi());

        donasiDb.save(donasi);
    }

    @Override
    public void updateStatus(DonasiModel updatedDonasi) {
        DonasiModel donasi = findByIdDonasi(updatedDonasi.getIdDonasi());
        donasi.setStatus(updatedDonasi.getStatus());
        donasi.setKeterangan(updatedDonasi.getKeterangan());
        donasi.setListItemDonasi(updatedDonasi.getListItemDonasi());
        donasiDb.save(donasi);
    }

    @Override
    public void updateStatusDone(DonasiModel updatedDonasi) {
        DonasiModel donasi = findByIdDonasi(updatedDonasi.getIdDonasi());
        DonaturModel donatur = donasi.getDonatur();

        // status
        donasi.setStatus(updatedDonasi.getStatus());

        // list item donasi
        for (ItemDonasiModel item: updatedDonasi.getListItemDonasi()) {
//            if (item.getKuantitas() == 0) {
//                updatedDonasi.getListItemDonasi().remove(item);
//            }
            item.setIdDonasi(donasi);
            item.setKuantitas(item.getKuantitas());
        }
        donasi.setListItemDonasi(updatedDonasi.getListItemDonasi());

        // poin & berat setelah
        donasi.setPoinEarned(calculatePoint(donasi));
        donasi.setBeratSetelah(accumulateWeight(donasi));

        // update poin user
        donatur.setPoin(donatur.getPoin()+donasi.getPoinEarned());

        // update kuantitas warehouse
        for (ItemDonasiModel item:donasi.getListItemDonasi()) {
            item.getIdItem().setKuantitasSampah(item.getIdItem().getKuantitasSampah() + item.getKuantitas());
        }

        donasiDb.save(donasi);
    }

    @Override
    @Transactional
    public void deleteAllItemDonasi(DonasiModel donasi) {
        itemDonasiDb.deleteAllByIdDonasi(donasi);
    }

    private int calculatePoint(DonasiModel donasi) {
        int total = 0;
        if (!donasi.getListItemDonasi().isEmpty() || donasi.getListItemDonasi() == null) {
            for (ItemDonasiModel item : donasi.getListItemDonasi()) {
                total += item.getKuantitas() * item.getIdItem().getHargaBeli();
            }
        }
        total = total/100;
        return total;
    }

    private int accumulateWeight(DonasiModel donasi) {
        int total = 0;
        if (!donasi.getListItemDonasi().isEmpty() || donasi.getListItemDonasi() == null) {
            for (ItemDonasiModel item : donasi.getListItemDonasi()) {
                total += item.getKuantitas();
            }
        }
        return total;
    }
}
