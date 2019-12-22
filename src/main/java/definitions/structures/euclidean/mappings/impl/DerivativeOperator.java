package definitions.structures.euclidean.mappings.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import definitions.structures.abstr.fields.scalars.Scalar;
import definitions.structures.abstr.mappings.Homomorphism;
import definitions.structures.abstr.vectorspaces.vectors.FiniteVectorMethods;
import definitions.structures.abstr.vectorspaces.vectors.Function;
import definitions.structures.abstr.vectorspaces.vectors.Vector;
import definitions.structures.euclidean.vectors.impl.FunctionTuple;
import definitions.structures.euclidean.vectorspaces.EuclideanSpace;

public final class DerivativeOperator extends FiniteDimensionalLinearMapping implements Homomorphism {

	private static final long serialVersionUID = -5103583236895270490L;

	final private Map<Vector, Map<Integer, Vector>> cachedDerivatives = new ConcurrentHashMap<>();

	public DerivativeOperator(final EuclideanSpace source, final EuclideanSpace target) {
		super(source, target);
		this.linearity = new HashMap<>();
		for (final Vector baseVec : source.genericBaseToList()) {
			final Function derivative = ((Function) baseVec).getDerivative();
			final Map<Vector, Scalar> derivativeOnSpace = derivative.getCoordinates(target);
			this.linearity.put(baseVec, derivativeOnSpace);
		}
		this.getGenericMatrix();
	}

	public DerivativeOperator(final EuclideanSpace source, final EuclideanSpace target,
			final Map<Vector, Map<Vector, Scalar>> coordinates) {
		super(source, target, coordinates);
	}

	private void addToCache(final Vector vec, final int degree, final Vector ans) {
		final Map<Integer, Vector> tmp = this.cachedDerivatives.get(vec);// new ConcurrentHashMap<>();
		tmp.put(degree, ans);
		// cachedDerivatives.put(vec, tmp);
	}

	public Vector get(final Vector vec, final int degree) {
		if (degree < 0) {
			return null;
		}
		if (degree == 0) {
			return vec;
		}

		if (degree == 1) {
			if (((FiniteVectorMethods) vec).getCoordinates() == null) {
				return (this.get(vec));// .getProjection((EuclideanSpace) getSource());
			}
			return ((Function) this.get(vec)).getProjection((EuclideanSpace) this.getSource());
		}
		if (vec instanceof FunctionTuple) {
			return this.get(this.get(vec), degree - 1);
		}
//		Scalar[][] tmp = this.getGenericMatrix().clone();
		Vector test = ((Function) vec).getProjection((EuclideanSpace) this.getSource());
		for (int i = 0; i < degree; i++) {
			test = this.get(test);
//			tmp = MappingGenerator.getInstance().composition(this.getGenericMatrix().clone(), tmp);
		}
//		final Vector ans = ((Function) MappingGenerator.getInstance()
//				.getFiniteDimensionalLinearMapping((EuclideanSpace) this.source, (EuclideanSpace) this.target, tmp)
//				.get(vec)).getProjection((EuclideanSpace) this.getSource());
//		this.addToCache(vec, degree, ans);
		return test;// ans;
	}

}
