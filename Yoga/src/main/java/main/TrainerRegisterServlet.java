package main;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import main.common.DBConnection;

/**
 * Servlet implementation class TrainerRegisterServlet
 */
@MultipartConfig(maxFileSize = 16177215) // upload file's size up to 16MB
public class TrainerRegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		String name = request.getParameter("formfirstname");
		String email = request.getParameter("formemail");

		String abuotme = request.getParameter("formaboutself");
		String experience = request.getParameter("formexperience");
		String qualification = request.getParameter("formqualification");

		String expertise = request.getParameter("formexpertise1");
		/*
		 * String monthlyfees=request.getParameter("formmonthlyfees1"); String
		 * classlevel=request.getParameter("formclasslevel1"); String
		 * from1=request.getParameter("formfrom1"); String
		 * to1=request.getParameter("formto1");
		 * 
		 * String expertise2=request.getParameter("formexpertise2"); String
		 * monthlyfees2=request.getParameter("formmonthlyfees2"); String
		 * classlevel2=request.getParameter("formclasslevel2"); String
		 * from2=request.getParameter("form-from2"); String
		 * to2=request.getParameter("form-to2");
		 * 
		 * String expertise3=request.getParameter("formexpertise3"); String
		 * monthlyfees3=request.getParameter("formmonthlyfees3"); String
		 * classlevel3=request.getParameter("formclasslevel3"); String
		 * from3=request.getParameter("form-from3"); String
		 * to3=request.getParameter("form-to3");
		 * 
		 * 
		 * // String schedule9to11=request.getParameter("flexCheckDefaultschms9to11");
		 * // String schedule11to1=request.getParameter("flexCheckDefaultschms11to1");
		 * // String schedule5to7=request.getParameter("flexCheckDefaultschms5to7"); //
		 * String schedule7to9=request.getParameter("flexCheckDefaultschms7to9");
		 * 
		 * 
		 * String schedule,schedule2,schedule3; schedule=from1+" to "+to1;
		 * if(from2.length()>0 && to2.length()>0) { schedule2=from2+" to "+to2; }else{
		 * schedule2=" "; classlevel2=" "; monthlyfees2=" "; expertise2=" "; }
		 * if(from3.length()>0 && to3.length()>0) {
		 * 
		 * schedule3=from3+" to "+to3; }else{ schedule3=" "; classlevel3=" ";
		 * monthlyfees3=" "; expertise3=" "; System.out.println("sc3:"+schedule3); }
		 * 
		 */
		Date date = new Date();
		Timestamp ts = new Timestamp(date.getTime());
		System.out.println("timestamp::" + ts);

		InputStream inputStream = null; // input stream of the upload file
		InputStream certificate1inputStream = null;
		InputStream certificate2inputStream = null;
		InputStream certificate3inputStream = null;

		// obtains the upload file part in this multipart request
		Part filePart = request.getPart("formimage");
		// String fileName1 = filePart.getSubmittedFileName();
		/*
		 * System.out.println("path:"+request.getContextPath()+"/target/Yoga/img/" +
		 * fileName1); for (Part part : request.getParts()) {
		 * part.write("/target/Yoga/img/" + fileName1);
		 * System.out.println("path:"+request.getContextPath()); }
		 */

		String fileName = extractFileName(filePart);
		if (filePart != null) {
			// prints out some information for debugging
			System.out.println(filePart.getName());
			System.out.println(filePart.getSize());
			System.out.println(filePart.getContentType());

			// obtains input stream of the upload file
			inputStream = filePart.getInputStream();
		}
		Part certificate1 = request.getPart("formcertificate1");
		String certificatefileName1 = extractFileName(certificate1);
		if (certificate1 != null) {
			// prints out some information for debugging
			System.out.println("certificate1::::" + certificate1.getName());
			System.out.println(certificate1.getSize());
			System.out.println(certificate1.getContentType());

			// obtains input stream of the upload file
			certificate1inputStream = certificate1.getInputStream();
		} else {
			certificate1inputStream = null;
			certificatefileName1 = "";
		}
		Part certificate2 = request.getPart("form-certificate2");
		String certificatefileName2 = extractFileName(certificate2);
		if (certificate2 != null) {
			// prints out some information for debugging
			System.out.println("certificate2::::" + certificate2.getName());
			System.out.println(certificate2.getSize());
			System.out.println(certificate2.getContentType());

			// obtains input stream of the upload file
			certificate2inputStream = certificate2.getInputStream();
		} else {
			certificate2inputStream = null;
			certificatefileName2 = "";
		}
		Part certificate3 = request.getPart("form-certificate3");
		String certificatefileName3 = extractFileName(certificate3);
		if (certificate3 != null) {
			// prints out some information for debugging
			System.out.println("certificate3::::" + certificate3.getName());
			System.out.println(certificate3.getSize());
			System.out.println(certificate3.getContentType());

			// obtains input stream of the upload file
			certificate3inputStream = certificate3.getInputStream();
		} else {
			certificate3inputStream = null;
			certificatefileName3 = "";
		}

		Connection con = null;
		String Countrow = null;
		try {
			con = DBConnection.createConnection();
			String cnt = "select count(*) from trainerregister where traineremail=?";
			PreparedStatement statement1 = con.prepareStatement(cnt);
			statement1.setString(1, email);
			ResultSet rs = statement1.executeQuery();
			rs.next();
			Countrow = rs.getString(1);
			if (Countrow.equals("0")) {

				// String query = "insert into
				// trainertemptable(trainername,traineremail,experience,qualification,expertise,classlevel,monthlyfees,schedules,expertise2,classlevel2,monthlyfees2,schedules2,expertise3,classlevel3,monthlyfees3,schedules3,aboutyourself,photoname,photo,creationtime,certificate1,certificate1filename,certificate2,certificate2filename,certificate3,certificate3filename)
				// values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; //Insert user
				// details into the table 'USERS'
				String query = "insert into trainertemptable(trainername,traineremail,experience,qualification,expertise,aboutyourself,photoname,photo,creationtime,certificate1,certificate1filename,certificate2,certificate2filename,certificate3,certificate3filename) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; // Insert
																																																																													// 'USERS'
				PreparedStatement statement = con.prepareStatement(query); // Making use of prepared statements here to
																			// insert bunch of data
				statement.setString(1, name);
				statement.setString(2, email);
				statement.setString(3, experience);
				statement.setString(4, qualification);
				statement.setString(5, expertise);
				statement.setString(6, abuotme);
				statement.setString(7, fileName);
				statement.setBlob(8, inputStream);
				statement.setTimestamp(9, ts);
				statement.setBlob(10, certificate1inputStream);
				statement.setString(11, certificatefileName1);
				statement.setBlob(12, certificate2inputStream);
				statement.setString(13, certificatefileName2);
				statement.setBlob(14, certificate3inputStream);
				statement.setString(15, certificatefileName3);

				int i = statement.executeUpdate();

				if (i != 0) {
					HttpSession session = request.getSession(true);
					session.setAttribute("traineremail", email);
					session.setAttribute("trainername", name);
					session.setAttribute("trainerexperience", experience);
					session.setAttribute("trainerqualification", qualification);
					session.setAttribute("trainerexpertise", expertise);
					/*
					 * session.setAttribute("trainerschedule", schedule);
					 * session.setAttribute("trainerclasslevel", classlevel);
					 * session.setAttribute("trainermonthlyfees", monthlyfees);
					 * session.setAttribute("trainerexpertise2", expertise2);
					 * session.setAttribute("trainerschedule2", schedule2);
					 * session.setAttribute("trainerclasslevel2", classlevel2);
					 * session.setAttribute("trainermonthlyfees2", monthlyfees2);
					 * session.setAttribute("trainerexpertise3", expertise3);
					 * session.setAttribute("trainerschedule3", schedule3);
					 * session.setAttribute("trainerclasslevel3", classlevel3);
					 * session.setAttribute("trainermonthlyfees3", monthlyfees3);
					 */ session.setAttribute("traineraboutme", abuotme);
					session.setAttribute("trainercertificatefileName1", certificatefileName1);
					session.setAttribute("trainercertificatefileName2", certificatefileName2);
					session.setAttribute("trainercertificatefileName3", certificatefileName3);

					// out.write("Request Sent Successfully");
					System.out.println("success");
					// request.setAttribute("message", "Request Sent Successfully");
					request.getRequestDispatcher("TrainerTempProfile.jsp").forward(request, response);

					// forwards to the message page
					// getServletContext().getRequestDispatcher("/trainersprofile.jsp").forward(request,
					// response);

				} else {
					out.print("Request Failed Plz Try Again ..!");
					System.out.println("fail");
					request.setAttribute("message", "Request Sent Successfully");
					request.getRequestDispatcher("/Trainer.jsp").include(request, response);
				}
			} else {
				System.out.println("Email ID is Already Registered");
				out.write("<font color='red'><b>Email ID is Already Registered</b></font>");
				request.setAttribute("message", "Request Sent Successfully");
				request.getRequestDispatcher("/Trainer.jsp").include(request, response);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e);
		}

	}

	private String extractFileName(Part part) {
		// form-data; name="file"; filename="C:\file1.zip"
		// form-data; name="file"; filename="C:\Note\file2.zip"
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");
		for (String s : items) {
			if (s.trim().startsWith("filename")) {
				// C:\file1.zip
				// C:\Note\file2.zip
				String clientFileName = s.substring(s.indexOf("=") + 2, s.length() - 1);
				clientFileName = clientFileName.replace("\\", "/");
				int i = clientFileName.lastIndexOf('/');
				// file1.zip
				// file2.zip
				return clientFileName.substring(i + 1);
			}
		}
		return null;
	}
}
