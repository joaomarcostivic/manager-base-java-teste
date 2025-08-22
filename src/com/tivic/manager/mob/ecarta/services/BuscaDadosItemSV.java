package com.tivic.manager.mob.ecarta.services;

import java.util.List;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.mob.ecarta.dtos.ECartaItemSV;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class BuscaDadosItemSV {
	
	public ECartaItemSV buscar(int cdAit, int cdLoteNotificacao, CustomConnection customConnection) throws ValidacaoException, Exception {
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		if (lgBaseAntiga) {
			return buscarEmBaseAntiga(cdAit, cdLoteNotificacao);
		} else {
			return buscarEmBaseNova(cdAit, cdLoteNotificacao);
		}
	}
	
	private ECartaItemSV buscarEmBaseAntiga(int cdAit, int cdLoteNotificacao) throws ValidacaoException, Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.codigo_ait", cdAit, true);
		List<ECartaItemSV> eCartaItemSVList = new SearchBuilder<ECartaItemSV>("ait A")
				.fields("A.codigo_ait as cd_ait, A.cod_bairro, A.cod_municipio, A.nm_proprietario as nm_destinatario, "
						+ "A.ds_logradouro, A.ds_nr_imovel as nr_endereco, A.nm_complemento as complemento_endereco, "
						+ "A.nr_cep, B.nm_bairro, C.nm_municipio, C.nm_uf as sg_estado")
				.addJoinTable("LEFT OUTER JOIN bairro B ON (A.cod_bairro = B.cod_bairro)")
				.addJoinTable("LEFT OUTER JOIN municipio C ON (A.cod_municipio = C.cod_municipio)")
        		.searchCriterios(searchCriterios)
				.build()
				.getList(ECartaItemSV.class);
		if (eCartaItemSVList.isEmpty())
			return new ECartaItemSV();
		ECartaItemSV eCartaItemSV = eCartaItemSVList.get(0);
		eCartaItemSV.setNrLote(String.valueOf(cdLoteNotificacao));
		return eCartaItemSV;	
	}
	
	private ECartaItemSV buscarEmBaseNova(int cdAit, int cdLoteNotificacao) throws ValidacaoException, Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit, true);
		List<ECartaItemSV> eCartaItemSVList = new SearchBuilder<ECartaItemSV>("mob_ait A")
				.fields("A.cd_ait, A.cd_bairro, A.cd_cidade, A.nm_proprietario as nm_destinatario, "
						+ "A.ds_logradouro, A.ds_nr_imovel as nr_endereco, A.nm_complemento as complemento_endereco, "
						+ "A.nr_cep, B.nm_bairro, C.nm_cidade, D.sg_estado")
				.addJoinTable("LEFT OUTER JOIN grl_bairro B ON (A.cd_bairro = B.cd_bairro)")
				.addJoinTable("LEFT OUTER JOIN grl_cidade C ON (A.cd_cidade = C.cd_cidade)")
				.addJoinTable("LEFT OUTER JOIN grl_estado D ON (C.cd_estado = D.cd_estado)")
        		.searchCriterios(searchCriterios)
				.build()
				.getList(ECartaItemSV.class);
		if (eCartaItemSVList.isEmpty())
			return new ECartaItemSV();
		ECartaItemSV eCartaItemSV = eCartaItemSVList.get(0);
		eCartaItemSV.setNrLote(String.valueOf(cdLoteNotificacao));
		return eCartaItemSV;	
	}
}
