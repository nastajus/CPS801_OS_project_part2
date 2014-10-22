import java.util.LinkedList;

//this Inode class follows UNIX V7 Inode as much as reasonable for this project
public class Inode extends Block {
	
	final static int MAX_ADDRESS_SIZE = 15 - 2; //13	removes double and triple indirect pointers
	static int inodes_used = 0;
	int inode_number;
	
	static boolean fully_addressed = false;
	static int addressed_size;
	
	static boolean addressing_done = false;
	static boolean fits_only;
	
	
	int size; //tracking only blocks for this assignment, since no tests for lower granularity of bytes exist
	//String location;
	
	
	String type; //direct, indirect, pointers
	
	//outside scope of implementation for this project:
	//String owner/group
	//String timestamps
	//String amount hardlinks

	
	//bonus representation beyond project request:
	public String data_field;
	
	
	LinkedList<Integer> addresses = new LinkedList<Integer>();
	
		
	
	
	
	Inode(int _inode_number, int _block_size){
		super();
		showCreationProcess( " new Inode! (of "+ _block_size +" blocks) @ " + _inode_number );
		
		this.inode_number = Inode.inodes_used;
		
		
		Inode.inodes_used++;
		
		size = _block_size;
		
		//very crucial line
		blocks_remaining_address = _block_size;
		blocks_remaining_write = _block_size;
		
		//capacity of 1-12 addresses
		if ( _block_size <= MAX_ADDRESS_SIZE - 1 ) {
			fits_only = true;
		}
		else 
			fits_only = false;
		
		
		blocks_written = 0;
		
		addressed_size = 0;
		
	}
	
	//attempt to store iterator reference directly, and mirror this method below as a way of fetching.
	boolean AddEntry(int _block_number_to_be, boolean _points_to_datum){
		
		String state_what_points_to;
		if ( !_points_to_datum ) state_what_points_to = " (to pointer block)"; else state_what_points_to = "";
		
		showCreationProcess( "  new address! (@ "+ _block_number_to_be +")" + state_what_points_to);

		if ( _points_to_datum == true )
			blocks_remaining_address--;
				
		addresses.add( _block_number_to_be );		
		
		if ( addresses.size() == Inode.MAX_ADDRESS_SIZE  ){
			fully_addressed = true;
			return true;
		}
		
		addressed_size++;

		return true;
			
	}
	
	void showMeta(){
		showCreationProcess( "Inode #: " + this.inode_number +", Block #: " + Integer.toString( getBlockNum() ) );	
	}
	
	
	void showEntries(){
		
		int counter = 0;
		for (Integer aAddress : addresses) {
			counter++;
			showCreationProcess( " address " + counter + ": " + aAddress );
		}
				
	}

		
		
	
//know i used these.
	
	static int getMaxSize(){
		return inodes_used;
	}	
	
	static int getRemainsAddress(){
		return blocks_remaining_address;
	}
	
	static int getRemainsWrite(){
		return blocks_remaining_write;
	}	
	
	//capability
	static boolean isFitsOnly(){
		return fits_only;
	}
	
	//current fullness
	static boolean isFull(){
		return fully_addressed;
	}
	
	//current 
	static int getWritten(){
		return blocks_written;
	}

	//current
	static int getAddressed(){
		return addressed_size;
	}
	
	
	
	
	
	
	
	int getInodeNum(){
		return this.inode_number;
	}	
	
	String getType(){
		return  "direct, indirect, pointers";
	}
	
	
	
	//single high level operation
	String traverse(String direction){

		//hardcoded to reverse only, inelegant due to time constraints
		//this is known to be the opposite direction the harddrive seeks and thus the worst efficiency. 
		/*
		for (int i=addresses.size(); i>0; i--){
			if ( addresses.size() == MAX_ADDRESS_SIZE ){
				//follow that pointer to it's pointer block, now traverse that
				
				//not used, insufficient time
				//int pointer_to_pointer_block = addresses.get( addresses.size()-1 );
				//Disk.blocks.get( pointer_to_pointer_block ) ........ //doesn't work
			}
		}
		*/
		return "";
	}	
	
	//lower level ops
	
	
	
	
	/*
	//bonus
	String getDatum(){
		return data_field;
	}
	*/	

}
