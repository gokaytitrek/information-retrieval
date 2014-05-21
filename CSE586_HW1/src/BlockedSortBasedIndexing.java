import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


public class BlockedSortBasedIndexing extends Retrieval {

	private static int bufferTempSize=0;
	private static String FolderPath = (new File(".").getAbsolutePath())+"/IndexFolder";
	private static int BSBIFileName=0;
	public static HashMap<String,  ArrayList<Integer>> BSBIPostingList = new HashMap<String, ArrayList<Integer>>();
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		readFile("BlockedSortBasedIndexing");

		File folder = new File(FolderPath);//Current Folder
		File[] listOfFiles = checkFileCount(folder.listFiles());

		while(listOfFiles.length>1)	
		{
			for(int i=0;i<listOfFiles.length;i=i)
			{
				createBSBI(listOfFiles[i]);
				listOfFiles[i].delete();
				i++;
				createBSBI(listOfFiles[i]);
				listOfFiles[i].delete();
				i++;
				//Clear Posting List
				File BSBIFile = new File((new File(".").getAbsolutePath())+"/IndexFolder"+"/a"+BSBIFileName);
				FileWriter fw = new FileWriter(BSBIFile.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				
				if (!BSBIFile.exists()) {
					BSBIFile.createNewFile();
				}
				Map<String, ArrayList<Integer>> map = new TreeMap<String, ArrayList<Integer>>(BSBIPostingList);//Unsorted hashmap convert to sorted treemap
				
				for (java.util.Map.Entry<String, ArrayList<Integer>> value : map.entrySet()) {
					Collections.sort(value.getValue());
				}
				
				for (java.util.Map.Entry<String, ArrayList<Integer>> value : map.entrySet()) {
					bw.write(value.getKey());
					bw.write(" ");
					for (int j:value.getValue()) {
						bw.write(j + ",");
					}
					bw.newLine();
				}
				bw.close();
				BSBIPostingList.clear();
				BSBIFileName++;
			}
			listOfFiles = checkFileCount(folder.listFiles());
		}
	}
	
	//Tek sayý oldugunda patlamamasý icin
	private static File[] checkFileCount(File[] listOfFiles) throws IOException
	{
		if(listOfFiles.length==1)
			return listOfFiles;
		else 
		{
			if(listOfFiles.length % 2 == 0)
				return listOfFiles;
			else
			{
				//BSBIFileName++;
				File BSBIFile = new File((new File(".").getAbsolutePath())+"/IndexFolder"+"/t"+BSBIFileName);
				FileWriter fw = new FileWriter(BSBIFile.getAbsoluteFile());
				
				if (!BSBIFile.exists()) {
					BSBIFile.createNewFile();
				}
				
				File folder = new File(FolderPath);//Current Folder
				return folder.listFiles();
			}
		}
	}
	
	private static void createBSBI(File file) throws IOException
	{
		//File folder = new File(FolderPath);//Current Folder
		//File[] listOfFiles = folder.listFiles();
		//File[] listOfFiles;
		//listOfFiles = files
		//for (File file : listOfFiles) {
		    if (file.isFile()) {
		    	 BufferedReader br = new BufferedReader(new FileReader(FolderPath+"/"+file.getName()));
		    	 try {
	    				String line = br.readLine();
	    	        	while (line != null) {
			    	     	//Split ""
			    	        //2. kýsým split ,
		    	            String [] Terms = line.split(" ");
		    	            for (String Term : Terms[1].split(",")) {
		    	            	if(!Term.equals(""))
		    	            	{
		    	            		addPosting(Terms[0],Integer.parseInt(Term));
		    	            	}
							}
		    	            line = br.readLine();
	    	        	}
		    	    }
		    	 	finally {
		    	        br.close();
		    	    }	 
		    }
		//}
		

	}
	
		private static void addPosting(String term,Integer fileCounter) {
			ArrayList<Integer> checkAvaible=BSBIPostingList.get(term);
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
			    		if(!((ArrayList<Integer>) BSBIPostingList.get(term)).contains(fileCounter))
			    			((ArrayList<Integer>) BSBIPostingList.get(term)).add(fileCounter);
		    	//	}
		    	//}
			}
	    	if(count==0 && !term.equals(""))
	    	{
	    		ArrayList<Integer> temp = new ArrayList<Integer>();
	    		temp.add(fileCounter);
	    		BSBIPostingList.put(term, temp);
	    	}
		}
	
	}

