package propensi.project.water.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;
import propensi.project.water.model.PoinReward.TukarPoinModel;
import propensi.project.water.model.Transaksi.*;
import propensi.project.water.service.TransaksiService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/transaksi")
public class TransaksiController {

    @Autowired
    private TransaksiService transaksiService;

    @GetMapping("/add")
    public String addTransaksiProsesLainForm(Model model){
        ProsesLainModel transaksiManual = new ProsesLainModel();
        transaksiManual.setProses(2);
        model.addAttribute("transaksi", transaksiManual);
        return "laporan-transaksi/add-transaksi-form";
    }

    @PostMapping(value = "/add")
    public String addTransaksiProsesLainSubmit(@ModelAttribute ProsesLainModel transaksiManual,
                                     @RequestParam("file") MultipartFile file,
                                     RedirectAttributes redirectAttrs
    ) throws IOException {

            System.out.println("INI FILE 3 " + file);
            transaksiManual.setTanggalDibuat(LocalDateTime.now());

            //file bukti
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            transaksiManual.setBukti(fileName);

            ProsesLainModel savedTransaksi = transaksiService.addTransaksiManual(transaksiManual);

            //save file bukti
            String uploadDir = "./src/main/resources/static/images/" + savedTransaksi.getIdTransaksi() + "-" + savedTransaksi.getBukti();
            FileUploadUtil.saveFile(uploadDir, fileName, file);

            transaksiService.addTransaksiManual(transaksiManual);

            redirectAttrs.addFlashAttribute("success","Transaksi baru berhasil ditambahkan");
            return "redirect:/transaksi/viewall/semua";

    }

