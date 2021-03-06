package definitions.aspectjtest;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import definitions.prototypes.AspectJTest;
import definitions.structures.abstr.algebra.fields.Field;
import definitions.structures.abstr.algebra.fields.impl.ComplexPlane;
import definitions.structures.abstr.algebra.fields.scalars.Scalar;
import definitions.structures.abstr.algebra.fields.scalars.impl.Complex;
import definitions.structures.abstr.algebra.groups.Group;
import definitions.structures.abstr.algebra.semigroups.Element;
import definitions.structures.abstr.mappings.VectorSpaceSelfMapping;
import definitions.structures.abstr.vectorspaces.VectorSpace;
import definitions.structures.abstr.vectorspaces.vectors.Function;
import definitions.structures.abstr.vectorspaces.vectors.Vector;
import definitions.structures.dynamicsystems.DynamicSystem;
import definitions.structures.euclidean.vectors.impl.GenericFunction;
import definitions.structures.euclidean.vectorspaces.EuclideanSpace;

@SuppressWarnings("serial")
public class TwoDimDynSysTest extends AspectJTest {

	final int duration=1000;
	final EuclideanSpace complexPhaseSpace = (EuclideanSpace) getSpaceGenerator().getFiniteDimensionalComplexSpace(1);
	final EuclideanSpace realPhaseSpace = getSpaceGenerator().getFiniteDimensionalVectorSpace(2);

	final Complex complexInitialCondition = ((ComplexPlane) complexPhaseSpace).get(0.9,0.1);
	final Vector realInitialCondition = realPhaseSpace.genericBaseToList().get(0);

	final VectorSpaceSelfMapping complexMapping = new VectorSpaceSelfMapping() {

		@Override
		public Element get(Element vec) {
			double a=((Complex) vec).getReal().getDoubleValue();
			double b=((Complex) vec).getImag().getDoubleValue();
			Element newVec = getComplexPlane().get(a-a*a,b);
			return getComplexPlane().multiplication(newVec,
					getComplexPlane().multiplication(getComplexPlane().getMinusOne(), getComplexPlane().getI()));
		}

		@Override
		public VectorSpace getSource() {
			return complexPhaseSpace;
		}

	};

	final DynamicSystem complexSystem = new DynamicSystem() {

		@Override
		public Group getPhaseSpace() {
			return complexPhaseSpace;
		}

		@Override
		public VectorSpaceSelfMapping getDefiningMapping() {
			return complexMapping;
		}

	};

	@Test
	public void complexDynamicSystemTest() {
		List<Complex> list = new ArrayList<>();
		list.add(complexInitialCondition);
		Complex last;
		for (int i = 0; i < duration; i++) {
			last = list.get(list.size() - 1);
			list.add((Complex) complexSystem.getEvolutionOperator(getIntegers().get((double) i)).get(last));
		}
		Function test = new GenericFunction() {

			@Override
			public Field getField() {
				return getRealLine();
			}

			@Override
			public Vector value(Scalar input) {
				return list.get((int) (duration/2 * (1 + input.getDoubleValue() / Math.PI))).getReal();
			}

		};

		test.plot(-Math.PI, Math.PI);
	}

}
