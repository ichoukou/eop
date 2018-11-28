package net.ytoec.kernel.exception;

import org.springframework.dao.DataAccessException;

public class ServiceException extends DataAccessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ServiceException(String param) {

		super(param);
	}
}
