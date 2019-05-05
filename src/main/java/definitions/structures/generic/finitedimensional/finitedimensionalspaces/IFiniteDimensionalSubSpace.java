package definitions.structures.generic.finitedimensional.finitedimensionalspaces;

import definitions.structures.abstr.IVector;
import definitions.structures.generic.finitedimensional.finitedimensionalspaces.linearmappings.IFiniteDimensionalLinearMapping;

public interface IFiniteDimensionalSubSpace extends IFiniteDimensionalVectorSpace {

	IFiniteDimensionalVectorSpace getSuperSpace();

	@Override
	boolean contains(IVector vec);
	
	@Override
	default int dim() {
		return getMap().getSource().dim()-getMap().getRank();
	}

	IFiniteDimensionalLinearMapping getMap();
}
