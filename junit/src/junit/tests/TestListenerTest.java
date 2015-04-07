package junit.tests;

/**
 * Test class used in SuiteTest
 */
import junit.framework.*;

public class TestListenerTest extends TestCase implements TestListener {
	private TestResult fResult;
	private int fStartCount;
	private int fEndCount;
	private int fFailureCount;
	private int fErrorCount;
	public TestListenerTest(String name) {
		super(name);
	}
	public void addError(Test test, Throwable t) {
		fErrorCount++;
	}
	public void addFailure(Test test, Throwable t) {
		fFailureCount++;
	}
	public void endTest(Test test) {
		fEndCount++;
	}
	protected void setUp() {
		fResult= new TestResult();
		fResult.addListener(this);
	
		fStartCount= 0;
		fEndCount= 0;
		fFailureCount= 0;
	}
	public void startTest(Test test) {
		fStartCount++;
	}
	public void testError() {
		TestCase test= new TestCase("noop") {
			public void runTest() {
				throw new Error();
			}
		};
		test.run(fResult);
		junitAssertEquals(1, fErrorCount);
		junitAssertEquals(1, fEndCount);
	}
	public void testFailure() {
		TestCase test= new TestCase("noop") {
			public void runTest() {
				junitFail();
			}
		};
		test.run(fResult);
		junitAssertEquals(1, fFailureCount);
		junitAssertEquals(1, fEndCount);
	}
	public void testStartStop() {
		TestCase test= new TestCase("noop") {
			public void runTest() {
			}
		};
		test.run(fResult);
		junitAssertEquals(1, fStartCount);
		junitAssertEquals(1, fEndCount);
	}
}