package com.tivic.manager.wsdl.detran.mg.builders;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.detran.mg.consultarmovimentacoes.ConsultarMovimentacoesDadosRetorno;
import com.tivic.manager.wsdl.detran.mg.consultarmovimentacoes.Movimentacao;
import com.tivic.manager.wsdl.interfaces.DadosRetorno;

public class AitBuilderSearchMovimentacoes extends AitBuilderSearchProdemge implements IAitBuilderSearchProdemge {

	@Override
	public void build(Ait ait, DadosRetorno dadosRetorno) throws Exception {

		ConsultarMovimentacoesDadosRetorno consultarMovimentacoesDadosRetorno = (ConsultarMovimentacoesDadosRetorno) dadosRetorno;
		
		for(Movimentacao movimentacao : consultarMovimentacoesDadosRetorno.getMovimentacoes()) {
			Integer tpStatus = new TabelaConversaoCodigoMovimentacao().convert(movimentacao.getCodigoMovimentacao()); 
			if(tpStatus != null) {
				AitMovimento aitMovimento = new AitMovimento();
				aitMovimento.setTpStatus(tpStatus);
				adicionarDataMovimento(ait, aitMovimento, movimentacao);
				aitMovimento.setLgEnviadoDetran(1);
				adicionarDataRegistroDetran(aitMovimento, movimentacao);
				ait.getMovimentos().add(aitMovimento);
			}
		}
		
	}

	private void adicionarDataMovimento(Ait ait, AitMovimento aitMovimento, Movimentacao movimentacao) {
		String[] partesDescricaoMovimentacao = movimentacao.getDescricaoMovimentacao1().split("-");
		if(Util.convStringToCalendar(partesDescricaoMovimentacao[1]) != null)
			aitMovimento.setDtMovimento(Util.convStringToCalendar(partesDescricaoMovimentacao[1]));
		else
			aitMovimento.setDtMovimento(Util.convStringToCalendar(partesDescricaoMovimentacao[2]));
		
		if(aitMovimento.getTpStatus() == AitMovimentoServices.REGISTRO_INFRACAO) {
			aitMovimento.setDtMovimento(ait.getDtInfracao());
		}
	}

	private void adicionarDataRegistroDetran(AitMovimento aitMovimento, Movimentacao movimentacao) {
		String[] partesDescricaoMovimentacao = movimentacao.getDescricaoMovimentacao1().split("-");
		if(Util.convStringToCalendar(partesDescricaoMovimentacao[1]) != null)
			aitMovimento.setDtRegistroDetran(Util.convStringToCalendar(partesDescricaoMovimentacao[1]));
		else
			aitMovimento.setDtRegistroDetran(Util.convStringToCalendar(partesDescricaoMovimentacao[2]));
	}

}
