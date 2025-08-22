package com.tivic.manager.ptc.protocolos.mg.fici;

import java.sql.Types;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.mob.Ocorrencia;
import com.tivic.manager.mob.OcorrenciaServices;
import com.tivic.manager.ptc.FaseServices;
import com.tivic.manager.ptc.protocolos.DadosProtocoloDTO;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.util.ResultSetMapper;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

public class DadosFiciBuilder {
	private DadosProtocoloDTO protocolo;

	public DadosFiciBuilder(DadosProtocoloDTO protocolo) {
		this.protocolo = protocolo;
	}
	
	public DadosFiciBuilder movimento() throws ValidationException, Exception{
		if(isIndeferido(protocolo)) {
			return this;
		}
		protocolo.getMovimento().setCdOcorrencia(getOcorrenciaFici().getCdOcorrencia());
		
		return this;
	}
	
	public DadosFiciBuilder apresentacaoCondutor() throws ValidationException, Exception {
		protocolo.getApresentacaoCondutor().setCdDocumento(protocolo.getCdDocumento());
		return this;
	}
	
	public DadosProtocoloDTO build() {
		return protocolo;
	}
	
	private Ocorrencia getOcorrenciaFici() throws ValidationException, Exception{
		Criterios criterios = new Criterios();
        criterios.add("tp_ocorrencia", "3", ItemComparator.EQUAL, Types.INTEGER);
        criterios.add("id_movimentacao", "22", ItemComparator.EQUAL, Types.INTEGER);
        ResultSetMap rsm = OcorrenciaServices.find(criterios);      
        
        if(rsm.next()) {
        	Ocorrencia ocorrencia = new ResultSetMapper<Ocorrencia>(rsm, Ocorrencia.class).toList().get(0);
        	return ocorrencia;
        }
        
        throw new ValidationException("Não foi encontrada ocorrencia de FICI aceita");
	}
	
	private boolean isIndeferido(DadosProtocoloDTO dadosProtocolo) {
		return dadosProtocolo.getCdFase() == FaseServices.getCdFaseByNome("indeferido");
	}
}
