package ccfindertools;



import java.io.BufferedReader;

import java.io.InputStreamReader;

import java.io.IOException;

import java.io.File;

import java.io.PrintStream;

import java.lang.InterruptedException;

import java.lang.Process;

import java.lang.Runtime;

import java.lang.SecurityException;

import java.lang.String;



public class CCFinder{



    static final String FILE_INTERIOR = "-cw-";

    static final String CROSS_FILES = "-cf-";

    static final String CROSS_GROUPS = "-cg-";



    private String language;

    private String memorySize;

    private String minimumLength;

    private File inputFile;

    private String tmpPairFile;

    private String tmpClassFile;

    private boolean isFileInterior;

    private boolean isCrossFiles;

    private boolean isCrossGroups;

    private File outputFile;

    private PrintStream out;



    //constructer

    public CCFinder(String language,String memorySize,String minimumLength,

		    File inputFile,File outputFile,

		    boolean isFileInterior,boolean isCrossFiles,boolean isCrossGroups,

		    PrintStream out){



	this.language = new String(language);

	this.memorySize = new String(memorySize);

	this.minimumLength = new String(minimumLength);

	this.inputFile = inputFile;

	this.tmpPairFile = null;

	this.tmpClassFile = null;

	this.outputFile = outputFile;

	this.isFileInterior = isFileInterior;

	this.isCrossFiles = isCrossFiles;

	this.isCrossGroups = isCrossGroups;

	this.out = out;

    }



    public int run(){



	this.tmpPairFile = this.inputFile.getName() + ".pair";

	this.tmpClassFile = this.inputFile.getName() + ".cls";



	int returnValue = this.ExecutePairMode();

	if( returnValue != 0 ) return returnValue;



	return this.ExecuteClassMode();

    }

    

    private int ExecutePairMode(){



	try{



	    //initialize variables

	    String command = this.makePairCommand();
	    System.out.println(command);

	    File workDirectory = new File(this.inputFile.getParent());



	    //command = new String("ccfinder JAVA -hr");

	    Process process = Runtime.getRuntime().exec(command,null,workDirectory);



	    //check ccfinder process

	    if( process == null ){

		System.err.println("Can not execute ccfinder");

		return 1;

	    }



	    //get standart error

	    BufferedReader err = 

		new BufferedReader(new InputStreamReader(process.getErrorStream()));



	    //output standard error

	    String line_std_err;

	    while( (line_std_err = err.readLine()) != null )

		out.println(line_std_err);

	    

	    //get standard output

	    BufferedReader in =	

		new BufferedReader(new InputStreamReader(process.getInputStream()));



	    //output standard output

	    String line_std_in;

	    while( (line_std_in = in.readLine()) != null )

		out.println(line_std_in);

	    

	    //wait ccfinder process finish

	    process.waitFor();



	    return 0;



	}catch(CCFinderCommandException e){

	    System.err.println(e.getMessage());

	    return 1;

	}catch(IOException e){

	    System.err.println(e.getMessage());

	    return 1;

	}catch(InterruptedException e){

	    System.err.println(e.getMessage());

	    return 1;

	}catch(SecurityException e){

	    System.err.println(e.getMessage());

	    return 1;

	}

	

    }



    private int ExecuteClassMode(){



	try{



	    //initialize variables

	    String command = this.makeClassCommand();

	    File workDirectory = new File(this.inputFile.getParent());
	   

	    Process process = Runtime.getRuntime().exec(command,null,workDirectory);

	  

	    //check ccfinder process

	    if( process == null ){

		System.err.println("Can not execute ccfinder");

		return 1;

	    }

	    

	    //get standart error

	    BufferedReader err = 

		new BufferedReader(new InputStreamReader(process.getErrorStream()));



	    //output standard error

	    String line_std_err;

	    while( (line_std_err = err.readLine()) != null )

		this.out.println(line_std_err);

	    

	    //get standard output

	    BufferedReader in =	

		new BufferedReader(new InputStreamReader(process.getInputStream()));



	    //output standard output

	    String line_std_in;

	    while( (line_std_in = in.readLine()) != null )

		this.out.println(line_std_in);

	    

	    //wait ccfinder process finish

	    process.waitFor();

	    

	    return 0;



	}catch(CCFinderCommandException e){

	    System.err.println(e.getMessage());

	    return 1;

	}catch(IOException e){

	    System.err.println(e.getMessage());

	    return 1;

	}catch(InterruptedException e){

	    System.err.println(e.getMessage());

	    return 1;

	}catch(SecurityException e){

	    System.err.println(e.getMessage());

	    return 1;

	}

    }

    



    //this method make command

    private String makePairCommand() throws CCFinderCommandException{



	//error check

	if( this.memorySize == null )

	    throw new CCFinderCommandException("Target Language Unselected.");

	if( this.memorySize == null )

	    throw new CCFinderCommandException("Memory Size not assigned.");

	if( this.minimumLength == null )

	    throw new CCFinderCommandException("Minimum Length not assigned.");

	if( this.inputFile == null )

	    throw new CCFinderCommandException("Input File not assigned.");



	//make command

	String command = new String("ccfinder ");

	command += this.language;

	//command += " -ra-b-d-f-i-k-m-n-p-s-u-v- -m512M ";
	command += " -rb-p-u-v- -m512M ";

	//command += this.memorySize;

	//command += " ";

	//command += this.minimumLength;

	//command += " ";



	if( this.isFileInterior == false ){

	    command += FILE_INTERIOR;

	    command += " ";

	}



	if( this.isCrossFiles == false ){

	    command += CROSS_FILES;

	    command += " ";

	}



	if( this.isCrossGroups == false ){

	    command += CROSS_GROUPS;

	    command += " ";

	}



	//command += "-v2g ";

	command += "-i ";

	command += this.inputFile.getAbsolutePath();

	command += " ";

	command += "-o ";

	command += this.tmpPairFile;



	return command;

    }



    private String makeClassCommand() throws CCFinderCommandException{



	//error check

    	if( this.outputFile == null )

	    throw new CCFinderCommandException("Output File not assigned.");



	//make command

	String command = new String("ccreformer ");

	command += "class ";

	command += this.tmpPairFile;

	command += " ";

	command += "-v2 ";

	command += "-o ";

	command += this.outputFile.getAbsolutePath();


	
	return command;

    }

}

