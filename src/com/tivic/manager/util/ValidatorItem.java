package com.tivic.manager.util;

public class ValidatorItem {
	
	private int tpValidator;
	private String txtReturn;
	
	public ValidatorItem(int tpValidator,
						 String txtReturn) {
		setTpValidator(tpValidator);
		setTxtReturn(txtReturn);
	}
	
	public void setTpValidator(int tpValidator) {
		this.tpValidator = tpValidator;
	}
	public int getTpValidator() {
		return tpValidator;
	}
	public void setTxtReturn(String txtReturn) {
		this.txtReturn = txtReturn;
	}
	public String getTxtReturn() {
		return txtReturn;
	}
	
	@Override
	public String toString() {
		return "tpValidator = " + getTpValidator() + 
			   ", txtReturn = " + getTxtReturn();
	}
}
