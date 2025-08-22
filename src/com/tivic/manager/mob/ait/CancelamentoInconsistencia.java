package com.tivic.manager.mob.ait;

import java.util.List;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.AitInconsistencia.AitInconsistencia;
import com.tivic.manager.mob.aitmovimento.CodigoCancelamentoMovimentoMap;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.aitmovimento.aitmovimentobuilder.AitMovimentoBuilder;
import com.tivic.manager.mob.inconsistencias.AitInconsistenciaDTO;
import com.tivic.manager.mob.inconsistencias.IAitInconsistenciaService;
import com.tivic.manager.mob.inconsistencias.TipoSituacaoInconsistencia;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class CancelamentoInconsistencia {
	
	private AitInconsistenciaDTO aitInconsistenciaDTO;
	private IAitService aitService;
	private IAitInconsistenciaService aitInconsistenciaService;
	private IAitMovimentoService aitMovimentoService;
	
	public CancelamentoInconsistencia(AitInconsistenciaDTO aitInconsistenciaDTO) throws Exception {
		this.aitInconsistenciaDTO = aitInconsistenciaDTO;
		this.aitService = (IAitService) BeansFactory.get(IAitService.class);
		this.aitInconsistenciaService = (IAitInconsistenciaService) BeansFactory.get(IAitInconsistenciaService.class);
		this.aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
	}

	public void cancelar() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			atualizarAitInconsistencia(this.aitInconsistenciaDTO.getCdAit(), this.aitInconsistenciaDTO.getTpStatusPretendido(), customConnection);		
			this.gerarCancelamento(customConnection);
			customConnection.finishConnection();
		}
		finally {
			customConnection.closeConnection();
		}
	}
	
	private void gerarCancelamento(CustomConnection customConnection) throws Exception {
		AitMovimento aitMovimento = this.aitMovimentoService.getUltimoMovimento(this.aitInconsistenciaDTO.getCdAit(), customConnection);
		if(aitMovimento == null || aitMovimento.getTpStatus() != TipoStatusEnum.CANCELA_REGISTRO_MULTA.getKey()) {
		this.aitService.gerarCancelamento(new AitMovimentoBuilder()
				.setCdAit(this.aitInconsistenciaDTO.getCdAit())
				.setCdOcorrencia(this.aitInconsistenciaDTO.getCdOcorrencia())
				.setCdUsuario(this.aitInconsistenciaDTO.getCdUsuario())
				.setDsObservacao(this.aitInconsistenciaDTO.getDsObservacao())
				.setTpStatus(this.aitInconsistenciaDTO.getTpStatusPretendido())
			.build(), customConnection);
		}
	}
	
	public void atualizarAitInconsistencia(int cdAit, int tpStatusPretendido, CustomConnection customConnection) throws Exception {
		List<AitInconsistencia> aitInconsistenciaList = getAitInconsistencia(cdAit, tpStatusPretendido, customConnection);
		setStInconsistencia(aitInconsistenciaList, customConnection);
	}
	
	private List<AitInconsistencia> getAitInconsistencia(int cdAit, int tpStatusPretendido, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit, true);
		searchCriterios.addCriteriosEqualInteger("st_inconsistencia", TipoSituacaoInconsistencia.PENDENTE.getKey(), true);
		searchCriterios.addCriteriosEqualInteger("tp_status_pretendido", tpStatusPretendido, true);
		List<AitInconsistencia> aitInconsistenciaList = this.aitInconsistenciaService.find(searchCriterios, customConnection);
		return aitInconsistenciaList;
	}
	
	private void setStInconsistencia(List<AitInconsistencia> aitInconsistenciaList, CustomConnection customConnection) throws Exception {
		for(AitInconsistencia aitInconsistencia: aitInconsistenciaList) {
			aitInconsistencia.setStInconsistencia(TipoSituacaoInconsistencia.CANCELADO.getKey());
			this.aitInconsistenciaService.update(aitInconsistencia, customConnection);
		}
	}
	
	public boolean isCancelado() throws Exception {
		int tpStatus = new CodigoCancelamentoMovimentoMap().get(aitInconsistenciaDTO.getTpStatusAtual());
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", aitInconsistenciaDTO.getCdAit(), true);
		searchCriterios.addCriteriosEqualInteger("tp_status", tpStatus, true);
		List<AitMovimento> aitMovimentoList = this.aitMovimentoService.find(searchCriterios);
		return !aitMovimentoList.isEmpty();
	}
	
	public boolean isNotAtualizado() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		List<AitInconsistencia> aitInconsistenciaPendenteList = getAitInconsistencia(this.aitInconsistenciaDTO.getCdAit(), this.aitInconsistenciaDTO.getTpStatusPretendido(), customConnection);
		return !aitInconsistenciaPendenteList.isEmpty();
	}
}
