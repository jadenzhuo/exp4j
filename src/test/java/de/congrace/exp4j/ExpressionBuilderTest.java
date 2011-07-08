package de.congrace.exp4j;

import static org.junit.Assert.assertTrue;

import java.text.DecimalFormat;
import java.util.Arrays;

import org.junit.Test;

public class ExpressionBuilderTest {
	@Test
	public void testCustomFunction1() throws Exception {
		CustomFunction custom = new CustomFunction("timespi") {
			@Override
			public double applyFunction(double value) {
				return value * Math.PI;
			}
		};
		Calculable calc = new ExpressionBuilder("timespi(x)").withVariable("x", 1).withCustomFunction(custom).build();
		double result = calc.calculate();
		assertTrue(result == Math.PI);
	}

	@Test
	public void testCustomFunction2() throws Exception {
		CustomFunction custom = new CustomFunction("loglog") {
			@Override
			public double applyFunction(double value) {
				return Math.log(Math.log(value));
			}
		};
		Calculable calc = new ExpressionBuilder("loglog(x)").withVariable("x", 1).withCustomFunction(custom).build();
		double result = calc.calculate();
		assertTrue(result == Math.log(Math.log(1)));
	}

	@Test
	public void testCustomFunction3() throws Exception {
		CustomFunction custom1 = new CustomFunction("foo") {
			@Override
			public double applyFunction(double value) {
				return value * Math.E;
			}
		};
		CustomFunction custom2 = new CustomFunction("bar") {
			@Override
			public double applyFunction(double value) {
				return value * Math.PI;
			}
		};
		Calculable calc = new ExpressionBuilder("foo(bar(x))").withVariable("x", 1).withCustomFunction(custom1).withCustomFunction(custom2).build();
		double result = calc.calculate();
		assertTrue(result == 1 * Math.E * Math.PI);
	}

	@Test
	public void testCustomFunction4() throws Exception {
		CustomFunction custom1 = new CustomFunction("foo") {
			@Override
			public double applyFunction(double value) {
				return value * Math.E;
			}
		};
		double varX = 32.24979131d;
		Calculable calc = new ExpressionBuilder("foo(log(x))").withVariable("x", varX).withCustomFunction(custom1).build();
		double result = calc.calculate();
		assertTrue(result == Math.log(varX) * Math.E);
	}

	@Test
	public void testCustomFunction5() throws Exception {
		CustomFunction custom1 = new CustomFunction("foo") {
			@Override
			public double applyFunction(double value) {
				return value * Math.E;
			}
		};
		CustomFunction custom2 = new CustomFunction("bar") {
			@Override
			public double applyFunction(double value) {
				return value * Math.PI;
			}
		};
		double varX = 32.24979131d;
		Calculable calc = new ExpressionBuilder("bar(foo(log(x)))").withVariable("x", varX).withCustomFunction(custom1).withCustomFunction(custom2).build();
		double result = calc.calculate();
		assertTrue(result == Math.log(varX) * Math.E * Math.PI);
	}

	@Test
	public void testCustomFunction6() throws Exception {
		CustomFunction custom1 = new CustomFunction("foo") {
			@Override
			public double applyFunction(double value) {
				return value * Math.E;
			}
		};
		CustomFunction custom2 = new CustomFunction("bar") {
			@Override
			public double applyFunction(double value) {
				return value * Math.PI;
			}
		};
		double varX = 32.24979131d;
		Calculable calc = new ExpressionBuilder("bar(foo(log(x)))").withVariable("x", varX).withCustomFunctions(Arrays.asList(custom1, custom2)).build();
		double result = calc.calculate();
		assertTrue(result == Math.log(varX) * Math.E * Math.PI);
	}

	@Test
	public void testExpressionBuilder1() throws Exception {
		Calculable calc = new ExpressionBuilder("f(x,y)=7*x + 3*y").withVariable("x", 1).withVariable("y", 2).build();
		double result = calc.calculate();
		assertTrue(result == 13d);
	}

	@Test
	public void testExpressionBuilder2() throws Exception {
		Calculable calc = new ExpressionBuilder("7*x + 3*y").withVariable("x", 1).withVariable("y", 2).build();
		double result = calc.calculate();
		assertTrue(result == 13d);
	}

	@Test
	public void testExpressionBuilder3() throws Exception {
		double varX = 1.3d;
		double varY = 4.22d;
		Calculable calc = new ExpressionBuilder("7*x + 3*y - log(y/x*12)^y").withVariable("x", varX).withVariable("y", varY).build();
		double result = calc.calculate();
		assertTrue(result == 7 * varX + 3 * varY - Math.pow(Math.log(varY / varX * 12), varY));
	}

