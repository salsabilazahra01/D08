package propensi.project.water.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import propensi.project.water.model.User.UserModel;

import java.util.List;

public interface MengelolaKaryawanService {

    void addKaryawan(UserModel user);

    public boolean uniqueValueConstraint(UserModel user);

    public boolean uniqueValueConstraintUpdate(UserModel user);

    List<UserModel> retrieveListUser(String role);

    UserModel retrieveUserDetail(String username);

    void updateUser(UserModel user);

    void deleteUser(UserModel user);

    Page<UserModel> retrievePage(String role, Pageable paging);
}
