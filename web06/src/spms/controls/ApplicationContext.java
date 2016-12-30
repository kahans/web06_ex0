package spms.controls;

import java.util.Hashtable;
import java.util.Map;

//application-context.properties °´Ã¼ °ü¸®
public class ApplicationContext {
	
	Map<String, Object> objTable = new Hashtable<String, Object>();
	private String propertiesPath;

	public ApplicationContext(String propertiesPath) {
		super();
		this.propertiesPath=propertiesPath;
	}
	
	public ApplicationContext(){
		
	}
}
