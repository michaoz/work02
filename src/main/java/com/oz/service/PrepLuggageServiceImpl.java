package com.oz.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oz.bean.common.SpotInfo;
import com.oz.bean.common.TripPlansCommonForm;
import com.oz.entity.SpotInfoEntity;
import com.oz.service.dao.BaseDao;
import com.oz.service.dao.LuggageInfoDao;

@Service
public class PrepLuggageServiceImpl implements PrepLuggageService {
	
	@Autowired
	BaseDao baseDao;
	
	@Autowired
	LuggageInfoDao luggageInfoDao;

	public List<SpotInfoEntity> selectRouteInfo(TripPlansCommonForm form) {
		List<SpotInfoEntity> resultList = new ArrayList<>();
		
		try {
			resultList = luggageInfoDao.select(form, null, null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;		
	}

	@Override
	public void insertLuggageInfo(TripPlansCommonForm form) {

		// DB登録
		try {
			luggageInfoDao.insert(form);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
