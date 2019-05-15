package definitions.structures.generic.finitedimensional.defs.mappings;

import java.util.HashMap;
import java.util.Map;

import definitions.structures.generic.finitedimensional.defs.spaces.IFiniteDimensionalVectorSpace;
import definitions.structures.generic.finitedimensional.defs.subspaces.IFiniteDimensionalSubSpace;
import definitions.structures.generic.finitedimensional.defs.vectors.FiniteVector;
import definitions.structures.generic.finitedimensional.defs.vectors.IFiniteVector;

public interface IFiniteDimensionalLinearMapping {

	IFiniteVector solve(IFiniteVector image) throws Throwable;

	IFiniteDimensionalVectorSpace getSource();

	IFiniteDimensionalVectorSpace getTarget();

	Map<IFiniteVector, Map<IFiniteVector, Double>> getLinearity();

	default Map<IFiniteVector, Double> getLinearity(IFiniteVector vec1) {
		return getLinearity().get(vec1);
	}

	default IFiniteVector get(IFiniteVector vec1) throws Throwable {
		if (getSource() instanceof IFiniteDimensionalSubSpace) {
			return getOnSubSpace(vec1);
		}
		final Map<IFiniteVector, Double> coordinates = vec1.getCoordinates();
		IFiniteVector ans = new FiniteVector(new double[getTarget().genericBaseToList().get(0).getDim()]);
		for (final IFiniteVector src : getSource().genericBaseToList()) {
			ans = (IFiniteVector) getTarget().add(ans,
					(IFiniteVector) getTarget().stretch(getColumn(src), coordinates.get(src)));
		}
		return ans;
	}

	default IFiniteVector getOnSubSpace(IFiniteVector vec1) throws Throwable {
		IFiniteVector inverseVector = new FiniteVector(
				((IFiniteDimensionalSubSpace) getSource()).getInverseCoordinates(vec1));
		IFiniteDimensionalLinearMapping mapOnSourceSpaces = MappingGenerator.getInstance()
				.getFiniteDimensionalLinearMapping(this.getGenericMatrix());
		IFiniteDimensionalLinearMapping composedMapping;
		if (getTarget() instanceof IFiniteDimensionalSubSpace) {
			composedMapping = MappingGenerator.getInstance()
					.getComposition(((IFiniteDimensionalSubSpace) getTarget()).getParametrization(), mapOnSourceSpaces);
		} else {
			composedMapping = mapOnSourceSpaces;
		}
		return composedMapping.get(inverseVector);

	}

	default IFiniteVector getColumn(IFiniteVector vec) throws Throwable {
		// if (vec.getDim() > getSource().dim()) {
		// throw new Throwable();
		// }
		final Map<IFiniteVector, Double> coordinates = new HashMap<>();
		for (final IFiniteVector vec1 : getTarget().genericBaseToList()) {
			coordinates.put(vec1, getLinearity().get(vec).get(vec1));
		}
		return new FiniteVector(coordinates);
	}

	default double[][] getGenericMatrix() throws Throwable {
		final double[][] matrix = new double[getTarget().dim()][getSource().dim()];
		int i = 0;
		for (final IFiniteVector vec1 : getSource().genericBaseToList()) {
			int j = 0;
			for (final IFiniteVector vec2 : getTarget().genericBaseToList()) {
				matrix[j][i] = getLinearity(vec1).get(vec2);
				j++;
			}
			i++;
		}
		return matrix;
	}

	default void swap(double[][] mat, int row1, int row2, int col) {
		for (int i = 0; i < col; i++) {
			double temp = mat[row1][i];
			mat[row1][i] = mat[row2][i];
			mat[row2][i] = temp;
		}
	}

	default int getRank() throws Throwable {
		double[][] mat = getGenericMatrix();
		int r = mat.length;
		int c = mat[0].length;
		int rank = c;

		for (int row = 0; row < rank; row++) {
			if (mat[row][row] != 0) {
				for (int col = 0; col < r; col++) {
					if (col != row) {
						double mult = mat[col][row] / mat[row][row];
						for (int i = 0; i < rank; i++) {
							mat[col][i] -= mult * mat[row][i];
						}
					}
				}
			} else {
				boolean reduce = true;
				for (int i = row + 1; i < r; i++) {
					if (mat[i][row] != 0) {
						swap(mat, row, i, rank);
						reduce = false;
						break;
					}
				}
				if (reduce) {
					rank--;
					for (int i = 0; i < r; i++) {
						mat[i][row] = mat[i][rank];
					}
				}
				row--;
			}
		}
		return rank;
	}

}
