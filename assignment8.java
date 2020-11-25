// persisting data

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.RequestDispatcher;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;


// adds servlet mapping annotation
import javax.servlet.annotation.WebServlet;
@WebServlet( name = "assignment8", urlPatterns = {"/assignment8"} )
public class assignment8 extends HttpServlet{
	static String Style ="https://www.cs.gmu.edu/~gterziys/public_html/style.css";
	static enum Data {LOGICALOPERATION, ENTRY, ENTRIES};
	
	static String RESOURCE_FILE = "predicates.xml";
	
	// Location of servlet.
	static String Domain  = "";
	static String Path    = "/";
	static String Servlet = "assignment8";
	
	// Button labels
	static String OperationSubmit = "Submit";
	static String OperationXMLfile = "XMLfile";
	
	public class Entry {
	    String predicate;
	  }
	
	List<Entry> entries;

  public class EntryManager {
    private String filePath = null;
    private XMLEventFactory eventFactory = null;
    private XMLEvent LINE_END = null;
    private XMLEvent LINE_TAB = null;
    private XMLEvent ENTRIES_START = null;
    private XMLEvent ENTRIES_END = null;
    private XMLEvent ENTRY_START = null;
    private XMLEvent ENTRY_END = null;
    
    public EntryManager(){
        eventFactory = XMLEventFactory.newInstance();
        LINE_END = eventFactory.createDTD("\n");
        LINE_TAB = eventFactory.createDTD("\t");

        ENTRIES_START = eventFactory.createStartElement(
          "","", Data.ENTRIES.name());
        ENTRIES_END =eventFactory.createEndElement(
          "", "", Data.ENTRIES.name());
        ENTRY_START = eventFactory.createStartElement(
          "","", Data.ENTRY.name());
        ENTRY_END =eventFactory.createEndElement(
          "", "", Data.ENTRY.name());
      }
      public void setFilePath(String filePath) {
        this.filePath = filePath;
      }
      
      public List<Entry> save(String predicate)
	      throws FileNotFoundException, XMLStreamException{
	      List<Entry> entries = getAll();
	      Entry newEntry = new Entry();
	      newEntry.predicate = predicate;
	      entries.add(newEntry);

	      XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
	      XMLEventWriter eventWriter = outputFactory
	              .createXMLEventWriter(new FileOutputStream(filePath));

	      eventWriter.add(eventFactory.createStartDocument());
	      eventWriter.add(LINE_END);

	      eventWriter.add(ENTRIES_START);
	      eventWriter.add(LINE_END);

	      for(Entry entry: entries){
	        addEntry(eventWriter, entry.predicate);
	      }

	      eventWriter.add(ENTRIES_END);
	      eventWriter.add(LINE_END);

	      eventWriter.add(eventFactory.createEndDocument());
	      eventWriter.close();
	      return entries;
	    }
  		
      private void addEntry(XMLEventWriter eventWriter, String predicate) throws XMLStreamException {
          eventWriter.add(ENTRY_START);
          eventWriter.add(LINE_END);
          createNode(eventWriter, Data.LOGICALOPERATION.name(), predicate);
          eventWriter.add(ENTRY_END);
          eventWriter.add(LINE_END);

      }

      private void createNode(XMLEventWriter eventWriter, String name,
              String value) throws XMLStreamException {
          StartElement sElement = eventFactory.createStartElement("", "", name);
          eventWriter.add(LINE_TAB);
          eventWriter.add(sElement);

          Characters characters = eventFactory.createCharacters(value);
          eventWriter.add(characters);

          EndElement eElement = eventFactory.createEndElement("", "", name);
          eventWriter.add(eElement);
          eventWriter.add(LINE_END);
        }

      private List<Entry> getAll(){
        List entries = new ArrayList();

        try{

          File file = new File(filePath);
          if(!file.exists()){
            return entries;
          }

          XMLInputFactory inputFactory = XMLInputFactory.newInstance();
          InputStream in = new FileInputStream(file);
          XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

          Entry entry = null;
          while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();

            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (startElement.getName().getLocalPart()
                  .equals(Data.ENTRY.name())) {
                    entry = new Entry();
                }

                if (event.isStartElement()) {
                    if (event.asStartElement().getName().getLocalPart()
                            .equals(Data.LOGICALOPERATION.name())) {
                        event = eventReader.nextEvent();
                        entry.predicate =event.asCharacters().getData();
                        continue;
                    }
                }
            }
            if (event.isEndElement()) {
                EndElement endElement = event.asEndElement();
                if (endElement.getName().getLocalPart()
                .equals(Data.ENTRY.name())) {
                    entries.add(entry);
                }
            }
          }
        }catch (FileNotFoundException e) {
          e.printStackTrace();
        }catch (XMLStreamException e) {
          e.printStackTrace();
        }catch(IOException ioException){
          ioException.printStackTrace();
        }

