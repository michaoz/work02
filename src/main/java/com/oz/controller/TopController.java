package com.oz.controller;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="travel")
public class TopController {

    @RequestMapping(value = "/", method = GET)
    public String showTop() {
        return "top";
    }
    
    @RequestMapping(value = "/destinationsList", method = GET)
    public String showDestinationsList() {
        return "destinations/destinations";
    }
    
    @RequestMapping(value = "/tripPlans/newPlans", method = GET)
    public String showNewPlans() {
        return "tripPlans/newPlans";
    }
    
    @RequestMapping(value = "/tripPlans/createdPlans", method = GET)
    public String showCreatedPlans() {
        return "tripPlans/createdPlans";
    }
}