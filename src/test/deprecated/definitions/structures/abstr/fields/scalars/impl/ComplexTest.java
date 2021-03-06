/**
 * 
 */
package definitions.structures.abstr.fields.scalars.impl;

import org.junit.Assert;
import org.junit.Test;

import definitions.structures.abstr.algebra.fields.impl.ComplexPlane;
import definitions.structures.abstr.algebra.fields.scalars.Scalar;
import definitions.structures.abstr.algebra.fields.scalars.impl.Complex;
import definitions.structures.abstr.vectorspaces.vectors.Vector;
import definitions.structures.abstr.vectorspaces.vectors.VectorTest;
import definitions.structures.euclidean.vectorspaces.EuclideanSpace;

/**
 * @author RoManski
 *
 */
public class ComplexTest extends VectorTest {

	@Override
	public EuclideanSpace getSpace() {
		return ComplexPlane.getInstance();
	}

	@Override
	public Vector getVector() {
		return new Complex(1, 1);
	}

	/**
	 * Test method for
	 * {@link definitions.structures.abstr.algebra.fields.scalars.impl.Complex#getInverse()}.
	 */
	@Test

	public final void testGetInverse() {
		Assert.assertTrue(((Scalar) this.getVector()).getInverse().equals(new Complex(0.5, -0.5)));
	}

}
