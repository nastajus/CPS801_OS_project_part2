import java.util.*;

//Disk Manager pseudo logic represented in Java
public class Disk {

	
	//part 2:
	
	/*
	 *   Logic abstracted out of the Disk class, because it's too complex to attempt to represent inside Disk representation
	 */
	
	//adjust these final static variables accordingly
	final static int MAX_DISK_NUM_BLOCKS = 400;
	final static int MAX_INODE_NUM_BLOCKS = 40; //40 results in max support of 40 files.		
	final static int DATUM_SIZE_BYTES = 4; 
	final static int POINTER_SIZE_BYTES = 4; 
	
	static int CURRENT_DISK_NUM_BLOCKS = 0; 	//this logic could've been stored in a super block.
	static int CURRENT_ROOT_DIR_BLOCKS = 0;
	static int CURRENT_INODE_BLOCKS = 0;
	//do i # direct, indirect, and pointer blocks? meh, presume not.
	
	
	
	
	//// SOME KIND OF LIST OF ROOT DIR BLOCKS
	
		//// DERP SOME KIND OF LIST TRAVERSAL DUDE!!!
	
	
	//some assumptions:
	//the following collection of lists is where some liberty has been taken with the logic implementation
	//the basic assumption is this "Disk manager" class handles anything that's difficult to implement
	//crucially, none of this Java code is represented inside this conceptual disk representation.
	
	static List<Block> blocks = new LinkedList<Block>();
	
	static List<Inode> inodes = new LinkedList<Inode>();

	List<RootDir> root_dir_blocks = new LinkedList<RootDir>();
	
	List <DirectBlock> direct_blocks = new LinkedList<DirectBlock>();
	
	List <IndirectBlock> indirect_blocks = new LinkedList<IndirectBlock>();

	List <PointerBlock> pointer_blocks = new LinkedList<PointerBlock>();
	
	
	
	
	//for files 1-12 blocks, 1 additional block is consumed for the inode
	//for files 13-40 blocks, 2 additional blocks are consumed, 1 for the inode, another for pointer block
	
	//collectively, each file consumes 1/32th of the root directory structure, which also must exist in a block.
	//worst case
	//best case
	
	//despite the image on page 274 of the Tanenbaum course textbook which shows Root dir as a distinctly separate section,
	//no statement could be found that the root directory structure distinctly limits the amount of files on a file system, 
	//unlike inode structure which clearly does result in distinctly separate limits.  
	//thus it is presumed to be a flexible architecture, and the logic is implemented as such, as scalable.

	
	
	
	//side-effect, not elegant code
	RootDir activeRootDir = null;
	

	
	
	Disk () {
		//format disk to initial state of all zeros
		CURRENT_DISK_NUM_BLOCKS = MAX_DISK_NUM_BLOCKS;
		//perform subtractions
		
		//calculate disk to be X'th size.
		
		
		
		
		
	}
	
	
	boolean Allocate(List<Fichier> _fichiers, List<Operation> _operations){
		
		boolean status;
		//step 1: extract "filenames" from fichiers, put into RootDir and generate Inode numbers
		for (Fichier f : _fichiers) {
			status = WriteFile ( f.getFileNum(), Integer.parseInt( f.getBlockSize() ) );  //project description seems to use file number and file name interchangeably.
			//System.out.println ( "status: " + status );
			
		}
		return true;
		
	}
	
	

	
	
	
	
	
	/* 
	 *  Few high level operations that controls the various lower level operations.
	 */
	
	//designed to only naively predict capacity exceeded a single file at a time
	//this is obviously not a full OS, not capable of performing a massive copy-paste operation
	//this won't detect in advance a series of write operations will exceed blocks.  
	boolean WriteFile(String _file_name, int _block_size){
				
		WriteRootDir( Block.getBlocksUsed() , _file_name ); //Inode.getMaxSize()
		
		Block.showCreationProcess( "\nFile: " + _file_name );//+ ", # Direct blocks, # Pointer blocks, # Indirect blocks");
		
		WriteInodeStructure( Block.getBlocksUsed(),  _block_size ); 

		Block.showCreationProcess( "File " + _file_name + " complete.");
				
		return true;
		
	}
	
	
	boolean DeleteFile(String _file_name){
		int inode_to_delete = SearchRootDirForInode(_file_name);		
		DeleteRootDirEntry(inode_to_delete);		
		DeleteInodeStructureEntry(inode_to_delete);
		return true;
	}
	
	
	/* 
	 *  Various lower level operations.
	 */
	
