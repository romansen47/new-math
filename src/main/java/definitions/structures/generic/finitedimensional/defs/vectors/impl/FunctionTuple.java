package definitions.structures.generic.finitedimensional.defs.vectors.impl;

import java.util.Map;

import definitions.structures.abstr.Vector;
import definitions.structures.generic.finitedimensional.defs.vectors.FiniteVector;
import definitions.structures.generic.finitedimensional.defs.vectors.Function;

public class FunctionTuple extends Tuple implements Function {

	public FunctionTuple(Map<Vector, Double> coordinates) throws Throwable {
		super(coordinates);
	}

	public FunctionTuple(double[] coordinates) throws Throwable {
		super(coordinates);
	}

}
