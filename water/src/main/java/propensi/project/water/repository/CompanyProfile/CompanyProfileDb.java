package propensi.project.water.repository.CompanyProfile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import propensi.project.water.controller.CompanyProfileController;
import propensi.project.water.model.CompanyProfile.CompanyProfileModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyProfileDb extends JpaRepository<CompanyProfileModel, String> {
    Optional<CompanyProfileModel> findByIdCompanyProfile(String id);
//    CompanyProfileModel findByIdCompanyProfile(String id);
}
