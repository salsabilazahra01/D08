package propensi.project.water.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import propensi.project.water.model.Donasi.DonasiModel;
import propensi.project.water.model.User.DonaturModel;
import propensi.project.water.model.User.UserModel;
import propensi.project.water.service.MengelolaKaryawanService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/manajemen-user")
public class MengelolaKaryawanController {

    @Qualifier("mengelolaKaryawanServiceImpl")
    @Autowired
    private MengelolaKaryawanService mengelolaKaryawanService;

    @GetMapping({"/viewall", "/viewall/{role}"})
    private String retrieveListUser(
            Model model,
            @PathVariable(required = false, name="role") String role,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        try {
            Pageable paging = PageRequest.of(page - 1, size);

            Page<UserModel> pageUser;
            // case: role user=semua
            if (role == null) {
                role="semua";
                pageUser = mengelolaKaryawanService.retrievePage(role, paging);
            } else {
                pageUser = mengelolaKaryawanService.retrievePage(role, paging);
            }

            List<UserModel> listUser = pageUser.getContent();

            Integer firstItem = (pageUser.getNumber() + 1)*size-size+1;
            Integer lastItem = firstItem + listUser.size() -1;

            model.addAttribute("currentPage", pageUser.getNumber() + 1);
            model.addAttribute("firstItem", firstItem);
            model.addAttribute("lastItem", lastItem);
            model.addAttribute("totalItems", pageUser.getTotalElements());
            model.addAttribute("totalPages", pageUser.getTotalPages());
            model.addAttribute("pageSize", size);
            model.addAttribute("role", role);
            model.addAttribute("listUser", listUser);
        }

        catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }


        return "mengelola-karyawan/viewall-user";
    }

    @GetMapping("/view/{username}")
    private String retrieveUserDetail(
            Model model,
            @PathVariable(name="username") String username,
            String role,
            Principal principal
    ){
        UserModel userSession = mengelolaKaryawanService.retrieveUserDetail(principal.getName());
        UserModel user = mengelolaKaryawanService.retrieveUserDetail(username);

        model.addAttribute("role", role);
        model.addAttribute("user", user);
        model.addAttribute("userSession", userSession);

        return "mengelola-karyawan/view-user";
    }

    @GetMapping("/add")
    private String addKaryawanForm(Model model) {
        UserModel user = new UserModel();
        model.addAttribute("user", user);
        return "mengelola-karyawan/add-karyawan";
    }

    @PostMapping("/add")
    private String addKaryawanSubmit(
            @ModelAttribute UserModel user,
            RedirectAttributes redirectAttributes
    ){
        if (mengelolaKaryawanService.uniqueValueConstraint(user)) {
            redirectAttributes.addFlashAttribute("error", "Username dan email telah terdaftar");
            return "redirect:add";
        } else {
            mengelolaKaryawanService.addKaryawan(user);
            redirectAttributes.addFlashAttribute("success", "Berhasil menambahkan karyawan");
            return "redirect:viewall";
        }
    }

    @GetMapping("/update/{username}")
    private String updateKaryawanForm(
            @PathVariable String username,
            Model model
    ){
        UserModel user = mengelolaKaryawanService.retrieveUserDetail(username);
        model.addAttribute("user", user);

        return "mengelola-karyawan/update-karyawan";
    }


    @PostMapping("/update")
    private String updateKaryawanSubmit(
            @ModelAttribute UserModel user,
            RedirectAttributes redirectAttributes
            ){

        // new kontak is not unique
        if (!mengelolaKaryawanService.uniqueValueConstraintUpdate(user)) {
            System.out.println("masuk di false controller");
            redirectAttributes.addFlashAttribute("failedUpdate",
                    String.format("Email dan Nomor Telepon harus unik"));
        } else {
            redirectAttributes.addFlashAttribute(
                    "success", "Data karyawan berhasil diubah");
        }

        return "redirect:view/" + user.getUsername();
    }

    @GetMapping(value = "/delete/{username}")
    private String deleteKaryawanSubmit(@PathVariable String username,
                                             RedirectAttributes redirectAttrs){

        UserModel karyawan = mengelolaKaryawanService.retrieveUserDetail(username);
        mengelolaKaryawanService.deleteUser(karyawan);

        redirectAttrs.addFlashAttribute("success",
                String.format("Karyawan dengan username %s berhasil dihapus", username));

        return "redirect:/manajemen-user/viewall/";
    }

    @PostMapping("/warehouse/delete")
    public String deleteItem(
            @RequestParam(value="username") String username
    ){
        UserModel user = mengelolaKaryawanService.retrieveUserDetail(username);
        mengelolaKaryawanService.deleteUser(user);
        return "redirect:/warehouse/laporan";
    }


}
