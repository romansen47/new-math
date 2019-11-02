package definitions.structures.abstr.vectorspaces;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import definitions.structures.abstr.fields.Field;
import definitions.structures.abstr.fields.scalars.Scalar;
import definitions.structures.abstr.vectorspaces.vectors.FiniteVectorMethods;
import definitions.structures.abstr.vectorspaces.vectors.Vector;

public interface VectorSpaceMethods extends Serializable {

	/**
	 * Not yet implemented.
	 * 
	 * @param vec the vector to check for.
	 * @return whether vec is an element of the space.
	 */
	boolean contains(Vector vec);

	/**
	 * Vector space is not empty.
	 * 
	 * @return the zero vector.
	 */
	default Vector nullVec() {
		return null;
	}

	/**
	 * For debugging purposes.
	 * 
	 * @return The string.
	 */
	@Override
	String toString();

	default Integer getDim() {
		return null;
	}

	default void assignOrthonormalCoordinates(List<Vector> newBase, Field field) {
		for (final Vector vec : newBase) {
			final Map<Vector, Scalar> tmpCoord = new ConcurrentHashMap<>();
			for (final Vector otherVec : newBase) {
				if (vec == otherVec) {
					tmpCoord.put(otherVec, (Scalar) field.getOne());
				} else {
					tmpCoord.put(otherVec, (Scalar) field.getZero());
				}
			}
			((FiniteVectorMethods) vec).setCoordinates(tmpCoord);
		}
	}

}