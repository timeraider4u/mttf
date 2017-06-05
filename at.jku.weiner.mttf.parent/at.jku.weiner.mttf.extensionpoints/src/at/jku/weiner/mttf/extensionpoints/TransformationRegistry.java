package at.jku.weiner.mttf.extensionpoints;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;

public class TransformationRegistry {

	private TransformationRegistry() {

	}

	private static final String ITRANSFORMATION_ID = "at.jku.weiner.mttf.extensionpoints.ITransformation";
	
	public static boolean isValidTransformation(final String uriAsString) {
		final List<ITransformation> transformations = TransformationRegistry
				.getITransformations();
		for (final ITransformation transformation : transformations) {
			final List<String> extensions = TransformationRegistry
					.getExtensionsFor(transformation);
			for (final String extension : extensions) {
				final boolean endsWith = uriAsString.endsWith(extension);
				if (endsWith) {
					final boolean temp = TransformationRegistry
							.isValidFor(transformation, uriAsString);
					if (temp) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static List<ITransformation> getITransformations() {
		final List<ITransformation> result = new ArrayList<ITransformation>();
		final IExtensionRegistry registry = Platform.getExtensionRegistry();
		final IConfigurationElement[] config = registry
				.getConfigurationElementsFor(
						TransformationRegistry.ITRANSFORMATION_ID);
		try {
			for (final IConfigurationElement element : config) {
				System.out.println("Evaluating extension");
				final Object object = element
						.createExecutableExtension("class");
				if (object instanceof ITransformation) {
					final ITransformation transformation = (ITransformation) object;
					result.add(transformation);
				}
			}
		} catch (final CoreException ex) {
			ex.printStackTrace();
		}
		return result;
	}
	
	public static List<String> getExtensionsFor(
			final ITransformation transformation) {
		final MyRunnable runnable = new MyRunnable(transformation, null);
		SafeRunner.run(runnable);
		return runnable.fileExtensions;
	}

	public static boolean isValidFor(final ITransformation transformation,
			final String uriAsString) {
		final MyRunnable runnable = new MyRunnable(transformation, uriAsString);
		SafeRunner.run(runnable);
		return runnable.isValid;
	}
	
}
