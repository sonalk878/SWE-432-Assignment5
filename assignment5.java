
import java.io.PrintWriter;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// adds servlet mapping annotation
import javax.servlet.annotation.WebServlet;
@WebServlet( name = "assignment5", urlPatterns = {"/assignment5"} )

public class assignment5 extends HttpServlet{
	// Location of servlet.
	static String Domain  = "";
	static String Path    = "";
	static String Servlet = "assignment5";
	
	// Button labels
	static String OperationSubmit = "Submit";
	
	static String Style ="https://www.cs.gmu.edu/~gterziys/public_html/style.css";
	
	/** *****************************************************
	 *  Overrides HttpServlet's doPost().
	 *  Converts the values in the form, performs the operation
	 *  indicated by the submit button, and sends the results
	 *  back to the client.
	********************************************************* */
	@Override
	public void doPost (HttpServletRequest request, HttpServletResponse response)
	   throws ServletException, IOException
	{
		// change these variables
		Float rslt = (float) 0.0;
		Float lhsVal;
		Float rhsVal;
		String operation = "";
		String lhsStr = "LHS";
		String rhsStr = "RHS";
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		PrintHead(out);
		PrintBody(out, lhsStr, rhsStr, rslt.toString());
		PrintTail(out);
	}
	
	/** *****************************************************
	 *  Overrides HttpServlet's doGet().
	 *  Prints an HTML page with a blank form.
	********************************************************* */
	@Override
	public void doGet (HttpServletRequest request, HttpServletResponse response)
	       throws ServletException, IOException
	{
	   response.setContentType("text/html");
	   PrintWriter out = response.getWriter();
	   PrintHead(out);
	   PrintBody(out);
	   PrintTail(out);
	} // End doGet
	
	/** *****************************************************
	 *  Prints the <head> of the HTML page, no <body>.
	********************************************************* */
	private void PrintHead (PrintWriter out)
	{
	   out.println("<html>");
	   out.println("");
	   out.println("<head>");
	   out.println("<meta charset=\"utf-8\" />");
	   out.println("<title>Logical Predicates</title>");
	   out.println(" <link rel=\"stylesheet\" type=\"text/css\" href=\"" + Style + "\">");
	   out.println("</head>");
	   out.println("");
	} // End PrintHead 

	
	/** *****************************************************
	 *  Prints the <BODY> of the HTML page with the form data
	 *  values from the parameters.
	********************************************************* */
	private void PrintBody (PrintWriter out, String lhs, String rhs, String rslt)
	{
		out.println("<body>");
		out.println("    <h1><center>Predicate Logic Calculator</center></h1>");
		out.println("    <h2><center>Sonal Kumar * Angela Gentile * George Terziysky * SWE-432-001</center>  </h2>");
		out.println("	 <h3><center>Formatting/Syntax Instructions:</center></h3>");
		out.println("    <h4><center>In order to calculate the final value of the logical operation, type in the predicate with the following constraints:</center></h4>");
		out.println("");
		out.println("    <ul>");
		out.println("        <li>The entry should be typed in the format of (TRUE/FALSE)(LOGICAL OPERATOR)(TRUE/FALSE)</li>");
		out.println("        <li>");
		out.println("            The entry can also be an extention of the format described above. There can be multiple True/False statements with logical operations in between them. For example, you can enter");
		out.println("            \"TRUE OR FALSE\",which would give you TRUE, and you could also enter \"FALSE AND(FALSE AND TRUE)\", which would result in False.");
		out.println("        </li>");
		out.println("    </ul>");
		out.println("    <h4><center>Options for supported logical symbols:</center></h4>");
		out.println("    <ul>");
		out.println("        <li>Supported symbols for AND: \"&&\", \"AND\", \"&\", \"^\"</li>");
		out.println("        <li>Supported symbols for OR: \"||\", \"|\", OR, \"V\" </li>");
		out.println("        <li>Supported symbols for NOT: \"~\", \"NOT\", \"!\"</li>");
		out.println("        <li>Supported symbols for EQUAL: \"==\", \"=\", \"EQUAL\"</li>");
		out.println("    </ul>");
		out.println("    <br />    <br />    <br />");
		out.println("");
		out.println("    <form method=\"post\" action=\"https://cs.gmu.edu:8443/offutt/servlet/formHandler\">");
		out.println("        <center>");
		out.println("            <label for=\"logicalOperation\">Enter Logical Operation:</label>");
		out.println("            <input type=\"text\" id=\"logicalOperation\" name=\"logicalOperation\"><br><br>");
		out.println("            <input type=\"submit\" value=\"Submit\" style=\"background-color: #80ced6\">");
		out.println("        </center>");
		out.println("    </form>");
		out.println("</body>");
		out.println("");
		out.println("");
		
	}
	
	/** *****************************************************
	 *  Overloads PrintBody (out,lhs,rhs,rslt) to print a page
	 *  with blanks in the form fields.
	********************************************************* */
	private void PrintBody (PrintWriter out)
	{
	   PrintBody(out, "", "", "");
	}
	
	/** *****************************************************
	 *  Prints the bottom of the HTML page.
	********************************************************* */
	private void PrintTail (PrintWriter out)
	{
	   out.println("");
	   out.println("</html>");
	} // End PrintTail

}
