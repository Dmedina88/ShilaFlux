package com.grayherring.shiala.simplesample.util;

public class MalformedExpression extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public MalformedExpression()
	{
		
	}
	
	public MalformedExpression(String info)
	{
		super(info);
	}
	
	public MalformedExpression(String loc, int pos)
	{
		super("Error at pos " + pos + " in: " + loc);
	}
}
