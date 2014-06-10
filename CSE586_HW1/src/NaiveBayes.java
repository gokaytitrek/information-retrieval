import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.omg.IOP.ENCODING_CDR_ENCAPS;


public class NaiveBayes extends Retrieval {

	public static HashMap<Integer, String> fileClassHolder = new HashMap<Integer, String>();
	public static HashMap<String, Double> classResultHolder = new HashMap<String, Double>();
	public static HashSet<String> distinctClass=new HashSet<String>();
	public static HashSet<String> distinctTestFileTerms=new HashSet<String>();
	public static ArrayList<String> testFileTerms=new ArrayList<String>();
	public static HashMap<String,  HashMap<String,  Integer>> classTermFrequency = new HashMap<String, HashMap<String,  Integer>>();
	private static int countDocs;
	private static String FolderPath;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		int fileCounter = 0;
		FolderPath = (new File(".").getAbsolutePath())+"/Classifier";
		File file = new File(FolderPath);
		String[] names = file.list();

		for(String name : names)
		{
			String subFolder = FolderPath + "\\"+ name;
		    if (new File(subFolder).isDirectory())
		    {
		    	distinctClass.add(name);
		        File subFile = new File(subFolder);
		        File[] subFileNames = subFile.listFiles();
		        for (File sub : subFileNames) {
				    if (sub.isFile()) {
				    	
						fileClassHolder.put(fileCounter,name);
			    	    fileNameHolder.put(fileCounter,sub.getName());
			    	    fileCounter++;
			    	    
			    	    BufferedReader br = new BufferedReader(new FileReader(subFolder+"/"+sub.getName()));
			    	    try
			    	    {
				    	    String line = br.readLine();
				    	    while (line != null) {
			    	            String [] Terms = line.split(" ");
			    	            for (String Term : Terms) {
			    	            	String tempTerm = processWord(Term.trim());
									distinctTerms.add(tempTerm);
								}
			    	            line = br.readLine();
			    	        }
			    	    } finally {
			    	        br.close();
			    	    }	 
				    }
		        }
		    }
		}
		
		countDocs=fileNameHolder.size();
		
		readTestFile();
		TrainMultinomialNM();
		
	}
	
	
	private static void TrainMultinomialNM() throws IOException
	{
		//c: class
		for (String c : distinctClass) {
			Integer cCounter = 0;
			for (Entry<Integer,String> value  : fileClassHolder.entrySet()) {
				if(value.getValue().equals(c))
					cCounter++;
			}
			//Index of Sub folder
			String subFolder = FolderPath + "\\"+ c;
	        File subFile = new File(subFolder);
	        File[] subFileNames = subFile.listFiles();
	        for (File sub : subFileNames) {
			    if (sub.isFile()) {
			    	BufferedReader br = new BufferedReader(new FileReader(subFolder+"/"+sub.getName()));
			    	try {
				    	String line = br.readLine();
		    	        while (line != null) {
		    	            String [] Terms = processWord(line).split(" ");
		    	            for (String Term : Terms) {
		    	            	if(!Term.equals(""))
		    	            	{
		    	            		addClassTermFrequency(c,Term);
		    	            	}
							}
		    	            line = br.readLine();
		    	        }
			    	} finally {
		    	        br.close();
			    	}
			    }
	        }
			
			double priorProbability  = (double)cCounter / (double)countDocs;
			classResultHolder.put(c,applyMultinomialNB(c,priorProbability));
		}
		
		showResult();
		
	}
	
	private static void addClassTermFrequency(String className,String term) {
		HashMap<String, Integer> checkAvaible=classTermFrequency.get(className);
		if(checkAvaible!=null)
		{
			if(checkAvaible.get(term)!=null)
				checkAvaible.put(term, checkAvaible.get(term)+1);
			else
				checkAvaible.put(term, 1);
		}
		else
		{
			HashMap<String, Integer> temp = new HashMap<String, Integer>();
			temp.put(term, 1);
			classTermFrequency.put(className, temp);
			
		}
	}
	
	private static void readTestFile() throws IOException
	{
		String subFolder = FolderPath; //+ "\\"+ "Test";
        File subFile = new File(subFolder);
        File[] subFileNames = subFile.listFiles();
        for (File sub : subFileNames) {
		    if (sub.isFile()) {
		    	BufferedReader br = new BufferedReader(new FileReader(subFolder+"/"+sub.getName()));
		    	try {
			    	String line = br.readLine();
	    	        while (line != null) {
	    	            String [] Terms = processWord(line).split(" ");
	    	            for (String Term : Terms) {
	    	            	if(!Term.equals(""))
	    	            	{
	    	            		testFileTerms.add(Term);
	    	            		distinctTestFileTerms.add(Term);  
	    	            	}
						}
	    	            line = br.readLine();
	    	        }
		    	} finally {
	    	        br.close();
		    	}
		    }
        }
	}
	
	private static double applyMultinomialNB(String c,double priorProbability)
	{
		double result=0;//1 * priorProbability;
		int smoothCount = distinctTerms.size();//distinctTestFileTerms.size()*distinctClass.size();//Olasýlýk hesabýnda bolum olarak eklenecek kýsým. Pay'a eklenen +1den dolayý
		HashMap<String, Integer> termFrequency = classTermFrequency.get(c);
		int totalCountOfTerms = 0;
		for (Entry<String, Integer> i : termFrequency.entrySet()) {
			totalCountOfTerms+=i.getValue();
		}
		
		for (String term : testFileTerms) {
			Integer frequency = termFrequency.get(term);
			if(frequency==null)
				frequency=0;
			result+= Math.log(((double)frequency+1)/((double)totalCountOfTerms+(double)smoothCount));
		}
		return result * priorProbability;
	}
	
	private static void showResult()
	{
		 double maxValueInMap=(Collections.max(classResultHolder.values()));  // This will return max value in the Hashmap
	        for (Entry<String, Double> entry : classResultHolder.entrySet()) {  // Itrate through hashmap
	            if (entry.getValue()==maxValueInMap) {
	                System.out.println(entry.getKey());     // Print the key with max value
	            }
	        }
		
	}

}
