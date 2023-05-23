package propensi.project.water.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import propensi.project.water.model.Warehouse.JenisItemModel;
import propensi.project.water.model.Warehouse.WarehouseModel;
import propensi.project.water.service.WarehouseService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @GetMapping("/warehouse/laporan")
    public String viewAllItem(Model model) {
        List<JenisItemModel> listJenisItem = warehouseService.getListJenisItem();
        model.addAttribute("listJenisItem", listJenisItem);
        //Integer totalKuantitasSampah = 0;
        //model.addAttribute("totalKuantitasSampah", totalKuantitasSampah);
        return "/warehouse/laporanWarehouse";
    }

    @PostMapping("/warehouse/update")
    public String updateItem(@ModelAttribute WarehouseModel item, Model model, RedirectAttributes redirectAttrs){
        WarehouseModel itemLama = warehouseService.getItemById(item.getIdItem());
        String namaItemLama = itemLama.getNamaItem();
        String namaItem = item.getNamaItem();
        WarehouseModel cariModel = warehouseService.getItemByNamaItem(namaItem);
        if(namaItemLama.equals(namaItem)){
            warehouseService.updateItem(item);
            redirectAttrs.addFlashAttribute("success",
                    String.format("Item %s berhasil diperbarui", namaItem ));
        }else{
            if(cariModel == null){
                warehouseService.updateItem(item);
                redirectAttrs.addFlashAttribute("success",
                        String.format("Item %s berhasil diperbarui", namaItem ));
            }else{
                redirectAttrs.addFlashAttribute("failed",
                        String.format("Item %s tidak dapat diperbarui. Item sudah terdaftar dalam warehouse", namaItem ));
            }
        }

        return "redirect:/warehouse/laporan";
    }

    @GetMapping("/warehouse/addNew")
    public String addItemForm(Model model, RedirectAttributes redirectAttrs){
        WarehouseModel itembaru = new WarehouseModel();
        model.addAttribute("itembaru", itembaru);
        return "redirect:/warehouse/laporan";
    }

    @PostMapping("/warehouse/add")
    public String addItem(@ModelAttribute WarehouseModel item, Model model, RedirectAttributes redirectAttrs){
        String namaItem = item.getNamaItem();
        WarehouseModel cariModel = warehouseService.getItemByNamaItem(namaItem);
        if(cariModel == null){
            warehouseService.addItem(item);
            redirectAttrs.addFlashAttribute("success",
                    String.format("Item %s berhasil dibuat", namaItem ));
        }else{
            redirectAttrs.addFlashAttribute("failed",
                    String.format("Item %s tidak dapat dibuat. Item sudah terdaftar dalam warehouse", namaItem ));
        }
        return "redirect:/warehouse/laporan";
    }

    @PostMapping("/warehouse/delete")
    public String deleteItem(@ModelAttribute WarehouseModel item, Model model, RedirectAttributes redirectAttrs){
        String namaItem = item.getNamaItem();
        if(item.getKuantitasSampah().equals(0) && item.getKuantitasOlahan().equals(0)){
            warehouseService.deleteItem(item);
            redirectAttrs.addFlashAttribute("success",
                    String.format("Item %s berhasil dihapus dari database", namaItem ));
        }else{
            redirectAttrs.addFlashAttribute("failed",
                    String.format("Item %s tidak dapat dihapus dari database. Kuantitas sampah dan hasil olahan item harus 0 sebelum dihapus.", namaItem ));
        }
        return "redirect:/warehouse/laporan";
    }


}
