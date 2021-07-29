package com.Controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.Helper.EmailWeb;

/**
 * Servlet implementation class EmailController
 */
@WebServlet("/sendMail")
@MultipartConfig
public class EmailController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EmailController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings("unused")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String to = request.getParameter("to");
		String subject = request.getParameter("subject");
		String message = request.getParameter("message");
		String from = "YourMailId";

		Part part = request.getPart("attachFile");
		String image = part.getSubmittedFileName();

		String path = getServletContext().getRealPath("/" + "images/Attach" + File.separator + image);
		
		InputStream is=part.getInputStream();
		
		boolean upload=uploadFile(is, path);

		EmailWeb emailWeb = new EmailWeb();
		emailWeb.sendMail(subject, message, to, from, path);

		HttpSession session = request.getSession();
		if(upload==true) {
			if (emailWeb != null) {
				session.setAttribute("successMessage", "Mail successfully sent....");
				response.sendRedirect("success.html");
			} else {
				session.setAttribute("errorMessage", "Something went wrong....");
				response.sendRedirect("index.html");
			}
		}else {
			session.setAttribute("errorMessage", "Something went wrong....");
			response.sendRedirect("index.html");
		}
	}

	public boolean uploadFile(InputStream is, String path) {

		boolean test = false;
		try {
			byte[] b = new byte[is.available()];
			is.read();
			FileOutputStream fops = new FileOutputStream(path);
			fops.write(b);
			fops.flush();
			fops.close();

			test = true;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return test;

	}

}
