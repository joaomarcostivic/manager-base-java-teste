package com.tivic.manager.wsdl.detran.mg.reaberturaprazo;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.InfracaoDAO;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.wsdl.AitDetranObject;

public class ReaberturaPrazoDadosEntradaFactory {
	
	public static ReaberturaPrazoDadosEntrada fazerDadoEntrada(AitDetranObject aitDetranObject, boolean lgHomologacao) {
		Ait ait = aitDetranObject.getAit();
		AitMovimento aitMovimento = aitDetranObject.getAitMovimento();
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Infracao infracao = InfracaoDAO.get(ait.getCdInfracao());
		
		ReaberturaPrazoDadosEntrada reaberturaPrazoDadosentrada = new ReaberturaPrazoDadosEntrada();
		
		reaberturaPrazoDadosentrada.setOrigemSolicitacao(3);// 3 = Prefeitura
		reaberturaPrazoDadosentrada.setOrgao(lgHomologacao ? 253710 : orgao.getCdOrgao());
		reaberturaPrazoDadosentrada.setPlaca(ait.getNrPlaca());
		reaberturaPrazoDadosentrada.setAit(ait.getIdAit());
		reaberturaPrazoDadosentrada.setNumeroProcessamento(ait.getNrControle());
		reaberturaPrazoDadosentrada.setCodigoRenainf(ait.getNrRenainf());
		reaberturaPrazoDadosentrada.setCodigoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(0, 4));
		reaberturaPrazoDadosentrada.setCodigoDesdobramentoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(4));
		incluirCodigoMovimentacao(reaberturaPrazoDadosentrada, aitMovimento);
//		reaberturaPrazoDadosentrada.setDataNotificacao(aitMovimento.getDtMovimento());
//		reaberturaPrazoDadosentrada.setNumeroNotificacao(Integer.toString(ait.getNrNotificacaoNip()));
//		reaberturaPrazoDadosentrada.setIndicadorContas(indicadorContas);
//		reaberturaPrazoDadosentrada.setNumeroArCorreio(numeroArCorreio);
//		reaberturaPrazoDadosentrada.setDataPostagemCorreio(dataPostagemCorreio);
//		reaberturaPrazoDadosentrada.setDataEntregaCorreio(dataEntregaCorreio);
//		reaberturaPrazoDadosentrada.setCodigoRetornoCorreio(codigoRetornoCorreio);
		incluirDataNovoMovimento(ait, aitMovimento, reaberturaPrazoDadosentrada);
//		reaberturaPrazoDadosentrada.setDataNovoPrazoFici(dataNovoPrazoFici);
//		reaberturaPrazoDadosentrada.setIndicadorNotificacaoEdital(1);//Não
//		reaberturaPrazoDadosentrada.setDataNotificacaoEdital(dataNotificacaoEdital);
//		incluirTipoPenalidade(ait, aitMovimento, reaberturaPrazoDadosentrada);
		reaberturaPrazoDadosentrada.setNmServico("REABERTURA PRAZO");
		return reaberturaPrazoDadosentrada;
	}
	
	private static void incluirCodigoMovimentacao(ReaberturaPrazoDadosEntrada reaberturaPrazoDadosentrada, AitMovimento aitMovimento) {
		if(aitMovimento.getTpStatus() == 97)
			reaberturaPrazoDadosentrada.setCodigoMovimentacao(7);

		if(aitMovimento.getTpStatus() == 98)
			reaberturaPrazoDadosentrada.setCodigoMovimentacao(8);
		
		if(aitMovimento.getTpStatus() == 99)
			reaberturaPrazoDadosentrada.setCodigoMovimentacao(9);
	}
	
	private static void incluirTipoPenalidade(Ait ait, AitMovimento aitMovimento, ReaberturaPrazoDadosEntrada reaberturaPrazoDadosentrada) {
		if(aitMovimento.getTpStatus() == AitMovimentoServices.NIP_ENVIADA) {
			if(ait.getLgAdvertencia() == 1)
				reaberturaPrazoDadosentrada.setTipoPenalidade(2);
			else
				reaberturaPrazoDadosentrada.setTipoPenalidade(1);
		}
	}
	
	private static void incluirDataNovoMovimento(Ait ait, AitMovimento aitMovimento, ReaberturaPrazoDadosEntrada reaberturaPrazoDadosEntrada) {
		GregorianCalendar novoPrazo = new GregorianCalendar();
		novoPrazo.set(Calendar.HOUR, 0);
		novoPrazo.set(Calendar.MINUTE, 0);
		novoPrazo.set(Calendar.SECOND, 0);
		novoPrazo.add(Calendar.DATE, 30);
		reaberturaPrazoDadosEntrada.setDataNovoPrazoFici(novoPrazo);
	}

}
