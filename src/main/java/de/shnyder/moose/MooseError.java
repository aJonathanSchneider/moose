package de.shnyder.moose;

/**
 * EditorError
 */
public class MooseError extends Error {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static long ERR_ANY = 0;
	public static long ERR_NOT_FOUND = 1;
	public static long ERR_USERS_FAULT_USERCANFIX = 2;

	private long _errorCode;

	public MooseError(String message) {
		super(message);
		this._errorCode = ERR_ANY;
	}

	public MooseError(String message, long errorCode) {
		super(message);
		this._errorCode = errorCode;
	}

	public long get_errorCode() {
		return _errorCode;
	}

	public void set_errorCode(long _errorCode) {
		this._errorCode = _errorCode;
	}
}
