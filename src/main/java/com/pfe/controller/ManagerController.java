package com.pfe.controller;

import com.pfe.entity.*;
import com.pfe.models.PageRequest;
import com.pfe.repository.GalleryRepository;
import com.pfe.service.CategoriesService;
import com.pfe.service.ManagerService;
import com.pfe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/manager")

public class ManagerController {
    private final ManagerService managerService;
    private final UserService userService;
    private final CategoriesService  categoriesService;
    private final GalleryRepository galleryRepository;

    @GetMapping("/dish/list")
    public ResponseEntity<Iterable<Dish>> DishList(PageRequest pageRequest, @RequestHeader("Authorization") String authorization) throws org.apache.tomcat.util.json.ParseException {
        String token = authorization.split(" ")[1];
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));
        Object object =  new JSONParser(payload).parse();

        String email = String.valueOf(object).split(",")[0].split("=")[1];
        User user = userService.getUser(email);
//        Restaurant restaurant = managerService.getRestaurant(email);
        return ResponseEntity.ok(managerService.DishList(pageRequest,user));
    }
    @GetMapping("/reservation/list")
    public ResponseEntity<Iterable<Reservation>> getReservation(@RequestHeader("Authorization") String authorization,
                                                                PageRequest pageRequest) throws org.apache.tomcat.util.json.ParseException {
        String token = authorization.split(" ")[1];
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));
        Object object =  new JSONParser(payload).parse();
        String email = String.valueOf(object).split(",")[0].split("=")[1];

        return ResponseEntity.ok(managerService.getRestaurantReservations(pageRequest,email));
    }
    
    @PostMapping("/dish/add")
    public ResponseEntity<String> addDish(@RequestParam(required = false) MultipartFile image,
                                          @RequestParam Map<String,String> requestParams,
                                          @RequestHeader("Authorization") String authorization) throws ParseException, IOException, org.apache.tomcat.util.json.ParseException {
        Dish dish;
        Long menuId = 0L;
        String token = authorization.split(" ")[1];
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));
        Object object =  new JSONParser(payload).parse();

        String email = String.valueOf(object).split(",")[0].split("=")[1];
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
            menuId = Long.parseLong(requestParams.get("menu"));
            if(Long.parseLong(requestParams.get("idDish")) == 0 || Long.parseLong(requestParams.get("id")) == 0){
                dish = new Dish(requestParams.get("name"),
                        Float.parseFloat(requestParams.get("price")),
                        requestParams.get("description"),image.getBytes(),
                        dateFormat.parse(requestParams.get("startDate")),
                        dateFormat.parse(requestParams.get("expirationDate")),
                        Float.parseFloat(requestParams.get("promo")));
            }
            else {
                dish = managerService.getDish(Long.parseLong(requestParams.get("idDish")));
                dish.setName(requestParams.get("name"));
                dish.setDescription(requestParams.get("description"));
                dish.setPrice(Float.parseFloat(requestParams.get("price")));
                dish.setStartDate(dateFormat.parse(requestParams.get("startDate")));
                dish.setExpirationDate(dateFormat.parse(requestParams.get("expirationDate")));
                dish.setPercentage(Float.parseFloat(requestParams.get("promo")));
                if(image != null){
                    dish.setImage(image.getBytes());
                }
            }
        User user = userService.getUser(email);
        managerService.addDish(dish,menuId, user);
        return ResponseEntity.ok("ok");
    }
    @DeleteMapping("/dish/delete")
    public ResponseEntity<String> deleteDish(@RequestBody Map<String,Long[]> request) {
        managerService.deleteDish(request.get("dishList"));
        return ResponseEntity.ok("ok");
    }
    @DeleteMapping("/reservation/delete")
    public ResponseEntity<String> deleteReservation(@RequestBody Map<String,Long[]> request) {
        managerService.cancelReservation(request.get("reservationList"));
        return ResponseEntity.ok("ok");
    }
    @GetMapping("/dish/{id}")
    public ResponseEntity<Dish> getDish(@RequestHeader("Authorization") String authorization,@PathVariable("id") Long id) throws org.apache.tomcat.util.json.ParseException {
        return ResponseEntity.ok(managerService.getDish(id));
    }
    @GetMapping("/menu/{id}")
    public ResponseEntity<Menu> getMenu(@RequestHeader("Authorization") String authorization,@PathVariable("id") Long id) throws org.apache.tomcat.util.json.ParseException {
        return ResponseEntity.ok(managerService.getMenu(id));
    }
    @DeleteMapping("/menu/delete")
    public ResponseEntity<String> deleteMenu(@RequestBody Map<String,Long[]> request) throws org.apache.tomcat.util.json.ParseException {
        managerService.deleteMenu(request.get("menuList"));
        return ResponseEntity.ok("ok");
    }
    @PostMapping("/menu/add")
    public ResponseEntity<String> addMenu(@RequestParam Map<String,String> requestParams,@RequestHeader("Authorization") String authorization) throws ParseException, IOException, org.apache.tomcat.util.json.ParseException {
        Menu menu = new Menu();
        String token = authorization.split(" ")[1];
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));
        Object object =  new JSONParser(payload).parse();

        String email = String.valueOf(object).split(",")[0].split("=")[1];
        if(Long.parseLong(requestParams.get("id")) == 0){
            menu.setName(requestParams.get("name"));
        }
        else {
            menu.setId(Long.parseLong(requestParams.get("id")));
            menu.setName(requestParams.get("name"));
        }


        User user = userService.getUser(email);

        managerService.addMenu(menu, user);

        return ResponseEntity.ok("ok");
    }
    @GetMapping("/menu/list")
    public ResponseEntity<List<Menu>> menuList(@RequestHeader("Authorization") String authorization) throws org.apache.tomcat.util.json.ParseException {
        String token = authorization.split(" ")[1];
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));
        Object object =  new JSONParser(payload).parse();

        String email = String.valueOf(object).split(",")[0].split("=")[1];
        User user = userService.getUser(email);

        return ResponseEntity.ok(managerService.menuList(user));
    }
    @GetMapping("/restaurant")
    public ResponseEntity<Manager> getRestaurant(@RequestHeader("Authorization") String authorization) throws org.apache.tomcat.util.json.ParseException {
        String token = authorization.split(" ")[1];
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));
        Object object =  new JSONParser(payload).parse();

        String email = String.valueOf(object).split(",")[0].split("=")[1];
        Manager manager = managerService.getManager(email);
        return ResponseEntity.ok(manager);
    }

    @PostMapping("/restaurant/update")
    public ResponseEntity<String> updateRestaurant(@RequestParam(required = false) MultipartFile image,
                                          @RequestParam Map<String,String> requestParams,
                                          @RequestHeader("Authorization") String authorization) throws ParseException, IOException, org.apache.tomcat.util.json.ParseException {
        String token = authorization.split(" ")[1];
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));
        Object object =  new JSONParser(payload).parse();
        String email = String.valueOf(object).split(",")[0].split("=")[1];
        Category category=categoriesService.getCategory(Long.parseLong(requestParams.get("categories"))).get();
        Manager manager = managerService.getManager(email);
        manager.getRestaurant().setName(requestParams.get("name"));
        manager.getRestaurant().setDescription(requestParams.get("description"));
        manager.getRestaurant().setNumberTables(Integer.parseInt(requestParams.get("numberTable")));
        manager.getRestaurant().setCountry(requestParams.get("country"));
        manager.getRestaurant().setCity(requestParams.get("city"));
        manager.getRestaurant().setAddress(requestParams.get("address"));
        manager.getRestaurant().setEmail(requestParams.get("email"));
        manager.getRestaurant().setPhone(requestParams.get("phone"));
        manager.getRestaurant().setCategories(category);
        if(!requestParams.get("latitude").equals("") && !requestParams.get("longitude").equals("")){
            manager.getRestaurant().setLatitude(requestParams.get("latitude"));
            manager.getRestaurant().setLongitude(requestParams.get("longitude"));
        }
        if(image != null){
            manager.getRestaurant().setImage(image.getBytes());
        }
        managerService.saveManager(manager);
        return ResponseEntity.ok("ok");
    }
    @PostMapping("/restaurant/updateWorkingTime")
    public ResponseEntity<String> updateRestaurantWorkingTime(@RequestParam Map<String,String> requestParams,
                                                   @RequestHeader("Authorization") String authorization) throws ParseException, IOException, org.apache.tomcat.util.json.ParseException {
        String token = authorization.split(" ")[1];
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));
        Object object =  new JSONParser(payload).parse();
        String email = String.valueOf(object).split(",")[0].split("=")[1];
        Manager manager = managerService.getManager(email);
        Boolean open = false;
        if(Integer.parseInt(requestParams.get("open")) == 1)
            open = true;
        manager.getRestaurant().setOpen(open);
        manager.getRestaurant().setStartWorkingTime(LocalTime.parse(requestParams.get("openAt")));
        manager.getRestaurant().setEndWorkingTime(LocalTime.parse(requestParams.get("closeAt")));
        managerService.saveManager(manager);
        return ResponseEntity.ok("ok");
    }
    @PostMapping("/restaurant/tables")
    public ResponseEntity<String> addTables(@RequestParam Map<Integer,Integer> requestParams,
                                                              @RequestHeader("Authorization") String authorization) throws org.apache.tomcat.util.json.ParseException {
        String token = authorization.split(" ")[1];
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));
        Object object =  new JSONParser(payload).parse();
        String email = String.valueOf(object).split(",")[0].split("=")[1];
        Manager manager = managerService.getManager(email);
        System.out.println(requestParams);
        for (Map.Entry<Integer,Integer> entry : requestParams.entrySet())
            System.out.println("Key = " + entry.getKey() +
                    ", Value = " + entry.getValue());

        return ResponseEntity.ok("ok");
    }
    @PostMapping("/restaurant/gallery")
    public ResponseEntity<String> updateRestaurantGallery(@RequestParam Map<String, MultipartFile> requestParam,
                                                   @RequestHeader("Authorization") String authorization) throws ParseException, IOException, org.apache.tomcat.util.json.ParseException {
        String token = authorization.split(" ")[1];
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));
        Object object =  new JSONParser(payload).parse();
        String email = String.valueOf(object).split(",")[0].split("=")[1];
        Manager manager = managerService.getManager(email);
        List<Gallery> galleries = new ArrayList<>();
