package propensi.project.water.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import propensi.project.water.model.PenjualanHasilOlahan.ItemPenawaranOlahanModel;
import propensi.project.water.model.PenjualanHasilOlahan.PenawaranOlahanModel;
import propensi.project.water.model.User.CustomerModel;
import propensi.project.water.repository.PenawaranOlahan.ItemPenawaranOlahanDb;
import propensi.project.water.repository.PenawaranOlahan.PenawaranOlahanDb;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PenawaranOlahanServiceImpl implements PenawaranOlahanService {
    @Autowired
    private PenawaranOlahanDb penawaranOlahanDb;

    @Autowired
    private ItemPenawaranOlahanDb itemPenawaranOlahanDb;

    @Override
    public Page<PenawaranOlahanModel> retrievePage(Pageable paging, Integer status, CustomerModel customer){

        if(customer == null){
            if(status == -1){
                return penawaranOlahanDb.findAllByIdPenawaranOlahanIsNotNullOrderByTanggalDibuat(paging);
            }
            return penawaranOlahanDb.findAllByStatusOrderByTanggalDibuat(status, paging);
        } else {
            if(status == -1){
                return penawaranOlahanDb.findAllByCustomerAndIdPenawaranOlahanIsNotNullOrderByTanggalDibuat(customer,paging);
            }
            return penawaranOlahanDb.findAllByCustomerAndStatusOrderByTanggalDibuat(customer,status, paging);
        }

    }

    @Override
    public PenawaranOlahanModel add(PenawaranOlahanModel penawaranOlahan, CustomerModel customer) {
        if(customer != null){
            penawaranOlahan.setCustomer(customer);
            penawaranOlahan.setTanggalDibuat(LocalDateTime.now());
            penawaranOlahan.setStatus(0);
        } else {
            penawaranOlahan.setStatus(4);
        }
        for(ItemPenawaranOlahanModel item : penawaranOlahan.getListItemPenawaranOlahan()){
            item.setIdPenawaranOlahan(penawaranOlahan);
        }
        return penawaranOlahanDb.save(penawaranOlahan);
    }

    @Override
    public  PenawaranOlahanModel getPenawaranOlahanById(String id){
        Optional<PenawaranOlahanModel> penawaranOlahan = penawaranOlahanDb.findById(id);
        return penawaranOlahan.orElse(null);
    }

    @Override
    public PenawaranOlahanModel update(PenawaranOlahanModel updatedPenawaran, Boolean isManual){

        PenawaranOlahanModel penawaranOlahan = getPenawaranOlahanById(updatedPenawaran.getIdPenawaranOlahan());

        //informasi customer
        penawaranOlahan.setNamaCustomer(updatedPenawaran.getNamaCustomer());
        penawaranOlahan.setNamaPic(updatedPenawaran.getNamaPic());
        penawaranOlahan.setEmail(updatedPenawaran.getEmail());
        penawaranOlahan.setHp(updatedPenawaran.getHp());

        //rekening & harga
        penawaranOlahan.setBank(updatedPenawaran.getBank());
        penawaranOlahan.setNoRekening(updatedPenawaran.getNoRekening());
        penawaranOlahan.setNamaRekening(updatedPenawaran.getNamaRekening());

        //pengiriman
        penawaranOlahan.setIsPickedUp(updatedPenawaran.getIsPickedUp());
        penawaranOlahan.setAlamatPic(updatedPenawaran.getAlamatPic());

        penawaranOlahan.setTanggalDibuat(updatedPenawaran.getTanggalDibuat());

        //update list
        for(ItemPenawaranOlahanModel item : updatedPenawaran.getListItemPenawaranOlahan()){
            item.setIdPenawaranOlahan(penawaranOlahan);
        }
        penawaranOlahan.setListItemPenawaranOlahan(updatedPenawaran.getListItemPenawaranOlahan());

        return penawaranOlahanDb.save(penawaranOlahan);
    }

    @Override
    public PenawaranOlahanModel updateStatus(PenawaranOlahanModel updatedPenawaran) {
        return penawaranOlahanDb.save(updatedPenawaran);
    }

    @Override
    public void delete(PenawaranOlahanModel updatedPenawaran){
        PenawaranOlahanModel penawaranOlahan = getPenawaranOlahanById(updatedPenawaran.getIdPenawaranOlahan());
        penawaranOlahan.setStatus(5);
        penawaranOlahan.setKeteranganTolak("Penawaran dibatalkan oleh Customer");
        penawaranOlahanDb.save(penawaranOlahan);
    }

    @Override
    @Transactional
    public void deleteAllItem(PenawaranOlahanModel penawaranOlahanModel){
        itemPenawaranOlahanDb.deleteAllByIdPenawaranOlahan(penawaranOlahanModel);
    }

    @Override
    public PenawaranOlahanModel setCustomerInfo(CustomerModel customer,
                                                PenawaranOlahanModel penawaranOlahan){

        penawaranOlahan.setCustomer(customer);
        penawaranOlahan.setNamaCustomer(customer.getNama());
        penawaranOlahan.setNamaPic(customer.getNamaPic());
        if(customer.getEmail() != null){
            penawaranOlahan.setEmail(customer.getEmail());
        }
        if(customer.getHp() != null){
            penawaranOlahan.setHp(customer.getHp());
        }
        penawaranOlahan.setAlamatPic(customer.getAlamat());
        penawaranOlahan.setBank(customer.getBank());
        penawaranOlahan.setNoRekening(customer.getNoRekening());
        penawaranOlahan.setNamaRekening(customer.getNamaRekening());

        return penawaranOlahan;
    }

    public List<PenawaranOlahanModel> findAll(){
        return penawaranOlahanDb.findAll();
    }

}
