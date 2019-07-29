package definitions.structures.finitedimensional.real.mappings.impl;

import java.util.HashMap;

import definitions.structures.abstr.Homomorphism;
import definitions.structures.abstr.Vector;
import definitions.structures.finitedimensional.real.functionspaces.EuclideanFunctionSpace;
import definitions.structures.finitedimensional.real.mappings.FiniteDimensionalHomomorphism;
import definitions.structures.finitedimensional.real.vectors.Function;
import definitions.structures.finitedimensional.real.vectorspaces.EuclideanSpace;

public final class DerivativeOperator extends FiniteDimensionalLinearMapping implements FiniteDimensionalHomomorphism {

	public DerivativeOperator(EuclideanSpace source, EuclideanSpace target) {
		super(source, target);
		this.linearity = new HashMap<>();
		for (final Vector baseVec : source.genericBaseToList()) {
			this.linearity.put(baseVec, ((Function) baseVec)
					.getProjectionOfDerivative((EuclideanFunctionSpace) this.target).getCoordinates());
		}
		this.getGenericMatrix();
	}

//	@Override
//	public Vector get(Vector vec) {
//		return ((Function) vec).getProjectionOfDerivative((EuclideanFunctionSpace) this.target);
//	}

	public Vector get(Vector vec, int degree) {
		if (degree < 1) {
			return null;
		}
		Homomorphism tmp = new FiniteDimensionalLinearMapping(
				(EuclideanFunctionSpace)this.source, 
				(EuclideanFunctionSpace)this.source) {
			@Override
			public Vector get(Vector vec) {
				return vec;
			}
		};
		for (int i = 0; i < degree; i++) {
			tmp = MappingGenerator.getInstance().getComposition(this, tmp);
		}
		return tmp.get(vec);
	}

}