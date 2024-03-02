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
		<link href="<c:url value='/resources/CSS/trip-plans/confirmPlans.css' />" rel="stylesheet">
		<title>confirm plans</title>
	</head>
	<body>
	<jsp:include page = "../common/header.jsp"/>

	<form:form action="/work02/travel/tripPlans/createRoute/prepLuggage/confirmPlans/resultTripPlans" method="POST" modelAttribute="tripPlansCommonForm">
		<div class="page-breadcrumb">
			<a  id="previous-page-name">Trip Planning</a>
			<span> > </span>
			<a  id="previous-page-name">Make a New Plan</a>
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
<%-- 	<div class="page-main-image">
		</div>  --%>

		<%-- DIV: body --%>
		<div class="body-trip-plan-summary">
		<div class="body-trip-plan-name">
		  <p>Your Trip Plan </p>
		  <form:input path="tripPlanName" readonly="true" />
		</div>
		<div class="body-route-luggage-summary">
		    <%-- DIV: Trip Plan Summary --%>
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
	    	        <tbody>
	    	        <c:forEach items="${tripPlansCommonForm.spotList}" var="spotInfo" varStatus="spotInfoStatus">
  			            <tr id="${tripPlansCommonForm.spotList[spotInfoStatus.index].recordNum}">
			    	          <td id="spot-list-record-no-${tripPlansCommonForm.spotList[spotInfoStatus.index].recordNum}:00000" >
					            <form:hidden path="spotList[${spotInfoStatus.index}].recordNum" />
					            ${tripPlansCommonForm.spotList[spotInfoStatus.index].recordNum}
					          </td>
					          <td id="spot-list-record-content-spotName-${tripPlansCommonForm.spotList[spotInfoStatus.index].recordNum}:00000">
					            <form:hidden path="spotList[${spotInfoStatus.index}].spotName" />
					            ${tripPlansCommonForm.spotList[spotInfoStatus.index].spotName}
					          </td>
					          <td id="spot-list-record-content-city-${tripPlansCommonForm.spotList[spotInfoStatus.index].recordNum}:00000">
					            <form:hidden path="spotList[${spotInfoStatus.index}].city" />
					            ${tripPlansCommonForm.spotList[spotInfoStatus.index].city}
					          </td>
					          <td id="spot-list-record-content-address-${tripPlansCommonForm.spotList[spotInfoStatus.index].recordNum}:00000">
					            <form:hidden path="spotList[${spotInfoStatus.index}].address" />
					            ${tripPlansCommonForm.spotList[spotInfoStatus.index].address}
					          </td>
					      </tr>
	    	        </c:forEach>
	    	        </tbody>
    		    </table>
    		</div>
    		
    		<div id="luggage-list-area">
			  <div id="page-title">
			    <p>The Luggage List</p>
			  </div>
			  <div class="css-luggage-keyword-list">
			      <c:forEach items="${luggageKeywordList}" var="luggageKeyword" varStatus="status">
  				    <div class="js-luggage-keyword_${status.index} css-luggage-keyword">
  				      <input type="checkbox" name="check" id="luggage-keyword-${status.index}" />
  				      <label for="luggage-keyword-${status.index}">${luggageKeyword}</label>
  				    </div>
				  </c:forEach>
			  </div>
			  <div class="js-luggage-keyword-items css-luggage-keyword-items">
				  <c:forEach items="${luggageKeywordList}" var="luggageKeyword" varStatus="status">
				    <c:forEach items="${luggageKeyword.getItems()}" var="item" varStatus="item_status">
				      <div id="item-${status.index}-${item_status.index}">
				        <input type="button" id="item-btn-${status.index}-${item_status.index}" />
				        <label for="item-btn-${status.index}-${item_status.index}"><c:out value="${item}" /></label>
				      </div>
				    </c:forEach>
				  </c:forEach>
			  </div>
		      <table id="luggage-list-table">
		        <thead>
		          <tr id="header-table">
				    <th style="display: none"></th>
		            <th>No</th>
		            <th>BagNo</th>
				    <th colspan="2">Luggage</th>
		          </tr>
		        </thead>
		        <tbody>
		          <c:forEach items="${tripPlansCommonForm.luggageInfoList}" var="luggageInfo" varStatus="luggageInfoStatus">
		            <tr id="${luggageInfoStatus.index}">
		              <td><span></span></td>
			          <%--<td id="luggage-record-no-${luggageInfoStatus.index + (luggageItemStatus.index + 1)}" > --%>
			          <td id="luggage-record-no" >
			            <input type="hidden" value="${luggageInfoStatus.index + (luggageItemStatus.index + 1)}">
			            ${luggageInfoStatus.index + (luggageItemStatus.index + 1)}
			          </td>
			          <%-- <td id="luggage-bag-no-${luggageInfoStatus.index + (luggageItemStatus.index + 1)}" > --%>
			          <td id="luggage-bag-no" >
			            <form:hidden path="luggageInfoList[${luggageInfoStatus.index}].bagNo" />
			            ${luggageInfo.bagNo}
			          </td>
		              <td id="luggage-item-name" >
			          <c:forEach items="${tripPlansCommonForm.luggageInfoList[luggageInfoStatus.index].luggageItemList}" var="luggageItem" varStatus="luggageItemStatus">
				          <form:hidden path="luggageInfoList[${luggageInfoStatus.index}].luggageItemList[${luggageItemStatus.index}].itemName" />
				          ${luggageItem.itemName}
				          <br>
			          </c:forEach>
			          </td>
			          <td>
			          <c:forEach items="${tripPlansCommonForm.luggageInfoList[luggageInfoStatus.index].luggageItemList}" var="luggageItem" varStatus="luggageItemStatus">
				          <form:hidden path="luggageInfoList[${luggageInfoStatus.index}].luggageItemList[${luggageItemStatus.index}].itemCount" />
				          ${luggageItem.itemCount}&nbsp;&nbsp;
				          <br>
			          </c:forEach>
			          </td>
		            </tr>
		          </c:forEach>
		        </tbody>
		      </table>
		    </div>

		</div>
		</div>

		<%-- DIV: control the page --%>
		<div class="page-control">
		  <input type="submit" id="submit-back" class="css-back-button" value="Back to Previous Page" >
		  <input type="submit" id="submit" class="css-submit-button" value="Save All" >
		</div>

		<%-- hidden items --%>
		<c:forEach items="${tripPlansCommonForm.spotList}" var="spotInfo" varStatus="spotInfoStatus">
		  <form:hidden path="spotList[${spotInfoStatus.index}].tripPlanName" />
		  <form:hidden path="spotList[${spotInfoStatus.index}].latLon" />
		  <form:hidden path="spotList[${spotInfoStatus.index}].latitude" />
		  <form:hidden path="spotList[${spotInfoStatus.index}].longitude" />
		  <form:hidden path="spotList[${spotInfoStatus.index}].geoType" />
		  <form:hidden path="spotList[${spotInfoStatus.index}].insUserId" />
		  <form:hidden path="spotList[${spotInfoStatus.index}].insDate" />
		  <form:hidden path="spotList[${spotInfoStatus.index}].updUserId" />
		  <form:hidden path="spotList[${spotInfoStatus.index}].updDate" />	
		</c:forEach>
        <c:forEach items="${tripPlansCommonForm.luggageInfoList}" var="luggageInfo" varStatus="luggageInfoStatus">
	      <form:hidden path="luggageInfoList[${luggageInfoStatus.index}].insUserId" />
	      <form:hidden path="luggageInfoList[${luggageInfoStatus.index}].insDate" />
	      <form:hidden path="luggageInfoList[${luggageInfoStatus.index}].updUserId" />
	      <form:hidden path="luggageInfoList[${luggageInfoStatus.index}].updDate" />
        <c:forEach items="${tripPlansCommonForm.luggageInfoList[luggageInfoStatus.index].luggageItemList}" var="luggageItem" varStatus="luggageItemStatus">
          <form:hidden path="luggageInfoList[${luggageInfoStatus.index}].luggageItemList[${luggageItemStatus.index}].bagNo" />
          <form:hidden path="luggageInfoList[${luggageInfoStatus.index}].luggageItemList[${luggageItemStatus.index}].itemNo" />
	      <form:hidden path="luggageInfoList[${luggageInfoStatus.index}].luggageItemList[${luggageItemStatus.index}].itemPreparedFlg" />
	      <form:hidden path="luggageInfoList[${luggageInfoStatus.index}].luggageItemList[${luggageItemStatus.index}].itemOwnerName" />
	      <form:hidden path="luggageInfoList[${luggageInfoStatus.index}].luggageItemList[${luggageItemStatus.index}].insUserId" />
	      <form:hidden path="luggageInfoList[${luggageInfoStatus.index}].luggageItemList[${luggageItemStatus.index}].insDate" />
	      <form:hidden path="luggageInfoList[${luggageInfoStatus.index}].luggageItemList[${luggageItemStatus.index}].updUserId" />
	      <form:hidden path="luggageInfoList[${luggageInfoStatus.index}].luggageItemList[${luggageItemStatus.index}].updDate" />
        </c:forEach>
        </c:forEach>


	</form:form>

	<jsp:include page = "../common/footer.jsp"/>
	<script type="text/javascript" src="../../../../resources/js/tripPlans/confirmPlans.js" defer></script>
	</body>
</html>