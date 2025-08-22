package com.tivic.manager.wsdl.detran.mg.notificacao;

import java.util.Calendar;
import java.util.GregorianCalendar;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitDAO;
import com.tivic.manager.mob.AitDataPandemiaValidator;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.AitReaberturaPrazo;
import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.SenderDetran;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoValidacaoBuilder;
import com.tivic.manager.wsdl.detran.mg.ServicosDetranEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.DetranRegistro;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;

public class Notificacao extends ServicoDetran {
	private DetranRegistro detranRegistro;
	private AitDataPandemiaValidator _aitPrazoPandemiaValidator;
	private AitReaberturaPrazo _aitReaberturaPrazo;
	
	public Notificacao() throws Exception {
		this.detranRegistro = new DetranRegistro();
		this._aitPrazoPandemiaValidator = new AitDataPandemiaValidator();
		this._aitReaberturaPrazo = new AitReaberturaPrazo();
	}
	@Override
	public ServicoDetranObjeto executar(AitDetranObject aitDetranObject) {
		try{			
			ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoMG();
			servicoDetranObjeto.setAitMovimento(aitDetranObject.getAitMovimento());
			servicoDetranObjeto.setAit(aitDetranObject.getAit());
			if(isNai(aitDetranObject) && naiAtrasada(aitDetranObject.getAitMovimento()))
				reabrirPrazo(aitDetranObject);
			NotificacaoDadosEntrada notificacaoDadosEntrada = NotificacaoDadosEntradaFactory.fazerDadoEntrada(aitDetranObject, this.arquivoConfiguracao.getLgHomologacao());
			servicoDetranObjeto.setDadosEntrada(notificacaoDadosEntrada);
			NotificacaoDadosRetorno notificacaoDadosRetorno = (NotificacaoDadosRetorno) SenderDetran.send(ServicosDetranEnum.INCLUIR_NOTIFICACAO_PUBLICACAO_PRAZO.getValue(), notificacaoDadosEntrada, this.arquivoConfiguracao, NotificacaoDadosRetorno.class);
			servicoDetranObjeto.setDadosRetorno(notificacaoDadosRetorno);
			detranRegistro.registrar(servicoDetranObjeto, this.arquivoConfiguracao.getLgHomologacao());
			return servicoDetranObjeto;
		}
		catch(ValidacaoException ve) {
			ve.printStackTrace(System.out);
			return new ServicoDetranObjetoValidacaoBuilder(aitDetranObject, ve.getMessage()).build();
		}
		catch(Exception e){
			e.printStackTrace();
			return new ServicoDetranObjetoValidacaoBuilder(aitDetranObject, "Erro de sistema ao enviar").build();
		}
	}

	private boolean isNai (AitDetranObject aitDetranObject){
		boolean isNai = false;
		if (aitDetranObject.getAitMovimento().getTpStatus() == AitMovimentoServices.NAI_ENVIADO)
			isNai = true;
		return isNai;
	}
	
	private boolean naiAtrasada(AitMovimento _movimento) {
		Ait _ait = AitDAO.get(_movimento.getCdAit());
		GregorianCalendar dtPrazo = new GregorianCalendar();
		dtPrazo.set(Calendar.HOUR, 0);
		dtPrazo.set(Calendar.MINUTE, 0);
		dtPrazo.set(Calendar.SECOND, 0);
		dtPrazo.add(Calendar.DATE, -30);
		return _ait.getDtInfracao().before(dtPrazo);
	}
	
	private void reabrirPrazo(AitDetranObject aitDetranObject) {
		if(_aitPrazoPandemiaValidator.validPeriodoPandemia(aitDetranObject.getAit().getDtInfracao())) {
			_aitReaberturaPrazo.reabrirPrazo(aitDetranObject.getAitMovimento(), null);
		}
	}

}
