package propensi.project.water.service;

import propensi.project.water.model.User.SupervisorModel;

import java.util.List;

public interface SupervisorService {
    public void addSupervisor(SupervisorModel supervisor);

    public List<SupervisorModel> getListSupervisor();

    public SupervisorModel getSupervisorByUsername(String username);

    public String encrypt(String password);
}
