package com.tivic.manager.triagem;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.cidade.builders.CidadeBuilder;
import com.tivic.manager.mob.eventoequipamento.EventoEquipamentoRepository;
import com.tivic.manager.triagem.builders.GrupoEventoTriagemBuilder;
import com.tivic.manager.triagem.builders.OrgaoTriagemBuilder;
import com.tivic.manager.triagem.dtos.GrupoEventoParamsDTO;
import com.tivic.manager.triagem.dtos.GrupoEventoTriagemDTO;
import com.tivic.manager.triagem.dtos.OrgaoTriagemDTO;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class TriagemService implements ITriagemService {

	private EventoEquipamentoRepository eventoEquipamentoRepository;
	
	public TriagemService() throws Exception {
		eventoEquipamentoRepository = (EventoEquipamentoRepository) BeansFactory.get(EventoEquipamentoRepository.class);
	}

	@Override
	public List<GrupoEventoTriagemDTO> findGrupos() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<GrupoEventoTriagemDTO> gruposEvento = findGrupos(customConnection);
			customConnection.finishConnection();
			return gruposEvento;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public List<GrupoEventoTriagemDTO> findGrupos(CustomConnection customConnection) throws Exception {
		List<GrupoEventoTriagemDTO> gruposEvento = new ArrayList<GrupoEventoTriagemDTO>();
		List<GrupoEventoParamsDTO> gruposEventoParams = eventoEquipamentoRepository.getGruposDeEventos(customConnection);
		
		for(GrupoEventoParamsDTO grupoEventoParams : gruposEventoParams) {
			OrgaoTriagemDTO orgaoTriagem = new OrgaoTriagemBuilder()
					.cdOrgao(grupoEventoParams.getCdOrgao())
					.nmOrgao(grupoEventoParams.getNmOrgao())
					.idOrgao(grupoEventoParams.getIdOrgao())
					.cidade(buildCidade(grupoEventoParams))
					.build();;
			
			gruposEvento.add(buildGrupoEvento(grupoEventoParams, orgaoTriagem));
		}
		
		return gruposEvento;
	}
	
	private GrupoEventoTriagemDTO buildGrupoEvento(GrupoEventoParamsDTO grupoEventoParams, OrgaoTriagemDTO orgaoTriagem) {
		return new GrupoEventoTriagemBuilder()
				.cdGrupoEvento(grupoEventoParams.getCdGrupoEvento())
				.orgao(orgaoTriagem)
				.dtGrupoEvento(grupoEventoParams.getDtGrupoEvento())
				.stEmProcessamento(false)
				.qtEventos(grupoEventoParams.getQtEventos())
				.build();
	}
	
	private Cidade buildCidade(GrupoEventoParamsDTO grupoEventoParams) {
		return new CidadeBuilder()
				.cdCidade(grupoEventoParams.getCdCidade())
				.nmCidade(grupoEventoParams.getNmCidade())
				.build();
	}

}
