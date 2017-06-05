package at.jku.weiner.mttf.tests;

import java.io.File;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

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

}