	//checks latest RootDir block if room to add, or new RootDir block necessary 
	boolean WriteRootDir(int _inode_number, String _file_name){
		
		//less granular catch, prevents file from being written at all
		if ( Block.getBlocksUsed() >= Disk.MAX_DISK_NUM_BLOCKS ) {
			System.out.println ("Max blocks exceeded, cannot add.");
			return false;
		}
		
		//add first root dir block
		else if ( root_dir_blocks.size() < 1 ) {
			//one time special case
			_inode_number++; 
			blocks.add( new RootDir(_inode_number, _file_name) );
			root_dir_blocks.add( (RootDir)blocks.get( blocks.size() - 1 ) );
			root_dir_blocks.get( root_dir_blocks.size() - 1).AddEntry(_inode_number, _file_name);
			return true;
		}
		
		//at least one block exists
		else  
			{
			//check latest root dir block
			if ( RootDir.getMaxSize() < RootDir.MAX_ROOT_DIRS_PER_BLOCK ){
				root_dir_blocks.get( root_dir_blocks.size() - 1).AddEntry(_inode_number, _file_name);
				//System.out.println("WTF");
				return true;
			} 
			else {
				//try to make new root dir block
				if (Block.getBlocksUsed() < Disk.MAX_DISK_NUM_BLOCKS ) {
					blocks.add( new RootDir(_inode_number, _file_name) );
					root_dir_blocks.add( (RootDir)blocks.get( blocks.size() - 1 ) );
					return true;
				}
				//more granular catch, can only occur when debugging
				else {
					System.out.println ("Max root dir blocks exceeded, cannot add.");
					return false;
				}
			}
		} 
	}	
	
	
	boolean WriteInodeStructure(int _inode_number, int _block_size){

		//already checked sufficient blocks exist in previous method call, sufficient for this project

		//initalize
		int predicted_block = Block.getBlocksUsed();
		predicted_block ++;
		
		//add first inode block
		WriteInode(_inode_number, _block_size);
		//predict_next_block = inodes.get( inodes.size() - 1).getBlockNum() + 1;
		
		//the following two if-statements were done to save time recoding, not as elegant as could be 
		//in theory a real disk manager would write the entire block at once, not come back like this implies
		if ( !Inode.isFitsOnly() ) { predicted_block ++; }
		inodes.get( inodes.size() - 1).AddEntry( predicted_block, true );
		if ( !Inode.isFitsOnly() ) { predicted_block --; }
		
		
		//keep disseminating until no blocks remaining 
		//top level case, go through everything
		
		
		//case: 1-12 blocks only
		if ( Inode.isFitsOnly() ){ //based on num blocks needed
			//writes addresses 2-12
			while ( Inode.getRemainsAddress() > 0 ) {
				predicted_block ++;
				inodes.get( inodes.size() - 1).AddEntry( predicted_block, true );
			}
			//writes blocks 1-12
			while ( Inode.getRemainsWrite() > 0 ) {
				WriteDirectBlock( TesterLoremIpsum.TESTER_LOREM_IPSUM );
			}
		}
		
		//cases: 13-40 blocks (or more)
		////finish writing inode that was started
		else { //know more than 12 blocks are necessary
			
			predicted_block++;
			
			//writes blocks 1-12
			while ( Inode.getAddressed() < Inode.MAX_ADDRESS_SIZE - 1 ) {
				predicted_block++;
				inodes.get( inodes.size() - 1).AddEntry( predicted_block, true);
			}

			//1st: push 13th address to pointer block
			predicted_block++;
			inodes.get( inodes.size() - 1).AddEntry( predicted_block, false);
			
			//2nd: write PointerBlock
			if ( Inode.isFull() ) { 
				WritePointerBlock( predicted_block );
			}

			//3rd: writes addresses 14-40
			while ( Inode.getRemainsAddress() > 0 ) {
				predicted_block++;
				pointer_blocks.get( pointer_blocks.size() - 1).AddEntry( predicted_block, true );
			}			
			
			//4th: write all direct blocks
			while ( Inode.getWritten() < Inode.MAX_ADDRESS_SIZE - 1  ) {
				WriteDirectBlock( TesterLoremIpsum.TESTER_LOREM_IPSUM );
			}
			
			//5th: write all subsequent remaining indirect blocks
			while ( Inode.getRemainsWrite() > 0 ) {
				WriteIndirectBlock( TesterLoremIpsum.TESTER_LOREM_IPSUM );
			}
		}		
			
		//no subsequent structure beyond initial inode required
		return true;
			
	}
	
