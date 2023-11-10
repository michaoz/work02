package com.oz.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
//import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oz.bean.common.SpotInfo;
import com.oz.bean.common.TripPlansCommonForm;
import com.oz.entity.SpotInfoEntity;
import com.oz.service.dao.BaseDao;
import com.oz.service.dao.RouteInfoDao;

@Service
public class CreateRouteServiceImpl implements CreateRouteService {
//	@Autowired
//	ModelMapper modelMapper;
	
	@Autowired
	BaseDao baseDao;
	
	@Autowired
	RouteInfoDao routeInfoDao;

	public List<SpotInfoEntity> selectRouteInfo(TripPlansCommonForm form) {
		List<SpotInfoEntity> resultList = new ArrayList<>();
		
		String condition = "WHERE UPD_DATE = ( SELECT MAX(UPD_DATE) FROM T_ROUTE_INFO ) ";
		try {
			// get the latest data
			resultList = routeInfoDao.select(form, condition, null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < resultList.size(); i++) {
			SpotInfoEntity sie = resultList.get(i);
			SpotInfo si = form.getSpotList().get(i);
			
        	// Mapping modelMapper
//			si = modelMapper.map(sie, SpotInfo.class);
//			modelMapper.map(sie, si);
        	
    	}
		return resultList;		
	}

	public boolean searchTripPlanName(TripPlansCommonForm form) {
		// flg if there's the trip plan name in DB
		boolean hasTripPlanName = false;
		
		String condition = "WHERE TRIP_PLAN_NAME = ? ";
    	String tripPlanName = null;
		try {
			tripPlanName = routeInfoDao.selectTripPlanName(form, condition, new Object[]{form.getTripPlanName()});
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (!StringUtils.isEmpty(tripPlanName)) {
			hasTripPlanName = true;
		}

		return hasTripPlanName;		
	}
		
	public void insertRouteInfo(TripPlansCommonForm form) {
		// DB登録
		try {
			routeInfoDao.insert(form);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}
