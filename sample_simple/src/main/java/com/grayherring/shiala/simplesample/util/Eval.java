package com.grayherring.shiala.simplesample.util;

import java.util.LinkedList;

/**
 * A simple evaluator for expressions that uses a recursive descent parser to 
 * do it's magic.
 * 
 * expression ::= term ((+|-) term)*
 * term ::= factor ((*|/) factor)*
 * factor ::=  number | (expression)
 * var ::= $(<char>)|$(<char>*)
 * 
 * To run, simply call eval on an expression:
 * 	Eval.eval("2 + 4 * (5 - 3)");
 * 
 * If an error was encountered, a MalformedExpression exception will be thrown
 * 
 * If you wish to include support for variables in your code, provide a 
 * VariableStore to the eval:
 *  Eval.eval("$A + $B * (5 - 3)", <VARIABLE_STORE_INSTANCE>);
 * 
 * The VariableStore will be called with "A" and "B" and is expected to return
 * doubles for the given values. If desired, you can also implement functions
 * in the variable store, so your program could react to more variable input.
 * 
 * Note that in your variables, you may include () and ", if whitespace is
 * within these, it will be fine, otherwise your variable won't work, i.e.
 * 
 * $HELLO WORLD would be incorrect but:
 * $"HELLO WORLD" would be fine, as would:
 * $(HELLO WORLD) or:
 * $HELLO(WORLD, TEST, 123)
 * 
 * 
 * @author Joseph Lewis <joehms22@gmail.com>
 * 
 */
public class Eval
{
	private static final char VARIABLE_CHAR = '$';
	
	/**
	 * Checks to see if a statement evaluates rather than throws an exception.
	 * 
	 * @param statement - The statement to eval
	 * @param vs - Null or a VariableStore instance that will be used to eval.
	 * @return True if the statement evals, false otherwise.
	 */
	public static boolean isErrorFree(String statement, VariableStore vs)
	{
		try
		{
			eval(statement, vs);
			return true;
		}catch(MalformedExpression e)
		{
			return false;
		}
	}
	
	/**
	 * Evaluates the given statement without a VariableStore.
	 * 
	 * @param statement - The statement to eval
	 * @return
	 * @throws MalformedExpression - if the expression has syntax errors.
	 */
	public static double eval(String statement) throws MalformedExpression
	{
		return eval(statement, null);
	}
	
	/**
	 * Evaluates the given statement with a VariableStore instance that will
	 * be passed all of the "variables" or "functions" (strings that follow
	 * a $)
	 * 
	 * @param statement - The statement to eval
	 * @param vs - The VariableStore to use.
	 * @return
	 * @throws MalformedExpression
	 */
	public static double eval(String statement, VariableStore vs) throws MalformedExpression
	{
		// Eval all "variables" first		
		if(vs != null)
			for(String var : fetchVariables(statement))
				statement = statement.replace(VARIABLE_CHAR + var, "" + vs.getValue(var));
		
		return evalExpression(tokenize(statement));
	}
	
	protected static double evalExpression(LinkedList<String> tokens) throws MalformedExpression
	{
		double initial = evalTerm(tokens);
		
		while(tokens.size() >= 1 && (tokens.peek().equals("-") || tokens.peek().equals("+")))
		{
			String tok = tokens.pop();
			
			if(tokens.size() == 0)
				throw new MalformedExpression("Trailing - or +");
			
			if(tok.equals("-"))
			{
				initial -= evalTerm(tokens);
			}else
			{
				initial += evalTerm(tokens);
			}
		}
		
		return initial;
	}
	
	protected static double evalTerm(LinkedList<String> tokens) throws MalformedExpression
	{
		double initial = evalFactor(tokens);
		
		while(tokens.size() > 1 && (tokens.peek().equals("*") || tokens.peek().equals("/")))
		{
			String tok = tokens.pop();
			if(tok.equals("*"))
			{
				initial *= evalFactor(tokens);
			}else
			{
				initial /= evalFactor(tokens);
			}
		}
		
		return initial;
	}
	
	protected static double evalFactor(LinkedList<String> tokens) throws MalformedExpression
	{
		try
		{
			String tok = tokens.peek();
			double value;
			
			if(tok.equals("("))
			{
				tokens.pop();
				value = evalExpression(tokens);
				
				// pop final )
				tok = tokens.pop();
				if(! tok.equals(")"))
					throw new MalformedExpression();
				
				return value;
			}
			
			return evalNumber(tokens);
			
		}catch(Exception ex)
		{
			throw new MalformedExpression();
		}
	}
	
	protected static double evalNumber(LinkedList<String> tokens) throws MalformedExpression
	{
		boolean negative = false;
		try
		{
			if(tokens.get(0).equals("-"))
			{
				negative = true;
				tokens.pop();
			}
			double result = Double.parseDouble(tokens.pop());
			return (negative)? -result:result;
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
			throw new MalformedExpression("Expected number, but couldn't find one.");
		}
	}
	
	protected static LinkedList<String> tokenize(String expression) throws MalformedExpression
	{
		LinkedList<String> terms = new LinkedList<String>();
		
		StringBuilder current = new StringBuilder();
				
		for(int i = 0; i < expression.length(); i++)
		{
			char c = expression.charAt(i);
			switch(c)
			{
				case ' ':
					if(!current.toString().isEmpty())
					{
						terms.add(current.toString());
						current = new StringBuilder();
					}
					break;
				
				case '+':
				case '*':
				case '/':
				case '(':
				case ')':
				case '-':
					if(!current.toString().isEmpty())
					{
						terms.add(current.toString());
						current = new StringBuilder();
					}
					terms.add("" + c);
					break;
					
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6': 
				case '7': 
				case '8':
				case '9':
				case '.':
					current.append(c);
					break;
				default:
					throw new MalformedExpression(expression, i);
			}
		}
		
		if(! current.toString().isEmpty())
			terms.add(current.toString());
		
		return terms;
	}
	
	protected static String[] fetchVariables(String statement)
	{
		LinkedList<String> vars = new LinkedList<String>();
		StringBuilder openVarStack = new StringBuilder();
		
		boolean inVar = false;
		boolean inQuotes = false;
		short parens = 0;
		boolean inEscape = false;
		
		for(char c : statement.toCharArray())
		{
			if(! inVar)
			{
				if(c == VARIABLE_CHAR)
					inVar = true;
				continue;
			}
			
			switch(c)
			{
				case '\\':
					if(inEscape)
						openVarStack.append(c);
					inEscape = !inEscape;
					break;
						
				case '"':
					inQuotes = !inQuotes;
					openVarStack.append(c);
					break;
				
				case '(':
					if(!inQuotes)
						parens++;
					openVarStack.append(c);
					break;
					
				case ')':
					if(!inQuotes)
						parens--;
					openVarStack.append(c);
					if (parens == 0 && !inQuotes)
					{
						vars.add(openVarStack.toString());
						openVarStack = new StringBuilder();
						inVar = false;
						break;
					}
					break;
				
				case ' ':
					if(!inQuotes && parens == 0)
					{
						vars.add(openVarStack.toString());
						openVarStack = new StringBuilder();
						inVar = false;
						break;
					}
				default:
					openVarStack.append(c);
			}
		}
		
		if(inVar)
			vars.add(openVarStack.toString());
		
		return vars.toArray(new String[vars.size()]);
	}
}
