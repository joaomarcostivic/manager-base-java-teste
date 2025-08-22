package com.tivic.manager.wsdl.detran.mg.recursodefesa.externo;

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
import com.tivic.manager.wsdl.detran.mg.TabelasAuxiliaresMG;
import com.tivic.manager.wsdl.detran.mg.recursodefesa.RecursoDefesaDadosEntrada;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class DefesaPreviaExternaDadosEntradaFactory {
	
static TabelasAuxiliaresMG tabelasAuxiliaresMG = new TabelasAuxiliaresMG();
	
	public static RecursoDefesaDadosEntrada fazerDadoEntrada(ProtocoloExternoDetranObject protocoloExternoDetranObject, boolean lgHomologacao) throws ValidacaoException{
		Ait ait = protocoloExternoDetranObject.getAit();
		AitMovimento aitMovimento = protocoloExternoDetranObject.getAitMovimento();
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Infracao infracao = InfracaoDAO.get(ait.getCdInfracao());
		
		RecursoDefesaDadosEntrada defesaPreviaDadosEntrada = new RecursoDefesaDadosEntrada();
		defesaPreviaDadosEntrada.setOrigemSolicitacao(3);//Prefeitura
		defesaPreviaDadosEntrada.setOrgao(lgHomologacao ? 253710 : orgao.getCdOrgao());
		defesaPreviaDadosEntrada.setPlaca(ait.getNrPlaca());
		defesaPreviaDadosEntrada.setAit(ait.getIdAit());
		defesaPreviaDadosEntrada.setNumeroProcessamento(formatNrProcesso(protocoloExternoDetranObject.getDocumento().getNrDocumento()));
		defesaPreviaDadosEntrada.setCodigoRenainf(String.valueOf(ait.getNrRenainf()));
		defesaPreviaDadosEntrada.setCodigoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(0, 4));
		defesaPreviaDadosEntrada.setCodigoDesdobramentoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(4));
		defesaPreviaDadosEntrada.setCodigoMovimentacao(tabelasAuxiliaresMG.getMovimentoDefesa(aitMovimento.getTpStatus()));
		defesaPreviaDadosEntrada.setNumeroProcessoDefesa(aitMovimento.getNrProcesso());
		
		adicionarDataEntrada(defesaPreviaDadosEntrada, protocoloExternoDetranObject);
		adicionarDataEncerramento(defesaPreviaDadosEntrada, protocoloExternoDetranObject);
		defesaPreviaDadosEntrada.setDataMovimentacao(aitMovimento.getDtMovimento());
		//defesaPreviaDadosEntrada.setNumeroDocumento(null);
		defesaPreviaDadosEntrada.setNmServico("DEFESA PREVIA EXTERNA");
		return defesaPreviaDadosEntrada;
	}
	
	private static void adicionarDataEntrada(RecursoDefesaDadosEntrada defesaPreviaDadosEntrada, ProtocoloExternoDetranObject protocoloExternoDetranObject) throws ValidacaoException{
		if(protocoloExternoDetranObject.getAit().getDtDigitacao() != null)
			defesaPreviaDadosEntrada.setDataEntradaDefesa(protocoloExternoDetranObject.getAit().getDtDigitacao());
		else
			defesaPreviaDadosEntrada.setDataEntradaDefesa(new GregorianCalendar());
	}
	
	private static void adicionarDataEncerramento(RecursoDefesaDadosEntrada defesaPreviaDadosEntrada, ProtocoloExternoDetranObject protocoloExternoDetranObject) {
		switch(protocoloExternoDetranObject.getAitMovimento().getTpStatus()) {
			case AitMovimentoServices.DEFESA_DEFERIDA:
			case AitMovimentoServices.DEFESA_INDEFERIDA:
				defesaPreviaDadosEntrada.setDataEncerramentoDefesa(protocoloExternoDetranObject.getAitMovimento().getDtMovimento());
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
