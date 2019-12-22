package definitions.structures.euclidean.vectorspaces;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import definitions.structures.abstr.fields.scalars.Scalar;
import definitions.structures.abstr.vectorspaces.vectors.FiniteVectorMethods;
import definitions.structures.abstr.vectorspaces.vectors.Vector;
import definitions.structures.euclidean.mappings.FiniteDimensionalHomomorphism;
import definitions.structures.euclidean.mappings.impl.MappingGenerator;
import definitions.structures.euclidean.vectors.FiniteVector;
import definitions.structures.euclidean.vectors.impl.Tuple;

public interface ParameterizedSpace extends EuclideanSpace {

	@Override
	default Vector add(final Vector vec1, final Vector vec2) {
		if ((vec1 instanceof FiniteVector) && (vec2 instanceof FiniteVector)) {
			final List<Vector> base = this.genericBaseToList();
			final Map<Vector, Scalar> coordinates = new ConcurrentHashMap<>();
			for (final Vector vec : base) {
				final double summand1 = ((FiniteVector) vec1).getCoordinates().get(this.getBaseVec(vec)).getValue();
				final double summand2 = ((FiniteVector) vec2).getCoordinates().get(this.getBaseVec(vec)).getValue();
				final double sumOnCoordinate = summand1 + summand2;
				coordinates.put(vec, this.getField().get(sumOnCoordinate));
			}
			return this.get(coordinates);
		} else {
			return this.add(vec1, vec2);
		}
	}

	@Override
	boolean contains(Vector vec);

	@Override
	default Integer getDim() {
		return this.getParametrization().getRank();
	}

	default Map<Vector, Scalar> getInverseCoordinates(final Vector vec2) {
		final Vector ans = this.getNearestVector(vec2);
		return ((FiniteVectorMethods) ans).getCoordinates();
	}

	default FiniteVector getNearestVector(final Vector vec2) {
		final FiniteDimensionalHomomorphism transposed = (FiniteDimensionalHomomorphism) MappingGenerator.getInstance()
				.getTransposedMapping(this.getParametrization());
		final FiniteDimensionalHomomorphism quadratic = (FiniteDimensionalHomomorphism) MappingGenerator.getInstance()
				.getComposition(transposed, this.getParametrization());
		final Vector transformed = transposed.get(vec2);
		/*
		 * Direct usage of constructor instead of get method in order to avoid cycles.
		 * Don't touch this
		 */
		return new Tuple(quadratic.solve((FiniteVector) transformed).getCoordinates());
	}

	FiniteDimensionalHomomorphism getParametrization();

	Map<Vector, Vector> getParametrizationBaseVectorMapping();

	EuclideanSpace getSuperSpace();

}
