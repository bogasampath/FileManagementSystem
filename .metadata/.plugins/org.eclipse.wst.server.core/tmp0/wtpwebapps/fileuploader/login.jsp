<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login - File Manager</title>
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
.labelStyle {
	color: #00629D;
	text-align: right;
	padding-right: 15px;	
	font-size: 14px;
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
				<div style="float:right;width: 30%;text-align:right;font-size:14px;">
					<div></div>
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
									<form action="/fileuploader/loginServlet" method="post">
										<div><div style="display: inline-block;width:70px;font-weight: normal;" class="labelStyle">Login Id </div><input type="text" name="uname"></div>
										<div style="min-height: 10px;"></div>
										<div><div style="display: inline-block;width:70px;font-weight: normal;" class="labelStyle">Password</div><input type="password" name="pwd"></div>
										<div style="min-height: 15px;"></div>
										<input type="hidden" name="action" value="LOGIN">
										<div style="margin-left: 85px;">
											<input type="submit" value="Log In" class="btnCss" />
										</div>
									</form>
									<div style="color: #00629D;margin-top: 15px;margin-left: 15px;font-size:13px;">You don't have an account yet? <a href="/fileuploader/loginServlet?action=REGISTER">Register here</a></div>				
								</td>
							</tr>
						</table>
					</td>
				</tr>
		</table>
	</div>	
</body>
</html>		
