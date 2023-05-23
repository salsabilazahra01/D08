package propensi.project.water.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

import propensi.project.water.model.User.ManajerModel;
import propensi.project.water.repository.User.ManajerDb;

@Service
public class ManajerServiceImpl implements ManajerService {
    @Autowired
    private ManajerDb manajerDb;

    @Override
    public ManajerModel getManajerByUsername(String username) {
        return manajerDb.findByUsername(username);
    }

    public String encrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword;
    }

    @Override
    public List<ManajerModel> getListManajer() {
        return manajerDb.findAll();
    }

    @Override
    public void addManajer(ManajerModel manajer) {
        manajer.setPassword(encrypt(manajer.getPassword()));
        manajerDb.save(manajer);
    }
}
