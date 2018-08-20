<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Preferences - File Manager</title>
<style type="text/css">
.tdBackgroundEven {
	background-color: #ebebeb;
}
.tdBackgroundOdd {
	background-color: #ffffff;
}
.btnCss {
	text-decoration: none !important;
	border: 1px solid #d9534f;
	padding: 10px;
	color: #fff !important;
	border-radius: 5px;
	background-color: #d9534f;
	font-size: 13px;
	font-weight: normal;
	height: 38px !important;
	min-height: 38px !important;
	max-height: 38px !important;
	font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
}

.headerTitle {
	font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
	font-weight: normal;
	color: #fff;
	font-size: 21px;
}
.headerPartClass {
	padding:25px;
	background-color: #2db3ac;
	font-size:21px;
	color:#FFF;
	height:75px;
}
.dataPartClass {
	padding:25px 20px;
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
				<div style="float:left;width: 30%;"><div class="headerTitle">File Manager</div></div>
				<div style="float:right;width: 30%;text-align:right;font-size:14px;white-space:nowrap;">
					<div>${fileUser.firstName} ${fileUser.lastName}</div>
					<div style="margin-top:10px;"><a href="/fileuploader/preferencesServlet" style="color: #fff !important;">Settings</a></div>
					<div style="margin-top:10px;white-space:nowrap;"><a href="/fileuploader/loginServlet?action=LOGOUT" style="color: #fff !important;">Log Out</a></div>
				</div>
				<div style="float:none;width: 40%;"></div>
			</div>
		</div>
		<table class="dataPartClass">
				<tr>
					<td><div style="color: red;">${errorMessage}</div></td>
				</tr>
				<tr style="height: 15px;">
					<td></td>
				</tr>
				<tr>
					<td>
						<table>
							<tr>
								<td>
									<form action="/fileuploader/preferencesServlet" method="post">
										<div style="font-size:14px;"><div style="display: inline-block;width:140px;">Upload File Path </div><input type="text" name="filePath" value="${userPreferences.fileUploadPath}" /></div>
										<div style="min-height: 10px;"></div>
										<div style="font-size:14px;">
											<div style="display: inline-block;width:140px;">Dedupe Type </div>
											<input type="radio" name="dedupeType" value="fixed_chunk" ${(userPreferences.dedupeType=='fixed_chunk') ? 'checked' : ''}> Fixed Chunk</input>
											<input type="radio" name="dedupeType" value="variable_chunk" ${(userPreferences.dedupeType=='variable_chunk') ? 'checked' : ''}> Variable Chunk</input>
										</div>
										<div style="min-height: 10px;"></div>
										<div style="font-size:14px;"><div style="display: inline-block;width:140px;">Chunk Size (Bytes)</div><input type="number" name="chunkSize" value="${userPreferences.chunkSize}" /></div>
										<div style="min-height: 5px;"></div>
										<div style="margin-top: 10px;">
											<input type="submit" value="Submit" class="btnCss"/>
											<!-- <div style="display:inline; width:10px; min-width: 10px;"></div> -->
											<a href="/fileuploader/preferencesServlet" class="btnCss" style="padding-bottom:12px;margin-left: 8px;">Cancel</a>
										</div>
									</form>					
								</td>
							</tr>
						</table>
					</td>
				</tr>
		</table>
	</div>	
</body>
</html>		
