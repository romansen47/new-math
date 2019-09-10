package definitions.structures.euclidean.mappings.impl;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.aop.lib.Trace;
import com.aop.lib.Operation;

import definitions.structures.abstr.fields.impl.RealLine;
import definitions.structures.abstr.fields.scalars.Scalar;
import definitions.structures.abstr.fields.scalars.impl.Real;
import definitions.structures.abstr.mappings.Homomorphism;
import definitions.structures.abstr.vectorspaces.VectorSpace;
import definitions.structures.abstr.vectorspaces.vectors.Vector;
import definitions.structures.euclidean.vectorspaces.EuclideanSpace;

public class FiniteDimensionalLinearMappingTest {

	Map<Vector, Map<Vector, Scalar>> ans1;
	Scalar[][] ans2;
	VectorSpace space = RealLine.getInstance();

	Homomorphism lin = new FiniteDimensionalLinearMapping((EuclideanSpace) this.space,
			(EuclideanSpace) this.space) {
		private static final long serialVersionUID = 8542796160933542925L;

		@Override
		public Vector get(Vector vec) {
			return ((EuclideanSpace) FiniteDimensionalLinearMappingTest.this.space).stretch(vec, new Real(5));
		}
	};

	@Before
	public void beforeClass() {
		this.ans1 = this.lin.getLinearity();
		this.ans2 = this.lin.getGenericMatrix();
	}

	public static void main(String[] args) {
		FiniteDimensionalLinearMappingTest test=new FiniteDimensionalLinearMappingTest();
		test.beforeClass();
		test.testGetLinearity();
		test.testGetGenericMatrix();
	}
	
	@Test
	public void testGetLinearity() {
//		Traced.show();
		Assert.assertTrue(
				this.ans1.get(((RealLine) this.space).getOne()).get(((RealLine) this.space).getOne()).getValue() == 5.);
	}

	@Test
	public void testGetGenericMatrix() {
//		Traced.show();
		Assert.assertTrue(this.ans2[0][0].getValue() == 5.);
//		Traced.show();
	}
}
