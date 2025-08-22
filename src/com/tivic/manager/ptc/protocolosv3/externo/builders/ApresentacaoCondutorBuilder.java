package com.tivic.manager.ptc.protocolosv3.externo.builders;

import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.ptc.protocolosv3.externo.ProtocoloExternoDTO;

public class ApresentacaoCondutorBuilder {

	private ApresentacaoCondutor apresentacaoCondutor;
	
	public ApresentacaoCondutorBuilder() {
		this.apresentacaoCondutor = new ApresentacaoCondutor();
	}
	
	public ApresentacaoCondutorBuilder(ProtocoloExternoDTO protocoloExternoDTO) {
		this.apresentacaoCondutor = new ApresentacaoCondutor();
		addCdApresentacaoCondutor(protocoloExternoDTO.getCdApresentacaoCondutor());
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
	}
	
	public ApresentacaoCondutorBuilder addCdApresentacaoCondutor(int cdApresentacaoCondutor) {
        this.apresentacaoCondutor.setCdApresentacaoCondutor(cdApresentacaoCondutor);
        return this;
    }
	
    public ApresentacaoCondutorBuilder addCdDocumento(int cdDocumento) {
        this.apresentacaoCondutor.setCdDocumento(cdDocumento);
        return this;
    }

    public ApresentacaoCondutorBuilder addNmCondutor(String nmCondutor) {
        this.apresentacaoCondutor.setNmCondutor(nmCondutor);
        return this;
    }

    public ApresentacaoCondutorBuilder addDsEndereco(String dsEndereco) {
        this.apresentacaoCondutor.setDsEndereco(dsEndereco);
        return this;
    }

    public ApresentacaoCondutorBuilder addDsComplementoEndereco(String dsComplementoEndereco) {
        this.apresentacaoCondutor.setDsComplementoEndereco(dsComplementoEndereco);
        return this;
    }

    public ApresentacaoCondutorBuilder addNmCidade(String nmCidade) {
        this.apresentacaoCondutor.setNmCidade(nmCidade);
        return this;
    }

    public ApresentacaoCondutorBuilder addNrTelefone1(String nrTelefone1) {
        this.apresentacaoCondutor.setNrTelefone1(nrTelefone1);
        return this;
    }

    public ApresentacaoCondutorBuilder addNrCpfCnpj(String nrCpfCnpj) {
        this.apresentacaoCondutor.setNrCpfCnpj(nrCpfCnpj);
        return this;
    }
    
    public ApresentacaoCondutorBuilder addNrCnh(String nrCnh) {
        this.apresentacaoCondutor.setNrCnh(nrCnh);
        return this;
    }
    
    public ApresentacaoCondutorBuilder addUfCnh(String ufCnh) {
        this.apresentacaoCondutor.setUfCnh(ufCnh);
        return this;
    }

    public ApresentacaoCondutorBuilder addNrRg(String nrRg) {
        this.apresentacaoCondutor.setNrRg(nrRg);
        return this;
    }
    
    public ApresentacaoCondutorBuilder addTpCategoriaCnh(int tpCategoriaCnh) {
        this.apresentacaoCondutor.setTpCategoriaCnh(tpCategoriaCnh);
        return this;
    }

    public ApresentacaoCondutorBuilder addTpModeloCnh(int tpModeloCnh) {
        this.apresentacaoCondutor.setTpModeloCnh(tpModeloCnh);
        return this;
    }

    public ApresentacaoCondutorBuilder addIdPaisCnh(String idPaisCnh) {
        this.apresentacaoCondutor.setIdPaisCnh(idPaisCnh);
        return this;
    }

    public ApresentacaoCondutorBuilder addNrEndereco(String nrEndereco) {
        this.apresentacaoCondutor.setNrEndereco(nrEndereco);
        return this;
    }
    
    public ApresentacaoCondutor build() {
        return this.apresentacaoCondutor;
    }
}