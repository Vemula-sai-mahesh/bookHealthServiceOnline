//package com.example.BookHealthServiceOnline.service.impl;
//
//import com.example.BookHealthServiceOnline.Security.SecurityUtils;
//import com.example.BookHealthServiceOnline.config.AppTenantContext;
//import com.example.BookHealthServiceOnline.dao.UserDao;
//import com.example.BookHealthServiceOnline.domain.User;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//import  com.example.BookHealthServiceOnline.config.AppTenantContextFilter;
//
//import java.io.IOException;
//import java.util.ArrayList;
//
//
//@Service
//public class UserDetailsServiceImpl extends AppTenantContextFilter implements UserDetailsService {
//
//    @Autowired
//    private final UserDao userDao;
//
//    private FilterChain filterChain;
//
//    public UserDetailsServiceImpl(UserDao userDao) {
//        this.userDao = userDao;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
////
////        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
////        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
////        String privateTenant = request.getHeader(PRIVATE_TENANT_HEADER);
////
////        if (privateTenant != null) {
////            AppTenantContext.setCurrentTenant(privateTenant);
////        }
////        AppTenantContextFilter appTenantContextFilter = new AppTenantContextFilter();
////
////        try {
////            appTenantContextFilter.doFilter(request,response,filterChain);
////        } catch (IOException e) {
////            throw new RuntimeException(e);
////        } catch (ServletException e) {
////            throw new RuntimeException(e);
////        }
//
//        User user=userDao.findByUsername(username)
//                .orElseThrow( );
//
//        return new org.springframework.security.core.userdetails.User(
//                user.getUsername(),
//                user.getPassword(),
//                // Assuming the user has authorities like roles, otherwise, you can pass empty list or set
//                new ArrayList<>()
//        );
//    }
//
//}
