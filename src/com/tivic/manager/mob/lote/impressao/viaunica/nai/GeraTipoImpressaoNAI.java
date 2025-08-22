package com.tivic.manager.mob.lote.impressao.viaunica.nai;

import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.lote.impressao.viaunica.TipoImpressaoNotificacao;
import com.tivic.manager.mob.lote.impressao.viaunica.builders.TipoImpressaoNotificacaoBuilder;
import com.tivic.manager.mob.lote.impressao.viaunica.nic.GeraSegundaViaNicNai;
import com.tivic.sol.connection.CustomConnection;

public class GeraTipoImpressaoNAI {
	
	private GeraNAIViaUnica geraNAIViaUnica;
	private GeraNICNaiLote geraNicNaiLote;
	private GeraSegundaViaNicNai geraSegundaViaNicNai;
	private GeraSegundaViaNAI geraSegundaViaNAI;
	private GeraSegundaViaNAILote geraSegundaViaNAILote;
	private GeraSegundaViaNAIEnvio geraSegundaViaNAIEnvio;
	private TipoImpressaoNotificacao tipoImpressaoNotificacao;
	
	public GeraTipoImpressaoNAI(int cdAit, int cdUsuario) throws Exception {
		this.geraNAIViaUnica = new GeraNAIViaUnica();
		this.geraNicNaiLote = new GeraNICNaiLote();
		this.geraSegundaViaNicNai = new GeraSegundaViaNicNai();
		this.geraSegundaViaNAI = new GeraSegundaViaNAI();
		this.geraSegundaViaNAILote = new GeraSegundaViaNAILote();
		this.geraSegundaViaNAIEnvio = new GeraSegundaViaNAIEnvio();
		this.tipoImpressaoNotificacao = new TipoImpressaoNotificacaoBuilder(cdAit, TipoStatusEnum.NAI_ENVIADO.getKey(), cdUsuario).build();
	}
	
	public byte[] gerar(CustomConnection customConnection) throws Exception {
		this.geraNAIViaUnica.setNextGenerator(this.geraNicNaiLote);
		this.geraNicNaiLote.setNextGenerator(this.geraSegundaViaNicNai);
		this.geraSegundaViaNicNai.setNextGenerator(this.geraSegundaViaNAI);
		this.geraSegundaViaNAI.setNextGenerator(this.geraSegundaViaNAILote);
		this.geraSegundaViaNAILote.setNextGenerator(this.geraSegundaViaNAIEnvio);
		byte[] loteNotificacaoNAI = geraNAIViaUnica.gerar(tipoImpressaoNotificacao, customConnection);
		return loteNotificacaoNAI;	
	}
	
}
