package com.tivic.manager.mob.lotes.impressao.strategy.viaunica;

import java.util.Arrays;

import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.lotes.dto.impressao.NipImpressaoDTO;
import com.tivic.manager.mob.lotes.factory.impressao.TipoImpressaoNotificacaoFactory;
import com.tivic.sol.connection.CustomConnection;

public class ProcessadorImpressaoNip {
	private TipoStatusEnum[] tipoStatusPermitidos = { TipoStatusEnum.NIP_ENVIADA, TipoStatusEnum.NOTIFICACAO_ADVERTENCIA };
	private GeraNpAdvertencia geraNpAdvertencia;
	private GeraSegundaViaNpAdvertencia geraSegundaViaNpAdvertencia;
	private GeraViaUnicaNip geraViaUnicaNip;
	private GeraSegundaViaNicNip geraSegundaViaNicNip;
	private GeraSegundaViaNip geraSegundaViaNip;
	private GeraSegundaViaNipLote geraSegundaViaNipLote;
	private GeraSegundaViaNipEnvio geraSegundaViaNipEnvio;
	private TipoImpressaoNotificacao tipoImpressaoNotificacao;
	private GeraViaUnicaNicNip geraNicNipViaUnica;
	
	public ProcessadorImpressaoNip(int cdAit, TipoStatusEnum tpNotificacao, int cdUsuario) throws Exception {
		if(!Arrays.asList(tipoStatusPermitidos).contains(tpNotificacao)) {
			throw new Exception("A classe GeraTipoImpressaoNIP não implementa o status ("+ tpNotificacao.getKey() + ")");
		}
		
		this.geraNpAdvertencia = new GeraNpAdvertencia();
		this.geraSegundaViaNpAdvertencia = new GeraSegundaViaNpAdvertencia();
		this.geraNicNipViaUnica = new GeraViaUnicaNicNip();
		this.geraSegundaViaNicNip = new GeraSegundaViaNicNip();
		this.geraViaUnicaNip = new GeraViaUnicaNip();
		this.geraSegundaViaNip = new GeraSegundaViaNip();
		this.geraSegundaViaNipLote = new GeraSegundaViaNipLote();
		this.geraSegundaViaNipEnvio = new GeraSegundaViaNipEnvio();
		this.tipoImpressaoNotificacao = TipoImpressaoNotificacaoFactory.criarAdvertenciaNip(cdAit, tpNotificacao.getKey(), cdUsuario);
	}
	
	public NipImpressaoDTO gerar(CustomConnection customConnection) throws Exception {
		this.geraNpAdvertencia.setNextGenerator(this.geraSegundaViaNpAdvertencia);
		this.geraSegundaViaNpAdvertencia.setNextGenerator(geraNicNipViaUnica);
		this.geraNicNipViaUnica.setNextGenerator(geraSegundaViaNicNip);
		this.geraSegundaViaNicNip.setNextGenerator(this.geraViaUnicaNip);
		this.geraViaUnicaNip.setNextGenerator(this.geraSegundaViaNip);
		this.geraSegundaViaNip.setNextGenerator(this.geraSegundaViaNipLote);
		this.geraSegundaViaNipLote.setNextGenerator(this.geraSegundaViaNipEnvio);
		NipImpressaoDTO loteNotificacaoNIP = geraNpAdvertencia.gerar(tipoImpressaoNotificacao, customConnection);
		customConnection.finishConnection();
		return loteNotificacaoNIP;
	}

}
