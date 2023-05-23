package propensi.project.water.repository.User;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import propensi.project.water.model.User.PartnerModel;

@Repository
public interface PartnerDb extends JpaRepository<PartnerModel, String> {
    PartnerModel findByUsername(String username);
    List<PartnerModel> findAll();
    void delete(PartnerModel partner);
}
