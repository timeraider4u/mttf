package at.jku.weiner.mttf.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.osgi.service.datalocation.Location;
import org.osgi.framework.Bundle;

public class UriUtils {

	public static File getFileFor(final IFile iFile) {
		final IPath path = iFile.getFullPath();
		final File result = path.toFile();
		return result;
	}

	public static File getFileFor(final String uriAsString) {
		try {
			final URI uri = URI.createURI(uriAsString);
			if (uri.isFile()) {
				return UriUtils.getFileForFileURI(uriAsString);
			} else if (uri.isPlatformPlugin()) {
				return UriUtils.getFileForPluginURI(uriAsString, uri);
			} else if (uri.isPlatformResource()) {
				return UriUtils.getFileForResourceURI(uriAsString, uri);
			}
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private static File getFileForFileURI(final String uriAsString)
			throws Exception {
		final java.net.URI uri = new java.net.URI(uriAsString);
		final File file = new File(uri);
		return file;
	}

	/***
	 * see also
	 * https://stackoverflow.com/questions/23825933/how-to-get-resource-from-another-plugin
	 */
	private static File getFileForPluginURI(final String uriAsString,
			final URI uri) throws Exception, IOException {
		
		final String myUri = uri.toPlatformString(true);
		System.out.println("myURI='" + myUri + "'");
		
		final String bundleName = UriUtils.getBundleStringFromURI(uri);
		System.out.println("bundleName='" + bundleName + "'");

		final String entry = UriUtils.getEntryStringFromURI(uri);
		System.out.println("entry='" + entry + "'");

		final Bundle bundle = Platform.getBundle(bundleName);
		final URL entryURL = bundle.getEntry(entry);
		System.out.println("entryURL='" + entryURL + "'");
		final URL fileURL = FileLocator.toFileURL(entryURL);
		System.out.println("fileURL='" + fileURL + "'");
		
		final String protocol = fileURL.getProtocol();
		final String path = fileURL.getPath();
		
		final java.net.URI resolvedURI = new java.net.URI(protocol, path, null);
		System.out.println("resolvedURI='" + resolvedURI + "'");

		final File result = new File(resolvedURI);
		System.out.println("result.path='" + result.getAbsolutePath() + "'");

		return result;
	}
	
	private static String getBundleStringFromURI(final URI uri) {
		final String myUri = uri.toPlatformString(true);
		final int index = myUri.indexOf("/", 1);
		final String result = myUri.substring(1, index);
		return result;
	}
	
	private static String getEntryStringFromURI(final URI uri) {
		final String myUri = uri.toPlatformString(true);
		final int index = myUri.indexOf("/", 1);
		final int length = myUri.length();
		final String result = myUri.substring(index + 1, length);
		return result;
	}
	
	private static File getFileForResourceURI(final String uriAsString,
			final URI uri) {
		final String myUri = uri.toPlatformString(true);

		return null;
	}
	
	public static File getBundlePath() {
		final Plugin plugin = ResourcesPlugin.getPlugin();
		final Bundle bundle = plugin.getBundle();
		final String name = bundle.getSymbolicName();
		final Location platformInstall = Platform.getInstallLocation();
		final URL url = platformInstall.getURL();
		final String installPath = url.toString().replaceAll(".*file:", "");
		
		String bundlePath = bundle.getLocation();
		if (bundlePath.startsWith("initial@reference:file:")) {
			bundlePath = bundlePath.replaceAll("initial@reference:file:",
					installPath);
		}
		final File result = new File(bundlePath);
		return result;
	}

}
