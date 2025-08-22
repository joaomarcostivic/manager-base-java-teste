package com.tivic.manager.mob.lotes.impressao.strategy.viaunica;

import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.lotes.builders.impressao.TipoImpressaoNotificacaoBuilder;
import com.tivic.sol.connection.CustomConnection;

public class ProcessadorImpressaoNai {
	private GeraViaUnicaNai geraViaUnicaNai;
	private GeraNicNaiSegundaViaLote geraNicNaiLote;
	private GeraSegundaViaNicNai geraSegundaViaNicNai;
	private GeraSegundaViaNai geraSegundaViaNAI;
	private GeraSegundaViaNaiLote geraSegundaViaNAILote;
	private GeraSegundaViaNaiEnvio geraSegundaViaNAIEnvio;
	private TipoImpressaoNotificacao tipoImpressaoNotificacao;
	
	public ProcessadorImpressaoNai(int cdAit, int cdUsuario) throws Exception {
		this.geraViaUnicaNai = new GeraViaUnicaNai();
		this.geraNicNaiLote = new GeraNicNaiSegundaViaLote();
		this.geraSegundaViaNicNai = new GeraSegundaViaNicNai();
		this.geraSegundaViaNAI = new GeraSegundaViaNai();
		this.geraSegundaViaNAILote = new GeraSegundaViaNaiLote();
		this.geraSegundaViaNAIEnvio = new GeraSegundaViaNaiEnvio();
		this.tipoImpressaoNotificacao = new TipoImpressaoNotificacaoBuilder(cdAit, TipoStatusEnum.NAI_ENVIADO.getKey(), cdUsuario).build();
	}
	
	public byte[] gerar(CustomConnection customConnection) throws Exception {
		this.geraViaUnicaNai.setNextGenerator(this.geraNicNaiLote);
		this.geraNicNaiLote.setNextGenerator(this.geraSegundaViaNicNai);
		this.geraSegundaViaNicNai.setNextGenerator(this.geraSegundaViaNAI);
		this.geraSegundaViaNAI.setNextGenerator(this.geraSegundaViaNAILote);
		this.geraSegundaViaNAILote.setNextGenerator(this.geraSegundaViaNAIEnvio);
		byte[] loteNotificacaoNAI = geraViaUnicaNai.gerar(tipoImpressaoNotificacao, customConnection);
		return loteNotificacaoNAI;	
	}
}
