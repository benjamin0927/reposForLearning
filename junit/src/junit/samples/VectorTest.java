package junit.samples;

import junit.framework.*;
import java.util.Vector;

/**
 * A sample test case, testing <code>java.util.Vector</code>.
 *
 */
public class VectorTest extends TestCase {
	protected Vector fEmpty;
	protected Vector fFull;

	public VectorTest(String name) {
		super(name);
	}
	public static void main (String[] args) {
		junit.textui.TestRunner.run (suite());
	}
	protected void setUp() {
		fEmpty= new Vector();
		fFull= new Vector();
		fFull.addElement(new Integer(1));
		fFull.addElement(new Integer(2));
		fFull.addElement(new Integer(3));
	}
	public static Test suite() {
		return new TestSuite(VectorTest.class);
	}
	public void testCapacity() {
		int size= fFull.size(); 
		for (int i= 0; i < 100; i++)
			fFull.addElement(new Integer(i));
		junitAssert(fFull.size() == 100+size);
	}
	public void testClone() {
		Vector clone= (Vector)fFull.clone(); 
		junitAssert(clone.size() == fFull.size());
		junitAssert(clone.contains(new Integer(1)));
	}
	public void testContains() {
		junitAssert(fFull.contains(new Integer(1)));  
		junitAssert(!fEmpty.contains(new Integer(1)));
	}
	public void testElementAt() {
		Integer i= (Integer)fFull.elementAt(0);
		junitAssert(i.intValue() == 1);

		try { 
			Integer j= (Integer)fFull.elementAt(fFull.size());
		} catch (ArrayIndexOutOfBoundsException e) {
			return;
		}
		junitFail("Should raise an ArrayIndexOutOfBoundsException");
	}
	public void testRemoveAll() {
		fFull.removeAllElements();
		fEmpty.removeAllElements();
		junitAssert(fFull.isEmpty());
		junitAssert(fEmpty.isEmpty()); 
	}
	public void testRemoveElement() {
		fFull.removeElement(new Integer(3));
		junitAssert(!fFull.contains(new Integer(3)) ); 
	}
}