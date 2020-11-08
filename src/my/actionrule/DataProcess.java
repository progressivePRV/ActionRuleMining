import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.lang.*;

public class DataProcess {
	
	public static List<String> attributeNames;
	public static ArrayList<ArrayList<String>> data;
	public static Map<String, ArrayList<String>> attributeValues;
	public static Map<String, HashSet<String>> distinctAttributeValues;

	public static Map<String, String> modeAttributeValues;
  public static Map<String, ArrayList<ArrayList<String>>> missingAttributeValues;
  public static Map<String, Integer> attributeIndex;
	

	public DataProcess() throws Exception{

		attributeNames = new ArrayList<String>();
		data = new ArrayList<ArrayList<String>>();
		attributeValues = new HashMap<String, ArrayList<String>>();
		distinctAttributeValues = new HashMap<String, HashSet<String>>();

		modeAttributeValues = new HashMap<String, String>();
    missingAttributeValues = new HashMap<String, ArrayList<ArrayList<String>>>();
    attributeIndex = new HashMap<String, Integer>();
		
	}
	
	
	//Required getters
	public List<String> getAttributes() {
		return attributeNames;
	}
	
	public static ArrayList<ArrayList<String>> getData() {
		return data;
	}
	
	public static HashSet<String> getDistinctAttributeValues(String attributeName) {
		return distinctAttributeValues.get(attributeName);
	}
	
	public static	Map<String, ArrayList<String>> getAllAttributeValues(){
		return attributeValues;
	}
	

	public void processFiles(String attributesFile, String dataFile, String splitChar) {
		readAttributes(attributesFile);
		readData(dataFile, splitChar);		
		
		// Comment follwing function to ignore missing values
		// To replace the missing attributes with mode
		
		populateAttributeValuesMode();
		replaceMissingAttribute();
		
		
		
	}
	
