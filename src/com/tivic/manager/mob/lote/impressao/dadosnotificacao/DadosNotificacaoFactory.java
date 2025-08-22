package com.tivic.manager.mob.lote.impressao.dadosnotificacao;

import com.tivic.manager.adapter.base.antiga.dadosnotificacao.DadosNotificacaoOldService;
import com.tivic.manager.conf.ManagerConf;

public class DadosNotificacaoFactory {
	boolean lgBaseAntiga;
	public DadosNotificacaoFactory() {
		this.lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
	}
	public IDadosNotificacaoService getStrategy() throws Exception {
		return this.lgBaseAntiga ? new DadosNotificacaoOldService() : new DadosNotificacaoService();
	}
}
