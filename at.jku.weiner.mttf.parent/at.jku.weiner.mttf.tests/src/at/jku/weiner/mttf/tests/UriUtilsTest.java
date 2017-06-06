package at.jku.weiner.mttf.tests;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import at.jku.weiner.mttf.utils.EclipseUtilities;
import at.jku.weiner.mttf.utils.UriUtils;

public class UriUtilsTest {
	
	@Test
	public void testFileForLinuxFilePath() {
		final String uriAsString = "file:///tmp/myfile.txt";
		final File actualFile = UriUtils.getFileFor(uriAsString);
		final String actual = actualFile.getAbsolutePath();
		Assert.assertEquals("/tmp/myfile.txt", actual);
	}
	
	@Test
	@Ignore
	public void testFileForWindowsFilePath() {
		final String uriAsString = "file:///C:/Temp/myfile.txt";
		final File actualFile = UriUtils.getFileFor(uriAsString);
		final String actual = actualFile.getAbsolutePath();
		Assert.assertEquals(File.separator + "C:" + File.separator + "Temp"
				+ File.separator + "myfile.txt", actual);
	}
	
	@Test
	public void testFileForPluginFile() {
		final String uriAsString = "platform:/plugin/at.jku.weiner.mttf.tests/metamodels/Class.xmi";
		final File actualFile = UriUtils.getFileFor(uriAsString);
		Assert.assertNotNull(actualFile);
		Assert.assertTrue(actualFile.exists());
		Assert.assertTrue(actualFile.canRead());
	}
	
	@Test
	@Ignore
	public void testFileForPluginFileNested() {
		final String uriAsString = "platform:/plugin/at.jku.weiner.mttf.parent/at.jku.weiner.mttf.tests/metamodels/Class.xmi";
		final File actualFile = UriUtils.getFileFor(uriAsString);
		Assert.assertNotNull(actualFile);
		Assert.assertTrue(actualFile.exists());
		Assert.assertTrue(actualFile.canRead());
	}

	@Test
	public void testFileForNonExistingPluginFile() {
		final String uriAsString = "platform:/plugin/NON-EXISTING/UNKNOWN/NON_EXISTING";
		final File actualFile = UriUtils.getFileFor(uriAsString);
		Assert.assertNotNull(actualFile);
		Assert.assertFalse(actualFile.exists());
		Assert.assertFalse(actualFile.canRead());
	}
	
	@Test(timeout = 10000)
	public void testFileForCopyingProject() {
		final String src = "platform:/plugin/at.jku.weiner.mttf.tests";
		final String dst = "platform:/resource/my-test";
		final File srcDir = UriUtils.getFileFor(src);
		final File dstDir = UriUtils.getFileFor(dst);
		// copy files
		final IProject dstProject = EclipseUtilities.copyProject(src, dst);
		Assert.assertNotNull(dstProject);
		Assert.assertTrue(dstProject.exists());
		Assert.assertTrue(dstProject.isOpen());
		Assert.assertNotNull(srcDir);
		Assert.assertNotNull(dstDir);
		Assert.assertTrue(dstDir.exists());
		Assert.assertTrue(dstDir.isDirectory());
		// test access to file in project
		final IFile iFile = dstProject.getFile("metamodels/Class.xmi");
		Assert.assertNotNull(iFile);
		Assert.assertTrue(iFile.exists());
		Assert.assertTrue(iFile.isAccessible());
		final String osString = iFile.getLocation().toOSString();
		// test access to resource file
		final File classMM = UriUtils
				.getFileFor("platform:/resource/my-test/metamodels/Class.xmi");
		Assert.assertNotNull(classMM);
		Assert.assertTrue(classMM.exists());
		Assert.assertTrue(classMM.canRead());
		final String absString = classMM.getAbsolutePath();
		Assert.assertEquals(absString, osString);
		EclipseUtilities.closeProject(dstProject);
	}

}
