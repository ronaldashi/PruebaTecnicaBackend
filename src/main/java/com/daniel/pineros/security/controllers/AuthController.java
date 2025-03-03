package com.daniel.pineros.security.controllers;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.daniel.pineros.entities.Message;
import com.daniel.pineros.security.dtos.LoginUser;
import com.daniel.pineros.security.dtos.NewUser;
import com.daniel.pineros.security.entities.Role;
import com.daniel.pineros.security.entities.User;
import com.daniel.pineros.security.enums.RoleList;
import com.daniel.pineros.security.jwt.JwtProvider;
import com.daniel.pineros.security.services.RoleService;
import com.daniel.pineros.security.services.UserService;

import com.daniel.pineros.security.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleService roleService;
    private final JwtProvider jwtProvider;

    @Value("${jwt.accessTokenCookieName}")
    private String cookieName;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder,
            UserService userService, RoleService roleService, JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.roleService = roleService;
        this.jwtProvider = jwtProvider;
    }
    @PostMapping("/login")
    public ResponseEntity<Object> login(HttpServletResponse httpServletResponse,
            @Valid @RequestBody LoginUser loginUser, BindingResult bidBindingResult){
        if(bidBindingResult.hasErrors())
            return new ResponseEntity<>(new Message("Revise sus credenciales"), HttpStatus.BAD_REQUEST);
        try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginUser.getUserName(), loginUser.getPassword())
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = jwtProvider.generateToken(authentication);
            CookieUtil.create(httpServletResponse, cookieName, jwt, true, -1, "dev-store-demo.firebaseapp.com");
                return new ResponseEntity<>(new Message("Sesión iniciada"), HttpStatus.OK);
        } catch (Exception e) {
                return new ResponseEntity<>(new Message("Revise sus credenciales"), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody NewUser newUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseEntity<>(new Message("Revise los campos e intente nuevamente"), HttpStatus.BAD_REQUEST);

        User user = new User(newUser.getUserName(), newUser.getEmail(),
                passwordEncoder.encode(newUser.getPassword()));
        Set<Role> roles = new HashSet<>();

        // ✅ Asignar el rol USER siempre
        roles.add(roleService.getOrCreateRole(RoleList.ROLE_USER));

        // ✅ Si es el primer usuario registrado, asignarle ADMIN
        if (userService.countUsers() == 0) {
            roles.add(roleService.getOrCreateRole(RoleList.ROLE_ADMIN));
        }

        user.setRoles(roles);
        userService.save(user);
        return new ResponseEntity<>(new Message("Registro exitoso! Inicie sesión"), HttpStatus.CREATED);
    }


    @GetMapping("/details")
    public ResponseEntity<Object> getUserDetails(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String userName = userDetails.getUsername();
        Optional<User> user= this.userService.getByUserName(userName);
        if (!user.isPresent())
            return new ResponseEntity<>(new Message("No encotrado"), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(user.get(), HttpStatus.OK) ;
    }
    @GetMapping("/logout")
    public ResponseEntity<Message> logOut(HttpServletResponse httpServletResponse){
        CookieUtil.clear(httpServletResponse,cookieName);
        return new ResponseEntity<>(new Message("Sesión cerrada"), HttpStatus.OK) ;
    }

}
