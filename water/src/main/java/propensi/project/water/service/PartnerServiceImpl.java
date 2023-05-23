package propensi.project.water.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;
import java.util.Optional;

import propensi.project.water.model.User.PartnerModel;
import propensi.project.water.repository.User.PartnerDb;

@Service
public class PartnerServiceImpl implements PartnerService {
    @Autowired
    private PartnerDb partnerDb;

    public String encrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword;
    }

    @Override
    public void save(PartnerModel partnerModel) {
        this.partnerDb.save(partnerModel);
    }

    @Override
    public void addPartner(PartnerModel partner) {
        partner.setPassword(encrypt(partner.getPassword()));
        partnerDb.save(partner);
    }

    @Override
    public List<PartnerModel> getListPartner() {
        return partnerDb.findAll();
    }

    @Override
    public PartnerModel getPartnerByUsername(String username) {
        return partnerDb.findByUsername(username);
    }
}
