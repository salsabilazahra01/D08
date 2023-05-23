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
import java.util.ArrayList;
import java.util.List;

import propensi.project.water.model.User.Role;
import propensi.project.water.model.User.UserModel;
import propensi.project.water.service.UserService;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {
    @Qualifier("userServiceImpl")
    @Autowired
    private UserService userService;

    @GetMapping(value = "/add")
    public String addAdminForm(Model model) {
        UserModel admin = new UserModel();
        model.addAttribute("admin", admin);

        return "admin/form-add-admin";
    }

    @PostMapping(value = "/add")
    public String addAdminSubmit(@ModelAttribute UserModel admin, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String password = admin.getPassword();
        String passwordConfirmer = request.getParameter("passwordConfirmer");

        for (UserModel user : userService.getListUser()) {
            if (user.getUsername().equals(admin.getUsername())) {
                return "admin/failed-add-admin";
            }
        }

        if ((!userService.verifyPassword(password)) || (!userService.matchPassword(password, passwordConfirmer))) {
            return "admin/failed-add-admin";
        }

        admin.setRole(Role.PARTNER);
        userService.addUser(admin);
        model.addAttribute("admin", admin);

        return "admin/success-add-admin";
    }

    @GetMapping("/viewall")
    public String viewAllAdmin(Model model) {
        List<UserModel> listAdmin = new ArrayList<>();
        for (int i = 0; i < userService.getListUser().size(); i++) {
            if (userService.getListUser().get(i).getRole().equals(Role.ADMIN)) {
                listAdmin.add(userService.getListUser().get(i));
            }
        }
        model.addAttribute("listAdmin", listAdmin);

        return "admin/viewall-admin";
    }
}
