package propensi.project.water.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import propensi.project.water.model.PenjualanHasilOlahan.PenawaranOlahanModel;
import propensi.project.water.model.PoinReward.TukarPoinModel;
import propensi.project.water.model.Transaksi.*;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface TransaksiService {

    // add
    ProsesLainModel addTransaksiManual(ProsesLainModel transaksiManual);
    ProsesPenawaranOlahanModel addTransaksiOlahan(PenawaranOlahanModel penawaranOlahan, String bukti, Boolean isManual);
    ProsesTukarPoinModel addTransaksiTukarPoin(String file, TukarPoinModel tukarPoin, Integer totalHarga);

    //get
    List<TransaksiModel> retrieveListAllTransaksi();
    Page<TransaksiModel> retrievePage(Pageable paging, Boolean jenis);
    Page<TransaksiModel> retrievePageIdContaining(String keyword, Pageable paging, Boolean jenis);
    TransaksiModel retrieveTransaksiById(String id);
    ProsesPenawaranSampahModel getProsesPenawaranSampah(String id);
    ProsesPenawaranOlahanModel getProsesPenawaranOlahan(String id);
    ProsesLainModel getProsesLain(String id);
    ProsesTukarPoinModel getProsesTukarPoin(String id);

    //delete
    void delete(TransaksiModel transaksi);

    //update
    TransaksiModel updateTransaksiSampahOlahan(TransaksiModel transaksi);
    ProsesLainModel updateTransaksiProsesLain(ProsesLainModel transaksi);

    //download
    List<TransaksiModel> getListDownload(Map<String, String> generateResult);

    void deleteFolder(File folder);
}
