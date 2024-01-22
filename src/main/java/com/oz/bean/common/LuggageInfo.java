package com.oz.bean.common;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public final class LuggageInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/** trip plan name */
	private String tripPlanName;
	
	/** luggage number */
	@NotEmpty(message="{errors.luggageNo.empty}")
	@Size(min=1, max=32767, message="{errors.range.minMax}")
	private int luggageNo;
	
	/** the number of the luggage */
	@NotEmpty(message="{errors.luggageCount.empty}")
	@Size(min=1, max=1000, message="{errors.range.minMax}")
	private int luggageCount;
	
	/** bag No */
	@NotEmpty(message="{errors.bagNo.empty}")
	private String bagNo;
	
	/** luggage prepared flg */
	private boolean luggagePrepaedFlg;
	
	/** luggage item list */
	private List<LuggageItem> luggageItemList;
	
	/** ins user id */
	private String insUserId;
	
	/** ins date */
	private String insDate;
	
	/** update user id */
	private String updUserId;
	
	/** update date */
	private String updDate;

	
	

	public String getTripPlanName() {
		return tripPlanName;
	}

	public void setTripPlanName(String tripPlanName) {
		this.tripPlanName = tripPlanName;
	}
	
	public int getLuggageNo() {
		return luggageNo;
	}

	public void setLuggageNo(int luggageNo) {
		this.luggageNo = luggageNo;
	}

	public int getLuggageCount() {
		return luggageCount;
	}

	public void setLuggageCount(int luggageCount) {
		this.luggageCount = luggageCount;
	}

	public String getBagNo() {
		return bagNo;
	}

	public void setBagNo(String bagNo) {
		this.bagNo = bagNo;
	}

	public boolean isLuggagePrepaedFlg() {
		return luggagePrepaedFlg;
	}

	public void setLuggagePrepaedFlg(boolean luggagePrepaedFlg) {
		this.luggagePrepaedFlg = luggagePrepaedFlg;
	}

	public List<LuggageItem> getLuggageItemList() {
		return luggageItemList;
	}

	public void setLuggageItemList(List<LuggageItem> luggageItemList) {
		this.luggageItemList = luggageItemList;
	}
	
	public String getInsUserId() {
		return insUserId;
	}

	public void setInsUserId(String insUserId) {
		this.insUserId = insUserId;
	}

	public String getInsDate() {
		return insDate;
	}

	public void setInsDate(String insDate) {
		this.insDate = insDate;
	}

	public String getUpdUserId() {
		return updUserId;
	}

	public void setUpdUserId(String updUserId) {
		this.updUserId = updUserId;
	}

	public String getUpdDate() {
		return updDate;
	}

	public void setUpdDate(String updDate) {
		this.updDate = updDate;
	}


}
