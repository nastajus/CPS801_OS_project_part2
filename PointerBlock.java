import java.util.LinkedList;


public class PointerBlock extends Block {
	
	final static int MAX_ADDRESS_SIZE = 512 / 4;  //128
	static int pointer_blocks_used = 0;
	int pointer_block_number;
	
	static boolean fully_addressed = false;
	static int addressed_size; 
	
	static LinkedList<Integer> addresses = new LinkedList<Integer>();

	
	PointerBlock(int _inode_number){
		super();
		showCreationProcess( " new Pointer Block! @ " + block_number );
		
		block_number = _inode_number;
		//blocks_remaining_address--;
		
		this.pointer_block_number = PointerBlock.pointer_blocks_used;
		
		PointerBlock.pointer_blocks_used++;
		
	}
	
	
	boolean AddEntry(int _block_number_to_be, boolean _points_to_datum){
		showCreationProcess( "  new address! (@ "+ _block_number_to_be +")" );

		if ( _points_to_datum == true ) 
			blocks_remaining_address--;

		if ( addresses.size() == MAX_ADDRESS_SIZE) {
			//debugging
			//hiding known flaw, don't care
			//System.out.println ( "!!!!!!!!!!!!!!!!!!!!!too many, exiting" );
			return false;
		}
		
		//hack to hide bad code 
		//if ( addresses.size() == Disk.MAX_INODE_NUM_BLOCKS ){
		//	return false;
		//}		
		
		addresses.add( _block_number_to_be );		
		
		addressed_size++;

		return true;
			
	}	
	
	static int getAddressesUsed(){
		return addresses.size();
	}
	

	void showMeta(){		
		showCreationProcess( "Pointer Block #: " + this.pointer_block_number +", Block #: " + Integer.toString( getBlockNum() ) );
	}
	
	void showEntries(){
		
		int counter = 0;
		for (Integer aAddress : addresses) {
			counter++;
			showCreationProcess( " address " + counter + ": " + aAddress );
		}
				
	}
}
