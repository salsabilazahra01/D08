package propensi.project.water.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import propensi.project.water.model.User.DonaturModel;
import propensi.project.water.model.User.Role;
import propensi.project.water.model.User.UserModel;
import propensi.project.water.service.DonaturService;
import propensi.project.water.service.UserService;

@Slf4j
@Controller
@RequestMapping("/donatur")
public class DonaturController {
    @Qualifier("userServiceImpl")
    @Autowired
    private UserService userService;

    @Qualifier("donaturServiceImpl")
    @Autowired
    private DonaturService donaturService;

    @GetMapping(value = "/add")
    public String addDonaturForm(Model model) {
        model.addAttribute("donatur", new DonaturModel());

        return "donatur/form-add-donatur";
    }

    @PostMapping(value = "/add")
    public String addDonaturSubmit(@ModelAttribute DonaturModel donatur,
                                   Model model, HttpServletRequest request,
                                   RedirectAttributes redirectAttributes)
    {
        String password = donatur.getPassword();
        String passwordConfirmer = request.getParameter("passwordConfirmer");

        if (donatur.getEmail().isEmpty() && donatur.getHp().isEmpty()) {
            return "donatur/failed-add-donatur";
        }

        if (donatur.getEmail().equals("")) {
            donatur.setEmail(null);
        }

        if (donatur.getHp().equals("")) {
            donatur.setHp(null);
        }

        for (UserModel user : userService.getListUser()) {
            if (user.getUsername().equals(donatur.getUsername())) {
                redirectAttributes.addFlashAttribute("failed", "Username telah terdaftar. Harap masukkan username lain");
                return "redirect:/donatur/add";
            }
        }

        if ((!userService.verifyPassword(password)) || (!userService.matchPassword(password, passwordConfirmer))) {
            redirectAttributes.addFlashAttribute("failed", "Password dan konfirmasi password tidak sama. Masukkan password kembali");
            return "redirect:/donatur/add";
        }

        donatur.setRole(Role.DONATUR);
        donaturService.addDonatur(donatur);
        model.addAttribute("donatur", donatur);

        return "redirect:/login";
    }

    @GetMapping("/viewall")
    public String viewAllDonatur(Model model) {
        model.addAttribute("listDonatur", donaturService.getListDonatur());

        return "donatur/viewall-donatur";
    }
}
