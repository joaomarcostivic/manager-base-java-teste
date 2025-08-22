package com.tivic.manager.mob.pessoa.dto;

import java.util.Arrays;
import java.util.List;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class ListAitPortalDTOBuilder {
	
	private List<AitPortalDTO> aits;
	private List<Integer> nipStatusPermitidos = Arrays.asList(
			TipoStatusEnum.NIP_ENVIADA.getKey(),  
			TipoStatusEnum.RECURSO_CETRAN.getKey(),
			TipoStatusEnum.RECURSO_JARI.getKey());
	
	private List<Integer> naiStatusPermitidos = Arrays.asList(
			TipoStatusEnum.NAI_ENVIADO.getKey(),
			TipoStatusEnum.NIP_ENVIADA.getKey(),  
			TipoStatusEnum.RECURSO_CETRAN.getKey(),
			TipoStatusEnum.RECURSO_JARI.getKey());
	
	public ListAitPortalDTOBuilder(List<AitPortalDTO> aits) throws Exception {
		this.aits = aits;		
		validarNaiNip();
	}
	
	private void validarNaiNip() throws Exception {
		boolean lgImpressaoPortalNaiNip = ParametroServices.getValorOfParametroAsInteger("LG_PORTAL_IMPRESSAO_NAI_NIP", 0) != 0;
		boolean lgImpressaoPortalAndamentoAit = ParametroServices.getValorOfParametroAsInteger("LG_PORTAL_ANTIGO_IMPRESSAO_AIT", 0) != 0;
		
		for(AitPortalDTO ait : aits) {
			ait.setPossuiNip(nipStatusPermitidos.contains(ait.getTpStatus()) && lgImpressaoPortalNaiNip);
			ait.setPossuiNai(naiStatusPermitidos.contains(ait.getTpStatus()) && lgImpressaoPortalNaiNip);
			ait.setImprimirAndamentoAit(lgImpressaoPortalAndamentoAit);
			ait.setPossuiResultadoJulgamento(verificarResultadoJari(ait.getCdAit()));
		}
	}
	
	private boolean verificarResultadoJari(int cdAit) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.codigo_ait", cdAit);
		Search<AitPortalDTO> search = new SearchBuilder<AitPortalDTO>("ait A")
		.fields("A.nr_placa, A.dt_infracao, A.dt_movimento, A.nr_ait, A.tp_status, A.codigo_ait as cd_ait, A.dt_prazo_defesa, A.dt_vencimento")
		.addJoinTable(" JOIN ait_movimento B ON (B.codigo_ait = A.codigo_ait) ")
		.additionalCriterias(" EXISTS ("
				+ " 	select C.* from ait_movimento C where ("
				+ "			C.tp_status = " + TipoStatusEnum.JARI_COM_PROVIMENTO.getKey()
				+ " 		OR C.tp_status = " + TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey() 
				+ " 	) AND C.codigo_ait = A.codigo_ait"
				+ " )")
		.searchCriterios(searchCriterios)
		.build();
		List<AitPortalDTO> resultadoJulgamento = search.getList(AitPortalDTO.class);
		return !resultadoJulgamento.isEmpty();
	}
			
	public List<AitPortalDTO> build() {		
		return this.aits;
	}
	
	public List<AitPortalDTO> getAits() {
		return aits;
	}
}
