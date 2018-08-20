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

public class AdminDashboardServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = 
			Logger.getLogger(AdminDashboardServlet.class.getName());

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {
		User user = null;
		try {
			user = (User)request.getSession().getAttribute("fileUser");
			if(user == null) {
				LOGGER.severe("User session expired, redirecting to login page");
				response.sendRedirect("/fileuploader/login.jsp");
				return;
			}
			request.getSession().setAttribute("appStats",
					DBUtil.getApplicationStatistics(user.getUserId()));
			request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error while fetching application stats for userId: "+
					user.getUserId(), e);
		}
	}

}
