package com.oz.controller;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oz.bean.common.TripPlansCommonForm;
import com.oz.consts.CommonConstant;
import com.oz.service.dao.RouteInfoDao;

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
    
    @RequestMapping(value = "/tripPlans/newPlan", method = GET)
    public String showNewPlan() {    	
		return CommonConstant.NEWPLAN_URL;
    }
    
    @RequestMapping(value = "/tripPlans/createdPlans", method = GET)
    public String showCreatedPlans() {
        return "tripPlans/createdPlans";
    }
}