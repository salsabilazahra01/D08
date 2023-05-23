package propensi.project.water.service;

import propensi.project.water.model.User.TeknisiModel;

import java.util.List;

public interface TeknisiService {
    public void addTeknisi(TeknisiModel teknisi);

    public List<TeknisiModel> getListTeknisi();

    public TeknisiModel getTeknisiByUsername(String username);

    public String encrypt(String password);
}
