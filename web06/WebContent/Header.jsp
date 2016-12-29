<%@page import="spms.vo.Member"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<div style="background-color:#00008b;color:#ffffff;height:20px;padding: 5px;">
SPMS(Simple Project Management System)
<!-- 아이디(이메일)이 로그인 하지 않을 경우 -->
<c:if test="${member.getEmail() eq null}">
	<span style="float:right;">
		<a style="color:white;" href="../auth/login.do">로그인</a>
	</span>
</c:if>
<!-- 아이디(이메일)이 로그인 후 -->
<c:if test="${member.getEmail() ne null}">
	<span style="float:right;">
		${member.getName()} 님
		<a style="color:white;" href="../auth/logout.do">로그아웃</a>
	</span>
</c:if>
<%-- <% if (member.getEmail() != null) { %>
<span style="float:right;">
<%=member.getName()%>
<a style="color:white;" 
  href="/logout.do">로그아웃</a>
</span>
<% } %> --%>
</div>