package com.tivic.manager.wsdl.detran.mg.recursojari.externo;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.InfracaoDAO;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.ProtocoloExternoDetranObject;
import com.tivic.manager.wsdl.detran.mg.recursojari.RecursoJariDadosEntrada;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class RecursoJariExternoDadosEntradaFactory {
	
	public static RecursoJariDadosEntrada fazerDadoEntrada(ProtocoloExternoDetranObject protocoloExternoDetranObject, boolean lgHomologacao) throws ValidacaoException{
		Ait ait = protocoloExternoDetranObject.getAit();
		AitMovimento aitMovimento = protocoloExternoDetranObject.getAitMovimento();
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Infracao infracao = InfracaoDAO.get(ait.getCdInfracao());
		
		RecursoJariDadosEntrada recursoJariDadosEntrada = new RecursoJariDadosEntrada();
		recursoJariDadosEntrada.setOrigemSolicitacao(3);//Prefeitura
		recursoJariDadosEntrada.setOrgao(lgHomologacao ? 253710 : orgao.getCdOrgao());
		recursoJariDadosEntrada.setPlaca(ait.getNrPlaca());
		recursoJariDadosEntrada.setAit(ait.getIdAit());
		recursoJariDadosEntrada.setNumeroProcessamento(formatNrProcesso(protocoloExternoDetranObject.getDocumento().getNrDocumento()));
		recursoJariDadosEntrada.setCodigoRenainf(String.valueOf(ait.getNrRenainf()));
		recursoJariDadosEntrada.setCodigoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(0, 4));
		recursoJariDadosEntrada.setCodigoDesdobramentoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(4));
		incluirCodigoMovimentacao(aitMovimento, recursoJariDadosEntrada);
		recursoJariDadosEntrada.setNumeroProcessoRecurso(aitMovimento.getNrProcesso());
		adicionarDataEntrada(protocoloExternoDetranObject, recursoJariDadosEntrada);
		adicionarDataEncerramento(aitMovimento, recursoJariDadosEntrada);
		//recursoJariDadosEntrada.setCodigoReclassificacaoInfracao(0);
		recursoJariDadosEntrada.setDataMovimentacao(aitMovimento.getDtMovimento());
		//recursoJariDadosEntrada.setNumeroDocumento(null);
		//recursoJariDadosEntrada.setDataPublicacaoResultadoJari(new GregorianCalendar());
		recursoJariDadosEntrada.setNmServico("RECURSO JARI EXTERNO");
		return recursoJariDadosEntrada;
	}
	
	private static void incluirCodigoMovimentacao(AitMovimento aitMovimento, RecursoJariDadosEntrada recursoJariDadosEntrada) {
		switch(aitMovimento.getTpStatus()) {
			case AitMovimentoServices.RECURSO_JARI:
				recursoJariDadosEntrada.setCodigoMovimentacao(10);
				break;
			case AitMovimentoServices.JARI_COM_PROVIMENTO:
				recursoJariDadosEntrada.setCodigoMovimentacao(11);
				break;
			case AitMovimentoServices.JARI_SEM_PROVIMENTO:
				recursoJariDadosEntrada.setCodigoMovimentacao(12);
				break;
			case AitMovimentoServices.RECURSO_CETRAN:
				recursoJariDadosEntrada.setCodigoMovimentacao(14);
				break;
			case AitMovimentoServices.CETRAN_DEFERIDO:
				recursoJariDadosEntrada.setCodigoMovimentacao(23);
				break;
			case AitMovimentoServices.CETRAN_INDEFERIDO:
				recursoJariDadosEntrada.setCodigoMovimentacao(24);
				break;
		}
	}
	

	private static void adicionarDataEntrada(ProtocoloExternoDetranObject protocoloExternoDetranObject, RecursoJariDadosEntrada recursoJariDadosEntrada) throws ValidacaoException{
		if(protocoloExternoDetranObject.getAit().getDtDigitacao() != null)
			recursoJariDadosEntrada.setDataEntradaRecurso(protocoloExternoDetranObject.getAit().getDtDigitacao());
		else
			recursoJariDadosEntrada.setDataEntradaRecurso(new GregorianCalendar());
	}
	
	private static void adicionarDataEncerramento(AitMovimento aitMovimento, RecursoJariDadosEntrada recursoJariDadosEntrada) {
		switch(aitMovimento.getTpStatus()) {
			case AitMovimentoServices.JARI_COM_PROVIMENTO:
			case AitMovimentoServices.JARI_SEM_PROVIMENTO:
				recursoJariDadosEntrada.setDataEncerramentoRecurso(aitMovimento.getDtMovimento());
		}
	}
	
	private static String formatNrProcesso(String entry) throws ValidacaoException {
		String num = entry.replaceAll("/", "");

		if(!Util.isNumber(num))
			throw new ValidacaoException("Nº do protocolo é inválido");

		if(entry.length() <= 16)
			return Util.fillLong(Long.parseLong(num), 16);
		else
			return entry;
	}

}
