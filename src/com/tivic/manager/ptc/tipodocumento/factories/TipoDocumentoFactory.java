package com.tivic.manager.ptc.tipodocumento.factories;

import java.util.List;

import com.tivic.manager.ptc.tipodocumento.construcaotipodocumento.ConstrucaoTipoDocumentoBuilder;
import com.tivic.manager.ptc.tipodocumento.tipostatusdisponiveis.TipoDocumentoStatus;

public class TipoDocumentoFactory {
	
	public int stategy(int tpStatus) throws Exception {
		List<TipoDocumentoStatus> tipoDocumentoStatus = new ConstrucaoTipoDocumentoBuilder()
				.construirTipoDocumentos()
				.build();
		
		return getTipoDocumento(tpStatus, tipoDocumentoStatus);
	}
	
	private int getTipoDocumento(int tpStatus, List<TipoDocumentoStatus> tipoDocumentoStatus) throws Exception {
		for(TipoDocumentoStatus tpDocumento : tipoDocumentoStatus) {
			if(tpDocumento.getTipoDocumento(tpStatus) != null) {
				return tpDocumento.getTipoDocumento(tpStatus);
			}
		}
		
		throw new Exception("Serviço não Implementado");
	}
	
}
