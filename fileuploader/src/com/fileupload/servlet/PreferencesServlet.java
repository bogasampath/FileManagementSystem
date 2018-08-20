package com.fileupload.servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fileupload.db.DBUtil;
import com.fileupload.db.User;
import com.fileupload.db.UserPreferences;

public class PreferencesServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = Logger.getLogger(PreferencesServlet.class.getName());
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException {
		String errorMessage = "";
		request.getSession().setAttribute("errorMessage", errorMessage);
		String action = request.getParameter("action");
		User user = (User) request.getSession().getAttribute("fileUser");
		if(user == null) {
			LOGGER.severe("User session not found, redirecting to login page");
			response.sendRedirect("/fileuploader/login.jsp");
			return;
		}
		UserPreferences userPrefs = null;
		try {
			userPrefs = DBUtil.getUserPreferences(user.getUserId());
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error while fetching preferences for userId: "+
					user.getUserId(), e);
			throw new IOException(e);
		}
		if(userPrefs == null) {
			userPrefs = new UserPreferences();
		}
		request.getSession().setAttribute("userPreferences", userPrefs);
		if(action != null && "EDIT_PREF".equals(action)) {
			response.sendRedirect("/fileuploader/editPreferences.jsp");
			return;
		}
		response.sendRedirect("/fileuploader/showPreferences.jsp");
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws IOException {
		String errorMessage = "";
		User user = (User) request.getSession().getAttribute("fileUser");
		if(user == null) {
			LOGGER.severe("User session not found, redirecting to login page");
			response.sendRedirect("/fileuploader/login.jsp");
			return;
		}
		UserPreferences userPrefs = null;
		try {
			userPrefs = DBUtil.getUserPreferences(user.getUserId());
			if(userPrefs == null) {
				userPrefs = new UserPreferences();
				userPrefs.setUserId(user.getUserId());
			}
			String filePath = request.getParameter("filePath");
			if(filePath == null || filePath.trim().isEmpty()) {
				errorMessage = "Invalid File Path";
				request.getSession().setAttribute("errorMessage", errorMessage);
				response.sendRedirect("/fileuploader/editPreferences.jsp");
				return;
			}
			String chunkSizeString = request.getParameter("chunkSize");
			if(chunkSizeString == null || chunkSizeString.trim().isEmpty()) {
				errorMessage = "Invalid Chunk Size";
				request.getSession().setAttribute("errorMessage", errorMessage);
				response.sendRedirect("/fileuploader/editPreferences.jsp");
				return;
			}
			String dedupeType = request.getParameter("dedupeType");
			if(dedupeType == null || dedupeType.trim().isEmpty()) {
				errorMessage = "Invalid Dedupe Type";
				request.getSession().setAttribute("errorMessage", errorMessage);
				response.sendRedirect("/fileuploader/editPreferences.jsp");
				return;
			}
			userPrefs.setChunkSize(Integer.valueOf(chunkSizeString));
			userPrefs.setFileUploadPath(filePath);
			userPrefs.setDedupeType(dedupeType);
			
			DBUtil.updateUserPreferences(userPrefs);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error while saving preferences for userId:"+
					user.getUserId(), e);
			throw new IOException(e);
		}
		response.sendRedirect("/fileuploader/preferencesServlet");
	}
	
}
