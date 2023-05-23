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
import propensi.project.water.model.User.ManajerModel;
import propensi.project.water.model.User.Role;
import propensi.project.water.model.User.UserModel;
import propensi.project.water.service.ManajerService;
import propensi.project.water.service.UserService;
import propensi.project.water.service.CustomerService;

@Slf4j
@Controller
@RequestMapping("/manajer")
public class ManajerController {
    @Qualifier("userServiceImpl")
    @Autowired
    private UserService userService;

    @Qualifier("manajerServiceImpl")
    @Autowired
    private ManajerService manajerService;

    @GetMapping(value = "/add")
    public String addManajerForm(Model model) {
        model.addAttribute("manajer", new ManajerModel());

        return "manajer/form-add-manajer";
    }

    @PostMapping(value = "/add")
    public String addManajerSubmit(@ModelAttribute ManajerModel manajer, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String password = manajer.getPassword();
        String passwordConfirmer = request.getParameter("passwordConfirmer");

        for (UserModel user : userService.getListUser()) {
            if (user.getUsername().equals(manajer.getUsername())) {
                return "manajer/failed-add-manajer";
            }
        }

        if ((!userService.verifyPassword(password)) || (!userService.matchPassword(password, passwordConfirmer))) {
            return "manajer/failed-add-manajer";
        }

        manajer.setRole(Role.MANAJER);
        manajerService.addManajer(manajer);
        model.addAttribute("namaManajer", manajer.getNama());

        return "manajer/success-add-manajer";
    }

    @GetMapping("/viewall")
    public String viewAllManajer(Model model) {
        model.addAttribute("listManajer", manajerService.getListManajer());

        return "manajer/viewall-manajer";
    }
}
