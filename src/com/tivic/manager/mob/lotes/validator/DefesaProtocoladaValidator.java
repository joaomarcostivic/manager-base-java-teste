package com.tivic.manager.mob.lotes.validator;

import java.sql.Connection;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.AitReportErrorException;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class DefesaProtocoladaValidator implements IValidator<Ait> {
	
	@Override
	public void validate(Ait object, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", object.getCdAit(), true);
		List<AitMovimento> aitsDefesas = getDefesas(searchCriterios, customConnection);
		if(aitsDefesas.isEmpty()){
			return;
		}
		else{
			validarDefesaProtocolada(object, aitsDefesas, customConnection.getConnection());
		}
	}
	
	public void validarDefesaProtocolada(Ait ait, List<AitMovimento> aitsDefesas, Connection connect ) throws AitReportErrorException {
		for(AitMovimento aitDefesa : aitsDefesas) {
			if(verificarDefesaIndeferidaEnviadoDetran(aitDefesa) || verificarAdvertenciaDefesaIndeferidaEnviadoDetran(aitDefesa)) {
				verificarCacelamentoIndeferimento(ait.getCdAit(), connect);
				return;
			}
			else if(verificarDefesaDeferidaEnviadoDetran(aitDefesa) || verificarAdvertenciaDefesaDeferidaEnviadoDetran(aitDefesa)) {
				verificarCacelamentoDeferimento(ait.getCdAit(), connect);
				return;
			} else if(verificarDefesaPreviaEnviadoDetran(aitDefesa) || verificarAdvertenciaDefesaPreviaEnviadoDetran(aitDefesa)) {
				verificarCacelamentoDefesa(ait.getCdAit(), connect);
				return;
			}
		}
		return;
	}
	
	private boolean verificarDefesaPreviaEnviadoDetran(AitMovimento aitDefesa) {
		return aitDefesa.getTpStatus() == TipoStatusEnum.DEFESA_PREVIA.getKey() && aitDefesa.getLgEnviadoDetran() == TipoStatusEnum.ENVIADO_AO_DETRAN.getKey();
	}
	
	private boolean verificarAdvertenciaDefesaPreviaEnviadoDetran(AitMovimento aitDefesa) {
		return aitDefesa.getTpStatus() == TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey() && aitDefesa.getLgEnviadoDetran() == TipoStatusEnum.ENVIADO_AO_DETRAN.getKey();
	}
	
	private boolean verificarDefesaIndeferidaEnviadoDetran(AitMovimento aitDefesa) {
		return aitDefesa.getTpStatus() == TipoStatusEnum.DEFESA_INDEFERIDA.getKey() && aitDefesa.getLgEnviadoDetran() == TipoStatusEnum.ENVIADO_AO_DETRAN.getKey();
	}
	
	private boolean verificarAdvertenciaDefesaIndeferidaEnviadoDetran(AitMovimento aitDefesa) {
		return aitDefesa.getTpStatus() == TipoStatusEnum.ADVERTENCIA_DEFESA_INDEFERIDA.getKey() && aitDefesa.getLgEnviadoDetran() == TipoStatusEnum.ENVIADO_AO_DETRAN.getKey();
	}
	
	private boolean verificarDefesaDeferidaEnviadoDetran(AitMovimento aitDefesa) {
		return aitDefesa.getTpStatus() == TipoStatusEnum.DEFESA_DEFERIDA.getKey() && aitDefesa.getLgEnviadoDetran() == TipoStatusEnum.ENVIADO_AO_DETRAN.getKey();
	}
	
	private boolean verificarAdvertenciaDefesaDeferidaEnviadoDetran(AitMovimento aitDefesa) {
		return aitDefesa.getTpStatus() == TipoStatusEnum.ADVERTENCIA_DEFESA_DEFERIDA.getKey() && aitDefesa.getLgEnviadoDetran() == TipoStatusEnum.ENVIADO_AO_DETRAN.getKey();
	}
	
	private void verificarCacelamentoIndeferimento(int cdAit, Connection connect) throws AitReportErrorException {
		List<AitMovimento> aitsDefesasCanceladas = AitMovimentoServices.getAllDefesasCanceladas(cdAit, connect);
		for(AitMovimento aitDefesa : aitsDefesasCanceladas) {
			if(aitDefesa.getTpStatus() == TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_INDEFERIDA.getKey() ||
							aitDefesa.getTpStatus() == TipoStatusEnum.CANCELAMENTO_DEFESA_INDEFERIDA.getKey()){
				throw new AitReportErrorException("AIT possui uma defesa");
			}
		}
	}
	
	private void verificarCacelamentoDeferimento(int cdAit, Connection connect) throws AitReportErrorException {
		List<AitMovimento> aitsDefesasCanceladas = AitMovimentoServices.getAllDefesasCanceladas(cdAit, connect);
		for(AitMovimento aitDefesa : aitsDefesasCanceladas) {
			if(aitDefesa.getTpStatus() == TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_DEFERIDA.getKey() ||
					aitDefesa.getTpStatus() == TipoStatusEnum.CANCELAMENTO_DEFESA_DEFERIDA.getKey()){
				return;
			}
		}
		throw new AitReportErrorException("O AIT possui uma defesa");
	}
	
	private void verificarCacelamentoDefesa(int cdAit, Connection connect) throws AitReportErrorException {
		List<AitMovimento> aitsDefesas = AitMovimentoServices.getAllDefesas(cdAit, connect);
		for (AitMovimento aitDefesa : aitsDefesas) {
			if ((existeResultadoDefesaEnviada(aitDefesa) || existeDefesaEnviada(aitDefesa)) &&
					aitDefesa.getLgCancelaMovimento() == 1) {
				return;
			}
		}
		throw new AitReportErrorException("AIT possui uma entrada de defesa");
	}
	
	private boolean existeResultadoDefesaEnviada(AitMovimento aitDefesa) {
		return (verificarDefesaIndeferidaEnviadoDetran(aitDefesa) || 
				   verificarAdvertenciaDefesaIndeferidaEnviadoDetran(aitDefesa) || 
				   verificarDefesaDeferidaEnviadoDetran(aitDefesa) || 
				   verificarAdvertenciaDefesaDeferidaEnviadoDetran(aitDefesa)) && 
				aitDefesa.getLgCancelaMovimento() == 0;
	}

	private boolean existeDefesaEnviada(AitMovimento aitDefesa) {
		return verificarDefesaPreviaEnviadoDetran(aitDefesa) || verificarAdvertenciaDefesaPreviaEnviadoDetran(aitDefesa);
	}
	
	private List<AitMovimento> getDefesas(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<AitMovimento> search = new SearchBuilder<AitMovimento>("mob_ait_movimento")
			.searchCriterios(searchCriterios)
			.additionalCriterias(" tp_status IN ("
				+ TipoStatusEnum.DEFESA_PREVIA.getKey() + ", " 
				+ TipoStatusEnum.DEFESA_DEFERIDA.getKey() + ", " 
				+ TipoStatusEnum.DEFESA_INDEFERIDA.getKey() + ", " 
				+ TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey() + ", " 
				+ TipoStatusEnum.ADVERTENCIA_DEFESA_DEFERIDA.getKey() + ", " 
				+ TipoStatusEnum.ADVERTENCIA_DEFESA_INDEFERIDA.getKey() +
			")")
			.customConnection(customConnection)
			.orderBy("dt_movimento DESC")
			.build();
		return search.getList(AitMovimento.class);
	}
}
