package definitions.structures.finitedimensional.real.vectorspaces;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import definitions.structures.abstr.Scalar;
import definitions.structures.abstr.Vector;
import definitions.structures.field.scalar.Real;
import definitions.structures.finitedimensional.real.Generator;
import definitions.structures.finitedimensional.real.mappings.FiniteDimensionalHomomorphism;
import definitions.structures.finitedimensional.real.vectors.FiniteVector;
import definitions.structures.finitedimensional.real.vectors.impl.Tuple;

public interface ParameterizedSpace extends EuclideanSpace {

	EuclideanSpace getSuperSpace();

	@Override
	boolean contains(Vector vec);

	@Override
	default Integer dim() {
		return getParametrization().getRank();
	}

	FiniteDimensionalHomomorphism getParametrization();

	@Override
	default Vector add(final Vector vec1, final Vector vec2) {
		if ((vec1 instanceof FiniteVector) && (vec2 instanceof FiniteVector)) {
			final List<Vector> base = genericBaseToList();
			final Map<Vector, Scalar> coordinates = new ConcurrentHashMap<>();
			for (final Vector vec : base) {
				final double summand1 = ((FiniteVector) vec1).getCoordinates().get(getBaseVec(vec)).getValue();
				final double summand2 = ((FiniteVector) vec2).getCoordinates().get(getBaseVec(vec)).getValue();
				final double sumOnCoordinate = summand1 + summand2;
				coordinates.put(vec, new Real(sumOnCoordinate));
			}
			return get(coordinates);
		} else {
			return this.add(vec1, vec2);
		}
	}

	default Map<Vector, Scalar> getInverseCoordinates(final Vector vec2) {
		final Vector ans = getNearestVector(vec2);
		return ans.getCoordinates();
	}

	Map<Vector, Vector> getParametrizationBaseVectorMapping();

	default FiniteVector getNearestVector(final Vector vec2) {
		final FiniteDimensionalHomomorphism transposed = (FiniteDimensionalHomomorphism) Generator.getGenerator()
				.getMappinggenerator().getTransposedMapping(getParametrization());
		final FiniteDimensionalHomomorphism quadratic = (FiniteDimensionalHomomorphism) Generator.getGenerator()
				.getMappinggenerator().getComposition(transposed, getParametrization());
		final Vector transformed = transposed.get(vec2);
		return new Tuple(quadratic.solve(transformed).getCoordinates());
	}

}