//a single entry in a Root Dir block
public class RootDirEntry {
	
	int inode_number;
	String file_name;
	
	
	RootDirEntry (int _inode_number, String _file_name){
		inode_number = _inode_number;
		file_name = _file_name;
		
	}
	
	int getRootDirInode(){
		return inode_number;		
	}

	String getRootDirFileName(){
		return file_name;
	}
	
	String getDetails(){
		return inode_number + ", " + file_name;
	}
}
