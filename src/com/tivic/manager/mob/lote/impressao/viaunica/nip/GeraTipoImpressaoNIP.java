package com.tivic.manager.mob.lote.impressao.viaunica.nip;

import java.util.Arrays;

import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.lote.impressao.viaunica.TipoImpressaoNotificacao;
import com.tivic.manager.mob.lote.impressao.viaunica.builders.TipoImpressaoNotificacaoAdvertenciaNipBuilder;
import com.tivic.manager.mob.lote.impressao.viaunica.nic.GeraSegundaViaNicNip;
import com.tivic.sol.connection.CustomConnection;

public class GeraTipoImpressaoNIP {
	private TipoStatusEnum[] tipoStatusPermitidos = { TipoStatusEnum.NIP_ENVIADA, TipoStatusEnum.NOTIFICACAO_ADVERTENCIA };
	private GeraAdvertencia geraAdvertencia;
	private GeraSegundaViaAdvertencia geraSegundaViaAdvertencia;
	private GeraNIPViaUnica geraNipViaUnica;
	private GeraSegundaViaNicNip geraSegundaViaNicNip;
	private GeraSegundaViaNIP geraSegundaViaNIP;
	private GeraSegundaViaNIPLote geraSegundaViaNIPLote;
	private GeraSegundaViaNIPEnvio geraSegundaViaNIPEnvio;
	private TipoImpressaoNotificacao tipoImpressaoNotificacao;
	private GeraNicNipViaUnica geraNicNipViaUnica;
	
	public GeraTipoImpressaoNIP(int cdAit, TipoStatusEnum tpNotificacao, int cdUsuario) throws Exception {
		if(!Arrays.asList(tipoStatusPermitidos).contains(tpNotificacao)) {
			throw new Exception("A classe GeraTipoImpressaoNIP não implementa o status ("+tpNotificacao.getKey()+")");
		}
		
		this.geraAdvertencia = new GeraAdvertencia();
		this.geraSegundaViaAdvertencia = new GeraSegundaViaAdvertencia();
		this.geraNicNipViaUnica = new GeraNicNipViaUnica();
		this.geraSegundaViaNicNip = new GeraSegundaViaNicNip();
		this.geraNipViaUnica = new GeraNIPViaUnica();
		this.geraSegundaViaNIP = new GeraSegundaViaNIP();
		this.geraSegundaViaNIPLote = new GeraSegundaViaNIPLote();
		this.geraSegundaViaNIPEnvio = new GeraSegundaViaNIPEnvio();
		this.tipoImpressaoNotificacao = new TipoImpressaoNotificacaoAdvertenciaNipBuilder(cdAit, tpNotificacao.getKey(), cdUsuario).build();
	}

	public NipImpressaoDTO gerar(CustomConnection customConnection) throws Exception {
	    this.geraAdvertencia.setNextGenerator(this.geraSegundaViaAdvertencia);
	    this.geraSegundaViaAdvertencia.setNextGenerator(this.geraNicNipViaUnica);
	    this.geraNicNipViaUnica.setNextGenerator(this.geraSegundaViaNicNip);
	    this.geraSegundaViaNicNip.setNextGenerator(this.geraNipViaUnica);
	    this.geraNipViaUnica.setNextGenerator(this.geraSegundaViaNIP);
	    this.geraSegundaViaNIP.setNextGenerator(this.geraSegundaViaNIPLote);
	    this.geraSegundaViaNIPLote.setNextGenerator(this.geraSegundaViaNIPEnvio);
	    NipImpressaoDTO loteNotificacaoNIP = geraAdvertencia.gerar(tipoImpressaoNotificacao, customConnection);
	    customConnection.finishConnection();
	    return loteNotificacaoNIP;	
	}

}
