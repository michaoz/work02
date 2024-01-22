package com.oz.controller.tripPlans;

import java.net.URI;
import java.text.SimpleDateFormat;
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
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Conventions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import com.oz.bean.common.LuggageInfo;
import com.oz.bean.common.LuggageItem;
import com.oz.bean.common.SpotInfo;
import com.oz.bean.common.SpotInfo.ValidSpotInfo;
import com.oz.bean.common.TripPlansCommonForm;
import com.oz.consts.CommonConstant;
import com.oz.consts.CommonConstant.LUGGAGE_KEYWORD_ITEMS;
import com.oz.entity.SpotInfoEntity;
import com.oz.helper.TripPlansHelper;
import com.oz.service.CreateRouteService;
import com.oz.service.PrepLuggageService;
import com.oz.service.dao.RouteInfoDao;
//import com.sun.org.apache.xml.internal.utils.URI;
import com.sun.org.apache.xml.internal.utils.URI.MalformedURIException;

@Controller
@ComponentScan({"com.oz"})
@RequestMapping(value="/travel/tripPlans")
public class TripPlansController {
	
	@Autowired
	private Validator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
	}
	
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
    @RequestMapping(value = "/createRoute", method = RequestMethod.POST)
    public String createRoute(@ModelAttribute("tripPlansCommonForm") TripPlansCommonForm form, BindingResult result,
    		HttpServletRequest req, HttpServletResponse res, HttpSession session) {
    	
    	// input check
    	// - either newTripPlanName or existedTripPlanName must not be NULL
    	
    	
    	// new plan or existed plan
    	form.setNewPlanFlg(!StringUtils.isEmpty(form.getNewTripPlanName()));
    	boolean hasTripPlanNameFlg = false;
    	form.setTripPlanName(!StringUtils.isEmpty(form.getNewTripPlanName()) ? form.getNewTripPlanName() : form.getExistedTripPlanName());
    	if (!StringUtils.isEmpty(form.getTripPlanName())) {
    		// search trip plan name for DB
    		hasTripPlanNameFlg = createRouteService.searchTripPlanName(form.getTripPlanName());    		
    	}
    	if (form.isNewPlanFlg() == hasTripPlanNameFlg) {
    		// either both are true or both are false, then rise an error.
    		// todo
    		// req.setAttribute(CommonConstant.TRIP_PLAN_NAME_LIST, tripPlanNameList);
    		return CommonConstant.NEWPLAN_URL;    		
    	}
    	
    	if (hasTripPlanNameFlg) {
    		// get the previous data from db
        	form.setSpotList(createRouteService.selectRouteInfo(form));
    	}

    	if (ObjectUtils.isNotEmpty(form.getSpotList())) {
        	tripPlansHelper.setCreateRouteModel(form);    		
    	}
    	
    	// @ModelAttribute があるのでModelの引数、この処理は省略する。
    	// model.addAttribute(tripPlansCommonForm);
        return CommonConstant.CREATEROUTE_URL;
    }

    @RequestMapping(value = "/createRoute", params="back", method = RequestMethod.POST)
    public String createRouteBack(@ModelAttribute("tripPlansCommonForm") TripPlansCommonForm form, BindingResult result,
    		HttpServletRequest req, HttpServletResponse res, HttpSession session) {
    	
    	return CommonConstant.CREATEROUTE_URL;
    }
    
    @ModelAttribute("tripPlansCommonForm")
    public TripPlansCommonForm tripPlansCommonForm() {
        return new TripPlansCommonForm();
    }
    
    @RequestMapping(value = "/createRoute", method = RequestMethod.GET)
    public String createRouteGet(@ModelAttribute("tripPlansCommonForm") TripPlansCommonForm form, 
    		BindingResult result, Model model, 
    		HttpServletRequest req, HttpServletResponse res, HttpSession session, RedirectAttributes attr) {

    	if (model.getAttribute("errors") != null) {
    		for (ObjectError error : (List<ObjectError>)model.asMap().get("errors")) {
        		result.addError(error);
        	}
    	}

    	return CommonConstant.CREATEROUTE_URL;
    }

	/**
	 * prepLuggage 荷物準備画面表示
	 * @return
	 */
    @RequestMapping(value = "/createRoute/prepLuggage", method = RequestMethod.POST)
    public String prepLuggage(@Validated(ValidSpotInfo.class) @ModelAttribute("tripPlansCommonForm") TripPlansCommonForm form, BindingResult result,
		    HttpServletRequest req, HttpServletResponse res, Model model, RedirectAttributes attr, UriComponentsBuilder builder) {
    	
    	/* 単項目チェック(BeanValidation) */
    	if (result.hasErrors()) {
    		// redirect時にパラメーターを継承するために設定
    		// - 関数の引数にRedirectAttributesを追加
    		// - form, BindingResult, erros を設定する
    		attr.addFlashAttribute("tripPlansCommonForm", form);
    		attr.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "tripPlansCommonForm", result);
    		attr.addFlashAttribute("errors", result.getAllErrors());
    		
    		// validation error 時にurlが元の画面のものになるようにするための設定
    		// - 関数の引数にUriComponentsBuilderを追加する
    		URI location = builder.path("/travel/" + CommonConstant.CREATEROUTE_URL).build().toUri();
    		return "redirect:" + location.toString();

//    		return CommonConstant.CREATEROUTE_URL;
    	}
    	
    	/* 相関チェック */
    	this.checkInput(form, result);
    	if (result.hasErrors()) {
    		System.out.println("相関チェックエラー");
    	}
    	
    	if (result.hasErrors()) {
    		tripPlansHelper.setPrepLuggageModel(form);
    		return CommonConstant.CREATEROUTE_URL;
    	}
    	
    	tripPlansHelper.editCreateRouteForm(form);

    	// register the create-route data and delete the old data
    	createRouteService.insertDeleteRouteInfo(form);