	boolean WriteInode(int _inode_number, int _block_size){
		blocks.add( new Inode(_inode_number, _block_size) );
		inodes.add( (Inode)blocks.get( blocks.size() - 1 ) );
		return true;
	}
	
	boolean WriteDirectBlock(String _sample_datum){
		blocks.add( new DirectBlock(_sample_datum) );
		direct_blocks.add( (DirectBlock)blocks.get( blocks.size() - 1 ) );
		return true;
	}
	
	boolean WritePointerBlock(int _inode_number){
		blocks.add( new PointerBlock(_inode_number) );
		pointer_blocks.add( (PointerBlock)blocks.get( blocks.size() - 1 ) );
		return true;
	}
	
	boolean WriteIndirectBlock(String _sample_datum){
		blocks.add( new IndirectBlock(_sample_datum) );
		indirect_blocks.add( (IndirectBlock)blocks.get( blocks.size() - 1 ) );	
		return true;
	}
	
	
	
	boolean DeleteRootDirEntry(int inode_to_delete){
		//side-effect non-elegant code. 
		activeRootDir.root_dir_entries.remove( inode_to_delete );
		
		if ( activeRootDir.root_dir_entries.size() == 0 ){
			blocks.remove(activeRootDir);
			System.out.println( "RootDir block removed.  \n  It's a case i'd hope not to exercise, but can happen" );
			return true;
		}
		else 
			return false;
	}
	
	
	//converts from main blocks list to sublist
	int getSubIndexForPointerBlock(int inode){
		for (int i=0; i<pointer_blocks.size(); i++){
			if ( pointer_blocks.get(i).getBlockNum() == inode )
				return i; //sublist index
			else
				return -1;
		}
		return -1;
	}
	
	int getSubIndexForIndirectBlock(int inode){
		for (int i=0; i<indirect_blocks.size(); i++){
			if (indirect_blocks.get(i).getBlockNum() == inode) 
				return i; //sublist index
			else
				return -1;
		}
		return -1;
	}
	

	int getSubIndexForDirectBlock(int inode){
		for (int i=0; i<direct_blocks.size(); i++){
			if (direct_blocks.get(i).getBlockNum() == inode) 
				return i; //sublist index
			else
				return -1;
		}
		return -1;
	}
	
	
	int getSubIndexForInode(int inode){
		for (int i=0; i<inodes.size(); i++){
			if (inodes.get(i).getBlockNum() == inode) 
				return i; //sublist index
			else
				return -1;
		}
		return -1;
	}
	
	
	boolean DeleteInodeStructureEntry(int inode_to_delete){
		
		int sublist = getSubIndexForPointerBlock(inode_to_delete);
		int last_address = pointer_blocks.get(sublist).addresses.size();

		//go to that inode, if 13th address exists, delete it's corresponding indirect blocks first
		if ( last_address == Inode.MAX_ADDRESS_SIZE ){
			for (int i=0; i<last_address; i++){
				DeleteIndirectBlock( pointer_blocks.get(sublist).addresses.get(i) );
			} 
		}
		
		DeletePointerBlock( inode_to_delete );
			
		//use all pointers in inode to delete all links
		for (int i=0; i<Inode.MAX_ADDRESS_SIZE; i++){
			DeleteDirectBlock( inodes.get(inode_to_delete).addresses.get(i) );
		}

		//a single block
		DeleteInode( inode_to_delete ); 
		
		return true;
	}
	
	
	boolean DeleteDirectBlock(int inode){

		//delete from blocks list
		blocks.remove(inode);
			
		//delete from indirect_blocks list
		direct_blocks.remove(getSubIndexForDirectBlock( inode ) );
		
		return true;
	}
	
