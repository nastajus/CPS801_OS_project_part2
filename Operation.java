public class Operation {
    
    String requested_file = "";
    String operation = "";
    
    Operation (String _requested_file, String _operation){
    	requested_file = _requested_file;
        operation = _operation;
    }
        
    //comma separated output
    public String getDetails(){
        String output = requested_file + ", " + operation + "\r\n";
        return output;
    }
        
}
