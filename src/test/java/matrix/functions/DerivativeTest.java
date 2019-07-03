package matrix.functions;

import org.junit.Assert;
import org.junit.BeforeClass;

import deprecated.math.matrix.IMatrix;
import deprecated.math.matrix.Matrix;
import deprecated.math.matrix.functions.IDerivative;
import deprecated.math.matrix.functions.IFunction;
import deprecated.math.matrix.regression.GradientMinimizer;
import deprecated.math.matrix.regression.IMinimizer;
//import deprecated.functions.Function;
//import deprecated.functions.IFunction;

public class DerivativeTest {

	static IFunction testfun;
	static IDerivative derivative;
	static IFunction val;
	static IMatrix mat;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		testfun = new deprecated.math.matrix.functions.Function() {
			@Override
			public IMatrix value(IMatrix input) throws Exception {
				return new Matrix(
						new double[][] { { Math.pow(input.getEntry(0, 0), 2) + Math.pow(input.getEntry(0, 1), 2) } });
			}
		};

		derivative = new deprecated.math.matrix.functions.Derivative(testfun);

		val = derivative.jacobi(new Matrix(new double[][] { { 1, -1 } }));

		mat = val.value(new Matrix(new double[][] { { 1, 3 } }));

		final IMinimizer min = new GradientMinimizer(1.e-5, 1.e-5);

		final IMatrix nul = min.find(testfun, new Matrix(new double[][] { { 1, 1 } }));

		System.out.println(nul.toString());

	}

	// @Test
	public void test() throws Exception {
		Assert.assertTrue(Math.abs(mat.getEntry(0, 0) + 4) < (1.e3 * IDerivative.eps));
	}

}
