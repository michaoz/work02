<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<%@ include file="../common/includes.jsp"%>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
		<link href="<c:url value='/resources/CSS/common.css' />" rel="stylesheet">
		<link href="<c:url value='/resources/CSS/trip-plans/createRoute.css' />" rel="stylesheet">

		<title>dialog confirm no inputs</title>
	</head>
	<body>	

	<jsp:include page = "../common/footer.jsp"/>
	<script type="text/javascript" src="../../resources/js/tripPlans/createRoute.js" defer></script>
	</body>
</html>