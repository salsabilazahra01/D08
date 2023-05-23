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
import propensi.project.water.model.User.Role;
import propensi.project.water.model.User.SupervisorModel;
import propensi.project.water.model.User.UserModel;
import propensi.project.water.service.SupervisorService;
import propensi.project.water.service.UserService;
import propensi.project.water.service.CustomerService;

@Slf4j
@Controller
@RequestMapping("/supervisor")
public class SupervisorController {
    @Qualifier("userServiceImpl")
    @Autowired
    private UserService userService;

    @Qualifier("supervisorServiceImpl")
    @Autowired
    private SupervisorService supervisorService;

    @GetMapping(value = "/add")
    public String addSupervisorForm(Model model) {
        model.addAttribute("supervisor", new SupervisorModel());

        return "supervisor/form-add-supervisor";
    }

    @PostMapping(value = "/add")
    public String addSupervisorSubmit(@ModelAttribute SupervisorModel supervisor, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String password = supervisor.getPassword();
        String passwordConfirmer = request.getParameter("passwordConfirmer");

        for (UserModel user : userService.getListUser()) {
            if (user.getUsername().equals(supervisor.getUsername())) {
                return "supervisor/failed-add-supervisor";
            }
        }

        if ((!userService.verifyPassword(password)) || (!userService.matchPassword(password, passwordConfirmer))) {
            return "supervisor/failed-add-supervisor";
        }

        supervisor.setRole(Role.SUPERVISOR);
        supervisorService.addSupervisor(supervisor);
        model.addAttribute("namaSupervisor", supervisor.getNama());

        return "supervisor/success-add-supervisor";
    }

    @GetMapping("/viewall")
    public String viewAllSupervisor(Model model) {
        model.addAttribute("listSupervisor", supervisorService.getListSupervisor());

        return "supervisor/viewall-supervisor";
    }
}
