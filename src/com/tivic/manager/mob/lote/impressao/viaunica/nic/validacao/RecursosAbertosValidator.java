package com.tivic.manager.mob.lote.impressao.viaunica.nic.validacao;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class RecursosAbertosValidator implements INICValidador {
	
	public boolean validate(Ait ait, CustomConnection customConnection) throws Exception, ValidacaoException {
		return !(verificarDefesaPrevia(ait.getCdAit(), customConnection) 
				&& verificarEntradaDefesaPreviaAdvertencia(ait.getCdAit(), customConnection) 
				&& verificarEntradaJari(ait.getCdAit(), customConnection)
				&& verificarEntradaCetran(ait.getCdAit(), customConnection));
	}
	
	private boolean verificarDefesaPrevia(int cdAit, CustomConnection customConnection) throws Exception{
	    boolean defesaPrevia = getStatusMovimento(cdAit, TipoStatusEnum.DEFESA_PREVIA.getKey(), customConnection) != null;
	    boolean defesaDeferida = getStatusMovimento(cdAit, TipoStatusEnum.DEFESA_DEFERIDA.getKey(), customConnection) != null;
	    boolean defesaIndeferida = getStatusMovimento(cdAit, TipoStatusEnum.DEFESA_INDEFERIDA.getKey(), customConnection) != null;
		return defesaPrevia && !(defesaDeferida || defesaIndeferida);
	}
	
	private boolean verificarEntradaDefesaPreviaAdvertencia(int cdAit, CustomConnection customConnection) throws Exception{
		boolean defesaPreviaAdvertencia = getStatusMovimento(cdAit, TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey(), customConnection) != null;
		boolean defesaAdvertenciaDeferida = getStatusMovimento(cdAit, TipoStatusEnum.ADVERTENCIA_DEFESA_DEFERIDA.getKey(), customConnection) != null;
		boolean defesaAdvertenciaIndeferida = getStatusMovimento(cdAit, TipoStatusEnum.ADVERTENCIA_DEFESA_INDEFERIDA.getKey(), customConnection) != null;		
		return defesaPreviaAdvertencia && !(defesaAdvertenciaDeferida || defesaAdvertenciaIndeferida);
	}
	
	private boolean verificarEntradaJari(int cdAit, CustomConnection customConnection) throws Exception{
		boolean jari = getStatusMovimento(cdAit, TipoStatusEnum.RECURSO_JARI.getKey(), customConnection) != null;;
		boolean jariComProvimento = getStatusMovimento(cdAit, TipoStatusEnum.JARI_COM_PROVIMENTO.getKey(), customConnection) != null;;
		boolean jariSemProvimento = getStatusMovimento(cdAit, TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey(), customConnection) != null;;		
		return jari && !(jariComProvimento || jariSemProvimento);
	}
	
	private boolean verificarEntradaCetran(int cdAit, CustomConnection customConnection) throws Exception{
		boolean cetran = getStatusMovimento(cdAit, TipoStatusEnum.RECURSO_CETRAN.getKey(), customConnection) != null;;
		boolean cetranDeferido = getStatusMovimento(cdAit, TipoStatusEnum.CETRAN_DEFERIDO.getKey(), customConnection) != null;;
		boolean cetranIndeferido = getStatusMovimento(cdAit, TipoStatusEnum.CETRAN_INDEFERIDO.getKey(), customConnection) != null;;
		return cetran && !(cetranDeferido || cetranIndeferido);
	}
	
	private AitMovimento getStatusMovimento(int cdAit, int tpStatus, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit);
		searchCriterios.addCriteriosEqualInteger("tp_status", tpStatus);
		Search<AitMovimento> movimentos = new SearchBuilder<AitMovimento>("mob_ait_movimento")
			.orderBy("cd_movimento DESC")
			.searchCriterios(searchCriterios)
			.customConnection(customConnection)
		.build();
		if(!movimentos.getList(AitMovimento.class).isEmpty()) {
			return movimentos.getList(AitMovimento.class).get(0);
		}
		return null;
	}
	
}
