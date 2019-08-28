package definitions.structures.euclidean.vectors.specialfunctions;

import definitions.structures.abstr.fields.scalars.Scalar;
import definitions.structures.euclidean.vectors.impl.GenericFunction;

public abstract class LinearFunction extends GenericFunction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1217714308801686785L;
	final Scalar a;
	final Scalar b;

	public LinearFunction(Scalar a, Scalar b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public Scalar value(Scalar input) {
		return this.getField().get(this.a.getValue() + (this.b.getValue() * input.getValue()));
	}

	@Override
	public String toString() {
		return "x -> " + this.a + "+" + this.b + "*x ";
	}

}
