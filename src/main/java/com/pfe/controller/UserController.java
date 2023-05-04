package com.pfe.controller;

import com.pfe.entity.Role;
import com.pfe.entity.User;
import com.pfe.models.RoleToUserForm;
import com.pfe.service.UserService;
import com.pfe.service.impl.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Base64;
import java.util.List;
import java.util.Map;


@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final RegistrationService registrationService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok()
                .body(userService.getUsers());
    }
    @GetMapping("/user")
    public ResponseEntity<User> getUserDetails(@RequestHeader("Authorization") String authorization) throws ParseException {
        String token = authorization.split(" ")[1];
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));
        Object object =  new JSONParser(payload).parse();
        String email = String.valueOf(object).split(",")[0].split("=")[1];
        return ResponseEntity.ok(userService.getUser(email));
    }
    @PostMapping("/user")
    public ResponseEntity<String> editUserDetails(@RequestHeader("Authorization") String authorization,
                                                      @RequestParam Map<String,String> requestParams) throws ParseException {
        String token = authorization.split(" ")[1];
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));
        Object object =  new JSONParser(payload).parse();

        String email = String.valueOf(object).split(",")[0].split("=")[1];
        User user = userService.getUser(email);
        user.setFirstName(requestParams.get("firstName"));
        user.setLastName(requestParams.get("lastName"));
        user.setPhone(requestParams.get("phone"));
        if(!user.getEmail().equals( requestParams.get("email"))){
            User user1 = userService.getUser(requestParams.get("email"));
            if(user1 != null){
                throw new IllegalStateException("This Email is taken!");
            }
            user.setEmail(requestParams.get("email"));
            user.setEnabled(false);
        }
        if(!requestParams.get("password").equals("") && !requestParams.get("password").isEmpty()){
            user.setPassword(passwordEncoder.encode(requestParams.get("password")));
        }
        userService.updateUser(user);
        return ResponseEntity.ok("ok");
    }
    @PostMapping("/user/save")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        URI uri = URI.create(
                ServletUriComponentsBuilder.
                        fromCurrentContextPath().
                        path("/api/user/save").toUriString()
        );
        return ResponseEntity.created(uri)
                .body(userService.saveUser(user));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveUser(@RequestBody Role role) {
        URI uri = URI.create(
                ServletUriComponentsBuilder.
                        fromCurrentContextPath().
                        path("/api/role/save").toUriString()
        );
        return ResponseEntity.created(uri)
                .body(userService.saveRole(role));
    }

    @PostMapping("/role/addtouser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
        userService.addRoleToUser(form.getEmail(), form.getRoleName());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/user/email/verification")
    public ResponseEntity<String> verifierEmail(@RequestHeader("Authorization") String authorization) throws ParseException {
        String token = authorization.split(" ")[1];
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));
        Object object =  new JSONParser(payload).parse();

        String email = String.valueOf(object).split(",")[0].split("=")[1];
        return ResponseEntity.ok(registrationService.verifierEmail(email));
    }
    @GetMapping("/test")
    public ResponseEntity<String> test(){

        return ResponseEntity.ok("ok");
    }
}



