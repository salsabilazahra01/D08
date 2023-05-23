package propensi.project.water.service;

import propensi.project.water.model.CompanyProfile.CompanyProfileModel;
import propensi.project.water.model.CompanyProfile.TestimoniModel;

import java.util.List;

public interface TestimoniService {
    void add(TestimoniModel testimoni);
    void delete(TestimoniModel testimoni);
    TestimoniModel getTestimoni(Integer id);
    void deleteAllTestimoni(List<TestimoniModel> listTestimoni);
    List<TestimoniModel> findAll();
    TestimoniModel getTestimoniByNamaPembuat(String nama);
}
