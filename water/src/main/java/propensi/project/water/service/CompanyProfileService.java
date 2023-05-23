package propensi.project.water.service;

import propensi.project.water.model.CompanyProfile.CompanyProfileModel;
import propensi.project.water.model.CompanyProfile.TestimoniModel;

import java.util.List;
import java.util.Optional;

public interface CompanyProfileService {
    void add(CompanyProfileModel companyProfile);
    CompanyProfileModel update(CompanyProfileModel companyProfile);
    CompanyProfileModel getCompanyProfile(String id);
//    CompanyProfileModel getCompanyProfile();
    Boolean checkDb();
    CompanyProfileModel setListTestimoni(List<TestimoniModel> listTestimoni);
}
