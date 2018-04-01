package org.jozif.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.jozif.po.User;
import org.jozif.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@CommonsLog
//@RestController
@Controller
public class UserController {
    @Autowired
    private UserService userService;


    @RequestMapping(value = "/auth/greeting", method = RequestMethod.GET)
    @ResponseBody
    public Object greeting() {
        User user = userService.userFindAll();
        Map<String, String> result = new HashMap<>();
        result.put("email", user.getEmail());
        result.put("password", user.getPassword());
        user.getEmail();
        return result;
    }


    @RequestMapping(value = "/auth/nav", method = RequestMethod.GET)
    public Object toNav() {
        User user = userService.userFindAll();
        Map<String, String> result = new HashMap<>();
        result.put("email", user.getEmail());
        result.put("password", user.getPassword());
        user.getEmail();
//        return "/jquery-easyui-1.5.4.4/ams/nav.html";
        return "/jquery-easyui-1.5.4.4/ams/nav.html";
    }
}
