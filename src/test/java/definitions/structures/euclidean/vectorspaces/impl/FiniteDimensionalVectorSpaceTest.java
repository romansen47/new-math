package definitions.structures.euclidean.vectorspaces.impl;

import org.junit.Assert;
import org.junit.Test;

import definitions.structures.abstr.vectorspaces.VectorSpace;
import definitions.structures.abstr.vectorspaces.VectorSpaceMethods;
import definitions.structures.abstr.vectorspaces.vectors.Vector;

/**
 * @author RoManski
 *
 */

public class FiniteDimensionalVectorSpaceTest {

	final int dim = 2;
	
	final VectorSpace space = SpaceGenerator.getInstance().getFiniteDimensionalVectorSpace(this.dim);
	
	final Vector nul = ((VectorSpaceMethods) this.space).nullVec();
	
	private final double factor = 1.e11;

	static FiniteDimensionalVectorSpaceTest test;

	public static void main(String[] args) {
		test = new FiniteDimensionalVectorSpaceTest();
		test.testContains();
		test.testNullVec();
		test.testAdd();
		test.testStretch();
//		Traced.show();
	}

	/**
	 * Test method for
	 * {@link definitions.structures.abstr.vectorspaces.VectorSpace#contains(definitions.structures.abstr.vectorspaces.vectors.Vector)}.
	 */
	@Test
	@settings.Trace(trace = settings.GlobalSettings.LOGGING, depth = settings.GlobalSettings.LOGGING_DEPTH, initial = true, transit = true)
	public void testContains() {
		Assert.assertTrue(((VectorSpaceMethods) this.space).contains(this.nul));
	}

	/**
	 * Test method for
	 * {@link definitions.structures.abstr.vectorspaces.VectorSpace#nullVec()}.
	 */
	@Test
	@settings.Trace(trace = settings.GlobalSettings.LOGGING, depth = settings.GlobalSettings.LOGGING_DEPTH, initial = true, transit = true)
	public void testNullVec() {
		Assert.assertTrue(((VectorSpaceMethods) this.space).nullVec().equals(this.nul));
	}

	/**
	 * Test method for
	 * {@link definitions.structures.abstr.vectorspaces.VectorSpace#add(definitions.structures.abstr.vectorspaces.vectors.Vector, definitions.structures.abstr.vectorspaces.vectors.Vector)}.
	 */
	@Test
	@settings.Trace(trace = settings.GlobalSettings.LOGGING, depth = settings.GlobalSettings.LOGGING_DEPTH, initial = true, transit = true)
	public void testAdd() {
		Assert.assertTrue(this.space.add(((VectorSpaceMethods) this.space).nullVec(), this.nul).equals(this.nul));
	}

	/**
	 * Test method for
	 * {@link definitions.structures.abstr.vectorspaces.VectorSpace#stretch(definitions.structures.abstr.vectorspaces.vectors.Vector, double)}.
	 */
	@Test
	@settings.Trace(trace = settings.GlobalSettings.LOGGING, depth = settings.GlobalSettings.LOGGING_DEPTH, initial = true, transit = true)
	public void testStretch() {
		Assert.assertTrue(this.space.stretch(this.nul, this.space.getField().get(this.factor)).equals(this.nul));
	}

}