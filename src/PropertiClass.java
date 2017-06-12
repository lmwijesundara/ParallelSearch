import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class PropertiClass {

	Properties property;
	FileInputStream fs;
	
	public PropertiClass() {
		try {
			
			fs = new FileInputStream(System.getProperty("user.dir")+"\\Config.properties");
			property = new Properties();
			property.load(fs);
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		
	
	}
	
	public String getStart() throws IOException {		
		
		return property.getProperty("Start_molecule");
	}
	
	public String getEnd() throws IOException {

		return property.getProperty("End_molecule");
	}
	
	public String getFilePath() throws IOException {
	
		return property.getProperty("File_path");
	}
	
	public int getNodeCountValue() {

		return  Integer.parseInt(property.getProperty("node_count_value"));
	}
}
