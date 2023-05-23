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

import propensi.project.water.model.User.Role;
import propensi.project.water.model.User.TeknisiModel;
import propensi.project.water.model.User.UserModel;
import propensi.project.water.service.TeknisiService;
import propensi.project.water.service.UserService;

@Slf4j
@Controller
@RequestMapping("/teknisi")
public class TeknisiController {
    @Qualifier("userServiceImpl")
    @Autowired
    private UserService userService;

    @Qualifier("teknisiServiceImpl")
    @Autowired
    private TeknisiService teknisiService;

    @GetMapping(value = "/add")
    public String addTeknisiForm(Model model) {
        model.addAttribute("teknisi", new TeknisiModel());

        return "teknisi/form-add-teknisi";
    }

    @PostMapping(value = "/add")
    public String addTeknisiSubmit(@ModelAttribute TeknisiModel teknisi, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String password = teknisi.getPassword();
        String passwordConfirmer = request.getParameter("passwordConfirmer");

        for (UserModel user : userService.getListUser()) {
            if (user.getUsername().equals(teknisi.getUsername())) {
                return "teknisi/failed-add-teknisi";
            }
        }

        if ((!userService.verifyPassword(password)) || (!userService.matchPassword(password, passwordConfirmer))) {
            return "teknisi/failed-add-teknisi";
        }

        teknisi.setRole(Role.TEKNISI);
        teknisiService.addTeknisi(teknisi);
        model.addAttribute("namaTeknisi", teknisi.getNama());

        return "teknisi/success-add-teknisi";
    }

    @GetMapping("/viewall")
    public String viewAllTeknisi(Model model) {
        model.addAttribute("listTeknisi", teknisiService.getListTeknisi());

        return "teknisi/viewall-teknisi";
    }
}
