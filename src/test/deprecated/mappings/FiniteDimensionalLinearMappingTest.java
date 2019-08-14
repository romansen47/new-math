package mappings;

import org.junit.BeforeClass;
import org.junit.Test;

import definitions.structures.abstr.fields.impl.RealLine;
import definitions.structures.abstr.fields.scalars.Scalar;
import definitions.structures.abstr.fields.scalars.impl.Real;
import definitions.structures.abstr.mappings.Automorphism;
import definitions.structures.abstr.mappings.Homomorphism;
import definitions.structures.abstr.mappings.Isomorphism;
import definitions.structures.abstr.vectorspaces.Algebra;
import definitions.structures.euclidean.mappings.FiniteDimensionalHomomorphism;
import definitions.structures.euclidean.mappings.impl.MappingGenerator;
import definitions.structures.euclidean.vectors.FiniteVector;
import definitions.structures.euclidean.vectors.impl.Tuple;
import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class FiniteDimensionalLinearMappingTest {

	final static Algebra realLine = RealLine.getInstance();
	static FiniteVector e1;
	static FiniteVector e2;
	static FiniteVector e3;
	static Homomorphism map;
	static Homomorphism inv;
	static Homomorphism composition;

	static Scalar[][] matrix = new Scalar[][] {
			{ ((RealLine) realLine).getOne(), (Scalar) realLine.nullVec(), ((RealLine) realLine).getOne() },
			{ (Scalar) realLine.nullVec(), ((RealLine) realLine).getOne(), (Scalar) realLine.nullVec() },
			{ new Real(-1), (Scalar) realLine.nullVec(), ((RealLine) realLine).getOne() } };

	static FiniteDimensionalHomomorphism product;

	@BeforeClass
	public static void setUpBeforeClass() throws Throwable {

		e1 = new Tuple(new Scalar[] { ((RealLine) realLine).getOne(), (Scalar) realLine.nullVec(),
				(Scalar) realLine.nullVec() });
		e2 = new Tuple(new Scalar[] { (Scalar) realLine.nullVec(), ((RealLine) realLine).getOne(),
				(Scalar) realLine.nullVec() });
		e3 = new Tuple(new Scalar[] { (Scalar) realLine.nullVec(), (Scalar) realLine.nullVec(),
				((RealLine) realLine).getOne() });

		map = MappingGenerator.getInstance().getFiniteDimensionalLinearMapping(matrix);
		inv = ((Automorphism) map).getInverse();

		composition = MappingGenerator.getInstance().getComposition(map, inv);

	}

	@Test
	public void first() throws Throwable {
		Assert.assertTrue(composition.get(e1).equals(e1));
	}

	@Test
	public void second() throws Throwable {
		Assert.assertTrue(composition.get(e2).equals(e2));
	}

	@Test
	public void third() throws Throwable {
		Assert.assertTrue(composition.get(e3).equals(e3));
	}

	@Test
	public void iso1() throws Throwable {
		Assert.assertTrue(map instanceof Isomorphism);
	}

	@Test
	public void iso2() throws Throwable {
		Assert.assertTrue(inv instanceof Isomorphism);
	}

	@Test
	public void iso3() throws Throwable {
		Assert.assertTrue(composition instanceof Isomorphism);
	}
}
