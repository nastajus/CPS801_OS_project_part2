//another assumption made, or rather difference between this Java app and a real world OS implementation, is:
//this Block class contains some logic, the part that generates block numbers
//whereas a real block is purely just data of some type
public class Block {

	//logic
	int max_size_bytes = OS.BYTES_PER_BLOCK;
	static int blocks_used = 0;
	int block_number = 0;
	
	static int blocks_remaining_address;
	static int blocks_remaining_write;
	static int blocks_written;
	
	static boolean showCreationProcess; 
	
	
	
	Block (){
		this.block_number = Block.blocks_used;
		
		//basic but incomplete protection against unbounded block growth.
		if ( blocks_used < Disk.MAX_DISK_NUM_BLOCKS ) {
			Block.blocks_used++;
		}
		
	}
	
	int getBlockNum(){
		return this.block_number; 
	}
	
	//empty on purpose, just need to compile generic method in parent, all children implement this
	void showMeta(){		
	}
	
	//empty on purpose, just need to compile generic method in parent, all children implement this
	void showEntries(){	
	}
	
	/*
	void Traverse(){
		
	}
	*/
	
	static int getBlocksUsed(){
		return blocks_used;
	}

	static void showCreationProcess(String printme){
		
		if ( showCreationProcess == true ){
			System.out.println (printme);
		}
		
	}
	
}
