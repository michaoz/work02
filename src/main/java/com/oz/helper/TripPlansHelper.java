package com.oz.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.util.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

/**
 * Helperクラス
 * Formの編集などを行う
 * @author abmg3
 *
 */
@Component
public class TripPlansHelper {
	
    public void editCreateRouteForm(TripPlansCommonForm form) {
    	
    	List<SpotInfo> formSpotList = form.getSpotList();
    	
    	/* Sort SpotList By Record Number */
//    	formSpotList.sort((x, y) -> x.getRecordNum() - y.getRecordNum());
//    	formSpotList = formSpotList.stream()
//    			.sorted((x, y) -> x.getRecordNum() - y.getRecordNum())
//    			.collect(Collectors.toList());;
    	List<SpotInfo> newSpotList = formSpotList.stream()
    			.sorted(Comparator.comparing(fs -> fs.getRecordNum()))
    			.collect(Collectors.toList());
    	
    	/* trip plan name */
    	newSpotList.forEach(x -> x.setTripPlanName(form.getTripPlanName()));
    	
    	/* city */
    	/* address */
    	for (SpotInfo si : newSpotList) {
    		si.setCity(removeSpace(si.getCity()));
    		si.setAddress(removeSpace(si.getAddress()));
    	}
    	
    	/* Set GeoType:Point */
    	newSpotList.forEach(fs -> fs.setGeoType("Point"));

    	/* Set Latitude and Longitude (fs.getLatLon() is not null) */
    	newSpotList.forEach(fs -> fs.setLatitude(
    			fs.getLatLon().substring(0, fs.getLatLon().indexOf("_"))));
    	newSpotList.forEach(fs -> fs.setLongitude(
    			fs.getLatLon().substring(fs.getLatLon().indexOf("_") + 1)));
    	
    	/* Set Leaflet Id */
//    	newSpotList.forEach(fs -> fs.setLeafletId(
//    			fs.getLeafletId().substring(fs.getLeafletId().indexOf(":") + 1)));
    	newSpotList.forEach(fs -> fs.setLeafletId(removeSpace(fs.getLeafletId())));
    	
    	form.setSpotList(newSpotList);
    }
    
    public void editPrepLuggageForm(TripPlansCommonForm form) {
    	List<LuggageInfo> luggageInfoList = form.getLuggageInfoList();
    	
    	for (LuggageInfo li : luggageInfoList) {
    		List<LuggageItem> luggageItemList = li.getLuggageItemList();
//    		for (LuggageItem lim :luggageItemList) {
//    			if ("-".equals(lim.getBagNo())) {
//    				lim.setBagNo("00");
//    			}
//    		}
    		// Set Bag No from the first Luggage Item
    		li.setBagNo(luggageItemList.get(0).getBagNo());    		
    	}
    }

    public void setCreateRouteModel(TripPlansCommonForm form) {

    	// trip plan name
    	form.setTripPlanName(form.getTripPlanName());
    	// ★ temp
    	if (StringUtils.isEmpty(form.getTripPlanName())) {
        	SimpleDateFormat sdfDate = new SimpleDateFormat(CommonConstant.DATEFORMAT_NO_PUNCTUATION);
        	SimpleDateFormat sdfTime = new SimpleDateFormat(CommonConstant.TIMEFORMAT_NO_PUNCTUATION);
        	String currentDate = sdfDate.format(new Date()) + "_" + sdfTime.format(new Date());
        	form.setTripPlanName("new-plan_" + currentDate);    		
    	}
    	
    	List<SpotInfo> spotList = form.getSpotList();
    	for (SpotInfo si : spotList) {
    		si.setLatLon(removeSpace(si.getLatitude()) + "_" + removeSpace(si.getLongitude()));
    	}

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

    }

    public void setPrepLuggageModel(TripPlansCommonForm form) {

    	// todo
    	/*
    	LuggageItem lim1 = new LuggageItem();
    	lim1.setItemNo(1);
    	lim1.setItemName("apple");
    	lim1.setItemCount(3);
    	lim1.setBagNo("01");
    	lim1.setItemOwnerName("user");
    	LuggageItem lim2 = new LuggageItem();
    	lim2.setItemNo(2);
    	lim2.setItemName("banana");
    	lim2.setItemCount(5);
    	lim2.setBagNo("01");
    	lim2.setItemOwnerName("user");
    	List<LuggageItem> limList = new ArrayList<>();
    	limList.add(lim1);
    	limList.add(lim2);
    	
    	LuggageInfo li = new LuggageInfo();
    	li.setLuggageNo(1);
    	li.setLuggageCount(1);
    	li.setBagNo("01");
    	li.setLuggageItemList(limList);
    	List<LuggageInfo> liList = new ArrayList<>();
    	liList.add(li);
    	form.setLuggageInfoList(liList);
    	*/
    	// todo end
    	
    }
    
    /**
     * 半角・全角スペースを削除する
     * @param targetStr
     * @return
     */
    private String removeSpace(String targetStr) {
    	targetStr = targetStr.replaceFirst(CommonConstant.PREFIX_MATCH + CommonConstant.REGEX_SPACE, "")
		.replaceFirst(CommonConstant.REGEX_SPACE + CommonConstant.SUFFIX_MATCH, "");
    	
    	// remove CR
    	targetStr = targetStr.replaceAll("[" + CommonConstant.REGEX_CR + "]", "");
    	// remove Tab
    	targetStr = targetStr.replaceAll("[" + CommonConstant.REGEX_TAB + "]", "");
    	// remove Space
    	targetStr = targetStr.replaceAll("[" + CommonConstant.REGEX_HALF_SPACE + "]", "");
    	
    	return targetStr;
    }
   
}