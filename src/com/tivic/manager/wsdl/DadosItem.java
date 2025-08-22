package com.tivic.manager.wsdl;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ValidatorField;

public class DadosItem {

	private String valor;
	private int tamanho;
	private boolean obrigatorio;
	private ValidatorField validator;
	
	public DadosItem() {
		// TODO Auto-generated constructor stub
	}

	public DadosItem(String valor, int tamanho, boolean obrigatorio, ValidatorField validator) {
		super();
		this.valor = valor;
		this.tamanho = tamanho;
		this.obrigatorio = obrigatorio;
		this.validator = validator;
	}
	
	public DadosItem(String valor, int tamanho, boolean obrigatorio) {
		super();
		this.valor = valor;
		this.tamanho = tamanho;
		this.obrigatorio = obrigatorio;
		this.validator = validator;
	}
	
	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public int getTamanho() {
		return tamanho;
	}

	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}

	public boolean isObrigatorio() {
		return obrigatorio;
	}

	public void setObrigatorio(boolean obrigatorio) {
		this.obrigatorio = obrigatorio;
	}
	
	public ValidatorField getValidator() {
		return validator;
	}
	
	public void setValidator(ValidatorField validator) {
		this.validator = validator;
	}

	public void validate() throws ValidacaoException{
		if(this.validator != null)
			this.validator.validate(this.valor, this.tamanho);
	}
	
	@Override
	public String toString() {
		return "DadosItem [valor=" + valor + ", tamanho=" + tamanho + ", obrigatorio=" + obrigatorio + ", validator=" + validator + "]";
	}
	
	
	
}
