//File == Fichier in French, needed a different word
public class Fichier {
    
    String file_num = "";
    String block_size = "";
    String byte_size = "";
    
    Fichier (String _file_num, String _block_size, String _byte_size){
        file_num = _file_num;
        block_size = _block_size;
        byte_size = _byte_size;
    }
    
    Fichier (String _file_num, String _block_size){
        file_num = _file_num;
        block_size = _block_size;
        byte_size = Integer.toString( Integer.parseInt(_block_size) * OS.BYTES_PER_BLOCK );
    }
    
    //comma separated output
    public String getDetails(){
        return file_num + ", " + block_size + ", " + byte_size + "\r\n";
    }
    
    public String getFileNum(){
    	return file_num;
    }
    
    public String getBlockSize(){
    	return block_size;
    }
    
    //rename
    void setFileNum(String _file_num){
        file_num = _file_num;
    }
    
}
