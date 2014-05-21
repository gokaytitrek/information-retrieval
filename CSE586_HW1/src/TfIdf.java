import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Map.Entry;


public class TfIdf extends PositionalPostingList {

	private static final String smartTermFrequency="l";
	private static final String smartDocumentFrequency="t";
	private static final Integer TopElementCount = 10;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		range = 10000000 ;
		readFile("TfIdf");
		ElapsedTime time=new ElapsedTime();
		Scanner in = new Scanner(System.in);
		String s ;
		do {
			System.out.println("Enter Query: (TfIdf)");
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
			HashMap<Integer, Double> resultOfTfIdf= new HashMap<Integer, Double>();
			HashMap<String,  HashMap<Integer,  ArrayList<Integer>>> Temp = getQueryResult(Term);
			Integer counter = 0;
			if(Temp!=null && Temp.size() ==	Term.size())
			{
				HashMap<Integer,  ArrayList<Integer>> resultData = getResultData(Temp);
				for(Entry<Integer,  ArrayList<Integer>> value:resultData.entrySet())
				{
					Double TfIdf = new Double(0);
					for (String string : Terms) {
						//her string icin tfidf hesapla
						HashMap<Integer,  ArrayList<Integer>> tempHashMap = positionalPostingList.get(string);
						int size=tempHashMap.get(value.getKey()).size();
						int documentSize=tempHashMap.size();
						TfIdf  = TfIdf + (calculateTf(size,smartTermFrequency) * calculateIdf(documentSize,smartDocumentFrequency));
					}
					resultOfTfIdf.put(value.getKey(), TfIdf);
					counter++;
				}

				printResult(resultOfTfIdf,counter);
			}
			else 
				System.out.println("No Result");
		}
	}
	
	private static void showSingleResult(HashMap<String,  HashMap<Integer,  ArrayList<Integer>>> resultSet)
	{
		int counter=0;
		HashMap<Integer, Double> resultOfTfIdf= new HashMap<Integer, Double>();
		if(resultSet!=null)
		{
			for (Entry<String,  HashMap<Integer,  ArrayList<Integer>>> value : resultSet.entrySet())
			{
				for(Entry<Integer,  ArrayList<Integer>> value2:value.getValue().entrySet())
				{
					Double TfIdf = new Double(0);
					counter++;
					int size=value2.getValue().size();
					TfIdf  = TfIdf + (calculateTf(size,smartTermFrequency) * calculateIdf(value.getValue().entrySet().size(),smartDocumentFrequency));
					resultOfTfIdf.put(value2.getKey(), TfIdf);
				}
				System.out.println();
			}
		}
		if(counter==0)
			System.out.println("No Result");
		else 
			printResult(resultOfTfIdf,counter);
	}
	
	private static double calculateTf(int termFrequency,String smart) {
		double result=0;
		switch (smart) {
		case "n":
			result = termFrequency;
			break;
		case "l":
			result = 1 + Math.log10(termFrequency);
			break;
		}
		return result;
		
	}
	
	private static double calculateIdf(double documentFrequency,String smart) {
		double result=0;
		switch (smart) {
		case "n":
			result = 1;
			break;
		case "t":
			result = Math.log10(documentTotalSize/documentFrequency);
		}
		return result;
	}
	
	private static void printResult(HashMap<Integer, Double> resultOfTfIdf,Integer counter)
	{
		Map<Integer, Double> map = new TreeMap<Integer, Double>(resultOfTfIdf);//Unsorted hashmap convert to sorted treemap
		map = sortByValues(map);
		Integer internalCounter = 0;
		for (Entry<Integer,Double> value: map.entrySet()) {
			if(internalCounter<TopElementCount)
			{
				showFileName(value.getKey());
				System.out.print("Tf-Idf :" + value.getValue());
				System.out.println("");
				internalCounter++;
			}
			else
				break;
		}
		
		System.out.println("Number Of Files : "+ counter);
	}
}
