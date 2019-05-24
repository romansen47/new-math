package definitions.structures.generic.finitedimensional.defs.mappings.impl;

import java.util.HashMap;
import java.util.Map;

import definitions.structures.abstr.Vector;
import definitions.structures.generic.finitedimensional.defs.mappings.Endomorphism;
import definitions.structures.generic.finitedimensional.defs.mappings.IFiniteDimensionalLinearMapping;
import definitions.structures.generic.finitedimensional.defs.mappings.IMappingGenerator;
import definitions.structures.generic.finitedimensional.defs.spaces.CoordinateSpace;
import definitions.structures.generic.finitedimensional.defs.spaces.impl.SpaceGenerator;
import definitions.structures.generic.finitedimensional.defs.subspaces.functionspaces.IFiniteDimensionalFunctionSpace;
import definitions.structures.generic.finitedimensional.defs.vectors.FiniteVector;

public class MappingGenerator implements IMappingGenerator {

	private static IMappingGenerator generator = null;

	public static IMappingGenerator getInstance() {
		if (generator == null) {
			generator = new MappingGenerator();
		}
		return generator;
	}

	private MappingGenerator() {
	}

	@Override
	public IFiniteDimensionalLinearMapping getFiniteDimensionalLinearMapping(double[][] genericMatrix)
			throws Throwable {
		final int dimSource = genericMatrix[0].length;
		final int dimTarget = genericMatrix.length;
		final CoordinateSpace source = SpaceGenerator.getInstance().getFiniteDimensionalVectorSpace(dimSource);
		final CoordinateSpace target = SpaceGenerator.getInstance().getFiniteDimensionalVectorSpace(dimTarget);
		final Map<Vector, Map<Vector, Double>> coordinates = new HashMap<>();
		int i = 0;
		for (final Vector vec1 : source.genericBaseToList()) {
			int j = 0;
			final Map<Vector, Double> tmp = new HashMap<>();
			for (final Vector vec2 : target.genericBaseToList()) {
				tmp.put(vec2, genericMatrix[j][i]);
				j++;
			}
			coordinates.put(vec1, tmp);
			i++;
		}
		if (dimSource < dimTarget) {
			IFiniteDimensionalLinearMapping tmp = new FiniteDimensionalLinearMapping(source, target, coordinates);
			if (dimSource == tmp.getRank()) {
				return new FiniteDimensionalInjectiveLinearMapping(tmp);
			}
		} else if (dimSource == dimTarget) {
			final Endomorphism ans = new LinearSelfMapping(source, coordinates);
			if (ans.getRank() == dimSource) {
				return new InvertibleFiniteDimensionalLinearMapping(source, coordinates);
			}
			return ans;
		}
		return new FiniteDimensionalLinearMapping(source, target, coordinates);
	}

	@Override
	public IFiniteDimensionalLinearMapping getFiniteDimensionalLinearMapping(CoordinateSpace source,
			CoordinateSpace target, Map<Vector, Map<Vector, Double>> coordinates) throws Throwable {
		if (source instanceof IFiniteDimensionalFunctionSpace) {
			return new FunctionSpaceOperator((IFiniteDimensionalFunctionSpace) source,
					(IFiniteDimensionalFunctionSpace) target, coordinates);
		}
		final int dimSource = source.dim();
		final int dimTarget = target.dim();
		if (dimSource < dimTarget) {
			IFiniteDimensionalLinearMapping tmp = 
					new FiniteDimensionalLinearMapping(source, target, coordinates);
			if (dimSource == tmp.getRank()) {
				return new FiniteDimensionalInjectiveLinearMapping(tmp);
			}
		} else if (dimSource == dimTarget) {
			final Endomorphism ans = new LinearSelfMapping(source, coordinates);
			if (ans.getRank() == dimSource) {
				return new InvertibleFiniteDimensionalLinearMapping(source, coordinates);
			}
			return ans;
		}
		return new FiniteDimensionalLinearMapping(source, target, coordinates);
	}

}