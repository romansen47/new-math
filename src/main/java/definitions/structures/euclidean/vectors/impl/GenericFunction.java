package definitions.structures.euclidean.vectors.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import definitions.structures.abstr.fields.Field;
import definitions.structures.abstr.fields.scalars.Scalar;
import definitions.structures.abstr.vectorspaces.FunctionSpace;
import definitions.structures.abstr.vectorspaces.VectorSpace;
import definitions.structures.abstr.vectorspaces.vectors.Function;
import definitions.structures.abstr.vectorspaces.vectors.Vector;
import definitions.structures.euclidean.functionspaces.EuclideanFunctionSpace;
import definitions.structures.euclidean.vectorspaces.EuclideanSpace;

public abstract class GenericFunction implements Function {

	private static final long serialVersionUID = 1L;

	protected Field field;

	Map<EuclideanSpace, Map<Vector, Scalar>> coordinatesMap = new HashMap<>();
	private Map<Vector, Scalar> coordinates;

	@Override
	public Map<Vector, Scalar> getCoordinates() {
		return this.coordinates;
	}

	@Override
	public Map<Vector, Scalar> getCoordinates(final EuclideanSpace space) {
		if (this.coordinatesMap != null) {
			if (this.coordinatesMap.get(space) != null) {
				return this.coordinatesMap.get(space);
			}
			final Map<Vector, Scalar> map = new ConcurrentHashMap<>();
			this.coordinatesMap.put(space, map);
		}
		final Map<Vector, Scalar> newCoordinates = new ConcurrentHashMap<>();
		for (final Vector baseVec : space.genericBaseToList()) {
			newCoordinates.put(baseVec, ((EuclideanFunctionSpace) space).innerProduct(this, baseVec));
		}
		this.coordinatesMap.put(space, newCoordinates);
		return newCoordinates;
	}

	@Override
	public Integer getDim() {
		return -1;
	}

	@Override
	public boolean elementOf(final VectorSpace space) {
		return space instanceof FunctionSpace;
	}

	@Override
	public boolean equals(Object vec) {
		return super.equals(vec);
	}

	@Override
	public void setCoordinates(final Map<Vector, Scalar> coordinates, final EuclideanSpace space) {
		this.coordinatesMap.put(space, coordinates);
		this.setCoordinates(coordinates);
	}

	@Override
	public void setCoordinates(final Map<Vector, Scalar> coordinates) {
		this.coordinates = coordinates;
	}

	@Override
	public Map<EuclideanSpace, Map<Vector, Scalar>> getCoordinatesMap() {
		return this.coordinatesMap;
	}

}
