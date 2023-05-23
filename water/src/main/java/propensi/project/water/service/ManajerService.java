package propensi.project.water.service;

import propensi.project.water.model.User.ManajerModel;

import java.util.List;

public interface ManajerService {
    public ManajerModel getManajerByUsername(String username);

    public void addManajer(ManajerModel manajer);

    public String encrypt(String password);

    public List<ManajerModel> getListManajer();
}
