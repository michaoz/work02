package com.oz.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.oz.bean.common.TripPlansCommonForm;
import com.oz.entity.SpotInfoEntity;

public interface PrepLuggageService {

	public List<SpotInfoEntity> selectRouteInfo(TripPlansCommonForm form);
	
	public void insertLuggageInfo(TripPlansCommonForm form);
	
}
