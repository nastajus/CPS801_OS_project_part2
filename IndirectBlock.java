
public class IndirectBlock extends Block {
	
	final static int MAX_DATUM_SIZE = 512 / 4; //128
	static int indirect_blocks_used = 0;
	int indirect_block_number;

	String datum = "";
	
	IndirectBlock(String _datum){
		super();
		
		//the following increment was done to save time recoding, not as elegant as could be
		//in theory a real disk manager would write the entire block at once, not come back like this implies
		block_number++;
		
		showCreationProcess(" new Indirect Block! @ " + block_number);
		
		this.indirect_block_number = IndirectBlock.indirect_blocks_used;
		
		//limits datum size to 512 bytes
		datum = _datum.substring(0, OS.BYTES_PER_BLOCK);
		
		blocks_remaining_write--;
		blocks_written++;
		
		//duplicates protection against unbounded block growth, but is basic
		if ( indirect_blocks_used < IndirectBlock.MAX_DATUM_SIZE ) {
			IndirectBlock.indirect_blocks_used++;
		}		
	}
	
	String getDatum(){
		return datum;
	}
	
	void showMeta(){
		showCreationProcess( "Indirect Block #: " + this.indirect_block_number +", Block #: " + Integer.toString( getBlockNum() ) );
	}

	void showEntries(){
		//showCreationProcess( " Datum size: " + datum.length() + " bytes" );		
		//showCreationProcess( " Datum: " + datum );
	}

}
