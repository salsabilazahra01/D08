package propensi.project.water.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import propensi.project.water.model.PembelianSampah.PenawaranSampahModel;
import propensi.project.water.model.Transaksi.ProsesPenawaranSampahModel;
import propensi.project.water.model.User.PartnerModel;
import propensi.project.water.model.User.UserModel;
import propensi.project.water.repository.PenawaranSampah.ItemPenawaranSampahDb;
import propensi.project.water.repository.PenawaranSampah.PenawaranSampahDb;
import propensi.project.water.repository.TransaksiDb.ProsesPenawaranSampahDb;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PenjualanSampahServiceImpl implements PenjualanSampahService {

    @Autowired
    private PenawaranSampahDb penawaranSampahDb;

    @Autowired
    private ItemPenawaranSampahDb itemPenawaranSampahDb;

    @Autowired
    private ProsesPenawaranSampahDb prosesPenawaranSampahDb;

    @Override
    public void addPenawaranSampah(PenawaranSampahModel penawaranSampah) {
        penawaranSampahDb.save(penawaranSampah);
    }

    @Override
    public void setDefaultPartner(PenawaranSampahModel penawaranSampah, UserModel user) {
        penawaranSampah.setPartner((PartnerModel) user);
        penawaranSampah.setNamaPic(user.getNama());
        penawaranSampah.setKontakPic(user.getHp());
        penawaranSampah.setAlamatPic(((PartnerModel) user).getAlamat());
    }

    @Override
    public void setDefaultManual(PenawaranSampahModel penawaranSampah, UserModel user) {
        penawaranSampah.setPartner((PartnerModel) user);
        penawaranSampah.setNamaPic(user.getNama());
        penawaranSampah.setKontakPic(user.getHp());
        penawaranSampah.setAlamatPic(((PartnerModel) user).getAlamat());
    }

    @Override
    public List<PenawaranSampahModel> findAll() {
        return penawaranSampahDb.findAll();
    }


    @Override
    public List<PenawaranSampahModel> findAll(UserModel user) {
        List<PenawaranSampahModel> listPenawaranSampahUser = penawaranSampahDb.findAll();
        for(PenawaranSampahModel penawaranSampah:listPenawaranSampahUser) {
            if (penawaranSampah.getPartner().getUsername() != user.getUsername()) {
                listPenawaranSampahUser.remove(penawaranSampah);
            }
        }
        return listPenawaranSampahUser;
    }

    @Override
    public Page<PenawaranSampahModel> retrievePage(Integer fragmentStatus, Pageable paging){
        if (fragmentStatus == 88){
            return penawaranSampahDb.findAllByIdPenawaranSampahIsNotNullOrderByTanggalDibuat(paging);
        }
        return penawaranSampahDb.findAllByStatusOrderByTanggalDibuat(fragmentStatus, paging);
    }

    @Override
    public Page<PenawaranSampahModel> retrievePage(PartnerModel partner, Integer fragmentStatus, Pageable paging){
        if (fragmentStatus == 88){
            return penawaranSampahDb.findAllByPartnerOrderByTanggalDibuat(partner, paging);
        }
        return penawaranSampahDb.findAllByPartnerAndStatusOrderByTanggalDibuat(partner, fragmentStatus, paging);
    }

    @Override
    public PenawaranSampahModel findByIdPenawaranSampah(String idDonasi) { return penawaranSampahDb.findByIdPenawaranSampah(idDonasi).orElse(null);}

    @Override
    public void deletePenawaranSampah(PenawaranSampahModel penawaranSampah) {
        PenawaranSampahModel penawaranSampahLama = findByIdPenawaranSampah(penawaranSampah.getIdPenawaranSampah());
        penawaranSampah.setKeterangan("Dibatalkan oleh Partner");
        penawaranSampahLama.setKeterangan(penawaranSampah.getKeterangan());
        penawaranSampah.setStatus(-1);
        penawaranSampahLama.setStatus(penawaranSampah.getStatus());
        penawaranSampahDb.save(penawaranSampah);
    }

    @Override
    public void updatePenawaranSampah(PenawaranSampahModel penawaranSampah) {
        PenawaranSampahModel penawaranSampahLama = findByIdPenawaranSampah(penawaranSampah.getIdPenawaranSampah());
        penawaranSampahLama.setNamaPic(penawaranSampah.getNamaPic());
        penawaranSampahLama.setKontakPic(penawaranSampah.getKontakPic());
        penawaranSampahLama.setIsPickedUp(penawaranSampah.getIsPickedUp());
        penawaranSampahLama.setAlamatPic(penawaranSampah.getAlamatPic());
        penawaranSampahLama.setBank(penawaranSampah.getBank());
        penawaranSampahLama.setNoRekening(penawaranSampah.getNoRekening());
        penawaranSampahLama.setBerat(penawaranSampah.getBerat());
        penawaranSampahLama.getListItemPenawaranSampah().clear();
        penawaranSampahLama.getListItemPenawaranSampah().addAll(penawaranSampah.getListItemPenawaranSampah());
        penawaranSampahLama.setTransaksiSampah(penawaranSampah.getTransaksiSampah());
        penawaranSampahDb.save(penawaranSampahLama);
    }

    @Override
    public void saveStatus(PenawaranSampahModel penawaranSampah) {
        PenawaranSampahModel penawaranSampahLama = findByIdPenawaranSampah(penawaranSampah.getIdPenawaranSampah());
        penawaranSampahLama.setStatus(penawaranSampah.getStatus());
        penawaranSampahDb.save(penawaranSampahLama);
    }

    @Override
    public void saveStatusDiTolak(PenawaranSampahModel penawaranSampah) {
        PenawaranSampahModel penawaranSampahLama = findByIdPenawaranSampah(penawaranSampah.getIdPenawaranSampah());
        penawaranSampahLama.setStatus(penawaranSampah.getStatus());
        penawaranSampahLama.setKeterangan(penawaranSampah.getKeterangan());
//        penawaranSampahLama.setPartner(penawaranSampah.getPartner());
        penawaranSampahDb.save(penawaranSampahLama);
    }

    @Override
    public void simpanInspeksiPenawaranSampah(PenawaranSampahModel penawaranSampah) {
        PenawaranSampahModel penawaranSampahLama = findByIdPenawaranSampah(penawaranSampah.getIdPenawaranSampah());
        penawaranSampahLama.getListItemPenawaranSampah().clear();
        penawaranSampahLama.getListItemPenawaranSampah().addAll(penawaranSampah.getListItemPenawaranSampah());
        penawaranSampahDb.save(penawaranSampahLama);
        penawaranSampahLama.setHarga(penawaranSampah.getHarga());
        penawaranSampahLama.setBerat(penawaranSampah.getBerat());
        penawaranSampahDb.save(penawaranSampahLama);
    }

    @Override
    public void addTransaksiSampah(PenawaranSampahModel penawaranSampah, Boolean isManual, String bukti){
        ProsesPenawaranSampahModel transaksi = new ProsesPenawaranSampahModel();
        transaksi.setJenisTransaksi(Boolean.TRUE);
        transaksi.setProses(0);
        transaksi.setPenawaranSampah(penawaranSampah);
        transaksi.setNominal(penawaranSampah.getHarga());
        transaksi.setTanggalDibuat(LocalDateTime.now());
        transaksi.setTanggalTransaksi(LocalDateTime.now());
        transaksi.setBukti(bukti);

        if(!isManual){
            transaksi.setKeterangan(penawaranSampah.getKeterangan());
        }
        prosesPenawaranSampahDb.save(transaksi);
    }

    @Override
    public ProsesPenawaranSampahModel getTransaksiByPenawaranSampah(PenawaranSampahModel penawaranSampah){
        return prosesPenawaranSampahDb.findProsesPenawaranSampahModelByPenawaranSampah(penawaranSampah).orElse(null);
    }



}
