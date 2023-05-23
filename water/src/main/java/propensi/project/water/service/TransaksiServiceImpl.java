package propensi.project.water.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import propensi.project.water.model.PenjualanHasilOlahan.PenawaranOlahanModel;
import propensi.project.water.model.PoinReward.TukarPoinModel;
import propensi.project.water.model.Transaksi.*;
import propensi.project.water.repository.TransaksiDb.*;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class TransaksiServiceImpl implements TransaksiService {
    @Autowired
    private TransaksiDb transaksiDb;

    @Autowired
    private ProsesLainDb prosesLainDb;

    @Autowired
    private ProsesPenawaranSampahDb prosesPenawaranSampahDb;

    @Autowired
    private ProsesPenawaranOlahanDb prosesPenawaranOlahanDb;

    @Autowired
    private ProsesTukarPoinDb prosesTukarPoinDb;

    @Override
    public ProsesLainModel addTransaksiManual (ProsesLainModel transaksiManual) {
        return prosesLainDb.save(transaksiManual);
    }

    @Override
    public ProsesPenawaranOlahanModel addTransaksiOlahan(PenawaranOlahanModel penawaranOlahan, String bukti, Boolean isManual){
        ProsesPenawaranOlahanModel transaksi = new ProsesPenawaranOlahanModel();
        transaksi.setJenisTransaksi(Boolean.FALSE);
        transaksi.setProses(1);
        transaksi.setPenawaranOlahan(penawaranOlahan);
        transaksi.setNominal(penawaranOlahan.getHarga());
        transaksi.setTanggalDibuat(LocalDateTime.now());
        transaksi.setTanggalTransaksi(LocalDateTime.now());
        transaksi.setBukti(bukti);

        if(!isManual){
            transaksi.setKeterangan(penawaranOlahan.getKeterangan());
        }
        prosesPenawaranOlahanDb.save(transaksi);
        return transaksi;
    }

    @Override
    public ProsesTukarPoinModel addTransaksiTukarPoin(String file, TukarPoinModel tukarPoin, Integer totalHarga){
        ProsesTukarPoinModel transaksi = new ProsesTukarPoinModel();
        transaksi.setJenisTransaksi(Boolean.TRUE);
        transaksi.setProses(3);
        transaksi.setNominal(totalHarga);
        transaksi.setTukarPoin(tukarPoin);
        transaksi.setTanggalDibuat(LocalDateTime.now());
        transaksi.setTanggalTransaksi(LocalDateTime.now());
        transaksi.setBukti(file);

        return prosesTukarPoinDb.save(transaksi);
    }

    @Override
    public List<TransaksiModel> retrieveListAllTransaksi(){
        return transaksiDb.findAll();
    }

    @Override
    public Page<TransaksiModel> retrievePage(Pageable paging, Boolean jenis){
        if (jenis == null){
            return transaksiDb.findAllByIdTransaksiIsNotNullOrderByTanggalDibuat(paging);
        }
        return transaksiDb.findAllByJenisTransaksiOrderByTanggalDibuat(jenis, paging);
    }

    @Override
    public Page<TransaksiModel> retrievePageIdContaining(String keyword,
                                                                 Pageable paging,
                                                                 Boolean jenis){
        if (jenis == null){
            return transaksiDb.findByIdTransaksiContainingIgnoreCaseOrderByTanggalDibuat(keyword,paging);
        }
        return transaksiDb.findByIdTransaksiContainingIgnoreCaseAndJenisTransaksiOrderByTanggalDibuat(jenis,keyword,paging);
    }

    @Override
    public TransaksiModel retrieveTransaksiById(String id){
        Optional<TransaksiModel> transaksi = transaksiDb.findById(id);
        if (transaksi.isPresent()){
            return transaksi.get();
        }
        return null;
    }

    @Override
    public ProsesPenawaranSampahModel getProsesPenawaranSampah(String id){
        Optional<ProsesPenawaranSampahModel> transaksi =
                prosesPenawaranSampahDb.findProsesPenawaranSampahModelByIdTransaksi(id);
        if (transaksi.isPresent()){
            return transaksi.get();
        }
        return null;
    }

    @Override
    public ProsesPenawaranOlahanModel getProsesPenawaranOlahan(String id){
        Optional<ProsesPenawaranOlahanModel> transaksi =
                prosesPenawaranOlahanDb.findProsesPenawaranOlahanModelByIdTransaksi(id);
        if (transaksi.isPresent()){
            return transaksi.get();
        }
        return null;
    }

    @Override
    public ProsesLainModel getProsesLain(String id){
        Optional<ProsesLainModel> transaksi = prosesLainDb.findProsesLainModelByIdTransaksi(id);
        if (transaksi.isPresent()){
            return transaksi.get();
        }
        return null;
    }

    @Override
    public ProsesTukarPoinModel getProsesTukarPoin(String id){
        Optional<ProsesTukarPoinModel> transaksi = prosesTukarPoinDb.findByIdTransaksi(id);
        if (transaksi.isPresent()){
            return transaksi.get();
        }
        return null;
    }

    @Override
    public void delete(TransaksiModel transaksi){
        transaksiDb.delete(transaksi);
    }

    @Override
    public TransaksiModel updateTransaksiSampahOlahan(TransaksiModel updatedTransaksi){
        TransaksiModel transaksi = retrieveTransaksiById(updatedTransaksi.getIdTransaksi());
        transaksi.setKeterangan(updatedTransaksi.getKeterangan());
        transaksi.setBukti(updatedTransaksi.getBukti());
        return transaksiDb.save(transaksi);
    }

    @Override
    public ProsesLainModel updateTransaksiProsesLain(ProsesLainModel updatedTransaksi){
        ProsesLainModel transaksi = (ProsesLainModel) retrieveTransaksiById(updatedTransaksi.getIdTransaksi());
        transaksi.setNamaProses(updatedTransaksi.getNamaProses());
        transaksi.setPerson(updatedTransaksi.getPerson());
        transaksi.setNominal(updatedTransaksi.getNominal());
        transaksi.setJenisTransaksi(updatedTransaksi.getJenisTransaksi());
        transaksi.setTanggalTransaksi(updatedTransaksi.getTanggalTransaksi());
        transaksi.setKeterangan(updatedTransaksi.getKeterangan());
        transaksi.setBukti(updatedTransaksi.getBukti());
        return transaksiDb.save(transaksi);
    }
    @Override
    public List<TransaksiModel> getListDownload(Map<String, String> generateResult){

        Boolean semua = Boolean.valueOf(generateResult.get("semua"));
        LocalDate periodeAwal = LocalDate.parse(generateResult.get("periodeAwal"), DateTimeFormatter.ISO_DATE);
        LocalDate periodeAkhir = LocalDate.parse(generateResult.get("periodeAkhir"), DateTimeFormatter.ISO_DATE);
        Boolean pembelianSampah = Boolean.valueOf(generateResult.get("pembelianSampah"));
        Boolean penjualanOlahan = Boolean.valueOf(generateResult.get("penjualanOlahan"));
        Boolean sumberLainPendapatan = Boolean.valueOf(generateResult.get("sumberLainPendapatan"));
        Boolean sumberLainPengeluaran = Boolean.valueOf(generateResult.get("sumberLainPengeluaran"));
        Boolean pendapatan = penjualanOlahan && sumberLainPendapatan;
        Boolean pengeluaran = pembelianSampah && sumberLainPengeluaran;

        List<TransaksiModel> allTransaksi = transaksiDb.findListByDateRange(periodeAwal, periodeAkhir);

        if(semua || (pendapatan && pengeluaran)){
            return allTransaksi;
        } else if(pendapatan && !pembelianSampah && !sumberLainPengeluaran){
            allTransaksi.removeIf(transaksi -> transaksi.getJenisTransaksi());
        } else if(pengeluaran && !penjualanOlahan && !sumberLainPendapatan){
            allTransaksi.removeIf(transaksi -> !transaksi.getJenisTransaksi());
        }

        else if(pendapatan && pembelianSampah){
            allTransaksi.removeIf(transaksi -> (transaksi.getJenisTransaksi() && transaksi.getProses() >= 2));
        } else if(pendapatan && sumberLainPengeluaran){
            allTransaksi.removeIf(transaksi -> (transaksi.getProses() == 0));
        } else if(sumberLainPendapatan && pengeluaran){
            allTransaksi.removeIf(transaksi -> (transaksi.getProses() == 1));
        } else if(penjualanOlahan && pengeluaran){
            allTransaksi.removeIf(transaksi -> (!transaksi.getJenisTransaksi() && transaksi.getProses() >= 2));
        }

        else if(penjualanOlahan && sumberLainPengeluaran){
            allTransaksi.removeIf(transaksi -> (!transaksi.getJenisTransaksi() && transaksi.getProses() >= 2));
            allTransaksi.removeIf(transaksi -> (transaksi.getProses() == 0));
        } else if(penjualanOlahan && pembelianSampah){
            allTransaksi.removeIf(transaksi -> transaksi.getProses() >= 2);
        } else if(sumberLainPendapatan && pembelianSampah){
            allTransaksi.removeIf(transaksi -> transaksi.getProses() == 1);
            allTransaksi.removeIf(transaksi -> (transaksi.getJenisTransaksi() && transaksi.getProses() >= 2));
        } else if(sumberLainPendapatan && sumberLainPengeluaran){
            allTransaksi.removeIf(transaksi -> transaksi.getProses() < 2);
        }

        else if(penjualanOlahan){
            allTransaksi.removeIf(transaksi -> (transaksi.getProses() != 1));
        } else if(pembelianSampah){
            allTransaksi.removeIf(transaksi -> (transaksi.getProses() != 0));
        } else if(sumberLainPendapatan){
            allTransaksi.removeIf(transaksi -> (transaksi.getProses() >= 2 || transaksi.getJenisTransaksi()));
        } else if(sumberLainPengeluaran){
            allTransaksi.removeIf(transaksi -> (transaksi.getProses() < 2 || !transaksi.getJenisTransaksi()));
        }

        return allTransaksi;
    }

    @Override
    public void deleteFolder(File folder) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteFolder(file);
                }
            }
        }
        folder.delete();
    }

}
