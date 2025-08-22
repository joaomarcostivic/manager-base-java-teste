package com.tivic.manager.mob.ait.relatorios.quantitativo;

import java.util.ArrayList;
import java.util.List;

import com.tivic.sol.util.date.DateUtil;
import com.tivic.sol.util.date.conversors.SqlFormat;

public class FiltrosRelatorioBuilder {
	private List<RelatorioQuantitativoSearch> relatorioQuantitativoSearch;
	private List<String> filtros;
	 
	public FiltrosRelatorioBuilder (RelatorioQuantitativoSearch relatorioQuantitativoSearch) throws Exception {
		 this.relatorioQuantitativoSearch = new ArrayList<>();
		 this.relatorioQuantitativoSearch.add(relatorioQuantitativoSearch);
		 this.filtros = new ArrayList<String>();
	}
	
	public FiltrosRelatorioBuilder construirFiltro() {
        for (RelatorioQuantitativoSearch criterio : relatorioQuantitativoSearch) {
            if (criterio.getDtMovimentoInicial() != null && !criterio.getDtMovimentoInicial().isEmpty()) {
                checkDtMovimentoInicial(criterio);
            }
            if (criterio.getDtMovimentoFinal() != null && !criterio.getDtMovimentoFinal().isEmpty()) {
                checkDtMovimentoFinal(criterio);
            }
        }
		return this;
    }
	
	private void checkDtMovimentoInicial(RelatorioQuantitativoSearch criterio) {
		if(criterio.getDtMovimentoInicial() != null)
			filtros.add("Data de movimento inicial: " + DateUtil.formatDate(DateUtil.convStringToCalendar(criterio.getDtMovimentoInicial(), new SqlFormat()), "dd/MM/yyyy"));	
	}
	
	private void checkDtMovimentoFinal(RelatorioQuantitativoSearch criterio) {
		if(criterio.getDtMovimentoFinal() != null)
			filtros.add("Data de movimento final: " + DateUtil.formatDate(DateUtil.convStringToCalendar(criterio.getDtMovimentoFinal(), new SqlFormat()), "dd/MM/yyyy"));	
	}
	
	public List<String> build(){
		return filtros;
	}
}
