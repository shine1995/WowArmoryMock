package com.theironyard.controllers;

import com.theironyard.entities.Item;
import com.theironyard.entities.User;
import com.theironyard.services.ItemRepository;
import com.theironyard.services.StatRepository;
import com.theironyard.services.UserRepository;
import com.theironyard.utilities.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

@RestController
@CrossOrigin
public class WowArmoryMockController {

    @Autowired
    UserRepository users;

    @Autowired
    ItemRepository items;

    @Autowired
    StatRepository stats;

    @CrossOrigin
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public Iterable<Item> index(Model model) {

        return items.findAll();
    }

    @RequestMapping(path = "/", method = RequestMethod.POST)
    public String items() {

        int itemInput = 10000;

        for (int i = 0; i < 5000; i++) {

            String itemUri = "https://us.api.battle.net/wow/item/" + itemInput
                    + "?locale=en_US&apikey=yz98b2qzp8qfp62axbgrmzsuzjkwbgc8";

            RestTemplate restTemplate = new RestTemplate();
            Item itemJson;
            try {
                itemJson = restTemplate.getForObject(itemUri, Item.class);
                if (Objects.equals(itemJson.getInventoryType(), "20")) {
                    itemJson.setInventoryType("5");
                }
                if (Objects.equals(itemJson.getInventoryType(), "13")) {
                    itemJson.setInventoryType("21");
                }
                if (!Objects.equals(itemJson.getInventoryType(), "0")) {
                    try {
                        for (int j = 0; j < stats.count(); j++) {
                            stats.findOne(j).setStatName(stringify(stats.findOne(j).getStatName()));
                        }
                        stats.save(itemJson.getBonusStats());
                        items.save(itemJson);
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (HttpClientErrorException ex) {
//                ex.printStackTrace();
            }
            itemInput++;
        }
        return "redirect:/";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public User login(String username, String password, HttpSession session, HttpServletResponse response) throws Exception {
        User user = users.findFirstByName(username);
        if (user == null) {
            user = new User(username, PasswordStorage.createHash(password));
            users.save(user);
        } else if (!PasswordStorage.verifyPassword(password, user.getPassword())) {
            throw new Exception("Wrong password");
        }
        session.setAttribute("username", username);
        response.sendRedirect("/");
        return user;
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public void logout(HttpSession session, HttpServletResponse response) throws IOException {
        session.invalidate();
        response.sendRedirect("/");
    }

    public String stringify(String statName) {

        String statString;

        switch (statName) {

            case "0":
                statString = "Mana";
                break;
            case "1":
                statString = "Health";
                break;
            case "3":
                statString = "Agility";
                break;
            case "4":
                statString = "Strength";
                break;
            case "5":
                statString = "Intellect";
                break;
            case "6":
                statString = "Spirit";
                break;
            case "7":
                statString = "Stamina";
                break;
            case "12":
                statString = "Defense";
                break;
            case "13":
                statString = "Dodge";
                break;
            case "14":
                statString = "Parry";
                break;
            case "15":
                statString = "Block";
                break;
            case "16":
            case "17":
            case "18":
                statString = "Hit";
                break;
            case "19":
            case "20":
            case "21":
                statString = "Critical Strike";
                break;
            case "22":
            case "23":
            case "24":
                statString = "Hit Avoidance";
                break;
            case "25":
            case "26":
            case "27":
                statString = "Critical Strike Avoidance";
                break;
            case "28":
            case "29":
            case "30":
                statString = "Haste";
                break;
            case "31":
                statString = "Hit";
                break;
            case "32":
                statString = "Critical Strike";
                break;
            case "33":
                statString = "Hit Avoidance";
                break;
            case "34":
                statString = "Critical Strike Avoidance";
                break;
            case "35":
                statString = "PvP Resilience";
                break;
            default:
                statString = "Default";
                break;
        }

        return statString;
    }
}