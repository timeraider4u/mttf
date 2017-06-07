package at.jku.weiner.mttf.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/***
 * Trying to centralize code for loading resources from a ResourceSet.
 * <br/>
 *
 * @author Harald Weiner
 */
public final class ResourceSetUtils {

	private ResourceSetUtils() {

	}

	public static ResourceSet getResourceSetForURI(final URI uri) {
		return new ResourceSetImpl();
	}
	
	public static Resource loadResource(final String uriAsString) {
		try {
			final Resource result = ResourceSetUtils
					.loadResourceWithoutHandling(uriAsString);
			return result;
		} catch (final IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static Resource loadResourceWithoutHandling(final String uriAsString)
			throws IOException {
		final URI uri = URI.createURI(uriAsString);
		final ResourceSet resourceSet = ResourceSetUtils
				.getResourceSetForURI(uri);
		final Resource resource = resourceSet.getResource(uri, true);
		
		final Map<Object, Object> options = ResourceSetUtils
				.getLoadOptionsForURI(uri);
		resource.load(options);
		return resource;
	}
	
	private static Map<Object, Object> getLoadOptionsForURI(final URI uri) {
		return new HashMap<Object, Object>();
	}

	/***
	 * see also
	 * http://blog.eclipse-tips.com/2008/03/converting-emf-resource-to-platform.html
	 */
	public static IFile getIFileForResource(final Resource resource) {
		final URI uri = resource.getURI();
		System.out.println("getIFileForResource-uri='" + uri + "'");
		if (!uri.isPlatformResource()) {
			return null;
		}
		final String platformString = uri.toPlatformString(true);
		System.out.println(
				"getIFileForResource-platformString='" + platformString + "'");
		final IWorkspaceRoot root = EclipseUtilities.getWorkspaceRoot();
		final Path path = new Path(platformString);
		final IFile result = root.getFile(path);
		return result;
	}

}
