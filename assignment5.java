
import java.io.PrintWriter;
import java.io.IOException;
import java.util.*;
import java.lang.Math;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// adds servlet mapping annotation
import javax.servlet.annotation.WebServlet;
@WebServlet( name = "assignment5", urlPatterns = {"/assignment5"} )

public class Assignment5 extends HttpServlet{
	static enum Data {LOGICALOPERATION};
	
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
		//THINGS NEED TO BE DONE
		//COMPETION OF TRUTH TABLE
		//CHECK FOR VALID INPUT PREDICATE AND PRINT MESSAGE TO USER
		
		
		//get vars
		String logicalOperation = request.getParameter(Data.LOGICALOPERATION.name()); //"A & B"
		
		//Parse it into a structure that separates boolean variables and logical operators
		ArrayList legalOps = new ArrayList(Arrays.asList("&&", "AND", "&", "^", "||", "|", "OR", "V", "~", "NOT", "!", "==", "=", "EQUAL"));
		ArrayList arrayVars = new ArrayList(Arrays.asList(logicalOperation.split(" "))); //split by space "A & B -> [A,&,B]"
		ArrayList arrayOps = new ArrayList();
		boolean makeTable = true;
		int length = 0;
		int width = 0;
		
		//loop through predicate to seperate operations
		for(int i = 0; i< arrayVars.size(); i++){
			if(legalOps.contains(arrayVars.get(i))){
				arrayOps.add(arrayVars.get(i));
				arrayVars.set(i, null); //set to null instead of removing to avoid indexing errors in the loop
			}	
		}
		
		//remove nulls from arrayVars
		arrayVars.removeAll(Collections.singleton(null)); //now we have two array lists {A, B} and {&}
		
		
		//check if invalid
		if (arrayOps.size() == 0){
			makeTable = false; //if maketable is false print out message (invalid predicate)
		}
		
		//get table dimensions
		length = (int) Math.pow(2, arrayVars.size());
		width = arrayVars.size();
		
		
		//print the predicate they enetered
		
		//Print a complete truth table for the predicate, including a column with the result for each row
		//for loop for table size look online
		
		//Echo the predicate to the user
		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		PrintHead(writer);
 		PrintResponseBody(writer);
		writer.append("<!DOCTYPE html>")
			.append("<html>")
			.append("	<center>You typed: " + logicalOperation + "</center>")
			.append("</html>");
						
		
		//IF BOOL MAKETABLE TRUE DO THIS
		for (int i=0; i<length; i++) {
			writer.append("<center>");
			for (int j=width; j>=0; j--) {
// 				if(j == 0){
// 					writer.append("| " +  (i/(int) Math.pow(2, j))%2);
// 					continue;
// 				}
				writer.append("| " +  (i/(int) Math.pow(2, j))%2);
			}
			//System.out.println();
			writer.append("</center>");
		}

		
		
		
		PrintResponseBodyEnd(writer);
		
	
		//Parse it into a structure that separates boolean variables and logical operators
		// var array = logicalOperation.split(" ") //split by space "A & B -> [A,&,B]"
		// var opIndex = array.indexOf("&") //get index of operators
		//get & into its own array
		// opIndex > -1 ? array.splice(myIndex, 1) : false //removes & from array
		
		
		//print the predicate they enetered
		
		//Print a complete truth table for the predicate, including a column with the result for each row
		//for loop for table size look online
		
		// change these variables
// 		Float rslt = (float) 0.0;
// 		Float lhsVal;
// 		Float rhsVal;
// 		String operation = "";
// 		String lhsStr = "LHS";
// 		String rhsStr = "RHS";
// 		response.setContentType("text/html");
// 		PrintWriter out = response.getWriter();
// 		PrintHead(out);
// 		PrintBody(out, lhsStr, rhsStr, rslt.toString());
// 		PrintTail(out);
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
		out.println("    <form method=\"post\" action=\"\\assignment5\">");
		out.println("        <center>");
		out.println("            <label for=\"logicalOperation\">Enter Logical Operation:</label>");
		out.println("            <input type=\"text\" id=\"logicalOperation\" name=\"LOGICALOPERATION\"><br><br>");
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
	
	
	
	
	private void PrintResponseBody (PrintWriter out)
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
		out.println("    <form method=\"post\" action=\"\\assignment5\">");
		out.println("        <center>");
		out.println("            <label for=\"logicalOperation\">Enter Logical Operation:</label>");
		out.println("            <input type=\"text\" id=\"logicalOperation\" name=\"LOGICALOPERATION\"><br><br>");
		out.println("            <input type=\"submit\" value=\"Submit\" style=\"background-color: #80ced6\">");
		out.println("        </center>");
		out.println("    </form>");
		
	}
	
	
	private void PrintResponseBodyEnd (PrintWriter out){
		
		out.println("</body>");
		out.println("");
		
	}
	
	
	
	

}
