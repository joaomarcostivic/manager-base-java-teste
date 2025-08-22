package com.tivic.manager.mob.funset.parts;

import com.tivic.manager.mob.funset.validations.FunsetValidation;
import com.tivic.manager.util.Util;

public class FunsetCampo {
	
	private String tipoRegistro;
	private int tamanho;
	private String valorCampo;
	private FunsetValidation funsetValidation;
	
	public FunsetCampo() {
	
	}

	public String getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public int getTamanho() {
		return tamanho;
	}

	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}

	public String getValorCampo() {
		return valorCampo;
	}

	public void setValorCampo(String valorCampo) {
		this.valorCampo = valorCampo;
	}

	public FunsetValidation getFunsetValidation() {
		return funsetValidation;
	}

	public void setFunsetValidation(FunsetValidation funsetValidation) {
		this.funsetValidation = funsetValidation;
	}

	public String build() {
		if(getValorCampo() != null && !getValorCampo().equals("")) {
			return Util.fill(getValorCampo(), getTamanho(), '0', 'E');
		}
		else {
			return Util.fill("", getTamanho(), '0', 'E');
		}
	}
}
