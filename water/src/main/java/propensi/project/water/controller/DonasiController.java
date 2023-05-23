package propensi.project.water.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import propensi.project.water.model.Donasi.DonasiModel;
import propensi.project.water.model.Donasi.ItemDonasiModel;
import propensi.project.water.model.Donasi.DonasiModel;
import propensi.project.water.model.User.DonaturModel;
import propensi.project.water.model.User.UserModel;
import propensi.project.water.model.Warehouse.WarehouseModel;
import propensi.project.water.service.DonasiService;
import propensi.project.water.service.UserService;
import propensi.project.water.service.WarehouseService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/donasi")
public class DonasiController {

    @Qualifier("donasiServiceImpl")
    @Autowired
    private DonasiService donasiService;

    @Qualifier("userServiceImpl")
    @Autowired
    private UserService userService;

    @Qualifier("warehouseServiceImpl")
    @Autowired
    private WarehouseService warehouseService;

    @GetMapping("/add")
    private String addDonasiForm(
            Model model,
            Principal principal) {
        UserModel userSession = userService.getUserByUsername(principal.getName());
        DonaturModel donatur = (DonaturModel) userSession;

        DonasiModel donasi = new DonasiModel();

        donasiService.setDefaultDonatur(donasi, userSession);
        donasi.setListItemDonasi(new ArrayList<>());
        List<WarehouseModel> listItemWarehouse = warehouseService.getListItemWarehouse();

        model.addAttribute("poin", donatur.getPoin());
        model.addAttribute("listItemWarehouse", listItemWarehouse);
        model.addAttribute("userSession", userSession);
        model.addAttribute("donasi", donasi);
        return "donasi/add-donasi";
    }

    @PostMapping(value="/add", params={"save"})
    private String addDonasiSubmit(
            @ModelAttribute DonasiModel donasi,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (donasi.getListItemDonasi()==null) {
            donasi.setListItemDonasi(new ArrayList<>());
        } else{
            for (ItemDonasiModel itemDonasi:donasi.getListItemDonasi()) {
                itemDonasi.setIdDonasi(donasi);
                itemDonasi.setKuantitas(0);
            }
        }

        List<ItemDonasiModel> listItemDonasi = donasi.getListItemDonasi();

        if(isDuplicate(listItemDonasi)) {
            redirectAttributes.addFlashAttribute("failed",
                    "Pembuatan donasi gagal dilakukan karena terdapat duplikasi jenis item donasi");
            return "redirect:/donasi/add/";
        }

        donasi.setTanggalDibuat(LocalDateTime.now());
        donasi.setStatus(0);
        donasi.setBeratSetelah(0);
        donasi.setPoinEarned(0);
        donasiService.addDonasi(donasi);
        redirectAttributes.addFlashAttribute("success", "Berhasil menambahkan donasi");
        return "redirect:viewall";
    }

    @PostMapping(value="/add", params={"addRow"})
    private String addRowListItemDonasi(
            @ModelAttribute DonasiModel donasi,
            Model model) {
        if (donasi.getListItemDonasi()==null || donasi.getListItemDonasi().size()==0) {
            donasi.setListItemDonasi(new ArrayList<>());
        } else{
            for (ItemDonasiModel itemDonasi:donasi.getListItemDonasi()) {
                itemDonasi.setIdDonasi(donasi);
                itemDonasi.setKuantitas(0);
            }
        }
        donasi.getListItemDonasi().add(new ItemDonasiModel());
        List<WarehouseModel> listItemWarehouse = warehouseService.getListItemWarehouse();

        model.addAttribute("donasi", donasi);
        model.addAttribute("listItemWarehouse", listItemWarehouse);

        return "donasi/add-donasi";
    }

