
package com.tivic.manager.util;

import java.util.ArrayList;
import java.util.HashMap;

import sol.util.Result;

public class ValidatorResult extends Result {

	public static final int VALID   = 1000;
	public static final int ALERT   = -1000;
	public static final int ERROR   = -1001;
	
	//Grupo padrão de validação
	public static final int STANDARD = 999;
	
	private ArrayList<Validator> results = new ArrayList<Validator>();

	public ValidatorResult (int code, String message, Exception e)	{
		super(code, message, e);
	}
	
	public ValidatorResult (int code, String message)	{
		super(code, message);
	}
	
	public ValidatorResult (int code)	{
		super(code);
	}
	
	public ValidatorResult (int code, String message, String name, Object obj)	{
		super(code, message, name, obj);
	}

	public void addResult(Validator validator){
		results.add(validator);
	}
	
	public void addListValidator(HashMap<Integer, Validator> listValidator){
		for(int key : listValidator.keySet()){
			results.add(listValidator.get(key));
		}
	}
	
	public boolean hasPassed(){
		return getCode() == VALID || getCode() == ALERT;
	}
	
	public void verify(int idGroup){
		String message_alert = "";
		int code = VALID;
		for(Validator validator : results){
			ValidatorItem resultGroup = validator.getByGroup(idGroup);
			if(resultGroup != null){
				if(resultGroup.getTpValidator() == ALERT){
					message_alert += "- " + resultGroup.getTxtReturn() + (validator.getTxtNotice() != null && !validator.getTxtNotice().equals("") ? " ("+validator.getTxtNotice()+")" : "") + "\n";
					code = ALERT;
				}
			}	
			resultGroup = validator.getByGroup(ValidatorResult.STANDARD);
			if(resultGroup != null){
				if(resultGroup.getTpValidator() == ALERT){
					message_alert += "- " + resultGroup.getTxtReturn() + (validator.getTxtNotice() != null && !validator.getTxtNotice().equals("") ? " ("+validator.getTxtNotice()+")" : "") + "\n";
					code = ALERT;
				}
			}
		}
		getObjects().put("RESULT_ALERT", message_alert);
		
		ArrayList<Integer> codesErros = new ArrayList<Integer>();
	
		String message_error = "";
		for(Validator validator : results){
			ValidatorItem resultGroup = validator.getByGroup(idGroup);
			if(resultGroup != null){
				if(resultGroup.getTpValidator() == ERROR){
					message_error += "- " + resultGroup.getTxtReturn() + (validator.getTxtNotice() != null && !validator.getTxtNotice().equals("") ? " ("+validator.getTxtNotice()+")" : "") + "\n";
					codesErros.add(validator.getIdValidator());
					code = ERROR;
				}
			}	
			
			resultGroup = validator.getByGroup(ValidatorResult.STANDARD);
			if(resultGroup != null){
				if(resultGroup.getTpValidator() == ERROR){
					message_error += "- " + resultGroup.getTxtReturn() + (validator.getTxtNotice() != null && !validator.getTxtNotice().equals("") ? " ("+validator.getTxtNotice()+")" : "") + "\n";
					codesErros.add(validator.getIdValidator());
					code = ERROR;
				}
			}
		}
		getObjects().put("RESULT_ERROR", message_error);
		getObjects().put("CODES_ERROR", codesErros);
		
		setCode(code);
		
		if(getCode() == ERROR){
			System.out.println("Erros: " + getObjects());
			setMessage("Erros de validação");
		}
		else if(getCode() == ALERT){
			setMessage("Alertas de validação");
		}
	}
	
	public ArrayList<Validator> getResults() {
		return results;
	}
	
	public void setResults(ArrayList<Validator> results) {
		this.results = results;
	}
}
