package propensi.project.water.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

import propensi.project.water.model.User.UserModel;
import propensi.project.water.repository.User.UserDb;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    final UserDb userRepository;

    public UserDetailsServiceImpl(UserDb userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userRepository.findByUsername(username)
                            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        else {
            List<GrantedAuthority> authorityList = new ArrayList<>();
            authorityList.add(new SimpleGrantedAuthority(user.getRole().toString()));

            return new User(user.getUsername(), user.getPassword(), authorityList);
        }
    }
}
