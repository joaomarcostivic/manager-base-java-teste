package com.tivic.manager.ptc.protocolosv3.apresentacaocondutor;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.ptc.fici.ApresentacaoCondutorRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class ApresentacaoCondutorUpdateBuilder {
	private ApresentacaoCondutorDTO protocolo;
	private ApresentacaoCondutorRepository ficiRepository;

	public ApresentacaoCondutorUpdateBuilder(ApresentacaoCondutorDTO protocolo) throws Exception {
		ficiRepository = (ApresentacaoCondutorRepository) BeansFactory.get(ApresentacaoCondutorRepository.class);
		this.protocolo = protocolo;
	}

	public ApresentacaoCondutorUpdateBuilder apresentacaoCondutor(CustomConnection customConnection) throws BadRequestException, Exception {
		ApresentacaoCondutor ficiUpdate =  ficiRepository.get(protocolo.getApresentacaoCondutor().getCdApresentacaoCondutor(), customConnection);
		ficiUpdate.setNrCnh(protocolo.getApresentacaoCondutor().getNrCnh());
		ficiUpdate.setUfCnh(protocolo.getApresentacaoCondutor().getUfCnh());
		ficiUpdate.setTpCategoriaCnh(protocolo.getApresentacaoCondutor().getTpCategoriaCnh());
		ficiUpdate.setNmCondutor(protocolo.getApresentacaoCondutor().getNmCondutor());
		ficiUpdate.setNrTelefone1(protocolo.getApresentacaoCondutor().getNrTelefone1());
		ficiUpdate.setNrTelefone2(protocolo.getApresentacaoCondutor().getNrTelefone2());
		ficiUpdate.setNrCpfCnpj(protocolo.getApresentacaoCondutor().getNrCpfCnpj());
		ficiUpdate.setTpModeloCnh(protocolo.getApresentacaoCondutor().getTpModeloCnh());
		ficiUpdate.setIdPaisCnh(protocolo.getApresentacaoCondutor().getIdPaisCnh());
		ficiUpdate.setNrCpfCnpj(protocolo.getApresentacaoCondutor().getNrCpfCnpj());
		protocolo.setApresentacaoCondutor(ficiUpdate);
		return this;
	}
		
	public ApresentacaoCondutorDTO build() {
		return protocolo;
	}
}
