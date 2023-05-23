package propensi.project.water.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

import propensi.project.water.model.User.TeknisiModel;
import propensi.project.water.repository.User.TeknisiDb;

@Service
public class TeknisiServiceImpl implements TeknisiService {
    @Autowired
    private TeknisiDb teknisiDb;

    public String encrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword;
    }

    @Override
    public void addTeknisi(TeknisiModel teknisi) {
        teknisi.setPassword(encrypt(teknisi.getPassword()));
        teknisiDb.save(teknisi);
    }

    @Override
    public List<TeknisiModel> getListTeknisi() {
        return teknisiDb.findAll();
    }

    @Override
    public TeknisiModel getTeknisiByUsername(String username) {
        return teknisiDb.findByUsername(username);
    }
}
