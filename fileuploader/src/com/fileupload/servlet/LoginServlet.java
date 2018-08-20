package com.fileupload.servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fileupload.db.DBUtil;
import com.fileupload.db.User;

public class LoginServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {
		String errorMessage = "";
		String action = request.getParameter("action");
		if(action != null) {
			if("LOGOUT".equals(action)) {
				request.getSession().setAttribute("fileUser", null);
				response.sendRedirect("/fileuploader/login.jsp");
				return;
			} else if("REGISTER".equals(action)) {
				request.getSession().setAttribute("fileUser", null);
				request.getRequestDispatcher("userRegistration.jsp").forward(request, response);
				return;
			}
		} else {
			User user = (User) request.getSession().getAttribute("fileUser");
			if(user == null) {
				LOGGER.severe("User session not found, redirecting to login page");
				response.sendRedirect("/fileuploader/login.jsp");
				return;
			}
		}
		String dashboardPath = "/fileuploader/uploadServlet";
		response.sendRedirect(dashboardPath);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws IOException {
		String errorMessage = "";
		String action = request.getParameter("action");
		User user = null;
		String task = "authentication";
		if(action != null) {
			try {
				String userName = request.getParameter("uname");
				String password = request.getParameter("pwd");
				if("CREATE_USER".equals(action)) {
					task = "creation";
					String firstName = request.getParameter("firstName");
					String lastName = request.getParameter("lastName");
					String confirmPassword = request.getParameter("confirmPwd");
					if(password == null || confirmPassword == null ||
							!password.equals(confirmPassword)) {
						request.setAttribute("uname", userName);
						request.setAttribute("pwd", password);
						request.setAttribute("confirmPwd", confirmPassword);
						request.setAttribute("firstName", firstName);
						request.setAttribute("lastName", lastName);
						errorMessage = "Input password not mathced";
						request.setAttribute("errorMessage", errorMessage);
						request.getRequestDispatcher("userRegistration.jsp")
							.forward(request, response);
						return;
					}
					
					user = DBUtil.findUserByUserName(userName);
					if(user != null) {
						request.setAttribute("uname", userName);
						request.setAttribute("pwd", password);
						request.setAttribute("confirmPwd", confirmPassword);
						request.setAttribute("firstName", firstName);
						request.setAttribute("lastName", lastName);
						errorMessage = "User already exists for the given user name";
						request.setAttribute("errorMessage", errorMessage);
						
						request.getRequestDispatcher("userRegistration.jsp")
							.forward(request, response);
						return;
					}
					user = new User();
					user.setFirstName(firstName);
					user.setLastName(lastName);
					user.setUserName(userName);
					user.setPassword(password);
					user = DBUtil.createUser(user);
				} else {
					task = "authentication";
					user = DBUtil.authenticateUser(userName, password);
					if(user == null) {
						errorMessage = "Invalid credentials";
						request.setAttribute("errorMessage", errorMessage);
						request.getRequestDispatcher("login.jsp")
							.forward(request, response);
						return;
					}
				}
				request.getSession().setAttribute("fileUser", user);
			} catch(Exception e) {
				errorMessage = "Error while user "+task + ", error: "+e.getMessage();
				LOGGER.log(Level.SEVERE, errorMessage, e);
				request.getSession().setAttribute("errorMessage", errorMessage);
				response.sendRedirect("/fileuploader/login.jsp");
				return;
			}
		}
		response.sendRedirect("/fileuploader/uploadServlet");
	}
}
