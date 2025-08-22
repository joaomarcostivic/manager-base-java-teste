package com.tivic.manager.wsdl.detran.mg.builders;

import java.util.HashMap;

import com.tivic.manager.mob.AitMovimentoServices;

public class TabelaConversaoCodigoMovimentacao {

	private HashMap<Integer, Integer> codigosMovimentacao;
	
	private static final Integer INCLUSAO = 1;
	private static final Integer QUITACAO_BANCARIA = 2;
	private static final Integer BAIXADA_SOLICITACAO_AUT_TRANSITO = 9;
	private static final Integer ABERTURA_RECURSO_JARI = 10;
	private static final Integer INDEFERIMENTO_RECURSO_JARI = 12;
	private static final Integer ABERTURA_DEFESA_AUTUACAO = 36;
	private static final Integer ACOLHIMENTO_DEFESA_AUTUACAO = 37;
	private static final Integer NAO_ACOLHIMENTO_DEFESA_AUTUACAO = 38;
	private static final Integer COBRANCA_OBRIGATORIA = 39;
	private static final Integer REGISTRO_FICI = 56;
	private static final Integer CANCELAMENTO_NAO_ACOLHIMENTO_DEFESA = 82;
	private static final Integer ATUALIZACAO_AIT = 84;
	private static final Integer ACEITE_REGISTRO_FICI = 90;
	private static final Integer CANCELAMENTO_NOTIFICACAO_PENALIDADE = 127;
	
	public TabelaConversaoCodigoMovimentacao() {
		codigosMovimentacao = new HashMap<Integer, Integer>();
		codigosMovimentacao.put(TabelaConversaoCodigoMovimentacao.INCLUSAO, AitMovimentoServices.REGISTRO_INFRACAO);
		codigosMovimentacao.put(TabelaConversaoCodigoMovimentacao.QUITACAO_BANCARIA, AitMovimentoServices.MULTA_PAGA);
		codigosMovimentacao.put(TabelaConversaoCodigoMovimentacao.BAIXADA_SOLICITACAO_AUT_TRANSITO, AitMovimentoServices.CANCELAMENTO_MULTA);
		codigosMovimentacao.put(TabelaConversaoCodigoMovimentacao.ABERTURA_RECURSO_JARI, AitMovimentoServices.RECURSO_JARI);
		codigosMovimentacao.put(TabelaConversaoCodigoMovimentacao.INDEFERIMENTO_RECURSO_JARI, AitMovimentoServices.JARI_SEM_PROVIMENTO);
		codigosMovimentacao.put(TabelaConversaoCodigoMovimentacao.ABERTURA_DEFESA_AUTUACAO, AitMovimentoServices.DEFESA_PREVIA);
		codigosMovimentacao.put(TabelaConversaoCodigoMovimentacao.ACOLHIMENTO_DEFESA_AUTUACAO, AitMovimentoServices.DEFESA_DEFERIDA);
		codigosMovimentacao.put(TabelaConversaoCodigoMovimentacao.NAO_ACOLHIMENTO_DEFESA_AUTUACAO, AitMovimentoServices.DEFESA_INDEFERIDA);
		codigosMovimentacao.put(TabelaConversaoCodigoMovimentacao.COBRANCA_OBRIGATORIA, AitMovimentoServices.FIM_PRAZO_DEFESA);
		//codigosMovimentacao.put(TabelaConversaoCodigoMovimentacao.REGISTRO_FICI, AitMovimentoServices.APRESENTACAO_CONDUTOR);
		codigosMovimentacao.put(TabelaConversaoCodigoMovimentacao.CANCELAMENTO_NAO_ACOLHIMENTO_DEFESA, AitMovimentoServices.CANCELAMENTO_DEFESA_INDEFERIDA);
		//codigosMovimentacao.put(TabelaConversaoCodigoMovimentacao.ATUALIZACAO_AIT, AitMovimentoServices.ATUALIZACAO_REGISTRO);
		codigosMovimentacao.put(TabelaConversaoCodigoMovimentacao.ACEITE_REGISTRO_FICI, AitMovimentoServices.APRESENTACAO_CONDUTOR);
		codigosMovimentacao.put(TabelaConversaoCodigoMovimentacao.CANCELAMENTO_NOTIFICACAO_PENALIDADE, AitMovimentoServices.CANCELAMENTO_NIP);
	}
	
	public Integer convert(Integer codigoMovimentacao) {
		return this.codigosMovimentacao.get(codigoMovimentacao);
	}
}
