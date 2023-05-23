package propensi.project.water.service;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import propensi.project.water.model.Donasi.DonasiModel;
import propensi.project.water.model.User.UserModel;
import propensi.project.water.repository.User.UserDb;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MengelolaKaryawanServiceImpl implements MengelolaKaryawanService{

    @Autowired
    private UserDb userDb;

    @Qualifier("userServiceImpl")
    @Autowired
    private UserService userService;

    @Override
    public void addKaryawan(UserModel user) {
        String pass = userService.encrypt(user.getPassword());
        user.setPassword(pass);
        userDb.save(user);
    }

    @Override
    public boolean uniqueValueConstraint(UserModel user) {
        Optional<UserModel> uniqueUsername = userDb.findByUsername(user.getUsername());
        Optional<UserModel> uniqueEmail = null;
        Optional<UserModel> uniqueHp = null;

        if (!user.getEmail().equals(null)) {
            uniqueEmail = userDb.findByEmail(user.getEmail());
        }
        if (!user.getHp().equals(null)) {
            uniqueHp = userDb.findByHp(user.getHp());
        }

        // if username/email/hp already in database
        if (uniqueUsername.isPresent() || uniqueEmail.isPresent() || uniqueHp.isPresent()) {
            return true;
        }

        // else if username & email & hp is unique
        return false;
    }

    @Override
    public Page<UserModel> retrievePage(String role, Pageable paging){
        if (role.equals("semua")){
            return userDb.findAllByUsernameIsNotNullOrderByUsername(paging);
        }
        else if (role.equals("karyawan")) {
            Page<UserModel> userlist = userDb.findAllInternal(paging);
            return userDb.findAllInternal(paging);
        }
        else {
            return userDb.findAllEksternal(paging);
        }
    }

    @Override
    public List<UserModel> retrieveListUser(String role) {

        List<UserModel> listUser = userDb.findAll();

        // if it doesn't fulfill any of the conditions below,
        // it will retrieve all users regardless of role
        if (role==null) return listUser;

        List<UserModel> finalList = new ArrayList<>();

        // to retrieve lists of karyawan (manajer, supervisor, teknisi, pekerja)
        if (role.equals("karyawan")) {
            for (UserModel user:listUser) {
                if (user.getRole().toString().equals("MANAJER")||
                    user.getRole().toString().equals("SUPERVISOR")||
                    user.getRole().toString().equals("TEKNISI")||
                    user.getRole().toString().equals("PEKERJA")){
                    finalList.add(user);
                }
            }
        }

        // to retrieve lists of external users (partner, donatur, customer)
        else if (role.equals("eksternal")) {
            for (UserModel user:listUser) {
                if (user.getRole().toString().equals("PARTNER")||
                    user.getRole().toString().equals("DONATUR")||
                    user.getRole().toString().equals("CUSTOMER")) {
                    finalList.add(user);
                }
            }
        }

        return finalList;
    }

    @Override
    public UserModel retrieveUserDetail(String username) {
        return userDb.findByUsername(username).orElse(null);
    }

    @Override
    public void updateUser(UserModel user) {
        UserModel userLama = userDb.findByUsername(user.getUsername()).orElse(null);
        userLama.setNama(user.getNama());
        userLama.setRole(user.getRole());
        userLama.setEmail(user.getEmail());
        userLama.setHp(user.getHp());
        userDb.save(userLama);
    }

    @Override
    public boolean uniqueValueConstraintUpdate(UserModel user) {
        UserModel karyawanLama = retrieveUserDetail(user.getUsername());

        String emailKaryawanLama = karyawanLama.getEmail();
        String emailKaryawanUpdated = user.getEmail();
        UserModel cariKaryawanEmail = userDb.findByEmail(user.getEmail()).orElse(null);

        String hpKaryawanLama = karyawanLama.getHp();
        String hpKaryawanUpdated = user.getHp();
        UserModel cariKaryawanHp = userDb.findByHp(user.getHp()).orElse(null);

        boolean emailHpUnique = true;

        // case email unchanged
        if(emailKaryawanLama.equals(emailKaryawanUpdated)) {}
        // case email changed
        else {
            // case new email is unique
            if (cariKaryawanEmail == null){}
            // case new email is NOT unique
            else {
                emailHpUnique = false;
            }
        }

        if (!emailHpUnique) return emailHpUnique;

        // case hp unchanged
        if(hpKaryawanLama.equals(hpKaryawanUpdated)){}
        // case hp changed
        else {
            // case new hp is unique
            if (cariKaryawanHp == null){}
            // case new hp is NOT unique
            else {
                emailHpUnique = false;
            }
        }

        // case if both email and hp are unique, do update
        if (emailHpUnique) {
            updateUser(user);
        }

        return emailHpUnique;
    }

    @Override
    public void deleteUser(UserModel user) {
        userDb.delete(user);
    }

}
