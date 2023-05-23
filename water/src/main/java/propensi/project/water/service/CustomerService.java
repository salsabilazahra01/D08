package propensi.project.water.service;

import java.util.List;

import propensi.project.water.model.User.CustomerModel;
import propensi.project.water.model.User.UserModel;

public interface CustomerService {
    public void addCustomer(CustomerModel customer);

    public List<CustomerModel> getListCustomer();

    public CustomerModel getCustomerByUsername(String username);

    public String encrypt(String password);

    void save(CustomerModel customerModel);
}
