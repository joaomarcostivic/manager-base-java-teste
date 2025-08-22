package com.tivic.manager.ptc.protocolosv3.apresentacaocondutor;

import com.tivic.manager.ptc.portal.request.DocumentoPortalRequest;
import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;

public class ApresentacaoCondutorDTOBuilder {
	private ApresentacaoCondutorDTO apresentacaoCondutor;
	private ApresentacaoCondutor novaFici;
	
	public ApresentacaoCondutorDTOBuilder(){
		apresentacaoCondutor = new ApresentacaoCondutorDTO();
		novaFici = new ApresentacaoCondutor();
	}
	
	public ApresentacaoCondutorDTOBuilder protocolo(ProtocoloDTO protocolo) {
		apresentacaoCondutor.setAit(protocolo.getAit());
		apresentacaoCondutor.setDocumento(protocolo.getDocumento());
		apresentacaoCondutor.setAitMovimento(protocolo.getAitMovimento());
		apresentacaoCondutor.setFase(protocolo.getFase());
		apresentacaoCondutor.setTipoDocumento(protocolo.getTipoDocumento());
		apresentacaoCondutor.setUsuario(protocolo.getUsuario());
		apresentacaoCondutor.setSituacaoDocumento(protocolo.getSituacaoDocumento());
		apresentacaoCondutor.setArquivos(protocolo.getArquivos());
		novaFici.setCdDocumento(protocolo.getDocumento().getCdDocumento());
		return this;
	}
	
	public ApresentacaoCondutorDTOBuilder apresentacaoCondutor(ApresentacaoCondutorInsertDTO apresentacaoCondutor) {
		
		novaFici.setNrCnh(apresentacaoCondutor.getNrCnh());
		novaFici.setUfCnh(apresentacaoCondutor.getUfCnh());
		novaFici.setTpCategoriaCnh(apresentacaoCondutor.getTpCategoriaCnh());
		novaFici.setNmCondutor(apresentacaoCondutor.getNmCondutor());
		novaFici.setNrTelefone1(apresentacaoCondutor.getNrTelefone1());
		novaFici.setNrTelefone2(apresentacaoCondutor.getNrTelefone2());
		novaFici.setNrCpfCnpj(apresentacaoCondutor.getNrCpfCnpj());
		novaFici.setTpModeloCnh(apresentacaoCondutor.getTpModeloCnh());
		novaFici.setIdPaisCnh(apresentacaoCondutor.getIdPaisCnh());
		this.apresentacaoCondutor.setApresentacaoCondutor(novaFici);
		return this;
	}
	
	public ApresentacaoCondutorDTOBuilder apresentacaoCondutor(DocumentoPortalRequest documentoRecurso) {
		
		novaFici.setNrCnh(documentoRecurso.getNrCnh());
		novaFici.setUfCnh(documentoRecurso.getUfCnh());
		novaFici.setTpCategoriaCnh(documentoRecurso.getTpCategoriaCnh());
		novaFici.setNmCondutor(documentoRecurso.getNmRequerente());
		novaFici.setNrTelefone1(documentoRecurso.getNrTelefoneRequerente());
		novaFici.setNrTelefone2(documentoRecurso.getNrCelularRequerente());
		novaFici.setNrCpfCnpj(documentoRecurso.getNrCpfCnpjRequerente());
		novaFici.setTpModeloCnh(documentoRecurso.getTpModeloCnh());
		novaFici.setIdPaisCnh(documentoRecurso.getIdPaisCnh());
		this.apresentacaoCondutor.setApresentacaoCondutor(novaFici);
		return this;
	}
	
	public ApresentacaoCondutorDTO build() {
		return apresentacaoCondutor;
	}
}