        return entries;
      }

    public String getAllAsHTMLTable(List<Entry> entries){
      StringBuilder htmlOut = new StringBuilder("<table>");
      htmlOut.append("<tr><th>Predicates</th></tr>");
      if(entries == null || entries.size() == 0){
        htmlOut.append("<tr><td>No entries yet.</td></tr>");
      }else{
        for(Entry entry: entries){
           htmlOut.append("<tr><td>"+entry.predicate +"</td></tr>");
        }
      }
      htmlOut.append("</table>");
      return htmlOut.toString();
    }


  }	
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
		 RequestDispatcher rd=request.getRequestDispatcher("assignment8a");  
        	rd.forward(request, response);  

 		
	}
	/**
	 * The method that constructs the truth table
	 * It goes through every possible binary combination for the variables, and calls parseEquation()
	 * for each one. If parseEquation returns false, the program stops executing
	 */
	private static String[][] TruthTable(ArrayList<EquationVariables1> variables, ArrayList<Object> equation) {
		int width = variables.size();
		int length = (int) Math.pow(2, width);
		String[][] table = new String[length+1][width+2];
		table[0][0] = "Rows";
		
		//prints out the top row of the truth table
		for (int i = 0; i < variables.size(); i++){
			table[0][i+1] = variables.get(i).getName();
			System.out.print(" | " + variables.get(i).getName());
		}
		table[0][width+1] = "Result";
		
		for (int i=1; i<length+1; i++) {
			String value = "";
			String row = i + ".";
			table[i][0] = row;
			for (int j=width-1; j>=0; j--) {
				int v = (i-1)/(int) Math.pow(2, j)%2;
				if (v == 1){
					variables.get(j).setState(true);
					table[i][j+1] = "1";
	            }
				else {
					variables.get(j).setState(false);
					table[i][j+1] = "0";
				}
				//writer.append(row);
			}
			// Calculate current state of equation
			value = calculateBoolean(equation);
			table[i][width+1] = value;		
		}
		return table;
	}
	
	private static String calculateBoolean(ArrayList<Object> equation) {
		ArrayList AND = new ArrayList(Arrays.asList("&&", "AND", "&", "*"));
		ArrayList OR = new ArrayList(Arrays.asList("+", "^", "||", "|", "OR", "V"));
		ArrayList NOT = new ArrayList(Arrays.asList("~", "NOT", "!"));
		ArrayList<Object> temp = new ArrayList<>(equation);
		String result = "E";
		// change boolean values to ints
		for (int j = 0; j < temp.size();j++){
			if (temp.get(j).getClass().equals(EquationVariables1.class)){
				temp.set(j, ((EquationVariables1)temp.get(j)).getState() ? 1 : 0);
			}
		}
		for (int i = 0; i < temp.size(); i++){
			if (temp.get(i).getClass().equals(String.class)){
				// check is eligible for NOT operation
				if ((NOT.contains(temp.get(i))) && (temp.get(i+1).getClass().equals(Integer.class))){
					invertVal(i+1, temp);
					//System.out.println("NOT check:" + temp);
				}else if ((!(i == 0) && !(i == temp.size()-1)) && (temp.get(i-1).getClass().equals(Integer.class))){
					if ((temp.get(i+1).getClass().equals(Integer.class))){
						if (OR.contains(temp.get(i))){
							orValues(i-1, i+1, temp);
							//System.out.println("OR check:" + temp);
						}else if (AND.contains(temp.get(i))){
							andValues(i-1, i+1, temp);
							//System.out.println("AND check:" + temp);
						}
					}else if (NOT.contains(temp.get(i+1)) && (temp.get(i+2).getClass().equals(Integer.class))){
						invertVal(i+2,temp);
						if (OR.contains(temp.get(i))){
							orValues(i-1, i+2, temp);
						}else if (AND.contains(temp.get(i))){
							andValues(i-1, i+2, temp);
						}
						temp.set(i+1, "");
					}else{
						//System.out.println("Invalid String");
						return result;
					}
				}else {
					//System.out.println("Invalid String");
					return result;
				}
			//If two integers are next to each other, end the program for an improper equation
			}else if (temp.get(i).getClass().equals(Integer.class) && i < temp.size()-1 && temp.get(i+1).getClass().equals(Integer.class)){
				return result;
			}
		}
		result = temp.get(temp.size()-1).toString();
		return result;
	}
	
	public static void invertVal(int pos, ArrayList<Object> temp){
		if ((Integer)temp.get(pos)==0){
			temp.set(pos, 1);
		}else{
			temp.set(pos, 0);
		}
	}
	
	public static void orValues(int leftPos, int rightPos,  ArrayList<Object> temp){
		if ((Integer)temp.get(leftPos) == 1 || (Integer) temp.get(rightPos) == 1){
			temp.set(rightPos, 1);
		}else{
			temp.set(rightPos, 0);
		}
	}
	
	public static void andValues(int leftPos, int rightPos,  ArrayList<Object> temp){
		if ((Integer) temp.get(leftPos) == 1 && ((Integer) temp.get(rightPos)) == 1){
			temp.set(rightPos, 1);
		}else{
			temp.set(rightPos, 0);
		}
	}
	
	public static void changeDisplay(String t, String f, String[][] table) {
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				if (table[i][j].equals("1")) {
					table[i][j] = t;
				}
				else if(table[i][j].equals("0"))
				{ 
					table[i][j] = f;
				}
			}
		}
	}
	
	public String printTable(String[][] table) {
    	String result = "";
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				result += table[i][j] + "|";
			}
			result += "\n";
		}
		return result;
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
	   // Put the focus in the predicate field
	   out.println ("<script>");
	   out.println ("  function setFocus(){");
	   out.println ("    document.persist2file.NAME.focus();");
	   out.println ("  }");
	   out.println ("</script>");
	   out.println("</head>");
	   out.println("");
	} // End PrintHead 

	
	/** *****************************************************
	 *  Prints the <BODY> of the HTML page with the form data
	 *  values from the parameters.
	********************************************************* */
	private void PrintBody (PrintWriter out, String lhs, String rhs, String rslt)
	{
		out.println("<body onLoad=\"setFocus()\">");
		out.println("    <h1><center>Predicate Logic Calculator</center></h1>");
		out.println("    <h2><center>Sonal Kumar * Angela Gentile * George Terziysky * SWE-432-001</center>  </h2>");
		out.println("	 <h3><center>Formatting/Syntax Instructions:</center></h3>");
		out.println("    <h4><center>In order to calculate the final value of the logical operation, type in the predicate with the following constraints:</center></h4>");
		out.println("");
		out.println("    <ul>");
		out.println("        <li>The entry should be typed in the format of (Variable A)(LOGICAL OPERATOR)(Variable B)</li>");
		out.println("        <li>");
		out.println("            The entry can also be an extention of the format described above. There can be multiple Variables with logical operations in between them. For example, you can enter");
		out.println("            \"Apple OR Orange\",which would give you its Truth Table Values.");
		out.println("            Variables can be any letter or Name. Each Variable and Operand must be separated by a space.");
		out.println("        </li>");
		out.println("    </ul>");
		out.println("    <h4><center>Options for supported logical symbols:</center></h4>");
		out.println("    <ul>");
		out.println("        <li>Supported symbols for AND: \"&&\", \"AND\", \"&\", \"^\", \"*\"</li>");
		out.println("        <li>Supported symbols for OR: \"||\", \"|\", OR, \"V\", \"+\" </li>");
		out.println("        <li>Supported symbols for NOT: \"~\", \"NOT\", \"!\"</li>");
		out.println("        <li>Supported symbols for EQUAL: \"==\", \"=\", \"EQUAL\"</li>");
		out.println("    </ul>");
		out.println("    <br />    <br />    <br />");
		out.println("");
		out.println("    <form name=\"persist2file\" method=\"post\" action=\"\\assignment8\">");
		out.println("        <center>");
		out.println("			<select name=\"display\">");
		out.println("		  	<option value=\"1/0\">1/0</option>");
		out.println("			<option value=\"T/F\">T/F</option>");
		out.println("			<option value=\"t/f\">t/f</option>");
		out.println("			<option value=\"X/O\">X/O</option>");
		out.println("			<option value=\"TRUE/FALSE\">TRUE/FALSE</option>");
		out.println("		</select>");
		out.println("		</center>");
		out.println("    <br />");
		out.println("        <center>");
		out.println("            <label for=\"logicalOperation\">Enter Logical Operation:</label>");
		out.println("            <input type=\"text\" id=\"logicalOperation\" name=\"LOGICALOPERATION\"><br><br>");
		out.println("            <input type=\"submit\" value=\"Submit\" style=\"background-color: #80ced6\">");
		out.println(" 			 <input type=\"submit\" value=\"" + OperationXMLfile  + "\" name=\"Operation\">");
		out.println("        </center>");
		out.println("    </form>");
		out.println("</body>");
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
	   out.println("<p><center>Collaboration Summary: All group members worked on different parts of the assignment and brought the pieces together in the end. Sonal & Angela worked on implementing nodes for the XML data into the existing Assignment5 code. George helped parse the data into the nodes and implement the html body. An error we ran into was the ampersand(&) can not be displayed as an XML element unless it is replaced with an entity refereance. </center></p>");
	   out.println("</html>");
	} // End PrintTail
	
	private void printXMLBody (PrintWriter out, String tableString){
	    out.println("<body>");
	    out.println("<p>");
	    out.println("On the left are previous predicates entered. You may enter one on the bottom");
	    out.println(tableString);
	    out.println("");
	    out.println("</p>");
		out.println("    <form name=\"persist2file\" method=\"post\" action=\"\\assignment8\">");
		out.println("        <center>");
		out.println("			<select name=\"display\">");
		out.println("		  	<option value=\"1/0\">1/0</option>");
		out.println("			<option value=\"T/F\">T/F</option>");
		out.println("			<option value=\"t/f\">t/f</option>");
		out.println("			<option value=\"X/O\">X/O</option>");
		out.println("			<option value=\"TRUE/FALSE\">TRUE/FALSE</option>");
		out.println("		</select>");
		out.println("		</center>");
		out.println("    <br />");
		out.println("        <center>");
		out.println("            <label for=\"logicalOperation\">Enter Logical Operation:</label>");
		out.println("            <input type=\"text\" id=\"logicalOperation\" name=\"LOGICALOPERATION\"><br><br>");
		out.println("            <input type=\"submit\" value=\"Submit\" style=\"background-color: #80ced6\">");
		out.println("        </center>");
		out.println("    </form>");
	    out.println("");
	    out.println("</body>");
	  }
	
	private void PrintResponseBody (PrintWriter out)
	{
		out.println("<body onLoad=\"setFocus()\">");
		out.println("    <h1><center>Predicate Logic Calculator</center></h1>");
		out.println("    <h2><center>Sonal Kumar * Angela Gentile * George Terziysky * SWE-432-001</center>  </h2>");
		out.println("	 <h3><center>Formatting/Syntax Instructions:</center></h3>");
		out.println("    <h4><center>In order to calculate the final value of the logical operation, type in the predicate with the following constraints:</center></h4>");
		out.println("");
		out.println("    <ul>");
		out.println("        <li>The entry should be typed in the format of (Variable A)(LOGICAL OPERATOR)(Variable B)</li>");
		out.println("        <li>");
		out.println("            The entry can also be an extention of the format described above. There can be multiple Variables with logical operations in between them. For example, you can enter");
		out.println("            \"Apple OR Orange\",which would give you its Truth Table Values.");
		out.println("            Variables can be any letter or Name. Each Variable and Operand must be separated by a space.");
		out.println("        </li>");
		out.println("    </ul>");
		out.println("    <h4><center>Options for supported logical symbols:</center></h4>");
		out.println("    <ul>");
		out.println("        <li>Supported symbols for AND: \"&&\", \"AND\", \"&\", \"^\", \"*\"</li>");
		out.println("        <li>Supported symbols for OR: \"||\", \"|\", OR, \"V\", \"+\" </li>");
		out.println("        <li>Supported symbols for NOT: \"~\", \"NOT\", \"!\"</li>");
		out.println("        <li>Supported symbols for EQUAL: \"==\", \"=\", \"EQUAL\"</li>");
		out.println("    </ul>");
		out.println("    <br />    <br />    <br />");
		out.println("");
		out.println("    <form name=\"persist2file\" method=\"post\" action=\"\\assignment8\">");
		out.println("        <center>");
		out.println("			<select name=\"display\">");
		out.println("		  	<option value=\"1/0\">1/0</option>");
		out.println("			<option value=\"T/F\">T/F</option>");
		out.println("			<option value=\"t/f\">t/f</option>");
		out.println("			<option value=\"X/O\">X/O</option>");
		out.println("			<option value=\"TRUE/FALSE\">TRUE/FALSE</option>");
		out.println("		</select>");
		out.println("		</center>");
		out.println("    <br />");
		out.println("        <center>");
		out.println("            <label for=\"logicalOperation\">Enter Logical Operation:</label>");
		out.println("            <input type=\"text\" id=\"logicalOperation\" name=\"LOGICALOPERATION\"><br><br>");
		out.println("            <input type=\"submit\" value=\"Submit\" style=\"background-color: #80ced6\">");
		out.println(" 			 <input type=\"submit\" value=\"" + OperationXMLfile  + "\" name=\"Operation\">");
		out.println("        </center>");
		out.println("    </form>");

	}
	
	
	private void PrintResponseBodyEnd (PrintWriter out){
		
		out.println("</body>");
		out.println("");
		
	}

}

class EquationVariables1 {
	
	private boolean state;
	private String name;
	
	/**
	 * Stores variables with a name and a binary state (1 or 0)
	 */
	public EquationVariables1(String theName, boolean theState){
		name = theName;
		state = theState;
	}
	
	// Returns the binary state 
	public boolean getState(){
		return state;
	}
	
	// Returns the name 
	public String getName(){
		return name;
	}

	public void setState(boolean b){
		state = b;	
	}
	
	@Override
	public String toString() {
		return ("Variable: " + this.getName()+ " Value: " + this.getState());
	}
		
}
