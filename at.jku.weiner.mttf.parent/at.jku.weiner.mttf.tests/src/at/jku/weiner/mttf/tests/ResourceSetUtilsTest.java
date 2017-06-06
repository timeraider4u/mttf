package at.jku.weiner.mttf.tests;

import java.io.IOException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Assert;
import org.junit.Test;

import at.jku.weiner.mttf.utils.ResourceSetUtils;

public class ResourceSetUtilsTest {

	@Test
	public void testResourceLoading() throws IOException {
		final String uriAsString = "platform:/plugin/at.jku.weiner.mttf.tests/metamodels/Class.xmi";
		final Resource resource = ResourceSetUtils.getInstance()
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

}
