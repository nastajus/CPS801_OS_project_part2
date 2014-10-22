import java.util.*;
import java.io.*;    //writing, reading
import java.io.File; //deleting


public class OS {
	
	//part 1:
	static final int MAX_SIZE_IN_BLOCKS = 40;
	static final int BYTES_PER_BLOCK = 512;
	static final int AMOUNT_CHARS = 10;
	
	static final String FILE_FILELIST = "filelist.txt";
	static final String FILE_FILEOP = "fileoperations.txt";
	
	static List<Fichier> fichiers;
	static List<Operation> operations = new ArrayList<Operation>();
	
	

	static void run(String programName, String[] args){
	    
    	if ( args.length < 1 || args.length > 3 ){
    		
    		print_how_to_use(programName);
    		
    	}
    	else if ( args.length == 1) {
	
        	if ( args[0].equals("create") ){
        		System.out.print("create!\n");
        		createSystem();
        		showSystem();
        		writeSystem();
        	}
        	else if ( args[0].equals("delete") ){
        		System.out.print("delete!\n");
        		deleteSystem();
        	}
        	else if ( args[0].equals("list") ){
        		System.out.print("show!\n");
        		if ( readSystem() ){
        			showSystem();
        		}
        	}
        	//part 2:
        	else if ( args[0].equals("allocate") ) {
        		System.out.print("allocate!\n");
        		if ( readSystem() ){
        			Disk disk = new Disk(); 

        			//toggle this
        			disk.activateShowCreationProcess();
        			
        			disk.Allocate(fichiers, operations);
        			disk.showBlocksWithContent();
        			
        			//toggle one of these
        			//disk.showBlocks();
        			//disk.showBlocksByType();
        			
        			//not functioning
        			//disk.DeleteFile("1");
        			
        		}        		
        	}
        	
        	//relatively simplistic, just clobbers to replace.
        	else if ( args[0].equals("op") ){
        		System.out.print("op!\n");
        		createOperations();
        		writeOperations();
        	}
    		
    	}
    	else if ( args.length == 2) {
    		
    		if ( args[0].equals("delete") ){
    			readSystem();
    			deleteFileInSystem(args[1]);
    			writeSystem();
    		}
    		else {
    			showSystem();
    		}
    		
    	}
    	else if ( args.length == 3 ) {
    		
			//find args[1]
			//change to args[2]
    		
    		if ( args[0].equals("rename") ){
    			
        		readSystem();
        		System.out.println ( "Seeking: " + args[1] );
        		
    			for (int i=0; i<Tester.SYSTEM_SIZE; i++){
    				if ( (fichiers.get(i).getFileNum()).equals( args[1] ) ){
    					System.out.print("FOUND! " + args[1] + "\n" );
    					fichiers.get(i).setFileNum( args[2] );
    					System.out.print("Successfully renamed.\n");
    				}
    			}
    			
    			writeSystem();
    			
    		}
    		
    		//use args[1] as file_num
			//use args[2] as byte_size
    		else if ( args[0].equals("create") ){
    			
    			readSystem();
    			
    			createFileInSystem( args[1], args[2] );
				
				writeSystem();
    			
    		}
    		
    		else {
    			showSystem();
    		}
    	}
		else {
			showSystem();
		}
    }

	
    static void createSystem(){
    	
    	fichiers = new ArrayList<Fichier>();
    	
    	for (int i=0; i < Tester.SYSTEM_SIZE; i++){
    		fichiers.add( Util.createRandomFile(i+1) );
    	}
    	System.out.print("Random file system created with size " + Tester.SYSTEM_SIZE + "\n");
    }
    
    static void showSystem(){
    	for (int i=0; i < fichiers.size(); i++){
    		System.out.print( fichiers.get(i).getDetails() );
    	}
    }
    

    static void writeSystem(){
   	
    	try{
    		
			// Create file 
			FileWriter fstream = new FileWriter(FILE_FILELIST);
			BufferedWriter out = new BufferedWriter(fstream);
	    	for (int i=0; i < fichiers.size(); i++){
	    		out.write(fichiers.get(i).getDetails() );
	    	}
			
			//Close the output stream
			out.close();
		}
    	catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
    	
    }
    
