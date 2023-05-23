package propensi.project.water.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import propensi.project.water.model.User.CustomerModel;
import propensi.project.water.model.User.SupervisorModel;
import propensi.project.water.repository.User.CustomerDb;
import propensi.project.water.repository.User.SupervisorDb;

import java.util.List;

@Service
public class SupervisorServiceImpl implements SupervisorService {
    @Autowired
    private SupervisorDb supervisorDb;

    public String encrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword;
    }

    @Override
    public void addSupervisor(SupervisorModel supervisor) {
        supervisor.setPassword(encrypt(supervisor.getPassword()));
        supervisorDb.save(supervisor);
    }

    @Override
    public List<SupervisorModel> getListSupervisor() {
        return supervisorDb.findAll();
    }

    @Override
    public SupervisorModel getSupervisorByUsername(String username) {
        return supervisorDb.findByUsername(username);
    }
}
