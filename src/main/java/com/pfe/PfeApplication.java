package com.pfe;

import com.pfe.entity.Role;
import com.pfe.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.TimeZone;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;



@SpringBootApplication
@EnableConfigurationProperties()
public class PfeApplication {
    @PostConstruct
    void started(){
        TimeZone.setDefault(TimeZone.getTimeZone("Africa/Casablanca"));
    }

    public static void main(String[] args) {
        SpringApplication.run(PfeApplication.class, args);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner run(UserService userService){
        return args -> {
            userService.saveRole(new Role(1, "ROLE_ADMIN"));
            userService.saveRole(new Role(2, "ROLE_MANAGER"));
            userService.saveRole(new Role(3, "ROLE_USER"));


//
//            userService.saveUser(new User("abdo", "ch", "abdo@abdo.com","pass","078","la"));
//            userService.saveUser(new User("ad", "ch", "abdo@a.com","pass","078","la"));
//            userService.saveUser(new User("da", "ch", "a@a.com","pass","078","la"));
//            userService.saveUser(new User("abadsado", "ch", "a@d.com","pass","078","la"));
//            userService.addRoleToUser("abdo@abdo.com", "ROLE_USER");
//            userService.addRoleToUser("abdo@abdo.com", "ROLE_MANAGER");
//            userService.addRoleToUser("abdo@a.com", "ROLE_MANAGER");
//            userService.addRoleToUser("a@a.com", "ROLE_ADMIN");
//            userService.addRoleToUser("a@d.com", "ROLE_USER");
//            userService.addRoleToUser("a@d.com", "ROLE_ADMIN");
        };
    }
}
