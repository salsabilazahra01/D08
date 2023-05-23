package propensi.project.water.controller;

import org.aspectj.weaver.ast.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import propensi.project.water.dto.RewardDTO;
import propensi.project.water.exceptions.donasi.RewardDuplicateJenisException;
import propensi.project.water.exceptions.donasi.RewardNotFoundException;
import propensi.project.water.model.CompanyProfile.CompanyProfileModel;
import propensi.project.water.model.CompanyProfile.TestimoniModel;
import propensi.project.water.model.PenjualanHasilOlahan.PenawaranOlahanModel;
import propensi.project.water.model.User.DonaturModel;
import propensi.project.water.model.User.UserModel;
import propensi.project.water.model.Warehouse.WarehouseModel;
import propensi.project.water.repository.CompanyProfile.CompanyProfileDb;
import propensi.project.water.service.CompanyProfileService;
import propensi.project.water.service.TestimoniService;
import propensi.project.water.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Controller
@RequestMapping("/about-us")
@AllArgsConstructor
public class CompanyProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyProfileService companyProfileService;

    @Autowired
    private TestimoniService testimoniService;

    @GetMapping("/view")
    private String viewCompanyProfile(Model model, HttpServletRequest request) {
        CompanyProfileModel companyProfile = companyProfileService.getCompanyProfile("COMPROF");
        List<TestimoniModel> listTestimoni = companyProfile.getListTestimoni();

        UserModel user = userService.getUserByUsername(request.getRemoteUser()) == null ?
                null : userService.getUserByUsername(request.getRemoteUser());
        if (user!=null) {
            if (user.getRole().toString().equals("DONATUR")) {
                DonaturModel donatur = (DonaturModel) user;
                model.addAttribute("poin", donatur.getPoin());
            }
        }

        model.addAttribute("companyProfile", companyProfile);
        model.addAttribute("listTestimoni", listTestimoni);
        return "/company-profile/view-company-profile";
    }

    @GetMapping(value = "/update")
    public String updateCompanyProfile(Model model) {
        CompanyProfileModel companyProfile =companyProfileService.getCompanyProfile("COMPROF");
        if (companyProfile.getListTestimoni() == null || companyProfile.getListTestimoni().size() == 0) {
            companyProfile.setListTestimoni(new ArrayList<>());
        }

        model.addAttribute("companyProfile", companyProfile);

        return "/company-profile/update-company-profile";
    }

    @PostMapping(value = "/update", params = {"addRow"})
    private String addRowTestimoni(@ModelAttribute CompanyProfileModel companyProfile, Model model) {
        if (companyProfile.getListTestimoni() == null || companyProfile.getListTestimoni().size() == 0) {
            companyProfile.setListTestimoni(new ArrayList<>());
        }
        companyProfile.getListTestimoni().add(new TestimoniModel());

        model.addAttribute("companyProfile", companyProfile);

        return "/company-profile/update-company-profile";
    }

    @PostMapping(value = "/update", params = {"deleteRow"})
    private String deleteRowTestimoni(@ModelAttribute CompanyProfileModel companyProfile, @RequestParam("deleteRow") Integer row, Model model) {
        final Integer rowId = Integer.valueOf(row);
        companyProfile.getListTestimoni().remove(rowId.intValue());

        model.addAttribute("companyProfile", companyProfile);

        return "/company-profile/update-company-profile";
    }

    @PostMapping(value = "/update", params = {"save"})
    private String submitCompanyProfile(@ModelAttribute CompanyProfileModel companyProfile, RedirectAttributes redirectAttrs, Model model) {
        CompanyProfileModel updatedCompanyProfile = companyProfileService.update(companyProfile);
        List<TestimoniModel> oldListTestimoni = testimoniService.findAll();

        //update new list testimoni for testimonidb
        List<TestimoniModel> listTestimoni = companyProfile.getListTestimoni();
        for (TestimoniModel testimoni : listTestimoni) {
            testimoni.setCompanyProfile(updatedCompanyProfile);
            testimoniService.add(testimoni);
        }
        //compare list testimoni
        List<Integer> namaPembuatOld = new ArrayList<>();
        List<Integer> namaPembuatNew = new ArrayList<>();
        if (oldListTestimoni.size() > listTestimoni.size()) {
            for (int i = 0; i < oldListTestimoni.size(); i++) {
                namaPembuatOld.add(oldListTestimoni.get(i).getIdTestimoni());
            }
            for (int j = 0; j < listTestimoni.size(); j++) {
                namaPembuatNew.add(listTestimoni.get(j).getIdTestimoni());
            }

            List<Integer> difference = new ArrayList<>(namaPembuatOld);
            difference.removeAll(namaPembuatNew);

            for (Integer id : difference) {
                testimoniService.delete(testimoniService.getTestimoni(id));
            }
        }

        //update new list testimoni for company profile
        CompanyProfileModel saveCompanyProfile = companyProfileService.setListTestimoni(testimoniService.findAll());

        redirectAttrs.addFlashAttribute("success",
                String.format("Company profile berhasil di-update"));

        model.addAttribute("companyProfile", saveCompanyProfile);
        return "redirect:/about-us/view";
    }
}
