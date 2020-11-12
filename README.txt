Action Rule Mining Project - Group 5 

- This project generates action rules for datasets. 
- Installations for project:
This project will run only on Netbeans. 
For installation of Netbeans, download Netbeans from this link : https://netbeans.org/downloads/8.2/rc/ (You can download the bundle having all features)
Step 1 -> 
	Install Netbeans
Step 2 ->
	Open Project: Project can be opened in 2 ways in netbeans:
	1.
		- Open Netbeans. 
		- Click on File -> Open Project.
		- Here navigate to where project is saved.
		- Select the ActionRuleMining project and give the project a desired name and finally, click on Open Project in the window.
		
	2. 
		-Open Netbeans 
		- Click File -> New Project
		- A New Project window will appear. 
		- In this window, select 'Java' option in Categories pane and 'Java Project with Existing Sources' in Projects pane.
		- Click Next
		- Give the Project a name, and assign a folder for the project.
		- Click Next
		- Now, in Source Package Folders, add the ActionRuleMining project folder.
		- Finally click on Finish

	Now the netbeans project will open in the netbeans window. 
Step 3 ->
	Run the project:
		- Once, the project is open in netbeans, navigate to my.actionrule package in Source Packages
		- Locate the ActionRuleUI.java file in here and right click on the file and select 'Run File' option.
		- This will open the the Action Rule UI.
Step 4 ->
	Executing the project:
		1. Select appropriate attribute files and data files:
			- The ActionRuleMining project has an 'input' folder which consists of various datasets you can test with
			- When choosing data and attribute files from UI, navigate to the ActionRuleMining project and open the input folder.
			- Open any desirable subfolder from the input folder. Each folder will have a <data_file_name>.txt file and 
			<attributes_file_name>.txt file. Select the files accordingly.
			- Select the delimeter as observer in the <data_file_name>.txt file and Load the data
			- All attributes will load. 
			- Select a decision attribute and the From and To values of the decision attribute
			- Enter a support greather than or equal to 0 and a confidence within the range of 0 to 100 and get the remaining attributes
			- Select a list of stable attributes (optional) and Generate action Rules.
			- Here the action rules will take some time to execute. You can observe the execution of the Code (generating LERS rules and action rules ) in the output window in Netbeans.
			- Once the code execution is complete, the binning values, the LERS rules and the Action Rules will be visible on the result pane of the UI.
			- This data will also get saved in a output.txt file. 
			- So that users can locate this file, the absolute path of this file will be seen printed at the bottom the result pane.
			- Navigate to that path to view the output.txt file.

Some explanations about the project:
	1. This code replaces missing values using 'mode' strategy in the dataset.
	2. This code also does discretization of numeric values using 'mode binning' strategy:
		- In order for the code to understand which value is a continuous value, in the uploaded <attributes_file_name>.txt file, after listing all the attributes, a line like the following should be present:  'NUMERIC:<attribute_name>'. 
		- This code will handle only one numeric attribute (Dataset can have only one numeric value). 
		- You can refer the mammographic_massesAttributes.txt file in input/MamographicData folder to see the correct format that will work.
	3. Format of the data Files:
		- The data file should be of the exact format as shared in the ActionRuleMining Project folder.
		- The inputs folder has 3 folder, namely: CarData, MamographicData and TestData. 
		- Please refer to the <data_file_name>.txt file and <attributes_file_name>.txt file inside each folder to see the format of the files to be uploaded.
		- Program will accept only .txt files having the required format. Other data will be rejected. 
	4. Generation of action rules will take some time. The progress of the code can be monitored in the output window of Netbeans. 


NOTE: The video will demo opening the project in netbeans and running it. It will also explain file formats and output file destination. 

		


