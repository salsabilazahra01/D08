package propensi.project.water.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;;

//import propensi.project.water.config.JwtAuthenticationEntryPoint;
//import propensi.project.water.config.JwtRequestFilter;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/bootstrap/**/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/script.js", "/style.css").permitAll()
                .antMatchers("/fragment.html", "fragment-transaksi.html").permitAll()
                .antMatchers("/login", "/validate-ticket").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/customer/add").permitAll()
                .antMatchers("/donatur/add").permitAll()
                .antMatchers("/partner/add").permitAll()
                .antMatchers("/transaksi/*").hasAnyAuthority("ADMIN","MANAJER","SUPERVISOR")
                .antMatchers("/manajemen-user/*").hasAnyAuthority("ADMIN","MANAJER")
                .antMatchers("/pengolahan-sampah/*").hasAnyAuthority("ADMIN","MANAJER","SUPERVISOR", "PEKERJA", "TEKNISI")
                .antMatchers("/pengolahan-sampah/save").hasAnyAuthority("SUPERVISOR", "ADMIN")
                .antMatchers("/warehouse/laporan").hasAnyAuthority("ADMIN", "MANAJER","SUPERVISOR","TEKNISI","PEKERJA")
                .antMatchers("/penawaran-hasil-olahan/*").hasAnyAuthority("ADMIN", "MANAJER","SUPERVISOR","CUSTOMER")
                .antMatchers("/penawaran/sampah/viewall").hasAnyAuthority("ADMIN", "MANAJER","SUPERVISOR","PARTNER")
                .antMatchers("/penawaran/sampah/view/*").hasAnyAuthority("ADMIN", "MANAJER","SUPERVISOR","PARTNER")
                .antMatchers("/penawaran/sampah/update/*").hasAnyAuthority("PARTNER")
                .antMatchers("/penawaran/sampah/delete/*").hasAnyAuthority("PARTNER")
                .antMatchers("/penawaran/sampah/persetujuan/*").hasAnyAuthority("ADMIN", "MANAJER","SUPERVISOR")
                .antMatchers("/penawaran/sampah/selesai/*").hasAnyAuthority("ADMIN", "MANAJER","SUPERVISOR","PARTNER")
                .antMatchers("/donasi/add", "/donasi/update").hasAnyAuthority("DONATUR")
                .antMatchers( "/donasi/update-status/{\\d+}/inspeksi").hasAnyAuthority("ADMIN", "MANAJER", "SUPERVISOR")
                .antMatchers("/donasi/").hasAnyAuthority("DONATUR", "ADMIN", "MANAJER", "SUPERVISOR")
                .antMatchers("/reward/*").hasAnyAuthority("ADMIN", "SUPERVISOR", "MANAJER")
                .antMatchers("/penawaran-hasil-olahan/update/*", "/penawaran-hasil-olahan/delete/*").hasAnyAuthority("CUSTOMER")
                .antMatchers("/about-us/view").permitAll()
                .antMatchers("/about-us/update").hasAnyAuthority("ADMIN", "MANAJER")
                .antMatchers("/artikel/viewall").permitAll()
                .antMatchers("/artikel/add", "/artikel/update/*", "/artikel/delete/*").hasAnyAuthority("ADMIN", "MANAJER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login").permitAll();

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

//    @Autowired
//    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//
//    @Autowired
//    private JwtRequestFilter jwtRequestFilter;
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    @Configuration
//    @Order(1)
//    public class RestApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
//        protected void configure(HttpSecurity httpSecurity) throws Exception {
//            httpSecurity.csrf().disable()
//                    .antMatcher("/api/**").cors()
//                    .and()
//                    .authorizeRequests()
//                    .antMatchers("/api/v1/authenticate").permitAll()
//                    .and()
//                    .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
//                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//            httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//        }
//    }
//
//    @Configuration
//    @Order(2)
//    public class UILoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
//
//        @Override
//        protected void configure(HttpSecurity httpSecurity) throws Exception {
//            httpSecurity
//                    .csrf().disable()
//                    .authorizeRequests()
//                    .antMatchers("/css/**").permitAll()
//                    .antMatchers("/js/**").permitAll()
//                    .antMatchers("/login-sso", "/validate-ticket", "/resep/view/*").permitAll()
//                    .anyRequest()
//                    .authenticated()
//                    .and()
//                    .formLogin()
//                    .loginPage("/login").permitAll()
//                    .and()
//                    .logout()
//                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                    .logoutSuccessUrl("/login").permitAll()
//                    .and()
//                    .sessionManagement().sessionFixation().newSession().maximumSessions(1);
//        }
//    }
}
