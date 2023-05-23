package propensi.project.water.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import propensi.project.water.model.User.CustomerModel;
import propensi.project.water.model.User.PekerjaModel;
import propensi.project.water.repository.User.CustomerDb;
import propensi.project.water.repository.User.PekerjaDb;

import java.util.List;
import java.util.Optional;

@Service
public class PekerjaServiceImpl implements PekerjaService {
    @Autowired
    private PekerjaDb pekerjaDb;

    public String encrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword;
    }

    @Override
    public void addPekerja(PekerjaModel pekerja) {
        pekerja.setPassword(encrypt(pekerja.getPassword()));
        pekerjaDb.save(pekerja);
    }

    @Override
    public List<PekerjaModel> getListPekerja() {
        return pekerjaDb.findAll();
    }

    @Override
    public PekerjaModel getPekerjaByUsername(String username) {
        return pekerjaDb.findByUsername(username);
    }
}
