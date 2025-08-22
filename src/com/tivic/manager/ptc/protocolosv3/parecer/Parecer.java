package com.tivic.manager.ptc.protocolosv3.parecer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Parecer {

	public int cdParecer;
	public int cdSituacaoDocumento;
	public String nmParecer;
	public String dsParecer;
	public int tpDocumento;

	public Parecer(int cdParecer, int cdSituacaoDocumento, String nmParecer, String dsParecer,  int tpDocumento) {
		this.cdParecer = cdParecer;
		this.nmParecer = nmParecer;
		this.dsParecer = dsParecer;
		this.cdSituacaoDocumento = cdSituacaoDocumento;
		this.tpDocumento = tpDocumento;
	}
	
	public Parecer() {}

	public int getCdParecer() {
		return cdParecer;
	}

	public void setCdParecer(int cdParecer) {
		this.cdParecer = cdParecer;
	}
	
	public int getCdSituacaoDocumento() {
		return cdSituacaoDocumento;
	}

	public void setCdSituacaoDocumento(int cdSituacaoDocumento) {
		this.cdSituacaoDocumento = cdSituacaoDocumento;
	}

	public String getNmParecer() {
		return nmParecer;
	}

	public void setNmParecer(String nmParecer) {
		this.nmParecer = nmParecer;
	}

	public String getDsParecer() {
		return dsParecer;
	}

	public void setDsParecer(String dsParecer) {
		this.dsParecer = dsParecer;
	}
	
	public int getTpDocumento() {
		return tpDocumento;
	}

	public void setTpDocumento(int tpDocumento) {
		this.tpDocumento = tpDocumento;
	}

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "Não foi possível serializar o objeto informado";
		}
	}
}