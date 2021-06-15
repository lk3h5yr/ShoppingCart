package com.example.demo.exceptions;

import org.springframework.lang.Nullable;

/**
 * BPM Exception status enum
 *
 * @author tp
 */
public enum ExceptionStatus {

    SYSTEM_ERROR("SYS", "系統錯誤", "SystemError"),

    INPUT_PARAMETER_ERROR("FORMAT", "輸入參數 {0} 錯誤。", "InputParameterError"),

    TRNS_PROCESS_ENDED_ERROR("TRNS", "No running process found", "ProcessEnded"),
    
    NOT_SUPPORT_TASK_TYPE_ERROR("TYPE", "Not support task type(Supported type:usertask, timer)", "TaskTypeError"),
    
    APPROVED_ERROR("APPROVED", "此案件已被審核", "ApprovedError"),
    
    PERMISSION_DENIED("PERMISSION", "沒有審核權限", "PermissionDenied");

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
