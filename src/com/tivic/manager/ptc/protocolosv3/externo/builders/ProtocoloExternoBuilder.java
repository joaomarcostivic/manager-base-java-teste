package com.tivic.manager.ptc.protocolosv3.externo.builders;

import java.util.GregorianCalendar;

import com.tivic.manager.ptc.protocolosv3.externo.ProtocoloExterno;
import com.tivic.manager.ptc.protocolosv3.externo.ProtocoloExternoDTO;

public class ProtocoloExternoBuilder {

	private ProtocoloExterno protocoloExterno;
	
	public ProtocoloExternoBuilder() {
		this.protocoloExterno = new ProtocoloExterno();
	}
	
	public ProtocoloExternoBuilder(ProtocoloExternoDTO protocoloExternoDTO) {
		this.protocoloExterno = new ProtocoloExterno();
		addCdDocumento(protocoloExternoDTO.getCdDocumento());
		addCdDocumentoExterno(protocoloExternoDTO.getCdDocumentoExterno());
		addIdAit(protocoloExternoDTO.getIdAit());
		addNrPlaca(protocoloExternoDTO.getNrPlaca());
		addCdInfracao(protocoloExternoDTO.getCdInfracao());
		addNrRenainf(protocoloExternoDTO.getNrRenainf());
		addDtEntrada(protocoloExternoDTO.getDtEntrada());
		addCdOrgaoExterno(protocoloExternoDTO.getCdOrgaoExterno());
		addNmCondutor(protocoloExternoDTO.getNmCondutor());
	}
	
	public ProtocoloExternoBuilder addCdDocumento(int cdDocumento) {
		this.protocoloExterno.setCdDocumento(cdDocumento);
		return this;
	}
	
	public ProtocoloExternoBuilder addCdDocumentoExterno(int cdDocumentoExterno) {
		this.protocoloExterno.setCdDocumentoExterno(cdDocumentoExterno);
		return this;
	}
	
	public ProtocoloExternoBuilder addIdAit(String idAit) {
		this.protocoloExterno.setIdAit(idAit);
		return this;
	}
	
	public ProtocoloExternoBuilder addNrPlaca(String nrPlaca) {
		this.protocoloExterno.setNrPlaca(nrPlaca);
		return this;
	}
	
	public ProtocoloExternoBuilder addCdInfracao(int cdInfracao) {
		this.protocoloExterno.setCdInfracao(cdInfracao);
		return this;
	}
	
	public ProtocoloExternoBuilder addNrRenainf(String nrRenainf) {
		this.protocoloExterno.setNrRenainf(nrRenainf);
		return this;
	}
	
	public ProtocoloExternoBuilder addDtEntrada(GregorianCalendar dtEntrada) {
		this.protocoloExterno.setDtEntrada(dtEntrada);
		return this;
	}
	
	public ProtocoloExternoBuilder addCdOrgaoExterno(int cdOrgaoExterno) {
		this.protocoloExterno.setCdOrgaoExterno(cdOrgaoExterno);
		return this;
	}
	
	public ProtocoloExternoBuilder addNmCondutor(String nmCondutor) {
		this.protocoloExterno.setNmCondutor(nmCondutor);
		return this;
	}
	
	public ProtocoloExterno build() {
        return this.protocoloExterno;
    }
}
