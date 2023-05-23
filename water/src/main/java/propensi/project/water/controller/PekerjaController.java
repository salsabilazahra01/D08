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

import propensi.project.water.model.User.PekerjaModel;
import propensi.project.water.model.User.Role;
import propensi.project.water.model.User.UserModel;
import propensi.project.water.service.PekerjaService;
import propensi.project.water.service.UserService;

@Slf4j
@Controller
@RequestMapping("/pekerja")
public class PekerjaController {
    @Qualifier("userServiceImpl")
    @Autowired
    private UserService userService;

    @Qualifier("pekerjaServiceImpl")
    @Autowired
    private PekerjaService pekerjaService;

    @GetMapping(value = "/add")
    public String addPekerjaForm(Model model) {
        model.addAttribute("pekerja", new PekerjaModel());

        return "pekerja/form-add-pekerja";
    }

    @PostMapping(value = "/add")
    public String addPekerjaSubmit(@ModelAttribute PekerjaModel pekerja, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String password = pekerja.getPassword();
        String passwordConfirmer = request.getParameter("passwordConfirmer");

        for (UserModel user : userService.getListUser()) {
            if (user.getUsername().equals(pekerja.getUsername())) {
                return "pekerja/failed-add-pekerja";
            }
        }

        if ((!userService.verifyPassword(password)) || (!userService.matchPassword(password, passwordConfirmer))) {
            return "pekerja/failed-add-pekerja";
        }

        pekerja.setRole(Role.PEKERJA);
        pekerjaService.addPekerja(pekerja);
        model.addAttribute("namaPekerja", pekerja.getNama());

        return "pekerja/success-add-pekerja";
    }

    @GetMapping("/viewall")
    public String viewAllPekerja(Model model) {
        model.addAttribute("listPekerja", pekerjaService.getListPekerja());

        return "pekerja/viewall-pekerja";
    }
}
