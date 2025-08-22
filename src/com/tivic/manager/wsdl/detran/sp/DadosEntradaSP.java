package com.tivic.manager.wsdl.detran.sp;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.tivic.manager.wsdl.DadosItem;
import com.tivic.manager.wsdl.interfaces.DadosEntrada;
import com.tivic.manager.wsdl.interfaces.ValidatorService;

public class DadosEntradaSP implements DadosEntrada {
	
	protected LinkedHashMap<String, DadosItem> itens;
	private String arquivo;
	private List<ValidatorService> validators;
	
	public DadosEntradaSP() {
		itens = new LinkedHashMap<String, DadosItem>();
		validators = new ArrayList<ValidatorService>();
	}
	
	public String getArquivo() {
		return arquivo;
	}
	
	public void setArquivo(String arquivo) {
		this.arquivo = arquivo;
	}
	
	public LinkedHashMap<String, DadosItem> getItens() {
		return itens;
	}
	
	public List<ValidatorService> getValidators() {
		return validators;
	}
	
	public void setValidators(List<ValidatorService> validators) {
		this.validators = validators;
	}
	
	public void addValidator(ValidatorService validator) {
		this.validators.add(validator);
	}

	@Override
	public String toString() {
		String valueToString = "{";
		for(String key : this.itens.keySet()){
			try{
				valueToString += "\"" + key + "\":" + Integer.parseInt(this.itens.get(key).getValor()) + ", ";
			} catch(NumberFormatException nfe){
				valueToString += "\"" + key + "\": \"" + this.itens.get(key).getValor() + "\", ";
			}
		}
		valueToString = valueToString.substring(0, valueToString.length()-2) + "}";
		return valueToString;
	}
	
}
