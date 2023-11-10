package com.oz.service.dao;

import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.oz.bean.common.LuggageInfo;
import com.oz.bean.common.LuggageItem;
import com.oz.bean.common.SpotInfo;
import com.oz.bean.common.TripPlansCommonForm;
import com.oz.consts.DBConst;
import com.oz.entity.SpotInfoEntity;

@Repository
public class LuggageInfoDaoImpl implements LuggageInfoDao {

	/** フィールドの数 */
	private static final short SPOT_INFO_FIELD_COUNT = 12;

	/** user id */
	private static final String USER_ID = "UILI001";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private BaseDao baseDao;
	
	protected Connection conn = null;

	private PreparedStatement pstmt = null;

	private Statement stmt = null;
	
	private ResultSet rs = null;
	
	@Override
	public List<SpotInfoEntity> select(TripPlansCommonForm form, String condition, Object[] paramArry) throws SQLException {
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM T_ROUTE_INFO ");
		if (condition != null) {
			sb.append(condition);
		}
		if (paramArry != null && paramArry.length > 0) {
			// TODO
		}
		sb.append(";");
		
		List<SpotInfoEntity> resultList = new ArrayList<>();
		try {
			// get db connection
			conn = baseDao.getConnection(conn);
			// get statement
			stmt = conn.createStatement();
			// execute sql
			rs = stmt.executeQuery(sb.toString());
						
			while (rs.next()) {
				System.out.println(rs.getRow() + "件目-------------");
				
				SpotInfoEntity resultEntity = new SpotInfoEntity();
				
			    resultEntity.setTripPlanName(rs.getString("TRIP_PLAN_NAME"));
			    resultEntity.setRecordNum(rs.getInt("RECORD_NUM"));
				resultEntity.setSpotName(rs.getString("SPOT_NAME"));
				resultEntity.setCity(rs.getString("CITY"));
				resultEntity.setAddress(rs.getString("ADDRESS"));
				resultEntity.setLatitude(rs.getString("LATITUDE"));
				resultEntity.setLongitude(rs.getString("LONGITUDE"));
				resultEntity.setLeafletId(rs.getString("LEAFLET_ID"));
				resultEntity.setGeoType(rs.getString("GEO_TYPE"));
				resultEntity.setInsUserId(rs.getString("INS_USER_ID"));
				resultEntity.setInsDate(rs.getTimestamp("INS_DATE"));
				resultEntity.setUpdUserId(rs.getString("UPD_USER_ID"));
				resultEntity.setUpdDate(rs.getTimestamp("UPD_DATE"));
				
				resultList.add(resultEntity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			baseDao.closeConnection(conn);
			baseDao.closeStatement(pstmt, rs);
		}
		return resultList;
	}
	
	
		
	@Override
	public void insert(TripPlansCommonForm form) throws SQLException {
		
		// get current timestamp
		Timestamp systemTimestamp = new Timestamp(System.currentTimeMillis());
		System.out.println("--current timestamp(System.currentTimeMillis):" + new SimpleDateFormat("yyyymmdd hh:mm:ss.sss").format(new Timestamp(System.currentTimeMillis())));
		System.out.println("--current timestamp(new Date):" + new SimpleDateFormat("yyyymmdd hh:mm:ss.sss").format(new Date()));
		
		StringBuilder sqlLuggageInfo = new StringBuilder();
		StringBuilder sqlLuggageItem = new StringBuilder();
		
		sqlLuggageInfo.append("INSERT INTO T_LUGGAGE_INFO");
		sqlLuggageInfo.append("( TRIP_PLAN_NAME ");
		sqlLuggageInfo.append(", LUGGAGE_NO ");
		sqlLuggageInfo.append(", LUGGAGE_COUNT ");
		sqlLuggageInfo.append(", BAG_NO ");
		sqlLuggageInfo.append(", LUGGAGE_PREPARED_FLG ");
		sqlLuggageInfo.append(", INS_USER_ID ");
		sqlLuggageInfo.append(", INS_DATE ");
		sqlLuggageInfo.append(", UPD_USER_ID ");
		sqlLuggageInfo.append(", UPD_DATE ");
		sqlLuggageInfo.append(") VALUES ");
		sqlLuggageInfo.append("(?, ?, ?, ?, ?, ?, ?, ?, ? ) ");
		
		sqlLuggageItem.append("INSERT INTO T_LUGGAGE_ITEM");
		sqlLuggageItem.append("( TRIP_PLAN_NAME ");
		sqlLuggageItem.append(", LUGGAGE_NO ");
		sqlLuggageItem.append(", BAG_NO ");
		sqlLuggageItem.append(", ITEM_NO ");
		sqlLuggageItem.append(", ITEM_NAME ");
		sqlLuggageItem.append(", ITEM_COUNT ");
		sqlLuggageItem.append(", ITEM_PREPARED_FLG ");
		sqlLuggageItem.append(", ITEM_OWNER_NAME ");
		sqlLuggageItem.append(") VALUES ");
		sqlLuggageItem.append("(?, ?, ?, ?, ?, ?, ?, ? ) ");
		
		sqlLuggageInfo.append(";");
		sqlLuggageItem.append(";");
		System.out.println("sqlLuggageInfo:" + sqlLuggageInfo.toString());
		System.out.println("sqlLuggageItem:" + sqlLuggageItem.toString());
		
		Object[][] params = new Object[form.getLuggageInfoList().size()][];
		List<LuggageInfo> luggageInfoList = form.getLuggageInfoList();
		
		String condition = null;
		
		try {
			// get db connection
			conn = baseDao.getConnection(conn);
			
			/* INSERT INTO T_LUGGAGE_INFO */
			// get statement
			pstmt = conn.prepareStatement(sqlLuggageInfo.toString());
			
			for (LuggageInfo luggageInfo : luggageInfoList) {
				int i = 0;
				pstmt.setString(++i, form.getTripPlanName());
				pstmt.setInt(++i, luggageInfo.getLuggageNo());
				pstmt.setInt(++i, luggageInfo.getLuggageCount());
				pstmt.setString(++i, luggageInfo.getBagNo());
				pstmt.setBoolean(++i, luggageInfo.isLuggagePrepaedFlg());
				pstmt.setString(++i, USER_ID);
				pstmt.setTimestamp(++i, systemTimestamp);
				pstmt.setString(++i, USER_ID);
				pstmt.setTimestamp(++i, systemTimestamp);
				System.out.println("index(columns count):" + i);
				
				pstmt.addBatch();
				System.out.println("pstmt:" + pstmt.toString());
			}
			
//			int cnt = stmt.executeUpdate(sql.toString());
			int[] resultLuggageInfo = pstmt.executeBatch();
			// result
			Arrays.stream(resultLuggageInfo).forEach(System.out::println);
			// INSERT結果が取得できない場合は例外処理を行う
			for (int resultCount : resultLuggageInfo) {
				if (resultCount <= 0) {
					throw new SQLException();
				}
			}			
			/* INSERT INTO T_LUGGAGE_INFO end */
			
			/* INSERT INTO T_LUGGAGE_ITEM */
			// get statement
			pstmt = null;
			pstmt = conn.prepareStatement(sqlLuggageItem.toString());
			
			for (LuggageInfo luggageInfo : luggageInfoList) {
				for (LuggageItem luggageItem : luggageInfo.getLuggageItemList()) {
					int i = 0;
					pstmt.setString(++i, form.getTripPlanName());
					pstmt.setInt(++i, luggageInfo.getLuggageNo());
					pstmt.setString(++i, luggageInfo.getBagNo());
					pstmt.setInt(++i, luggageItem.getItemNo());
					pstmt.setString(++i, luggageItem.getItemName());
					pstmt.setInt(++i, luggageItem.getItemCount());
					pstmt.setBoolean(++i, luggageItem.isItemPreparedFlg());
					pstmt.setString(++i, luggageItem.getItemOwnerName());
					System.out.println("index(columns count):" + i);
					
					pstmt.addBatch();
					System.out.println("pstmt:" + pstmt.toString());
				}
			}
//			int cnt = stmt.executeUpdate(sql.toString());
			int[] resultLuggageItem = pstmt.executeBatch();
			// result
			Arrays.stream(resultLuggageItem).forEach(System.out::println);
			// INSERT結果が取得できない場合は例外処理を行う
			for (int resultCount : resultLuggageItem) {
				if (resultCount <= 0) {
					throw new SQLException();
				}
			}			
			/* INSERT INTO T_LUGGAGE_ITEM end */

			// Set the latest data to model to synchronize with DB data
			updateForm(form, systemTimestamp);			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			baseDao.closeConnection(conn);
			baseDao.closeStatement(pstmt, rs);
		}
	}
	
	private void updateForm(TripPlansCommonForm form, Timestamp systemTimestamp) {
		
		List<LuggageInfo> luggageInfoList = form.getLuggageInfoList();
		
		luggageInfoList.forEach(s -> s.setInsUserId(USER_ID));
		luggageInfoList.forEach(s -> s.setInsDate(systemTimestamp));
		luggageInfoList.forEach(s -> s.setUpdUserId(USER_ID));
		luggageInfoList.forEach(s -> s.setUpdDate(systemTimestamp));
	}
	
}