//        for(MultipartFile img: requestParam){
//            galleries.add(new Gallery(img.getBytes(),manager.getRestaurant()));
//        }
//        System.out.println("test");
        for (Map.Entry mapentry : requestParam.entrySet()) {
            System.out.println("clé: "+mapentry.getKey()
                    + " | valeur: " + mapentry.getValue());
            galleries.add(new Gallery(((MultipartFile) mapentry.getValue()).getBytes(),manager.getRestaurant()));
        }
//        System.out.println(requestParam.get("images").toString());
        galleryRepository.saveAll(galleries);
        return ResponseEntity.ok("ok");
    }
    @GetMapping("/restaurant/gallery")
    public ResponseEntity<List<Gallery>> getRestaurantGallery(@RequestHeader("Authorization") String authorization) throws ParseException, IOException, org.apache.tomcat.util.json.ParseException {
        String token = authorization.split(" ")[1];
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));
        Object object =  new JSONParser(payload).parse();
        String email = String.valueOf(object).split(",")[0].split("=")[1];

        Restaurant restaurant = managerService.getManager(email).getRestaurant();
        return ResponseEntity.ok(managerService.getGallery(restaurant));
    }
    @PostMapping("/restaurant/gallery/{id}")
    public ResponseEntity<String> deleteImage(@RequestHeader("Authorization") String authorization,@PathVariable("id") Long id) throws org.apache.tomcat.util.json.ParseException {
        managerService.deleteImage(id);
        return ResponseEntity.ok("ok");
    }

}