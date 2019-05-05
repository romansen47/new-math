package definitions.structures.generic.finitedimensional.finitedimensionalspaces;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import definitions.structures.abstr.IVector;

public class FiniteDimensionalVectorSpace implements IFiniteDimensionalVectorSpace {

	final List<FiniteVector> base;

	final int dim;

	protected FiniteDimensionalVectorSpace(List<FiniteVector> basetmp) throws Throwable {
		dim = basetmp.size();
		base = basetmp;
	}

	@Override
	public double product(IVector vec1, IVector vec2) throws Throwable {
		if (!(vec1 instanceof FiniteVector && vec2 instanceof FiniteVector)) {
			throw new Throwable();
		}
		double prod = 0;
		final Map<IFiniteVector, Double> vecCoord1 = ((FiniteVector) vec1).getGenericCoordinates();
		final Map<IFiniteVector, Double> vecCoord2 = ((FiniteVector) vec2).getGenericCoordinates();
		final List<FiniteVector> base = getGenericBase();
		for (final IFiniteVector vec : base) {
			prod += vecCoord1.get(vec) * vecCoord2.get(vec);
		}
		return prod;
	}

	@Override
	public boolean contains(IVector vec) throws Throwable {
		return (vec instanceof FiniteVector && vec.getDim() == dim());
	}

	@Override
	public IVector nullVec() throws Throwable {
		return new FiniteVector(new double[base.size()]);
	}

	@Override
	public final List<FiniteVector> getGenericBase() {
		return base;
	}

	@Override
	public int dim() {
		return dim;
	}

	@Override
	public Set<IVector> baseToArray() {
		return new HashSet<>(getGenericBase());
	}

}