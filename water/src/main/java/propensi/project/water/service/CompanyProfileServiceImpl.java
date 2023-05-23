package propensi.project.water.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import propensi.project.water.model.CompanyProfile.CompanyProfileModel;
import propensi.project.water.model.CompanyProfile.TestimoniModel;
import propensi.project.water.repository.CompanyProfile.CompanyProfileDb;

@Service
public class CompanyProfileServiceImpl implements CompanyProfileService {
    @Autowired
    private CompanyProfileDb companyProfileDb;

    @Override
    public void add(CompanyProfileModel companyProfile) {
        companyProfileDb.save(companyProfile);
    }

    @Override
    public CompanyProfileModel update(CompanyProfileModel newCompanyProfile) {
        CompanyProfileModel companyProfile = getCompanyProfile("COMPROF");

        companyProfile.setMisiPerusahaan(newCompanyProfile.getMisiPerusahaan());
        companyProfile.setAlamatPerusahaan(newCompanyProfile.getAlamatPerusahaan());
        companyProfile.setVisiPerusahaan(newCompanyProfile.getVisiPerusahaan());
        companyProfile.setHpPerusahaan(newCompanyProfile.getHpPerusahaan());
        companyProfile.setEmailPerusahaan(newCompanyProfile.getEmailPerusahaan());
        companyProfile.setDeskripsiPerusahaan(newCompanyProfile.getDeskripsiPerusahaan());
        companyProfile.setNamaPerusahaan(newCompanyProfile.getNamaPerusahaan());

        return companyProfileDb.save(companyProfile);
    }

    @Override
    public CompanyProfileModel getCompanyProfile(String id) {
        Optional<CompanyProfileModel> companyProfile = companyProfileDb.findByIdCompanyProfile(id);
        if (companyProfile.isPresent()) {
            return companyProfile.get();
        }

        else {
            return null;
        }
    }

//    @Override
//    public CompanyProfileModel getCompanyProfile() {
//        return companyProfileDb.findByNamaPerusahaan("water");
//    }

    @Override
    public Boolean checkDb() {
        List<CompanyProfileModel> companyProfile = companyProfileDb.findAll();
        if (companyProfile.isEmpty()) {
            return true;
        }

        return false;
    }

    @Override
    public CompanyProfileModel setListTestimoni(List<TestimoniModel> listTestimoni) {
        CompanyProfileModel companyProfileTemp = getCompanyProfile("COMPROF");
        companyProfileTemp.setListTestimoni(new ArrayList<>());
        companyProfileTemp.setListTestimoni(listTestimoni);

        return companyProfileDb.save(companyProfileTemp);
    }
}
