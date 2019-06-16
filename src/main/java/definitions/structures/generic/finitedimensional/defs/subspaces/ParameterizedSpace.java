package definitions.structures.generic.finitedimensional.defs.subspaces;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import definitions.structures.abstr.Vector;
import definitions.structures.generic.finitedimensional.defs.Generator;
import definitions.structures.generic.finitedimensional.defs.mappings.IFiniteDimensionalLinearMapping;
import definitions.structures.generic.finitedimensional.defs.spaces.EuclideanSpace;
import definitions.structures.generic.finitedimensional.defs.vectors.FiniteVector;
import definitions.structures.generic.finitedimensional.defs.vectors.impl.Tuple;

public interface ParameterizedSpace extends EuclideanSpace {

	EuclideanSpace getSuperSpace();

	@Override
	boolean contains(Vector vec) throws Throwable;

	@Override
	default int dim() throws Throwable {
		return getParametrization().getRank();
	}

	IFiniteDimensionalLinearMapping getParametrization();

	@Override
	default Vector add(final Vector vec1, final Vector vec2) throws Throwable {
		if ((vec1 instanceof FiniteVector) && (vec2 instanceof FiniteVector)) {
			final List<Vector> base = genericBaseToList();
			final Map<Vector, Double> coordinates = new ConcurrentHashMap<>();
			for (final Vector vec : base) {
				final double summand1 = ((FiniteVector) vec1).getCoordinates().get(getBaseVec(vec));
				final double summand2 = ((FiniteVector) vec2).getCoordinates().get(getBaseVec(vec));
				final double sumOnCoordinate = summand1 + summand2;
				coordinates.put(vec, sumOnCoordinate);
			}
			return get(coordinates);
		} else {
			return this.add(vec1, vec2);
		}
	}

	default Map<Vector, Double> getInverseCoordinates(final Vector vec2) throws Throwable {
		final Vector ans = getNearestVector(vec2);
		// if (getSuperSpace().getDistance((FiniteVector) getParametrization().get(ans),
		// vec2) < 1.e-5) {
		return ans.getCoordinates();
//		} else {
//			return null;
//		}
	}

	Map<Vector, Vector> getParametrizationBaseVectorMapping();

	default FiniteVector getNearestVector(final Vector vec2) throws Throwable {
		final IFiniteDimensionalLinearMapping transposed = (IFiniteDimensionalLinearMapping) Generator.getGenerator()
				.getMappinggenerator().getTransposedMapping(getParametrization());
		final IFiniteDimensionalLinearMapping quadratic = (IFiniteDimensionalLinearMapping) Generator.getGenerator()
				.getMappinggenerator().getComposition(transposed, getParametrization());
		final Vector transformed = transposed.get(vec2);
		return new Tuple(quadratic.solve(transformed).getCoordinates());
	}
}
