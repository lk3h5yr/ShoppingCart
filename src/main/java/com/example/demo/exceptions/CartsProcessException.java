package com.example.demo.exceptions;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * Exception
 */
public class CartsProcessException extends RuntimeException implements Serializable {

    private String msg;
    private int code;
    private ExceptionStatus status;
    private int rtnCode;

    public CartsProcessException(String msg, ExceptionStatus status) {
		super(msg);
		this.msg = msg;
		this.status = status;
	}

	public CartsProcessException(String msg, ExceptionStatus status, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.status = status;
	}

	public CartsProcessException(String msg, ExceptionStatus status, int code) {
		super(msg);
		this.msg = msg;
		this.status = status;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public ExceptionStatus getStatus() {
		return status;
	}

	public void setStatus(ExceptionStatus status) {
		this.status = status;
	}
	
    public int getRtnCode() {
		return rtnCode;
	}

	public void setRtnCode(int rtnCode) {
		this.rtnCode = rtnCode;
	}

	public static String getInputParameterError(String errorParameter) {
		return MessageFormat.format(ExceptionStatus.INPUT_PARAMETER_ERROR.msg(), errorParameter);
	}
}
