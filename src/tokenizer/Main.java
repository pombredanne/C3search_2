package tokenizer;
import java.io.File;
import java.io.FileNotFoundException;


public class Main {
	static String path = "C:\\Users\\peixia\\Desktop\\SmallGame\\src\\gameTools\\GLTextureFactory.java";
	public static void main(String[] args){
		Tokenizer tokenizer = new Tokenizer();
	//	tokenizer.tokennize("", 0, 0, 0,args);
		System.out.println(tokenizer.toString());
		
	}
}
