package at.jku.weiner.mttf.utils;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;

public class EclipseUtilities {
	
	private EclipseUtilities() {
		
	}

	public static IWorkspaceRoot getWorkspaceRoot() {
		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IWorkspaceRoot root = workspace.getRoot();
		return root;
	}

	public static IProgressMonitor getProgressMonitor() {
		return new NullProgressMonitor();
	}
	
	public static IProject copyProject(final String srcUriAsString,
			final String dstUriAsString) {
		try {
			final File srcDir = UriUtils.getFileFor(srcUriAsString);
			final File dstDir = UriUtils.getFileFor(dstUriAsString);
			final URI dstUri = URI.createURI(dstUriAsString);
			final String dstProjectName = UriUtils
					.getBundleStringFromURI(dstUri);
			final IWorkspaceRoot root = EclipseUtilities.getWorkspaceRoot();
			final IProject dstProject = root.getProject(dstProjectName);
			if (dstProject.exists()) {
				dstProject.delete(true, EclipseUtilities.getProgressMonitor());
			}
			Thread.sleep(100);
			dstProject.create(EclipseUtilities.getProgressMonitor());
			dstProject.open(EclipseUtilities.getProgressMonitor());
			dstProject.refreshLocal(IResource.DEPTH_INFINITE,
					EclipseUtilities.getProgressMonitor());
			FileUtils.copyDirectory(srcDir, dstDir);
			dstProject.refreshLocal(IResource.DEPTH_INFINITE,
					EclipseUtilities.getProgressMonitor());
			return dstProject;
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static void closeProject(final IProject project) {
		try {
			project.close(EclipseUtilities.getProgressMonitor());
		} catch (final CoreException ex) {
			ex.printStackTrace();
		}
	}

}