//    	if (form.isNewPlanFlg()) {
//        	createRouteService.insertRouteInfo(form);    		
//    	} else {
//    		createRouteService.updateRouteInfo(form);
//    	}
    	
		// fetch the previous prep-luggage data from db
    	form.setLuggageInfoList(prepLuggageService.selectLuggageInfo(form));
    	
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

    @RequestMapping(value = "/createRoute/prepLuggage", params="back", method = RequestMethod.POST)
    public String prepLuggageBack(@ModelAttribute("tripPlansCommonForm") TripPlansCommonForm form, BindingResult result,
    		HttpServletRequest req, HttpServletResponse res, Model model) {
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

    	prepLuggageService.insertDeleteLuggageInfo(form);
//    	if (form.isNewPlanFlg()) {
//        	prepLuggageService.insertLuggageInfo(form);
//    	}
    	
    	/* create route info */
//    	List<SpotInfoEntity> resultList = createRouteService.selectRouteInfo(form);
//    	// map the spot info
//    	form.setSpotList(mapSpotInfo(resultList));
    	form.setSpotList(createRouteService.selectRouteInfo(form));

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
					spotNameCity[i] = spotNameCity[i].replaceFirst(CommonConstant.PREFIX_MATCH + CommonConstant.REGEX_SPACE, "")
							.replaceFirst(CommonConstant.REGEX_SPACE + CommonConstant.SUFFIX_MATCH, "");
					
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
		SimpleDateFormat sdf = new SimpleDateFormat(CommonConstant.DATETIMEFORMAT_HYPHEN_COLON);
		
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
    		si.setInsDate(sdf.format(entity.getInsDate()));
    		si.setUpdUserId(entity.getUpdUserId());
    		si.setUpdDate(sdf.format(entity.getUpdDate()));
    		
    		spotInfoList.add(si);
    	}
    	return spotInfoList;
    }

}