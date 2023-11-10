package com.oz.bean.common;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

public class TripPlansCommonForm implements Serializable{

	/** シリアルID */
	private static final long serialVersionUID = 1L;
	
	/** trip plan name */
	private String tripPlanName;
	
	/** spot info list */
	private List<SpotInfo> spotList;
	
	/** luggage info list */
	private List<LuggageInfo> luggageInfoList;

	

	public String getTripPlanName() {
		return tripPlanName;
	}

	public void setTripPlanName(String tripPlanName) {
		this.tripPlanName = tripPlanName;
	}
	
	public List<SpotInfo> getSpotList() {
		return spotList;
	}

	public void setSpotList(List<SpotInfo> spotList) {
		this.spotList = spotList;
	}

	public List<LuggageInfo> getLuggageInfoList() {
		return luggageInfoList;
	}

	public void setLuggageInfoList(List<LuggageInfo> luggageInfoList) {
		this.luggageInfoList = luggageInfoList;
	}

	
	
}
