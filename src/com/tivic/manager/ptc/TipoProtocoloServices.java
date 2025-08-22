package com.tivic.manager.ptc;

import java.sql.Connection;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.util.ResultSetMapper;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

public class TipoProtocoloServices {
	
	private ProtocolosDisponiveisValidator validators;
	
	public List<TipoProtocolo> getTiposDisponiveis(Ait ait) throws Exception {
		return getTiposDisponiveis(ait, null);
	}
	
	public List<TipoProtocolo> getTiposDisponiveis(Ait ait, Connection connect) throws Exception {
		boolean isConnectionNull = (connect == null);
		
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			List<TipoProtocolo> protocolosDisponiveis = new ArrayList<TipoProtocolo>();
			List<AitMovimento> movimentos = getMovimentos(ait, connect);
			validators = new ProtocolosDisponiveisValidator(movimentos);
			
			if(validators.nipLancada()) 
				setTiposPosNip(protocolosDisponiveis);
			else if(validators.naiLancada())
				setTiposPosNai(protocolosDisponiveis);			
			
			return protocolosDisponiveis;
		} finally { 
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	private List<AitMovimento> getMovimentos(Ait ait, Connection connect) throws Exception {
		Criterios criterios = new Criterios("A.cd_ait", String.valueOf(ait.getCdAit()), ItemComparator.EQUAL, Types.INTEGER);
		ResultSetMap rsm = AitMovimentoServices.find(criterios);
		
		List<AitMovimento> movimentos = new ResultSetMapper<AitMovimento>(rsm, AitMovimento.class).toList();
		return movimentos;
	}
	
	private void setTiposPosNai(List<TipoProtocolo> protocolosDisponiveis) {
		if(!validators.ficiLancada())
			protocolosDisponiveis.add(new TipoProtocolo("Apresentação de Condutor", AitMovimentoServices.APRESENTACAO_CONDUTOR));
		
		validarLancamentoDefesa(protocolosDisponiveis);
		validarLancamentoDefesaAdvertencia(protocolosDisponiveis);
	}
	
	private void setTiposPosNip(List<TipoProtocolo> protocolosDisponiveis) {
		validarLancamentoApresentacaoCondutor(protocolosDisponiveis);
		validarLancamentoDefesa(protocolosDisponiveis);
		validarLancamentoDefesaAdvertencia(protocolosDisponiveis);
		if(validators.entradaCetranLancada()) {
			if(validators.resultadoCetranLancado())
				return;
			
			protocolosDisponiveis.add(new TipoProtocolo("Resultado Recurso CETRAN", AitMovimentoServices.CETRAN_DEFERIDO));
		} else if (validators.entradaJariLancada()) {
			if(validators.resultadoJariLancado()) {
				protocolosDisponiveis.add(new TipoProtocolo("Entrada Recurso CETRAN", AitMovimentoServices.RECURSO_CETRAN));
				return;
			}
			
			protocolosDisponiveis.add(new TipoProtocolo("Resultado Recurso JARI", AitMovimentoServices.JARI_COM_PROVIMENTO));
		} else {
			protocolosDisponiveis.add(new TipoProtocolo("Entrada Recurso JARI", AitMovimentoServices.RECURSO_JARI));
		}
	}
	
	private void validarLancamentoDefesa(List<TipoProtocolo> protocolosDisponiveis) {
		if(validators.entradaDefesaLancada()) {
			if(validators.resultadoDefesaLancada())
				return;
			
			protocolosDisponiveis.add(new TipoProtocolo("Resultado de Defesa Prévia", AitMovimentoServices.DEFESA_DEFERIDA));
		} else {
			protocolosDisponiveis.add(new TipoProtocolo("Entrada de Defesa Prévia", AitMovimentoServices.DEFESA_PREVIA));
		}
	}
	
	private void validarLancamentoDefesaAdvertencia(List<TipoProtocolo> protocolosDisponiveis) {
		if(validators.entradaAdvertenciaDefesaLancada())
			return;
		
		if(validators.entradaJariLancada())
			return;
		
		protocolosDisponiveis.add(new TipoProtocolo("Advertência por Escrito", AitMovimentoServices.ADVERTENCIA_DEFESA_ENTRADA));	
	}
	
	private void validarLancamentoApresentacaoCondutor(List<TipoProtocolo> protocolosDisponiveis) {
		if(validators.ficiLancada()) 
			return;
		
		protocolosDisponiveis.add(new TipoProtocolo("Apresentação de Condutor", AitMovimentoServices.APRESENTACAO_CONDUTOR));
	}

}
