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
import org.eclipse.xtext.generator.IGenerator2
import at.jku.weiner.mttf.utils.ResourceSetUtils
import at.jku.weiner.mttf.generator.MttfGenerator

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
		// ...
		Assert.assertEquals(1, fsa.getAllFiles.size)
		val filename = MttfGenerator.MTTF_GEN_CONFIG_ID+"/metamodels/Class2Relational.atl.xmi"
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
	//(timeout = 10000)
	def testGeneratingCombinedMetaModelWithCopyingPluginToResource() {
		//copy files
		val src = "platform:/plugin/at.jku.weiner.mttf.tests/";
		val dst = "platform:/resource/my-test/";
		val project = EclipseUtilities.copyProject(src, dst, true);
		Assert.assertNotNull(project);
		Assert.assertTrue(project.exists());
		// ...
		val uriAsString = "platform:/resource/my-test/res/TestGeneratingCombinedMetaModelWithCopyingPluginToResource.mttf";
		val resource = ResourceSetUtils.loadResourceWithoutHandling(uriAsString);
		val model = resource.allContents.head
		validationHelper.assertNoErrors(model)
		val fsa = new EclipseResourceFileSystemAccess2();
		underTest.doGenerate(model.eResource, fsa, null)
		// ...
		val outputConfigurationName = MttfGenerator.MTTF_GEN_CONFIG_ID;
		val fileName = "metamodels/Class2Relational.atl.xmi";
		val isFile = fsa.isFile(fileName, outputConfigurationName);
		Assert.assertTrue(isFile);
		val actualFileContent = fsa.readTextFile(fileName, outputConfigurationName);
		val expectedFileContent = 
			'''
            public class Alice {
                 
            }
			''';
		Assert. assertEquals(expectedFileContent.toString, actualFileContent.toString);
	}
	
	def void printFilesInFileSystem(InMemoryFileSystemAccess fsa) {
		val keyset = fsa.getAllFiles().keySet
		for (var i = 0; i < keyset.size; i++) {
			val key = keyset.get(i);
			println("key(" + i + ")='" + key + "'");
		}
	}
	
	def void printOutputs(EclipseResourceFileSystemAccess2 fsa) {
		val outputs = fsa.getOutputConfigurations();
		val outputKeys = outputs.keySet;
		for (var i = 0; i < outputKeys.length(); i++) {
			val key = outputKeys.get(i);
			val value = outputs.get(key);
			val output = value.outputDirectory;
			val createOut = value.createOutputDirectory;
			System.out.println("key='" + key + "', output='" + output + "', createOut='" + createOut + "'");
		}
	}
	
}