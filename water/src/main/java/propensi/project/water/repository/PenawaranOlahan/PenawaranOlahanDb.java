package propensi.project.water.repository.PenawaranOlahan;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import propensi.project.water.model.PenjualanHasilOlahan.PenawaranOlahanModel;
import propensi.project.water.model.User.CustomerModel;

@Repository
public interface PenawaranOlahanDb  extends JpaRepository<PenawaranOlahanModel, String> {
    Page<PenawaranOlahanModel> findAllByIdPenawaranOlahanIsNotNullOrderByTanggalDibuat(Pageable pageable);
    Page<PenawaranOlahanModel> findAllByStatusOrderByTanggalDibuat(Integer status, Pageable pageable);
    Page<PenawaranOlahanModel> findAllByCustomerAndIdPenawaranOlahanIsNotNullOrderByTanggalDibuat(CustomerModel customer, Pageable pageable);
    Page<PenawaranOlahanModel> findAllByCustomerAndStatusOrderByTanggalDibuat(CustomerModel customer, Integer status, Pageable pageable);


}
