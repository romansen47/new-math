package definitions.structures.abstr.fields.scalars.impl;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;

import definitions.structures.abstr.fields.Field;
import definitions.structures.abstr.fields.impl.ComplexPlane;
import definitions.structures.abstr.fields.impl.RealLine;
import definitions.structures.abstr.fields.scalars.Scalar;
import definitions.structures.abstr.vectorspaces.VectorSpace;
import definitions.structures.abstr.vectorspaces.vectors.Vector;
import definitions.structures.euclidean.vectors.impl.Tuple;
import definitions.structures.euclidean.vectorspaces.EuclideanSpace;

public class Complex extends Tuple implements Scalar {

	@XmlElement
	private static final long serialVersionUID = 2160805146914088274L;

	@XmlElement
	private Scalar real;

	@XmlElement
	private Scalar imag;

	@XmlElement
	private Map<Vector, Scalar> coordinates;

	public Complex(final double x, final double y) {
		this(RealLine.getInstance().get(x), RealLine.getInstance().get(y));
	}

	public Complex(final Scalar re, final Scalar im) {
		super(2);
		this.real = re;
		this.imag = im;
	}

	@Override
	public boolean elementOf(final VectorSpace space) {
		return space == ComplexPlane.getInstance();
	}

	@Override
	public boolean equals(final Object vec) {
		return vec instanceof Complex && ((Complex) vec).getReal().equals(this.getReal())
				&& ((Complex) vec).getImag().equals(this.getImag());
	}

	@Override
	public Map<Vector, Scalar> getCoordinates() {
		this.coordinates = new HashMap<>();
		this.coordinates.put(((Field) ComplexPlane.getInstance()).getOne(), this.getReal());
		this.coordinates.put(((ComplexPlane) ComplexPlane.getInstance()).getI(), this.getImag());
		return this.coordinates;
	}

	@Override
	public Map<Vector, Scalar> getCoordinates(final EuclideanSpace source) {
		return this.getCoordinates();
	}

	@Override
	public Integer getDim() {
		return 2;
	}

	/**
	 * @return the imag
	 */
	public Scalar getImag() {
		return this.imag;
	}

	@Override
	public Scalar getInverse() {
		return (Scalar) ((Field) ComplexPlane.getInstance()).inverse(this);
	}

	/**
	 * @return the real
	 */
	public Scalar getReal() {
		return this.real;
	}

	@Override
	public double getValue() {
		return this.getReal().getValue();
	}

	// @Override
	// public String toString() {
	// return "( " + this.getReal().getValue() + " ) + i * ( " +
	// this.getImag().getValue() + " )";
	// }

	@Override
	public void setCoordinates(final Map<Vector, Scalar> coordinates) {
		this.coordinates = coordinates;
		this.real = this.coordinates.get(((Field) ComplexPlane.getInstance()).getOne());
	}

	@Override
	public void setCoordinates(final Map<Vector, Scalar> coordinates, final EuclideanSpace space) {
	}

//	@Override
//	public Set<Vector> getGenericBase() {
//		return ComplexPlane.getInstance().getGenericBase();
//	}

	public void setValue(final double realValue, final double imValue) {
		this.real = RealLine.getInstance().get(realValue);
		this.imag = RealLine.getInstance().get(realValue);

	}

	@Override
	public String toString() {
		return "<real>" + this.getReal().getValue() + "</real>\r" + "<imag>" + this.getImag().getValue() + "</imag>\r";
	}

	@Override
	public String toXml() {
		return "<complex>\r" + this.real.toXml() + this.imag.toXml() + "</complex>\r";
	}
}
