package com.oz.consts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommonConstant {
	
	private CommonConstant() {
	}
	
	public static final List<String> LUGGAGE_KEYWORD_LIST = new ArrayList<>(
			Arrays.asList(
					"travel items"
					, "morning"
					, "afternoon"
					, "night"
					, "useful items"
					, "weather goods"
			)
	);
	
	public enum LUGGAGE_KEYWORD_ITEMS {
		Travel_Items("camera", "smartphone", "tent")
		, Morning("toothbrush", "hair brush", "face towel")
		, Afternoon("water bottle", "handkerchief")
		, Night("body towel", "glasses")
		, Useful_Items("sissors", "handy telescope", "watch")
		, Weather_Goods("umbrella")
		;
			
		private final String[] items;
		
		LUGGAGE_KEYWORD_ITEMS(String... items) {
			this.items = items;
		}
		
		public String[] getItems() {
			return this.items;
		}
	}
	
	// geometry type
	public static final String[] GEOMETRY_TYPES = {
			"POINT", "LINESTRING", "POLYGON", "POLYHEDRALSURFACE", "TRIANGLE", 
			"MULTIPOINT", "MULTILINESTRING", "MULTIPOLYGON", "TIN", "GEOMETRYCOLLECTION"
	};
	
	// bag No 
	public static final String[] BAG_NO_ARRY = {
			"-",
			"01",
			"02"
	};
	
	/** 画面 */
	public static final String NEWPLAN_URL = "tripPlans/newPlan";
	public static final String CREATEROUTE_URL = "/tripPlans/createRoute";
	public static final String PREPLUGGAGE_URL = "/tripPlans/prepLuggage";
	public static final String CONFIRM_PLANS_URL = "/tripPlans/confirmPlans";
	public static final String RESULT_TRIP_PLANS_URL = "/tripPlans/resultTripPlans";
}
