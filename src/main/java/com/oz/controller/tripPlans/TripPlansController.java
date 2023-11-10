package com.oz.controller.tripPlans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.util.StringUtil;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.oz.bean.common.LuggageInfo;
import com.oz.bean.common.LuggageItem;
import com.oz.bean.common.SpotInfo;
import com.oz.bean.common.TripPlansCommonForm;
import com.oz.consts.CommonConstant;
import com.oz.consts.CommonConstant.LUGGAGE_KEYWORD_ITEMS;
import com.oz.entity.SpotInfoEntity;
import com.oz.helper.TripPlansHelper;
import com.oz.service.CreateRouteService;
import com.oz.service.PrepLuggageService;
import com.oz.service.dao.RouteInfoDao;

@Controller
@ComponentScan({"com.oz"})
@RequestMapping(value="/travel/tripPlans")
public class TripPlansController {
	
	@Autowired
	TripPlansHelper tripPlansHelper;
	
	@Autowired
	CreateRouteService createRouteService;
//	@Autowired
//	public void setCreateRouteService(CreateRouteService createRouteService) {
//		this.createRouteService = createRouteService;
//	}
	
	@Autowired
	PrepLuggageService prepLuggageService;

	// TODO 戻るボタン
	

	/**
	 * createRoute ルート作成画面表示
	 * @return
	 */
    @RequestMapping(value = "/createRoute", method = RequestMethod.GET)
    public String createRoute(@ModelAttribute("tripPlansCommonForm") TripPlansCommonForm form, BindingResult result,
    		HttpServletRequest req, HttpServletResponse res, HttpSession session) {
    	
    	// new plan or existed plan
//    	boolean newPlanFlg = "0".equals(req.getAttribute("isNewPlan")) ? true : false ;
//    	boolean hasTripPlanNameFlg = createRouteService.searchTripPlanName(form);
//    	if (newPlanFlg == hasTripPlanNameFlg) {
//    		// either both are true or both are false, then rise an error.
//    		// todo
//    		return CommonConstant.NEWPLAN_URL;    		
//    	}
    	
    	/*
    	List<SpotInfo> l = new ArrayList<>();
    	SpotInfo s0 = new SpotInfo();
    	s0.setRecordNum(1);
    	s0.setAddress("test-0");
    	SpotInfo s1 = new SpotInfo();
    	s1.setRecordNum(2);
    	s1.setAddress("test-1");
    	l.add(s0);
    	l.add(s1);
    	form.setSpotList(l);
    	*/

    	List<SpotInfoEntity> resultList = createRouteService.selectRouteInfo(form);
    	
    	
    	tripPlansHelper.setCreateRouteModel(form);
    	
    	// trip plan name
    	form.setTripPlanName("sample_plan_20231019");

    	// @ModelAttribute があるのでModelの引数、この処理は省略する。
    	// model.addAttribute(tripPlansCommonForm);
        return CommonConstant.CREATEROUTE_URL;
    }

	/**
	 * createRoute ルート作成画面再表示
	 * @return
	 */
    /*
    @RequestMapping(value = "/createRoute", params="back", method = RequestMethod.GET)
    public String backCreateRoute(@ModelAttribute("tripPlansCommonForm") TripPlansCommonForm form, BindingResult result,
    		HttpServletRequest req, HttpServletResponse res, Model model) {
    	
    	List<TripPlansEntity> resultList = createRouteService.selectRouteInfo(form);

    	// @ModelAttribute があるのでModelの引数、この処理は省略する。
    	// model.addAttribute(tripPlansCommonForm);
    	System.out.println("back to create route");

    	return CommonConstant.CREATEROUTE_URL;
    }
    */

	/**
	 * prepLuggage 荷物準備画面表示
	 * @return
	 */
    @RequestMapping(value = "/createRoute/prepLuggage", method = RequestMethod.POST)
    public String prepLuggage(@ModelAttribute("tripPlansCommonForm") TripPlansCommonForm form, BindingResult result,
    		HttpServletRequest req, HttpServletResponse res, Model model) {
    	
    	/* todo: 単項目チェック */
    	
    	/* 相関チェック */
    	this.checkInput(form, result);
    	
    	if (result.hasErrors()) {
    		tripPlansHelper.setPrepLuggageModel(form);
    		return CommonConstant.CREATEROUTE_URL;
    	}
    	
    	tripPlansHelper.editCreateRouteForm(form);

    	// todo: service呼び出し
    	createRouteService.insertRouteInfo(form);
    	
    	/* luggageKeywordList */
    	LUGGAGE_KEYWORD_ITEMS a =  CommonConstant.LUGGAGE_KEYWORD_ITEMS.Travel_Items;
    	String[] s = a.getItems();
    	LUGGAGE_KEYWORD_ITEMS[] luggageKeywordItems 
    	= {CommonConstant.LUGGAGE_KEYWORD_ITEMS.Travel_Items,
				CommonConstant.LUGGAGE_KEYWORD_ITEMS.Morning,
				CommonConstant.LUGGAGE_KEYWORD_ITEMS.Afternoon,
				CommonConstant.LUGGAGE_KEYWORD_ITEMS.Night,
				CommonConstant.LUGGAGE_KEYWORD_ITEMS.Useful_Items};
    	req.setAttribute("luggageKeywordList", luggageKeywordItems);
    	
    	/* Bag No List */
    	req.setAttribute("bagNoArry", CommonConstant.BAG_NO_ARRY);
    	
    	tripPlansHelper.setPrepLuggageModel(form);
    	
    	return CommonConstant.PREPLUGGAGE_URL;
    }
    
