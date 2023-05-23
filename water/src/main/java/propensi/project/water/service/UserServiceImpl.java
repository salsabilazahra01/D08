package propensi.project.water.service;

import propensi.project.water.model.User.UserModel;
import propensi.project.water.repository.User.UserDb;

import javax.transaction.TransactionScoped;
import javax.transaction.Transactional;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDb userDb;

    @Override
    public UserModel addUser(UserModel user) {
        user.setPassword(encrypt(user.getPassword()));
        userDb.save(user);
        return user;
    }

    @Override
    public String encrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword;
    }

    @Override
    public UserModel getUserByUsername(String username) {
        UserModel user = userDb.findByUsername(username)
                            .orElse(null);

        return user;
    }

    @Override
    public List<UserModel> getListUser() {
        return userDb.findAll();
    }

    @Override
    public UserModel getUserById(String id) {
        Optional<UserModel> user = userDb.findById(id);
        if (user.isPresent()) {
            return user.get();
        }

        return null;
    }

    @Override
    public void deleteUser(UserModel user) {
        userDb.delete(user);
    }

    @Override
    public Boolean checkPassword(UserModel user, String password) {
        return new BCryptPasswordEncoder().matches(password, user.getPassword());
    }

    @Override
    public void updatePassword(UserModel user, String password) {
        String passwordTemp = encrypt(password);
        user.setPassword(passwordTemp);
        userDb.save(user);
    }

    @Override
    public Boolean verifyPassword(String password) {
        String numeric = ".*[0-9].*";
        String alphabet = ".*[A-Za-z].*";
        String symbol = ".*[!@#$%&*()_+=|<>?{}\\[\\]~-].*";
        if(password.length() < 8 || !password.matches(numeric) || !password.matches(alphabet) || !password.matches(symbol)){
            return false;
        }

        return true;
    }

    @Override
    public Boolean matchPassword(String password, String passwordConfirmer) {
        return password.equals(passwordConfirmer);
    }

    @Override
    public void saveUser(UserModel userModel) {
        this.userDb.save(userModel);
    }
}
