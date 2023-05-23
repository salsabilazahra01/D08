package propensi.project.water.service;

import java.util.List;

import propensi.project.water.model.User.PartnerModel;

public interface PartnerService {
    public void addPartner(PartnerModel partner);

    public List<PartnerModel> getListPartner();

    public PartnerModel getPartnerByUsername(String username);

    public String encrypt(String password);

    void save(PartnerModel partnerModel);
}
