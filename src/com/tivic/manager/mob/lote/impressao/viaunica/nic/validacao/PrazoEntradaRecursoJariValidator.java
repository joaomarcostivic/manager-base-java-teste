package com.tivic.manager.mob.lote.impressao.viaunica.nic.validacao;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class PrazoEntradaRecursoJariValidator implements INICValidador {
    private AitMovimentoRepository aitMovimentoRepository;

    public PrazoEntradaRecursoJariValidator() throws Exception {
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
	}
    
	@Override
	public boolean validate(Ait ait, CustomConnection customConnection) throws Exception, ValidacaoException {
	    if (ait.getDtVencimento() == null) {
	        return false;
	    }
	    GregorianCalendar dtVencimentoMaisCincoDias = (GregorianCalendar) ait.getDtVencimento().clone();
	    dtVencimentoMaisCincoDias.add(Calendar.DATE, 5);
	    if(Util.getDataAtual().after(dtVencimentoMaisCincoDias) && getRecursoJari(ait.getCdAit(), customConnection)) {
	    	return false;
	    }
	    return Util.getDataAtual().after(dtVencimentoMaisCincoDias) && !getRecursoJari(ait.getCdAit(), customConnection);
	}
	
	private boolean getRecursoJari(int cdAit, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit, true);
		searchCriterios.addCriteriosEqualInteger("tp_status", TipoStatusEnum.RECURSO_JARI.getKey(), true);
		List<AitMovimento> aitMovimentoList = this.aitMovimentoRepository.find(searchCriterios, customConnection);
		if(!aitMovimentoList.isEmpty()) {
			return true;
		}
		return false;
	}
	
}
