<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<%@ include file="../common/includes.jsp"%>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
		<link href="<c:url value='/resources/CSS/common.css' />" rel="stylesheet">
		<link href="<c:url value='/resources/CSS/trip-plans/newPlan.css' />" rel="stylesheet">
		<script type="text/javascript" src="../../resources/js/top.js" ></script>
		
		<title>NewPlan</title>
		
	</head>
	<body>
		<jsp:include page = "../common/header.jsp"/>
		<div class="page-breadcrumb">
			<a  id="previous-page-name">Trip Planning</a>
			<span> > </span>
			<p id="current-page-name">Your Trip Plans-Make a New Plan</p>
		</div>		
		<div class="page-top-nav">
		  <div class="page-top-nav-links">
			  <div id="about"><p>About</p></div>
			  <div id="community"><p>Community</p></div>
			  <div id="blog"><p>Blog</p></div>
		  </div>
		</div>
		<div class="page-main-image">
			<!-- <img src="../../resources/images/planTrip.png"> --> 
		</div>
		<div class="body-make-new-plans">
			<div id="page-title">
				<p>Your Trip Plans /  <span>Make a New Plan</span></p>
			</div>
			<div id="new-plans-option">
				<a href="/work02/travel/tripPlans/createRoute">> Create a Route to the Destination</a>
				<p>Visit here to create a new route to your destinations.</p>
			</div>
			<div id="new-plans-option">
				<a href="">> Get Ideas</a>
				<p>Visit here to get ideas of your new trip from browsing great spots around the world.</p>
			</div>
		</div>

		<jsp:include page = "../common/footer.jsp"/>
	</body>
</html>
