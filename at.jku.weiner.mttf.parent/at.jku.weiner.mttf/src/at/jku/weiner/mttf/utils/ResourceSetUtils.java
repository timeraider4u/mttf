package at.jku.weiner.mttf.utils;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
//import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Inject;
import com.google.inject.Provider;

/***
 * Trying to centralize code for loading resources from a ResourceSet.
 * <br/>
 * <br/>
 * Singleton object
 * 
 * @author Harald Weiner
 *
 */
public final class ResourceSetUtils {

	private static ResourceSetUtils instance = null;

	private ResourceSetUtils() {

	}

	public static ResourceSetUtils getInstance() {
		if (ResourceSetUtils.instance == null) {
			ResourceSetUtils.instance = new ResourceSetUtils();
		}
		return ResourceSetUtils.instance;
	}

	@Inject
	private Provider<XtextResourceSet> resourceSetProvider;
	
	public ResourceSet getResourceSetForURI(final URI uri) {
		// return new ResourceSetImpl();
		final XtextResourceSet result = this.resourceSetProvider.get();
		return result;
	}

	public Map<Object, Object> getLoadOptionsForURI(final URI uri) {
		return new HashMap<Object, Object>();
	}

}
