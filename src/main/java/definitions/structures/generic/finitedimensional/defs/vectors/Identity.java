package definitions.structures.generic.finitedimensional.defs.vectors;

import definitions.structures.generic.finitedimensional.defs.vectors.impl.FunctionTuple;

public class Identity extends FunctionTuple implements Function {

	public Identity(final double[] coordinates) throws Throwable {
		super(coordinates);
	}

	@Override
	public double value(final double input) throws Throwable {
		return input;// Math.pow(Math.PI, 2);
	}

	@Override
	public String toString() {
		return "the identity";
	}

}
