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

import propensi.project.water.model.User.CustomerModel;
import propensi.project.water.model.User.PartnerModel;
import propensi.project.water.model.User.Role;
import propensi.project.water.model.User.UserModel;
import propensi.project.water.service.CustomerService;
import propensi.project.water.service.PartnerService;
import propensi.project.water.service.UserService;

@Slf4j
@Controller
@RequestMapping("/partner")
public class PartnerController {
    @Qualifier("userServiceImpl")
    @Autowired
    private UserService userService;

    @Qualifier("partnerServiceImpl")
    @Autowired
    private PartnerService partnerService;

    @GetMapping(value = "/add")
    public String addPartnerForm(Model model) {
        PartnerModel partner = new PartnerModel();
        model.addAttribute("partner", partner);

        return "partner/form-add-partner";
    }

    @PostMapping(value = "/add")
    public String addPartnerSubmit(@ModelAttribute PartnerModel partner, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String password = partner.getPassword();
        String passwordConfirmer = request.getParameter("passwordConfirmer");

        if (partner.getEmail().isEmpty() && partner.getHp().isEmpty()) {
            return "partner/failed-add-partner";
        }

        if (partner.getEmail().equals("")) {
            partner.setEmail(null);
        }

        if (partner.getHp().equals("")) {
            partner.setHp(null);
        }

        for (UserModel user : userService.getListUser()) {
            if (user.getUsername().equals(partner.getUsername())) {
                redirectAttributes.addFlashAttribute("failed", "Username telah terdaftar. Harap masukkan username lain");
                return "redirect:/partner/add";
            }
        }

        if ((!userService.verifyPassword(password)) || (!userService.matchPassword(password, passwordConfirmer))) {
            redirectAttributes.addFlashAttribute("failed", "Password dan konfirmasi password tidak sama. Masukkan password kembali");
            return "redirect:/partner/add";
        }

        partner.setRole(Role.PARTNER);
        partnerService.addPartner(partner);
        model.addAttribute("partner", partner);

        return "redirect:/login";
    }

    @GetMapping("/viewall")
    public String viewAllPartner(Model model) {
        model.addAttribute("listPartner", partnerService.getListPartner());

        return "partner/viewall-partner";
    }
}