	/**
	 * confirmPlans 確認画面表示
	 * @return
	 */
    @RequestMapping(value = "/createRoute/prepLuggage/confirmPlans", method = RequestMethod.POST)
    public String confirmPlans(@ModelAttribute("tripPlansCommonForm") TripPlansCommonForm form, BindingResult result,
    		HttpServletRequest req, HttpServletResponse res, Model model) {

    	/* prep luggage info */
    	/* todo: 単項目チェック  */
    	/* todo: 相関チェック  */
    	tripPlansHelper.editPrepLuggageForm(form);

    	// todo: service呼び出し
    	prepLuggageService.insertLuggageInfo(form);
    	
        	
    	
    	/* create route info */
    	List<SpotInfoEntity> resultList = createRouteService.selectRouteInfo(form);
    	// map the spot info
    	form.setSpotList(mapSpotInfo(resultList));
    	
    	form.setTripPlanName(form.getSpotList().get(0).getTripPlanName());

    	return CommonConstant.CONFIRM_PLANS_URL;
    }
    
    @RequestMapping(value = "/createRoute/prepLuggage/confirmPlans/resultTripPlans", method = RequestMethod.POST)
    public String resultTripPlans(@ModelAttribute("tripPlansCommonForm") TripPlansCommonForm form, BindingResult result,
    		HttpServletRequest req, HttpServletResponse res, Model model) {
    	
    	return CommonConstant.RESULT_TRIP_PLANS_URL;
    }

    private void checkInput(TripPlansCommonForm form, BindingResult result) {
		
    	// deep copied form for checking input
    	TripPlansCommonForm checkForm = SerializationUtils.clone(form);
    	
		List<SpotInfo> spotList = checkForm.getSpotList();
		
		/* record number */
		// duplicate check
		for (int i = 1; i < spotList.size(); i++) {
			if (spotList.get(i - 1) == spotList.get(i)) {
				result.rejectValue("recordNum", "");
			}
		}
			
		for (SpotInfo spot : spotList) {
			
			/* Address */
			// check if address contains either spot name or city
			if (!StringUtils.isEmpty(spot.getSpotName()) && !StringUtils.isEmpty(spot.getCity())) {
				// if ((new ArrayList<String>(Arrays.asList(spot.getSpotName(), spot.getCity()))
				// .stream().filter(s -> (spot.getAddress())
				// .contains(s))).collect(Collectors.toList()).size() > 0)

				String[] spotNameCity = new String[] {spot.getSpotName(), spot.getCity()};
				for (short i = 0; i < spotNameCity.length; i++) {
					if (!spot.getAddress().contains(spotNameCity[i])) {
						String field = i == 0 ? "spotName" : "city";
						result.rejectValue(field, "errors.address.match");						
					}
				}	
			}
			
			/*
			// Geometry Type
			for (String GEO_TYPE : CommonConstant.GEOMETRY_TYPES) {
				if (GEO_TYPE.equals(spot.getGeoType())) {
					result.rejectValue("geoType", "errors.geometry.type");
				}
			}
			*/
		}
	}
    
    private List<SpotInfo> mapSpotInfo(List<SpotInfoEntity> entityList) {
    	
    	List<SpotInfo> spotInfoList = new ArrayList<>();
    	
    	for (SpotInfoEntity entity : entityList) {
    		SpotInfo si = new SpotInfo();
    		
    		si.setTripPlanName(entity.getTripPlanName());
    		si.setRecordNum(entity.getRecordNum());
    		si.setSpotName(entity.getSpotName());
    		si.setCity(entity.getCity());
    		si.setAddress(entity.getAddress());
    		si.setLatitude(entity.getLatitude());
    		si.setLongitude(entity.getLongitude());
    		si.setLeafletId(entity.getLeafletId());
    		si.setGeoType(entity.getGeoType());
    		si.setInsUserId(entity.getInsUserId());
    		si.setInsDate(entity.getInsDate());
    		si.setUpdUserId(entity.getUpdUserId());
    		si.setUpdDate(entity.getUpdDate());
    		
    		spotInfoList.add(si);
    	}
    	return spotInfoList;
    }

}