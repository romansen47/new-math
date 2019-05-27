package definitions.structures.generic.finitedimensional.defs.subspaces.functionalspaces;

import org.junit.BeforeClass;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import definitions.structures.abstr.Vector;
import definitions.structures.generic.finitedimensional.defs.spaces.EuclideanSpace;
import definitions.structures.generic.finitedimensional.defs.spaces.impl.SpaceGenerator;
import definitions.structures.generic.finitedimensional.defs.vectors.ExponentialFunction;
import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class OrthonormalizationTest {

	final double eps=1.e-3;
	
	static EuclideanSpace space;
	static EuclideanSpace space2;

	static Vector a;
	static Vector b;
	static Vector c;
	
	static double ans1;
	static double ans2;
	static double ans3;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Throwable {

		space = SpaceGenerator.getInstance().getTrigonometricSpace(1);

		List<Vector> genericBase = space.genericBaseToList();
		List<Vector> system = new ArrayList<>();

		Vector x1 = space.add(genericBase.get(0), genericBase.get(1));
		Vector x2 = space.add(genericBase.get(1), genericBase.get(2));
		Vector x3 = space.add(genericBase.get(2), space.stretch(genericBase.get(0),2));
		
		system.add(x1);
		system.add(x2);
		system.add(x3);

		List<Vector> newBase = space.getOrthonormalBase(system);

		a = newBase.get(0);
		b = newBase.get(1);
		c = newBase.get(2);

		ans1 = space.product(a, b);
		ans2 = space.product(b, c);
		ans3 = space.product(c, a);
		
	}

	@Test
	public void orthogonal() throws Throwable {
		Assert.assertTrue(Math.abs(ans1) < eps);
		Assert.assertTrue(Math.abs(ans2) < eps);
		Assert.assertTrue(Math.abs(ans3) < eps);
	}

	@Test
	public void normalized() throws Throwable {
		Assert.assertTrue(Math.abs(space.norm(a) - 1) < eps);
		Assert.assertTrue(Math.abs(space.norm(b) - 1) < eps);
		Assert.assertTrue(Math.abs(space.norm(c) - 1) < eps);
	}

	@Test
	public void exponential() throws Throwable {
		Vector exp=new ExponentialFunction(new double[] {1,0,0});
		Vector x=space2.getCoordinates(exp);
		double y=(Math.exp(Math.PI)-Math.exp(-Math.PI))/Math.sqrt(2*Math.PI);
		Assert.assertTrue(Math.abs(x.getGenericCoordinates()[0]-y) < eps);
	}
}
