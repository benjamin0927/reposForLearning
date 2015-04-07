package junit.tests;

/**
 * Test class used in TestTestCaseClassLoader
 */
import junit.framework.*;
import junit.runner.*;

public class ClassLoaderTest extends JunitAssert {
	public ClassLoaderTest() {
	}
	public void verify() {
		verifyApplicationClassLoadedByTestLoader();
		verifySystemClassNotLoadedByTestLoader();
	}
	private boolean isTestCaseClassLoader(ClassLoader cl) {
		return (cl != null && cl.getClass().getName().equals(junit.runner.TestCaseClassLoader.class.getName()));
	}
	private void verifyApplicationClassLoadedByTestLoader() {
		junitAssert(isTestCaseClassLoader(getClass().getClassLoader()));
	} 
	private void verifySystemClassNotLoadedByTestLoader() {
		junitAssert(!isTestCaseClassLoader(Object.class.getClassLoader()));
		junitAssert(!isTestCaseClassLoader(TestCase.class.getClassLoader()));
	}
}