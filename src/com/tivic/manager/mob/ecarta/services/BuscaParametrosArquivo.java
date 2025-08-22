package com.tivic.manager.mob.ecarta.services;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.report.ReportCriterios;

public class BuscaParametrosArquivo {
	private boolean lgBaseAntiga;
	private IParametroRepository parametroRepository;

	public BuscaParametrosArquivo() throws Exception {
		this.lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
	}

	public ReportCriterios buscar(CustomConnection customConnection) throws Exception {
		return lgBaseAntiga ? popularParametrosBaseAntiga(customConnection)
				: popularParametrosBaseNova(customConnection);
	}
	
	private ReportCriterios popularParametrosBaseAntiga(CustomConnection customConnection) throws Exception {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("IS_BASE_OLD", true);
		reportCriterios.addParametros("MOB_CORREIOS_NR_CARTAO_POSTAGEM",
				this.parametroRepository.getValorOfParametroAsString("ECT_NR_CARTAO_POSTAGEM", customConnection));
		reportCriterios.addParametros("MOB_CORREIOS_NR_CONTRATO",
				this.parametroRepository.getValorOfParametroAsString("ECT_NR_CORREIOS", customConnection));
		reportCriterios.addParametros("MOB_CORREIOS_SG_CLIENTE",
				this.parametroRepository.getValorOfParametroAsString("ECT_SG_CORREIOS", customConnection));
		reportCriterios.addParametros("MOB_CORREIOS_MATRIZ_NAI",
				this.parametroRepository.getValorOfParametroAsString("mob_correios_matriz_nai", customConnection));
		reportCriterios.addParametros("MOB_CORREIOS_MATRIZ_NIP",
				this.parametroRepository.getValorOfParametroAsString("mob_correios_matriz_nip", customConnection));
		reportCriterios.addParametros("MOB_ECARTA_ENVIO",
				this.parametroRepository.getValorOfParametroAsString("mob_ecarta_envio", customConnection));
		reportCriterios.addParametros("mob_impressao_tp_modelo_nai",
				this.parametroRepository.getValorOfParametroAsString("tp_nai", customConnection));
		reportCriterios.addParametros("mob_impressao_tp_modelo_nip",
				this.parametroRepository.getValorOfParametroAsString("tp_nip", customConnection));
		return reportCriterios;
	}

	private ReportCriterios popularParametrosBaseNova(CustomConnection customConnection) throws Exception {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("IS_BASE_OLD", false);
		reportCriterios.addParametros("MOB_CORREIOS_NR_CARTAO_POSTAGEM", this.parametroRepository
				.getValorOfParametroAsString("MOB_CORREIOS_NR_CARTAO_POSTAGEM", customConnection));
		reportCriterios.addParametros("MOB_CORREIOS_NR_CONTRATO",
				this.parametroRepository.getValorOfParametroAsString("MOB_CORREIOS_NR_CONTRATO", customConnection));
		reportCriterios.addParametros("MOB_CORREIOS_SG_CLIENTE",
				this.parametroRepository.getValorOfParametroAsString("MOB_CORREIOS_SG_CLIENTE", customConnection));
		reportCriterios.addParametros("MOB_CORREIOS_MATRIZ_NAI",
				this.parametroRepository.getValorOfParametroAsString("MOB_CORREIOS_MATRIZ_NAI", customConnection));
		reportCriterios.addParametros("MOB_CORREIOS_MATRIZ_NIP",
				this.parametroRepository.getValorOfParametroAsString("MOB_CORREIOS_MATRIZ_NIP", customConnection));
		reportCriterios.addParametros("MOB_ECARTA_ENVIO",
				this.parametroRepository.getValorOfParametroAsString("MOB_ECARTA_ENVIO", customConnection));
		reportCriterios.addParametros("mob_impressao_tp_modelo_nai", this.parametroRepository
				.getValorOfParametroAsString("mob_impressao_tp_modelo_nai", customConnection));
		reportCriterios.addParametros("mob_impressao_tp_modelo_nip", this.parametroRepository
				.getValorOfParametroAsString("mob_impressao_tp_modelo_nip", customConnection));
		return reportCriterios;
	}
}
