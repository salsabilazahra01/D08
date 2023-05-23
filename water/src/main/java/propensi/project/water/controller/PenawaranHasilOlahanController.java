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
import propensi.project.water.model.PenjualanHasilOlahan.ItemPenawaranOlahanModel;
import propensi.project.water.model.PenjualanHasilOlahan.PenawaranOlahanModel;
import propensi.project.water.model.PoinReward.TukarPoinModel;
import propensi.project.water.model.Transaksi.ProsesLainModel;
import propensi.project.water.model.Transaksi.ProsesPenawaranOlahanModel;
import propensi.project.water.model.User.CustomerModel;
import propensi.project.water.model.Warehouse.WarehouseModel;
import propensi.project.water.service.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/penawaran-hasil-olahan")
public class PenawaranHasilOlahanController {

    @Autowired
    private PenawaranOlahanService penawaranOlahanService;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private TransaksiService transaksiService;

    @GetMapping(value="/viewall/{status}")
    private String viewAllPenawaranOlahan(Model model,
                                          @PathVariable(name="status") String status,
                                          @RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "5") int size,
                                          HttpServletRequest request) {

        try{
            Integer statusInt = Integer.parseInt(status);
            Pageable paging = PageRequest.of(page - 1, size);

            //get customer if exists
            CustomerModel customer =  getCustomer(request);

            Page<PenawaranOlahanModel> pagePenawaranOlahan =
                    penawaranOlahanService.retrievePage(paging, statusInt, customer);

            List<PenawaranOlahanModel> listPageOlahan = pagePenawaranOlahan.getContent();

            Integer firstItem = (pagePenawaranOlahan.getNumber() + 1)*size-size+1;
            Integer lastItem = firstItem + listPageOlahan.size() - 1;

            model.addAttribute("currentPage", pagePenawaranOlahan.getNumber() + 1);
            model.addAttribute("firstItem", firstItem);
            model.addAttribute("lastItem", lastItem);
            model.addAttribute("totalItems", pagePenawaranOlahan.getTotalElements());
            model.addAttribute("totalPages", pagePenawaranOlahan.getTotalPages());
            model.addAttribute("pageSize", size);
            model.addAttribute("listPenawaranOlahan", listPageOlahan);
            model.addAttribute("status", statusInt);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "penawaran-olahan/viewall-penawaran-olahan";
    }

    @GetMapping(value="/view/{idPenawaranOlahan}")
    private String viewPenawaranOlahan(Model model,
                                       @PathVariable String idPenawaranOlahan) {

        PenawaranOlahanModel penawaranOlahan = penawaranOlahanService.getPenawaranOlahanById(idPenawaranOlahan);
        List<ItemPenawaranOlahanModel> listItem = penawaranOlahan.getListItemPenawaranOlahan();

        int totalBerat = 0;
        for(int i = 0; i < listItem.size(); i++){
            totalBerat += listItem.get(i).getKuantitas();
        }

        model.addAttribute("totalBerat", totalBerat);
        model.addAttribute("penawaranOlahan", penawaranOlahan);
        return "penawaran-olahan/view-penawaran-olahan";
    }

    @GetMapping(value="/add")
    private String addPenawaranOlahanForm(Model model,
                                          HttpServletRequest request) {

        //get customer if exists
        CustomerModel customer =  getCustomer(request);

        PenawaranOlahanModel penawaranOlahan = new PenawaranOlahanModel();
        List<WarehouseModel> listItemEx = getListItemExisting(penawaranOlahan);

        //create new list item
        List<ItemPenawaranOlahanModel> listItemPenawaranOlahan = new ArrayList<>();
        penawaranOlahan.setListItemPenawaranOlahan(listItemPenawaranOlahan);
        penawaranOlahan.getListItemPenawaranOlahan().add(new ItemPenawaranOlahanModel());

        //input customer info to penawaranOlahan
        if(customer != null){
            penawaranOlahan = penawaranOlahanService.setCustomerInfo(customer, penawaranOlahan);
        }

        model.addAttribute("penawaranOlahan", penawaranOlahan);
        model.addAttribute("listItemEx", listItemEx);
        return "penawaran-olahan/add-penawaran-olahan";
    }

    @PostMapping(value="/add", params = {"save"})
    private String addPenawaranOlahanSubmit(@ModelAttribute PenawaranOlahanModel penawaranOlahan,
                                            Model model,
                                            RedirectAttributes redirectAttrs,
                                            HttpServletRequest request,
                                            @RequestParam(name="fileTransaksi", required = false) MultipartFile fileTransaksi) throws IOException {

        List<ItemPenawaranOlahanModel> listItem = penawaranOlahan.getListItemPenawaranOlahan();

        //get customer if exists
        CustomerModel customer =  getCustomer(request);

        //check validation
        if(kontakEmpty(penawaranOlahan)){
            redirectAttrs.addFlashAttribute("failed",
                    "E-mail atau nomor telepon harus diisi");
            return "redirect:/penawaran-hasil-olahan/add";
        }
        else if(isDuplicate(listItem)){
            redirectAttrs.addFlashAttribute("failed",
                    "Penawaran hasil olahan sampah tidak dapat dibuat karena terdapat duplikasi jenis item");
            return "redirect:/penawaran-hasil-olahan/add";
        }
        else if(itemValueInvalid(listItem)){
            redirectAttrs.addFlashAttribute("failed",
                    "Perubahan tidak dapat dibuat karena terdapat jenis yang tidak valid");
            return "redirect:/penawaran-hasil-olahan/add";
        }
        else if(!quantityExceeded(listItem, penawaranOlahan).isEmpty()){
            List<String> itemQuantity = quantityExceeded(listItem, penawaranOlahan);
            redirectAttrs.addFlashAttribute("failed",
                    String.format("Berat %s tidak boleh melebihi %s kg", itemQuantity.get(0), itemQuantity.get(1)));
            return "redirect:/penawaran-hasil-olahan/add";
        }

        PenawaranOlahanModel savedPenawaranOlahan = penawaranOlahanService.add(penawaranOlahan, customer);

        //integrate if manual
        if(customer == null){
            integrateTransaksi(fileTransaksi, savedPenawaranOlahan, false);
            integrateWarehouse(savedPenawaranOlahan, false);
        }

        redirectAttrs.addFlashAttribute("success",
            String.format("Penawaran hasil olahan sampah baru dengan id %s berhasil dibuat",
                    penawaranOlahan.getIdPenawaranOlahan()));

        model.addAttribute("penawaranOlahan", penawaranOlahan.getIdPenawaranOlahan());
        return "redirect:/penawaran-hasil-olahan/viewall/-1";
    }

    @GetMapping(value="/update/{idPenawaranOlahan}")
    private String updatePenawaranOlahanForm(Model model,
                                             @PathVariable String idPenawaranOlahan) {

        PenawaranOlahanModel penawaranOlahan = penawaranOlahanService.getPenawaranOlahanById(idPenawaranOlahan);
        List<WarehouseModel> listItemEx = getListItemExisting(penawaranOlahan);

        model.addAttribute("penawaranOlahan", penawaranOlahan);
        model.addAttribute("listItemEx", listItemEx);
        return "penawaran-olahan/update-penawaran-olahan";
    }

    @PostMapping(value="/update", params = {"save"})
    private String updatePenawaranOlahanSubmit(@ModelAttribute PenawaranOlahanModel penawaranOlahan,
                                               Model model,
                                               RedirectAttributes redirectAttrs) {

        List<ItemPenawaranOlahanModel> listItem = penawaranOlahan.getListItemPenawaranOlahan();

        //check validation
        if(kontakEmpty(penawaranOlahan)){
            redirectAttrs.addFlashAttribute("failed",
                    "E-mail atau nomor telepon harus diisi");
            return "redirect:/penawaran-hasil-olahan/update/" + penawaranOlahan.getIdPenawaranOlahan();
        }
        else if(isDuplicate(listItem)){
            redirectAttrs.addFlashAttribute("failed",
                    "Perubahan tidak dapat dibuat karena terdapat duplikasi jenis item");
            return "redirect:/penawaran-hasil-olahan/update/" + penawaranOlahan.getIdPenawaranOlahan();
        }
        else if(itemValueInvalid(listItem)){
            redirectAttrs.addFlashAttribute("failed",
                    "Perubahan tidak dapat dibuat karena terdapat jenis yang tidak valid");
            return "redirect:/penawaran-hasil-olahan/update/" + penawaranOlahan.getIdPenawaranOlahan();
        }
        else if(!quantityExceeded(listItem, penawaranOlahan).isEmpty()){
            List<String> itemQuantity = quantityExceeded(listItem, penawaranOlahan);
            redirectAttrs.addFlashAttribute("failed",
                    String.format("Berat %s tidak boleh melebihi %s kg", itemQuantity.get(0), itemQuantity.get(1)));
            return "redirect:/penawaran-hasil-olahan/update/" + penawaranOlahan.getIdPenawaranOlahan();
        }

        //delete previous items in listItemPenawaranOlahan
        PenawaranOlahanModel penawaranOlahanEx = penawaranOlahanService.getPenawaranOlahanById(penawaranOlahan.getIdPenawaranOlahan());
        penawaranOlahanEx.getListItemPenawaranOlahan().removeAll(penawaranOlahanEx.getListItemPenawaranOlahan());
        penawaranOlahanService.deleteAllItem(penawaranOlahanEx);
        penawaranOlahanService.update(penawaranOlahan, false);

        redirectAttrs.addFlashAttribute("success",
                String.format("id %s berhasil diperbaharui", penawaranOlahan.getIdPenawaranOlahan()));

        model.addAttribute("totalBerat", getTotalBerat(listItem));
        model.addAttribute("penawaranOlahan", penawaranOlahan);
        return "penawaran-olahan/view-penawaran-olahan";
    }

    @PostMapping(value="/add", params = {"addRowItem"})
    private String addRowAddForm(@ModelAttribute PenawaranOlahanModel penawaranOlahan,
                                       Model model){

        //create list item with item quantity > 0
        List<WarehouseModel> listItemEx = warehouseService.getListItem();
        listItemEx.removeIf(item -> (item.getKuantitasOlahan() == 0));

        //get current list item
        List<ItemPenawaranOlahanModel> listItemPO = penawaranOlahan.getListItemPenawaranOlahan();

        //create new list
        if(listItemPO == null || listItemPO.size() == 0){
            penawaranOlahan.setListItemPenawaranOlahan(new ArrayList<>());
        }
        listItemPO.add(new ItemPenawaranOlahanModel());

        model.addAttribute("penawaranOlahan", penawaranOlahan);
        model.addAttribute("listItemEx", listItemEx);

        return "penawaran-olahan/add-penawaran-olahan";
    }

    @PostMapping(value = "/add", params = {"deleteRowItem"})
    private String deleteRowAddForm(@ModelAttribute PenawaranOlahanModel penawaranOlahan,
                                      @RequestParam("deleteRowItem") Integer row,
                                      Model model){
        final Integer rowId = Integer.valueOf(row);

        penawaranOlahan.getListItemPenawaranOlahan().remove(rowId.intValue());
        List<WarehouseModel> listItemEx = warehouseService.getListItem();

        model.addAttribute("penawaranOlahan", penawaranOlahan);
        model.addAttribute("listItemEx", listItemEx);

        return "penawaran-olahan/add-penawaran-olahan";
    }

    @PostMapping(value="/update", params = {"addRowItem"})
    private String addRowUpdateForm(@ModelAttribute PenawaranOlahanModel penawaranOlahan,
                                       Model model){

        //create list item with item quantity > 0
        List<WarehouseModel> listItemEx = warehouseService.getListItem();
        listItemEx.removeIf(item -> (item.getKuantitasOlahan() == 0));

        //get current list item
        List<ItemPenawaranOlahanModel> listItemPO = penawaranOlahan.getListItemPenawaranOlahan();

        //create new list
        if(listItemPO == null || listItemPO.size() == 0){
            penawaranOlahan.setListItemPenawaranOlahan(new ArrayList<>());
        }
        listItemPO.add(new ItemPenawaranOlahanModel());

        model.addAttribute("penawaranOlahan", penawaranOlahan);
        model.addAttribute("listItemEx", listItemEx);

        return "penawaran-olahan/update-penawaran-olahan";
    }

    @PostMapping(value = "/update", params = {"deleteRowItem"})
    private String deleteRowUpdateForm(@ModelAttribute PenawaranOlahanModel penawaranOlahan,
                                          @RequestParam("deleteRowItem") Integer row,
                                          Model model){
        final Integer rowId = Integer.valueOf(row);

        penawaranOlahan.getListItemPenawaranOlahan().remove(rowId.intValue());
        List<WarehouseModel> listItemEx = warehouseService.getListItem();

        model.addAttribute("penawaranOlahan", penawaranOlahan);
        model.addAttribute("listItemEx", listItemEx);

        return "penawaran-olahan/update-penawaran-olahan";
    }

    @GetMapping(value = "/delete/{idPenawaranOlahan}")
    private String deletePenawaranOlahan(@PathVariable String idPenawaranOlahan,
                                         RedirectAttributes redirectAttrs){

        PenawaranOlahanModel penawaranOlahan = penawaranOlahanService.getPenawaranOlahanById(idPenawaranOlahan);
        penawaranOlahanService.delete(penawaranOlahan);

        redirectAttrs.addFlashAttribute("success",
                String.format("Penawaran hasil olahan sampah dengan id %s berhasil dihapus",
                        penawaranOlahan.getIdPenawaranOlahan()));

        return "redirect:/penawaran-hasil-olahan/viewall/-1";
    }

    @PostMapping(value = "/update-status")
    private String updateStatusPenawaranOlahan(@ModelAttribute PenawaranOlahanModel penawaranOlahan,
                                               RedirectAttributes redirectAttrs,
                                               Model model,
                                               @RequestParam(name="filePengiriman", required = false) MultipartFile filePengiriman,
                                               @RequestParam(name="fileTransaksi", required = false) MultipartFile fileTransaksi) throws IOException {


        PenawaranOlahanModel penawaranOlahanEx = penawaranOlahanService.getPenawaranOlahanById(penawaranOlahan.getIdPenawaranOlahan());
        List<ItemPenawaranOlahanModel> listItem = penawaranOlahanEx.getListItemPenawaranOlahan();

        //approval & status
        Boolean isApproved = penawaranOlahan.getKeteranganTolak() == null;
        Integer status = penawaranOlahanEx.getStatus();

        if(isApproved){
            if(status == 0){
                listItem = penawaranOlahan.getListItemPenawaranOlahan();
                int totalHarga = 0;
                for(ItemPenawaranOlahanModel item : listItem){
                    totalHarga += item.getHarga();
                }
                penawaranOlahanEx.setHarga(totalHarga);
                penawaranOlahanEx.setListItemPenawaranOlahan(listItem);
            }
            else if(status == 1){
                integrateTransaksi(fileTransaksi, penawaranOlahanEx, true);
            }
            else if(status == 2){
                if(filePengiriman != null){
                    String fileName = setBuktiKirim(filePengiriman, penawaranOlahanEx);
                    penawaranOlahanEx.setBuktiKirim(fileName);
                }
            }
            else if(status == 3){
                integrateWarehouse(penawaranOlahanEx, true);
            }
            penawaranOlahanEx.setStatus(penawaranOlahanEx.getStatus()+1);
        } else {
            if(status == 0){
                penawaranOlahanEx.setKeteranganTolak(penawaranOlahan.getKeteranganTolak());
            } else if(status == 1){
                penawaranOlahanEx.setKeteranganTolak("Customer menolak penawaran harga");
            }
            penawaranOlahanEx.setStatus(5);
        }
        penawaranOlahanService.updateStatus(penawaranOlahanEx);

        redirectAttrs.addFlashAttribute("success",
                String.format("Status penawaran hasil olahan sampah dengan id %s berhasil diperbarui",
                        penawaranOlahan.getIdPenawaranOlahan()));

        model.addAttribute("listItem", listItem);
        model.addAttribute("penawaranOlahan", penawaranOlahanEx);
        return "redirect:/penawaran-hasil-olahan/view/" + penawaranOlahanEx.getIdPenawaranOlahan();
    }

    private Integer getTotalBerat(List<ItemPenawaranOlahanModel> listItem){
        int totalBerat = 0;
        for(int i = 0; i < listItem.size(); i++){
            totalBerat += listItem.get(i).getKuantitas();
        }
        return totalBerat;
    }

    private void integrateTransaksi(MultipartFile fileTransfer, PenawaranOlahanModel penawaranOlahan, Boolean isUpdate)
            throws IOException {

        List<ItemPenawaranOlahanModel> listItem = penawaranOlahan.getListItemPenawaranOlahan();

        //update harga
        int totalHarga = 0;
        for(ItemPenawaranOlahanModel item: listItem){
            totalHarga += item.getHarga();
        }
        penawaranOlahan.setHarga(totalHarga);

        //update transaksi
        String fileName = StringUtils.cleanPath(fileTransfer.getOriginalFilename());
        ProsesPenawaranOlahanModel transaksi = transaksiService.addTransaksiOlahan(penawaranOlahan, fileName, true);

        //set bukti file
        setBuktiTransaksi(fileTransfer, transaksi);

        penawaranOlahan.setTransaksiOlahan(transaksi);

        if(isUpdate){
            penawaranOlahanService.updateStatus(penawaranOlahan);
        } else {
            penawaranOlahanService.add(penawaranOlahan, null);
        }
    }

    private void integrateWarehouse(PenawaranOlahanModel penawaranOlahan, Boolean isUpdate){

        List<ItemPenawaranOlahanModel> listItem = penawaranOlahan.getListItemPenawaranOlahan();

        //update warehouse
        for(ItemPenawaranOlahanModel item: listItem){
            WarehouseModel itemWarehouse = warehouseService.getItemById(item.getIdItem().getIdItem());
            itemWarehouse.setKuantitasOlahan(itemWarehouse.getKuantitasOlahan()-item.getKuantitas());
            warehouseService.updateItem(itemWarehouse);
        }

        if(isUpdate){
            penawaranOlahanService.updateStatus(penawaranOlahan);
        } else {
            penawaranOlahanService.add(penawaranOlahan, null);
        }
    }

    private List<String> quantityExceeded(List<ItemPenawaranOlahanModel> listItem, PenawaranOlahanModel penawaranOlahan) {
        List<String> itemQuantity = new ArrayList<>();
        List<WarehouseModel> listItemEx = warehouseService.getListItem();

        //check list of items in the ordering process
        Map<String, Integer> qtyUnavailable = getOrderedQty(listItemEx, penawaranOlahan);

        for(ItemPenawaranOlahanModel item : listItem){
            Integer qtyAvailable = item.getIdItem().getKuantitasOlahan() - qtyUnavailable.get(item.getIdItem().getNamaItem());
            Integer qtyNewPO = item.getKuantitas();
            if(qtyNewPO > qtyAvailable){
                itemQuantity.add(item.getIdItem().getNamaItem());
                itemQuantity.add(qtyAvailable.toString());
            }
        }

        return itemQuantity;
    }

    private boolean isDuplicate(List<ItemPenawaranOlahanModel> listItem) {
        final Set<WarehouseModel> set1 = new HashSet<>();

        if (!listItem.isEmpty() || listItem == null) {
            for (ItemPenawaranOlahanModel item : listItem) {
                if (!set1.add(item.getIdItem())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean itemValueInvalid(List<ItemPenawaranOlahanModel> listItem) {
        for(ItemPenawaranOlahanModel item:listItem){
            if(item.getIdItem() == null){
                return true;
            }
        }
        return false;
    }

    private boolean kontakEmpty(PenawaranOlahanModel penawaranOlahan) {
        return penawaranOlahan.getEmail().isEmpty() && penawaranOlahan.getHp().isEmpty();
    }

    private CustomerModel getCustomer(HttpServletRequest request) {
        return customerService.getCustomerByUsername(request.getRemoteUser()) == null ?
                null : customerService.getCustomerByUsername(request.getRemoteUser());
    }

    private List<WarehouseModel> getListItemExisting(PenawaranOlahanModel penawaranOlahan) {
        //create list of items that has qty > 0
        List<WarehouseModel> listItemEx = warehouseService.getListItem();
        listItemEx.removeIf(item -> (item.getKuantitasOlahan() == 0));

        Map<String, Integer> qtyUnavailable = getOrderedQty(listItemEx, penawaranOlahan);

        //remove items if qty is used in the ordering process
        listItemEx.removeIf(item -> (item.getKuantitasOlahan() - qtyUnavailable.get(item.getNamaItem()) <= 0));

        return listItemEx;
    }

    private Map<String, Integer> getOrderedQty(List<WarehouseModel> listItemEx, PenawaranOlahanModel penawaranOlahan) {

        List<PenawaranOlahanModel> listAllPO = penawaranOlahanService.findAll();
        Map<String, Integer> orderedQty = new HashMap<>();

        //put all items in warehouse
        for(WarehouseModel item:listItemEx){
            orderedQty.put(item.getNamaItem(), 0);
        }

        //put PO with existing status < 4 to qtyWarehouse
        for(PenawaranOlahanModel po : listAllPO){
            if(po.getStatus() < 4 && !po.getIdPenawaranOlahan().equals(penawaranOlahan.getIdPenawaranOlahan())){
                for(ItemPenawaranOlahanModel item:po.getListItemPenawaranOlahan()){
                    Integer qtyExisting = orderedQty.get(item.getIdItem().getNamaItem());
                    orderedQty.put(item.getIdItem().getNamaItem(), qtyExisting + item.getKuantitas());
                }
            }
        }

        return orderedQty;
    }

    private String setBuktiKirim(MultipartFile file, PenawaranOlahanModel penawaranOlahan) throws IOException {

        //file bukti
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        //save file bukti
        String uploadDir = "./src/main/resources/static/images/" +
                penawaranOlahan.getIdPenawaranOlahan() + "-" + fileName;
        FileUploadUtil.saveFile(uploadDir, fileName, file);

        return fileName;
    }

    private String setBuktiTransaksi(MultipartFile file, ProsesPenawaranOlahanModel transaksi) throws IOException {

        //file bukti
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        //save file bukti
        String uploadDir = "./src/main/resources/static/images/" +
                transaksi.getIdTransaksi() + "-" + fileName;
        FileUploadUtil.saveFile(uploadDir, fileName, file);

        return fileName;
    }

}
