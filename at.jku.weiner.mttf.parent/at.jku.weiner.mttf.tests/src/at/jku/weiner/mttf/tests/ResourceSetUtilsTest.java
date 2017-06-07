package at.jku.weiner.mttf.tests;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Assert;
import org.junit.Test;

import at.jku.weiner.mttf.utils.EclipseUtilities;
import at.jku.weiner.mttf.utils.ResourceSetUtils;
import at.jku.weiner.mttf.utils.UriUtils;

public class ResourceSetUtilsTest {

	@Test
	public void testResourceLoading() throws IOException {
		final String uriAsString = "platform:/plugin/at.jku.weiner.mttf.tests/metamodels/Class.xmi";
		final Resource resource = ResourceSetUtils
				.loadResourceWithoutHandling(uriAsString);
		Assert.assertNotNull(resource);
		final EList<EObject> content = resource.getContents();
		Assert.assertNotNull(content);
		Assert.assertEquals(2, content.size());
		// primitive types
		final EObject packagePrimitiveTypesObj = content.get(0);
		Assert.assertNotNull(packagePrimitiveTypesObj);
		Assert.assertTrue(packagePrimitiveTypesObj instanceof EPackage);
		final EPackage packagePrimitiveTypes = (EPackage) packagePrimitiveTypesObj;
		Assert.assertEquals("PrimitiveTypes", packagePrimitiveTypes.getName());
		Assert.assertEquals(null, packagePrimitiveTypes.getNsPrefix());
		Assert.assertEquals(null, packagePrimitiveTypes.getNsURI());
		// class
		final EObject packageClassObj = content.get(1);
		Assert.assertNotNull(packageClassObj);
		Assert.assertTrue(packageClassObj instanceof EPackage);
		final EPackage packageClass = (EPackage) packageClassObj;
		Assert.assertEquals("Class", packageClass.getName());
		Assert.assertEquals(null, packageClass.getNsPrefix());
		Assert.assertEquals(null, packageClass.getNsURI());
	}

	@Test
	public void testGetIFileForResource() throws IOException {
		final String resName = "metamodels/Class.xmi";
		this.doGetIFileForResource(resName);
	}
	
	@Test
	public void testGetIFileForXtextResource() throws IOException {
		final String resName = "res/TestGeneratingCombinedMetaModelWithCopyingPluginToResource.mttf";
		this.doGetIFileForResource(resName);
	}
	
	private void doGetIFileForResource(final String resName)
			throws IOException {
		// copy files
		final String src = "platform:/plugin/at.jku.weiner.mttf.tests";
		final String dst = "platform:/resource/my-test";
		final IProject dstProject = EclipseUtilities.copyProject(src, dst,
				true);
		// expected file
		final IFile iFile2 = dstProject.getFile(resName);
		Assert.assertNotNull(iFile2);
		Assert.assertTrue(iFile2.exists());
		Assert.assertTrue(iFile2.isAccessible());
		// load resource
		final String uriAsString = "platform:/resource/my-test/" + resName;
		final Resource resource = ResourceSetUtils
				.loadResourceWithoutHandling(uriAsString);
		Assert.assertNotNull(resource);
		final IFile iFile = ResourceSetUtils.getIFileForResource(resource);
		Assert.assertNotNull(iFile);
		Assert.assertTrue(iFile.exists());
		Assert.assertTrue(iFile.isAccessible());
		// compare absolute paths
		final File actualFile = UriUtils.getFileFor(iFile);
		final File expectedFile = UriUtils.getFileFor(iFile2);
		final String actual = actualFile.getAbsolutePath();
		final String expected = expectedFile.getAbsolutePath();
		Assert.assertEquals(expected, actual);
	}
	
}
