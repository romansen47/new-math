package definitions.structures.euclidean.vectors.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import definitions.structures.abstr.algebra.fields.scalars.Scalar;
import definitions.structures.abstr.vectorspaces.VectorSpace;
import definitions.structures.abstr.vectorspaces.vectors.Vector;
import definitions.structures.euclidean.vectors.FiniteVector;
import definitions.structures.euclidean.vectorspaces.EuclideanSpace;
import definitions.structures.euclidean.vectorspaces.impl.FiniteDimensionalVectorSpace;
import definitions.structures.euclidean.vectorspaces.impl.SpaceGenerator;

public class Tuple implements FiniteVector {

	private static final long serialVersionUID = -738201540142933649L;

	final int dim;

	private Map<Vector, Scalar> coordinates;

	public Tuple() {
		this.dim = 0;
	}

	public Tuple(final int dim) {
		this.dim = dim;
		this.coordinates = new ConcurrentHashMap<>();
	}

	public Tuple(final Map<Vector, Scalar> coordinates) {
		this.setCoordinates(coordinates);
		this.dim = coordinates.keySet().size();
	}

	public Tuple(final Scalar[] coordinates) {
		this.dim = coordinates.length;
		this.setCoordinates(new ConcurrentHashMap<>());
		int i = 0;
		for (final Vector vec : SpaceGenerator.getInstance().getFiniteDimensionalVectorSpace(this.dim)
				.genericBaseToList()) {
			this.getCoordinates().put(vec, coordinates[i++]);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean elementOf(final VectorSpace space) {
		if (!(space instanceof FiniteDimensionalVectorSpace)
				&& (((FiniteDimensionalVectorSpace) space).getDim() == this.dim)) {
			return false;
		}
		final List<Vector> base = ((EuclideanSpace) space).genericBaseToList();
		for (final Vector vec : this.coordinates.keySet()) {
			boolean ans = false;
			for (final Vector spaceBaseVec : base) {
				if (vec.equals(spaceBaseVec)) {
					ans = true;
				}
			}
			if (ans == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object vec) {
		if (!(vec instanceof Vector)) {
			return false;
		} else if (((Vector) vec).getDim() == this.dim) { 
			return this.getCoordinates().equals(((FiniteVector) vec).getCoordinates());
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
//	@XmlAttribute
	public Map<Vector, Scalar> getCoordinates() {
		return this.coordinates;
	}

	@Override
	public String toString() {
		String str = "<Tuple>\r";
		for (Entry<Vector, Scalar> entry : coordinates.entrySet()) {
			str += entry.getValue().toXml() + "\r";
		}
		str += "</Tuple>\r";
		return str;
//		try {
//			for (int i = 0; i < this.dim; i++) {
//				str += this.getCoordinates()
//						.get(((EuclideanSpace) SpaceGenerator.getInstance().getFiniteDimensionalVectorSpace(this.dim))
//								.genericBaseToList().get(i))
//						+ "\r";
//			}
//			return str;
//		} catch (final Throwable e) {
//			e.printStackTrace();
//			return "Problems occured...";
//		}
	}

	@Override
//	@XmlAttribute
	public Map<Vector, Scalar> getCoordinates(final EuclideanSpace source) {
		final Map<Vector, Scalar> newCoordinates = new ConcurrentHashMap<>();
		if (this.elementOf(source)) {
			return this.getCoordinates();
		} else {
			for (final Vector baseVec : source.genericBaseToList()) {
				newCoordinates.put(baseVec, source.innerProduct(this, baseVec));
			}
		}
		return newCoordinates;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getDim() {
		return this.dim;
	}

	// @Override
	// public Boolean equals(final Vector vec) {
	// for (final Vector key : this.getCoordinates().keySet()) {
	// if (!(this.coordinates.get(key).getValue() ==
	// vec.getCoordinates().get(key).getValue())) {
	// return false;
	// }
	// }
	// return true;
	// }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCoordinates(final Map<Vector, Scalar> coordinates) {
		this.coordinates = coordinates;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCoordinates(final Map<Vector, Scalar> coordinates, final EuclideanSpace space) {
		this.setCoordinates(coordinates);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toXml() {
		String ans = "<tuple>\r";
		for (final Vector x : SpaceGenerator.getInstance().getFiniteDimensionalVectorSpace(this.dim)
				.genericBaseToList()) {
//			String name=x.getClass().toString().split("class ")[1];
			ans += this.getCoordinates().get(x).toXml();
		}
		ans += "</tuple>\r";
		return ans;
	}

}
