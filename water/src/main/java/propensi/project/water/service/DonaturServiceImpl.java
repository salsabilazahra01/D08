package propensi.project.water.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;
import java.util.Optional;

import propensi.project.water.model.User.DonaturModel;
import propensi.project.water.repository.User.DonaturDb;

@Service
public class DonaturServiceImpl implements DonaturService {
    @Autowired
    private DonaturDb donaturDb;

    public String encrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword;
    }

    @Override
    public void save(DonaturModel donaturModel) {
        this.donaturDb.save(donaturModel);
    }

    @Override
    public void addDonatur(DonaturModel donatur) {
        donatur.setPassword(encrypt(donatur.getPassword()));
        donatur.setPoin(0);
        donaturDb.save(donatur);
    }

    @Override
    public List<DonaturModel> getListDonatur() {
        return donaturDb.findAll();
    }

    @Override
    public DonaturModel getDonaturByUsername(String username) {
        return donaturDb.findByUsername(username);
    }

    @Override
    public DonaturModel updatePoin(DonaturModel updatedDonatur) {
        DonaturModel donatur = getDonaturByUsername(updatedDonatur.getUsername());
        donatur.setPoin(updatedDonatur.getPoin());
        return donaturDb.save(donatur);
    }
}
