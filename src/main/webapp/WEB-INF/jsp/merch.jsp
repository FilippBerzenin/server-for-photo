<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
</head>
<body>

	<div class="jumbotron text-center" style="margin-bottom: 0">
		<h1>Logo</h1>
	</div>

	<div class="container" align="center" style="margin-top: 30px">
		<div align="left">
			<a href="${prefix}/">Меню</a>
		</div>	
			<h1>Создание и отображение мерча</h1>
		<div class="row align-items-center">
		<!--List of merchs  -->
		<div class="col-sm-6">
		<table class="table table-hover table-sm">
			<thead class="table-info">
				<tr>
					<th>Список мерчей</th>
					<th>Update</th>
					<th>Delete</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="merch" items="${listOfEntites}">
					<tr>
						<td>${merch.name}</td>
						<td>
							<form action="${prefix}/merch_inner/show/${merch.id}"
								method="get">
								<input type="hidden" name="_csrf" value="${_csrf.token}" />
								<button type="submit" name="update" value="update">Update</button>
							</form>	
						</td>
						<td>										
							<form action="${prefix}/${page}/delete/${merch.id}"
								method="post">
								<input type="submit" value="delete" onclick="if (confirm('Are you sure you want to delete ${merch.name}?')) form.action='${prefix}/${page}/delete/${merch.id}'; else return false;" />
								<!-- <button type="submit" name="delete" value="Delete">Delete</button> -->
							</form>						
						<td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
			</div>
			<!--Add new merchs  -->
			<div class="col-sm-6">
				<div class="form-group"  style="margin-top: 20px">
				<form:form method="post" action="${prefix}/merch/create/" modelAttribute="new_merch">
					<table>
						<tr>
							<td><form:input path="name" placeholder="ФИО"/></td>
							<td><font color="red"><form:errors path="name" /></font></td>
						</tr>
						<tr>
							<td><form:input path="login" placeholder="логин" style="margin-top: 5px"/></td>
							<td><font color="red"><form:errors path="login" /></font></td>
						</tr>
						<tr>							
							<td><form:input path="pass" placeholder="пароль" style="margin-top: 5px"/></td>
							<td><font color="red"><form:errors path="pass" /></font></td>
						</tr>
						<tr>
							<td><button type="submit" style="margin-top: 5px">Добавить</button></td>
						</tr>
					</table>
				</form:form>
			</div>
			</div>
		</div>
	</div>
</body>
</html>