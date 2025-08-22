package com.tivic.manager.mob.lote.impressao.viaunica.nic.validacao;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class PrazoEntradaRecursoCetranValidator implements INICValidador {
    private AitMovimentoRepository aitMovimentoRepository;
	
	public PrazoEntradaRecursoCetranValidator() throws Exception {
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
	}
	
	public boolean validate(Ait ait, CustomConnection customConnection) throws Exception, ValidacaoException {
		return verificarPrazoFinalizadoEntradaCetran(ait.getCdAit(), customConnection);
	}
	
	public boolean verificarPrazoFinalizadoEntradaCetran(int cdAit, CustomConnection customConnection) throws Exception {
	    AitMovimento movimentoPublicacaoResultado = getRecurso(cdAit, TipoStatusEnum.PUBLICACAO_RESULTADO_JARI.getKey(), customConnection);
	    if (movimentoPublicacaoResultado != null && movimentoPublicacaoResultado.getDtPublicacaoDo() != null) {
	        GregorianCalendar dtRecursoMaisTrintaECincoDias = (GregorianCalendar) movimentoPublicacaoResultado.getDtPublicacaoDo().clone();
	        dtRecursoMaisTrintaECincoDias.add(Calendar.DATE, 35);
	        return Util.getDataAtual().after(dtRecursoMaisTrintaECincoDias);
	    } 
	    AitMovimento movimentoEntradaJari = getRecurso(cdAit, TipoStatusEnum.RECURSO_JARI.getKey(), customConnection);
	    if (movimentoEntradaJari == null) {
	        return true;
	    } 
	    AitMovimento movimentoRecursoCetranIndeferido = getRecurso(cdAit, TipoStatusEnum.CETRAN_INDEFERIDO.getKey(), customConnection);
	    if (movimentoRecursoCetranIndeferido != null) {
	        return true;
	    } 
	    return false;
	}

	private AitMovimento getRecurso(int cdAit, int tpStatus, CustomConnection customConnection) throws Exception{
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit, true);
		searchCriterios.addCriteriosEqualInteger("tp_status", tpStatus, true);
		searchCriterios.addCriteriosEqualInteger("lg_enviado_detran", TipoLgEnviadoDetranEnum.REGISTRADO.getKey(), true);
		List<AitMovimento> aitMovimentoList = this.aitMovimentoRepository.find(searchCriterios, customConnection);
		if(!aitMovimentoList.isEmpty()) {
			return aitMovimentoList.get(0);
		}
		return null;
	}
	
}
