package propensi.project.water.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import propensi.project.water.model.PembelianSampah.ItemPenawaranSampahModel;
import propensi.project.water.model.PembelianSampah.PenawaranSampahModel;
import propensi.project.water.model.Transaksi.ProsesPenawaranOlahanModel;
import propensi.project.water.model.Transaksi.ProsesPenawaranSampahModel;
import propensi.project.water.model.User.PartnerModel;
import propensi.project.water.model.User.Role;
import propensi.project.water.model.User.UserModel;
import propensi.project.water.model.Warehouse.WarehouseModel;
import propensi.project.water.service.PenjualanSampahService;
import propensi.project.water.service.TransaksiService;
import propensi.project.water.service.UserService;
import propensi.project.water.service.WarehouseService;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Controller
@RequestMapping("/penawaran/sampah")
public class PenjualanSampahOlehPartnerController {

    @Qualifier("penjualanSampahServiceImpl")
    @Autowired
    private PenjualanSampahService penjualanSampahService;

    @Qualifier("userServiceImpl")
    @Autowired
    private UserService userService;

    @Qualifier("transaksiServiceImpl")
    @Autowired
    private TransaksiService transaksiService;

    @Qualifier("warehouseServiceImpl")
    @Autowired
    private WarehouseService warehouseService;

    @GetMapping("/add")
    private String addPenawaranSampahForm(
            Model model,
            Principal principal) {
        UserModel userSession = userService.getUserByUsername(principal.getName());
        PenawaranSampahModel penawaranSampah = new PenawaranSampahModel();
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Role userDetails =  userSession.getRole();
        if(userDetails.name().equals("PARTNER")){
            penjualanSampahService.setDefaultPartner(penawaranSampah, userSession);
        }
        penawaranSampah.setListItemPenawaranSampah(new ArrayList<>());
        List<WarehouseModel> listItemWarehouse = warehouseService.getListItemWarehouse();

        model.addAttribute("listItemWarehouse", listItemWarehouse);
        model.addAttribute("userSession", userSession);
        model.addAttribute("penawaranSampah", penawaranSampah);
        return "penawaran-sampah/add-PenawaranSampah";
    }

    @PostMapping(value="/add", params={"save"})
    private String addPenawaranSampahSubmit(
            @ModelAttribute PenawaranSampahModel penawaranSampah,
            RedirectAttributes redirectAttributes,
            @RequestParam(name="fileTransaksi", required = false) MultipartFile fileTransaksi) throws IOException {
        if (penawaranSampah.getListItemPenawaranSampah()==null) {
            penawaranSampah.setListItemPenawaranSampah(new ArrayList<>());
        } else{
            for (ItemPenawaranSampahModel itemPenawaranSampah:penawaranSampah.getListItemPenawaranSampah()) {
                itemPenawaranSampah.setIdPenawaranSampah(penawaranSampah);
                itemPenawaranSampah.setKuantitas(1);
            }
        }
        List<ItemPenawaranSampahModel> listItemPenawaranSampah = penawaranSampah.getListItemPenawaranSampah();

        if(isDuplicate(listItemPenawaranSampah)) {
            redirectAttributes.addFlashAttribute("failed",
                    "Pembuatan penawaran sampah gagal dilakukan karena terdapat duplikasi jenis item sampah");
            return "redirect:add";
        }

        penawaranSampah.setTanggalDibuat(LocalDateTime.now());
        penawaranSampah.setStatus(0);
        penjualanSampahService.addPenawaranSampah(penawaranSampah);

        if(penawaranSampah.getPartner() == null){
            penawaranSampah.setStatus(3);
            penjualanSampahService.saveStatus(penawaranSampah);
            String sRedirect = "redirect:selesai/" + penawaranSampah.getIdPenawaranSampah();
            return sRedirect ;
        }else{
            redirectAttributes.addFlashAttribute("success", "Berhasil menambahkan penawaran sampah!");
            return "redirect:viewall";
        }
    }

