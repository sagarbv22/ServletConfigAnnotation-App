package in.ineuron.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.cj.jdbc.Driver;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet(urlPatterns = { "/test" },
		initParams = { @WebInitParam(name = "url", value = "jdbc:mysql:///school"),
		@WebInitParam(name = "username", value = "root"),
		@WebInitParam(name = "password", value = "Sagar@123") }, loadOnStartup = 10)
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private PreparedStatement pstmt = null;
	private PrintWriter out = null;

	static {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver loaded succesfully...");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void init(){
		String url = getInitParameter("url");
		String username = getInitParameter("username");
		String password = getInitParameter("password");
		System.out.println(url+" "+ username+" "+ password);
		try {
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String insertQuery = "insert into student(sname, sage, saddress) values(?,?,?)";
		try {
			if (connection != null)
				pstmt = connection.prepareStatement(insertQuery);

			if (pstmt != null) {
				pstmt.setString(1, request.getParameter("name"));
				pstmt.setInt(2, Integer.parseInt(request.getParameter("age")));
				pstmt.setString(3, request.getParameter("addr"));
			}
			int row = pstmt.executeUpdate();

			if (row == 1) {
				out = response.getWriter();
				out.println("<h1 style='color:green;'>Registration Sucessfull..</h1>");
				out.flush();
			} else {
				out.println("<h1 style='color:red;'>Registration failed click the below link to try again..</h1>");
				out.println("<a href='./reg.html'>Register</a>");
				out.flush();
			}
			out.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void destroy() {

		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
