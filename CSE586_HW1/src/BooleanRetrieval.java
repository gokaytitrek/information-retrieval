import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;



public class BooleanRetrieval extends Retrieval {

	public static HashMap<String, ArrayList<Integer>> matrix = new HashMap<String, ArrayList<Integer>>();
	public static void main(String[] args) throws IOException {
		readFile("BooleanRetrieval");
		long startTime = System.currentTimeMillis();
		makeMatrix(distinctTerms,documentAndTerms,false);
		long stopTime = System.currentTimeMillis();
		System.out.println(stopTime);
	    long elapsedTime = stopTime - startTime;
	    System.out.println(String.format("%d min, %d sec", 
	    	    TimeUnit.MILLISECONDS.toMinutes(elapsedTime),
	    	    TimeUnit.MILLISECONDS.toSeconds(elapsedTime) - 
	    	    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedTime))
	    	));
		
		Scanner in = new Scanner(System.in);
		String s ;
		do {
			System.out.println("Enter Query:(BooleanRetrieval)");
			s = in.nextLine();
			query(s);
			System.out.println("to exit : *EXIT*");
			System.out.println();
			System.out.println();
		} while (!s.equals("*EXIT*"));
		in.close();
	}
	
	private static void makeMatrix(HashSet<String> distinctTerms,HashMap<Integer,ArrayList<String>> documentAndTerms,Boolean showMatrix)
	{
		ArrayList<Integer> Matrix = new ArrayList<Integer>();
		for (String  Term : distinctTerms) {
			Matrix = new ArrayList<Integer>();
			for (Entry<Integer, ArrayList<String>> value : documentAndTerms.entrySet()) {
				//if(value.contains(Term))
				if(value.getValue().contains(Term))
					Matrix.add(1);
				else
					Matrix.add(0);
			}
			//System.out.print(Term+":\t\t ");
			matrix.put(Term, Matrix);
			if(showMatrix)
			{
				for (Integer B : Matrix) {
					System.out.print(B+"\t");
				}
				System.out.println();
			}
		}

	}
	
	private static void query(String QueryString) {
		String[] Terms = QueryString.toUpperCase().split(" ");
		ArrayList<String> Term=new ArrayList<String>();
		for (String string : Terms) {
			Term.add(string);
		}
		if (Term.contains("AND"))
		{
			removeKeyWords(Term);
			ArrayList<ArrayList<Integer>> Temp = getQueryResult(Term);
			if(Temp!=null)
				showResult(Temp,"AND");
			else 
				System.out.println("No Result");
		}
		else if (Term.contains("OR"))
		{
			removeKeyWords(Term);			
			ArrayList<ArrayList<Integer>> Temp = getQueryResult(Term);
			if(Temp!=null)
				showResult(Temp,"OR");
			else 
				System.out.println("No Result");
		}
		else
		{
			ArrayList<ArrayList<Integer>> Temp = getQueryResult(Term);
			if(Temp!=null)
				showResult(Temp,"NO");
			else 
				System.out.println("No Result");
		}
	}
	

	
	private static ArrayList<ArrayList<Integer>> getQueryResult(ArrayList<String> Terms)
	{
		ArrayList<ArrayList<Integer>> ResultMatrix = new ArrayList<ArrayList<Integer>>();
		for (String string : Terms) {
			ArrayList<Integer> Temp	=	matrix.get(string);
			if(Temp !=null)
			{
				if(!Temp.isEmpty())
				{
					ResultMatrix.add(Temp);
				}
				else
				{
					ArrayList<Integer> emptyArray=new ArrayList<Integer>(fileNameHolder.entrySet().size());
					ResultMatrix.add(emptyArray);
				}
			}
			else 
			{
				ArrayList<Integer> emptyArray=new ArrayList<Integer>(Collections.nCopies(fileNameHolder.entrySet().size(), 0));
				ResultMatrix.add(emptyArray);
			}
		}
		return ResultMatrix;
	}
	
	private static void showResult(ArrayList<ArrayList<Integer>> ResultMatrix,String KeyWord)
	{
		int [] resultArray=new int[fileNameHolder.entrySet().size()];
		if(KeyWord.equals("AND"))
		{
			int Temp = 1;
			for(int i=0;i<fileNameHolder.entrySet().size();i++){
				Temp = 1;
				for (ArrayList<Integer> j : ResultMatrix) {
					Temp = Temp * j.get(i);
				}
				resultArray[i]=Temp;
			}
		}
		else 
		{
			int Temp = 0;
			for(int i=0;i<fileNameHolder.entrySet().size();i++){
				Temp = 0;
				for (ArrayList<Integer> j : ResultMatrix) {
					Temp = Temp + j.get(i);
				}
				resultArray[i]=Temp;
			}			
		}
		int Counter=0;
		for (Entry<Integer,String> value : fileNameHolder.entrySet()) {
			if(resultArray[value.getKey()]>0)
			{	
				Counter++;
				System.out.println(value.getValue());
			}
		}
		if(Counter==0)
			System.out.println("No Result");
		else 
			System.out.println("Number Of Files : "+ Counter);
		
		
	}
	
	

}




















