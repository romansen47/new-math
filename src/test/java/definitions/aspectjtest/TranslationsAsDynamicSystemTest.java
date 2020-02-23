package definitions.aspectjtest;

import java.util.ArrayList;
import java.util.List;

import definitions.SpringConfiguration;
import definitions.structures.abstr.algebra.fields.Field;
import definitions.structures.abstr.algebra.fields.impl.RealLine;
import definitions.structures.abstr.algebra.fields.scalars.Scalar;
import definitions.structures.abstr.algebra.fields.scalars.impl.Real;
import definitions.structures.abstr.algebra.groups.Group;
import definitions.structures.abstr.algebra.semigroups.Element;
import definitions.structures.abstr.mappings.VectorSpaceSelfMapping;
import definitions.structures.abstr.vectorspaces.LinearMappingsSpace;
import definitions.structures.abstr.vectorspaces.VectorSpace;
import definitions.structures.abstr.vectorspaces.vectors.Function;
import definitions.structures.abstr.vectorspaces.vectors.Vector;
import definitions.structures.dynamicsystems.DynamicSystem;
import definitions.structures.euclidean.mappings.impl.DerivativeOperator;
import definitions.structures.euclidean.vectors.impl.GenericFunction;
import definitions.structures.euclidean.vectors.specialfunctions.Sine;
import definitions.structures.euclidean.vectorspaces.EuclideanSpace;
import definitions.structures.euclidean.vectorspaces.impl.SpaceGenerator;
import definitions.structures.euclidean.vectorspaces.impl.TrigonometricSobolevSpace;
import processing.template.Gui;
import solver.StdDraw;

public class TranslationsAsDynamicSystemTest extends Gui {

	private static SpringConfiguration springConfiguration;
	private static DynamicSystem differentialEquation;
	private static EuclideanSpace functionSpace;
	private static Function initialCondition;
	private static int degree = 10;
	private static int sobolevDegree = 1;
	private static Field realLine;
	private static EuclideanSpace space;
	private static TranslationsAsDynamicSystemTest test;
	private static Function tmp;

	final int iterations = 100000;
	final double eps = 1.e-3;
	int iteration = 0;

	List<Function> list = new ArrayList<>();
	private int speed = 20;

	public static void main(String[] args) {
		setSpringConfiguration(SpringConfiguration.getSpringConfiguration());
		setRealLine(RealLine.getInstance());
		test = new TranslationsAsDynamicSystemTest();
		setFunctionSpace(
				SpaceGenerator.getInstance().getTrigonometricSobolevSpace(getRealLine(), degree, sobolevDegree));
		VectorSpaceSelfMapping map = ((TrigonometricSobolevSpace) getFunctionSpace()).getDerivativeBuilder();
		VectorSpaceSelfMapping nonlinearity = new VectorSpaceSelfMapping() {

			@Override
			public Element get(Element vec) {
				return new GenericFunction() {

					@Override
					public Scalar value(Scalar input) {
						return (Scalar) getRealLine().product(((Function) vec).value(input),
								((Function) map.get(vec)).value(input));
					}

				};
			}

			@Override
			public VectorSpace getSource() {
				return getFunctionSpace();
			}

		};
		VectorSpaceSelfMapping newMap = new VectorSpaceSelfMapping() {

			@Override
			public Element get(Element vec) {
				Vector newVec = getSource().stretch(((DerivativeOperator) map).get((Vector) vec, 3),RealLine.getInstance().get(0.1));
				newVec = getSource().addition((Vector) map.get(vec),
						getSource().stretch((Vector) map.get(newVec), (Scalar) getRealLine().getMinusOne()));
//				newVec = getSource()
//						.addition(
//								getSource().addition((Vector) map.get(vec),
//										getSource().stretch((Vector) map.get(newVec),
//												(Scalar) getRealLine().getMinusOne())),
//								((EuclideanSpace) getSource()).getCoordinates((Vector) nonlinearity.get(vec)));
				return newVec;
			}

			@Override
			public VectorSpace getSource() {
				return getFunctionSpace();
			}

		};
		setInitialCondition(new GenericFunction() {

			double support = 2.0;

			@Override
			public Scalar value(Scalar input) {
				double val = input.getDoubleValue();
				if (val <= -support / 2 || val >= support / 2) {
					return RealLine.getInstance().get(0);
				}
				return RealLine.getInstance().get(5*(1+Math.cos(2*Math.PI*val))*Math.exp(-1 / (Math.pow(support/2, 2) - Math.pow(val, 2))));
			}

		});
		LinearMappingsSpace linearMappingsSpace = new LinearMappingsSpace(getFunctionSpace(), getFunctionSpace());
		setDifferentialEquation(new DynamicSystem() {

			@Override
			public Group getPhaseSpace() {
				return getFunctionSpace();
			}

			@Override
			public VectorSpaceSelfMapping getDefiningMapping() {
				return newMap;
			}

		});
		space = (EuclideanSpace) getDifferentialEquation().getPhaseSpace();
//		tmp = initialCondition;
		tmp = (Function) space.getCoordinates(initialCondition);
		initialCondition.plotCompare(-Math.PI, Math.PI, tmp);
		((Gui) test).run("definitions.aspectjtest.TranslationsAsDynamicSystemTest");
	}

