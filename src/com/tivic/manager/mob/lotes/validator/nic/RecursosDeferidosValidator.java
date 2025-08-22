package com.tivic.manager.mob.lotes.validator.nic;

import java.sql.Types;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class RecursosDeferidosValidator implements INICValidador {

	private AitMovimentoRepository aitMovimentoRepository;
	
	public RecursosDeferidosValidator() throws Exception {
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
	}
	
	public boolean validate(Ait ait, CustomConnection customConnection) throws Exception, ValidacaoException {
		return !(verificarRecursosDeferidos(ait.getCdAit(), customConnection));
	}
	
	private boolean verificarRecursosDeferidos(int cdAit, CustomConnection customConnection) throws Exception{
		SearchCriterios searchCriterios = new SearchCriterios();
		String recursosDeferidos = String.valueOf(TipoStatusEnum.DEFESA_DEFERIDA.getKey()) 
				+ ", " + String.valueOf(TipoStatusEnum.ADVERTENCIA_DEFESA_DEFERIDA.getKey())
				+ ", " + String.valueOf(TipoStatusEnum.JARI_COM_PROVIMENTO.getKey())
				+ ", " + String.valueOf(TipoStatusEnum.CETRAN_DEFERIDO.getKey());
		
		searchCriterios.addCriterios("tp_status", recursosDeferidos, ItemComparator.IN, Types.INTEGER);
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit, true);
		List<AitMovimento> aitMovimentoList = this.aitMovimentoRepository.find(searchCriterios, customConnection);
		if(aitMovimentoList.isEmpty()) {
			return false;
		}
		return true;
	}
	
}
