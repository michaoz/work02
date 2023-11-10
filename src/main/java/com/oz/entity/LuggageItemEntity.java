package com.oz.entity;

import java.io.Serializable;

public class LuggageItemEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/** trip plan name */
	private String tripPlanName;
	
	/** luggage number */
	private int luggageNo;

	/** bag No */
	private String bagNo;
	
	/** item number */
	private int itemNo;

	/** item name */
	private String itemName;
	
	/** the number of item */
	private int itemCount;
	
	/** item prepared flg */
	private boolean itemPreparedFlg;
	
	/** item owner */
	private String itemOwnerName;

	
	
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
}
