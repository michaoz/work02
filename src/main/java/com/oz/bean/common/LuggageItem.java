package com.oz.bean.common;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class LuggageItem implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/** trip plan name */
	private String tripPlanName;
		
	/** luggage number */
	private int luggageNo;
	
	/** bag No */
	private String bagNo;
	
	/** item number */
	@NotEmpty(message="{errors.itemNo.empty}")
	@Pattern(regexp="(^\\d$)", message="{errors.format.itemNo.integer}")
	private int itemNo;

	/** item name */
	@NotEmpty(message="{errors.itemName.empty}")
	@Size(min=1, max=1000, message="{errors.range.minMax}")
	private String itemName;
	
	/** the number of item */
	@NotEmpty(message="{errors.itemCount.empty}")
	@Size(min=1, max=32767, message="{errors.range.minMax}")
	private int itemCount;
	
	/** item prepared flg */
	private boolean itemPreparedFlg;
	
	/** item owner */
	private String itemOwnerName;

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

	public String getBagNo() {
		return bagNo;
	}

	public void setBagNo(String bagNo) {
		this.bagNo = bagNo;
	}
	
	public int getItemNo() {
		return itemNo;
	}

	public void setItemNo(int itemNo) {
		this.itemNo = itemNo;
	}
	
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
	
	public boolean isItemPreparedFlg() {
		return itemPreparedFlg;
	}

	public void setItemPreparedFlg(boolean itemPreparedFlg) {
		this.itemPreparedFlg = itemPreparedFlg;
	}

	public String getItemOwnerName() {
		return itemOwnerName;
	}

	public void setItemOwnerName(String itemOwnerName) {
		this.itemOwnerName = itemOwnerName;
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
