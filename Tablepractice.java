import java.io.PrintWriter;
import java.util.*;

public class Tablepractice {
	
	public static void main(String args[])
	  {

	     String	logicalOperation;
	     
	     //Get input String	 
	     Scanner in = new Scanner(System.in);
	     System.out.println("Enter a string: ");
	     logicalOperation = in.nextLine();
	     System.out.println("Operation: "+logicalOperation);
	     
	     
	     
	     ArrayList legalOps = new ArrayList(Arrays.asList("&&", "AND", "&","*", "^", "+", "||", "|", "OR", "V", "~", "NOT", "!", "==", "=", "EQUAL"));
	     ArrayList arrayEq = new ArrayList(Arrays.asList(logicalOperation.split(" "))); //split by space "A & B -> [A,&,B]"
	     ArrayList arrayOps = new ArrayList();
	     
	     ArrayList<EquationVariables> variableArray = new ArrayList<>();
	 	 ArrayList<Object> equationArray = new ArrayList<>();
	 	 String[][] Table = null;
	     
	     int length = 0;
	     int width = 0;
	     
	     //loops through the equation and stores all variables in a variable array.
	     for(int i = 0; i< arrayEq.size(); i++){
	    	 if(!(legalOps.contains(arrayEq.get(i)))){
	    		 boolean alreadyExists = false; //Keeps track of duplicate variables
	    		 EquationVariables temp = new EquationVariables((String)arrayEq.get(i),true);
	    		 
	    		//checks for duplicate variables and doesn't add them to the array twice
				for (EquationVariables v : variableArray){
					if (v.getName()==temp.getName()){
						alreadyExists = true;
						temp = v;
					}
				}
				if (!alreadyExists){
					variableArray.add(temp); 
				}
				//stores the variable objects that are created in an equation array as well
				equationArray.add(temp);
	    	 }else {
	    		 // Operators get stored in an equation array
	    		 equationArray.add(arrayEq.get(i));
	    	 }
	     }
	     
	     //Creates an instance of the truth table with the proper parameters
	     if (variableArray.size() > 0){
	    	 Table = TruthTable(variableArray, equationArray);
	     }
	     
	     // Change Display Feature
	     String[][] temp = Table;
	     String t = "TRUE";
	     String f = "FALSE";
	     //printTable(temp);
	     changeDisplay(t, f, temp);
	     
	     printTable(temp);
	     
		System.out.println("Parse: "+arrayEq);
		
		//remove nulls from arrayEq
		arrayEq.removeAll(Collections.singleton(null)); //now we have two array lists {A, B} and {&}
				
		//get table dimensions
		width = arrayEq.size();
		length = (int) Math.pow(2, width);
		
		//print the predicate they enetered
		//Print a complete truth table for the predicate, including a column with the result for each row
		//for loop for table size look online

		in.close();	
	  }

	/**
	 * The method that constructs the truth table
	 * It goes through every possible binary combination for the variables, and calls parseEquation()
	 * for each one. If parseEquation returns false, the program stops executing
	 */
	private static String[][] TruthTable(ArrayList<EquationVariables> variables, ArrayList<Object> equation) {
		int width = variables.size();
		int length = (int) Math.pow(2, width);
		//String input = "";
		String[][] table = new String[length+1][width+2];
		table[0][0] = "Rows";
		
		//prints out the top row of the truth table
		System.out.println("START OF TABLE");
		System.out.print(table[0][0]);
		for (int i = 0; i < variables.size(); i++){
			table[0][i+1] = variables.get(i).getName();
			System.out.print(" | " + variables.get(i).getName());
		}
		table[0][width+1] = "Result";
		System.out.println(" || Result");
		
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
			
			// Table test print
			for (int j=0; j<width+2; j++) {
				System.out.print(table[i][j]);
			}
			System.out.println();
			//writer.append("</center>");
			
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
			if (temp.get(j).getClass().equals(EquationVariables.class)){
				temp.set(j, ((EquationVariables)temp.get(j)).getState() ? 1 : 0);
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
						System.out.println("Invalid String");
						return result;
					}
				}else {
					System.out.println("Invalid String");
					return result;
				}
			
			}
		}
		result = temp.get(temp.size()-1).toString();
		//System.out.println(temp.get(temp.size()-1));
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
	
	public static void printTable(String[][] table) {
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				System.out.print(table[i][j] + "|");
			}
			System.out.println();
		}
	}
}

class EquationVariables {
	
	private boolean state;
	private String name;
	
	/**
	 * Stores variables with a name and a binary state (1 or 0)
	 */
	public EquationVariables(String theName, boolean theState){
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
