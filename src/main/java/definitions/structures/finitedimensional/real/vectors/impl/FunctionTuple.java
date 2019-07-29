package definitions.structures.finitedimensional.real.vectors.impl;

import java.util.Map;

import definitions.structures.abstr.Scalar;
import definitions.structures.abstr.Vector;
import definitions.structures.abstr.VectorSpace;
import definitions.structures.abstr.impl.LinearMapping;
import definitions.structures.field.scalar.Real;
import definitions.structures.finitedimensional.real.functionspaces.EuclideanFunctionSpace;
import definitions.structures.finitedimensional.real.mappings.impl.FiniteDimensionalLinearMapping;
import definitions.structures.finitedimensional.real.vectors.Function;
import definitions.structures.finitedimensional.real.vectorspaces.EuclideanSpace;

public class FunctionTuple extends Tuple implements Function {

	public FunctionTuple(final Map<Vector, Scalar> coordinates) {
		super(coordinates);
	}

	public FunctionTuple(final Scalar[] coordinates) {
		super(coordinates);
	}

	@Override
	public Scalar value(final Scalar input) {
		double ans = 0;
		for (final Vector fun : this.getCoordinates().keySet()) {
			ans += ((Function) fun).value(input).getValue() * this.getCoordinates().get(fun).getValue() ;
		}
		return new Real(ans);
	}

	public LinearMapping getDerivative(VectorSpace space) {
		return new FiniteDimensionalLinearMapping((EuclideanFunctionSpace)space, (EuclideanFunctionSpace)space) {
			@Override
			public Vector get(Vector vec2) {
				return ((Function) vec2).getDerivative();
			}

			@Override
			public Map<Vector, Scalar> getLinearity(Vector vec1) {
				return null;
			}
		};
	}

	@Override
	public Function getProjection(EuclideanSpace source) {
		return Function.super.getProjection(source);
	}
}