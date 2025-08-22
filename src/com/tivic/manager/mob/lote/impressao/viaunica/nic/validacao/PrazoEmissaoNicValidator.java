package com.tivic.manager.mob.lote.impressao.viaunica.nic.validacao;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class PrazoEmissaoNicValidator implements INICValidador {

	private IParametroRepository parametroRepository;
	
	public PrazoEmissaoNicValidator() throws Exception {
		parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
	}

	public boolean validate(Ait ait, CustomConnection customConnection) throws Exception, ValidacaoException {
		return verificarDataInfracao(ait.getDtInfracao());
	}
	
	public boolean verificarDataInfracao(GregorianCalendar dtInfracao) throws Exception {
		int limiteDeAnos = getLimiteDeAnos();
	    if (dtInfracao != null) {
	    	GregorianCalendar dataInfracao = (GregorianCalendar) dtInfracao.clone();
	    	dataInfracao.add(Calendar.YEAR, limiteDeAnos);
	        return Util.getDataAtual().before(dataInfracao);
	    }
	    return false;
	}
	
	private int getLimiteDeAnos() throws Exception {
		int limiteDeAnos = parametroRepository.getValorOfParametroAsInt("MOB_PRAZO_EMISSAO_NIC");
		if(limiteDeAnos <= 0)
			throw new BadRequestException("O parâmetro MOB_PRAZO_EMISSAO_NIC não foi configurado.");	
		return limiteDeAnos;
	}
}
