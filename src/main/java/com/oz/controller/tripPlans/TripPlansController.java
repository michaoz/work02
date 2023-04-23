package com.oz.controller.tripPlans;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="travel")
public class TripPlansController {

	// TODO 戻るボタン
	

	/**
	 * createRoute 初期表示
	 * @return
	 */
    @RequestMapping(value = "/tripPlans/createRoute", method = GET)
    public String showTop() {
        return "tripPlans/createRoute";
    }
    
    
    
}