package propensi.project.water.service;

import org.aspectj.weaver.ast.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.web.servlet.oauth2.resourceserver.OpaqueTokenDsl;
import org.springframework.stereotype.Service;
import propensi.project.water.model.CompanyProfile.CompanyProfileModel;
import propensi.project.water.model.CompanyProfile.TestimoniModel;
import propensi.project.water.repository.CompanyProfile.TestimoniDb;

import java.util.List;
import java.util.Optional;

@Service
public class TestimoniServiceImpl implements TestimoniService {
    @Autowired
    private TestimoniDb testimoniDb;

    @Override
    public void add(TestimoniModel testimoni) {
        if (testimoni.getIdTestimoni() != null) {
            TestimoniModel testimoniTemp = getTestimoni(testimoni.getIdTestimoni());
            testimoniTemp.setNamaPembuatTestimoni(testimoni.getNamaPembuatTestimoni());
            testimoniTemp.setRole(testimoni.getRole());
            testimoniTemp.setReview(testimoni.getReview());
            testimoniTemp.setCompanyProfile(testimoni.getCompanyProfile());

            testimoniDb.save(testimoniTemp);
        }
        else {
            testimoniDb.save(testimoni);
        }
    }

    @Override
    public void delete(TestimoniModel testimoni) {
        testimoniDb.delete(testimoni);
    }

    @Override
    public TestimoniModel getTestimoni(Integer id) {
        Optional<TestimoniModel> testimoni = testimoniDb.findByIdTestimoni(id);
        return testimoni.get();
    }

    @Override
    public void deleteAllTestimoni(List<TestimoniModel> listTestimoni) {
        for (TestimoniModel testimoni : listTestimoni) {
            testimoniDb.delete(testimoni);
        }
    }

    @Override
    public List<TestimoniModel> findAll() {
        return testimoniDb.findAll();
    }

    @Override
    public TestimoniModel getTestimoniByNamaPembuat(String nama) {
        return testimoniDb.findTestimoniModelByNamaPembuatTestimoni(nama);
    }
}
