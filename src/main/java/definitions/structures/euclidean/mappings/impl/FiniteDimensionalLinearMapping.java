package definitions.structures.euclidean.mappings.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import definitions.structures.abstr.algebra.fields.scalars.Scalar;
import definitions.structures.abstr.mappings.impl.LinearMapping;
import definitions.structures.abstr.vectorspaces.VectorSpace;
import definitions.structures.abstr.vectorspaces.vectors.Function;
import definitions.structures.abstr.vectorspaces.vectors.Vector;
import definitions.structures.euclidean.mappings.FiniteDimensionalHomomorphism;
import definitions.structures.euclidean.vectorspaces.EuclideanSpace;

/**
 * Finite dimensional linear mapping
 * 
 * @author ro
 *
 */
public class FiniteDimensionalLinearMapping extends LinearMapping implements FiniteDimensionalHomomorphism {

	/**
	 * 
	 */
	private static final long serialVersionUID = -261334109954833773L;
	
	public FiniteDimensionalLinearMapping(final EuclideanSpace source, final EuclideanSpace target) {
		super(source, target);
	}

	/**
	 * Constructor.
	 * 
	 * @param source      the source vector space.
	 * @param target      the target vector space.
	 * @param coordinates the coordinates mapping.
	 */
	public FiniteDimensionalLinearMapping(final EuclideanSpace source, final EuclideanSpace target,
			final Map<Vector, Map<Vector, Scalar>> coordinates) {
		super(source, target);
		this.linearity = coordinates;
		this.genericMatrix = new Scalar[((EuclideanSpace) this.getTarget())
				.getDim()][((EuclideanSpace) this.getSource()).getDim()];
		int i = 0;
		for (final Vector vec1 : source.genericBaseToList()) {
			int j = 0;
			for (final Vector vec2 : target.genericBaseToList()) {
				this.genericMatrix[j][i] = this.getImageVectorOfBaseVector(source.getBaseVec(vec1))
						.get(target.getBaseVec(vec2));
				j++;
			}
			i++;
		}
	}

	public FiniteDimensionalLinearMapping(final EuclideanSpace source, final EuclideanSpace target,
			final Map<Vector, Map<Vector, Scalar>> coordinates, final Scalar[][] matrix) {
		this(source, target, coordinates);
		this.genericMatrix = matrix;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Scalar[][] getGenericMatrix() {
		if (!((this.source instanceof EuclideanSpace) && (this.target instanceof EuclideanSpace))) {
			return null;
		}
		if (this.genericMatrix == null) {
			if (this.linearity == null) {
				this.linearity = new HashMap<>();
				for (final Vector sourceVec : ((EuclideanSpace) this.getSource()).genericBaseToList()) {
					this.linearity.put(sourceVec,
							((Function) this.get(sourceVec)).getCoordinates((EuclideanSpace) this.target));
				}
			}
			this.genericMatrix = new Scalar[((EuclideanSpace) this.getTarget())
					.getDim()][((EuclideanSpace) this.getSource()).getDim()];
			int i = 0;
			for (final Vector vec1 : ((EuclideanSpace) this.getSource()).genericBaseToList()) {
				int j = 0;
				for (final Vector vec2 : ((EuclideanSpace) this.getTarget()).genericBaseToList()) {
					this.genericMatrix[j][i] = this.getImageVectorOfBaseVector(vec1).get(vec2);
					j++;
				}
				i++;
			}
		}
		return this.genericMatrix;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Vector, Scalar> getImageVectorOfBaseVector(final Vector vec1) {
		return this.linearity.get(vec1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Vector, Map<Vector, Scalar>> getLinearity() {
		if (this.linearity == null) {
			this.linearity = new ConcurrentHashMap<>();
			for (final Vector vec1 : ((EuclideanSpace) this.source).genericBaseToList()) {
				final Vector tmp = this.get(vec1);
				final Map<Vector, Scalar> tmpCoord = new ConcurrentHashMap<>();
				for (final Vector vec2 : ((EuclideanSpace) this.target).genericBaseToList()) {
					tmpCoord.put(vec2, ((EuclideanSpace) this.target).innerProduct(vec2, tmp));
				}
				this.linearity.put(vec1, tmpCoord);
			}
		}
		return this.linearity;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VectorSpace getSource() {
		return this.source;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VectorSpace getTarget() {
		return this.target;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String str = "";
		Scalar[][] matrix;
		try {
			matrix = this.getGenericMatrix();
			double x;
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[i].length; j++) {
					x = matrix[i][j].getDoubleValue();
					str += " " + (x - (x % 0.001)) + " ";
				}
				str += " \r";
			}
		} catch (final Throwable e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toXml() {
		String ans = "<linearMapping>";
		ans += "<source>" + this.source.toXml() + "</source>";
		ans += "<target>" + this.target.toXml() + "</target>";
		ans += "<base>";
		for (final Vector vec1 : ((EuclideanSpace) this.source).genericBaseToList()) {
			for (final Vector vec2 : ((EuclideanSpace) this.target).genericBaseToList()) {
				ans += "<sourceVector>";
				ans += vec1.toXml();
				ans += "</sourceVector>";

				ans += "<targetVector>";
				ans += vec2.toXml();
				ans += "</targetVector>";

				ans += "<value>";
				ans += this.getImageVectorOfBaseVector(vec1).get(vec2).toXml();
				ans += "</value>";
			}
		}
		ans += "</base>";
		ans += "</linearMapping>";
		return ans;
	}

}
