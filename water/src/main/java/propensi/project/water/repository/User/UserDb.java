package propensi.project.water.repository.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import propensi.project.water.model.User.UserModel;

@Repository
public interface UserDb extends JpaRepository<UserModel, String> {
    Optional<UserModel> findByUsername(String username);
    List<UserModel> findAll();
    void delete(UserModel user);

     Optional<UserModel> findByEmail(String email);

     Optional<UserModel> findByHp(String hp);

     Page<UserModel> findAllByUsernameIsNotNullOrderByUsername(Pageable paging);

    @Query(value = "SELECT * , 0 AS clazz_ FROM user WHERE role = 'ADMIN' OR role = 'MANAJER' OR role = 'SUPERVISOR' OR role = 'TEKNISI' OR role = 'PEKERJA'",
            countQuery = "SELECT COUNT(*) FROM user WHERE role = 'ADMIN' OR role = 'MANAJER' OR role = 'SUPERVISOR' OR role = 'TEKNISI' OR role = 'PEKERJA'",
            nativeQuery = true)
    Page<UserModel> findAllInternal(Pageable paging);

    @Query(value = "SELECT * , 0 AS clazz_ FROM user WHERE role = 'DONATUR' OR role = 'CUSTOMER' OR role = 'PARTNER'",
            countQuery = "SELECT COUNT(*) FROM user WHERE role = 'DONATUR' OR role = 'CUSTOMER' OR role = 'PARTNER'",
            nativeQuery = true)
    Page<UserModel> findAllEksternal(Pageable paging);
}
