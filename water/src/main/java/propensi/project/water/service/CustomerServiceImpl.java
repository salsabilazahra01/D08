package propensi.project.water.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;
import java.util.Optional;

import propensi.project.water.model.User.CustomerModel;
import propensi.project.water.repository.User.CustomerDb;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerDb customerDb;

    public String encrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword;
    }

    @Override
    public void save(CustomerModel customerModel) {
        this.customerDb.save(customerModel);
    }

    @Override
    public void addCustomer(CustomerModel customer) {
        customer.setPassword(encrypt(customer.getPassword()));
        customerDb.save(customer);
    }

    @Override
    public List<CustomerModel> getListCustomer() {
        return customerDb.findAll();
    }

    @Override
    public CustomerModel getCustomerByUsername(String username) {
        return customerDb.findByUsername(username);
    }
}
