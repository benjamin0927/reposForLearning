package junit.framework;

/**
 * A set of assert methods.
 */

public class JunitAssert {
	/**
	 * Protect constructor since it is a static only class
	 */
	protected JunitAssert() {
	}
	/**
	 * Asserts that a condition is true. If it isn't it throws
	 * an AssertionFailedError with the given message.
	 */
	static public void junitAssert(String message, boolean condition) {
		if (!condition)
			junitFail(message);
	}
	/**
	 * Asserts that a condition is true. If it isn't it throws
	 * an AssertionFailedError.
	 */
	static public void junitAssert(boolean condition) {
		junitAssert(null, condition);
	}
	/**
	 * Asserts that two doubles are equal.
	 * @param expected the expected value of an object
	 * @param actual the actual value of an object
	 * @param delta tolerated delta
	 */
	static public void junitAssertEquals(double expected, double actual, double delta) {
	    junitAssertEquals(null, expected, actual, delta);
	}
	/**
	 * Asserts that two longs are equal.
	 * @param expected the expected value of an object
	 * @param actual the actual value of an object
	 */
	static public void junitAssertEquals(long expected, long actual) {
	    junitAssertEquals(null, expected, actual);
	}
	/**
	 * Asserts that two objects are equal. If they are not
	 * an AssertionFailedError is thrown.
	 * @param expected the expected value of an object
	 * @param actual the actual value of an object
	 */
	static public void junitAssertEquals(Object expected, Object actual) {
	    junitAssertEquals(null, expected, actual);
	}
	/**
	 * Asserts that two doubles are equal.
	 * @param message the detail message for this assertion
	 * @param expected the expected value of an object
	 * @param actual the actual value of an object
	 * @param delta tolerated delta
	 */
	static public void junitAssertEquals(String message, double expected, double actual, double delta) {
		if (!(Math.abs(expected-actual) <= delta)) // Because comparison with NaN always returns false
			junitFailNotEquals(message, new Double(expected), new Double(actual));
	}

	/**
	 * Asserts that two longs are equal.
	 * @param message the detail message for this assertion
	 * @param expected the expected value of an object
	 * @param actual the actual value of an object
	 */
	static public void junitAssertEquals(String message, long expected, long actual) {
	    junitAssertEquals(message, new Long(expected), new Long(actual));
	}
	/**
	 * Asserts that two objects are equal. If they are not
	 * an AssertionFailedError is thrown.
	 * @param message the detail message for this assertion
	 * @param expected the expected value of an object
	 * @param actual the actual value of an object
	 */
	static public void junitAssertEquals(String message, Object expected, Object actual) {
		if (expected == null && actual == null)
			return;
		if (expected != null && expected.equals(actual))
			return;
		junitFailNotEquals(message, expected, actual);
	}
	/**
	 * Asserts that an object isn't null.
	 */
	static public void assertNotNull(Object object) {
		junitAssertNotNull(null, object);
	}
	/**
	 * Asserts that an object isn't null.
	 */
	static public void junitAssertNotNull(String message, Object object) {
		junitAssert(message, object != null); 
	}
	/**
	 * Asserts that an object is null.
	 */
	static public void junitAssertNull(Object object) {
		junitAssertNull(null, object);
	}
	/**
	 * Asserts that an object is null.
	 */
	static public void junitAssertNull(String message, Object object) {
		junitAssert(message, object == null); 
	}
	/**
	 * Asserts that two objects refer to the same object. If they are not
	 * the same an AssertionFailedError is thrown.
	 * @param expected the expected value of an object
	 * @param actual the actual value of an object
	 */
	static public void junitAssertSame(Object expected, Object actual) {
	    junitAssertSame(null, expected, actual);
	}
	/**
	 * Asserts that two objects refer to the same object. If they are not
	 * an AssertionFailedError is thrown.
	 * @param message the detail message for this assertion
	 * @param expected the expected value of an object
	 * @param actual the actual value of an object
	 */
	static public void junitAssertSame(String message, Object expected, Object actual) {
		if (expected == actual)
			return;
		junitFailNotSame(message, expected, actual);
	}
	/**
	 * Fails a test with no message. 
	 */
	static public void fail() {
		junitFail(null);
	}
	/**
	 * Fails a test with the given message. 
	 */
	static public void junitFail(String message) {
		throw new AssertionFailedError(message);
	}
	static private void junitFailNotEquals(String message, Object expected, Object actual) {
		String formatted= "";
		if (message != null)
			formatted= message+" ";
		junitFail(formatted+"expected:<"+expected+"> but was:<"+actual+">");
	}
	static private void junitFailNotSame(String message, Object expected, Object actual) {
		String formatted= "";
		if (message != null)
			formatted= message+" ";
		junitFail(formatted+"expected same");
	}
}