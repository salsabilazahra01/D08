package propensi.project.water.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import propensi.project.water.model.User.DonaturModel;
import propensi.project.water.model.User.UserModel;
import propensi.project.water.model.artikel.ArtikelModel;
import propensi.project.water.service.ArtikelService;
import propensi.project.water.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/artikel")
public class ArtikelController {

    @Autowired
    private UserService userService;
    @Autowired
    private ArtikelService artikelService;

    @GetMapping("/viewall")
    public String viewAllArtikel(Model model, RedirectAttributes redirectAttrs, HttpServletRequest request) {

        UserModel user = userService.getUserByUsername(request.getRemoteUser()) == null ?
                null : userService.getUserByUsername(request.getRemoteUser());
        if (user!=null) {
            if (user.getRole().toString().equals("DONATUR")) {
                DonaturModel donatur = (DonaturModel) user;
                model.addAttribute("poin", donatur.getPoin());
            }
        }

        List<ArtikelModel> listArtikel = artikelService.getListArtikel();
        model.addAttribute("listArtikel", listArtikel);
        return "artikel/artikel-viewall";
    }

    @GetMapping("/add")
    public String addArtikelForm(Model model, RedirectAttributes redirectAttrs) {
        ArtikelModel artikelBaru = new ArtikelModel();
        model.addAttribute("artikel", artikelBaru);
        return "artikel/artikel-add";
    }


    @PostMapping(value = "/add")
    public String addArtikelSubmit(Model model, @ModelAttribute ArtikelModel artikel,
                                   @RequestParam("image") MultipartFile file,
                                   RedirectAttributes redirectAttrs
    ) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (fileName == "" || artikel.getContent().equals("")) {
            redirectAttrs.addFlashAttribute("failed", "Lengkapi seluruh field");
            model.addAttribute("artikel", artikel);
            return "redirect:/artikel/add";
        } else {
            artikel.setImageTitle(fileName);
            ArtikelModel savedArtikel = artikelService.addArtikel(artikel);
            String uploadDir = "./src/main/resources/static/images/" + savedArtikel.getIdArtikel();
            FileUploadUtil.saveFile(uploadDir, fileName, file);
            artikelService.addArtikel(artikel);

            redirectAttrs.addFlashAttribute("success", "Artikel baru berhasil dibuat");
            return "redirect:/artikel/viewall";
        }
    }

    @GetMapping("/view/{idArtikel}")
    private String viewArtikel(Model model,
                               @PathVariable(name = "idArtikel") String idArtikel,
                               Principal principal,
                               HttpServletRequest request
    ) {

        UserModel user = userService.getUserByUsername(request.getRemoteUser()) == null ?
                null : userService.getUserByUsername(request.getRemoteUser());
        if (user!=null) {
            if (user.getRole().toString().equals("DONATUR")) {
                DonaturModel donatur = (DonaturModel) user;
                model.addAttribute("poin", donatur.getPoin());
            }
        }

        ArtikelModel artikel = artikelService.findByIdArtikel(idArtikel);
        model.addAttribute("artikel", artikel);
        return "artikel/artikel-view";
    }

    @GetMapping(value = "/delete/{idArtikel}")
    private String deleteArtikel(@PathVariable String idArtikel,
                                 RedirectAttributes redirectAttrs) {
        ArtikelModel artikel = artikelService.findByIdArtikel(idArtikel);
        String folderPath = "./src/main/resources/static/images/" + artikel.getIdArtikel();
        File folder = new File(folderPath);
        artikelService.deleteFolder(folder);
        artikelService.deleteArtikel(artikel);
        redirectAttrs.addFlashAttribute("success",
                String.format("Artikel  dengan ID %s berhasil dibatalkan", idArtikel));

        return "redirect:/artikel/viewall";
    }

    @GetMapping("/update/{idArtikel}")
    private String updateArtikelForm(@PathVariable String idArtikel,
                                     Model model,
                                     Principal principal) {

        ArtikelModel artikel = artikelService.findByIdArtikel(idArtikel);
        model.addAttribute("artikel", artikel);
        return "artikel/artikel-update";
    }

    @PostMapping(value = "/update", params = {"save"})
    private String updateArtikelSubmit(
            @ModelAttribute ArtikelModel artikel, @RequestParam("image") MultipartFile file,
            RedirectAttributes redirectAttributes) throws IOException {

        String idArtikel = artikel.getIdArtikel();
        ArtikelModel artikelLama = artikelService.findByIdArtikel(idArtikel);
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String namaFileLama = artikelLama.getImageTitle();
        artikel.setImageTitle(fileName);

        if (fileName == "") {
            artikel.setImageTitle(artikelLama.getImageTitle());
        } else {
            ArtikelModel savedArtikel = artikelService.addArtikel(artikel);
            String uploadDir = "./src/main/resources/static/images/" + savedArtikel.getIdArtikel();
            artikelService.deleteFile(uploadDir, namaFileLama);
            FileUploadUtil.saveFile(uploadDir, fileName, file);
        }

        artikelService.updateArtikel(artikel);
        redirectAttributes.addFlashAttribute("success", "Berhasil memperbarui penawaran sampah!");
        return "redirect:/artikel/viewall";
    }

}
