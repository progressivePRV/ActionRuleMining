package my.actionrule;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class DataProcess {
	
	public String continuousAttribute;
	public List<String> attributeNames;
	public ArrayList<ArrayList<String>> data;
	public Map<String, ArrayList<String>> attributeValues;
	public Map<String, HashSet<String>> distinctAttributeValues;

	public Map<String, String> modeAttributeValues;
	public ArrayList<ArrayList<String>> missingAttributeValues;
	public Map<String, Integer> attributeIndex;
	public Map<String, Integer> binMap;
	
	public DataProcess() throws Exception {

		attributeNames = new ArrayList<String>();
		data = new ArrayList<ArrayList<String>>();
		attributeValues = new HashMap<String, ArrayList<String>>();
		distinctAttributeValues = new HashMap<String, HashSet<String>>();

		modeAttributeValues = new HashMap<String, String>();
		missingAttributeValues = new ArrayList<ArrayList<String>>();
		attributeIndex = new HashMap<String, Integer>();
		binMap = new HashMap<String, Integer>();
	}

	// Required getters
	public List<String> getAttributes() {
		return attributeNames;
	}

	public ArrayList<ArrayList<String>> getData() {
		return data;
	}

	public HashSet<String> getDistinctAttributeValues(String attributeName) {
		return distinctAttributeValues.get(attributeName);
	}

	public Map<String, ArrayList<String>> getAllAttributeValues() {
		return attributeValues;
	}
	
	public String getContinuousAttribute() {
		return continuousAttribute;
	}

	public void setContinuousAttribute(String attribute) {
		continuousAttribute = attribute;
	}
	
	public Map<String, Integer> getBinMap() {
		return binMap;
	}

	public void setBinMap(Map<String, Integer> binMap) {
		this.binMap = binMap;
	}

	public void processFiles(String attributesFile, String dataFile, String splitChar) {
		try {
			readAttributes(attributesFile);
			readData(dataFile, splitChar);

			// Comment following function to ignore missing values
			// To replace the missing attributes with mode

			populateAttributeValuesMode();
			replaceMissingAttribute();
			
			if(getContinuousAttribute() != null)
				createBin(getContinuousAttribute());

		}catch(Exception e) {
			System.out.println("Exception occured while processing. Please check the files uploaded");
		}
	}

	/**
	 * Processing attributes file
	 * 
	 * @param file : Attributes file path
	 */
	private void readAttributes(String attributesFilePath) {
		try {
			Scanner input = new Scanner(new File(attributesFilePath));
			int index = 0;
			while (input.hasNextLine()) {
				String attributeName = input.nextLine();
				
				if (attributeName.contains("NUMERIC:")) {
					setContinuousAttribute(attributeName.split(":")[1].strip());
					continue;
				}
				if(attributeName.contains("/t") || attributeName.contains(",")) {
					System.out.println("Please upload proper attribute.txt file without tab or comma");
					return ;
				}
				attributeNames.add(attributeName);

				// required to replace missing data
				attributeIndex.put(attributeName, index);
				index++;
			}

		} catch (FileNotFoundException e) {
			System.out.println("Exception Occurred:" + attributesFilePath + "File not found");
			e.printStackTrace();
		}

	}

	/**
	 * Processing data file
	 * 
	 * @param dataFilePath        : Absolute path of data file
	 * @param splittingCharacter: Character to split the data. Can be "\t" | ",".
	 */
	private void readData(String dataFilePath, String splittingCharacter) {
		try {

			int lineNo = 0;
			Scanner input = new Scanner(new File(dataFilePath));

			while (input.hasNextLine()) {
				String[] line = input.nextLine().split(splittingCharacter);
				ArrayList<String> lineData = new ArrayList<String>(Arrays.asList(line));
				if (lineData.size() != attributeNames.size()) {
					System.out.println("PLease upload data.txt file with exact values for all attributes");
					return;
				}
				// If no missing data add to data set
				if (!checkEmptyValueInStringArray(lineData)) {

					for (int i = 0; i < lineData.size(); i++) {
						String currentAttributeValue = lineData.get(i);
						String attributeName = attributeNames.get(i);

						// Adding attribute values for processing mode
						setAttributeValues(attributeName, currentAttributeValue);

						// Populating distinct attribute values descion attribute
						setDistinctAttributeValues(attributeName, currentAttributeValue);

					}
					data.add(lineData);
					// dataLineMap.put(lineNo, tempList);
					lineNo++;

				} else {
					// continue;
					String attributeName = "";
					Boolean addToMissingList = false;
					for (int i = 0; i < lineData.size(); i++) {
						String currentAttributeValue = lineData.get(i);
						attributeName = attributeNames.get(i);
						if (!addToMissingList
								&& (currentAttributeValue.equals("?") || currentAttributeValue.equals(" "))) {
							missingAttributeValues.add(lineData);
							addToMissingList = true;
						} else if (!currentAttributeValue.equals("?") && !currentAttributeValue.equals(" ")) {
							// Populating distinct attribute values descion attribute
							setDistinctAttributeValues(attributeName, currentAttributeValue);

							// Adding attribute values for processing mode
							setAttributeValues(attributeName, currentAttributeValue);
						}
					}
					// dataLineMap.put(lineNo, tempList);
					lineNo++;
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Exception occurred :File not found");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Exception occurred :" + e);
			e.printStackTrace();
		}
	}

	// Checks if string array contains empty value
	private boolean checkEmptyValueInStringArray(ArrayList<String> lineData) {
		return lineData.contains(" ") || lineData.contains("?");
	}

	// Set Attribute values
	public void setAttributeValues(String attributeName, String attributeValue) {

		ArrayList<String> valuesList;
		if (attributeValues.containsKey(attributeName)) {
			valuesList = attributeValues.get(attributeName);

		} else {
			valuesList = new ArrayList<String>();
		}
		valuesList.add(attributeValue);
		attributeValues.put(attributeName, valuesList);

	}

	// Sets Distinct Attribute values
	private void setDistinctAttributeValues(String attributeName, String atributeValue) {
		HashSet<String> set;
		if (distinctAttributeValues.containsKey(attributeName)) {
			set = distinctAttributeValues.get(attributeName);

		} else {
			set = new HashSet<String>();
		}
		set.add(atributeValue);
		distinctAttributeValues.put(attributeName, set);
	}

	private void populateAttributeValuesMode() {
		String attributeName;
		ArrayList<String> valuesList;
		for (Map.Entry<String, ArrayList<String>> entry : attributeValues.entrySet()) {
			attributeName = entry.getKey();
			valuesList = entry.getValue();
			Integer maximum = 0;
			String modeAttribute = "";
			HashMap<String, Integer> mode = new HashMap<String, Integer>();
			for (String value : valuesList) {
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

	private void replaceMissingAttribute() {
		ArrayList<String> existingAttributeValues;
		for (ArrayList<String> linedata : missingAttributeValues) {
			int index = 0;
			for (String value : linedata) {
				if (value.equals("?") || value.equals(" ")) {
					String attribute = attributeNames.get(index);
					String modeValue = modeAttributeValues.get(attribute);
					linedata.set(index, modeValue);

					// Update attribute values map
					existingAttributeValues = attributeValues.get(attribute);
					existingAttributeValues.add(modeValue);
					attributeValues.put(attribute, existingAttributeValues);
				}
				index++;
			}
			// Add replaced data to complete data set
			data.add(linedata);
		}
	}
	
	// create bins for continuous attribute
	public void createBin(String continuousAttributeName) {
		
		ArrayList<String> valuesList = attributeValues.get(continuousAttributeName);
		HashMap<String, Integer> mode = new HashMap<String, Integer>();
		for (String val : valuesList) {
			if (mode.containsKey(val))
				mode.put(val,  mode.get(val)+1);
			else {
				mode.put(val, 1);
			}	
//			System.out.println(val+": "+mode.get(val));
		}
		ArrayList<Integer> values = new ArrayList<Integer>(mode.values());
		HashSet<Integer> val = new HashSet<Integer>(values);
		ArrayList<Integer> binVals = new ArrayList<Integer>(val);

		Collections.sort(binVals);

		for (Map.Entry<String, Integer> entry : mode.entrySet()) {
			binMap.put(entry.getKey(), (binVals.indexOf(entry.getValue()))+1);
		}	
		// Print bin values
//		for (Map.Entry<String, Integer> entry : binMap.entrySet()) {
//			System.out.println(entry.getKey()+":"+entry.getValue());
//		}		
		
		ArrayList<String> newAttributeValues = new ArrayList<String>();
		HashSet<String> newDistinctValues = new HashSet<String>();
		// Replace data in data set with bin values
		for (ArrayList<String> linedata : data) {
			int index = (int) attributeNames.indexOf(continuousAttributeName);
			String val1 = linedata.get(index);
			String newBinValue = binMap.get(val1).toString();
			linedata.set(index, newBinValue);
			newAttributeValues.add(newBinValue);
		}
		
		newDistinctValues = new HashSet<String>(newAttributeValues);
		attributeValues.put(continuousAttributeName, newAttributeValues);
		distinctAttributeValues.put(continuousAttributeName, newDistinctValues);
		
	}

	public static void main(String args[]) throws Exception {

		DataProcess dp = new DataProcess();
		String path = "/home/ubuntu/ActionRuleMining/input/";
		String attributesFile = path + "TestAttribute.txt";
		String dataFile = path + "TestData.txt";
		//attributesFile = path + "carAttributes.txt";
		//dataFile = path + "carData.txt";

		attributesFile = path + "mammographic_massesAttributes.txt";
		dataFile = path + "mammographic_massesData.txt";

		dp.processFiles(attributesFile, dataFile, ",");
		List<String> attributeNames = dp.getAttributes();

		System.out.println("----- Attribute Namess ---------");
		for (String attribute : attributeNames) {
			System.out.print(attribute + ", ");
		}

		System.out.println("\n----- Data Set after replacing missing values with mode ---------");
		ArrayList<ArrayList<String>> data = dp.getData();
		for (ArrayList<String> line : data) {
			 for (String val : line)
			 System.out.print(val + " ");
			 System.out.println("");
		}

		System.out.println("\n----- Print Distinct values for given attribute ---------");

		for (String attribute : attributeNames) {
			System.out.print("\n" + attribute + " ");
			HashSet<String> distinct = dp.getDistinctAttributeValues(attribute);
			for (String value : distinct) {
				System.out.print(value + " ");
			}
		}

		System.out.println("\n----- Print values per attribute ---------");
		Map<String, ArrayList<String>> attValues = dp.getAllAttributeValues();
		for (Map.Entry<String, ArrayList<String>> entry : attValues.entrySet()) {
			System.out.print("\n" + entry.getKey() + " -->");
			ArrayList<String> values = entry.getValue();
			// for (String val : values)
			// System.out.print(val + ", ");
		}
		System.out.println("\n Print values :      bin values");
		for (Map.Entry<String, Integer> entry : dp.getBinMap().entrySet()) {
			System.out.println(entry.getKey()+":"+entry.getValue());
		}		

	}
}
