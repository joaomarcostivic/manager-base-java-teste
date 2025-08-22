package com.tivic.manager.mob.lotes.validator;

import java.sql.Types;
import java.util.List;

import com.tivic.manager.mob.CorreiosEtiqueta;
import com.tivic.manager.mob.CorreiosLote;
import com.tivic.manager.mob.StCorreiosLote;
import com.tivic.manager.mob.correios.CorreiosEtiquetaService;
import com.tivic.manager.mob.correios.ICorreiosLoteService;
import com.tivic.manager.mob.lotes.enums.correios.TipoRemessaCorreiosEnum;
import com.tivic.manager.util.Util;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class CorreiosEtiquetaValidator  implements IValidatorNovoLoteNotificacao {
	ICorreiosLoteService correiosLote;
	CorreiosEtiquetaService correiosEtiquetaService;
	int tpRemessaCorreios;
	
	public CorreiosEtiquetaValidator(int tpRemessaCorreios) throws Exception {
		correiosLote = (ICorreiosLoteService) BeansFactory.get(ICorreiosLoteService.class);
		correiosEtiquetaService = new CorreiosEtiquetaService();		
		this.tpRemessaCorreios = tpRemessaCorreios;
	}

	@Override
	public void validate(int qtdNotificacao, CustomConnection customConnection) throws Exception {
		if(this.tpRemessaCorreios == TipoRemessaCorreiosEnum.CARTA_SIMPLES.getKey()) 
			return;
		List<CorreiosLote> listCorreiosLote = correiosLote.find(criteriosLoteCorreios());
		if(listCorreiosLote.isEmpty()) {
			throw new Exception("Nenhum Lote de etiquetas disponível.");
		}
		int quantidadeEtiquetasLivres = buscarQuantidadeEtiquetasLivres(listCorreiosLote);
		if(quantidadeEtiquetasLivres == 0) {
			throw new Exception("Nenhuma etiqueta disponível.");
		}
		if(quantidadeEtiquetasLivres < qtdNotificacao) {
			throw new Exception("Quantidade de etiquetas insuficiente, existem " + quantidadeEtiquetasLivres + " etiquetas disponíveis.");
		}
	}
	
	private int buscarQuantidadeEtiquetasLivres(List<CorreiosLote> ListCorreiosLote) throws Exception {
		Integer total = 0;
		for (CorreiosLote correiosLote : ListCorreiosLote) {
			List<CorreiosEtiqueta> listaEtiqueta = correiosEtiquetaService.find(criteriosEtiquetasLivres(correiosLote));
			total+= listaEtiqueta.size();
		}
		return total;
	}
	
	private SearchCriterios criteriosEtiquetasLivres(CorreiosLote correiosLote) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriterios("dt_envio", "", ItemComparator.ISNULL, Types.DATE);
		searchCriterios.addCriteriosEqualInteger("cd_lote", correiosLote.getCdLote(), true);
		searchCriterios.addCriterios("cd_ait", "" , ItemComparator.ISNULL, Types.INTEGER, true);
		searchCriterios.addCriterios("cd_lote_impressao", "" , ItemComparator.ISNULL, Types.INTEGER, true);
		return searchCriterios;
	}
	
	private SearchCriterios criteriosLoteCorreios() {
		SearchCriterios searchCriterios = new SearchCriterios();
		int stLoteDisponivel = StCorreiosLote.DISPONIVEL;
		searchCriterios.addCriteriosGreaterDate("dt_vencimento", Util.formatDate(Util.getDataAtual(), "yyyy-MM-dd"), true);
		searchCriterios.addCriteriosEqualInteger("st_lote", stLoteDisponivel, true);
		searchCriterios.addCriteriosEqualInteger("tp_lote", tpRemessaCorreios, true);
		searchCriterios.setOrderBy("cd_lote ASC");
		return searchCriterios;
	}
}