	@Test
	public void testExpressionBuilder4() throws Exception {
		double varX = 1.3d;
		double varY = 4.22d;
		Calculable calc = new ExpressionBuilder("7*x + 3*y - log(y/x*12)^y").withVariable("x", varX).withVariable("y", varY).build();
		double result = calc.calculate();
		assertTrue(result == 7 * varX + 3 * varY - Math.pow(Math.log(varY / varX * 12), varY));
		varX = 1.79854d;
		varY = 9281.123d;
		calc.setVariable("x", varX);
		calc.setVariable("y", varY);
		result = calc.calculate();
		assertTrue(result == 7 * varX + 3 * varY - Math.pow(Math.log(varY / varX * 12), varY));
	}

	@Test
	public void testExpressionBuilder5() throws Exception {
		double varX = 1.3d;
		double varY = 4.22d;
		Calculable calc = new ExpressionBuilder("3*y").withVariable("x", varX).withVariable("y", varY).build();
		double result = calc.calculate();
		assertTrue(result == 3 * varY);
	}

	@Test
	public void testExpressionBuilder6() throws Exception {
		double varX = 1.3d;
		double varY = 4.22d;
		double varZ = 4.22d;
		Calculable calc = new ExpressionBuilder("x * y * z").withVariableNames("x", "y", "z").build();
		calc.setVariable("x", varX);
		calc.setVariable("y", varY);
		calc.setVariable("z", varZ);
		double result = calc.calculate();
		assertTrue(result == varX * varY * varZ);
	}

	@Test
	public void testExpressionBuilder7() throws Exception {
		double varX = 1.3d;
		Calculable calc = new ExpressionBuilder("log(sin(x))").withVariable("x", varX).build();
		double result = calc.calculate();
		assertTrue(result == Math.log(Math.sin(varX)));
	}

	@Test
	public void testExpressionBuilder8() throws Exception {
		double varX = 1.3d;
		Calculable calc = new ExpressionBuilder("f(x)=log(sin(x))").withVariable("x", varX).build();
		double result = calc.calculate();
		assertTrue(result == Math.log(Math.sin(varX)));
	}

	@Test(expected=UnparsableExpressionException.class)
	public void testSameName() throws Exception {
		CustomFunction custom = new CustomFunction("bar") {
			@Override
			public double applyFunction(double value) {
				return value / 2;
			}
		};
		double varBar = 1.3d;
		Calculable calc = new ExpressionBuilder("f(bar)=bar(bar)").withVariable("bar", varBar).withCustomFunction(custom).build();
		double result = calc.calculate();
		assertTrue(result == varBar/2);
	}

	@Test(expected = UnparsableExpressionException.class)
	public void testInvalidFunction() throws Exception {
		double varY = 4.22d;
		Calculable calc = new ExpressionBuilder("3*invalid_function(y)").withVariable("y", varY).build();
		calc.calculate();
	}

	@Test(expected = UnparsableExpressionException.class)
	public void testMissingVar() throws Exception {
		double varY = 4.22d;
		Calculable calc = new ExpressionBuilder("3*y*z").withVariable("y", varY).build();
		calc.calculate();
	}
	@Test
	public void testBench1() throws Exception {
		double factor;
		String expr = "foo(x,y)=log(x) - y * (cbrt(x^cos(y)))";
		int xMax = 100, yMax = 1000;
		Calculable calc=new ExpressionBuilder(expr).build();
		long time = System.currentTimeMillis();
		for (int x = 0; x < xMax; x++) {
			for (int y = 0; y < yMax; y++) {
				calc.setVariable("x", x);
				calc.setVariable("y", y);
				calc.calculate();
			}
		}
		time = System.currentTimeMillis() - time;
		factor = (double) time;
		System.out.println("\n:: [ExpressionBuilder] simple benchmark");
		System.out.println("expression\t\t" + expr);
		System.out.println("num calculations\t" + xMax*yMax);
		System.out.println("exp4j\t\t\t~" + time + " ms");
		time = System.currentTimeMillis();
		@SuppressWarnings("unused")
		double val;
		for (int x = 0; x < xMax; x++) {
			for (int y = 0; y < yMax; y++) {
				val = Math.log(x) - y * (Math.cbrt(Math.pow(x, Math.cos(y))));
			}
		}
		time = System.currentTimeMillis() - time;
		System.out.println("Java Math\t\t~" + time + " ms");
		System.out.println("factor\t\t\t" + DecimalFormat.getInstance().format(factor / (double) time) + "\n");
	}

}