	boolean DeleteIndirectBlock(int inode){
	
		//delete from blocks list
		blocks.remove(inode);
			
		//delete from indirect_blocks list
		indirect_blocks.remove(getSubIndexForIndirectBlock( inode ) );
		
		return true;
	}
	
	boolean DeletePointerBlock(int inode){

		//delete from blocks list
		blocks.remove(inode);
			
		//delete from indirect_blocks list
		pointer_blocks.remove(getSubIndexForPointerBlock( inode ) );
		
		return true;	
	}
	
	boolean DeleteInode(int inode){ //a single block
		//delete from blocks list
		blocks.remove(inode);
			
		//delete from indirect_blocks list
		inodes.remove(getSubIndexForInode( inode ) );
		
		return true;
	} 
	
	
	//the fact that this is searching a distinctly separate list is a difference how this would operate on a 
	//normal OS. On p274 of the Tanenbaum textbook it implies it's separate by drawing section lines around "Root dir"
	//note it is a linked list.
	int SearchRootDirForInode(String _file_name){
		
		int lastRootDir;
		
		for (RootDir aRootDirBlock : root_dir_blocks) {
			lastRootDir = aRootDirBlock.root_dir_entries.size();			
			for (int i=0; i<=lastRootDir; i++){			
				//note: stopped caring about OO when implementing search & delete, no time left to spare.
				if ( _file_name.equals ( aRootDirBlock.root_dir_entries.get(i).getRootDirFileName() ) ){
					//side-effect, not elegant code
					activeRootDir = aRootDirBlock;
					return aRootDirBlock.root_dir_entries.get(i).getRootDirInode();
				}
			}
		}
		return -1;
	}
	
	
	boolean DeleteAddressesOfInode(Inode b){
		return true;
	}
	
	//removes direct and indirect blocks
	boolean DeleteAddressesOfPointer(int inode){
		
		//delete from blocks list
		blocks.remove(inode);
			
		//delete from pointer_blocks list
		for (int i=0; i<pointer_blocks.size(); i++){
			if (pointer_blocks.get(i).getBlockNum() == inode) {
				pointer_blocks.remove(i);
			}
		}
		
		return true;		
	}
	
	
	
	//typically only 1, but capable of scaling until system full. 
	void showRootDirs(){

		for (RootDir r : root_dir_blocks) {
			r.showEntries(); 
		}		
	}

	
	void showInodes(){

		for (Inode i : inodes) {
			i.showEntries(); 
		}		
	}	
	
	//by type in order for each
	void showBlocksByType(){
		showRootDirs();
		showInodes();
	}

	
	//by blocks in order overall
	void showBlocksWithContent(){
		
		activateShowCreationProcess();
		
		//TEST!!//
		//causes strange symptom, don't care, don't need
		//blocks.add(new RootDir(0, "random file name"));
		//blocks.add(new RootDir(0, "another file"));
		//TEST!!//

		for ( Block aBlock : blocks ) {
			Block currentBlock = aBlock ;
			currentBlock.showMeta();
			currentBlock.showEntries();
			Block.showCreationProcess( "" );
		}
		
	}

	
	void showBlocks(){
		
		activateShowCreationProcess();
		
		for ( Block aBlock : blocks ) {
			Block currentBlock = aBlock ;
			currentBlock.showMeta();
		}
		
	}
	
	//actually the same method name as in Block but not affected by the other one
	void activateShowCreationProcess() {
		Block.showCreationProcess = true;
		Block.showCreationProcess( "\r\nShowing Creation Process " ); 
	}
	
	/*
	int getRootDirNum(){
		return root_dir_blocks.size();
	}
	*/
	
	
}
