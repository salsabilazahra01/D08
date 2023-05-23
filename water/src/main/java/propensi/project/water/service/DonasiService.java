package propensi.project.water.service;

import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import propensi.project.water.model.Donasi.DonasiModel;
import propensi.project.water.model.User.DonaturModel;
import propensi.project.water.model.User.UserModel;

import java.util.List;

public interface DonasiService {
    public void addDonasi(DonasiModel donasi);

    public void setDefaultDonatur(DonasiModel donasi, UserModel user);

    public List<DonasiModel> findAll();

    public List<DonasiModel> findAll(UserModel user);

    Page<DonasiModel> retrievePage(Integer fragmentStatus, Pageable paging);

    Page<DonasiModel> retrievePage(DonaturModel donatur, Integer fragmentStatus, Pageable paging);

    public DonasiModel findByIdDonasi(String idDonasi);

    public void deleteDonasi(DonasiModel donasi);

    public void updateDonasi(DonasiModel updatedDonasi);

    public void updateStatus(DonasiModel updatedDonasi);

    public void updateStatusDone(DonasiModel updatedDonasi);

    public void deleteAllItemDonasi(DonasiModel donasi);
}
