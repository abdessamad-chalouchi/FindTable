package com.pfe.service.impl;

import com.pfe.entity.*;
import com.pfe.models.RegistrationRequest;
import com.pfe.models.RestaurantRegistrationRequest;
import com.pfe.repository.TablesRepository;
import com.pfe.service.CategoriesService;
import com.pfe.service.ManagerService;
import com.pfe.service.RestaurantService;
import com.pfe.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@AllArgsConstructor
@Slf4j
public class RegistrationService {
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailValidator emailValidator;
    private final EmailSenderService emailSenderService;
    private final UserService userService;
    private final RestaurantService restaurantService;
    private final ManagerService managerService;
    private final CategoriesService categoriesService;
    private final TablesRepository tablesRepository;


    public String restaurantRegister(RestaurantRegistrationRequest request, String role) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail) {
            log.error("Email not Valid");
            throw new IllegalStateException("email not Valid");
        }

        String registrationToken = userService.signUpUser(new User(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                request.getPhone()
        ));
        Category category = categoriesService.getCategory(request.getCategory()).get();
        Restaurant restaurant =restaurantService.saveRestaurant(
                new Restaurant(
                        request.getRestaurantName(),
                        request.getEmail(),
                        request.getPhone(),
                        request.getNumberTables(),
                        request.getAddress(),
                        request.getCity(),
                        request.getCountry(),
                        request.getLatitude(),
                        request.getLongitude(),
                        request.getDescription(),
                        category,
                        "00:00",
                        "00:00"
                        )
        );
        User user = userService.getUser(request.getEmail());
        Manager manager = new Manager(user.getId(),user,restaurant);
        managerService.saveManager(manager);
        String link = "http://localhost:8080/api/confirm?registrationToken=" + registrationToken;
        userService.addRoleToUser(request.getEmail(), role);
        for(int i=0; i<request.getNumberTables();i++){
            tablesRepository.save(new Tables(restaurant));
        }
        try {
            emailSenderService.send(
                    request.getEmail(),
                    buildEmail(request.getFirstName(), link),
                    "Confirm Your Email"
            );
        }catch (Exception ignored){}

        return "confirmed";
    }


    public String register(RegistrationRequest request, String role) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail) {
            log.error("Email not Valid");
            throw new IllegalStateException("email not Valid");
        }

        String registrationToken = userService.signUpUser(new User(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                request.getPhone()
        ));
        String link = "http://localhost:8080/api/confirm?registrationToken=" + registrationToken;

        userService.addRoleToUser(request.getEmail(), role);
        userService.addUserToCustomer(request.getEmail());
        try {
            emailSenderService.send(
                    request.getEmail(),
                    buildEmail(request.getFirstName(), link),
                    "Confirm Your Email"
            );
        }catch (Exception ignored){}

        // generate access token

        //generate access token
        return "confirmed";
    }
    public String verifierEmail(String email) {
        User user = userService.getUser(email);
        if(!user.getEnabled()){
            String registrationToken = userService.emailVerification(email);
            String link = "http://localhost:8080/api/confirm?registrationToken=" + registrationToken;
            emailSenderService.send(
                    email,
                    buildEmail(user.getFirstName(), link),
                    "Confirm Your Email"
            );
        }

        return "confirmed";
    }

    @Transactional
    public String confirmToken(String registrationToken) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(registrationToken)
                .orElseThrow(()->new IllegalStateException("token not found"));
        if(confirmationToken.getConfirmedAt() != null)
            throw new IllegalStateException("Email Already Confirmed!");

        LocalDateTime expireAt = confirmationToken.getExpiresAt();

        if(expireAt.isBefore(LocalDateTime.now()))
            throw new IllegalStateException("token expired");
        confirmationTokenService.setConfirmedAt(registrationToken);
        userService.enableAppUser(
                confirmationToken.getUser().getEmail()
        );
        userService.enableAppUser(
                confirmationToken.getUser().getEmail()
        );
        return "confirmed";
    }
    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }


}