    @PostMapping(value="/add", params={"addRow"})
    private String addRowListItemPenawaranSampah(
            @ModelAttribute PenawaranSampahModel penawaranSampah,
            Model model) {
        if (penawaranSampah.getListItemPenawaranSampah()==null || penawaranSampah.getListItemPenawaranSampah().size()==0) {
            penawaranSampah.setListItemPenawaranSampah(new ArrayList<>());
        }
        penawaranSampah.getListItemPenawaranSampah().add(new ItemPenawaranSampahModel());
        List<WarehouseModel> listItemWarehouse = warehouseService.getListItemWarehouse();

        model.addAttribute("penawaranSampah", penawaranSampah);
        model.addAttribute("listItemWarehouse", listItemWarehouse);

        return "penawaran-sampah/add-PenawaranSampah";
    }

    @PostMapping(value="/add", params={"deleteRow"})
    private String deleteRowListItemPenawaranSampah(
            @ModelAttribute PenawaranSampahModel penawaranSampah,
            @RequestParam("deleteRow") Integer row,
            Model model) {
        final Integer rowId = Integer.valueOf(row);
        penawaranSampah.getListItemPenawaranSampah().remove(rowId.intValue());

        List<WarehouseModel> listItemWarehouse = warehouseService.getListItemWarehouse();

        model.addAttribute("penawaranSampah",penawaranSampah);
        model.addAttribute("listItemWarehouse", listItemWarehouse);

        return "penawaran-sampah/add-PenawaranSampah";
    }

