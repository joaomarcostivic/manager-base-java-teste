package com.tivic.manager.ptc.protocolosv3.externo.builders;

import java.util.GregorianCalendar;

import com.tivic.manager.ptc.protocolosv3.externo.IProtocoloExternoService;
import com.tivic.manager.ptc.protocolosv3.externo.ProtocoloExternoDTO;
import com.tivic.sol.cdi.BeansFactory;

public class ProtocoloExternoUpdateBuilder {

	private IProtocoloExternoService protocoloExternoService;
	private ProtocoloExternoDTO protocolo;
	
	public ProtocoloExternoUpdateBuilder() {
		this.protocolo = new ProtocoloExternoDTO();
	}
	
	public ProtocoloExternoUpdateBuilder(ProtocoloExternoDTO protocoloExternoDTO, int cdDocumentoExterno, int cdDocumento) throws Exception {
		this.protocoloExternoService = (IProtocoloExternoService) BeansFactory.get(IProtocoloExternoService.class);
		this.protocolo = this.protocoloExternoService.get(cdDocumento, cdDocumentoExterno);
		addCdDocumento(protocoloExternoDTO.getCdDocumento());
		addCdDocumentoExterno(protocoloExternoDTO.getCdDocumentoExterno());
		addCdApresentacaoCondutor(protocoloExternoDTO.getCdApresentacaoCondutor());
		addDtProtocolo(protocoloExternoDTO.getDtProtocolo());
		addNmRequerente(protocoloExternoDTO.getNmRequerente());
		addNrDocumento(protocoloExternoDTO.getNrDocumento());
		addIdAit(protocoloExternoDTO.getIdAit());
		addNrPlaca(protocoloExternoDTO.getNrPlaca());
		addCdInfracao(protocoloExternoDTO.getCdInfracao());
		addDtEntrada(protocoloExternoDTO.getDtEntrada());
		addCdDocumento(protocoloExternoDTO.getCdDocumento());
		addNmCondutor(protocoloExternoDTO.getNmCondutor());
		addNrRg(protocoloExternoDTO.getNrRg());
		addNrCnh(protocoloExternoDTO.getNrCnh());
		addNrCpfCnpj(protocoloExternoDTO.getNrCpfCnpj());
		addUfCnh(protocoloExternoDTO.getUfCnh());
		addTpCategoriaCnh(protocoloExternoDTO.getTpCategoriaCnh());
		addTpModeloCnh(protocoloExternoDTO.getTpModeloCnh());
		addIdPaisCnh(protocoloExternoDTO.getIdPaisCnh());
		addNrTelefone1(protocoloExternoDTO.getNrTelefone1());
		addDsEndereco(protocoloExternoDTO.getDsEndereco());
		addDsComplementoEndereco(protocoloExternoDTO.getDsComplementoEndereco());
		addNmCidade(protocoloExternoDTO.getNmCidade());
		addNrEndereco(protocoloExternoDTO.getNrEndereco());
		addTxtObservacao(protocoloExternoDTO.getTxtObservacao());
		addCdOrgaoExterno(protocoloExternoDTO.getCdOrgaoExterno());
		
	}
	
	public ProtocoloExternoUpdateBuilder addCdDocumento(int cdDocumento) {
        this.protocolo.setCdDocumento(cdDocumento);
        return this;
    }
	
	public ProtocoloExternoUpdateBuilder addCdDocumentoExterno(int cdDocumentoExterno) {
        this.protocolo.setCdDocumentoExterno(cdDocumentoExterno);
        return this;
    } 
    
	public ProtocoloExternoUpdateBuilder addCdApresentacaoCondutor(int cdApresentacaoCondutor) {
        this.protocolo.setCdApresentacaoCondutor(cdApresentacaoCondutor);
        return this;
    } 
	
    public ProtocoloExternoUpdateBuilder addCdUsuario(int cdUsuario) {
        this.protocolo.setCdUsuario(cdUsuario);
        return this;
    }

    public ProtocoloExternoUpdateBuilder addDtProtocolo(GregorianCalendar dtProtocolo) {
        this.protocolo.setDtProtocolo(dtProtocolo);
        return this;
    }
    
