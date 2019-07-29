package definitions.structures.finitedimensional.real.vectorspaces;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import definitions.structures.abstr.InnerProductSpace;
import definitions.structures.abstr.Scalar;
import definitions.structures.abstr.Vector;
import definitions.structures.field.impl.RealLine;
import definitions.structures.field.scalar.Real;
import definitions.structures.finitedimensional.real.vectors.FiniteVector;
import definitions.structures.finitedimensional.real.vectors.impl.FunctionTuple;
import definitions.structures.finitedimensional.real.vectors.impl.Tuple;

/**
 * 
 * @author RoManski
 *
 *         An euclidean vector space is a finite dimensional hilbert space. It
 *         is equipped with a base. The norm can be used to normalize vectors,
 *         compute distances between vectors and generate an orthonormal base.
 */
public interface EuclideanSpace extends InnerProductSpace {

	/**
	 * A base is an ordered set of linearly independent vectors.
	 * 
	 * @return the base as ordered base. @
	 */
	List<Vector> genericBaseToList();

	/**
	 * The base can be returned as an unordered set.
	 * 
	 * @return the base as a set. @
	 */
	Set<Vector> getGenericBase();

	/**
	 * The dimension of the space. This is the size of the base.
	 * 
	 * @return the dimension. null if space is infinitely dimensional.
	 */
	@Override
	Integer dim();

	/**
	 * Elements of the vector space can be created using a map (Vector -> double).
	 * 
	 * @param tmp the coordinates with respect to the base
	 * @return the corresponding vector @
	 */
	default Vector get(final Map<Vector, Scalar> tmp) {
		Vector vec = nullVec();
		for (final Vector basevec : genericBaseToList()) {
			vec = add(vec, stretch(basevec, tmp.get(basevec)));
		}
		return vec;
	}

	@Override
	default Vector add(final Vector vec1, final Vector vec2) {
		if ((vec1 instanceof FiniteVector) && (vec2 instanceof FiniteVector) && (vec1.getDim() == vec2.getDim())
				&& (vec1.getDim() == dim())) {
			final List<Vector> base = genericBaseToList();
			final Map<Vector, Scalar> coordinates = new ConcurrentHashMap<>();
			for (final Vector vec : base) {
				coordinates.put(getBaseVec(vec), new Real(((FiniteVector) vec1).getCoordinates().get(getBaseVec(vec)).getValue()
						+ ((FiniteVector) vec2).getCoordinates().get(getBaseVec(vec)).getValue()));
			}
			return new Tuple(coordinates);
		}
		return null;
	}

	@Override
	default Vector stretch(final Vector vec, final Scalar r) {
		final Map<Vector, Scalar> stretched = new ConcurrentHashMap<>();
		final Map<Vector, Scalar> coordinates = ((FiniteVector) vec).getCoordinates();
		final List<Vector> base = genericBaseToList();
		for (final Vector vec1 : base) {
			stretched.put(vec1, (Scalar) RealLine.getRealLine().product(coordinates.get(vec1) , r));
		}
		return new Tuple(stretched);
	}

	/**
	 * Compare vectors in order to identify base vectors.
	 * 
	 * @param vec2
	 * @return the base vector, if has same coordinates. Otherwise null.
	 */
	default Vector getBaseVec(final Vector vec2) {
		for (final Vector vec : genericBaseToList()) {
			if (vec2.equals(vec)) {
				return vec;
			}
		}
		return null;
	}

	/**
	 * Method to project a vector. Transformes instances of generic functions to
	 * function tuples in concrete space.
	 * 
	 * @param vec the vector to clone.
	 * @return copy of vec.
	 */
	Vector getCoordinates(Vector vec);

	/**
	 * Method to compute the distance between two vectors.
	 * 
	 * @param vec1 first vector.
	 * @param vec2 second vector.
	 * @return the distance. @
	 */
	default Real getDistance(final Vector vec1, final Vector vec2) {
		final Vector diff = add(vec1, (stretch(vec2, new Real(-1))));
		return norm(diff);
	}

	/**
	 * Method to create an orthonormal base.
	 * 
	 * @param the original base.
	 * @return an orthonormal base of same span. @
	 */
	List<Vector> getOrthonormalBase(List<Vector> base);

	/**
	 * Method to show the matrix of scalar products between the base elements.
	 */
	default void show() {
		final List<Vector> base = this.genericBaseToList();
		final Scalar[][] scalarProducts = new Scalar[base.size()][base.size()];
		int i = 0;
		for (final Vector vec1 : base) {
			int j = 0;
			for (final Vector vec2 : base) {
				scalarProducts[i][j] = this.innerProduct(vec1, vec2);
				System.out.print((scalarProducts[i][j].getValue() - (scalarProducts[i][j].getValue() % 0.001)) + ",");
				j++;
			}
			System.out.println("");
			i++;
		}

	}

	default Vector copyVector(final Vector vec) {
		final Map<Vector, Scalar> coordinates = new ConcurrentHashMap<>();
		for (final Vector baseVec : this.genericBaseToList()) {
			coordinates.put(baseVec, this.innerProduct(vec, baseVec));
		}
		return new FunctionTuple(coordinates);
	}

}