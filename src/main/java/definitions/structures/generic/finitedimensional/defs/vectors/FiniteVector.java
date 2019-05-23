package definitions.structures.generic.finitedimensional.defs.vectors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import definitions.structures.abstr.Vector;
import definitions.structures.abstr.VectorSpace;
import definitions.structures.generic.finitedimensional.defs.spaces.FiniteDimensionalVectorSpace;
import definitions.structures.generic.finitedimensional.defs.spaces.SpaceGenerator;

public class FiniteVector implements IFiniteVector {

	final int dim;

	@Override
	public final int getDim() {
		return dim;
	}

	private Map<IFiniteVector, Double> coordinates;

	public FiniteVector(double[] coordinates) throws Throwable {
		dim = coordinates.length;
		setCoordinates(new HashMap<>());
		int i = 0;
		for (final IFiniteVector vec : SpaceGenerator.getInstance().getFiniteDimensionalVectorSpace(dim)
				.genericBaseToList()) {
			getCoordinates().put(vec, coordinates[i++]);
		}
	}

	public FiniteVector(Map<IFiniteVector, Double> coordinates) throws Throwable {
		setCoordinates(coordinates);
		dim = coordinates.keySet().size();
	}

	protected FiniteVector(int dim) {
		this.dim = dim;
		coordinates = new HashMap<>();
	}

	@Override
	public boolean elementOf(VectorSpace space) throws Throwable {
		if (space instanceof FiniteDimensionalVectorSpace && ((FiniteDimensionalVectorSpace) space).dim() == dim) {
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object vec){
		if (!(vec instanceof Vector)){
			return false;
		}
		else if (vec instanceof FiniteVector && ((FiniteVector) vec).dim == dim) {
			return getCoordinates().equals(((FiniteVector) vec).getCoordinates());
		}
		return false;
	}

	@Override
	public String toString() {
		String str = "";
		try {
			for (final Vector vec : getGenericBase()) {
				str += getCoordinates().get(vec) + "\r";
			}
			return str;
		} catch (final Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Problems occured...";
		}
	}

	@Override
	public Map<IFiniteVector, Double> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Map<IFiniteVector, Double> coordinates) {
		this.coordinates = coordinates;
	}

	@Override
	public Set<IFiniteVector> getGenericBase() throws Throwable {
		return new HashSet<IFiniteVector>(getCoordinates().keySet());
	}

	@Override
	public boolean equals(Vector vec) throws Throwable {
		return equals((Object)vec);
	}

}
