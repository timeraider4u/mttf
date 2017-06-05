package at.jku.weiner.mttf.extensions.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import at.jku.weiner.mttf.extensionpoints.ITransformation;
import at.jku.weiner.mttf.utils.UriUtils;

public class ATLTransformation implements ITransformation {

	public static final String ATL_FILE_EXT = ".atl";

	@Override
	public boolean isValidTransformation(final String uriAsString) {
		final File file = UriUtils.getFileFor(uriAsString);
		final boolean result = (file.exists() && file.canRead());
		// System.out.println("file.exists='" + file.exists() + "'");
		return result;
	}

	@Override
	public List<String> isRegisteredForFileExtensions() {
		final List<String> result = new ArrayList<String>();
		result.add(ATLTransformation.ATL_FILE_EXT);
		return result;
	}

}
