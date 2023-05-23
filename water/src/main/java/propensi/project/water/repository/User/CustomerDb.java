package propensi.project.water.repository.User;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import propensi.project.water.model.User.CustomerModel;

@Repository
public interface CustomerDb extends JpaRepository<CustomerModel, String> {
    CustomerModel findByUsername(String username);
    List<CustomerModel> findAll();
    void delete(CustomerModel customer);
}
