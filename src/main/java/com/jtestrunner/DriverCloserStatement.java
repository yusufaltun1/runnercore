package com.jtestrunner;

import org.junit.runners.model.Statement;

public class DriverCloserStatement extends Statement {

	private final Statement statement;

	public DriverCloserStatement(Statement aStatement) {
		statement = aStatement;
	}

	@Override
	public void evaluate() throws Throwable {
		System.out.println();
		statement.evaluate();
		System.out.println();
	}

}