package at.jku.weiner.mttf.extensionpoints;

import java.util.List;

public interface ITransformation {
	
	public boolean isValidTransformation(String uriAsString);
	
	public List<String> isRegisteredForFileExtensions();

}