    @PostMapping(value="/add", params={"deleteRow"})
    private String deleteRowListItemDonasi(
            @ModelAttribute DonasiModel donasi,
            @RequestParam("deleteRow") Integer row,
            Model model) {
        final Integer rowId = Integer.valueOf(row);
        donasi.getListItemDonasi().remove(rowId.intValue());

        List<WarehouseModel> listItemWarehouse = warehouseService.getListItemWarehouse();

        model.addAttribute("donasi",donasi);
        model.addAttribute("listItemWarehouse", listItemWarehouse);

        return "donasi/add-donasi";
    }
    @GetMapping({"/viewall", "/viewall/status={fragmentStatus}"})
    private String viewAllDonasi(Model model,
                                    Principal principal,
                                    @PathVariable(required = false, name="fragmentStatus") Integer fragmentStatus,
                                    @RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "5") int size) {

        try{
            UserModel userSession = userService.getUserByUsername(principal.getName());
            Pageable paging = PageRequest.of(page - 1, size);

            Page<DonasiModel> pageDonasi;
            // case: userSession=donatur
            if (userSession.getRole().toString().equals("DONATUR")) {
                // case: fragment=semua
                if (fragmentStatus == null){
                    fragmentStatus = 88;
                    pageDonasi = donasiService.retrievePage((DonaturModel) userSession, fragmentStatus, paging);
                } else {
                    pageDonasi = donasiService.retrievePage((DonaturModel) userSession, fragmentStatus, paging);
                }
            }
            // case: userSession=internal
            else {
                if (fragmentStatus == null){
                    fragmentStatus = 88;
                    pageDonasi = donasiService.retrievePage(fragmentStatus, paging);
                } else {
                    pageDonasi = donasiService.retrievePage(fragmentStatus, paging);
                }
            }

            List<DonasiModel> listDonasi = pageDonasi.getContent();

            Integer firstItem = (pageDonasi.getNumber() + 1)*size-size+1;
            Integer lastItem = firstItem + listDonasi.size() -1;

            if(userSession.getRole().toString().equals("DONATUR")){
                DonaturModel donatur = (DonaturModel) userSession;
                model.addAttribute("poin", donatur.getPoin());
            }

            model.addAttribute("currentPage", pageDonasi.getNumber() + 1);
            model.addAttribute("firstItem", firstItem);
            model.addAttribute("lastItem", lastItem);
            model.addAttribute("totalItems", pageDonasi.getTotalElements());
            model.addAttribute("totalPages", pageDonasi.getTotalPages());
            model.addAttribute("pageSize", size);
            model.addAttribute("listDonasi", listDonasi);
            model.addAttribute("fragmentStatus", fragmentStatus);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "donasi/viewall-donasi";
    }

    @GetMapping("/view/{idDonasi}")
    private String viewDonasi(Model model,
                              @PathVariable(name="idDonasi") String idDonasi,
                              Principal principal
    ){
        UserModel userSession = userService.getUserByUsername(principal.getName());
        DonasiModel donasi = donasiService.findByIdDonasi(idDonasi);

        if(userSession.getRole().toString().equals("DONATUR")){
            DonaturModel donatur = (DonaturModel) userSession;
            model.addAttribute("poin", donatur.getPoin());
        }
        model.addAttribute("donasi", donasi);
        model.addAttribute("userSession", userSession);

        return "donasi/view-donasi";
    }

    @GetMapping(value = "/delete/{idDonasi}")
    private String deleteKaryawanSubmit(@PathVariable String idDonasi,
                                        RedirectAttributes redirectAttrs){

        DonasiModel donasi = donasiService.findByIdDonasi(idDonasi);
        donasiService.deleteDonasi(donasi);

        redirectAttrs.addFlashAttribute("success",
                String.format("Donasi dengan ID %s berhasil dibatalkan", idDonasi));

        return "redirect:/donasi/viewall/";
    }


    @GetMapping("/update/{idDonasi}")
    private String updateDonasiForm(
            @PathVariable String idDonasi,
            Model model,
            Principal principal
    ){
        DonasiModel donasi = donasiService.findByIdDonasi(idDonasi);

        List<WarehouseModel> listItemWarehouse = warehouseService.getListItemWarehouse();

        UserModel userSession = userService.getUserByUsername(principal.getName());
        DonaturModel donatur = (DonaturModel) userSession;

        model.addAttribute("poin", donatur.getPoin());
        model.addAttribute("listItemWarehouse", listItemWarehouse);
        model.addAttribute("donasi", donasi);

        return "donasi/update-donasi";
    }

    @PostMapping(value="/update", params="save")
    private String updateDonasiSubmit(
            @ModelAttribute DonasiModel donasi,
            RedirectAttributes redirectAttributes
    ){
        if (donasi.getListItemDonasi()==null) {
            donasi.setListItemDonasi(new ArrayList<>());
        } else{
            for (ItemDonasiModel itemDonasi:donasi.getListItemDonasi()) {
                itemDonasi.setIdDonasi(donasi);
                itemDonasi.setKuantitas(0);
            }
        }

        List<ItemDonasiModel> listItemDonasi = donasi.getListItemDonasi();

        if(isDuplicate(listItemDonasi)) {
            redirectAttributes.addFlashAttribute("failed",
                    "Perubahan tidak dapat dilakukan karena terdapat duplikasi jenis item donasi");
            return "redirect:/donasi/update/" + donasi.getIdDonasi();
        }

        // delete items in itemDonasiList
        DonasiModel donasiEx = donasiService.findByIdDonasi(donasi.getIdDonasi());
        donasiEx.getListItemDonasi().removeAll(donasiEx.getListItemDonasi());
        donasiService.deleteAllItemDonasi(donasiEx);
        donasiService.updateDonasi(donasi);

        redirectAttributes.addFlashAttribute("success", "Detail donasi berhasil diubah");
        return "redirect:view/" + donasi.getIdDonasi();
    }

    @PostMapping(value="/update", params={"addRow"})
    private String addRowListItemDonasiUpdate(
            @ModelAttribute DonasiModel donasi,
            Model model) {
        if (donasi.getListItemDonasi()==null || donasi.getListItemDonasi().size()==0) {
            donasi.setListItemDonasi(new ArrayList<>());
        } else{
            for (ItemDonasiModel itemDonasi:donasi.getListItemDonasi()) {
                itemDonasi.setIdDonasi(donasi);
                itemDonasi.setKuantitas(0);
            }
        }
        donasi.getListItemDonasi().add(new ItemDonasiModel());
        List<WarehouseModel> listItemWarehouse = warehouseService.getListItemWarehouse();

        model.addAttribute("donasi", donasi);
        model.addAttribute("listItemWarehouse", listItemWarehouse);

        return "donasi/update-donasi";
    }

    @PostMapping(value="/update", params={"deleteRow"})
    private String deleteRowListItemDonasiUpdate(
            @ModelAttribute DonasiModel donasi,
            @RequestParam("deleteRow") Integer row,
            Model model) {
        final Integer rowId = Integer.valueOf(row);
        donasi.getListItemDonasi().remove(rowId.intValue());

        List<WarehouseModel> listItemWarehouse = warehouseService.getListItemWarehouse();

        model.addAttribute("donasi",donasi);
        model.addAttribute("listItemWarehouse", listItemWarehouse);

        return "donasi/update-donasi";
    }

    @GetMapping(value = "/update-status/{idDonasi}")
    private String updateStatusDonasi(@PathVariable String idDonasi,
                                      RedirectAttributes redirectAttrs,
                                      Principal principal) {
        DonasiModel donasi = donasiService.findByIdDonasi(idDonasi);
        UserModel user = userService.getUserByUsername(principal.getName());

        donasi.setStatus(donasi.getStatus()+1);

        donasiService.updateStatus(donasi);

        String status = "";
        if (donasi.getStatus()==-1) {
            status = "ditolak";
        } else if (donasi.getStatus()==1) {
            status = "disetujui";
        } else if (donasi.getStatus()==2) {
            status = "dalam perjalanan";
        } else if (donasi.getStatus()==3) {
            status = "dalam proses inspeksi";
        } else if (donasi.getStatus()==4) {
            status = "selesai";
        }

        if(user.getRole().equals("DONATUR")){
            redirectAttrs.addFlashAttribute("success",
                    String.format("Status donasi dengan id %s berhasil diperbarui menjadi %s",
                            donasi.getIdDonasi(), status));
        } else {
            redirectAttrs.addFlashAttribute("success",
                    String.format("Status donasi dengan id %s berhasil diperbarui menjadi %s",
                            donasi.getIdDonasi(), status));
        }

        return "redirect:/donasi/view/" + donasi.getIdDonasi();
    }

    @PostMapping(value = "/update-status2/{idDonasi}")
    private String updateStatusDonasi2(@PathVariable String idDonasi,
                                      @ModelAttribute DonasiModel donasi,
                                      @RequestParam(required = false, defaultValue = "true") String isApproved,
                                      RedirectAttributes redirectAttrs,
                                      Principal principal) {
        UserModel user = userService.getUserByUsername(principal.getName());

        donasi.setStatus(-1);
        donasiService.updateStatus(donasi);

        String status = "ditolak";
        if(user.getRole().equals("DONATUR")){
            redirectAttrs.addFlashAttribute("success",
                    String.format("Status donasi dengan id %s berhasil diperbarui menjadi %s",
                            donasi.getIdDonasi(), status));
        } else {
            redirectAttrs.addFlashAttribute("success",
                    String.format("Status donasi dengan id %s berhasil diperbarui menjadi %s",
                            donasi.getIdDonasi(), status));
        }

        return "redirect:/donasi/view/" + donasi.getIdDonasi();
    }

    @GetMapping("/update-status/{idDonasi}/inspeksi")
    private String updateStatusDonasiInspeksiForm(@PathVariable String idDonasi,
                                                  Model model,
                                                  Principal principal) {
        DonasiModel donasi = donasiService.findByIdDonasi(idDonasi);

        if (donasi.getListItemDonasi()==null || donasi.getListItemDonasi().size()==0) {
            donasi.setListItemDonasi(new ArrayList<>());
        }
        List<WarehouseModel> listItemWarehouse = warehouseService.getListItemWarehouse();

        model.addAttribute("listItemWarehouse", listItemWarehouse);
        model.addAttribute("donasi", donasi);
        return "donasi/inspeksi-donasi";
    }

    @PostMapping(value="/update-status/inspeksi", params="save")
    private String updateStatusDonasiInspeksiForm(
            @ModelAttribute DonasiModel donasi,
            RedirectAttributes redirectAttributes,
            Principal principal
    ){
        if (donasi.getListItemDonasi() == null || donasi.getListItemDonasi().size() == 0) {
            donasi.setListItemDonasi(new ArrayList<>());
        }

        List<ItemDonasiModel> listItemDonasi = donasi.getListItemDonasi();

        if(isDuplicate(listItemDonasi)) {
            redirectAttributes.addFlashAttribute("failed",
                    "Penyimpanan gagal karena terdapat duplikasi jenis item donasi");
            return "redirect:/donasi/update-status/" + donasi.getIdDonasi() + "/inspeksi";
        }

        // delete items in itemDonasiList
        DonasiModel donasiEx = donasiService.findByIdDonasi(donasi.getIdDonasi());
        donasiEx.getListItemDonasi().removeAll(donasiEx.getListItemDonasi());
        donasiService.deleteAllItemDonasi(donasiEx);

        donasi.setStatus(donasi.getStatus()+1);
        donasiService.updateStatusDone(donasi);

        redirectAttributes.addFlashAttribute("success",
                String.format("Status donasi dengan id %s berhasil diperbarui menjadi selesai",
                        donasi.getIdDonasi()));

        return "redirect:/donasi/view/" + donasi.getIdDonasi();
    }

    @PostMapping(value="/update-status/inspeksi", params={"addRow"})
    private String addRowListItemDonasiInspeksi(
            @ModelAttribute DonasiModel donasi,
            Model model) {
        if (donasi.getListItemDonasi()==null || donasi.getListItemDonasi().size()==0) {
            donasi.setListItemDonasi(new ArrayList<>());
        }

        donasi.getListItemDonasi().add(new ItemDonasiModel());

        for (ItemDonasiModel itemDonasi:donasi.getListItemDonasi()) {
            if (itemDonasi.getIdItem()==null) {
                itemDonasi.setIdItem(warehouseService.getListItemWarehouse().get(0));
                itemDonasi.setKuantitas(0);
            }
            itemDonasi.setIdDonasi(donasi);
        }

        List<WarehouseModel> listItemWarehouse = warehouseService.getListItemWarehouse();

        model.addAttribute("donasi", donasi);
        model.addAttribute("listItemWarehouse", listItemWarehouse);

        return "donasi/inspeksi-donasi";
    }

    @PostMapping(value="/update-status/inspeksi", params={"deleteRow"})
    private String deleteRowListItemDonasiInspeksi(
            @ModelAttribute DonasiModel donasi,
            @RequestParam("deleteRow") Integer row,
            Model model) {
        final Integer rowId = Integer.valueOf(row);
        donasi.getListItemDonasi().remove(rowId.intValue());

        List<WarehouseModel> listItemWarehouse = warehouseService.getListItemWarehouse();

        model.addAttribute("donasi",donasi);
        model.addAttribute("listItemWarehouse", listItemWarehouse);

        return "donasi/inspeksi-donasi";
    }

    private boolean isDuplicate(List<ItemDonasiModel> listItemDonasi) {
        final Set<WarehouseModel> set1 = new HashSet<>();

        if (!listItemDonasi.isEmpty() || listItemDonasi == null) {
            for (ItemDonasiModel item : listItemDonasi) {
                if (!set1.add(item.getIdItem())) {
                    return true;
                }
            }
        }
        return false;
    }
}
