package propensi.project.water.repository.User;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import propensi.project.water.model.User.TeknisiModel;

@Repository
public interface TeknisiDb extends JpaRepository<TeknisiModel, String> {
    TeknisiModel findByUsername(String username);
    List<TeknisiModel> findAll();
    void delete(TeknisiModel teknisi);
}
