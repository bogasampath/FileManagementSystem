package com.fileupload.servlet;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.fileupload.db.FileObject;
import com.fileupload.db.User;
import com.fileupload.util.FileManager;
import com.fileupload.util.FileUtils;

public class FileUploadServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public static final int BUF_SIZE = 2 * 1024;
    
	private static final Logger LOGGER = Logger.getLogger(FileUploadServlet.class.getName());

    public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException {
		User user = getUserFromSession(request);
		if(user == null) {
			LOGGER.severe("User session expired, redirecting to login page");
			response.sendRedirect("/fileuploader/login.jsp");
			return;
		}
		request.getSession().setAttribute("fileItems",
				FileManager.getFilesForUser(user.getUserId()));
		
		String action = request.getParameter("action");
		if(action != null && "DOWNLOAD".equals(action)) {
			String fileIdString = request.getParameter("fileId");
			Integer fileId = Integer.valueOf(fileIdString);
			try {
				FileObject fileObject = FileManager.findFileById(fileId);
				this.downloadFile(fileObject, request, response);
				return;
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "Error while downloading file for fileId:"+
						fileId, e);
			}
		}
		String path = "/fileuploader/dashboard.jsp";
		response.sendRedirect(path);
	}

    private void downloadFile(FileObject fileObject, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/plain");
		response.setHeader("Content-Disposition","attachment;filename="+fileObject.getFileName());
		
		byte[] buffer = new byte[1024];
        int len = 0;
        OutputStream outputStream = response.getOutputStream();
		InputStream inputStream = null;
		try {
			List<String> chunksPathList = FileManager.fetchChunksPathList(fileObject);
			for(String filePath : chunksPathList) {
				inputStream = new FileInputStream(filePath);
		        while ((len = inputStream.read(buffer)) > 0) {
		            outputStream.write(buffer, 0, len);
		        }
		        try{
		        	inputStream.close();
		        } catch (Exception e) {
		        	LOGGER.log(Level.WARNING, "Error while closing input stream", e);
		        }
			}
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Error while downloading file: "+
					fileObject.getFileName(), e);
			throw new IOException("Error while downloading file: "+
					fileObject.getFileName(), e);
		} finally {
			if(outputStream != null) {
				try {
					outputStream.flush();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String name = null;
		try {
			User user = getUserFromSession(request);
			if(user == null) {
				LOGGER.severe("User session expired, redirecting to login page");
				response.sendRedirect("/fileuploader/login.jsp");
				return;
			}

			ServletFileUpload upload = new ServletFileUpload();
			FileItemIterator iter = upload.getItemIterator(request);
			FileItemStream imageItem = iter.next();
			name = imageItem.getName();
			LOGGER.log(Level.INFO, "Received File Name: " + name);
			
			if(name == null || name.trim().isEmpty()) {
				request.setAttribute("errorMessage", "Invalid file, please select file");
				request.getRequestDispatcher("uploadFile.jsp")
					.forward(request, response);
				return;
			}
			
			InputStream inputStream = imageItem.openStream();

			OutputStream out = null;
			String uniquePath = FileUtils.getUniqueUploadFilePath(user.getUserId());
			File directory = new File(uniquePath);
	        if(!directory.exists()) {
	            directory.mkdirs();
	            LOGGER.log(Level.INFO, "Created directory path:"+uniquePath);
	        }
	        File dst = new File( uniquePath + name);
	        try {
                out = new BufferedOutputStream(new FileOutputStream(dst), BUF_SIZE);
                byte[] buffer = new byte[BUF_SIZE];
                int len = 0;
                long receivedFileSize = 0;
                while ((len = inputStream.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                    receivedFileSize = receivedFileSize + len;
                }
                if(receivedFileSize <= 0) {
                	request.setAttribute("errorMessage", "Invalid file size, please select a valid file");
    				request.getRequestDispatcher("uploadFile.jsp")
    					.forward(request, response);
    				return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != inputStream) {
                    try {
                    	inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (null != out) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
	        
	        FileManager.saveFile(uniquePath, name, user.getUserId());

	        String dashboardPath = "/fileuploader/uploadServlet";
			response.sendRedirect(dashboardPath);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private User getUserFromSession(HttpServletRequest request) {
		return (User)request.getSession().getAttribute("fileUser");
	}
}
