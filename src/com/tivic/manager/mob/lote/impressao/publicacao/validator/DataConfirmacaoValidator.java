package com.tivic.manager.mob.lote.impressao.publicacao.validator;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.validation.Validator;

public class DataConfirmacaoValidator implements Validator<LoteImpressao> {

	@Override
	public void validate(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception {
		dataConfirmacaoValidate(loteImpressao.getDtCriacao(), loteImpressao.getDtEnvio());
		dataAtualValidate(loteImpressao.getDtEnvio());
	}

	public void dataConfirmacaoValidate(GregorianCalendar dateCriacao, GregorianCalendar dateConfirmacao) throws Exception, ValidacaoException {
		dateCriacao.set(Calendar.HOUR_OF_DAY, 0);
		dateCriacao.set(Calendar.MINUTE, 0);
		dateCriacao.set(Calendar.SECOND, 0);
		dateCriacao.set(Calendar.MILLISECOND, 0);
		if (dateConfirmacao.before(dateCriacao)) {
			throw new ValidacaoException("A data de confirmação não deve ser menor que a data de criação.");
		} 
	}
	
	public void dataAtualValidate(GregorianCalendar dateConfirmacao) throws Exception, ValidacaoException {
		GregorianCalendar dataAtual = new GregorianCalendar();
		dataAtual.set(Calendar.HOUR_OF_DAY, 23);
		dataAtual.set(Calendar.MINUTE, 59);
		dataAtual.set(Calendar.SECOND, 59);
		dataAtual.set(Calendar.MILLISECOND, 0);
		if (dateConfirmacao.after(dataAtual)) {
			throw new ValidacaoException("A data de confirmação não deve maior que a data de atual.");
		} 
	}
	
}
