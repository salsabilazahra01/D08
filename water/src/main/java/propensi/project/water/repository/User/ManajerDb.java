package propensi.project.water.repository.User;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import propensi.project.water.model.User.ManajerModel;

@Repository
public interface ManajerDb extends JpaRepository<ManajerModel, String> {
    ManajerModel findByUsername(String username);
    List<ManajerModel> findAll();
    void delete(ManajerModel manajer);
}
