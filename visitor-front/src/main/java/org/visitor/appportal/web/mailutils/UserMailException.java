package org.visitor.appportal.web.mailutils;

public class UserMailException extends Exception
{

	/**
	 * The next exception in the chain.
	 * 
	 * @serial
	 */
	private Exception next;

	/**
	 * 
	 */
	private static final long serialVersionUID = 3796172180382714914L;

	/**
	 * Constructs a UserMailException with no detail message.
	 */
	public UserMailException()
	{
		super();
		initCause(null); // prevent anyone else from setting it
	}

	/**
	 * Constructs a UserMailException with the specified detail message.
	 * 
	 * @param s
	 *            the detail message
	 */
	public UserMailException(String s)
	{
		super(s);
		initCause(null); // prevent anyone else from setting it
	}

	/**
	 * Constructs a UserMailException with the specified Exception and detail
	 * message. The specified exception is chained to this exception.
	 * 
	 * @param s
	 *            the detail message
	 * @param e
	 *            the embedded exception
	 * @see #getNextException
	 * @see #setNextException
	 * @see #getCause
	 */
	public UserMailException(String s, Exception e)
	{
		super(s);
		next = e;
		initCause(null); // prevent anyone else from setting it
	}

	/**
	 * Get the next exception chained to this one. If the next exception is a
	 * UserMailException, the chain may extend further.
	 * 
	 * @return next Exception, null if none.
	 */
	public synchronized Exception getNextException()
	{
		return next;
	}

	/**
	 * Overrides the <code>getCause</code> method of <code>Throwable</code> to
	 * return the next exception in the chain of nested exceptions.
	 * 
	 * @return next Exception, null if none.
	 */
	public synchronized Throwable getCause()
	{
		return next;
	}

	/**
	 * Add an exception to the end of the chain. If the end is
	 * <strong>not</strong> a UserMailException, this exception cannot be added
	 * to the end.
	 * 
	 * @param ex
	 *            the new end of the Exception chain
	 * @return <code>true</code> if this Exception was added, <code>false</code>
	 *         otherwise.
	 */
	public synchronized boolean setNextException(Exception ex)
	{
		Exception theEnd = this;
		while (theEnd instanceof UserMailException && ((UserMailException) theEnd).next != null)
		{
			theEnd = ((UserMailException) theEnd).next;
		}
		// If the end is a UserMailException, we can add this
		// exception to the chain.
		if (theEnd instanceof UserMailException)
		{
			((UserMailException) theEnd).next = ex;
			return true;
		} else
			return false;
	}

	/**
	 * Override toString method to provide information on nested exceptions.
	 */
	public synchronized String toString()
	{
		String s = super.toString();
		Exception n = next;
		if (n == null)
			return s;
		StringBuffer sb = new StringBuffer(s == null ? "" : s);
		while (n != null)
		{
			sb.append(";\n  nested exception is:\n\t");
			if (n instanceof UserMailException)
			{
				UserMailException mex = (UserMailException) n;
				sb.append(mex.superToString());
				n = mex.next;
			} else
			{
				sb.append(n.toString());
				n = null;
			}
		}
		return sb.toString();
	}

	/**
	 * Return the "toString" information for this exception, without any
	 * information on nested exceptions.
	 */
	private final String superToString()
	{
		return super.toString();
	}

}