    @GetMapping({"/viewall", "/viewall/status={fragmentStatus}"})
    private String viewAllPenawaranSampah(Model model,
                                 Principal principal,
                                 @PathVariable(required = false, name="fragmentStatus") Integer fragmentStatus,
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "5") int size) {

        try{
            UserModel userSession = userService.getUserByUsername(principal.getName());
            Pageable paging = PageRequest.of(page - 1, size);

            Page<PenawaranSampahModel> pagePenawaranSampah;
            // case: userSession=Partner
            if (userSession.getRole().toString().equals("PARTNER")) {
                // case: fragment=semua
                if (fragmentStatus == null){
                    fragmentStatus = 88;
                    pagePenawaranSampah = penjualanSampahService.retrievePage((PartnerModel) userSession,fragmentStatus,paging);
                } else {
                    pagePenawaranSampah = penjualanSampahService.retrievePage((PartnerModel) userSession, fragmentStatus, paging);
                }
            }
            // case: userSession=internal
            else {
                if (fragmentStatus == null){
                    fragmentStatus = 88;
                    pagePenawaranSampah = penjualanSampahService.retrievePage(fragmentStatus, paging);
                } else {
                    pagePenawaranSampah = penjualanSampahService.retrievePage(fragmentStatus, paging);
                }
            }

            List<PenawaranSampahModel> listPenawaranSampah = pagePenawaranSampah.getContent();

            Integer firstItem = (pagePenawaranSampah.getNumber() + 1)*size-size+1;
            Integer lastItem = firstItem + listPenawaranSampah.size() -1;

            model.addAttribute("currentPage", pagePenawaranSampah.getNumber() + 1);
            model.addAttribute("firstItem", firstItem);
            model.addAttribute("lastItem", lastItem);
            model.addAttribute("totalItems", pagePenawaranSampah.getTotalElements());
            model.addAttribute("totalPages", pagePenawaranSampah.getTotalPages());
            model.addAttribute("pageSize", size);
            model.addAttribute("listPenawaranSampah", listPenawaranSampah);
            model.addAttribute("fragmentStatus", fragmentStatus);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "penawaran-sampah/viewall-PenawaranSampah";
    }

    @GetMapping("/view/{idPenawaranSampah}")
    private String viewPenawaranSampah(Model model,
                              @PathVariable(name="idPenawaranSampah") String idPenawaranSampah,
                              Principal principal
    ){
        UserModel userSession = userService.getUserByUsername(principal.getName());
        PenawaranSampahModel penawaranSampah = penjualanSampahService.findByIdPenawaranSampah(idPenawaranSampah);
        List<ItemPenawaranSampahModel> listItemPenawaranSampah = penawaranSampah.getListItemPenawaranSampah();

        model.addAttribute("penawaranSampah", penawaranSampah);
        model.addAttribute("listItemPenawaranSampah", listItemPenawaranSampah);
        model.addAttribute("userSession", userSession);

        return "penawaran-sampah/view-penawaranSampah";
    }

    @GetMapping(value = "/delete/{idPenawaranSampah}")
    private String deletePenawaranSampahSubmit(@PathVariable String idPenawaranSampah,
                                        RedirectAttributes redirectAttrs){
        PenawaranSampahModel penawaranSampah = penjualanSampahService.findByIdPenawaranSampah(idPenawaranSampah);
        penjualanSampahService.deletePenawaranSampah(penawaranSampah);

        redirectAttrs.addFlashAttribute("successDelete",
                String.format("Penawaran penjualan sampah dengan ID %s berhasil dibatalkan", idPenawaranSampah));

        return "redirect:/penawaran/sampah/viewall";
    }

    @GetMapping("/update/{idPenawaranSampah}")
    private String updatePenawaranSampahForm(@PathVariable String idPenawaranSampah,
            Model model,
            Principal principal) {
        UserModel userSession = userService.getUserByUsername(principal.getName());
        PenawaranSampahModel penawaranSampah = penjualanSampahService.findByIdPenawaranSampah(idPenawaranSampah);
        List<ItemPenawaranSampahModel> listItemPenawaranSampah = penawaranSampah.getListItemPenawaranSampah();
        List<WarehouseModel> listItemWarehouse = warehouseService.getListItemWarehouse();

        model.addAttribute("listItemWarehouse", listItemWarehouse);
        model.addAttribute("listItemPenawaranSampah", listItemPenawaranSampah);
        model.addAttribute("userSession", userSession);
        model.addAttribute("penawaranSampah", penawaranSampah);
        return "penawaran-sampah/update-PenawaranSampah";
    }

    @PostMapping(value="/update", params={"save"})
    private String updatePenawaranSampahSubmit(
            @ModelAttribute PenawaranSampahModel penawaranSampah,
            RedirectAttributes redirectAttributes) {

        for (ItemPenawaranSampahModel itemPenawaranSampah:penawaranSampah.getListItemPenawaranSampah()) {
            itemPenawaranSampah.setIdPenawaranSampah(penawaranSampah);
                itemPenawaranSampah.setKuantitas(0);
        }
        List<ItemPenawaranSampahModel> listItemPenawaranSampah = penawaranSampah.getListItemPenawaranSampah();
        if(isDuplicate(listItemPenawaranSampah)) {
            redirectAttributes.addFlashAttribute("failed",
                    "Update penawaran sampah gagal dilakukan karena terdapat duplikasi jenis item sampah");
            String sRedirect = "redirect:update/" + penawaranSampah.getIdPenawaranSampah();
            return sRedirect ;
        }

       penjualanSampahService.updatePenawaranSampah(penawaranSampah);
        redirectAttributes.addFlashAttribute("success", "Berhasil memperbarui penawaran sampah!");
        return "redirect:/penawaran/sampah/viewall";
    }

    @PostMapping(value="/update", params={"addRowUpdate"})
    private String addRowListItemPenawaranSampahUpdate(
            @ModelAttribute PenawaranSampahModel penawaranSampah,
            Model model) {
        if (penawaranSampah.getListItemPenawaranSampah()==null || penawaranSampah.getListItemPenawaranSampah().size()==0) {
            penawaranSampah.setListItemPenawaranSampah(new ArrayList<>());
        }
        penawaranSampah.getListItemPenawaranSampah().add(new ItemPenawaranSampahModel());
        List<WarehouseModel> listItemWarehouse = warehouseService.getListItemWarehouse();

        model.addAttribute("penawaranSampah", penawaranSampah);
        model.addAttribute("listItemWarehouse", listItemWarehouse);

        return "penawaran-sampah/update-PenawaranSampah";
    }

    @PostMapping(value="/update", params={"deleteRowUpdate"})
    private String deleteRowListItemPenawaranSampahUpdate(
            @ModelAttribute PenawaranSampahModel penawaranSampah,
            @RequestParam("deleteRowUpdate") Integer row,
            Model model) {
        final Integer rowId = Integer.valueOf(row);
        //PenawaranSampahModel penawaranSampahLama = penjualanSampahService.findByIdPenawaranSampah(penawaranSampah.getIdPenawaranSampah());
        penawaranSampah.getListItemPenawaranSampah().remove(rowId.intValue());

        List<WarehouseModel> listItemWarehouse = warehouseService.getListItemWarehouse();

        model.addAttribute("penawaranSampah",penawaranSampah);
        model.addAttribute("listItemWarehouse", listItemWarehouse);

        return "penawaran-sampah/update-PenawaranSampah";
    }


    @PostMapping(value="/persetujuan", params={"save"})
    private String persetujuanPenawaranSampahSubmit(
            @ModelAttribute PenawaranSampahModel penawaranSampah,
            RedirectAttributes redirectAttributes) {
        Integer status = penawaranSampah.getStatus();
        penawaranSampah.setStatus(status+1);
        penjualanSampahService.saveStatus(penawaranSampah);
        redirectAttributes.addFlashAttribute("success", "Berhasil memproses penawaran sampah!");
        return "redirect:/penawaran/sampah/viewall";
    }

    @PostMapping(value="/persetujuan/ditolak", params={"save"})
    private String penolakanPenawaranSampahSubmit(
            @ModelAttribute PenawaranSampahModel penawaranSampah,
            RedirectAttributes redirectAttributes) {
        Integer status = penawaranSampah.getStatus();
        penawaranSampah.setStatus(status-1);
        penjualanSampahService.saveStatusDiTolak(penawaranSampah);
        redirectAttributes.addFlashAttribute("success", "Berhasil menolak penawaran sampah!");
        return "redirect:/penawaran/sampah/viewall";
    }

    @GetMapping("/selesai/{idPenawaranSampah}")
    private String selesaiPenawaranSampahForm(@PathVariable String idPenawaranSampah,
                                             Model model,
                                             Principal principal) {
        UserModel userSession = userService.getUserByUsername(principal.getName());
        PenawaranSampahModel penawaranSampah = penjualanSampahService.findByIdPenawaranSampah(idPenawaranSampah);
        List<WarehouseModel> listItemWarehouse = warehouseService.getListItemWarehouse();

        model.addAttribute("listItemWarehouse", listItemWarehouse);
        model.addAttribute("userSession", userSession);
        model.addAttribute("penawaranSampah", penawaranSampah);
        return "penawaran-sampah/inspeksi-PenawaranSampah";
    }

    @PostMapping(value="/selesai", params={"save"})
    private String selesaiPenawaranSampahSubmit(
            @ModelAttribute PenawaranSampahModel penawaranSampah,
            RedirectAttributes redirectAttributes,
            @RequestParam(name="fileTransaksi", required = false) MultipartFile fileTransaksi) throws IOException {

        Integer status = penawaranSampah.getStatus();
        Integer harga = 0;
        Integer berat = 0;
        for (ItemPenawaranSampahModel itemPenawaranSampah:penawaranSampah.getListItemPenawaranSampah()) {
            itemPenawaranSampah.setIdPenawaranSampah(penawaranSampah);
            berat += itemPenawaranSampah.getKuantitas();
            Integer hargaItem = itemPenawaranSampah.getIdItem().getHargaBeli() * itemPenawaranSampah.getKuantitas();
            harga += hargaItem;
            WarehouseModel item = itemPenawaranSampah.getIdItem();
            item.setKuantitasSampah(item.getKuantitasSampah()+itemPenawaranSampah.getKuantitas());
            warehouseService.updateKuantitas(item);
        }

        List<ItemPenawaranSampahModel> listItemPenawaranSampah = penawaranSampah.getListItemPenawaranSampah();
        if(isDuplicate(listItemPenawaranSampah)) {
            redirectAttributes.addFlashAttribute("failed",
                    "Penyelesaian penawaran sampah gagal dilakukan karena terdapat duplikasi jenis item sampah");
            String sRedirect = "redirect:selesai/" + penawaranSampah.getIdPenawaranSampah();
            return sRedirect ;
        }
        penawaranSampah.setBerat(berat);
        penawaranSampah.setHarga(harga);
        penjualanSampahService.simpanInspeksiPenawaranSampah(penawaranSampah);
        PenawaranSampahModel penawaranSampahAsli = penjualanSampahService.findByIdPenawaranSampah(penawaranSampah.getIdPenawaranSampah());

        penawaranSampah.setStatus(status+1);
        penjualanSampahService.saveStatus(penawaranSampah);

        //set file name
        String fileName = StringUtils.cleanPath(fileTransaksi.getOriginalFilename());

        //update transaksi
        if(penawaranSampah.getPartner() == null){
            penjualanSampahService.addTransaksiSampah(penawaranSampahAsli, true, fileName);
        } else {
            penjualanSampahService.addTransaksiSampah(penawaranSampahAsli, false, fileName);
        }
        ProsesPenawaranSampahModel transaksi = penjualanSampahService.getTransaksiByPenawaranSampah(penawaranSampahAsli);
        penawaranSampahAsli.setTransaksiSampah(transaksi);
        penjualanSampahService.updatePenawaranSampah(penawaranSampahAsli);

        //set bukti file
        setBuktiTransaksi(fileTransaksi, transaksi);

        redirectAttributes.addFlashAttribute("success", "Berhasil menyelesaikan penawaran sampah!");
        return "redirect:/penawaran/sampah/viewall";
    }

    @PostMapping(value="/selesai", params={"addRowSelesai"})
    private String addRowListItemPenawaranSampahSelesai(
            @ModelAttribute PenawaranSampahModel penawaranSampah,
            Model model) {
        if (penawaranSampah.getListItemPenawaranSampah()==null || penawaranSampah.getListItemPenawaranSampah().size()==0) {
            penawaranSampah.setListItemPenawaranSampah(new ArrayList<>());
        }
        penawaranSampah.getListItemPenawaranSampah().add(new ItemPenawaranSampahModel());
        List<WarehouseModel> listItemWarehouse = warehouseService.getListItemWarehouse();

        model.addAttribute("penawaranSampah", penawaranSampah);
        model.addAttribute("listItemWarehouse", listItemWarehouse);
        return "penawaran-sampah/inspeksi-PenawaranSampah";
    }

    @PostMapping(value="/selesai", params={"deleteRowSelesai"})
    private String deleteRowListItemPenawaranSampahSelesai(
            @ModelAttribute PenawaranSampahModel penawaranSampah,
            @RequestParam("deleteRowSelesai") Integer row,
            Model model) {
        final Integer rowId = Integer.valueOf(row);
        penawaranSampah.getListItemPenawaranSampah().remove(rowId.intValue());

        List<WarehouseModel> listItemWarehouse = warehouseService.getListItemWarehouse();

        model.addAttribute("penawaranSampah",penawaranSampah);
        model.addAttribute("listItemWarehouse", listItemWarehouse);

        return "penawaran-sampah/inspeksi-PenawaranSampah";
    }

    private boolean isDuplicate(List<ItemPenawaranSampahModel> listItemPenawaranSampah) {
        final Set<WarehouseModel> set1 = new HashSet<>();

        if (!listItemPenawaranSampah.isEmpty() || listItemPenawaranSampah == null) {
            for (ItemPenawaranSampahModel item : listItemPenawaranSampah) {
                if (!set1.add(item.getIdItem())) {
                    return true;
                }
            }
        }
        return false;
    }

    private String setBuktiTransaksi(MultipartFile file, ProsesPenawaranSampahModel transaksi) throws IOException {

        //file bukti
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        //save file bukti
        String uploadDir = "./src/main/resources/static/images/" +
                transaksi.getIdTransaksi() + "-" + fileName;
        FileUploadUtil.saveFile(uploadDir, fileName, file);

        return fileName;
    }

}
