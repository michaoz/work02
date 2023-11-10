package com.oz.helper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.StringUtils;
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
    	
    }

    public void setPrepLuggageModel(TripPlansCommonForm form) {

    	// todo
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
    	// todo end
    	
    }
   
}