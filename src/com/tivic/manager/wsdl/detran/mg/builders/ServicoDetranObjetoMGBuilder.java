package com.tivic.manager.wsdl.detran.mg.builders;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.wsdl.detran.mg.DadosRetornoErroMG;
import com.tivic.manager.wsdl.detran.mg.DadosRetornoMG;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;

public class ServicoDetranObjetoMGBuilder {

	private ServicoDetranObjetoMG servicoDetranObjetoMG;
	
	public ServicoDetranObjetoMGBuilder(Ait ait, AitMovimento aitMovimento) {
		servicoDetranObjetoMG = new ServicoDetranObjetoMG();
		servicoDetranObjetoMG.setAit(ait);
		servicoDetranObjetoMG.setAitMovimento(aitMovimento);
	}
	
	public ServicoDetranObjetoMGBuilder dadosRetornoErro(String mensagem) {
		DadosRetornoMG dadosRetornoMG = new DadosRetornoErroMG(-1, mensagem);
		servicoDetranObjetoMG.setDadosRetorno(dadosRetornoMG);
		return this;
	}
	
	public ServicoDetranObjetoMG build() {
		return this.servicoDetranObjetoMG;
	}
}
