import java.util.ArrayList;
import java.util.List;


/*
 *  Short Description: directory associates filenames with inodes
 */

//while not directly described in the as being a block, all data structures must fit in blocks regardless
//thus this has been constructed as such.
public class RootDir extends Block {
	
	//logic  
	final static int MAX_INODE_SIZE = (1024 / 16) * 2;
	final static int MAX_FILE_NAME_SIZE = (1024 / 16) * 14;
	final static int MAX_ROOT_DIRS_PER_BLOCK = 32;

	static int root_dirs_used = 0;
	int root_dir_number = 0;
	
	
	//static int current_inode_num = 0; 
	
	String inode_number = "";	//2 bytes
	String file_name = "";		//14 bytes
	
	//since blocks are 512 bytes, and each rootdir entry is 16 bytes, 
	//there are 32 entries possible per block.
	
	//since characters are 16-bit in Java, 
	//thus 2 bytes support up to 128 characters - 1, and
	//thus 14 bytes support up to 896 characters - 1.
	//by far both exceed the parameters of this assignment significantly.

	
	
	
	static List<RootDirEntry> root_dir_entries = new ArrayList<RootDirEntry>();
	
	
	RootDir(int _inode_number, String _file_name){
		super();
		showCreationProcess( "\r\nRoot Dir #: " +  this.root_dir_number + ", Block #: " + Integer.toString( getBlockNum() ) );
		
		
		this.root_dir_number = RootDir.root_dirs_used;
		
		//basic but incomplete protection against unbounded block growth.
		if ( root_dirs_used < RootDir.MAX_ROOT_DIRS_PER_BLOCK ) {
			RootDir.root_dirs_used++;
		}
		
		
		/*
		 * All the other block stuff also instantiated
		 */
		
		inode_number = String.valueOf( _inode_number );
		file_name = _file_name;
		
		
		
		
	}
	
	
	boolean AddEntry(int _inode_number, String _file_name){
		
		if ( root_dir_entries.size() < RootDir.MAX_ROOT_DIRS_PER_BLOCK ){
			root_dir_entries.add( new RootDirEntry(_inode_number, _file_name) );

			//repeat Root Dir info
			showCreationProcess( "\r\nRoot Dir #: " +  this.root_dir_number + ", Block #: " + Integer.toString( getBlockNum() ) );
			showCreationProcess( " new entry! (@ " +  _inode_number + ") to File: " + _file_name ) ;
			return true;
		}
		else {
			showCreationProcess ("Max entries exceeded, cannot add.");
			return false;
		}
	}

	void showMeta(){
		showCreationProcess( "\r\nRoot Dir #: " +  this.root_dir_number + ", Block #: " + Integer.toString( getBlockNum() ) );
	}
	
	void showEntries(){
		
		for (RootDirEntry rde : root_dir_entries) {
			showCreationProcess( " entry: " + rde.getDetails() );
		}
				
		//System.out.println("");

		
	}
	
	static int getMaxSize(){
		return root_dirs_used;
	}
	
	

}
