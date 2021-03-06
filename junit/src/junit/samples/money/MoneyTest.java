package junit.samples.money;

import junit.framework.*;

public class MoneyTest extends TestCase {
	private Money f12CHF;
	private Money f14CHF;
	private Money f7USD;
	private Money f21USD;

	private MoneyBag fMB1;
	private MoneyBag fMB2;

	public MoneyTest(String name) {
		super(name);
	}
	public static void main(String args[]) {
		junit.textui.TestRunner.run(MoneyTest.class);
	}
	protected void setUp() {
		f12CHF= new Money(12, "CHF");
		f14CHF= new Money(14, "CHF");
		f7USD= new Money( 7, "USD");
		f21USD= new Money(21, "USD");

		fMB1= new MoneyBag(f12CHF, f7USD);
		fMB2= new MoneyBag(f14CHF, f21USD);
	}
	public void testBagMultiply() {
		// {[12 CHF][7 USD]} *2 == {[24 CHF][14 USD]}
		Money bag[]= { new Money(24, "CHF"), new Money(14, "USD") };
		MoneyBag expected= new MoneyBag(bag);
		junitAssertEquals(expected, fMB1.multiply(2)); 
		junitAssert(fMB1.multiply(0).isZero());
	}
	public void testBagNegate() {
		// {[12 CHF][7 USD]} negate == {[-12 CHF][-7 USD]}
		Money bag[]= { new Money(-12, "CHF"), new Money(-7, "USD") };
		MoneyBag expected= new MoneyBag(bag);
		junitAssertEquals(expected, fMB1.negate());
	}
	public void testBagSimpleAdd() {
		// {[12 CHF][7 USD]} + [14 CHF] == {[26 CHF][7 USD]}
		Money bag[]= { new Money(26, "CHF"), new Money(7, "USD") };
		MoneyBag expected= new MoneyBag(bag);
		junitAssertEquals(expected, fMB1.add(f14CHF));
	}
	public void testBagSubtract() {
		// {[12 CHF][7 USD]} - {[14 CHF][21 USD] == {[-2 CHF][-14 USD]}
		Money bag[]= { new Money(-2, "CHF"), new Money(-14, "USD") };
		MoneyBag expected= new MoneyBag(bag);
		junitAssertEquals(expected, fMB1.subtract(fMB2));
	}
	public void testBagSumAdd() {
		// {[12 CHF][7 USD]} + {[14 CHF][21 USD]} == {[26 CHF][28 USD]}
		Money bag[]= { new Money(26, "CHF"), new Money(28, "USD") };
		MoneyBag expected= new MoneyBag(bag);
		junitAssertEquals(expected, fMB1.add(fMB2));
	}
	public void testIsNull() {
		junitAssert(fMB1.subtract(fMB1).isZero()); 
	}
	public void testMixedSimpleAdd() {
		// [12 CHF] + [7 USD] == {[12 CHF][7 USD]}
		Money bag[]= { f12CHF, f7USD };
		MoneyBag expected= new MoneyBag(bag);
		junitAssertEquals(expected, f12CHF.add(f7USD));
	}
	public void testMoneyBagEquals() {
		junitAssert(!fMB1.equals(null)); 

		junitAssertEquals(fMB1, fMB1);
		MoneyBag equal= new MoneyBag(new Money(12, "CHF"), new Money(7, "USD"));
		junitAssert(fMB1.equals(equal));
		junitAssert(!fMB1.equals(f12CHF));
		junitAssert(!f12CHF.equals(fMB1));
		junitAssert(!fMB1.equals(fMB2));
	}
	public void testMoneyBagHash() {
		MoneyBag equal= new MoneyBag(new Money(12, "CHF"), new Money(7, "USD"));
		junitAssertEquals(fMB1.hashCode(), equal.hashCode());
	}
	public void testMoneyEquals() {
		junitAssert(!f12CHF.equals(null)); 
		Money equalMoney= new Money(12, "CHF");
		junitAssertEquals(f12CHF, f12CHF);
		junitAssertEquals(f12CHF, equalMoney);
		junitAssertEquals(f12CHF.hashCode(), equalMoney.hashCode());
		junitAssert(!f12CHF.equals(f14CHF));
	}
	public void testMoneyHash() {
		junitAssert(!f12CHF.equals(null)); 
		Money equal= new Money(12, "CHF");
		junitAssertEquals(f12CHF.hashCode(), equal.hashCode());
	}
	public void testNormalize() {
		Money bag[]= { new Money(26, "CHF"), new Money(28, "CHF"), new Money(6, "CHF") };
		MoneyBag moneyBag= new MoneyBag(bag);
		Money expected[]= { new Money(60, "CHF") };
		// note: expected is still a MoneyBag
		MoneyBag expectedBag= new MoneyBag(expected);
		junitAssertEquals(expectedBag, moneyBag);
	}
	public void testNormalize2() {
		// {[12 CHF][7 USD]} - [12 CHF] == [7 USD]
		Money expected= new Money(7, "USD");
		junitAssertEquals(expected, fMB1.subtract(f12CHF));
	}
	public void testNormalize3() {
		// {[12 CHF][7 USD]} - {[12 CHF][3 USD]} == [4 USD]
		Money s1[]= { new Money(12, "CHF"), new Money(3, "USD") };
		MoneyBag ms1= new MoneyBag(s1);
		Money expected= new Money(4, "USD");
		junitAssertEquals(expected, fMB1.subtract(ms1));
	}
	public void testNormalize4() {
		// [12 CHF] - {[12 CHF][3 USD]} == [-3 USD]
		Money s1[]= { new Money(12, "CHF"), new Money(3, "USD") };
		MoneyBag ms1= new MoneyBag(s1);
		Money expected= new Money(-3, "USD");
		junitAssertEquals(expected, f12CHF.subtract(ms1));
	}
	public void testPrint() {
		junitAssertEquals("[12 CHF]", f12CHF.toString());
	}
	public void testSimpleAdd() {
		// [12 CHF] + [14 CHF] == [26 CHF]
		Money expected= new Money(26, "CHF");
		junitAssertEquals(expected, f12CHF.add(f14CHF));
	}
	public void testSimpleBagAdd() {
		// [14 CHF] + {[12 CHF][7 USD]} == {[26 CHF][7 USD]}
		Money bag[]= { new Money(26, "CHF"), new Money(7, "USD") };
		MoneyBag expected= new MoneyBag(bag);
		junitAssertEquals(expected, f14CHF.add(fMB1));
	}
	public void testSimpleMultiply() {
		// [14 CHF] *2 == [28 CHF]
		Money expected= new Money(28, "CHF");
		junitAssertEquals(expected, f14CHF.multiply(2));
	}
	public void testSimpleNegate() {
		// [14 CHF] negate == [-14 CHF]
		Money expected= new Money(-14, "CHF");
		junitAssertEquals(expected, f14CHF.negate());
	}
	public void testSimpleSubtract() {
		// [14 CHF] - [12 CHF] == [2 CHF]
		Money expected= new Money(2, "CHF");
		junitAssertEquals(expected, f14CHF.subtract(f12CHF));
	}
}