package definitions.structures.abstr.algebra.fields.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import definitions.Unweavable;
import definitions.structures.abstr.algebra.fields.IFieldGenerator;
import definitions.structures.abstr.algebra.groups.impl.BinaryField;
import definitions.structures.euclidean.vectorspaces.impl.FiniteDimensionalVectorSpace;

@Service
public class FieldGenerator implements IFieldGenerator, Unweavable {

	private static IFieldGenerator instance;

	public static IFieldGenerator getInstance() {
		return instance;
	}

	public static IFieldGenerator getInstance(final FieldGenerator fieldGenerator) {
		return instance;
	}

	public static void setInstance(final FieldGenerator fieldGenerator) {
		instance = fieldGenerator;
		((FieldGenerator) instance).setRealLine(fieldGenerator.getRealLine());
	}

	@Autowired
	private RealLine realLine;

	@Autowired
	private BinaryField binaryField;

	public FieldGenerator() {
	}

	public BinaryField getBinaryField() {
		if (BinaryField.getInstance() == null) {
			BinaryField.setInstance(this.binaryField);
		}
		return this.binaryField;
	}

	@Override
	public RealLine getRealLine() {
		return this.realLine;
	}

	public void setBinaryField(final BinaryField binaryField) {
		this.binaryField = binaryField;
	}

	public void setRealLine(final RealLine realLine) {
		this.realLine = realLine;
		RealLine.setInstance(realLine);
		ComplexPlane.setRealLine(realLine);
		((FiniteDimensionalVectorSpace) QuaternionSpace.getInstance()).setField(realLine);
	}

}
