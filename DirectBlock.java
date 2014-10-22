
public class DirectBlock extends Block {
	
	final static int MAX_DATUM_SIZE = 512 / 4; //128
	static int direct_blocks_used = 0;
	int direct_block_number;
			
	String datum = "";
	
	DirectBlock (String _datum) {
		super();
		
		showCreationProcess(" new Direct Block! @ " + block_number);
		
		this.direct_block_number = DirectBlock.direct_blocks_used;
		
		//limits datum size to 512 bytes
		datum = _datum.substring(0, OS.BYTES_PER_BLOCK);
		
		blocks_remaining_write--;
		blocks_written++;

		//duplicates protection against unbounded block growth, but is basic
		if ( direct_blocks_used < DirectBlock.MAX_DATUM_SIZE ) {
			DirectBlock.direct_blocks_used++;
		}		
		
	}
	
	String getDatum(){
		return datum;
	}

	void showMeta(){
		showCreationProcess( "Direct Block #: " + this.direct_block_number +", Block #: " + Integer.toString( getBlockNum() ) );
	}
	
	void showEntries(){
		//showCreationProcess( " Datum size: " + datum.length() + " bytes" );		
		//showCreationProcess( " Datum: " + datum );
	}

}
