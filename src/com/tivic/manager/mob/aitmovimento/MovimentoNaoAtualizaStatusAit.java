package com.tivic.manager.mob.aitmovimento;

import java.util.ArrayList;
import com.tivic.manager.mob.TipoStatusEnum;

public class MovimentoNaoAtualizaStatusAit {

	private ArrayList<Integer> movimentos = new ArrayList<Integer>();
	
	public MovimentoNaoAtualizaStatusAit() throws Exception {
		adicionarMovimentos();
	}
	
	private void adicionarMovimentos() {
		movimentos.add(TipoStatusEnum.APRESENTACAO_CONDUTOR.getKey());
		movimentos.add(TipoStatusEnum.PUBLICACAO_NAI.getKey());
		movimentos.add(TipoStatusEnum.PUBLICACAO_NIP.getKey());
		movimentos.add(TipoStatusEnum.PUBLICACAO_RESULTADO_JARI.getKey());
		movimentos.add(TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_DEFERIDA.getKey());
		movimentos.add(TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_INDEFERIDA .getKey());
		movimentos.add(TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_PREVIA.getKey());
		movimentos.add(TipoStatusEnum.CANCELAMENTO_CETRAN_COM_PROVIMENTO.getKey());
		movimentos.add(TipoStatusEnum.CANCELAMENTO_CETRAN_SEM_ACOLHIMENTO.getKey());
		movimentos.add(TipoStatusEnum.CANCELAMENTO_DEFESA_DEFERIDA.getKey());
		movimentos.add(TipoStatusEnum.CANCELAMENTO_DEFESA_INDEFERIDA.getKey());
		movimentos.add(TipoStatusEnum.CANCELAMENTO_DEFESA_PREVIA.getKey());
		movimentos.add(TipoStatusEnum.CANCELAMENTO_FICI.getKey());
		movimentos.add(TipoStatusEnum.CANCELAMENTO_JARI_COM_PROVIMENTO.getKey());
		movimentos.add(TipoStatusEnum.CANCELAMENTO_JARI_SEM_ACOLHIMENTO.getKey());
		movimentos.add(TipoStatusEnum.CANCELAMENTO_MULTA.getKey());
		movimentos.add(TipoStatusEnum.CANCELAMENTO_NIP.getKey());
		movimentos.add(TipoStatusEnum.CANCELAMENTO_PONTUACAO.getKey());
		movimentos.add(TipoStatusEnum.CANCELAMENTO_RECURSO_CETRAN.getKey());
		movimentos.add(TipoStatusEnum.CANCELAMENTO_RECURSO_JARI.getKey());
		movimentos.add(TipoStatusEnum.FIM_PRAZO_DEFESA.getKey());
		movimentos.add(TipoStatusEnum.NOVO_PRAZO_DEFESA.getKey());
		movimentos.add(TipoStatusEnum.NOVO_PRAZO_JARI.getKey());
		movimentos.add(TipoStatusEnum.SITUACAO_NAO_DEFINIDA.getKey());
		movimentos.add(TipoStatusEnum.CANCELAMENTO_CETRAN_SEM_PROVIMENTO.getKey());
		movimentos.add(TipoStatusEnum.CANCELAMENTO_PAGAMENTO.getKey());
		movimentos.add(TipoStatusEnum.CANCELAMENTO_DIVIDA_ATIVA.getKey());
		movimentos.add(TipoStatusEnum.DADOS_CORREIO_NA.getKey());
		movimentos.add(TipoStatusEnum.DADOS_CORREIO_NP.getKey());
	}
	
	public ArrayList<Integer> getMovimento() {
		return this.movimentos;
	}
	
	public boolean verificar(int tpStatus) {
		return this.movimentos.contains(tpStatus);
	}
	
}
