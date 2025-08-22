package com.tivic.manager.util;

import com.tivic.manager.seg.EstruturaOfSeguranca;

public class MethodCaller extends sol.util.MethodCaller {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	static {
		STRUCTURE_SECURITY = new EstruturaOfSeguranca();
	}

}