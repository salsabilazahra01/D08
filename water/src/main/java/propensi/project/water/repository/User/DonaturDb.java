package propensi.project.water.repository.User;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import propensi.project.water.model.User.DonaturModel;

@Repository
public interface DonaturDb extends JpaRepository<DonaturModel, String> {
    DonaturModel findByUsername(String username);
    List<DonaturModel> findAll();
    void delete(DonaturModel donatur);
}
