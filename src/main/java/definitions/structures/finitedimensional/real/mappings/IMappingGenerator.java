package definitions.structures.finitedimensional.real.mappings;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import definitions.structures.abstr.Homomorphism;
import definitions.structures.abstr.Scalar;
import definitions.structures.abstr.Vector;
import definitions.structures.field.impl.RealLine;
import definitions.structures.field.scalar.Real;
import definitions.structures.finitedimensional.real.mappings.impl.MappingGenerator;
import definitions.structures.finitedimensional.real.vectorspaces.EuclideanSpace;

public interface IMappingGenerator {

	default Homomorphism getComposition(final Homomorphism a, final Homomorphism b) {
		final Map<Vector, Map<Vector, Scalar>> linearity = new ConcurrentHashMap<>();
		final double tmp;
//		for (final Vector vec : ((EuclideanSpace) b.getSource()).genericBaseToList()) {
//			linearity.put(vec,((Function)a.get(b.get(vec))).getCoordinates((EuclideanSpace) a.getTarget()));
//		}
//		return getFiniteDimensionalLinearMapping(((EuclideanSpace) a.getSource()), ((EuclideanSpace) b.getTarget()),
//				linearity);
		final Scalar[][] genericMatrix = MappingGenerator.getInstance().composition(a.getGenericMatrix(),
				b.getGenericMatrix());
		return getFiniteDimensionalLinearMapping(((EuclideanSpace) b.getSource()), ((EuclideanSpace) a.getSource()),
				genericMatrix);
	}

	default Scalar[][] composition(final Scalar[][] scalars, final Scalar[][] scalars2) {
		final Scalar[][] matC = new Scalar[scalars.length][scalars2[0].length];
		for (int i = 0; i < scalars.length; i++) {
			for (int j = 0; j < scalars2[0].length; j++) {
				matC[i][j]=RealLine.getRealLine().getZero();
				for (int k = 0; k < scalars[0].length; k++) {
					matC[i][j] = new Real(matC[i][j].getValue()+scalars[i][k].getValue() * scalars2[k][j].getValue());
				}
			}
		}
		return matC;
	}

	Homomorphism getFiniteDimensionalLinearMapping(EuclideanSpace source, EuclideanSpace target,
			Map<Vector, Map<Vector, Scalar>> matrix);

	Homomorphism getFiniteDimensionalLinearMapping(EuclideanSpace source, EuclideanSpace target,
			Scalar[][] genericMatrix);

	Homomorphism getFiniteDimensionalLinearMapping(Scalar[][] genericMatrix);

	default Homomorphism getTransposedMapping(final FiniteDimensionalHomomorphism map) {
		final Map<Vector, Map<Vector, Scalar>> transposedMatrix = new ConcurrentHashMap<>();
		for (final Vector targetVec : ((EuclideanSpace) map.getTarget()).genericBaseToList()) {
			final Map<Vector, Scalar> entry = new ConcurrentHashMap<>();
			for (final Vector sourceVec : ((EuclideanSpace) map.getSource()).genericBaseToList()) {
				entry.put(sourceVec, map.getLinearity().get(sourceVec).get(targetVec));
			}
			transposedMatrix.put(targetVec, entry);
		}
		return getFiniteDimensionalLinearMapping((EuclideanSpace) map.getTarget(), (EuclideanSpace) map.getSource(),
				transposedMatrix);
	}

}