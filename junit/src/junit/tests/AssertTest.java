package junit.tests;

import junit.framework.*;

public class AssertTest extends TestCase {
	public AssertTest(String name) {
		super(name);
	}
	
	/**
  	 * Test for the special Double.NaN value.
  	 */
 	public void testAssertEqualsNaNFails() {
 		try {
 			junitAssertEquals(1.234, Double.NaN, 0.00001);
 		} catch (AssertionFailedError e) {
 			return;
  		}
  		junitFail();
 	}

 	public void testAssertNaNEqualsFails() {
 		try {
 			junitAssertEquals(Double.NaN, 1.234, 0.00001);
 		} catch (AssertionFailedError e) {
 			return;
  		}
  		junitFail();
 	}

 	public void testAssertNaNEqualsNaNFails() {
 		try {
 			junitAssertEquals(Double.NaN, Double.NaN, 0.00001);
 		} catch (AssertionFailedError e) {
 			return;
  		}
  		junitFail();
 	}

	public void testAssertEquals() {
		Object o= new Object();
		junitAssertEquals(o, o);
	}
	
	public void testAssertEqualsNull() {
		junitAssertEquals(null, null);
	}
	
	public void testAssertNull() {
		junitAssertNull(null);
	}
	
	public void testAssertNullNotEqualsNull() {
 		try {
 			junitAssertEquals(null, new Object());
 		} catch (AssertionFailedError e) {
 			return;
  		}
  		junitFail();
	}
	
	public void testAssertSame() {
		Object o= new Object();
		junitAssertSame(o, o);
	}
	
	public void testAssertSameFails() {
 		try {
 			junitAssertSame(new Integer(1), new Integer(1));
    	} catch (AssertionFailedError e) {
 			return;
  		}
  		junitFail();
	}

	public void testFail() {
 		try {
     		junitFail();
    	} catch (AssertionFailedError e) {
 			return;
  		}
  		throw new AssertionFailedError(); // You can't call fail() here
	}
	
	public void testFailAssertNotNull() {
 		try {
     		junitAssertNotNull(null);
      	} catch (AssertionFailedError e) {
 			return;
  		}
  		junitFail();
	}
	
	public void testSucceedAssertNotNull() {
		junitAssertNotNull(new Object());
	}
}