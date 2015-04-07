package junit.tests;

/**
 * Test class used in TestTestCaseClassLoader
 */
import junit.framework.*;
import junit.runner.*;

public class LoadedFromJar extends JunitAssert {
	public void verify() {
		verifyApplicationClassLoadedByTestLoader();
	}
	private boolean isTestCaseClassLoader(ClassLoader cl) {
		return (cl != null && cl.getClass().getName().equals(junit.runner.TestCaseClassLoader.class.getName()));
	}
	private void verifyApplicationClassLoadedByTestLoader() {
		junitAssert(isTestCaseClassLoader(getClass().getClassLoader()));
	} 
}