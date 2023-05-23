package propensi.project.water.repository.User;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import propensi.project.water.model.User.PekerjaModel;

@Repository
public interface PekerjaDb extends JpaRepository<PekerjaModel, String> {
    PekerjaModel findByUsername(String username);
    List<PekerjaModel> findAll();
    void delete(PekerjaModel pekerja);
}
