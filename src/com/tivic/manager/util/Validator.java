package com.tivic.manager.util;

import java.util.ArrayList;
import java.util.HashMap;

public class Validator {
	
	private int idValidator;
	private HashMap<Integer, ValidatorItem> registerReturn;
	private String txtNotice;
	
	public Validator(int idValidator,
					int tpValidator){
		setIdValidator(idValidator);
		
		//Registra o tpValidator e o txtReturn no grupo padrão da validação
		ValidatorItem validatorItem = new ValidatorItem(tpValidator, "");
		registerReturn = new HashMap<Integer, ValidatorItem>();
		registerReturn.put(ValidatorResult.STANDARD, validatorItem);
	}
	
	public Validator(int idValidator,
					int tpValidator,
					String txtReturn){
		setIdValidator(idValidator);
		
		//Registra o tpValidator e o txtReturn no grupo padrão da validação
		ValidatorItem validatorItem = new ValidatorItem(tpValidator, txtReturn);
		registerReturn = new HashMap<Integer, ValidatorItem>();
		registerReturn.put(ValidatorResult.STANDARD, validatorItem);
	}
	
	public Validator(int idValidator){
		setIdValidator(idValidator);
		
		//Registra o tpValidator e o txtReturn no grupo padrão da validação
		ValidatorItem validatorItem = new ValidatorItem(ValidatorResult.VALID, "Passou na validação");
		registerReturn = new HashMap<Integer, ValidatorItem>();
		registerReturn.put(ValidatorResult.STANDARD, validatorItem);
	}
	
	public Validator(int idValidator,
					int tpValidator,
					String txtReturn,
					int idGroup){
		setIdValidator(idValidator);
		
		//Registra o tpValidator e o txtReturn no grupo padrão da validação
		ValidatorItem validatorItem = new ValidatorItem(tpValidator, txtReturn);
		registerReturn = new HashMap<Integer, ValidatorItem>();
		registerReturn.put(idGroup, validatorItem);
	}
	
	public void setIdValidator(int idValidator) {
		this.idValidator = idValidator;
	}
	public int getIdValidator() {
		return idValidator;
	}
	
	//Metodo que adicionará uma validação em um determinado grupo. Caso já exista tal registro no validator, para aquele grupo, será sobreescrito
	public void add(int tpValidator, String txtReturn){
		add(tpValidator, txtReturn, ValidatorResult.STANDARD);
	}
	public void add(int tpValidator, String txtReturn, int idGroup){
		ValidatorItem validatorItem = new ValidatorItem(tpValidator, txtReturn);
		registerReturn.put(idGroup, validatorItem);
	}
	
	public ValidatorItem getByGroup(int idGroup){
		return registerReturn.get(idGroup);
	}
	
	public int getTpByGroup(int idGroup){
		return registerReturn.get(idGroup).getTpValidator();
	}
	
	public String getTxtNotice() {
		return txtNotice;
	}
	
	public void setTxtNotice(String txtNotice) {
		this.txtNotice = txtNotice;
	}
	
	public HashMap<Integer, ValidatorItem> getRegisterReturn() {
		return registerReturn;
	}
	
	@Override
	public String toString() {
		return  "idValidator = " + getIdValidator() + 
				", registerReturn = " + getRegisterReturn() + 
				", txtNotice = " + getTxtNotice();
	}
	
}
