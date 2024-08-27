package com.example.BookHealthServiceOnline.controller;


import com.example.BookHealthServiceOnline.Response.GetTokenResponse;
import com.example.BookHealthServiceOnline.Response.TokenValidationResponse;
import com.example.BookHealthServiceOnline.config.AppTenantContext;
import com.example.BookHealthServiceOnline.domain.AdminUser;
import com.example.BookHealthServiceOnline.domain.User;
import com.example.BookHealthServiceOnline.service.AuthenticationService;
import com.example.BookHealthServiceOnline.service.Dto.LoginUserDto;
import com.example.BookHealthServiceOnline.service.JwtService;
import com.example.BookHealthServiceOnline.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import static com.example.BookHealthServiceOnline.config.AppTenantContext.DEFAULT_TENANT;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    private final UserService userService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, UserService userService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }


    @PostMapping("/token")
    public ResponseEntity<GetTokenResponse> generateTokenPost(@RequestBody LoginUserDto loginUserDto) {
        String jwtToken="";
        String tenant=AppTenantContext.getCurrentTenant();
        if(tenant.equals(DEFAULT_TENANT)){
            AdminUser authenticatedAdminUser = authenticationService.authenticateAdmin(loginUserDto);
            jwtToken = jwtService.generateToken( authenticatedAdminUser);

        }else{
            User authenticatedUser = authenticationService.authenticate(loginUserDto);
             jwtToken = jwtService.generateToken( authenticatedUser);

        }
        GetTokenResponse loginResponse = new GetTokenResponse(jwtToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }
    @GetMapping("/validate-token")
    public ResponseEntity<TokenValidationResponse> validateTokenGet(@RequestParam String token) {
        boolean isValid = jwtService.isTokenValid(token);
        TokenValidationResponse response = new TokenValidationResponse(isValid);
        return ResponseEntity.ok(response);
    }
}
