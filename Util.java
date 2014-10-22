import java.util.Random;

public class Util {

	static Fichier createRandomFile(int file_num){
	
		Random randomGenerator = new Random();
		
		int randomInt = randomGenerator.nextInt(OS.MAX_SIZE_IN_BLOCKS) + 1;
		Fichier file = new Fichier (Integer.toString(file_num), Integer.toString(randomInt), Integer.toString(randomInt * OS.BYTES_PER_BLOCK));  	
		file.getDetails();

		return file;

	}

	static Operation createRandomOp(){
		
		Random randomGenerator = new Random();
		
		int randomIntOperation = randomGenerator.nextInt(Tester.OPERATION_RANGE) + 1;
		int randomIntBinary = randomGenerator.nextInt(2);
		String randomReadWrite;

		if (randomIntBinary == 0) { randomReadWrite = "R"; }
		else { randomReadWrite = "W"; }
		
		Operation op = new Operation(Integer.toString(randomIntOperation), randomReadWrite );  	
		op.getDetails();

		return op;

	}

}
