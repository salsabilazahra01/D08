package propensi.project.water.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import propensi.project.water.dto.UpdateProfileDTO;
import propensi.project.water.model.User.*;
import propensi.project.water.service.CustomerService;
import propensi.project.water.service.DonaturService;
import propensi.project.water.service.PartnerService;
import propensi.project.water.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Qualifier("userServiceImpl")
    @Autowired
    private UserService userService;

    @Qualifier("donaturServiceImpl")
    @Autowired
    private DonaturService donaturService;

    @Qualifier("partnerServiceImpl")
    @Autowired
    private PartnerService partnerService;

    @Qualifier("customerServiceImpl")
    @Autowired
    private CustomerService customerService;

    @GetMapping("/profile")
    public String viewProfile(Model model, Principal principal, HttpServletRequest request) {
        UserModel userModel = userService.getUserByUsername(principal.getName());

        model.addAttribute("auth", userModel);

        if (userModel.getRole().equals(Role.DONATUR)) {
            DonaturModel donaturModel = this.donaturService.getDonaturByUsername(userModel.getUsername());
            model.addAttribute("added", donaturModel);
            model.addAttribute("point", donaturModel.getPoin() == null ? 0 : donaturModel.getPoin());
        } else if (userModel.getRole().equals(Role.CUSTOMER)) {
            CustomerModel customerModel = this.customerService.getCustomerByUsername(userModel.getUsername());
            model.addAttribute("added", customerModel);
        } else if (userModel.getRole().equals(Role.PARTNER)) {
            PartnerModel partnerModel = this.partnerService.getPartnerByUsername(userModel.getUsername());
            model.addAttribute("added", partnerModel);
        }

        UserModel user = userService.getUserByUsername(request.getRemoteUser()) == null ?
                null : userService.getUserByUsername(request.getRemoteUser());
        if (user!=null) {
            if (user.getRole().toString().equals("DONATUR")) {
                DonaturModel donatur = (DonaturModel) user;
                model.addAttribute("poin", donatur.getPoin());
            }
        }

        return "user/profile";
    }

    @GetMapping("/profile/edit")
    public String viewEditProfile(Model model, Principal principal) {
        UserModel userModel = userService.getUserByUsername(principal.getName());

        model.addAttribute("auth", userModel);

        if (userModel.getRole().equals(Role.DONATUR)) {
            DonaturModel donaturModel = this.donaturService.getDonaturByUsername(userModel.getUsername());
            model.addAttribute("added", donaturModel);
        } else if (userModel.getRole().equals(Role.CUSTOMER)) {
            CustomerModel customerModel = this.customerService.getCustomerByUsername(userModel.getUsername());
            model.addAttribute("pic", "customer");
            model.addAttribute("added", customerModel);
        } else if (userModel.getRole().equals(Role.PARTNER)) {
            PartnerModel partnerModel = this.partnerService.getPartnerByUsername(userModel.getUsername());
            model.addAttribute("pic", "partner");
            model.addAttribute("added", partnerModel);
        }

        if (userModel!=null) {
            if (userModel.getRole().toString().equals("DONATUR")) {
                DonaturModel donatur = (DonaturModel) userModel;
                model.addAttribute("poin", donatur.getPoin());
            }
        }

        return "user/edit";
    }

    @PostMapping("/profile/edit")
    public String editProfile(Principal principal,
                              @ModelAttribute UpdateProfileDTO updateProfileDTO,
                              HttpServletRequest request,
                              Model model) {

        UserModel userModel = userService.getUserByUsername(principal.getName());

        userModel.setEmail(updateProfileDTO.getEmail());
        userModel.setNama(updateProfileDTO.getFname());

        this.userService.saveUser(userModel);

        if (userModel.getRole().equals(Role.DONATUR)) {
            DonaturModel donaturModel = this.donaturService.getDonaturByUsername(userModel.getUsername());
            donaturModel.setAlamat(updateProfileDTO.getAlamat());
            donaturModel.setKota(updateProfileDTO.getKota());
            donaturModel.setKecamatan(updateProfileDTO.getKecamatan());
            donaturModel.setKelurahan(updateProfileDTO.getKelurahan());
            donaturModel.setProvinsi(updateProfileDTO.getProvinsi());
            donaturModel.setKodePos(updateProfileDTO.getKodePos());
            this.donaturService.save(donaturModel);
        } else if (userModel.getRole().equals(Role.CUSTOMER)) {
            CustomerModel customerModel = this.customerService.getCustomerByUsername(userModel.getUsername());
            customerModel.setAlamat(updateProfileDTO.getAlamat());
            customerModel.setKota(updateProfileDTO.getKota());
            customerModel.setKecamatan(updateProfileDTO.getKecamatan());
            customerModel.setKelurahan(updateProfileDTO.getKelurahan());
            customerModel.setProvinsi(updateProfileDTO.getProvinsi());
            customerModel.setKodePos(updateProfileDTO.getKodePos());
            customerModel.setNamaPic(updateProfileDTO.getNamaPic());
            this.customerService.save(customerModel);
        } else if (userModel.getRole().equals(Role.PARTNER)) {
            PartnerModel partnerModel = this.partnerService.getPartnerByUsername(userModel.getUsername());
            partnerModel.setAlamat(updateProfileDTO.getAlamat());
            partnerModel.setKota(updateProfileDTO.getKota());
            partnerModel.setKecamatan(updateProfileDTO.getKecamatan());
            partnerModel.setKelurahan(updateProfileDTO.getKelurahan());
            partnerModel.setProvinsi(updateProfileDTO.getProvinsi());
            partnerModel.setKodePos(updateProfileDTO.getKodePos());
            partnerModel.setNamaPic(updateProfileDTO.getNamaPic());
            this.partnerService.save(partnerModel);
        }

        if (userModel!=null) {
            if (userModel.getRole().toString().equals("DONATUR")) {
                DonaturModel donatur = (DonaturModel) userModel;
                model.addAttribute("poin", donatur.getPoin());
            }
        }
        return "redirect:/user/profile";
    }

}
