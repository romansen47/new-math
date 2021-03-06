/**
* 
*/
package definitions.structures.abstr.fields.impl;

import org.junit.Assert;
import org.junit.Test;

import definitions.structures.abstr.VectorSpaceTest;
import definitions.structures.abstr.algebra.fields.impl.ComplexPlane;
import definitions.structures.abstr.algebra.fields.impl.RealLine;
import definitions.structures.abstr.algebra.fields.scalars.Scalar;
import definitions.structures.abstr.algebra.fields.scalars.impl.Complex;
import definitions.structures.abstr.vectorspaces.VectorSpace;
import definitions.structures.abstr.vectorspaces.vectors.Vector;

/**
 * @author ro
 *
 */
public class ComplexPlaneTest extends VectorSpaceTest {

	final ComplexPlane complexPlane = (ComplexPlane) ComplexPlane.getInstance();
	final Vector one = this.complexPlane.getOne();
	final Vector im = this.complexPlane.getI();

	Vector reMin = new Complex(-1, 0);
	Vector imMin = new Complex(0, -1);

	@Override
	public Scalar getFactor() {
		return RealLine.getInstance().get(2);
	}

	@Override
	public VectorSpace getSpace() {
		return this.complexPlane;
	}

	@Override
	public Vector getVec1() {
		return this.one;
	}

	@Override
	public Vector getVec2() {
		return this.im;
	}

	/**
	 * Test method for
	 * {@link definitions.structures.abstr.algebra.fields.impl.ComplexPlane#addition(definitions.structures.abstr.vectorspaces.vectors.Vector, definitions.structures.abstr.vectorspaces.vectors.Vector)}.
	 */
	@Test

	public void testAdd() {
		Assert.assertTrue(this.complexPlane.addition(this.one, this.reMin).equals(this.complexPlane.nullVec()));
		Assert.assertTrue(this.complexPlane.addition(this.im, this.imMin).equals(this.complexPlane.nullVec()));
	}

	/**
	 * Test method for
	 * {@link definitions.structures.abstr.algebra.fields.impl.ComplexPlane#getI()}.
	 */
	@Test

	public void testGetI() {
		Assert.assertTrue(this.complexPlane.getI().equals(new Complex(0, 1)));
	}

	/**
	 * Test method for
	 * {@link definitions.structures.abstr.algebra.fields.impl.ComplexPlane#getOne()}.
	 */
	@Test

	public void testGetOne() {
		Assert.assertTrue(this.complexPlane.getOne().equals(new Complex(1, 0)));
	}

	/**
	 * Test method for
	 * {@link definitions.structures.abstr.algebra.fields.impl.ComplexPlane#getMultiplicativeInverseElement(definitions.structures.abstr.vectorspaces.vectors.Vector)}.
	 */
	@Test

	public void testInverse() {
		Assert.assertTrue(this.complexPlane.getMultiplicativeInverseElement(this.im).equals(this.imMin));
	}

	/**
	 * Test method for
	 * {@link definitions.structures.abstr.algebra.fields.impl.ComplexPlane#nullVec()}.
	 */
	@Test

	public void testNullVec() {
		boolean ans = true;
		final Complex nul = this.complexPlane.nullVec();
		if (nul.getReal().getDoubleValue() != 0.0 || nul.getImag().getDoubleValue() != 0.0) {
			ans = false;
		}
		Assert.assertTrue(ans);
	}

	/**
	 * Test method for
	 * {@link definitions.structures.abstr.algebra.fields.impl.ComplexPlane#product(definitions.structures.abstr.vectorspaces.vectors.Vector, definitions.structures.abstr.vectorspaces.vectors.Vector)}.
	 */
	@Test

	public void testProduct() {
		Assert.assertTrue(this.complexPlane.product(this.one, this.one).equals(this.one));
		Assert.assertTrue(this.complexPlane.product(this.one, this.im).equals(this.im));
		Assert.assertTrue(this.complexPlane.product(this.im, this.one).equals(this.im));
		Assert.assertTrue(this.complexPlane.product(this.im, this.im).equals(new Complex(-1, 0)));
	}

	/**
	 * Test method for
	 * {@link definitions.structures.abstr.algebra.fields.impl.ComplexPlane#stretch(definitions.structures.abstr.vectorspaces.vectors.Vector, definitions.structures.abstr.Scalar)}.
	 */
	@Test

	public void testStretch() {
		Assert.assertTrue(this.complexPlane.stretch(this.one, RealLine.getInstance().get(5)).equals(new Complex(5, 0)));
		Assert.assertTrue(
				this.complexPlane.stretch(this.im, RealLine.getInstance().get(-5)).equals(new Complex(0, -5)));
		Assert.assertTrue(this.complexPlane.stretch(this.one, RealLine.getInstance().getZero())
				.equals(this.complexPlane.nullVec()));
	}

}
