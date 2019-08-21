package definitions.structures.euclidean.vectorspaces.impl;

import java.util.ArrayList;
import java.util.List;

import definitions.structures.abstr.fields.Field;
import definitions.structures.abstr.fields.scalars.impl.Real;
import definitions.structures.abstr.vectorspaces.vectors.Vector;
import definitions.structures.euclidean.functionspaces.impl.FiniteDimensionalFunctionSpace;
import definitions.structures.euclidean.vectors.specialfunctions.Constant;

/**
 * Fourier space.
 * 
 * @author ro
 *
 */
public class TrigonometricSpace extends FiniteDimensionalFunctionSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6752082006058465558L;

	/**
	 * Constructor.
	 * 
	 * @param n     the highest degree of the trigonometric polynomials.
	 * @param left  the inf of the interval.
	 * @param right the sup of the interval.
	 */
	public TrigonometricSpace(Field field, final int n, final double right) {
		super(field);
		final double left = -right;
		final List<Vector> tmpBase = new ArrayList<>();
		this.dim = (2 * n) + 1;
//		final EuclideanSpace space = (EuclideanSpace) Generator.getGenerator().getSpacegenerator()
//				.getFiniteDimensionalVectorSpace(this.dim);
//		final List<Vector> coordinates = space.genericBaseToList();
		this.interval = new double[] { left, right };
		final Field f = field;
		tmpBase.add(new Constant(new Real(1. / Math.sqrt(2 * right))) {
			private static final long serialVersionUID = 7393292837814311224L;

			@Override
			public Field getField() {
				return f;
			}
		});
		this.getSineFunctions(n, Math.PI / right, tmpBase);
		this.getCosineFunctions(n, Math.PI / right, tmpBase);
		this.base = tmpBase;
		this.assignOrthonormalCoordinates(this.base, field);
	}

}
