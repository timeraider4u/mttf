package at.jku.weiner.mttf.tests;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import at.jku.weiner.mttf.extensionpoints.ITransformation;
import at.jku.weiner.mttf.extensionpoints.TransformationRegistry;
import at.jku.weiner.mttf.extensions.core.ATLTransformation;

public class ExtensionsTest {

	private List<ITransformation> transformations;

	@Before
	public void setUp() {
		this.transformations = TransformationRegistry.getITransformations();
	}

	@Test
	public void testGetATLExtension() {
		Assert.assertNotNull(this.transformations);
		Assert.assertTrue(this.transformations.size() >= 1);
		final ATLTransformation atl = this.getATLTransformation();
		Assert.assertNotNull(atl);
		final List<String> fileExtensions = TransformationRegistry
				.getExtensionsFor(atl);
		Assert.assertNotNull(fileExtensions);
		Assert.assertTrue(fileExtensions.size() >= 1);
		final boolean contains = fileExtensions
				.contains(ATLTransformation.ATL_FILE_EXT);
		Assert.assertTrue(contains);
	}
	
	private ATLTransformation getATLTransformation() {
		ATLTransformation atl = null;
		for (int i = 0; i < this.transformations.size(); i++) {
			final ITransformation transformation = this.transformations.get(i);
			if (transformation instanceof ATLTransformation) {
				atl = (ATLTransformation) transformation;
			}
		}
		return atl;
	}

}
