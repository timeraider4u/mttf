package at.jku.weiner.mttf.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.osgi.service.datalocation.Location;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

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
		
		// final Bundle bundle = FrameworkUtil.getBundle(UriUtils.class);
		// final String bundleName = bundle.getSymbolicName();
		// System.out.println("bundleName='" + bundleName + "'");
		//
		// final String bundlePath = UriUtils.getBundlePath();
		// final String installPath = UriUtils.getPluginsPath();
		
		// final URL pluginURL = bundle.getEntry("/");
		// System.out.println("pluginURL='" + pluginURL + "'");
		// final URL resolvedURL = FileLocator.resolve(pluginURL);
		// System.out.println("resolvedURL='" + resolvedURL + "'");
		// final java.net.URI resolvedURI = resolvedURL.toURI();
		// System.out.println("resolvedURI='" + resolvedURI + "'");
		
		// final File bundleDirectory = new File(bundlePath);
		// final String entry = UriUtils.getPluginEntryStringFromURI(uri);
		// final File result = new File(bundleDirectory, entry);
		// System.out.println("bundlePath='" + bundlePath + "'");
		// System.out.println("installPath='" + installPath + "'");
		//
		// System.out.println("entry='" + entry + "'");
		// System.out.println(
		// "getFileForPluginURI='" + result.getAbsolutePath() + "'");
		
		// System.out.println("entry='" + entry + "'");
		// System.out.println(
		// "getFileForPluginURI='" + result.getAbsolutePath() + "'");
		
		// final String bundlePath = UriUtils.getBundlePath();
		// final String entry = UriUtils.getEntryStringFromURI(uri);
		// final File result = new File(bundlePath, entry);
		// System.out.println("bundlePath='" + bundlePath + "'");
		// System.out.println("entry='" + entry + "'");
		// System.out.println(
		// "getFileForPluginURI='" + result.getAbsolutePath() + "'");
		
		final String bundleName = UriUtils.getBundleStringFromURI(uri);
		final String entry = UriUtils.getPluginEntryStringFromURI(uri);
		System.out
				.println("getFileForPluginURI.bundleName='" + bundleName + "'");
		System.out.println("getFileForPluginURI.entry='" + entry + "'");
		final Bundle bundle = Platform.getBundle(bundleName);
		if (bundle == null) {
			return new File("");
		}
		final URL entryURL = bundle.getEntry(entry);
		final URL fileURL = FileLocator.toFileURL(entryURL);
		final String protocol = fileURL.getProtocol();
		final String path = fileURL.getPath();
		final java.net.URI resolvedURI = new java.net.URI(protocol, path, null);
		final File result = new File(resolvedURI);
		System.out.println("getFileForPluginURI.result='"
				+ result.getAbsolutePath() + "'");
		return result;
	}

	protected static String getBundleStringFromURI(final URI uri) {
		final String myUri = uri.toPlatformString(true);
		final int length = myUri.length();
		int index = myUri.indexOf("/", 1);
		if (index < 0) {
			index = length;
		}
		final String result = myUri.substring(1, index);
		return result;
	}

	protected static String getPluginEntryStringFromURI(final URI uri) {
		final String myUri = uri.toPlatformString(true);
		final int length = myUri.length();
		final int index = myUri.indexOf("/", 1);
		if (index < 0) {
			return "/";
		}
		final String result = myUri.substring(index + 1, length);
		return result;
	}

	private static File getFileForResourceURI(final String uriAsString,
			final URI uri) {
		final String entry = UriUtils.getEntryStringFromURI(uri);
		System.out.println("getFileForResourceURI.entry='" + entry + "'");
		final IWorkspaceRoot root = EclipseUtilities.getWorkspaceRoot();
		// final IResource resource = root.findMember(entry);
		// if (resource == null) {
		final IPath path = root.getLocation();
		final File rootFile = path.toFile();
		System.out.println("getFileForResourceURI.rootFile='"
				+ rootFile.getAbsolutePath() + "'");
		final File result = new File(rootFile, entry);
		System.out.println("getFileForResourceURI.result='"
				+ result.getAbsolutePath() + "'");
		return result;
		// }
		// final IPath path = resource.getFullPath();
		// final File result = path.toFile();
		// return result;
	}
	
	private static String getEntryStringFromURI(final URI uri) {
		final String myUri = uri.toPlatformString(true);
		final int length = myUri.length();
		final String substring = myUri.substring(1, length);
		final String result = substring.replace("/", File.separator);
		return result;
	}
	
	private static String getPluginsPath() {
		final Location platformInstall = Platform.getInstallLocation();
		final URL url = platformInstall.getURL();
		final String installPath = url.toString().replaceAll(".*file:", "");
		return installPath;
	}

	/***
	 * partially copied from
	 * org.eclipse.gmt.modisco.infra.common.core.internal.utils.ProjectUtils
	 * see also
	 * http://git.eclipse.org/c/modisco/org.eclipse.modisco.git/tree/org.eclipse.gmt.modisco.infra.common.core/src/org/eclipse/gmt/modisco/infra/common/core/internal/utils/ProjectUtils.java
	 */
	private static String getBundlePath() {
		final Bundle bundle = FrameworkUtil.getBundle(UriUtils.class);
		final String bundlePath = bundle.getLocation();
		final Location platformInstall = Platform.getInstallLocation();
		final URL url = platformInstall.getURL();
		final String installPath = url.toString().replaceAll(".*file:", "");

		String result;
		if (bundlePath.startsWith("initial@reference:file:")) {
			result = bundlePath.replaceAll("initial@reference:file:",
					installPath);
		} else {
			result = bundlePath.replaceFirst("^reference:file:", "");
		}
		return result;
	}
	
}