    static void deleteSystem(){
    	
    	try{
    		 
    		File file = new File(FILE_FILELIST);
 
    		if(file.delete()){
    			System.out.println(file.getName() + " is deleted!");
    		}else{
    			System.out.println("Delete operation is failed.");
    		}
 
    	}catch(Exception e){
 
    		e.printStackTrace();
 
    	}
    	
    	
    }
    
    
    static boolean readSystem(){
    	
    	boolean status;    	
		BufferedReader br = null;
		fichiers = new ArrayList<Fichier>();
		StringTokenizer st;
    	   	
		try {
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader(FILE_FILELIST));
 
			while ((sCurrentLine = br.readLine()) != null) {
				
				st = new StringTokenizer(sCurrentLine, ", ");
		        String file_num = st.nextToken();
		        String byte_size = st.nextToken();
		        String block_size = st.nextToken();
		        
		        fichiers.add( new Fichier( file_num, byte_size, block_size ) );
			}
			status = true;
 
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.print(FILE_FILELIST + " not found.\n");
			status = false;
			
		} finally {
			try {
				if (br != null)br.close();
				status = true; //guess, not much thought put here.
			} 
			catch (IOException ex) {
				ex.printStackTrace();
				status = false; 
			}
			
		}

		//System.out.println(" status is " + status);
    	return status;
    }

    
    static void print_how_to_use(String programName){
    	System.out.print("usage: \n \t" + programName + " create\n \t" + programName + " delete\n \t" + programName + " list\n \t" + programName + " list 1\n \t" + programName + " create 2 22\n \t" + programName + " rename 3 333\n \t" + programName + " delete 4\n\t" + programName + " allocate\n\n");
    }
    
    
    static void createFileInSystem(String file_num, String block_size){
    	
    	if ( Integer.parseInt(block_size) <= OS.MAX_SIZE_IN_BLOCKS ){ 

        	boolean brute_force_exists = false;

    		//check if exists already, if not proceed
    		for (int i=0; i<fichiers.size(); i++){
    			//System.out.println(fichiers.get(i).getFileNum());
    			if ( (fichiers.get(i).getFileNum()).equals( file_num ) ){
    				System.out.print("File_num exists, not added to system.\n");
    				brute_force_exists = true;
    			}
    		}
    		
    		if ( !brute_force_exists ) {
    			fichiers.add( new Fichier (file_num, block_size) );
    			System.out.print("File " + file_num + " of size " + block_size + " successfully created.\n");
    		}

    	}
    	else{
    		System.out.print( "Cannot create file, block size requested exceeds maximum of " + OS.MAX_SIZE_IN_BLOCKS + "\n" );
    	}			
    }
    
    static void deleteFileInSystem(String file_num){

    	boolean brute_force_found = false;

    	
		//check if exists
    	int i=0; 
		while (i<(fichiers.size()-1) && brute_force_found == false ){
	    	//has off-by-one error can't figure out. grr.
	    	//hacked by making not examine last file.
	    	//System.out.println( "h " + i + " " + fichiers.size() + " " + brute_force_found + " " + fichiers.get(i).getFileNum());
			i++;
			if ( (fichiers.get(i).getFileNum()).equals( file_num )  ){
				fichiers.remove(i);
				System.out.print(file_num + " removed.\n");
				brute_force_found = true;
			}
		}
		
		if ( !brute_force_found ) {
			System.out.print( file_num + " not found.\n");
		}

    
    }

    static void createOperations(){
    	
    	for (int i=0; i<Tester.OPERATION_AMOUNT; i++){
    		operations.add(  Util.createRandomOp() );
    	}
    	System.out.print( Tester.OPERATION_AMOUNT + " random operations created.\n");
    	
    }
	
    //no sophistication, just overwrites
    static void writeOperations(){
    	try{
    		
			// Create file 
			FileWriter fstream = new FileWriter(FILE_FILEOP);
			BufferedWriter out = new BufferedWriter(fstream);
	    	for (int i=0; i < operations.size(); i++){
	    		out.write(operations.get(i).getDetails() );
	    	}
			
			//Close the output stream
			out.close();
		}
    	catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		System.out.print("Operations file written.\n");

    	
    }
    
    
    /*
    static void test(){
        File file = new File("a", "b", "c");
        
        File[] files = { new File("a", "b", "c"), new File("d", "e", "f"), file };

        String thing1 = files[0].getFileDetails();
	    String thing2 = files[1].getFileDetails();
	    String thing3 = files[2].getFileDetails();
    }
    */
	
}
