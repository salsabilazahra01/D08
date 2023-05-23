package propensi.project.water.repository.CompanyProfile;


import org.aspectj.weaver.ast.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import propensi.project.water.model.CompanyProfile.TestimoniModel;
import propensi.project.water.model.CompanyProfile.CompanyProfileModel;

import java.util.Optional;

@Repository
public interface TestimoniDb extends JpaRepository<TestimoniModel, String> {
    Optional<TestimoniModel> findByIdTestimoni(Integer id);
//    void deleteAll(CompanyProfileModel companyProfile);
    TestimoniModel findTestimoniModelByNamaPembuatTestimoni(String nama);
}