    public ProtocoloExternoUpdateBuilder addNmRequerente(String nmRequerente) {
        this.protocolo.setNmRequerente(nmRequerente);
        return this;
    }

    public ProtocoloExternoUpdateBuilder addNmCondutor(String nmCondutor) {
        this.protocolo.setNmCondutor(nmCondutor);
        return this;
    }
    
    public ProtocoloExternoUpdateBuilder addNrDocumento(String nrDocumento) {
        this.protocolo.setNrDocumento(nrDocumento);
        return this;
    }
	
	public ProtocoloExternoUpdateBuilder addIdAit(String idAit) {
		this.protocolo.setIdAit(idAit);
		return this;
	}
	
	public ProtocoloExternoUpdateBuilder addNrPlaca(String nrPlaca) {
		this.protocolo.setNrPlaca(nrPlaca);
		return this;
	}
	
	public ProtocoloExternoUpdateBuilder addCdInfracao(int cdInfracao) {
		this.protocolo.setCdInfracao(cdInfracao);
		return this;
	}

	public ProtocoloExternoUpdateBuilder addNrEndereco(String nrEndereco) {
		this.protocolo.setNrEndereco(nrEndereco);
		return this;
	}

	public ProtocoloExternoUpdateBuilder addDtEntrada(GregorianCalendar dtEntrada) {
		this.protocolo.setDtEntrada(dtEntrada);
		return this;
	}
	
	public ProtocoloExternoUpdateBuilder addTxtObservacao(String txtObservacao) {
		this.protocolo.setTxtObservacao(txtObservacao);
		return this;
	}
	
	public ProtocoloExternoUpdateBuilder addNrRg(String nrRg) {
		this.protocolo.setNrRg(nrRg);
		return this;
	}
	
	public ProtocoloExternoUpdateBuilder addNrCpfCnpj(String nrCpfCnpj) {
		this.protocolo.setNrCpfCnpj(nrCpfCnpj);
		return this;
	}
	
	public ProtocoloExternoUpdateBuilder addNrCnh(String nrCnh) {
		this.protocolo.setNrCnh(nrCnh);
		return this;
	}
	
	public ProtocoloExternoUpdateBuilder addUfCnh(String ufCnh) {
		this.protocolo.setUfCnh(ufCnh);
		return this;
	}
	
	public ProtocoloExternoUpdateBuilder addTpCategoriaCnh(int tpCategoriaCnh) {
		this.protocolo.setTpCategoriaCnh(tpCategoriaCnh);
		return this;
	}
	
	public ProtocoloExternoUpdateBuilder addTpModeloCnh(int tpModeloCnh) {
		this.protocolo.setTpModeloCnh(tpModeloCnh);
		return this;
	}
	
	public ProtocoloExternoUpdateBuilder addIdPaisCnh(String idPaisCnh) {
		this.protocolo.setIdPaisCnh(idPaisCnh);
		return this;
	}
	
	public ProtocoloExternoUpdateBuilder addNrTelefone1(String nrTelefone1) {
		this.protocolo.setNrTelefone1(nrTelefone1);
		return this;
	}
	
	public ProtocoloExternoUpdateBuilder addDsEndereco(String dsEndereco) {
		this.protocolo.setDsEndereco(dsEndereco);
		return this;
	}
	
	public ProtocoloExternoUpdateBuilder addDsComplementoEndereco(String dsComplementoEndereco) {
		this.protocolo.setDsComplementoEndereco(dsComplementoEndereco);
		return this;
	}
	
	public ProtocoloExternoUpdateBuilder addNmCidade(String nmCidade) {
		this.protocolo.setNmCidade(nmCidade);
		return this;
	}
	
	public ProtocoloExternoUpdateBuilder addCdOrgaoExterno(int cdOrgaoExterno) {
		this.protocolo.setCdOrgaoExterno(cdOrgaoExterno);
		return this;
	}
	
    public ProtocoloExternoDTO build() {
    	return this.protocolo;
    }
	
}
