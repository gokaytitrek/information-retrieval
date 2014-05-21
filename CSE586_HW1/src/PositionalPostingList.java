import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Map.Entry;


public class PositionalPostingList  extends Retrieval {

	
	public static int range;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		range = 10 ;
		readFile("PositionalPostingList");
		ElapsedTime time=new ElapsedTime();
		Scanner in = new Scanner(System.in);
		String s ;
		do {
			System.out.println("Enter Query: (PositionalPostingList)");
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
	
	private static void query(String QueryString) {
		String[] Terms = QueryString.toUpperCase().split(" ");
		ArrayList<String> Term=new ArrayList<String>();
		for (String string : Terms) {
			Term.add(string);
		}
		removeKeyWords(Term);
		
		
		if(Term.size()==1)
		{
			HashMap<String,  HashMap<Integer,  ArrayList<Integer>>> Temp = getQueryResult(Term);
			if(Temp!=null && Temp.size()>0)
			{
				showSingleResult(Temp);
			}
			else 
				System.out.println("No Result");
		}
		else 
		{
			HashMap<String,  HashMap<Integer,  ArrayList<Integer>>> Temp = getQueryResult(Term);
			if(Temp!=null && Temp.size() ==	Term.size())
			{
				//showResult(getResultData(Temp));
				getResultData(Temp);
			}
			else 
				System.out.println("No Result");
		}
	}
	
	public static HashMap<Integer,  ArrayList<Integer>> getResultData(HashMap<String,  HashMap<Integer,  ArrayList<Integer>>> queryResultList)
	{
		HashMap<Integer,  ArrayList<Integer>> resultArrayList=new HashMap<Integer,  ArrayList<Integer>>();
		resultArrayList=null;
		for (Entry<String, HashMap<Integer, ArrayList<Integer>>> value : queryResultList.entrySet()) {
			if(value.getValue()!=null)
				resultArrayList = getMultiKeyWordResult(resultArrayList, value.getValue());
			else
				return null;
		}
		
		return resultArrayList;
	}

//	Original Hali buydu daha sonrasýnda ustteki versiyon ile degistirildi
//	private static HashMap<Integer,  ArrayList<Integer>> getResultData(HashMap<String,  HashMap<Integer,  ArrayList<Integer>>> queryResultList)
//	{
//		HashMap<Integer,  ArrayList<Integer>> resultArrayList=new HashMap<Integer,  ArrayList<Integer>>();
//		resultArrayList=null;
//		for (Entry<String, HashMap<Integer, ArrayList<Integer>>> value : queryResultList.entrySet()) {
//			if(value.getValue()!=null)
//				resultArrayList = getMultiKeyWordResult(resultArrayList, value.getValue());
//			else
//				return null;
//		}
//		return resultArrayList;
//	}
	
	private static HashMap<Integer,  ArrayList<Integer>> getMultiKeyWordResult(HashMap<Integer, ArrayList<Integer>> result,HashMap<Integer, ArrayList<Integer>> newValue)
	{
		HashMap<Integer,  ArrayList<Integer>> temp=new HashMap<Integer,  ArrayList<Integer>>();
		if(result==null)
			return newValue;
		int resultSize=result.size();
		int newValueSize=newValue.size();
		int resultPointer = 0;
		int newValuePointer = 0;
		
		
		ArrayList<Integer> resultKeys=new ArrayList<Integer>();
		ArrayList<Integer> newValueKeys=new ArrayList<Integer>();
		for ( Integer key : result.keySet() ) {
		    resultKeys.add(key);
		}
		for ( Integer key : newValue.keySet() ) {
			newValueKeys.add(key);
		}
		
		Collections.sort(resultKeys);
		Collections.sort(newValueKeys);
		while (resultPointer<resultSize && newValuePointer<newValueSize) {
			if(resultKeys.get(resultPointer).equals(newValueKeys.get(newValuePointer)))
			{
				int i=0;
				int j=0;
				ArrayList<Integer> resultPoint=result.get(resultKeys.get(resultPointer));
				ArrayList<Integer> newValuePoint=newValue.get(newValueKeys.get(newValuePointer));
				ArrayList<Integer> holdPosition =new  ArrayList<Integer>();
				while(i<resultPoint.size())
				{
					
					j=0;
					while(j<newValuePoint.size())
					{
						if((Math.abs(resultPoint.get(i) - newValuePoint.get(j)) <= range))
						{
							
							holdPosition.add(resultPoint.get(i));
						}
						
						j++;
					}
					i++;
				}
				
				if(holdPosition!=null && holdPosition.size()>0)
				{
					temp.put(resultKeys.get(resultPointer), holdPosition);
				}
				
				if(resultKeys.get(resultPointer).equals(newValueKeys.get(newValuePointer)))
				{
					resultPointer++;
					newValuePointer++;
				}
				else if(resultKeys.get(resultPointer) < newValueKeys.get(newValuePointer))
				{
					resultPointer++;
				}
				else if(resultKeys.get(resultPointer) > newValueKeys.get(newValuePointer))
				{
					newValuePointer++;
				}
				
			}
			else if(resultKeys.get(resultPointer) < newValueKeys.get(newValuePointer))
			{
				resultPointer++;
			}
			else if(resultKeys.get(resultPointer) > newValueKeys.get(newValuePointer))
			{
				newValuePointer++;
			}
			else//Change File
			{
				resultPointer++;
				newValuePointer++;
			}
		}
		
		return temp;
	}	
	
//	Original Hali buydu daha sonrasýnda ustteki versiyon ile degistirildi	
//	private static HashMap<Integer,  ArrayList<Integer>> getMultiKeyWordResult(HashMap<Integer, ArrayList<Integer>> result,HashMap<Integer, ArrayList<Integer>> newValue)
//	{
//		if(result==null)
//			return newValue;
//		int resultSize=result.size();
//		int newValueSize=newValue.size();
//		int resultPointer = 0;
//		int newValuePointer = 0;
//		HashMap<Integer,  ArrayList<Integer>> temp=new HashMap<Integer,  ArrayList<Integer>>();
//		
//		ArrayList<Integer> resultKeys=new ArrayList<Integer>();
//		ArrayList<Integer> newValueKeys=new ArrayList<Integer>();
//		for ( Integer key : result.keySet() ) {
//		    resultKeys.add(key);
//		}
//		for ( Integer key : newValue.keySet() ) {
//			newValueKeys.add(key);
//		}
//		
//		Collections.sort(resultKeys);
//		Collections.sort(newValueKeys);
//		while (resultPointer<resultSize && newValuePointer<newValueSize) {
//			if(resultKeys.get(resultPointer).equals(newValueKeys.get(newValuePointer)))
//			{
//				int i=0;
//				int j=0;
//				ArrayList<Integer> resultPoint=result.get(resultKeys.get(resultPointer));
//				ArrayList<Integer> newValuePoint=newValue.get(newValueKeys.get(newValuePointer));
//				HashSet<Integer> holdPosition =new  HashSet<Integer>();
//				while(i<resultPoint.size())
//				{
//					
//					j=0;
//					while(j<newValuePoint.size())
//					{
//						if((Math.abs(resultPoint.get(i) - newValuePoint.get(j)) <= range))
//						{
//							
//							holdPosition.add(resultPoint.get(i));
//						}
//						
//						j++;
//					}
//					i++;
//				}
//				
//				if(holdPosition!=null && holdPosition.size()>0)
//				{
//					showFileName(resultKeys.get(resultPointer));
//					System.out.print("Count :"+ holdPosition.size()+ "\t");
//					for(Integer pos:holdPosition)
//						System.out.print(pos+ "   ");
//					
//					System.out.println();
//				}
//				
//				if(resultKeys.get(resultPointer).equals(newValueKeys.get(newValuePointer)))
//				{
//					resultPointer++;
//					newValuePointer++;
//				}
//				else if(resultKeys.get(resultPointer) < newValueKeys.get(newValuePointer))
//				{
//					resultPointer++;
//				}
//				else if(resultKeys.get(resultPointer) > newValueKeys.get(newValuePointer))
//				{
//					newValuePointer++;
//				}
//				
//			}
//			else if(resultKeys.get(resultPointer) < newValueKeys.get(newValuePointer))
//			{
//				resultPointer++;
//			}
//			else if(resultKeys.get(resultPointer) > newValueKeys.get(newValuePointer))
//			{
//				newValuePointer++;
//			}
//			else//Change File
//			{
//				resultPointer++;
//				newValuePointer++;
//			}
//			/*
//			if(result.get(resultPointer).equals(newValue.get(newValuePointer)))
//			{
//				//temp.add(result.get(resultPointer));
//				resultPointer++;
//				newValuePointer++;
//			}
//			*/
//			/*
//			else if(result.get(resultPointer) < newValue.get(newValuePointer))
//			{
//				resultPointer++;
//			}
//			else if(result.get(resultPointer) > newValue.get(newValuePointer))
//			{
//				newValuePointer++;
//			}
//			*/
//		}
//		
//		return temp;
//	}
	
	public static HashMap<String,  HashMap<Integer,  ArrayList<Integer>>> getQueryResult(ArrayList<String> Terms)
	{
		HashMap<String,  HashMap<Integer,  ArrayList<Integer>>> resultList = new HashMap<String,  HashMap<Integer,  ArrayList<Integer>>>();
		for (String string : Terms) {
			HashMap<Integer,  ArrayList<Integer>> tempHashMap = positionalPostingList.get(string);
			if(tempHashMap!=null)
				resultList.put(string, tempHashMap);
		}
		return resultList;
	}
	
	
	private static void showSingleResult(HashMap<String,  HashMap<Integer,  ArrayList<Integer>>> resultSet)
	{
		int Counter=0;
		if(resultSet!=null)
		{
			for (Entry<String,  HashMap<Integer,  ArrayList<Integer>>> value : resultSet.entrySet())
			{
				System.out.println(value.getKey());
				for(Entry<Integer,  ArrayList<Integer>> value2:value.getValue().entrySet())
				{
					Counter++;
					showFileName(value2.getKey());
					System.out.print("Count :"+ value2.getValue().size()+ "\t");
					for (Integer i : value2.getValue()) {
						System.out.print(i + "   ");
					}
					System.out.println();
				}
				System.out.println();
			}
		}
		if(Counter==0)
			System.out.println("No Result");
		else 
			System.out.println("Number Of Files : "+ Counter);
		
	}
	
	protected static void showFileName(Integer fileKey)
	{
		if(fileKey!=null)
		{
			System.out.print("\t" + fileNameHolder.get(fileKey) + ":   ");
		}
	}

}
