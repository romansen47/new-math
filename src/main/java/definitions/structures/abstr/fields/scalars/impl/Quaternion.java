package definitions.structures.abstr.fields.scalars.impl;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import definitions.structures.abstr.fields.Field;
import definitions.structures.abstr.fields.impl.QuaternionSpace;
import definitions.structures.abstr.fields.impl.RealLine;
import definitions.structures.abstr.fields.scalars.Scalar;
import definitions.structures.abstr.vectorspaces.VectorSpace;
import definitions.structures.abstr.vectorspaces.vectors.Vector;
import definitions.structures.euclidean.vectors.impl.Tuple;
import definitions.structures.euclidean.vectorspaces.EuclideanSpace;

@XmlRootElement
public class Quaternion extends Tuple implements Scalar {

	@XmlElement
	private static final long serialVersionUID = -7587290161110602891L;

	@XmlElement
	private final Scalar real;

	@XmlElement
	private final Scalar i;

	@XmlElement
	private final Scalar j;

	@XmlElement
	private final Scalar k;

	@XmlElement
	private Map<Vector, Scalar> coordinates;

	public Quaternion(final double r, final double i, final double j, final double k) {
		this(RealLine.getInstance().get(r), RealLine.getInstance().get(i), RealLine.getInstance().get(j),
				RealLine.getInstance().get(k));
	}

	public Quaternion(final Scalar re, final Scalar i, final Scalar j, final Scalar k) {
		super(4);
		this.real = re;
		this.i = i;
		this.j = j;
		this.k = k;
	}

	@Override
	public boolean elementOf(final VectorSpace space) {
		return space == QuaternionSpace.getInstance();
	}

	@Override
	public boolean equals(final Object vec) {
		return vec instanceof Quaternion && ((Quaternion) vec).getReal().equals(this.getReal())
				&& ((Quaternion) vec).getI().equals(this.getI()) && ((Quaternion) vec).getJ().equals(this.getJ())
				&& ((Quaternion) vec).getK().equals(this.getK());
	}

	@Override
	public Map<Vector, Scalar> getCoordinates() {
		if (this.coordinates == null) {
			this.coordinates = new HashMap<>();
			this.coordinates.put(((QuaternionSpace) QuaternionSpace.getInstance()).getOne(), this.getReal());
			this.coordinates.put(((QuaternionSpace) QuaternionSpace.getInstance()).getI(), this.getI());
			this.coordinates.put(((QuaternionSpace) QuaternionSpace.getInstance()).getJ(), this.getJ());
			this.coordinates.put(((QuaternionSpace) QuaternionSpace.getInstance()).getK(), this.getK());
		}
		return this.coordinates;
	}

	@XmlAttribute
	@Override
	public Map<Vector, Scalar> getCoordinates(final EuclideanSpace source) {
		return this.getCoordinates();
	}

	@Override
	public Integer getDim() {
		return 4;
	}

	/**
	 * @return the i
	 */
	@XmlAttribute
	public Scalar getI() {
		return this.i;
	}

	@XmlAttribute
	@Override
	public Scalar getInverse() {
		return (Scalar) ((Field) QuaternionSpace.getInstance()).inverse(this);
	}

	/**
	 * @return the j
	 */
	@XmlAttribute
	public Scalar getJ() {
		return this.j;
	}

	/**
	 * @return the k
	 */
	@XmlAttribute
	public Scalar getK() {
		return this.k;
	}

	/**
	 * @return the real
	 */
	@XmlAttribute
	public Scalar getReal() {
		return this.real;
	}

	@XmlAttribute
	@Override
	public double getValue() {
		return this.getReal().getValue();
	}

	@Override
	public void setCoordinates(final Map<Vector, Scalar> coordinates) {
		this.coordinates = coordinates;
	}

	@Override
	public void setCoordinates(final Map<Vector, Scalar> coordinates, final EuclideanSpace space) {
	}

	@Override
	@XmlAttribute
	public String toString() {
		return "(" + this.getReal().getValue() + "," + this.getI().getValue() + "," + this.getJ().getValue() + ","
				+ this.getK().getValue() + ")";
	}

}
