package com.tivic.manager.wsdl.detran.mg.notificacao;

import java.util.ArrayList;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.InfracaoDAO;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.notificacao.alterarprazorecurso.DataNovoPrazoRecurso;

public class NotificacaoDadosEntradaFactory {
	
	public static NotificacaoDadosEntrada fazerDadoEntrada(AitDetranObject aitDetranObject, boolean lgHomologacao){
		Ait ait = aitDetranObject.getAit();
		AitMovimento aitMovimento = aitDetranObject.getAitMovimento();
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Infracao infracao = InfracaoDAO.get(ait.getCdInfracao());
		
		NotificacaoDadosEntrada notificacaoDadosEntrada = new NotificacaoDadosEntrada();
		notificacaoDadosEntrada.setOrigemSolicitacao(3);//Prefeitura
		notificacaoDadosEntrada.setOrgao(lgHomologacao ? 253710 : orgao.getCdOrgao());
		notificacaoDadosEntrada.setPlaca(ait.getNrPlaca());
		notificacaoDadosEntrada.setAit(ait.getIdAit());
		notificacaoDadosEntrada.setNumeroProcessamento(ait.getNrControle());
		notificacaoDadosEntrada.setCodigoRenainf(ait.getNrRenainf());
		notificacaoDadosEntrada.setCodigoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(0, 4));
		notificacaoDadosEntrada.setCodigoDesdobramentoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(4));
		notificacaoDadosEntrada.setCodigoMovimentacao(new CodigoMovimentacaoMap().get(aitMovimento.getTpStatus()));
		notificacaoDadosEntrada.setDataNotificacao(aitMovimento.getDtMovimento());
		notificacaoDadosEntrada.setNumeroNotificacao(Integer.toString(ait.getNrNotificacaoNip()));
		notificacaoDadosEntrada.setIndicadorNotificacaoEdital(1);//Não
		incluirTipoPenalidade(ait, aitMovimento, notificacaoDadosEntrada);
		setNovoPrazoRecurso(notificacaoDadosEntrada, aitMovimento, ait);
		notificacaoDadosEntrada.setNmServico("NOTIFICACAO");
		return notificacaoDadosEntrada;
	}
	
	private static void incluirTipoPenalidade(Ait ait, AitMovimento aitMovimento, NotificacaoDadosEntrada notificacaoDadosEntrada) {
		if(aitMovimento.getTpStatus() == AitMovimentoServices.NIP_ENVIADA) {
			if(ait.getLgAdvertencia() == 1)
				notificacaoDadosEntrada.setTipoPenalidade(2);
			else
				notificacaoDadosEntrada.setTipoPenalidade(1);
		} else {
			if(aitMovimento.getTpStatus() == AitMovimentoServices.NOTIFICACAO_ADVERTENCIA) {
				notificacaoDadosEntrada.setTipoPenalidade(2);
			}
		}
	}
	
	private static void setNovoPrazoRecurso(NotificacaoDadosEntrada notificacaoDadosEntrada, AitMovimento aitMovimento, Ait ait) {
		if (isNovoPrazoRecurso(notificacaoDadosEntrada.getCodigoMovimentacao())) {
			notificacaoDadosEntrada.setDataNovoPrazoFici(new DataNovoPrazoRecurso().get(aitMovimento.getTpStatus(), ait));
			notificacaoDadosEntrada.setDataNotificacao(null);
			notificacaoDadosEntrada.setNumeroNotificacao(null);
		}
	}
	
	private static boolean isNovoPrazoRecurso(int codMovimento){
		ArrayList<Integer> codNovoPrazoRecurso = new ArrayList<Integer>();
		codNovoPrazoRecurso.add(TipoNotificacaoEnum.NOVO_PRAZO_DEFESA.getKey());
		codNovoPrazoRecurso.add(TipoNotificacaoEnum.NOVO_PRAZO_JARI.getKey());
		return codNovoPrazoRecurso.contains(codMovimento);
	}
	
}
