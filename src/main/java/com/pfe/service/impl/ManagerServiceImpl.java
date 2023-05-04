package com.pfe.service.impl;

import com.pfe.entity.*;
import com.pfe.models.PageRequest;
import com.pfe.repository.*;
import com.pfe.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ManagerServiceImpl implements ManagerService {
    private final ManagerRepository managerRepository;
    private final DishRepository dishRepository;
    private final MenuRepository menuRepository;
    private final GalleryRepository galleryRepository;
    private final TablesRepository tablesRepository;
    private final ReservationRepository reservationRepository;
    private final EmailSenderService emailSenderService;

    @Override
    public void saveManager(Manager manager) {
        int count=0;
        List<Tables> tables = tablesRepository.findAllByIdRestauraut(manager.getRestaurant());
        if(tables.size() > manager.getRestaurant().getNumberTables()){
            for(Tables tab: tables){
                if((tables.size()-manager.getRestaurant().getNumberTables()) == count){
                    break;
                }
                try {
                    tablesRepository.delete(tab);
                    count++;
                }catch (Exception ignored){}
            }
        }
        for(int i = 0; i < manager.getRestaurant().getNumberTables()-tables.size();i++){
            tablesRepository.save(new Tables(manager.getRestaurant()));
        }
        managerRepository.save(manager);
    }

    @Override
    public void addDish(Dish dish, Long idMenu, User user) {
        Optional<Menu> menu = menuRepository.findById(idMenu);

        dish.setMenu(menu.get());
        dishRepository.save(dish);
    }
    @Override
    public void addMenu(Menu menu, User user) {
        Optional<Manager> manager = managerRepository.findById(user.getId());
        menu.setRestaurant(manager.get().getRestaurant());
        menuRepository.save(menu);
    }
    @Override
    public void addTable(Tables tables) {
        tablesRepository.save(tables);
    }
    @Override
    public Page<Dish> DishList(PageRequest pageRequest, User user) {
        Optional<Manager> manager = managerRepository.findById(user.getId());
        Restaurant restaurant = manager.get().getRestaurant();
        Sort sort = Sort.by(pageRequest.getSortDirection(), pageRequest.getSortBy());
        Pageable pageable = org.springframework.data.domain.PageRequest.of(pageRequest.getPageNumber(),
                pageRequest.getPageSize(),sort);
        return dishRepository.findAllByMenu_Restaurant(restaurant,pageable);
    }
    @Override
    public List<Dish> DishList(User user) {
        Optional<Manager> manager = managerRepository.findById(user.getId());
        Restaurant restaurant = manager.get().getRestaurant();
        List<Menu> menus = menuRepository.findByRestaurant(restaurant);
        List<Dish> dishes = new ArrayList<>();
        for (Menu m: menus){
            dishes.addAll(dishRepository.findByMenu(m));
        }

        return dishes;
    }
    @Override
    public void deleteDish(Long[] idDish) {
        for(Long d: idDish){
            dishRepository.deleteById(d);
        }
    }
    @Override
    public void cancelReservation(Long[] idReservation) {
        for(Long d: idReservation){
            Reservation reservation = reservationRepository.getById(d);
            if(reservation.getDate().isAfter(LocalDate.now())){
                try{
                    emailSenderService.send(
                            reservation.getIdCustomer().getUser().getEmail(),
                            buildEmail(reservation.getIdCustomer().getUser().getFirstName(),
                                    reservation.getIdTable().getIdRestauraut().getName(),
                                    reservation.getIdTable().getIdRestauraut().getId()),
                            "Reservation Cancelled"
                    );
                }catch (Exception ignored){}
                reservationRepository.deleteById(d);
            } else if ( reservation.getDate().equals(LocalDate.now()) && reservation.getTime().isAfter(LocalTime.now())) {
                try{
                    emailSenderService.send(
                            reservation.getIdCustomer().getUser().getEmail(),
                            buildEmail(reservation.getIdCustomer().getUser().getFirstName(),
                                    reservation.getIdTable().getIdRestauraut().getName(),
                                    reservation.getIdTable().getIdRestauraut().getId()),
                            "Reservation Cancelled"
                    );
                }catch (Exception ignored){}
                reservationRepository.deleteById(d);
            }

        }
    }
    @Override
    public List<Menu> menuList(User user) {
        Optional<Manager> manager = managerRepository.findById(user.getId());
        Restaurant restaurant = manager.get().getRestaurant();
        return menuRepository.findByRestaurant(restaurant);
    }
    @Override
    public Menu getMenu(Long id) {
        return menuRepository.findById(id).get();
    }
    @Override
    public void deleteMenu(Long[] idMenu) {
        for(Long m: idMenu){
            menuRepository.deleteById(m);
        }
    }
    @Override
    public Dish getDish(Long id) {
        return dishRepository.findById(id).get();
    }
    @Override
    public Manager getManager(String email) {
        return managerRepository.findByUser_Email(email);
    }

    @Override
    public void deleteImage(Long id) {
        galleryRepository.deleteById(id);
    }

    @Override
    public Page<Reservation> getRestaurantReservations(PageRequest pageRequest, String email) {
        Sort sort = Sort.by(pageRequest.getSortDirection(), pageRequest.getSortBy());
        Pageable pageable = org.springframework.data.domain.PageRequest.of(pageRequest.getPageNumber(),
                pageRequest.getPageSize(),sort);
        Manager manager = managerRepository.findByUser_Email(email);
        return reservationRepository.findAllByIdTable_IdRestauraut(pageable, manager.getRestaurant());
    }

    @Override
    public List<Gallery> getGallery(Restaurant restaurant) {

        return galleryRepository.findByRestaurant(restaurant);
    }

    private String buildEmail(String name, String restaurantName,Long restaurantId) {
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
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">your reservation has been cancelled </span>\n" +
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
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> your reservation has been cancelled By the manager of the restaurant "+restaurantName+" </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">  </p></blockquote>\n " +
                " <p>Please contact the restaurant or book other date <a href=\"http://localhost:3000/restaurant?id="+restaurantId+"\">"+restaurantName+"</a></p>" +
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
