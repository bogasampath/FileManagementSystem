<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Dashboard - File Manager</title>
<style type="text/css">
.tdBackgroundEven {
	background-color: #ffffff;
}
.tdBackgroundOdd {
	background-color: #ebebeb;
}
.uploadBtnCss {
	text-decoration: none !important;
	border-color: #d9534f;
	padding: 10px;
	color: #fff !important;
	border-radius: 5px;
	background-color: #d9534f;
	font-size: 13px;
	font-weight: normal;
	font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
}
.headerTitle {
	font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
	font-weight: normal;
	color: #fff;
	font-size: 21px;
}
.dataTableClass {
	width:100%;
	border: 0px solid #f1f1f1 !important;
}
.tableHeaderRowClass {
	text-align: center;
	 height: 45px;
	 background-color: #1a1f25;
	 color: #d2cbcb;
	 font-size:14px;
}
.headerPartClass {
	padding:25px;
	background-color: #2db3ac;
	font-size:21px;
	color:#FFF;
	height:75px;
}
.dataPartClass {
	padding:25px 0px;
	padding-bottom:2px;
	font-size:15px;
	border: 1px solid #f1f1f1;
	border-top-width:0px;
	width:100%;
}
</style>

</head>
<body style="font-family:sans-serif;">
	<div style="margin:25px 50px;overflow: hidden;">
		<div class="headerPartClass">
			<div style="text-align:top;">
				<div style="float:left;width: 30%;">
					<div class="headerTitle">File Manager</div>
				</div>
				<div style="float:right;width: 30%;text-align:right;white-space:nowrap;font-size:14px;">
					<div>${fileUser.firstName} ${fileUser.lastName}</div>
					<div style="margin-top:10px;"><a href="/fileuploader/preferencesServlet" style="color: #fff !important;">Settings</a></div>
					<div style="margin-top:10px;white-space:nowrap;"><a href="/fileuploader/loginServlet?action=LOGOUT" style="color: #fff !important;">Log Out</a></div>
				</div>
				<div style="float:none;width: 40%;"></div>
			</div>
		</div>
		<table class="dataPartClass">
				<tr>
					<td>
						<div style="padding: 10px;padding-top: 0px;">
							<a href="/fileuploader/uploadFile.jsp" class="uploadBtnCss">Upload File</a>
							<c:if test="${fileUser.userName == 'admin'}">
								<a href="/fileuploader/adminDashboard" 
								   style="margin-left: 8px;color: #fff !important;"
								   class="uploadBtnCss">Reports</a>
							</c:if>
						</div>
					</td>
				</tr>
				<tr style="height:10px;"><td></td></tr>
				<tr>
					<td>
						<table class="dataTableClass" border="1" cellspacing="0" cellpadding="0" bordercolor="#dedde2">
							<thead>
								<tr class="tableHeaderRowClass">
									<th style="width: 80px;border-color: #7f7e80;">Serial No</th>
									<th style="width: auto;border-color: #7f7e80;">File Name</th>
									<!-- <th style="width: auto;border-color: #7f7e80;">Path</th> -->
									<th style="width: 130px;border-color: #7f7e80;">File Size (Bytes)</th>
									<th style="width: 325px;border-color: #7f7e80;">Hash</th>
									<th style="width: 120px;border:1px solid #f1f1f1;">Actions</th>
								</tr>
							</thead>
							<tbody>
								<%-- <c:forEach items="${user.fileItems}" var="file"> --%>
								 <c:if test="${empty fileItems}">
								    <tr style="height:40px; font-size:14px;">
								    	<td colspan="5" ><div style="padding-left: 15px">No files uploaded yet. Please upload a file.</div></td>	
								    </tr>
								  </c:if>
								<c:forEach items="${fileItems}" var="file" varStatus="fileItemsIndex">
									<tr style="vertical-align: top;padding:10px;color:#555555;font-size: 13px;" class="${(fileItemsIndex.index%2) == 0 ? 'tdBackgroundEven': 'tdBackgroundOdd'}">
										<td style="width: 60px;"><div style="padding:10px 0px 10px 10px;">${(fileItemsIndex.index + 1)}</div></td>
										<td style="width: auto;">
											<%-- <div><fmt:formatDate type="date" pattern="dd/MMM/yyyy" value="${file.path}"/></div> --%>
											<div style="padding:10px 0px 10px 10px;">${file.fileName}</div>
										</td>
										<%-- <td style="width: auto;">
											<div style="padding:10px 0px 10px 10px;">${file.filePath}</div>
										</td> --%>
										<td style="width: 130px;">
											<div style="padding:10px 0px 10px 10px;">${file.fileSize}</div>
										</td>
										<td style="width: 325px;word-wrap:break-word;word-break: break-all;">
											<div style="padding:10px 0px 10px 10px;">${file.fileHash}</div>
										</td>
										<td style="width: 120px;text-align: center;">
											<div style="padding-top:10px;">
												<a href="/fileuploader/uploadServlet?action=DOWNLOAD&fileId=${file.fileId}" >Download</a>
											</div>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</td>
				</tr>
		</table>
	</div>	
</body>
</html>