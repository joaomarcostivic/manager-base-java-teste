package com.tivic.manager.mob.lote.impressao.validator;

import java.sql.Connection;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.AitReportErrorException;
import com.tivic.manager.mob.AitReportServices;
import com.tivic.manager.mob.CorreiosEtiqueta;
import com.tivic.manager.mob.EstrategiaVerificarPrazoDefesa;
import com.tivic.manager.mob.IVerificarPrazoDefesa;
import com.tivic.manager.mob.correios.CorreiosEtiquetaRepositoryDAO;
import com.tivic.manager.util.validator.Validator;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class SemDefesaValidator implements Validator<Ait> {
	public static final int DOCUMENTO_ENTREGUE = 1;
	public static final int DOCUMENTO_PUBLICADO_BASE_ANTIGA = 100;
	public static final int DOCUMENTO_PUBLICADO = 1;
	
	@Override
	public void validate(Ait object, CustomConnection customConnection) throws Exception {
		List<AitMovimento> aitsDefesas = AitMovimentoServices.getAllDefesas(object.getCdAit(), customConnection.getConnection());
		if(aitsDefesas.isEmpty()){
			validarSemDefesa(object, customConnection.getConnection());
		}
	}
	
	private void validarSemDefesa(Ait ait, Connection connect ) throws ValidacaoException, AitReportErrorException, Exception {
		AitMovimento aitNai = AitMovimentoServices.getNai(ait.getCdAit(), connect);
		CorreiosEtiqueta etiqueta = getEtiqueta(aitNai.getCdAit(), aitNai.getTpStatus());
		if (etiqueta != null && verificarEntrega(etiqueta)){
			verificarDadosEntregaPublicacao(ait.getCdAit(), etiqueta.getDtAvisoRecebimento(), connect);
		}
		else if (verificarPublicacao(aitNai)){
			verificarDadosEntregaPublicacao(ait.getCdAit(), aitNai.getDtPublicacaoDo(), connect);
		}
		else{
			verificarSemDadosEntregaPublicacao(ait);
		}
	}
	
	private static CorreiosEtiqueta getEtiqueta(int cdAit, int tpStatus) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("B.cd_ait", cdAit);
		searchCriterios.addCriteriosEqualInteger("A.tp_status", tpStatus);
		List<CorreiosEtiqueta> correiosEtiquetaList = new CorreiosEtiquetaRepositoryDAO().find(searchCriterios, new CustomConnection());
		if(correiosEtiquetaList == null || correiosEtiquetaList.isEmpty()) {
			return null;
		}
		return correiosEtiquetaList.get(0);
	}
	
	private boolean verificarEntrega(CorreiosEtiqueta etiqueta) {
		return  ( etiqueta.getStAvisoRecebimento() <= DOCUMENTO_ENTREGUE || etiqueta.getStAvisoRecebimento() == DOCUMENTO_PUBLICADO_BASE_ANTIGA ) && etiqueta.getDtAvisoRecebimento() != null;
	}
	
	private boolean verificarPublicacao(AitMovimento aitNai) {
		return aitNai.getStPublicacaoDo() == DOCUMENTO_PUBLICADO && aitNai.getDtPublicacaoDo() != null;
	}
	
	private void verificarDadosEntregaPublicacao(int cdAit, GregorianCalendar dtEntreguePublicado, Connection connect)  throws AitReportErrorException{
		try {
		IVerificarPrazoDefesa estrategiaVerificarPrazoDefesa = new EstrategiaVerificarPrazoDefesa().getEstrategia(AitReportServices.verificarEstado(connect));
		estrategiaVerificarPrazoDefesa.verificarPrazoDefesa(cdAit, dtEntreguePublicado, connect);
		} catch (ValidacaoException e) {
			throw new AitReportErrorException(e.getMessage());
		}
	}
	
	private void verificarSemDadosEntregaPublicacao(Ait ait)  throws AitReportErrorException{
		GregorianCalendar dtExpiracao = (GregorianCalendar) ait.getDtInfracao();
		dtExpiracao.add(Calendar.DAY_OF_MONTH, 60);
		if(Util.getDataAtual().before(dtExpiracao))
			throw new AitReportErrorException("NAI não expirou prazo de 60 dias e não possui dados de entrega ou publicação");
	}
}
