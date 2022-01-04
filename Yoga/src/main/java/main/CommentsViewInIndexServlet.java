package main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.common.DBConnection;
import main.common.Json;
import main.model.SeriesSchedulesVO;
import main.model.TrainerDetailsVO;

/**
 * Servlet implementation class CommentsViewInIndexServlet
 */
@WebServlet(name = "/CommentsViewInIndexServlet", urlPatterns = "/IndexCommentsView")
public class CommentsViewInIndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");

		List<TrainerDetailsVO> commentsList = new ArrayList<TrainerDetailsVO>();
		Map<String, SeriesSchedulesVO> commentSeriesbyviewers = new LinkedHashMap<>();

		try (Connection connection = DBConnection.createConnection()) {
			String sql = "select * from fitnesscomments order by commenttime desc;";
			try (PreparedStatement statement = connection.prepareStatement(sql)) {				
				try (ResultSet rs = statement.executeQuery()) {
					while (rs.next()) {
						// Defining Student Object
						TrainerDetailsVO details = new TrainerDetailsVO();
						SeriesSchedulesVO seriesVo = new SeriesSchedulesVO();
						details.setName(rs.getString("commentorname"));
						details.setEmail(rs.getString("commentoremail"));
						details.setSubject(rs.getString("subject"));
						details.setComment(rs.getString("comment"));
						details.setCreatetime(rs.getTimestamp("commenttime"));
						
						Timestamp tsstart=rs.getTimestamp("commenttime");
						Date date = new Date();
						long mlend=date.getTime();
						long mlstart=tsstart.getTime();
				        long deff=mlend-mlstart;
				        int defdays=(int)(deff/(1000*60*60*24));
				        int defhrs=(int)(deff/(1000*60*60));
				        int defmin=(int)(deff/(1000*60));
				        
						System.out.println("deff::" + deff);
						System.out.println("days::" + defdays);
				        
				        if(defdays<1){
				        	if(defhrs<1){
				        		if(defmin<1){
				        			String days="Justnow";
									details.setDefdays(days);
				        		}else{
				        		String days=defmin+"mins";
								details.setDefdays(days);
				        		}
				        	}else{
				        		String days=defhrs+"hrs";
								details.setDefdays(days);
				        	}
				        }else{
				        	String days=defdays+"days";
							details.setDefdays(days);
				        }
						
						// Adding the Student Object to List
	//			commentsList.add(details);
				       seriesVo.setTrainer(details);
						// Just create dummy id so that we can add to map, this id has no significant.
				       commentSeriesbyviewers.put(UUID.randomUUID().toString(), seriesVo);
					}
//					request.setAttribute("CommentListData", commentsList);		
//					request.getRequestDispatcher("/index.jsp").forward(request, response);
					
					response.setContentType("application/json");
					response.getWriter().write(Json.stringify(commentSeriesbyviewers.values()));
					
				}
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
	}

	private String asBlobEncoded(Blob blob) throws SQLException, IOException {
		try (InputStream inputStream = blob.getBinaryStream()) {
			try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
				byte[] buffer = new byte[4096];
				int bytesRead = -1;

				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}

				byte[] imageBytes = outputStream.toByteArray();
				return Base64.getEncoder().encodeToString(imageBytes);
			}
		}
	}
}