	/**
	  * Processing  attributes file
		* @params file : Attributes file path
	 */
	private static void readAttributes(String attributesFilePath) {
		try {
			Scanner input = new Scanner(new File(attributesFilePath));
			int index = 0;
			while (input.hasNext()) {
				String attributeName = input.next(); 				
				attributeNames.add(attributeName);	
				
				// required to replace missing data
				attributeIndex.put(attributeName, index);
				index ++;
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("Exception Occurred:"+ attributesFilePath+ "File not found");
			e.printStackTrace();
		}
			
	}
	
	/**
	  * Processing data file
		* @params dataFilePath : Absolute path of data file
		* @params splittingCharacter: Character to split the data. Can be "\t" | ",".
  	*/		 									
	private static void readData(String dataFilePath, String splittingCharacter) {
		try {
				
				int lineNo = 0;			
				Scanner input = new Scanner(new File(dataFilePath));	
			
				while(input.hasNextLine()) {
					String[] line = input.nextLine().split(splittingCharacter);
					ArrayList<String> lineData = new ArrayList<String>(Arrays.asList(line));
				
					// If no missing data add to dataset
					if(!checkEmptyValueInStringArray(lineData)){
					
						ArrayList<String> tempList = new ArrayList<String>();
							
						for (int i=0; i<lineData.size(); i++) {
							String currentAttributeValue = lineData.get(i);
							String attributeName = attributeNames.get(i);
						
						  // Adding attribute values for processing mode
							setAttributeValues(attributeName, currentAttributeValue);						
							
							// Populating distinct attribute vaues descion attribute
							setDistinctAttributeValues(attributeName, currentAttributeValue);
												
							tempList.add(currentAttributeValue);
						}	
						
						data.add(tempList);					
						//dataLineMap.put(lineNo, tempList);
						lineNo ++;
					
					} else {
					
					  // continue;
						ArrayList<ArrayList<String>> tempList;
						String attributeName = "";	
						for (int i=0; i<lineData.size(); i++) {
							String currentAttributeValue = lineData.get(i);
							attributeName = attributeNames.get(i);
						  if (currentAttributeValue.equals("?") || currentAttributeValue.equals(" ")) {
						  								  		
						  		if (missingAttributeValues.containsKey(attributeName)) {
						  			tempList = missingAttributeValues.get(attributeNames);
						  		} else {
						  			tempList = new ArrayList<ArrayList<String>>();
						  		}
						  		tempList.add(lineData);
						  		missingAttributeValues.put(attributeName, tempList);
						  } else {
						  	// Adding attribute values for processing mode
								setAttributeValues(attributeName, currentAttributeValue);	
								
							// Populating distinct attribute vaues descion attribute
							setDistinctAttributeValues(attributeName, currentAttributeValue);
						  }
						}	
						//dataLineMap.put(lineNo, tempList);
						lineNo ++;
					}
				}		
		} catch (FileNotFoundException e) {
			System.out.println("Exception occurred :File not found");
			e.printStackTrace();
		}
	}
	

	//Checks if string array contains empty value
	private static boolean checkEmptyValueInStringArray(ArrayList<String> lineData) {
		return lineData.contains(" ") || lineData.contains("?");
	}
	
	
	// Set Attribute values
	public static void  setAttributeValues(String attributeName, String attributeValue) {
				
  			ArrayList<String> valuesList;
				if (attributeValues.containsKey(attributeName)) {
					valuesList = attributeValues.get(attributeName);
	
				} else {
					valuesList = new ArrayList<String>();						
				}
				valuesList.add(attributeValue);						
				attributeValues.put(attributeName, valuesList);
	
	}
	
	//Sets Distinct Attribute values
	private static void setDistinctAttributeValues(String attributeName, String atributeValue)
	{
		HashSet<String> set;
		if (distinctAttributeValues.containsKey(attributeName)) {
				set = distinctAttributeValues.get(attributeName);
							
		}else{
				set = new HashSet<String>();
		}
		set.add(atributeValue);
		distinctAttributeValues.put(attributeName, set);	
	}
	
	private static void populateAttributeValuesMode()
	{
		String attributeName;
		ArrayList<String> valuesList;
		for (Map.Entry<String, ArrayList<String>> entry : attributeValues.entrySet()){
			attributeName = entry.getKey();
			valuesList = entry.getValue();
			Integer maximum = 0;
			String modeAttribute = "";
			HashMap<String, Integer> mode = new HashMap<String, Integer>();
			for( String value : valuesList) {
				Integer frequency = mode.get(value);
				frequency = (frequency == null) ? 1 : frequency + 1;
				mode.put(value, frequency);
				if (maximum < frequency) {
					maximum = frequency;
					modeAttribute = value;
					}
			}
			modeAttributeValues.put(attributeName, modeAttribute);	
		}
	}
	
	private static void replaceMissingAttribute() {
			String attributeName;
			ArrayList<ArrayList<String>> valuesList;
			for (Map.Entry<String, ArrayList<ArrayList<String>>> entry : missingAttributeValues.entrySet()){
				attributeName = entry.getKey();
				valuesList = entry.getValue();
				int index = (int) attributeIndex.get(attributeName);
				String modeValue = modeAttributeValues.get(attributeName);
				ArrayList<String> existingAttributeValues = attributeValues.get(attributeName);
				for(ArrayList<String> values : valuesList) {
			  	values.set(index, modeValue);
			  	
			  	// Add replaced data to complete data set
			  	data.add(values);
			  	
			  	// Update attribute values map
			  	existingAttributeValues.add(modeValue);
			  	
			  }
			  attributeValues.put(attributeName, existingAttributeValues);			  
			}
	}
	

	public static void main(String args[]) throws Exception{
	
	DataProcess dp = new DataProcess();
	String path = "/home/ubuntu/ActionRuleMining/input/";
	String attributesFile = path +"TestAttribute.txt";
	String dataFile =  path +"TestData.txt";
	//attributesFile = path +"carAttributes.txt";
	//dataFile = path +"carAttributes.txt";
	
	//attributesFile = path +"mammographic_massesAttributes.txt";
	//dataFile = path +"mammographic_massesData.txt";
	
		
	dp.processFiles(attributesFile, dataFile, "\t");
	List<String> attributeNames = dp.getAttributes();
	
	System.out.println("----- Attribute Names ---------");
	for (String attribute : attributeNames) {
		System.out.print(attribute+" ");
	}
	
	System.out.println("\n----- Data Set after replacing missing values with mode ---------");
	ArrayList<ArrayList<String>> data =	dp.getData();
	for (ArrayList<String> line : data) {
			for (String val: line)
				System.out.print(val+" ");
			System.out.println("");
	}
	
	System.out.println("\n----- Print Distinct values for given attribute ---------");
	
	for (String attribute : attributeNames) {
	    System.out.print("\n"+attribute+" ");
	    HashSet<String> distinct = dp.getDistinctAttributeValues(attribute);
	    for (String value: distinct) {
		System.out.print(value+" ");
	}
	}
	
	
	System.out.println("\n----- Print values per attribute ---------");
	Map<String, ArrayList<String>> attValues = dp.getAllAttributeValues();
	for (Map.Entry<String, ArrayList<String>> entry : attValues.entrySet()) {
			System.out.print("\n"+entry.getKey()+" -->");
			ArrayList<String> values = entry.getValue();
			for (String val: values)
					System.out.print(val+", ");
	}
		
}
}
