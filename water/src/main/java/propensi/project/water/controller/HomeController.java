package propensi.project.water.controller;

import lombok.extern.slf4j.Slf4j;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;

import propensi.project.water.model.User.*;
import propensi.project.water.model.artikel.ArtikelModel;
import propensi.project.water.service.DonaturService;
import propensi.project.water.model.CompanyProfile.TestimoniModel;
import propensi.project.water.model.User.DonaturModel;
import propensi.project.water.model.User.ManajerModel;
import propensi.project.water.model.User.UserModel;
import propensi.project.water.model.CompanyProfile.CompanyProfileModel;
import propensi.project.water.model.User.Role;
import propensi.project.water.service.*;
import propensi.project.water.setting.Settings;
import propensi.project.water.setting.xml.Attributes;
import propensi.project.water.setting.xml.ServiceResponse;
import propensi.project.water.service.ManajerService;
import propensi.project.water.service.UserService;

@Slf4j
@Controller
public class HomeController {
    @Autowired
    private ServerProperties serverProperties;

    @Autowired
    private ManajerService manajerService;

    @Autowired
    private UserService userService;

    @Autowired
    private DonaturService donaturService;

    @Autowired
    private CompanyProfileService companyProfileService;

    @Autowired
    private TestimoniService testimoniService;

    @Autowired
    private ArtikelService artikelService;

    private WebClient webClient = WebClient.builder().build();

    @RequestMapping("/")
    public String homeAfterLogin(HttpServletRequest request, Model model) {
        UserModel user = userService.getUserByUsername(request.getRemoteUser()) == null ?
                null : userService.getUserByUsername(request.getRemoteUser());
        if (user!=null) {
            if (user.getRole().toString().equals("DONATUR")) {
                DonaturModel donatur = (DonaturModel) user;
                model.addAttribute("poin", donatur.getPoin());
            }
            model.addAttribute("user", user);
        }

        if (companyProfileService.checkDb()) {
            System.out.println("test");
            CompanyProfileModel companyProfile = new CompanyProfileModel();
            companyProfile.setIdCompanyProfile("COMPROF");
            companyProfile.setNamaPerusahaan("WATER");
            companyProfile.setDeskripsiPerusahaan("Ini deskripsi");
            companyProfile.setVisiPerusahaan("Ini visi");
            companyProfile.setMisiPerusahaan("Ini misi");
            companyProfile.setEmailPerusahaan("iniemail@gmail.com");
            companyProfile.setHpPerusahaan(1234567);
            companyProfile.setAlamatPerusahaan("Ini alamat");
            companyProfile.setListTestimoni(new ArrayList<>());
            companyProfileService.add(companyProfile);
        }

        CompanyProfileModel companyProfile = companyProfileService.getCompanyProfile("COMPROF");
        List<TestimoniModel> listTestimoni = companyProfile.getListTestimoni();
        model.addAttribute("companyProfile", companyProfile);
        model.addAttribute("listTestimoni", listTestimoni);


        List<ArtikelModel> listArtikel = artikelService.getListArtikel();
        if (listArtikel.size()>3) {
            listArtikel = listArtikel.subList(0, 3);
        }
        model.addAttribute("listArtikel", listArtikel);

        return "home";
    }

    @RequestMapping("/login")
    public String login() {
        if (userService.getListUser().size() == 0 || userService.getListUser() == null) {
            AdminModel admin = new AdminModel();
            admin.setNama("Admin");
            admin.setRole(Role.ADMIN);
            admin.setHp("08121202256");
            admin.setUsername("admin_prb");
            admin.setPassword("Test1234!");

            userService.addUser(admin);
        }

        return "login";
    }

    @GetMapping(value = "/logout")
    public ModelAndView logout(Principal principal) {
        UserModel user = userService.getUserByUsername(principal.getName());

        return new ModelAndView("redirect:" + Settings.SERVER_LOGOUT + Settings.CLIENT_LOGOUT);
    }

    @GetMapping(value = "/register")
    public String registrasiUser() {
        return  "manajemen-user/registrasi-user";
    }

    @RequestMapping("/user/viewall")
    private String manajemenUser(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        String username = user.getUsername();
        UserModel userModel = userService.getUserByUsername(username);

        if (userModel.getRole().equals(Role.ADMIN)) {
            return "manajemen-user/menu";
        }

        return "/home";
    }
}
