package propensi.project.water.service;

import propensi.project.water.model.User.PekerjaModel;

import java.util.List;

public interface PekerjaService {
    public void addPekerja(PekerjaModel pekerja);

    public List<PekerjaModel> getListPekerja();

    public PekerjaModel getPekerjaByUsername(String username);

    public String encrypt(String password);
}