    @GetMapping(value="/viewall/{jenis}")
    private String viewAllTransaksi(Model model,
                                    @PathVariable(name="jenis") String jenis,
                                    @RequestParam(required = false) String keyword,
                                    @RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "5") int size) {

        try{
            Pageable paging = PageRequest.of(page - 1, size);

            Page<TransaksiModel> pageTransaksi;
            if (keyword == null) {
                if (jenis.equals("semua")){
                    pageTransaksi = transaksiService.retrievePage(paging, null);
                } else if(jenis.equals("pendapatan")){
                    pageTransaksi = transaksiService.retrievePage(paging,Boolean.FALSE);
                } else {
                    pageTransaksi = transaksiService.retrievePage(paging,Boolean.TRUE);
                }
            } else {
                if (jenis.equals("semua")){
                    pageTransaksi = transaksiService.retrievePageIdContaining(keyword, paging, null);
                } else if(jenis.equals("pendapatan")){
                    pageTransaksi = transaksiService.retrievePageIdContaining(keyword, paging, Boolean.FALSE);
                } else {
                    pageTransaksi = transaksiService.retrievePageIdContaining(keyword, paging, Boolean.TRUE);
                }
                model.addAttribute("keyword", keyword);
            }

            List<TransaksiModel> listPageTransaksi = pageTransaksi.getContent();
            List<TransaksiModel> listAllTransaksi = transaksiService.retrieveListAllTransaksi();

            int total = 0;
            for(TransaksiModel transaksi : listAllTransaksi){
                if(jenis.equals("semua")){
                    if(transaksi.getJenisTransaksi()){
                        total -= transaksi.getNominal();
                    } else {
                        total += transaksi.getNominal();
                    }
                } else if(jenis.equals("pendapatan")){
                    if(!transaksi.getJenisTransaksi()){
                        total += transaksi.getNominal();
                    }
                } else {
                    if(transaksi.getJenisTransaksi()){
                        total += transaksi.getNominal();
                    }
                }
            }

            Integer firstItem = (pageTransaksi.getNumber() + 1)*size-size+1;
            Integer lastItem = firstItem + listPageTransaksi.size() -1;

            model.addAttribute("currentPage", pageTransaksi.getNumber() + 1);
            model.addAttribute("firstItem", firstItem);
            model.addAttribute("lastItem", lastItem);
            model.addAttribute("totalItems", pageTransaksi.getTotalElements());
            model.addAttribute("totalPages", pageTransaksi.getTotalPages());
            model.addAttribute("pageSize", size);
            model.addAttribute("listTransaksi", listPageTransaksi);
            model.addAttribute("jenis", jenis);
            model.addAttribute("total", total);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "laporan-transaksi/viewall-transaksi";
    }

    @GetMapping(value="/view/{idTransaksi}")
    private String viewTransaksi(Model model,
                                 @PathVariable String idTransaksi) {

        var transaksi = transaksiService.retrieveTransaksiById(idTransaksi);
        model.addAttribute("transaksi", transaksi);

        return "laporan-transaksi/view-transaksi";
    }

    @GetMapping(value = "/delete/{idTransaksi}")
    private String deleteTransaksiProsesLain(@PathVariable String idTransaksi,
                                             RedirectAttributes redirectAttrs){

        TransaksiModel transaksi = transaksiService.retrieveTransaksiById(idTransaksi);
        transaksiService.delete(transaksi);

        String uploadDir = "./src/main/resources/static/images/"+ transaksi.getIdTransaksi() + "-";
        transaksiService.deleteFolder(new File(uploadDir + transaksi.getBukti()));

        redirectAttrs.addFlashAttribute("success",
                String.format("Transaksi dengan ID %s berhasil dihapus", idTransaksi));

        return "redirect:/transaksi/viewall/semua";
    }

    @GetMapping("/update/{idTransaksi}")
    private String updateTransaksiForm(
            @PathVariable String idTransaksi,
            Model model
    ) {

        var transaksi = transaksiService.retrieveTransaksiById(idTransaksi);
        model.addAttribute("transaksi", transaksi);

        return "laporan-transaksi/update-transaksi";
    }

    @PostMapping("/update/sampah-olahan")
    private String updateTransaksiSubmitSampahDanOlahan(@ModelAttribute TransaksiModel transaksi,
                                                        RedirectAttributes redirectAttributes,
                                                        Model model,
                                                        @RequestParam(value = "file", required = false) MultipartFile file
    ) throws IOException {

        //get transaksi lama
        String idTransaksi = transaksi.getIdTransaksi();
        String buktiTransaksiLama = transaksiService.retrieveTransaksiById(idTransaksi).getBukti();

        TransaksiModel updatedTransaksi;
        if(file == null){
            transaksi.setBukti(buktiTransaksiLama);
            updatedTransaksi = transaksiService.updateTransaksiSampahOlahan(transaksi);
        }else{
            String fileName =  StringUtils.cleanPath(file.getOriginalFilename());
            transaksi.setBukti(fileName);
            updatedTransaksi = transaksiService.updateTransaksiSampahOlahan(transaksi);

            String uploadDir = "./src/main/resources/static/images/"+ updatedTransaksi.getIdTransaksi() + "-";
            transaksiService.deleteFolder(new File(uploadDir + buktiTransaksiLama));

            FileUploadUtil.saveFile(uploadDir + updatedTransaksi.getBukti(), fileName, file);
        }

        model.addAttribute("transaksi", updatedTransaksi);
        redirectAttributes.addFlashAttribute("success","Transaksi berhasil diubah");

        return "redirect:/transaksi/view/" + transaksi.getIdTransaksi();
    }

    @PostMapping("/update/proses-lain")
    private String updateTransaksiSubmitProsesLain(@ModelAttribute ProsesLainModel transaksi,
                                                   RedirectAttributes redirectAttributes,
                                                   Model model,
                                                   @RequestParam(value = "file", required = false) MultipartFile file
    ) throws IOException {

        //get transaksi lama
        String idTransaksi = transaksi.getIdTransaksi();
        String buktiTransaksiLama = transaksiService.retrieveTransaksiById(idTransaksi).getBukti();

        TransaksiModel updatedTransaksi;
        if(file == null){
            transaksi.setBukti(buktiTransaksiLama);
            updatedTransaksi = transaksiService.updateTransaksiProsesLain(transaksi);
        }else{
            String fileName =  StringUtils.cleanPath(file.getOriginalFilename());
            transaksi.setBukti(fileName);
            updatedTransaksi = transaksiService.updateTransaksiProsesLain(transaksi);

            String uploadDir = "./src/main/resources/static/images/"+ updatedTransaksi.getIdTransaksi() + "-";
            transaksiService.deleteFolder(new File(uploadDir + buktiTransaksiLama));

            FileUploadUtil.saveFile(uploadDir + updatedTransaksi.getBukti(), fileName, file);
        }

        model.addAttribute("transaksi", updatedTransaksi);
        redirectAttributes.addFlashAttribute("success",
                String.format("Transaksi dengan ID %s berhasil diubah", updatedTransaksi.getIdTransaksi()));

        return "redirect:/transaksi/view/" + transaksi.getIdTransaksi();
    }

    @PostMapping("/download")
    public void exportToCSV(HttpServletResponse response, HttpServletRequest request) throws IOException {

        Map<String, String> generateResult = new HashMap<>();
        generateResult.put("pembelianSampah", request.getParameter("pembeliansampah"));
        generateResult.put("penjualanOlahan", request.getParameter("penjualanolahan"));
        generateResult.put("sumberLainPendapatan", request.getParameter("sumberlainpendapatan"));
        generateResult.put("sumberLainPengeluaran", request.getParameter("sumberlainpengeluaran"));
        generateResult.put("periodeAwal", request.getParameter("periodeawal"));
        generateResult.put("periodeAkhir", request.getParameter("periodeakhir"));

        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=transaksi_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);

        List<TransaksiModel> listTransaksi = transaksiService.getListDownload(generateResult);

        ICsvListWriter csvWriter = new CsvListWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"ID Transaksi", "Jenis Transaksi", "Nominal", "Tanggal Transaksi", "Keterangan", "Sumber"};
        csvWriter.writeHeader(csvHeader);

        for (TransaksiModel transaksi : listTransaksi) {
            csvWriter.write(
                    transaksi.getIdTransaksi(),
                    transaksi.getJenisString(),
                    transaksi.getNominal().toString(),
                    transaksi.getTanggalTransaksi().toString(),
                    transaksi.getKeterangan(),
                    transaksi.getProsesString()
            );
        }

        csvWriter.close();

    }
}
