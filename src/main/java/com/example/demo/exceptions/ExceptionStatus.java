package com.example.demo.exceptions;

import org.springframework.lang.Nullable;

/**
  * Exception status enum
 */
public enum ExceptionStatus {

    SYSTEM_ERROR("SYS", "系統錯誤", "SystemError"),

    INPUT_PARAMETER_ERROR("FORMAT", "輸入參數 {0} 錯誤。", "InputParameterError");

    private final String code;
    private final String msg;
	private final String description;

	ExceptionStatus(String code, String msg, String description) {
	    this.code = code;
	    this.msg = msg;
	    this.description = description;
	}

	public String code() {
		return this.code;
	}

	public String msg() {
		return this.msg;
	}

	public String description() {
		return this.description;
	}

	@Override
	public String toString() {
		return this.code + " " + name();
	}

	@Nullable
	public static ExceptionStatus resolve(String code) {
		for (ExceptionStatus status : values()) {
			if (status.code.equals(code)) {
				return status;
			}
		}
		return null;
	}
}
