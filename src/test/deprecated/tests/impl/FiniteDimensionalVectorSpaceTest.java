package tests.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import definitions.structures.abstr.algebra.fields.impl.RealLine;
import definitions.structures.abstr.vectorspaces.vectors.Vector;
import definitions.structures.euclidean.Generator;
import definitions.structures.euclidean.vectorspaces.EuclideanSpace;
import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class FiniteDimensionalVectorSpaceTest {

	static EuclideanSpace space;

	static Vector a;
	static Vector b;
	static Vector c;
	static Vector d;

	static double ans1;
	static double ans2;
	static double ans3;
	static double ans4;

	@BeforeClass
	public static void setUpBeforeClass() throws Throwable {

		space = Generator.getInstance().getSpaceGenerator().getFiniteDimensionalVectorSpace(4);

		final List<Vector> genericBase = space.genericBaseToList();
		final List<Vector> system = new ArrayList<>();

		final Vector x1 = space.addition(genericBase.get(0), genericBase.get(1));
		final Vector x2 = space.addition(genericBase.get(1), genericBase.get(2));
		final Vector x3 = space.addition(genericBase.get(2), genericBase.get(3));
		final Vector x4 = space.addition(genericBase.get(3),
				space.stretch(genericBase.get(0), RealLine.getInstance().get(-1)));

		system.add(x1);
		system.add(x2);
		system.add(x3);
		system.add(x4);

		final List<Vector> newBase = space.getOrthonormalBase(system);

		a = newBase.get(0);
		b = newBase.get(1);
		c = newBase.get(2);
		d = newBase.get(3);

		ans1 = space.innerProduct(a, b).getDoubleValue();
		ans2 = space.innerProduct(b, c).getDoubleValue();
		ans3 = space.innerProduct(c, d).getDoubleValue();
		ans4 = space.innerProduct(a, d).getDoubleValue();

	}

	@Test
	public void normalized() throws Throwable {
		Assert.assertTrue(Math.abs(space.norm(a).getDoubleValue() - 1) < 1.e-5);
		Assert.assertTrue(Math.abs(space.norm(b).getDoubleValue() - 1) < 1.e-5);
		Assert.assertTrue(Math.abs(space.norm(c).getDoubleValue() - 1) < 1.e-5);
		Assert.assertTrue(Math.abs(space.norm(d).getDoubleValue() - 1) < 1.e-5);
	}

	@Test
	public void orthogonal() throws Throwable {
		Assert.assertTrue(Math.abs(ans1) < 1.e-5);
		Assert.assertTrue(Math.abs(ans2) < 1.e-5);
		Assert.assertTrue(Math.abs(ans3) < 1.e-5);
		Assert.assertTrue(Math.abs(ans4) < 1.e-5);
	}

}
