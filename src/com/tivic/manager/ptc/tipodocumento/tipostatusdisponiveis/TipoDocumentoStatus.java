package com.tivic.manager.ptc.tipodocumento.tipostatusdisponiveis;

import java.util.ArrayList;
import java.util.List;

public class TipoDocumentoStatus {
	
	private Integer tipoDocumento; 
	private String nomeProtocolo;
	private List<Integer> tpStaus;
	
	public TipoDocumentoStatus(Integer tipoDocumento, String nomeProtocolo, List<Integer> tpStaus) {
		this.tipoDocumento = tipoDocumento;
		this.nomeProtocolo = nomeProtocolo;
		this.tpStaus = new ArrayList<Integer>(tpStaus);
	}
	
	public Integer getTipoDocumento(int tpStatus) {
		if(tpStaus.contains(tpStatus))
			return tipoDocumento;
		
		return null;
	}
	
	public String nomeProtocolo(int tpStatus) {
		if(tpStaus.contains(tpStatus))
			return nomeProtocolo;
		
		return null;
	}
	
}
