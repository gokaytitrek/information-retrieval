import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Map.Entry;

public class PostingList  extends Retrieval {

	public static void main(String[] args) throws IOException{
		readFile("PostingList");
		//showResult(postingList.get("THE"));
		ElapsedTime time=new ElapsedTime();
		Scanner in = new Scanner(System.in);
		String s ;
		do {
			System.out.println("Enter Query: (PostingList)");
			s = in.nextLine();
			time.start();
			query(s);
			System.out.println("to exit : *EXIT*");
			time.stop();
			time.getElapsedTime();
			System.out.println();
			System.out.println();
		} while (!s.equals("*EXIT*"));
		in.close();
	}
	
	private static void showResult(ArrayList<Integer> resultSet)
	{
		int Counter=0;
		if(resultSet!=null)
		{
			for (Integer integer : resultSet) {
				for (Entry<Integer,String> value : fileNameHolder.entrySet()) {
					if(value.getKey().equals(integer))
					{	
						Counter++;
						System.out.println(value.getValue());
					}
				}
			}
		}
		if(Counter==0)
			System.out.println("No Result");
		else 
			System.out.println("Number Of Files : "+ Counter);
		
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
			HashMap<String,  ArrayList<Integer>> Temp = getQueryResult(Term);
			if(Temp!=null)
				showResult(getResultData(Temp,"AND"));
			else 
				System.out.println("No Result");
		}
		else if (Term.contains("OR"))
		{
			removeKeyWords(Term);
			HashMap<String,  ArrayList<Integer>> Temp = getQueryResult(Term);
			if(Temp!=null)
				showResult(getResultData(Temp,"OR"));
			else 
				System.out.println("No Result");
		}
		else
		{
			removeKeyWords(Term);
			HashMap<String,  ArrayList<Integer>> Temp = getQueryResult(Term);
			if(Temp!=null)
				showResult(getResultData(Temp,"NO"));
			else 
				System.out.println("No Result");
		}
	}
	
	private static HashMap<String,  ArrayList<Integer>> getQueryResult(ArrayList<String> Terms)
	{
		HashMap<String,  ArrayList<Integer>> resultList = new HashMap<String, ArrayList<Integer>>();
		for (String string : Terms) {
			ArrayList<Integer> tempArrayList = postingList.get(string);
			resultList.put(string, tempArrayList);
		}
		return resultList;
	}
	
	private static ArrayList<Integer> getResultData(HashMap<String,  ArrayList<Integer>> queryResultList, String keyWord)
	{
		ArrayList<Integer> resultArrayList=new ArrayList<Integer>();
		if(keyWord.equals("AND"))
		{
			resultArrayList=null;
			for (Entry<String,ArrayList<Integer>> value : queryResultList.entrySet()) {
				if(value.getValue()!=null)
					resultArrayList = getAndResult(resultArrayList, value.getValue());
				else
					return null;
			}
		}
		else 
		{
			 HashSet<Integer> distinctFiles=new HashSet<Integer>();//Store All Distinct Terms
			for (Entry<String,ArrayList<Integer>> value : queryResultList.entrySet()) {
				if(value.getValue()!=null)
				{
					for (Integer i : value.getValue()) {
						distinctFiles.add(i);
					}
				}
			}
			for (Integer i : distinctFiles) {
				resultArrayList.add(i);
			}
		}
		return resultArrayList;
	}
	
	private static ArrayList<Integer> getAndResult(ArrayList<Integer> result,ArrayList<Integer> newValue)
	{
		if(result==null)
			return newValue;
		int resultSize=result.size();
		int newValueSize=newValue.size();
		int resultPointer = 0;
		int newValuePointer = 0;
		ArrayList<Integer> temp=new ArrayList<Integer>();
		while (resultPointer<resultSize && newValuePointer<newValueSize) {
			if(result.get(resultPointer).equals(newValue.get(newValuePointer)))
			{
				temp.add(result.get(resultPointer));
				resultPointer++;
				newValuePointer++;
			}
			else if(result.get(resultPointer) < newValue.get(newValuePointer))
			{
				resultPointer++;
			}
			else if(result.get(resultPointer) > newValue.get(newValuePointer))
			{
				newValuePointer++;
			}
		}
		
		return temp;
	}

}
