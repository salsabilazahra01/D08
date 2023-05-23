package propensi.project.water.service;

import propensi.project.water.model.User.UserModel;

import java.util.List;

public interface UserService {
    UserModel addUser(UserModel user);
    public String encrypt(String password);
    public UserModel getUserByUsername(String username);
    List<UserModel> getListUser();
    UserModel getUserById(String id);
    void deleteUser(UserModel user);
    Boolean checkPassword(UserModel user, String password);
    void updatePassword(UserModel user, String password);
    Boolean verifyPassword(String password);

    Boolean matchPassword(String password, String passwordConfirmer);

    void saveUser(UserModel userModel);
}
