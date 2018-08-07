import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


public class Retrieval {

	public static HashSet<String> distinctTerms=new HashSet<String>();//Store All Distinct Terms
	public static HashMap<Integer, ArrayList<String>> documentAndTerms = new HashMap<Integer, ArrayList<String>>();
	public static HashMap<Integer, String> fileNameHolder = new HashMap<Integer, String>();
	public static HashMap<String,  ArrayList<Integer>> postingList = new HashMap<String, ArrayList<Integer>>();
	public static HashMap<String,  HashMap<Integer,  ArrayList<Integer>>> positionalPostingList = new HashMap<String, HashMap<Integer,  ArrayList<Integer>>>();
	public static int bufferSize=1310720;
	private static int bufferTempSize=0;
	private static int BSBIFileName=0;
	public static double documentTotalSize=0;
	
	public static void readFile(String type) throws IOException
	{
		ElapsedTime time=new ElapsedTime();
	   	time.start();
	    System.out.println("Loading..." + type);
	    
		String FolderPath = (new File(".").getAbsolutePath())+"/TextFolder";
		File folder = new File(FolderPath);//Current Folder
		File[] listOfFiles = folder.listFiles();
		documentTotalSize = listOfFiles.length;
		int fileCounter = 0;
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		    	 //ArrayList<String> DocumentAllTerms = new ArrayList<String>();
		    	 BufferedReader br = new BufferedReader(new FileReader(FolderPath+"/"+file.getName()));
		    	 try {
		    	        String line = br.readLine();
		    	        if(type	== "BooleanRetrieval")
		    	        {
		    	        	ArrayList<String> DocumentAllTerms = new ArrayList<String>();
			    	        while (line != null) {
			    	            String [] Terms = line.split(" ");
			    	            for (String Term : Terms) {
			    	            	String tempTerm = processWord(Term.trim());
									distinctTerms.add(tempTerm);
									DocumentAllTerms.add(tempTerm);
								}
			    	            line = br.readLine();
			    	        }
			    	        documentAndTerms.put(fileCounter, DocumentAllTerms);
		    	        }
		    	        else if (type=="PostingList")
		    	        {
			    	        while (line != null) {
			    	            String [] Terms = processWord(line).split(" ");
			    	            for (String Term : Terms) {
			    	            	//String [] tempTerms = processWord(Term).split(" ");
			    	            	//for (String tempTerm : tempTerms) {
			    	            	//if(!distinctTerms.contains(tempTerm))
				    	            //{
				    	            	distinctTerms.add(Term.trim());
				    	            	addPostingList(Term.trim(),fileCounter);
				    	            //}
			    	            	//}
								}
			    	            line = br.readLine();
			    	        }
		    	        }
		    	        else if (type=="PositionalPostingList")
		    	        {
		    	        	int tokenCounter=0;
			    	        while (line != null) {
			    	            String [] Terms = processWord(line).split(" ");
			    	            for (String Term : Terms) {
			    	            	//String [] tempTerms = processWord(Term).split(" ");
			    	            	//for (String tempTerm : tempTerms) {
			    	            	//if(!distinctTerms.contains(tempTerm))
				    	            //{
			    	            		if(!Term.equals(""))
			    	            		{
			    	            			distinctTerms.add(Term.trim());
			    	            			addPositionalPostingList(Term.trim(), fileCounter,tokenCounter);
			    	            			tokenCounter++;
			    	            		}
				    	            //}
			    	            	//}
								}
			    	            line = br.readLine();
			    	        }
		    	        }
		    	        else if (type=="TfIdf")
		    	        {
		    	        	int tokenCounter=0;
			    	        while (line != null) {
			    	            String [] Terms = processWord(line).split(" ");
			    	            for (String Term : Terms) {
			    	            	//String [] tempTerms = processWord(Term).split(" ");
			    	            	//for (String tempTerm : tempTerms) {
			    	            	//if(!distinctTerms.contains(tempTerm))
				    	            //{
			    	            		if(!Term.equals(""))
			    	            		{
			    	            			distinctTerms.add(Term.trim());
			    	            			addPositionalPostingList(Term.trim(), fileCounter,tokenCounter);
			    	            			tokenCounter++;
			    	            		}
				    	            //}
			    	            	//}
								}
			    	            line = br.readLine();
			    	        }
		    	        }
		    	        else if (type=="BlockedSortBasedIndexing")
		    	        {
		    	        	
		    	        	int fileSize = (int) (file.length()*1);
		    	        	File BSBIFile = null;
		    	        	if(bufferSize>=(bufferTempSize+fileSize))
		    	        	{
		    	        		BSBIFile = new File((new File(".").getAbsolutePath())+"/IndexFolder"+"/"+BSBIFileName);
		    	        		bufferTempSize+=fileSize;
		    	        	}
		    	        	else
		    	        	{
		    	        		//Write File
		    	        		BSBIFile = new File((new File(".").getAbsolutePath())+"/IndexFolder"+"/"+BSBIFileName);
		    	        		FileWriter fw = new FileWriter(BSBIFile.getAbsoluteFile());
		    	        		BufferedWriter bw = new BufferedWriter(fw);
		    	        		
		    	        		Map<String, ArrayList<Integer>> map = new TreeMap<String, ArrayList<Integer>>(postingList);//Unsorted hashmap convert to sorted treemap
		    	        		
		    	    			for (Entry<String,ArrayList<Integer>> value : map.entrySet()) {
		    	    				bw.write(value.getKey());
		    	    				bw.write(" ");
		    	    				for (int i:value.getValue()) {
		    	    					bw.write(i + ",");
									}
		    	    				bw.newLine();
		    	    			}
		    	    			bw.close();
		    	        		//Clear Posting List
		    	    			postingList.clear();
		    	    			
		    	        		bufferTempSize=0;
		    	        		BSBIFileName++;
		    	        		BSBIFile = new File((new File(".").getAbsolutePath())+"/IndexFolder"+"/"+BSBIFileName);
		    	        	}
		    	        	
		    				if (!BSBIFile.exists()) {
		    					BSBIFile.createNewFile();
		    				}
		    				
		    	        	while (line != null) {
			    	            String [] Terms = processWord(line).split(" ");
			    	            for (String Term : Terms) {
			    	            	if(!Term.equals(""))
			    	            	{
			    	            		addPostingList(Term.trim(),fileCounter);
			    	            	}
								}
			    	            line = br.readLine();
		    	        	}
		    	        }

		    	    } finally {
		    	    	fileNameHolder.put(fileCounter,file.getName());
		    	        br.close();
		    	    }	 
		    }
		    fileCounter++;
		}
		time.stop();
		time.getElapsedTime();
	}
	
	public static void removeKeyWords(ArrayList<String> Terms)
	{
		ArrayList<String> Temp=new ArrayList<String>();
		Temp =(ArrayList<String>) Terms.clone();
		for (String foo : Temp) {
			if(foo.equals("AND") || foo.equals("OR"))
				Terms.remove(foo);
		}
		
	}
	
	public static String processWord(String x) {
	    return x.replaceAll("[^a-zA-Z ]", " ").toUpperCase();
	}
	
	private static void addPostingList(String term,Integer fileCounter) {
		ArrayList<Integer> checkAvaible=postingList.get(term);
		int count=0;
		if(checkAvaible!=null && !term.equals(""))
		{
			//Set keys = postingList.keySet();
			
	    	//for (Iterator i = keys.iterator(); i.hasNext();) 
	    	//{
	    	//	String key = (String) i.next();
	    	//	if(key.equals(term))
	    	//	{
	    			count++;
		    		if(!((ArrayList<Integer>) postingList.get(term)).contains(fileCounter))
		    			((ArrayList<Integer>) postingList.get(term)).add(fileCounter);
	    	//	}
	    	//}
		}
    	if(count==0 && !term.equals(""))
    	{
    		ArrayList<Integer> temp = new ArrayList<Integer>();
    		temp.add(fileCounter);
    		postingList.put(term, temp);
    	}
	}
	
	private static void addPositionalPostingList(String term,Integer fileCounter,Integer tokenCounter) {
		HashMap<Integer, ArrayList<Integer>> checkAvaible=positionalPostingList.get(term);
		int count=0;
		if(checkAvaible!=null && !term.equals(""))
		{
			//Set keys = postingList.keySet();
			
	    	//for (Iterator i = keys.iterator(); i.hasNext();) 
	    	//{
	    	//	String key = (String) i.next();
	    	//	if(key.equals(term))
	    	//	{
	    			count++;
		    		//if(!((ArrayList<Integer>) postingList.get(term)).contains(fileCounter))
		    		//	((ArrayList<Integer>) postingList.get(term)).add(fileCounter);
	    			if(((HashMap<Integer, ArrayList<Integer>>)positionalPostingList.get(term)).containsKey(fileCounter))
	    				((HashMap<Integer, ArrayList<Integer>>)positionalPostingList.get(term)).get(fileCounter).add(tokenCounter);
	    			else
	    			{
	    				ArrayList<Integer> tempArraylist = new ArrayList<Integer>();
	    				tempArraylist.add(tokenCounter);
	    				positionalPostingList.get(term).put(fileCounter, tempArraylist);
	    			}
	    	//	}
	    	//}
		}
    	if(count==0 && !term.equals(""))
    	{
    		HashMap<Integer, ArrayList<Integer>> temp = new HashMap<Integer, ArrayList<Integer>>();
			ArrayList<Integer> tempArraylist = new ArrayList<Integer>();
			tempArraylist.add(tokenCounter);
    		temp.put(fileCounter, tempArraylist);
    		positionalPostingList.put(term,temp);
    	}
	}
	
	public static <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {
		Comparator<K> valueComparator =  new Comparator<K>() {
		    public int compare(K k1, K k2) {
		        int compare = map.get(k2).compareTo(map.get(k1));
		        if (compare == 0) return 1;
		        else return compare;
		    }
		};
		Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
		sortedByValues.putAll(map);
		return sortedByValues;
	}
}
