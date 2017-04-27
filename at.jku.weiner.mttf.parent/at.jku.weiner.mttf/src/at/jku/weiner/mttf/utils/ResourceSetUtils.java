package at.jku.weiner.mttf.utils;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

public class ResourceSetUtils {

	public static ResourceSet getResourceSetForURI(final URI uri) {
		return new ResourceSetImpl();
	}
	
	public static Map<Object, Object> getLoadOptionsForURI(final URI uri) {
		return new HashMap<Object, Object>();
	}
	
}
