package com.hckk.sgl.orderservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Sun Guolei 2018/6/22 17:48
 */
@Controller
public class HomeController {
    @GetMapping(value = {"/", "/index"})
    public String home(@RequestParam(value = "url", defaultValue = "/order-front/index.html") String url) {
        return "redirect:" + url;
    }
}
