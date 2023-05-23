//package propensi.project.water.security;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import java.util.ArrayList;
//import java.util.List;
//
//import propensi.project.water.model.User.UserModel;
//import propensi.project.water.repository.User.UserDb;
//
//@Service
//public class WebUserDetailsServiceImpl implements UserDetailsService {
//    final UserDb userRepository;
//
//    public WebUserDetailsServiceImpl(UserDb userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserModel user = userRepository.findByUsername(username);
//        List<GrantedAuthority> authorityList = new ArrayList<>();
//        String role = user.getRole().toString();
//        authorityList.add(new SimpleGrantedAuthority(role));
//
//        return new User(user.getUsername(), user.getPassword(), authorityList);
//    }
//}
