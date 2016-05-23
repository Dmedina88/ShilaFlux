package com.grayherring.shiala.simplesample.util;

public interface VariableStore
{
	/**
	 * Gets the value of a variable, the variable will include the 
	 * 
	 * @param variable
	 * @return
	 * @throws MalformedExpression
	 */
	public double getValue(String variable) throws MalformedExpression;
}