	@Override
	public void settings() {
		size(1000, 1000);
	}

	@Override
	public void setup() {
		frameRate(60);
		drawPreparationsWindows();
		int size = 20;
		int sizeOfRect = (xScale - deltaX) / size;
		int deltaIt = iterations / size;
		list.add(tmp);
		int count = 0;
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.text(-xScale + deltaX, 1 * sizeOfRect, "loading...");
		StdDraw.text(-xScale + deltaX, 1.5 * sizeOfRect, "loading...");
		StdDraw.text(-xScale + deltaX, 2 * sizeOfRect, "loading...");
		StdDraw.text(-xScale + deltaX, 2.5 * sizeOfRect, "loading...");
		StdDraw.text(-xScale + deltaX, 3 * sizeOfRect, "loading...");
		StdDraw.text(-xScale + deltaX, 3.5 * sizeOfRect, "loading...");
		for (int i = 0; i < iterations; i++) {
			Function tmp2 = (Function) space.addition(tmp, space
					.stretch((Function) differentialEquation.getDefiningMapping().get(tmp), getRealLine().get(eps)));
			list.add(tmp2);
			tmp = tmp2;
			if (i % deltaIt == 0) {
				StdDraw.setPenColor(StdDraw.GREEN);
//				StdDraw.filledSquare(-xScale+deltaX + count * sizeOfRect, 3*sizeOfRect, sizeOfRect);
				StdDraw.square(-xScale + 2 * deltaX + count * sizeOfRect, sizeOfRect, sizeOfRect/2);
				count++;
			}
		}
//		getInitialCondition().plot(-Math.PI, Math.PI);
	}

	StdDraw stddraw;
	int xScale = 400;
	int yScale = xScale / 2;
	int deltaX = 100;

	private void drawPreparationsWindows() {
		stddraw = new StdDraw();
		stddraw.setCanvasSize(2 * xScale + deltaX, yScale);
		StdDraw.setXscale(-xScale, xScale);
		StdDraw.setYscale(-yScale, yScale);
		StdDraw.setPenRadius(0.001);
	}

	@Override
	public void draw() {
		clear();
		background(255);
		line(500, 0, 500, 1000);
		line(0, 500, 1000, 500);

		stroke(0);
		fill(0);
		text("time: " + iteration + " * " + eps + " = " + iteration++ * eps, 200, 200, 15);
		Function tmp = list.get(iteration);

//		tmp.plot(-Math.PI,Math.PI);
		draw(tmp);
		if (tmp.getDerivative().equals(getFunctionSpace().nullVec()) || iteration >= iterations) {
			System.exit(0);
		}
		iteration += speed;
	}

	private void draw(Function tmp2) {
		double x1 = -Math.PI;
		Real realX1;
		double x2 = x1 + eps;
		Real realX2;
		while (x2 < Math.PI) {
			x2 = x1 + eps;
			realX1 = (Real) getRealLine().get(x1);
			realX2 = (Real) getRealLine().get(x2);
			line(500 + 100 * (float) x1, (float) (500 * (1 - tmp2.value(realX1).getDoubleValue())),
					500 + 100 * (float) x2, (float) (500 * (1 - tmp2.value(realX2).getDoubleValue())));
			x1 = x2;
		}
	}

	/**
	 * @return the differentialEquation
	 */
	public static DynamicSystem getDifferentialEquation() {
		return differentialEquation;
	}

	/**
	 * @param differentialEquation the differentialEquation to set
	 */
	public static void setDifferentialEquation(DynamicSystem differentialEquation) {
		TranslationsAsDynamicSystemTest.differentialEquation = differentialEquation;
	}

	/**
	 * @return the functionSpace
	 */
	public static EuclideanSpace getFunctionSpace() {
		return functionSpace;
	}

	/**
	 * @param functionSpace the functionSpace to set
	 */
	public static void setFunctionSpace(EuclideanSpace functionSpace) {
		TranslationsAsDynamicSystemTest.functionSpace = functionSpace;
	}

	/**
	 * @return the initialCondition
	 */
	public static Function getInitialCondition() {
		return initialCondition;
	}

	/**
	 * @param initialConditio)n the initialCondition to set
	 */
	public static void setInitialCondition(Function initialCondition) {
		TranslationsAsDynamicSystemTest.initialCondition = initialCondition;
	}

	/**
	 * @return the realLine
	 */
	public static Field getRealLine() {
		return realLine;
	}

	/**
	 * @param realLine the realLine to set
	 */
	public static void setRealLine(Field realLine) {
		TranslationsAsDynamicSystemTest.realLine = realLine;
	}

	/**
	 * @return the springConfiguration
	 */
	public static SpringConfiguration getSpringConfiguration() {
		return springConfiguration;
	}

	/**
	 * @param springConfiguration the springConfiguration to set
	 */
	public static void setSpringConfiguration(SpringConfiguration springConfiguration) {
		TranslationsAsDynamicSystemTest.springConfiguration = springConfiguration;
	}

}
