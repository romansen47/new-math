package definitions.structures.abstr.fields.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import definitions.Unweavable;
import definitions.structures.abstr.fields.IFieldGenerator;
import definitions.structures.euclidean.vectorspaces.impl.FiniteDimensionalVectorSpace;

@Configuration
public class FieldGenerator implements IFieldGenerator, Unweavable {

	private static IFieldGenerator instance;

	@Autowired
	private RealLine realLine;

	public IFieldGenerator getInstance() {
		return instance;
	}

	public FieldGenerator() {
	}

	public RealLine getRealLine() {
		return realLine;
	}

	public void setRealLine(RealLine realLine) {
		this.realLine = realLine;
		RealLine.setInstance(realLine);
		ComplexPlane.setRealLine(realLine);
		((FiniteDimensionalVectorSpace)QuaternionSpace.getInstance()).setField(realLine);
	}

	public static void setInstance(FieldGenerator fieldGenerator) {
		instance = fieldGenerator;
		((FieldGenerator) instance).setRealLine(fieldGenerator.getRealLine());
	}

}
