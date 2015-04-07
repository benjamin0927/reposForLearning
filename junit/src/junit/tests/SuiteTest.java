package junit.tests;

import java.util.Vector;
import junit.framework.*;

/**
 * A fixture for testing the "auto" test suite feature.
 *
 */
public class SuiteTest extends TestCase {
	protected TestResult fResult;
	public SuiteTest(String name) {
		super(name);
	}
	protected void setUp() {
		fResult= new TestResult();
	}
	public static Test suite() {
		TestSuite suite= new TestSuite("Suite Tests");
		// build the suite manually
		suite.addTest(new SuiteTest("testNoTestCaseClass"));
		suite.addTest(new SuiteTest("testNoTestCases"));
		suite.addTest(new SuiteTest("testOneTestCase"));
		suite.addTest(new SuiteTest("testNotPublicTestCase"));
		suite.addTest(new SuiteTest("testNotVoidTestCase"));
		suite.addTest(new SuiteTest("testNotExistingTestCase"));
		suite.addTest(new SuiteTest("testInheritedTests"));
		suite.addTest(new SuiteTest("testShadowedTests"));
		
		return suite;
	}
	public void testInheritedTests() {
		TestSuite suite= new TestSuite(InheritedTestCase.class);
		suite.run(fResult);
		junitAssert(fResult.wasSuccessful());
		junitAssertEquals(2, fResult.runCount());
	}
	public void testNoTestCaseClass() {
		Test t= new TestSuite(NoTestCaseClass.class);
		t.run(fResult);
		junitAssertEquals(1, fResult.runCount());  // warning test
		junitAssert(! fResult.wasSuccessful());
	}
	public void testNoTestCases() {
		Test t= new TestSuite(NoTestCases.class);
		t.run(fResult);
		junitAssert(fResult.runCount() == 1);  // warning test
		junitAssert(fResult.failureCount() == 1);
		junitAssert(! fResult.wasSuccessful());
	}
	public void testNotExistingTestCase() {
		Test t= new SuiteTest("notExistingMethod");
		t.run(fResult);
		junitAssert(fResult.runCount() == 1);  
		junitAssert(fResult.failureCount() == 1);
		junitAssert(fResult.errorCount() == 0);
	}
	public void testNotPublicTestCase() {
		TestSuite suite= new TestSuite(NotPublicTestCase.class);
		// 1 public test case + 1 warning for the non-public test case
		junitAssertEquals(2, suite.countTestCases());
	}
	public void testNotVoidTestCase() {
		TestSuite suite= new TestSuite(NotVoidTestCase.class);
		junitAssert(suite.countTestCases() == 1);
	}
	public void testOneTestCase() {
		Test t= new TestSuite(OneTestCase.class);
		t.run(fResult);
		junitAssert(fResult.runCount() == 1);  
		junitAssert(fResult.failureCount() == 0);
		junitAssert(fResult.errorCount() == 0);
		junitAssert(fResult.wasSuccessful());
	}
	public void testShadowedTests() {
		TestSuite suite= new TestSuite(OverrideTestCase.class);
		suite.run(fResult);
		junitAssertEquals(1, fResult.runCount());
	}
}