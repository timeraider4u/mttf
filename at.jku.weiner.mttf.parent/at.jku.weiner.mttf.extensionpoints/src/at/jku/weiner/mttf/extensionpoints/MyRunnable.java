package at.jku.weiner.mttf.extensionpoints;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.ISafeRunnable;

public class MyRunnable implements ISafeRunnable {

	private final ITransformation transformation;

	private final String uriAsString;
	public List<String> fileExtensions;

	public boolean isValid;

	public MyRunnable(final ITransformation transformation,
			final String uriAsString) {
		this.transformation = transformation;
		this.uriAsString = uriAsString;
		this.fileExtensions = new ArrayList<String>();
		this.isValid = false;
	}

	@Override
	public void handleException(final Throwable ex) {
		ex.printStackTrace();
	}

	@Override
	public void run() throws Exception {
		if (this.uriAsString == null) {
			this.fileExtensions = this.transformation
					.isRegisteredForFileExtensions();
		} else {
			this.isValid = this.transformation
					.isValidTransformation(this.uriAsString);
		}
	}
}
