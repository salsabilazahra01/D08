package propensi.project.water.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import propensi.project.water.model.PembelianSampah.PenawaranSampahModel;
import propensi.project.water.model.Transaksi.ProsesPenawaranSampahModel;
import propensi.project.water.model.User.PartnerModel;
import propensi.project.water.model.User.UserModel;

import java.util.List;
public interface PenjualanSampahService {
    public void addPenawaranSampah(PenawaranSampahModel penawaranSampah);

    public void setDefaultPartner(PenawaranSampahModel penawaranSampah, UserModel user);
    public void setDefaultManual(PenawaranSampahModel penawaranSampah, UserModel user);

    public List<PenawaranSampahModel> findAll();

    public List<PenawaranSampahModel> findAll(UserModel user);

    Page<PenawaranSampahModel> retrievePage(Integer fragmentStatus, Pageable paging);

    Page<PenawaranSampahModel> retrievePage(PartnerModel partner, Integer fragmentStatus, Pageable paging);

    public PenawaranSampahModel findByIdPenawaranSampah(String idPenawaranSampah);

    public void deletePenawaranSampah(PenawaranSampahModel penawaranSampah);
    public void updatePenawaranSampah(PenawaranSampahModel penawaranSampah);
    public void saveStatus(PenawaranSampahModel penawaranSampah);
    public void saveStatusDiTolak(PenawaranSampahModel penawaranSampah);
    public void simpanInspeksiPenawaranSampah(PenawaranSampahModel penawaranSampah);
    public void addTransaksiSampah(PenawaranSampahModel penawaranSampah, Boolean isManual, String bukti);
    public ProsesPenawaranSampahModel getTransaksiByPenawaranSampah(PenawaranSampahModel penawaranSampah);
}
