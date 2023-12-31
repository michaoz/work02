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

import com.oz.bean.common.SpotInfo;
import com.oz.bean.common.TripPlansCommonForm;
import com.oz.consts.DBConst;
import com.oz.entity.SpotInfoEntity;

@Repository
public class TripPlanDaoImpl implements TripPlanDao {

	/** フィールドの数 */
	private static final short SPOT_INFO_FIELD_COUNT = 12;

	/** user id */
	private static final String USER_ID = "UIRI001";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private BaseDao baseDao;
	
	protected Connection conn = null;

	private PreparedStatement pstmt = null;

	private Statement stmt = null;
	
	private ResultSet rs = null;
	
	@Override
	public String selectTripPlanName(TripPlansCommonForm form, String condition, Object[] paramArry) throws SQLException {
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT TRIP_PLAN_NAME FROM T_ROUTE_INFO ");
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
			System.out.println(sb.toString());
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
		return "";
	}
	
	
		
	@Override
	public void insert(TripPlansCommonForm form) throws SQLException {
		
		// get current timestamp
		Timestamp systemTimestamp = new Timestamp(System.currentTimeMillis());
		System.out.println("--current timestamp(System.currentTimeMillis):" + new SimpleDateFormat("yyyymmdd hh:mm:ss.sss").format(new Timestamp(System.currentTimeMillis())));
		System.out.println("--current timestamp(new Date):" + new SimpleDateFormat("yyyymmdd hh:mm:ss.sss").format(new Date()));
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO T_ROUTE_INFO");
		sql.append("( TRIP_PLAN_NAME ");
		sql.append(", RECORD_NUM ");
		sql.append(", SPOT_NAME ");
		sql.append(", CITY ");
		sql.append(", ADDRESS ");
		sql.append(", LATITUDE ");
		sql.append(", LONGITUDE ");
		sql.append(", LEAFLET_ID ");
		sql.append(", GEO_TYPE ");
		sql.append(", INS_USER_ID ");
		sql.append(", INS_DATE ");
		sql.append(", UPD_USER_ID ");
		sql.append(", UPD_DATE ) ");
		sql.append("VALUES ");
		sql.append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ");
		
		/*
		for (SpotInfo si : form.getSpotList()) {
			sql.append("( ");
			sql.append(si.getRecordNum());
			sql.append(si.getSpotName());
			sql.append(si.getCity());
			sql.append(si.getAddress());
			sql.append(si.getLatitude());
			sql.append(si.getLongitude());
			sql.append(si.getLeafletId());
			sql.append(si.getGeoType());
			sql.append(si.getInsUserId());
			sql.append(si.getInsDate());
			sql.append(si.getUpdUserId());
			sql.append(si.getUpdDate());
			sql.append("), ");			
		}
//		sql.append("WHERE RECORD_NUM = ? AND LEAFLET_ID = ? AND GEO_TYPE = ? AND INS_DATE = ?");
		sql.substring(0, sql.lastIndexOf(","));
		*/
		
		sql.append(";");
		System.out.println("sql:" + sql.toString());
		
		Object[][] params = new Object[form.getSpotList().size()][];
		List<SpotInfo> spotList = form.getSpotList();
		
		String condition = null;
		
		try {
			// get db connection
			conn = baseDao.getConnection(conn);
			
			// get statement
			pstmt = conn.prepareStatement(sql.toString());
			
			for (SpotInfo spotInfo : spotList) {
				int i = 0;
				pstmt.setString(++i, form.getTripPlanName());
				pstmt.setInt(++i, spotInfo.getRecordNum());
				pstmt.setString(++i, spotInfo.getSpotName());
				pstmt.setString(++i, spotInfo.getCity());
				pstmt.setString(++i, spotInfo.getAddress());
				pstmt.setString(++i, spotInfo.getLatitude());
				pstmt.setString(++i, spotInfo.getLongitude());
				pstmt.setString(++i, spotInfo.getLeafletId());
				pstmt.setString(++i, spotInfo.getGeoType());
				pstmt.setString(++i, USER_ID);
				pstmt.setTimestamp(++i, systemTimestamp);
				pstmt.setString(++i, USER_ID);
				pstmt.setTimestamp(++i, systemTimestamp);
				System.out.println("index(columns count):" + i);
				
				pstmt.addBatch();
				System.out.println("pstmt:" + pstmt.toString());
			}
			
//			int cnt = stmt.executeUpdate(sql.toString());
			int[] result = pstmt.executeBatch();
			// result
			Arrays.stream(result).forEach(System.out::println);
			
			// INSERT結果が取得できない場合は例外処理を行う
			for (int resultCount : result) {
				if (resultCount <= 0) {
					throw new SQLException();
				}
			}
			
			// Set the latest data to model to synchronize with DB data
			spotList.forEach(s -> s.setInsUserId(USER_ID));
//			spotList.forEach(s -> s.setInsDate(systemTimestamp));
			spotList.forEach(s -> s.setUpdUserId(USER_ID));
//			spotList.forEach(s -> s.setUpdDate(systemTimestamp));
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			baseDao.closeConnection(conn);
			baseDao.closeStatement(pstmt, rs);
		}
	}
	
}
