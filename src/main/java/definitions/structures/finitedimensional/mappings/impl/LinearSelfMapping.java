package definitions.structures.finitedimensional.mappings.impl;

import java.util.Map;

import definitions.structures.abstr.Vector;
import definitions.structures.finitedimensional.mappings.Endomorphism;
import definitions.structures.finitedimensional.vectorspaces.EuclideanSpace;

/**
 * Linear self mapping.
 * 
 * @author ro
 *
 */
public class LinearSelfMapping extends FiniteDimensionalLinearMapping implements Endomorphism {

	/**
	 * Constructor.
	 * 
	 * @param source      the source and target space.
	 * @param coordinates the coordinates.
	 */
	protected LinearSelfMapping(final EuclideanSpace source, final Map<Vector, Map<Vector, Double>> coordinates) {
		super(source, source, coordinates);
	}

}