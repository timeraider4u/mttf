package at.jku.weiner.mttf.tests

import org.junit.runner.RunWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.InjectWith
import com.google.inject.Inject
import org.eclipse.xtext.junit4.util.ParseHelper
import at.jku.weiner.mttf.mttf.TestSuite
import org.junit.Test
import org.eclipse.xtext.generator.InMemoryFileSystemAccess
import org.junit.Assert
import org.eclipse.xtext.builder.EclipseResourceFileSystemAccess2
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import at.jku.weiner.mttf.utils.EclipseUtilities
import at.jku.weiner.mttf.generator.MttfGenerator
import org.eclipse.xtext.generator.IGenerator2

@RunWith(XtextRunner)
@InjectWith(MttfInjectorProvider)
class GeneratorTest {
	@Inject
	ParseHelper<TestSuite> parseHelper
	@Inject 
	ValidationTestHelper validationHelper
	@Inject
	//MttfGenerator underTest
	IGenerator2 underTest
	
	@Test
	def testGeneratingCombinedMetaModel() {
		val model = parseHelper.parse('''
			test-suite
				source-metamodel="platform:/plugin/at.jku.weiner.mttf.tests/metamodels/Class.xmi"
				target-metamodel="platform:/plugin/at.jku.weiner.mttf.tests/metamodels/Relational.xmi"
				transformation="platform:/plugin/at.jku.weiner.mttf.tests/transformations/Class2Relational.atl"
		''')
		validationHelper.assertNoErrors(model)
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa, null)
		// print files
		val keyset = fsa.getAllFiles().keySet
		for (var i = 0; i < keyset.size; i++) {
			val key = keyset.get(i);
			println("key(" + i + ")='" + key + "'");
		}
		// ...
		Assert.assertEquals(1, fsa.getAllFiles.size)
		val filename = IFileSystemAccess::DEFAULT_OUTPUT+"metamodels/Class2Relational.atl.xmi"
		Assert.assertTrue(fsa.getAllFiles.containsKey(filename))
		val actualFileContent = fsa.getAllFiles.get(filename);
		val expectedFileContent = 
			'''
            public class Alice {
                 
            }
			''';
		Assert. assertEquals(expectedFileContent.toString, actualFileContent.toString);
	}
	
	@Test
	def testGeneratingCombinedMetaModelWithCopyingPluginToResource() {
		//copy files
		val src = "platform:/plugin/at.jku.weiner.mttf.tests/";
		val dst = "platform:/resource/my-test/";
		val project = EclipseUtilities.copyProject(src, dst, true);
		Assert.assertNotNull(project);
		Assert.assertTrue(project.exists());
		// ...
		val model = parseHelper.parse('''
			test-suite
				source-metamodel="platform:/resource/my-test/metamodels/Class.xmi"
				target-metamodel="platform:/resource/my-test/metamodels/Relational.xmi"
				transformation="platform:/resource/my-test/transformations/Class2Relational.atl"
		''')
		validationHelper.assertNoErrors(model)
		val fsa = new EclipseResourceFileSystemAccess2();
		fsa.setMonitor(EclipseUtilities.getProgressMonitor());
		//underTest.setProject(project);
		underTest.doGenerate(model.eResource, fsa, null)
		// ...
		val fileName = "metamodels/Class2Relational.atl.xmi";
		Assert.assertTrue(fsa.isFile(fileName));
		val actualFileContent = fsa.readTextFile(fileName);
		val expectedFileContent = 
			'''
            public class Alice {
                 
            }
			''';
		Assert. assertEquals(expectedFileContent.toString, actualFileContent.toString);
	}
	
}