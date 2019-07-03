package definitions.structures.generic.finitedimensional.defs.vectors;

import java.awt.Color;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import definitions.structures.abstr.Vector;
import definitions.structures.generic.finitedimensional.defs.Generator;
import definitions.structures.generic.finitedimensional.defs.spaces.EuclideanSpace;
import definitions.structures.generic.finitedimensional.defs.spaces.FunctionSpace;
import definitions.structures.generic.finitedimensional.defs.subspaces.functionspaces.EuclideanFunctionSpace;
import definitions.structures.generic.finitedimensional.defs.vectors.impl.FunctionTuple;
import definitions.structures.generic.finitedimensional.defs.vectors.impl.GenericFunction;
import deprecated.proprietary.StdDraw;

public interface Function extends Vector {

	double eps = 1.e-3;

	Function one = new GenericFunction() {
		@Override
		public double value(double input) {
			return 1.;
		}

		@Override
		public String toString() {
			return "1-function";
		}
	};

	double left = -1;

	double right = 1;

	default double getLeft() {
		return -Math.PI;
	}

	default double getRight() {
		return Math.PI;
	}

	double value(double input);

	default boolean equals(final Function other, final EuclideanFunctionSpace source) {
		final int n = 100;
		final double a = source.getInterval()[0];
		final double b = source.getInterval()[1];
		for (int i = 0; i < n; i++) {
			if (Math.abs(value(a + ((i * (b - a)) / 99.)) - other.value(a + ((i * (b - a)) / 99.))) > eps) {
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("unused")
	default void plot(final double left, final double right) {

		final int count = 1000;

		final double delta = (right - left) / count;
		double x = 0;
		double min = value((right - left) / 2.);
		double max = min;
		for (double i = 0; i < (count - 1); i += 1) {
			x = left + (delta * i);
			final double y = value(x);
			if (y > max) {
				max = y;
			}
			if (y < min) {
				min = y;
			}
		}
		final double d = max - min;
		if (delta == 0) {
			min = min - 100;
			max = max + 100;
		}
		final StdDraw stddraw = new StdDraw();
		stddraw.setCanvasSize(1000, 700);
		StdDraw.setXscale(left, right);
		StdDraw.setYscale(1.5 * min, 1.5 * max);

		double z = 0;
		StdDraw.setPenRadius(0.001);
		for (double i = 0; i < (count - 1); i += 1) {
			z = left + (delta * i);
			StdDraw.setPenColor(Color.black);
			StdDraw.line(z, value(z), z + delta, value(z + delta));
		}
	}

	default void plotCompare(final double left, final double right, final Function fun) {

		final int count = 500;
		final double delta = (right - left) / count;
		double x = 0;
		double min = value((right - left) / 2.);
		double max = min;

		for (double i = 0; i < (count - 1); i += 1) {
			x = left + (delta * i);
			final double y = value(x);
			if (y > max) {
				max = y;
			}
			if (y < min) {
				min = y;
			}
		}

		final double d = max - min;

		if (d == 0) {
			min = min - 50;
			max = max + 50;
		} else {
			min = min - (0.2 * d);
			max = max + (0.5 * d);
		}

		final StdDraw stddraw = new StdDraw();
		stddraw.setCanvasSize(1000, 700);
		StdDraw.setXscale(left, right);
		StdDraw.setYscale(min, max);

		double alpha = value(left);
		double beta = fun.value(left);

		double z = 0;

		for (double i = 0; i < (count - 1); i += 1) {

			z = left + (delta * i);

			final double alphaNext = value(z + delta);
			final double betaNext = fun.value(z + delta);

			StdDraw.setPenRadius(0.0035);
			StdDraw.setPenColor(Color.blue);
			StdDraw.line(z, alpha, z + delta, alphaNext);
			StdDraw.setPenRadius(0.0025);
			StdDraw.setPenColor(Color.red);
			StdDraw.line(z, beta, z + delta, betaNext);

			alpha = alphaNext;
			beta = betaNext;
		}

//		StdDraw.save(Integer.toString(this.hashCode()) + ".png");

	}

	default Function getDerivative() {
		final Function fun = this;
		return new GenericFunction() {
			@Override
			public double value(final double input) {
				return (fun.value(input + eps) - fun.value(input)) / eps;
			}

		};
	}

	default Function getDerivative(int n) {
		if (n < 0) {
			return getPrimitiveIntegral(-n);
		}
		if (n == 0) {
			return this;
		} else {
			return getDerivative().getDerivative(n - 1);
		}
	}

	default Function getPrimitiveIntegral() {
		final EuclideanSpace space = (EuclideanSpace) Generator.getGenerator().getTrigonometricSpace(20);
		final Function projection = getProjection(space);
		return new FunctionTuple(new GenericFunction() {
			@Override
			public double value(double input) {
				return FunctionSpace.getIntegral(projection, one, left, input, eps);
			}
		}.getCoordinates(space));
	}

	default Function getPrimitiveIntegral(int n) {
		if (n < 0) {
			return getDerivative(-n);
		}
		if (n == 0) {
			return this;
		} else {
			return getPrimitiveIntegral().getPrimitiveIntegral(n - 1);
		}
	}

	default Function getProjectionOfDerivative(EuclideanFunctionSpace space) {
		return new FunctionTuple(this.getDerivative().getCoordinates(space));
	}

	default Map<Vector, Double> getCoordinates(final EuclideanSpace space) {
		final Map<Vector, Double> newCoordinates = new ConcurrentHashMap<>();
		for (final Vector baseVec : space.genericBaseToList()) {
			newCoordinates.put(baseVec, space.product(this, baseVec));
		}
		return newCoordinates;
	}

	default Function getProjection(EuclideanSpace source) {
		return new FunctionTuple(getCoordinates(source));
	}
}
