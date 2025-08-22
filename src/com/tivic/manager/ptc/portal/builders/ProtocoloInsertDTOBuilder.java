package com.tivic.manager.ptc.portal.builders;

import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.ptc.protocolosv3.ProtocoloInsertDTO;

public class ProtocoloInsertDTOBuilder {
	
	private ProtocoloInsertDTO protocoloInsertDTO;
	
	public ProtocoloInsertDTOBuilder() throws Exception {
		this.protocoloInsertDTO = new ProtocoloInsertDTO();
	}
	
	public ProtocoloInsertDTOBuilder setCdAit(int cdAit) {
		this.protocoloInsertDTO.setCdAit(cdAit);	
		return this;
	}
	
	public ProtocoloInsertDTOBuilder setCdTipoDocumento(int cdTipoDocumento) {
		this.protocoloInsertDTO.setCdTipoDocumento(cdTipoDocumento);
		return this;
	}	
	
	public ProtocoloInsertDTOBuilder setCdOcorrencia(int cdOcorrencia) {
		this.protocoloInsertDTO.setCdOcorrencia(cdOcorrencia);
		return this;
	}

	public ProtocoloInsertDTOBuilder setNrDocumento(String nrDocumento) {
		this.protocoloInsertDTO.setNrDocumento(nrDocumento);
		return this;
	}

	public ProtocoloInsertDTOBuilder setNmRequerente(String nmRequerente) {
		this.protocoloInsertDTO.setNmRequerente(nmRequerente);
		return this;
	}

	public ProtocoloInsertDTOBuilder setTxtObservacao(String txtObservacao) {
		this.protocoloInsertDTO.setTxtObservacao(txtObservacao);
		return this;
	}

	public ProtocoloInsertDTOBuilder setDtProtocolo(GregorianCalendar dtProtocolo) {
		this.protocoloInsertDTO.setDtProtocolo(dtProtocolo);
		return this;
	}

	public ProtocoloInsertDTOBuilder setCdUsuario(int cdUsuario) {
		this.protocoloInsertDTO.setCdUsuario(cdUsuario);
		return this;
	}

	public ProtocoloInsertDTOBuilder setArquivos(List<Arquivo> arquivos) {
		this.protocoloInsertDTO.setArquivos(arquivos);
		return this;
	}

	public ProtocoloInsertDTOBuilder setLgGerar(Boolean lgGerar) {
		this.protocoloInsertDTO.setLgGerar(lgGerar);
		return this;
	}

	public ProtocoloInsertDTOBuilder setCdSituacaoDocumento(int cdSituacaoDocumento) {
		this.protocoloInsertDTO.setCdSituacaoDocumento(cdSituacaoDocumento);
		return this;
	}
	
	public ProtocoloInsertDTO build() {
		return this.protocoloInsertDTO;
	}
}
