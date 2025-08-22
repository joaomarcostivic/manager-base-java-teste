package com.tivic.manager.ptc.portal.builders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.EnviadoDetranEnum;
import com.tivic.manager.ptc.portal.dtos.AitMovimentoDTO;
import com.tivic.manager.ptc.portal.dtos.DocumentoDTO;
import com.tivic.manager.ptc.portal.response.AitResponse;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class ListAitResponseBuilder {

	private final List<AitResponse> aits;

	private final List<Integer> nipStatusPermitidos = Collections
			.unmodifiableList(Arrays
					.asList(TipoStatusEnum.NIP_ENVIADA.getKey(), 
							TipoStatusEnum.RECURSO_CETRAN.getKey(),
					TipoStatusEnum.RECURSO_JARI.getKey()));

	private final List<Integer> naiStatusPermitidos = Collections
			.unmodifiableList(Arrays
					.asList(TipoStatusEnum.NAI_ENVIADO.getKey(),
							TipoStatusEnum.NIP_ENVIADA.getKey(),
					TipoStatusEnum.RECURSO_CETRAN.getKey(), 
					TipoStatusEnum.RECURSO_JARI.getKey()));

	public ListAitResponseBuilder(List<AitResponse> aits) throws Exception {
		this.aits = aits;
		validarNaiNip();
	}

	private void validarNaiNip() throws Exception {
		boolean lgImpressaoPortalNaiNip = ParametroServices.getValorOfParametroAsInteger
				("LG_PORTAL_IMPRESSAO_NAI_NIP", 0) != 0;

		if (!lgImpressaoPortalNaiNip) {
			return;
		}

		for (AitResponse ait : aits) {
			ait.setPossuiNip(nipStatusPermitidos.contains(ait.getTpStatus()));
			ait.setPossuiNai(naiStatusPermitidos.contains(ait.getTpStatus()));
			ait.setPossuiMultaPaga(TipoStatusEnum.MULTA_PAGA.getKey() == ait.getTpStatus());
			ait.setMovimentos(getMovimentos(ait.getCdAit()));
			ait.setProtocolos(getProtocolos(ait.getCdAit()));
		}
	}
	
	private List<AitMovimentoDTO> getMovimentos(int cdAit) throws Exception{
		List<AitMovimentoDTO> movimentos = searchMovimentos(cdAit, new CustomConnection());
		if(movimentos.isEmpty())
			return new ArrayList<AitMovimentoDTO>();
		return movimentos;
	}
	
	private List<AitMovimentoDTO> searchMovimentos(int cdAit, CustomConnection customConnection) throws Exception{
		SearchCriterios searchCriterios = new SearchCriterios(); 
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit);
		searchCriterios.addCriteriosEqualInteger("lg_enviado_detran", EnviadoDetranEnum.LG_DETRAN_ENVIADA.getKey());
		Search<AitMovimentoDTO> search = new SearchBuilder<AitMovimentoDTO>("mob_ait_movimento")
				.fields("tp_status, dt_movimento")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.orderBy("dt_movimento DESC")
				.build();
		return search.getList(AitMovimentoDTO.class);
	}
	
	private List<DocumentoDTO> getProtocolos(int cdAit) throws Exception{
		List<DocumentoDTO> protocolos = searchProtocolos(cdAit, new CustomConnection());
		if(protocolos.isEmpty())
			return new ArrayList<DocumentoDTO>();
		return protocolos;
	}
	
	private List<DocumentoDTO> searchProtocolos(int cdAit, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios(); 
		searchCriterios.addCriteriosEqualInteger("C.cd_ait", cdAit);
		Search<DocumentoDTO> search = new SearchBuilder<DocumentoDTO>("ptc_documento A")
				.fields("DISTINCT A.cd_documento, A.cd_tipo_documento, A.nr_documento, A.cd_situacao_documento, A.dt_protocolo, D.nm_tipo_documento, E.nm_situacao_documento")
				.searchCriterios(searchCriterios)
				.addJoinTable("LEFT JOIN mob_ait_movimento_documento B ON (A.cd_documento = B.cd_documento)")
				.addJoinTable("LEFT JOIN mob_ait_movimento C ON (B.cd_ait = C.cd_ait)")
				.addJoinTable("LEFT JOIN gpn_tipo_documento D ON (A.cd_tipo_documento = D.cd_tipo_documento)")
				.addJoinTable("LEFT JOIN ptc_situacao_documento E ON (A.cd_situacao_documento = E.cd_situacao_documento)")
				.customConnection(customConnection)
				.build();
		return search.getList(DocumentoDTO.class);
	}

	public List<AitResponse> build() {
		return aits;
	}

	public List<AitResponse> getAits() {
		return aits;
	}
}