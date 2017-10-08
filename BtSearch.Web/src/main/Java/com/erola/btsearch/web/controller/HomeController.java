package com.erola.btsearch.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by Erola on 2017/9/30.
 */
@Controller
@RequestMapping(value = "/home")
public class HomeController extends BaseController {

    @RequestMapping(value="/index", method = {RequestMethod.GET})
    public ModelAndView index() throws URISyntaxException, IOException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home/index");
        return modelAndView;
    }

}