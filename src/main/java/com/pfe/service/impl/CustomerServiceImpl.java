package com.pfe.service.impl;

import com.pfe.entity.*;
import com.pfe.models.PageRequest;
import com.pfe.repository.*;
import com.pfe.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository ;
    private final ReviewRepository reviewRepository;
    private final FavoriteRepository favoriteRepository;
    private final ReservationRepository reservationRepository;
    private final TablesRepository tablesRepository;
    private final EmailSenderService emailSenderService;
    @Override
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
    @Override
    public Customer getCustomer(User user){
        return customerRepository.findByUser(user);
    }
    @Override
    public Customer getCustomerByUser(User user){
        return customerRepository.findByUser(user);
    }
    @Override
    public void saveComment(ReviewRestaurant reviewRestaurant) {
        reviewRepository.save(reviewRestaurant);
    }
    @Override
    public void saveFavorite(Favorite favorite) {
        favoriteRepository.save(favorite);
    }

    @Override
    public Favorite isInFavorite(Customer customer, Long id) {
        return favoriteRepository.findByCustomerAndRestaurantId(customer,id);
    }
    @Override
    public void deleteFavorite(Customer customer, Long id) {
        favoriteRepository.deleteByCustomerAndRestaurantId(customer,id);
    }

    @Override
    public Customer getCustomerByUserEmail(String email) {
        return customerRepository.findByUser_Email(email);
    }

    @Override
    public Page<Favorite> getCustomerFavorites(PageRequest pageRequest, String email) {
        Sort sort = Sort.by(pageRequest.getSortDirection(), pageRequest.getSortBy());
        Pageable pageable = org.springframework.data.domain.PageRequest.of(pageRequest.getPageNumber(),
                pageRequest.getPageSize(),sort);
        return favoriteRepository.findAllByCustomer_User_Email(pageable, email);
    }

    @Override
    public void deleteReservation(Customer customer, Long id) {
        reservationRepository.deleteByIdAndIdCustomer(id,customer);
    }

    @Override
    public Page<Reservation> getCustomerReservations(PageRequest pageRequest, String email) {
        Sort sort = Sort.by(pageRequest.getSortDirection(), pageRequest.getSortBy());
        Pageable pageable = org.springframework.data.domain.PageRequest.of(pageRequest.getPageNumber(),
                pageRequest.getPageSize(),sort);
        return reservationRepository.findAllByIdCustomer_User_Email(pageable, email);
    }
    public void SendEmail(String email,int seats, String date, String time) {
        Customer customer = customerRepository.findByUser_Email(email);

            emailSenderService.send(
                    email,
                    buildEmail(customer.getUser().getFirstName(),seats,date, time),
                    "Reservation"
            );
    }
    @Override
    public List<Object[]> checkReservations(Restaurant restaurant, LocalDate date) {
        return reservationRepository.findAllByIdAndRestaurant(restaurant,date);
    }

    @Override
    public void addReservation(Customer customer, Restaurant restaurant, Map<String, String> requestParams) throws ParseException {
        List<Tables> tablesList = tablesRepository.findAllByIdRestauraut(restaurant);
        Tables selectedTable = null;
        for(Tables table : tablesList){
            Reservation reservation = reservationRepository.findByDateAndIdTableAndTime(LocalDate.parse(requestParams.get("date")),
                    table,LocalTime.parse(requestParams.get("time")));
            if(reservation == null){
                selectedTable = table;
                break;
            }
        }
        try{
            Reservation reservation = new Reservation(LocalDate.parse(requestParams.get("date")),
                    Integer.parseInt(requestParams.get("seats")),
                    LocalTime.parse(requestParams.get("time")),
                    customer,
                    selectedTable,
                    requestParams.get("note"));
//            System.out.println(reservation.toString());
            reservationRepository.save(reservation);
            try{
                SendEmail(customer.getUser().getEmail(),Integer.parseInt(requestParams.get("seats")),
                        requestParams.get("date"), requestParams.get("time"));
            }catch (Exception e){}
        }
        catch (Exception e){
            throw new IllegalStateException("Tables already reserved");
        }

    }
    private String buildEmail(String name, int seats,String date, String time) {
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
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Your reserved a table for "+seats+" person</span>\n" +
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
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for reserve a Table. Reservation details: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">  </p></blockquote>\n " +
                "Date: "+date+"\n" +
                "At Time: "+time+"\n" +
                " <p>See you soon</p>" +
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
