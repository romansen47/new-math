package definitions.structures.euclidean.mappings.impl;

import java.util.Map;

import definitions.structures.abstr.fields.scalars.Scalar;
import definitions.structures.abstr.vectorspaces.vectors.Vector;
import definitions.structures.euclidean.functionspaces.EuclideanFunctionSpace;
import definitions.structures.euclidean.mappings.FiniteDimensionalInjection;

public class InjectiveFunctionSpaceOperator extends FiniteDimensionalLinearMapping
		implements FiniteDimensionalInjection {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1601115668857737675L;

	/**
	 * Constructor
	 * 
	 * @param source the source vector space.
	 * @param target the target vector space.
	 * @param matrix the matrix.
	 */
	public InjectiveFunctionSpaceOperator(final EuclideanFunctionSpace source, final EuclideanFunctionSpace target,
			final Map<Vector, Map<Vector, Scalar>> matrix) {
		super(source, target, matrix);
	}

}
