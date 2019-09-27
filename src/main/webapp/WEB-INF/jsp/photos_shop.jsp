<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix='form' uri='http://www.springframework.org/tags/form'%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
<style>
.fakeimg {
	height: 200px;
	background: #aaa;
}
</style>
<c:set var="prefix" value="${pageContext.request.contextPath}" />
<c:set var="page" value="${page}" />
<c:set var="central_image" value="${central_image}" />
<c:set var="potos_list" value="${potos_list}" />
</head>
<body>

	<div class="jumbotron text-center" style="margin-bottom: 0">
		<h1>Logo</h1>
	</div>

	<div class="container" style="margin-top: 30px">
		<div align="left">
			<a href="${prefix}/">Меню</a>
		</div>
		<div class="row">
			<div class="col-sm-8">
			<div align="center">
				<h1>Отчет ${central_image.merch.name}, ${central_image.objectPlace.name}, ${central_image.date}</h1>
			</div>
				<img src="${central_image.pathFoPhoto}" class="img-fluid">

			</div>
			<div class="col-sm-4" style="height: 450px; overflow-y: scroll;">
				<div class="row">
					<div class="col-sm-6 p-1">
						<c:forEach var="photo" items="${potos_list}" begin="0" step="2"
							varStatus="iter">
							<c:if test="${iter.count/2>0}">
								<div class="col-sm-12 my-2">
									<form action="${prefix}/reports/shop_report/${photo.id}" method="post">
										<input type="hidden" name="_csrf" value="${_csrf.token}" />
										<input type="hidden" name="photos" value="<c:out value="${potos_list}"/>"/>
										<input class="img-fluid" type="image" alt="Photo"
											src="<c:url value="${photo.pathFoPhoto}"/>"/>
									</form>
								</div>
							</c:if>
						</c:forEach>
					</div>
					<div class="col-sm-6 p-1">
						<c:forEach var="photo" items="${potos_list}" begin="1" step="2"
							varStatus="iter">
							<c:if test="${iter.count/2>0}">
								<div class="col-sm-12 my-2">
									<form action="${prefix}/reports/shop_report/${photo.id}" method="post">
										<input type="hidden" name="_csrf" value="${_csrf.token}" />
										<input type="hidden" name="photos" value="<c:out value="${potos_list}"/>"/>
										<input class="img-fluid" type="image" alt="Photo"
											src="<c:url value="${photo.pathFoPhoto}"/>"/>
									</form>									
								</div>
							</c:if>
						</c:forEach>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>