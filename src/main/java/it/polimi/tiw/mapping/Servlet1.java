package it.polimi.tiw.mapping;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet implementation class Servlet1
 */
@WebServlet("/test")
public class Servlet1 extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Servlet1()
	{
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
	{

		response.setContentType("text/html");

		PrintWriter out = response.getWriter();

		out.println("<HTML><BODY>");
		out.println("<HEAD><TITLE>Servlet 1 response </TITLE></HEAD>");

		out.println("<P>" + "THIS IS SERVLET1" + "</P>");
		
		out.println("<P> Request URI:   " + request.getRequestURI() + "</P>");

		out.println("<P> Context path: " + request.getContextPath() + "</P>");

		out.println("<P> Servlet path: " + request.getServletPath() + "</P>");

		out.println("<P> Path info: " + request.getPathInfo() + "</P>");

		out.println("<P> Translated path: " + request.getPathTranslated() + "</P>");

		out.println("</HTML></BODY>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		doGet(request, response);
	}

}
