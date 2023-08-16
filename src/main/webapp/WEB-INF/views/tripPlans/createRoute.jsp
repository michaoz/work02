<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<%@ include file="../common/includes.jsp"%>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>		
		<!-- JQueryUI : to use Sortable function -->
		<script type="text/javascript" src="https://code.jquery.com/ui/1.12.0/jquery-ui.min.js"></script>
		<!-- JQueryUI : to use Sortable function end -->
		<link href="<c:url value='/resources/CSS/common.css' />" rel="stylesheet">
		<link href="<c:url value='/resources/CSS/trip-plans/createRoute.css' />" rel="stylesheet">

		<!-- OpenStreetMapを使用するためのLeafletライブラリ  --> 
		<link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.2/dist/leaflet.css"
	     integrity="sha256-sA+zWATbFveLLNqWO2gtiw3HL/lh1giY/Inf1BJ0z14="
	     crossorigin=""/>
     	<!-- Make sure you put this AFTER Leaflet's CSS -->
     	<!-- <script src="https://unpkg.com/leaflet@1.8.0/dist/leaflet.js"
        integrity="sha512-BB3hKbKWOc9Ez/TAwyWxNXeoV9c1v6FIeYiBieIWkpLjauysF18NzgR1MBNBXf8/KABdlkX68nAhlwcDFLGPCQ=="
        crossorigin=""></script> -->
        <script src="https://unpkg.com/leaflet@1.9.2/dist/leaflet.js"
	     integrity="sha256-o9N1jGDZrf5tS+Ft4gbIK7mYMipq9lqpVJ91xHSyKhg="
	     crossorigin=""></script>
		<!-- OpenStreetMapを使用するためのLeafletライブラリend  --> 
				
		<!--<script type="text/javascript" src="../../resources/js/map.js" ></script>-->
		
		<title>create</title>
	</head>
	<body>
		<jsp:include page = "../common/header.jsp"/>
		<div class="page-breadcrumb">
			<a  id="previous-page-name">Trip Planning</a>
			<span> > </span>
			<a  id="previous-page-name">Make New Plans</a>
			<span> > </span>
			<p id="current-page-name">Create a Route to the Destination</p>
		</div>		
		<div class="page-top-nav">
		  <div class="page-top-nav-links">
			  <div id="about"><p>About</p></div>
			  <div id="community"><p>Community</p></div>
			  <div id="blog"><p>Blog</p></div>
		  </div>
		</div>
		<div class="page-main-image">
			<c:set var="reloadCnt" value="1"/>
			<c:out value="<br>reloadCnt: ${reloadCnt}<br>" />
			<%-- TODO <c:if test="${empty initDisplayFlg or initDisplayFlg == 0}">  --%> 
			<c:if test="${empty reloadCnt or reloadCnt == 0}">
			  <div id="worldMap">
			    <img src="../../resources/images/WorldMap02.png">
			  </div>
			</c:if>
			<%-- TODO <c:if test="${initDisplayFlg > 0}">  --%>
			<div id="mapContainer"></div>
  			<%-- <form:input path="initDisplayFlg" value="${reloadCnt}" />  --%> 
			<div class="search-box">
	           <div class="form-field">
	               <input type="search" name="search" placeholder="Search..." id="content-to-search" value="東京駅">
	               <input type="button" value="Search" id="search-button">
	           </div>
		     </div>
		</div>
		
		<%-- DIV: Map Result --%>
		<div class="map-search-results">
		  <div>
	      <table id="search-result-header-table">
	        <thead>
	          <tr>
      		    <th>Search Results</th>
	          </tr>
	        </thead>
	      </table>
	      <table id="search-result-table">
	        <thead>
	          <tr id="header-table">
	            <th>No</th>
			    <th>Adress</th>
			    <th style="display: none">LatLon</th>
			    <th>Add to list</th>
	          </tr>
	        </thead>
	      </table>
	      </div>
		</div>
		
		<button class="js-confirm css-confirm-btn">
		  <p>confirm the created route</p>
		</button>
		
		<%-- DIV: Spot List, Delete List --%>
		<div class="body-make-new-plans">
			<div id="spot-list-area">
			  <div id="page-title">
			    <p>Spot List</p>
			  </div>
			  <div class="css-spot-list-nav">
				  <div class="js-draw-route css-draw-route"><input type="checkbox" name="check"></div>
				  <button class="js-to-delete-list css-add-recode-button">send to Delete List</button>
				  <button class="js-to-sort-spot-list css-spot-list-table-btn">sort Spot List</button>
			  </div>
			  
			  <%-- remained for studying --%>
    	      <%-- <c:set var="spotListRecordNo" value="0"/> --%>
		      <%-- <span id="span-spotListRecordNo"><c:out value="${spotListRecordNo}" /></span> --%>
			  <%-- remained for studying end--%>
			  
		      <table id="spot-list-table">
		        <thead>
		          <tr id="header-table">
				    <th style="display: none"></th>
				    <th>Emit</th>
		            <th>No</th>
				    <th>Spot Name</th>
				    <th>City</th>
				    <th>Address</th>
		          </tr>
		        </thead>
		      </table>
		    </div>
			<div id="delete-list-area">
			  <div id="page-title">
			    <p>Delete List</p>
			  </div>
			  <button class="js-back-to-recode css-back-to-recode-button">back to recode</button>
			  <table id="delete-list-table">
		        <thead>
		          <tr id="header-table">
				    <th>Add</th>
		            <th>No</th>
				    <th>Spot Name</th>
		          </tr>
		        </thead>
		      </table>
		    </div>
		</div>
		
		<%-- DIV: confirm pane --%>
		<div class="body-create-route">
			<div id="page-title">
			  <p>The Created Route</p>
			</div>
		    <table>
		        <thead>
		            <tr>
	   	              <th>No</th>
				      <th>Spot Name</th>
				      <th>City</th>
				      <th>Address</th>
		            </tr>
		        </thead>
		    </table>
		</div>
		
		<%-- DIV: control the page --%>
		<div class="page-control">
		  <input type="button" value="Back to Make New Plans" id="back-to-make-new-plans">
		  <input type="button" value="Save the Created Route" id="submit">
		</div>
		
		<jsp:include page = "../common/footer.jsp"/>
	    <script type="text/javascript" src="../../resources/js/createRoute.js" defer></script>
	</body>
</html>