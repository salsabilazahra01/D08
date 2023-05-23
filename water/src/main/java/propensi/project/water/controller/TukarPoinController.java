package propensi.project.water.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import propensi.project.water.model.PoinReward.RewardModel;
import propensi.project.water.model.PoinReward.RewardTukarPoinModel;
import propensi.project.water.model.PoinReward.TukarPoinModel;
import propensi.project.water.model.Transaksi.ProsesLainModel;
import propensi.project.water.model.Transaksi.ProsesTukarPoinModel;
import propensi.project.water.model.User.DonaturModel;
import propensi.project.water.model.User.UserModel;
import propensi.project.water.service.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/penukaran-poin")
@AllArgsConstructor
public class TukarPoinController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final TukarPoinService tukarPoinService;
    @Autowired
    private final RewardService rewardService;
    @Autowired
    private final DonaturService donaturService;
    @Autowired
    private final TransaksiService transaksiService;

    @GetMapping(value="/viewall/{status}")
    private String viewAllTukarPoin(Model model,
                                          @PathVariable(name="status") String status,
                                          @RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "5") int size,
                                          HttpServletRequest request) {

        try{
            Integer statusInt = Integer.parseInt(status);
            Pageable paging = PageRequest.of(page - 1, size);

            //get donatur if exists
            DonaturModel donatur = getDonatur(request);

            Page<TukarPoinModel> pageTukarPoin =
                    tukarPoinService.findAllByDonatur(paging, donatur, statusInt);

            List<TukarPoinModel> listPageTukarPoin = pageTukarPoin.getContent();

            Integer firstItem = (pageTukarPoin.getNumber() + 1)*size-size+1;
            Integer lastItem = firstItem + listPageTukarPoin.size() - 1;


            if (donatur!=null) {
                model.addAttribute("poin", donatur.getPoin());
            }

            model.addAttribute("currentPage", pageTukarPoin.getNumber() + 1);
            model.addAttribute("firstItem", firstItem);
            model.addAttribute("lastItem", lastItem);
            model.addAttribute("totalItems", pageTukarPoin.getTotalElements());
            model.addAttribute("totalPages", pageTukarPoin.getTotalPages());
            model.addAttribute("pageSize", size);
            model.addAttribute("listTukarPoin", listPageTukarPoin);
            model.addAttribute("status", statusInt);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "/donasi/penukaran-poin/index";
    }

    @GetMapping(value="/add")
    private String addTukarPoin(Model model, HttpServletRequest request) {

        //get donatur if exists
        DonaturModel donatur = getDonatur(request);

        TukarPoinModel tukarPoin = new TukarPoinModel();
        List<RewardModel> listRewardEx = getListRewardEx(donatur.getPoin());

        //create new list reward
        List<RewardTukarPoinModel> listRewardTukarPoin = new ArrayList<>();
        tukarPoin.setListReward(listRewardTukarPoin);
        tukarPoin.getListReward().add(new RewardTukarPoinModel());

        //input donatur info to tukar poin
        if(donatur != null){
            tukarPoin = tukarPoinService.setDonaturInfo(donatur, tukarPoin);
            model.addAttribute("poin", donatur.getPoin());
        }

        Integer poinAvailable = donatur.getPoin() - getTotalPoinInProcess(donatur.getListTukarPoin());

        model.addAttribute("poinAvailable", poinAvailable);
        model.addAttribute("tukarPoin", tukarPoin);
        model.addAttribute("listRewardEx", listRewardEx);
        return "/donasi/penukaran-poin/add-tukar-poin";
    }

    @PostMapping(value="/add", params = {"save"})
    private String addTukarPoinSubmit(@ModelAttribute TukarPoinModel tukarPoin,
                                      Model model,
                                      RedirectAttributes redirectAttrs,
                                      HttpServletRequest request) {

        List<RewardTukarPoinModel> listReward = tukarPoin.getListReward();

        //get customer if exists
        DonaturModel donatur =  getDonatur(request);

        //check validation
        if(kontakEmpty(tukarPoin)){
            redirectAttrs.addFlashAttribute("failed",
                    "E-mail atau nomor telepon harus diisi");
            return "redirect:/penukaran-poin/add";
        }
        else if(isDuplicate(listReward)){
            redirectAttrs.addFlashAttribute("failed",
                    "Penukaran poin tidak dapat dibuat karena terdapat duplikasi reward");
            return "redirect:/penukaran-poin/add";
        }
        else if(itemValueInvalid(listReward)){
            redirectAttrs.addFlashAttribute("failed",
                    "Penukaran poin tidak dapat dibuat karena terdapat jenis yang tidak valid");
            return "redirect:/penukaran-poin/add";
        } else if(quantityExceeded(listReward, request) >= 0){
            redirectAttrs.addFlashAttribute("failed",
                    String.format("Total poin ditukar tidak boleh melebihi %s", quantityExceeded(listReward, request)));
            return "redirect:/penukaran-poin/add";
        }

        tukarPoinService.add(tukarPoin);

        if(donatur != null){
            model.addAttribute("poin", donatur.getPoin());
        }
        redirectAttrs.addFlashAttribute("success",
                String.format("Penukaran poin baru dengan id %s berhasil dibuat",
                         tukarPoin.getIdTukarPoin()));

        model.addAttribute("tukarPoin", tukarPoin.getIdTukarPoin());
        return "redirect:/penukaran-poin/viewall/-1";
    }

    @PostMapping(value="/add", params = {"addRowItem"})
    private String addRowAddForm(@ModelAttribute TukarPoinModel tukarPoin,
                                 Model model,
                                 HttpServletRequest request){

        DonaturModel donatur =  getDonatur(request);
        List<RewardModel> listRewardEx = getListRewardEx(donatur.getPoin());

        //get current list item
        List<RewardTukarPoinModel> listRewardTukarPoin = tukarPoin.getListReward();

        //create new list
        if(listRewardTukarPoin == null || listRewardTukarPoin.size() == 0){
            tukarPoin.setListReward(new ArrayList<>());
        }
        listRewardTukarPoin.add(new RewardTukarPoinModel());

        Integer poinAvailable = donatur.getPoin() - getTotalPoinInProcess(donatur.getListTukarPoin());

        model.addAttribute("poinAvailable", poinAvailable);
        model.addAttribute("tukarPoin", tukarPoin);
        model.addAttribute("listRewardEx", listRewardEx);

        return "/donasi/penukaran-poin/add-tukar-poin";
    }

    @PostMapping(value = "/add", params = {"deleteRowItem"})
    private String deleteRowAddForm(@ModelAttribute TukarPoinModel tukarPoin,
                                    @RequestParam("deleteRowItem") Integer row,
                                    Model model,
                                    HttpServletRequest request){

        DonaturModel donatur =  getDonatur(request);

        final Integer rowId = Integer.valueOf(row);

        tukarPoin.getListReward().remove(rowId.intValue());
        List<RewardModel> listRewardEx = getListRewardEx(donatur.getPoin());
        Integer poinAvailable = donatur.getPoin() - getTotalPoinInProcess(donatur.getListTukarPoin());

        model.addAttribute("poinAvailable", poinAvailable);
        model.addAttribute("tukarPoin", tukarPoin);
        model.addAttribute("listRewardEx", listRewardEx);

        return "/donasi/penukaran-poin/add-tukar-poin";
    }

    @GetMapping(value="/view/{idTukarPoin}")
    private String viewTukarPoin(Model model, @PathVariable String idTukarPoin, HttpServletRequest request) {

        TukarPoinModel tukarPoin = tukarPoinService.findById(idTukarPoin);
        List<RewardTukarPoinModel> listReward = tukarPoinService.getListRewardById(idTukarPoin);

        int totalPoin = 0;
        for(int i = 0; i < listReward.size(); i++){
            totalPoin += listReward.get(i).getIdReward().getPoin()*listReward.get(i).getKuantitas();
        }

        int totalKuantitas = 0;
        for(int i = 0; i < listReward.size(); i++){
            totalKuantitas += listReward.get(i).getKuantitas();
        }

        DonaturModel donatur = getDonatur(request);
        if(donatur != null){
            model.addAttribute("poin", donatur.getPoin());
        }

        model.addAttribute("totalPoin", totalPoin);
        model.addAttribute("totalKuantitas", totalKuantitas);
        model.addAttribute("tukarPoin", tukarPoin);
        model.addAttribute("listReward", listReward);
        return "/donasi/penukaran-poin/view-tukar-poin";
    }

    @GetMapping(value = "/delete/{idTukarPoin}")
    private String deleteTukarPoin(@PathVariable String idTukarPoin,
                                   RedirectAttributes redirectAttrs,
                                   HttpServletRequest request,
                                   Model model){

        TukarPoinModel tukarPoin = tukarPoinService.findById(idTukarPoin);
        tukarPoinService.delete(tukarPoin);

        DonaturModel donatur = getDonatur(request);
        if(donatur != null){
            model.addAttribute("poin", donatur.getPoin());
        }

        redirectAttrs.addFlashAttribute("success",
                String.format("Penukaran poin dengan id %s berhasil dibatalkan",
                        tukarPoin.getIdTukarPoin()));

        return "redirect:/penukaran-poin/viewall/-1";
    }

    @PostMapping(value = "/update-status")
    private String updateStatusTukarPoin(@ModelAttribute TukarPoinModel tukarPoin, RedirectAttributes redirectAttrs,
                                         Model model, HttpServletRequest request,
                                         @RequestParam(name="filePengiriman", required = false) MultipartFile filePengiriman,
                                         @RequestParam(name="fileTransaksi", required = false) MultipartFile fileTransaksi)
                                         throws IOException {

        TukarPoinModel tukarPoinEx = tukarPoinService.findById(tukarPoin.getIdTukarPoin());
        List<RewardTukarPoinModel> listReward = tukarPoinService.getListRewardById(tukarPoin.getIdTukarPoin());

        //approval & status
        Boolean isApproved = tukarPoin.getKeteranganTolak() == null;
        Integer status = tukarPoinEx.getStatus();

        if(isApproved){
            if(status == 1){
                integrateTransaksi(tukarPoinEx, fileTransaksi);
                if(filePengiriman != null){
                    String fileName = setFileBuktiKirim(filePengiriman, tukarPoinEx);
                    tukarPoinEx.setBuktiKirim(fileName);
                }
            } else if(status==2) {
                integratePoinDonatur(tukarPoinEx, request);
            }
            tukarPoinEx.setStatus(tukarPoinEx.getStatus()+1);
        } else {
            if(status == 0){
                tukarPoinEx.setKeteranganTolak(tukarPoin.getKeteranganTolak());
            }
            tukarPoinEx.setStatus(4);
        }
        tukarPoinService.update(tukarPoinEx);

        redirectAttrs.addFlashAttribute("success",
                String.format("Status penukaran poin dengan id %s berhasil diperbarui",
                        tukarPoin.getIdTukarPoin()));

        model.addAttribute("listReward", listReward);
        model.addAttribute("listItem", listReward);
        model.addAttribute("penawaranOlahan", tukarPoinEx);
        return "redirect:/penukaran-poin/view/" + tukarPoinEx.getIdTukarPoin();
    }

    private void integrateTransaksi(TukarPoinModel tukarPoin, MultipartFile file) throws IOException {
        List<RewardTukarPoinModel> listReward = tukarPoinService.getListRewardById(tukarPoin.getIdTukarPoin());
        
        //update warehouse
        int totalHarga = 0;
        for(RewardTukarPoinModel reward: listReward){
            totalHarga += reward.getIdReward().getHarga() * reward.getKuantitas();
        }
        
        //file bukti
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        //add new transaksi
        ProsesTukarPoinModel transaksi = transaksiService.addTransaksiTukarPoin(fileName, tukarPoin, totalHarga);
        
        //save file bukti
        String uploadDir = "./src/main/resources/static/images/" + transaksi.getIdTransaksi() + "-" + transaksi.getBukti();
        FileUploadUtil.saveFile(uploadDir, fileName, file);

        //update transaksi
        TukarPoinModel tukarPoinEx = tukarPoinService.findById(tukarPoin.getIdTukarPoin());
        tukarPoinEx.setTransaksiTukarPoin(transaksi);
        tukarPoinService.update(tukarPoinEx);
    }

    private void integratePoinDonatur(TukarPoinModel tukarPoinEx, HttpServletRequest request) {
        List<RewardTukarPoinModel> listReward = tukarPoinService.getListRewardById(tukarPoinEx.getIdTukarPoin());

        Integer totalPoinDitukar = 0;
        for(RewardTukarPoinModel reward : listReward){
            totalPoinDitukar += reward.getIdReward().getPoin() * reward.getKuantitas();
        }

        DonaturModel donatur = getDonatur(request);
        Integer updatedPoin = donatur.getPoin() - totalPoinDitukar;
        donatur.setPoin(updatedPoin);

        donaturService.updatePoin(donatur);
    }

    private DonaturModel getDonatur(HttpServletRequest request) {
        return donaturService.getDonaturByUsername(request.getRemoteUser()) == null ?
                null : donaturService.getDonaturByUsername(request.getRemoteUser());
    }

    private List<RewardModel> getListRewardEx(Integer poinDonatur) {
        List<RewardModel> rewards = rewardService.findAll();
        rewards.removeIf(reward -> (reward.getPoin() > poinDonatur));
        return rewards;
    }

    private boolean isDuplicate(List<RewardTukarPoinModel> listReward) {
        final Set<RewardModel> set1 = new HashSet<>();

        if (!listReward.isEmpty() || listReward == null) {
            for (RewardTukarPoinModel reward : listReward) {
                if (!set1.add(reward.getIdReward())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean itemValueInvalid(List<RewardTukarPoinModel> listReward) {
        for(RewardTukarPoinModel reward:listReward){
            if(reward.getIdReward() == null){
                return true;
            }
        }
        return false;
    }

    private boolean kontakEmpty(TukarPoinModel tukarPoin) {
        return tukarPoin.getEmail().isEmpty() && tukarPoin.getHp().isEmpty();
    }

    private Integer quantityExceeded(List<RewardTukarPoinModel> listReward, HttpServletRequest request) {

        DonaturModel donatur = getDonatur(request);

        //total poin in process
        List<TukarPoinModel> allTukarPoin = donatur.getListTukarPoin();
        Integer totalPoinProcessed = getTotalPoinInProcess(allTukarPoin);

        //total poin new added
        Integer totalPoinNewAdded = getTotalPoin(listReward);

        if(totalPoinNewAdded > donatur.getPoin() - totalPoinProcessed ){
            return donatur.getPoin() - totalPoinProcessed;
        }
        return -1;
    }

    private Integer getTotalPoinInProcess(List<TukarPoinModel> allTukarPoin){
        Integer totalPoinProcessed = 0;
        for(TukarPoinModel tukarPoinDonatur : allTukarPoin){
            if(tukarPoinDonatur.getStatus() < 3){
                List<RewardTukarPoinModel> listRewardTukarPoin =
                        tukarPoinService.getListRewardById(tukarPoinDonatur.getIdTukarPoin());
                totalPoinProcessed += getTotalPoin(listRewardTukarPoin);
            }
        }
        return totalPoinProcessed;
    }

    private Integer getTotalPoin(List<RewardTukarPoinModel> listReward){
        Integer totalPoin = 0;
        for(RewardTukarPoinModel reward : listReward) {
            totalPoin += reward.getIdReward().getPoin() * reward.getKuantitas();
        }
        return totalPoin;
    }

    private String setFileBuktiKirim(MultipartFile filePengiriman, TukarPoinModel tukarPoinEx) throws IOException {

        //file bukti
        String fileName = StringUtils.cleanPath(filePengiriman.getOriginalFilename());

        //save file bukti
        String uploadDir = "./src/main/resources/static/images/" +
                tukarPoinEx.getIdTukarPoin() + "-" + fileName;
        FileUploadUtil.saveFile(uploadDir, fileName, filePengiriman);

        return fileName;
    }

}
