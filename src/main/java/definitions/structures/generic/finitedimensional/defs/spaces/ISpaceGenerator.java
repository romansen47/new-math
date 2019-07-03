package definitions.structures.generic.finitedimensional.defs.spaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import definitions.structures.abstr.Vector;
import definitions.structures.abstr.VectorSpace;
import definitions.structures.generic.finitedimensional.defs.Generator;
import definitions.structures.generic.finitedimensional.defs.spaces.impl.FiniteDimensionalVectorSpace;
import definitions.structures.generic.finitedimensional.defs.spaces.impl.SpaceGenerator;
import definitions.structures.generic.finitedimensional.defs.subspaces.functionspaces.EuclideanFunctionSpace;
import definitions.structures.generic.finitedimensional.defs.subspaces.functionspaces.impl.FiniteDimensionalFunctionSpace;
import definitions.structures.generic.finitedimensional.defs.subspaces.functionspaces.impl.FiniteDimensionalSobolevSpace;
import definitions.structures.generic.finitedimensional.defs.vectors.Function;
import definitions.structures.generic.finitedimensional.defs.vectors.functions.LinearFunction;
import definitions.structures.generic.finitedimensional.defs.vectors.impl.Monome;
import exceptions.WrongClassException;

public interface ISpaceGenerator {

	Map<Integer, EuclideanSpace> getCachedCoordinateSpaces();

	Map<Integer, EuclideanFunctionSpace> getCachedFunctionSpaces();

	default EuclideanSpace getFiniteDimensionalVectorSpace(final int dim) {
		if (!getCachedCoordinateSpaces().containsKey(dim)) {
			final List<Vector> basetmp = new ArrayList<>();
			for (int i = 0; i < dim; i++) {
				basetmp.add(Generator.getGenerator().getVectorgenerator().getFiniteVector(dim));
			}
			for (int i = 0; i < dim; i++) {
				for (int j = 0; j < dim; j++) {
					if (i == j) {
						basetmp.get(i).getCoordinates().put(basetmp.get(i), 1.);
					} else {
						basetmp.get(i).getCoordinates().put(basetmp.get(j), 0.);
					}
				}
			}
			getCachedCoordinateSpaces().put(Integer.valueOf(dim), new FiniteDimensionalVectorSpace(basetmp));
		}
		return getCachedCoordinateSpaces().get(dim);
	}

	default EuclideanFunctionSpace getFiniteDimensionalFunctionSpace(final List<Vector> genericBase, final double left,
			final double right) {
		final EuclideanFunctionSpace space = getCachedFunctionSpaces().get(genericBase.hashCode());
		if ((space != null) && (space.getInterval()[0] == left) && (space.getInterval()[1] == right)) {
			return space;
		}
		final FiniteDimensionalFunctionSpace newSpace = new FiniteDimensionalFunctionSpace(genericBase, left, right);
		getCachedFunctionSpaces().put(genericBase.hashCode(), newSpace);
		return newSpace;
	}

	default EuclideanFunctionSpace getFiniteDimensionalSobolevSpace(final List<Vector> genericBase, final double left,
			final double right, final int degree) {
		final EuclideanFunctionSpace space = getCachedFunctionSpaces().get(genericBase.hashCode());
		if ((space != null) && (space instanceof FiniteDimensionalSobolevSpace) && (space.getInterval()[0] == left)
				&& (space.getInterval()[1] == right)) {
			return space;
		}
		final FiniteDimensionalFunctionSpace newSpace = new FiniteDimensionalSobolevSpace(genericBase, left, right,
				degree);
		getCachedFunctionSpaces().put(genericBase.hashCode(), newSpace);
		return newSpace;
	}

	default EuclideanFunctionSpace getTrigonometricSpace(final int n) {
		return new TrigonometricSpace(n, -Math.PI, Math.PI);
	}

	default EuclideanFunctionSpace getTrigonometricSobolevSpace(final int n, final int degree) {
		return new TrigonometricSobolevSpace(n, -Math.PI, Math.PI, degree);
	}

	default EuclideanFunctionSpace getFiniteDimensionalSobolevSpace(final EuclideanFunctionSpace space,
			final int degree) {
		return new FiniteDimensionalSobolevSpace(space, degree);
	}

	default EuclideanFunctionSpace getTrigonometricFunctionSpaceWithLinearGrowth(final int n)
			throws WrongClassException {
		return (EuclideanFunctionSpace) extend(getTrigonometricSpace(n), new LinearFunction(1, 1));
	}

	void setCachedCoordinateSpaces(ISpaceGenerator readObject);

	void setCachedFunctionSpaces(ISpaceGenerator gen);

	default VectorSpace getPolynomialFunctionSpace(int maxDegree, double left, double right) {
		final int key = (int) (maxDegree * (1.e3 + left) * (1.e3 + right));
		final List<Vector> base = new ArrayList<>();
		if (getCachedFunctionSpaces().containsKey(key)) {
			return getCachedFunctionSpaces().get(key);
		}
		for (int i = 0; i < (maxDegree + 1); i++) {
			base.add(new Monome(i));
		}
		final VectorSpace ans = Generator.getGenerator().getFiniteDimensionalFunctionSpace(base, left, right);
		getCachedFunctionSpaces().put(key, (EuclideanFunctionSpace) ans);
		return ans;
	}

	default VectorSpace getPolynomialSobolevSpace(int maxDegree, double left, double right, int degree) {
		return Generator.getGenerator().getFiniteDimensionalSobolevSpace(
				(EuclideanFunctionSpace) getPolynomialFunctionSpace(maxDegree, left, right), degree);
	}

	default EuclideanSpace extend(VectorSpace space, Vector fun) throws WrongClassException {
		final EuclideanSpace asEuclidean = (EuclideanSpace) space;
		if (fun instanceof Function) {
			final List<Vector> base = asEuclidean.genericBaseToList();
			final List<Vector> newBase = new ArrayList<>();
			Vector ortho = asEuclidean.normalize(fun);
			for (final Vector baseVec : base) {
				final Vector product = asEuclidean.stretch(baseVec, asEuclidean.product(ortho, baseVec));
				final Vector summand = asEuclidean.stretch(product, -1);
				ortho = asEuclidean.normalize(asEuclidean.add(ortho, summand));
				newBase.add(baseVec);
			}
			newBase.add(ortho);
			if (asEuclidean instanceof FiniteDimensionalSobolevSpace) {
				return SpaceGenerator.getInstance().getFiniteDimensionalSobolevSpace(newBase,
						((FiniteDimensionalSobolevSpace) asEuclidean).getInterval()[0],
						((FiniteDimensionalSobolevSpace) asEuclidean).getInterval()[1],
						((FiniteDimensionalSobolevSpace) asEuclidean).getDegree());
			} else {
				if (asEuclidean instanceof FiniteDimensionalFunctionSpace) {
					return SpaceGenerator.getInstance().getFiniteDimensionalFunctionSpace(newBase,
							((FiniteDimensionalFunctionSpace) asEuclidean).getInterval()[0],
							((FiniteDimensionalFunctionSpace) asEuclidean).getInterval()[1]);
				}
			}
			return getFiniteDimensionalVectorSpace(newBase);
		}
		throw new WrongClassException("Input should be a function, not a vector.");
	}

	default EuclideanSpace getFiniteDimensionalVectorSpace(List<Vector> newBase) {
		return new FiniteDimensionalVectorSpace(newBase);
	}
}
