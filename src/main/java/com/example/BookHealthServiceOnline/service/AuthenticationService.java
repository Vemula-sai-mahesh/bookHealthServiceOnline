package com.example.BookHealthServiceOnline.service;


import com.example.BookHealthServiceOnline.dao.AdminUserDao;
import com.example.BookHealthServiceOnline.dao.UserDao;
import com.example.BookHealthServiceOnline.dao.impl.AdminUserDaoImpl;
import com.example.BookHealthServiceOnline.domain.AdminUser;
import com.example.BookHealthServiceOnline.domain.User;
import com.example.BookHealthServiceOnline.service.Dto.LoginUserDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserDao userDao;

    private final AdminUserDao adminUserDao;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final AdminUserDaoImpl adminUserDaoImpl;

    public AuthenticationService(
            UserDao userDao, AdminUserDao adminUserDao,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            AdminUserDaoImpl adminUserDaoImpl) {
        this.userDao = userDao;
        this.adminUserDao = adminUserDao;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.adminUserDaoImpl = adminUserDaoImpl;
    }

//    public User signup(RegisterUserDto input) {
//        User user = new User();
//        user .setFullName(input.getFullName());
//        user .setEmail(input.getEmail());
//        user  .setPassword(passwordEncoder.encode(input.getPassword()));
//
//        return userRepository.save(user);
//    }
//
    public User authenticate(LoginUserDto input) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        input.getUsername(),
//                        input.getPassword()
//                )
//        );

        return userDao.findByUsername(input.getUsername())
                .orElseThrow();
    }
    public AdminUser authenticateAdmin(LoginUserDto input) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        input.getUsername(),
//                        input.getPassword()
//                )
//        );

        return adminUserDao.findByUsername(input.getUsername())
                .orElseThrow();
    }
}
