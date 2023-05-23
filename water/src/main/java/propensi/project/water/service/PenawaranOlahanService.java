package propensi.project.water.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import propensi.project.water.model.PenjualanHasilOlahan.PenawaranOlahanModel;
import propensi.project.water.model.User.CustomerModel;

import java.util.List;

public interface PenawaranOlahanService {
    Page<PenawaranOlahanModel> retrievePage(Pageable paging, Integer status, CustomerModel customer);
    PenawaranOlahanModel add(PenawaranOlahanModel penawaranOlahan, CustomerModel customer);
    PenawaranOlahanModel getPenawaranOlahanById(String id);
    PenawaranOlahanModel update(PenawaranOlahanModel updatedPenawaran, Boolean isManual);
    PenawaranOlahanModel updateStatus(PenawaranOlahanModel updatedPenawaran);
    void delete(PenawaranOlahanModel penawaranOlahan);
    void deleteAllItem(PenawaranOlahanModel penawaranOlahan);
    PenawaranOlahanModel setCustomerInfo(CustomerModel customer, PenawaranOlahanModel penawaranOlahan);
    List<PenawaranOlahanModel> findAll();

}
