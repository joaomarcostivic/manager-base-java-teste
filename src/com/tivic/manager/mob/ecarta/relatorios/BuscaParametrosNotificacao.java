package com.tivic.manager.mob.ecarta.relatorios;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.cidade.CidadeRepository;
import com.tivic.manager.grl.estado.EstadoRepository;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.colaborador.IColaboradorService;
import com.tivic.manager.mob.orgao.IOrgaoService;
import com.tivic.manager.util.richtext.ConversorRichText;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.report.ReportCriterios;

public class BuscaParametrosNotificacao {
	private CidadeRepository cidadeRepository;
	private EstadoRepository estadoRepository;
	private IOrgaoService orgaoService;
	private boolean lgBaseAntiga; 
	private IParametroRepository parametroRepository;
	private IColaboradorService colaboradorService;
	
	public BuscaParametrosNotificacao() throws Exception {
		this.cidadeRepository = (CidadeRepository) BeansFactory.get(CidadeRepository.class);
		this.estadoRepository = (EstadoRepository) BeansFactory.get(EstadoRepository.class);
		this.orgaoService = (IOrgaoService) BeansFactory.get(IOrgaoService.class);
		this.lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");	
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
		this.colaboradorService = (IColaboradorService) BeansFactory.get(IColaboradorService.class);
	}

	public ReportCriterios buscar(CustomConnection customConnection) throws Exception {
		return lgBaseAntiga ? popularParametrosBaseAntiga(customConnection) : popularParametrosBaseNova(customConnection);
	}
	
	private ReportCriterios popularParametrosBaseNova(CustomConnection customConnection) throws Exception {
		try {
			ReportCriterios reportCriterios = new ReportCriterios();
			reportCriterios.addParametros("IS_BASE_OLD", false);
			reportCriterios.addParametros("DS_TITULO_1", this.parametroRepository.getValorOfParametroAsString("DS_TITULO_1", customConnection));
			reportCriterios.addParametros("DS_TITULO_2", this.parametroRepository.getValorOfParametroAsString("DS_TITULO_2", customConnection));
			reportCriterios.addParametros("DS_TITULO_3", this.parametroRepository.getValorOfParametroAsString("DS_TITULO_3", customConnection));
			reportCriterios.addParametros("NR_TELEFONE", this.parametroRepository.getValorOfParametroAsString("NR_TELEFONE", customConnection));
			reportCriterios.addParametros("NM_EMAIL", this.parametroRepository.getValorOfParametroAsString("NM_EMAIL", customConnection));
			reportCriterios.addParametros("LOGO_1", this.parametroRepository.recImageToString("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA", customConnection));
			reportCriterios.addParametros("LOGO_2", this.parametroRepository.recImageToString("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO"));
			reportCriterios.addParametros("NM_CIDADE", this.parametroRepository.getValorOfParametroAsString("NM_CIDADE", customConnection));
			reportCriterios.addParametros("MOB_CD_ORGAO_AUTUADOR", this.parametroRepository.getValorOfParametroAsString("MOB_CD_ORGAO_AUTUADOR", customConnection));
			reportCriterios.addParametros("MOB_IMPRESSOS_DS_TEXTO_NAI", this.parametroRepository.getValorOfParametroAsString("MOB_IMPRESSOS_DS_TEXTO_NAI", customConnection));
			reportCriterios.addParametros("MOB_BLB_MENSAGEM_EDUCATIVA_NAI", this.parametroRepository.getValorOfParametroAsString("MOB_BLB_MENSAGEM_EDUCATIVA_NAI", customConnection));
			reportCriterios.addParametros("NM_LOGRADOURO", this.parametroRepository.getValorOfParametroAsString("NM_LOGRADOURO", customConnection));
			reportCriterios.addParametros("NR_ENDERECO", this.parametroRepository.getValorOfParametroAsString("NR_ENDERECO", customConnection));
			reportCriterios.addParametros("NM_BAIRRO", this.parametroRepository.getValorOfParametroAsString("NM_BAIRRO", customConnection));
			reportCriterios.addParametros("NR_CEP", this.parametroRepository.getValorOfParametroAsString("NR_CEP", customConnection).replaceAll("[^\\d]", ""));
			reportCriterios.addParametros("MOB_CORREIOS_NR_CONTRATO", this.parametroRepository.getValorOfParametroAsString("MOB_CORREIOS_NR_CONTRATO", customConnection));
			reportCriterios.addParametros("SG_ORGAO", this.parametroRepository.getValorOfParametroAsString("SG_ORGAO", customConnection));
			reportCriterios.addParametros("SG_DEPARTAMENTO", this.parametroRepository.getValorOfParametroAsString("SG_DEPARTAMENTO", customConnection));
			reportCriterios.addParametros("MOB_PRAZOS_NR_DEFESA_PREVIA", this.parametroRepository.getValorOfParametroAsString("MOB_PRAZOS_NR_DEFESA_PREVIA", customConnection));
			reportCriterios.addParametros("MOB_BLB_DEFESA_PREVIA_INDEFERIDA", this.parametroRepository.getValorOfParametroAsString("MOB_BLB_DEFESA_PREVIA_INDEFERIDA", customConnection));
			reportCriterios.addParametros("DS_ENDERECO", this.parametroRepository.getValorOfParametroAsString("DS_ENDERECO", customConnection));
			reportCriterios.addParametros("NM_SUBORGAO", this.parametroRepository.getValorOfParametroAsString("NM_SUBORGAO", customConnection));
			reportCriterios.addParametros("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS", this.parametroRepository.getValorOfParametroAsByte("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS", customConnection));
			reportCriterios.addParametros("MOB_APRESENTACAO_CONDUTOR", this.parametroRepository.getValorOfParametroAsString("MOB_APRESENTACAO_CONDUTOR", customConnection));
			reportCriterios.addParametros("NM_COMPLEMENTO", this.parametroRepository.getValorOfParametroAsString("NM_COMPLEMENTO", customConnection));
			reportCriterios.addParametros("mob_apresentar_observacao", this.parametroRepository.getValorOfParametroAsString("mob_apresentar_observacao", customConnection));
			reportCriterios.addParametros("MOB_INFORMACOES_ADICIONAIS_NAI", this.parametroRepository.getValorOfParametroAsString("MOB_INFORMACOES_ADICIONAIS_NAI", customConnection));
			reportCriterios.addParametros("REPORT_LOCALE", new Locale("pt", "BR"));
			reportCriterios.addParametros("SG_UF", getEstado());
			reportCriterios.addParametros("E_CARTAS", true);
			reportCriterios.addParametros("NR_CD_FEBRABAN", this.parametroRepository.getValorOfParametroAsInt(("NR_CD_FEBRABAN")));
			reportCriterios.addParametros("NR_BANCO_CREDITO", this.parametroRepository.getValorOfParametroAsString("NR_BANCO_CREDITO", customConnection));
	        reportCriterios.addParametros("NR_AGENCIA_CREDITO", this.parametroRepository.getValorOfParametroAsString("NR_AGENCIA_CREDITO", customConnection));
	        reportCriterios.addParametros("NR_CONTA_CREDITO", this.parametroRepository.getValorOfParametroAsString("NR_CONTA_CREDITO", customConnection));
			return reportCriterios;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private ReportCriterios popularParametrosBaseAntiga(CustomConnection customConnection) throws Exception {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("IS_BASE_OLD", true);
		reportCriterios.addParametros("DS_TITULO_1", this.parametroRepository.getValorOfParametroAsString("DS_TITULO_1", customConnection));
		reportCriterios.addParametros("DS_TITULO_2", this.parametroRepository.getValorOfParametroAsString("DS_TITULO_2", customConnection));
		reportCriterios.addParametros("DS_TITULO_3", this.parametroRepository.getValorOfParametroAsString("DS_TITULO_3", customConnection));
		reportCriterios.addParametros("NR_TELEFONE", this.parametroRepository.getValorOfParametroAsString("NR_TELEFONE", customConnection));
		reportCriterios.addParametros("NM_EMAIL", this.parametroRepository.getValorOfParametroAsString("NM_EMAIL", customConnection));
		reportCriterios.addParametros("LOGO_1", this.parametroRepository.byteasImgBuffer("img_logo_orgao"));
		reportCriterios.addParametros("LOGO_2", this.parametroRepository.byteasImgBuffer("img_logo_departamento"));
		reportCriterios.addParametros("NM_CIDADE", this.parametroRepository.getValorOfParametroAsString("nm_municipio", customConnection));			
		reportCriterios.addParametros("MOB_CD_ORGAO_AUTUADOR", String.valueOf(this.parametroRepository.getValorOfParametroAsInt("cd_orgao_autuante", customConnection)));
		reportCriterios.addParametros("MOB_IMPRESSOS_DS_TEXTO_NAI", this.parametroRepository.getValorOfParametroAsString("txt_nai", customConnection));
		reportCriterios.addParametros("MOB_BLB_MENSAGEM_EDUCATIVA_NAI", new ConversorRichText().convert(new String(this.parametroRepository.getValorOfParametroAsByte("ds_mensagem_nai", customConnection), StandardCharsets.ISO_8859_1)));	
		reportCriterios.addParametros("NM_LOGRADOURO", this.parametroRepository.getValorOfParametroAsString("NM_LOGRADOURO", customConnection));
		reportCriterios.addParametros("NR_ENDERECO", this.parametroRepository.getValorOfParametroAsString("NR_ENDERECO", customConnection));
		reportCriterios.addParametros("NM_BAIRRO", this.parametroRepository.getValorOfParametroAsString("NM_BAIRRO", customConnection));
		reportCriterios.addParametros("NR_CEP", this.parametroRepository.getValorOfParametroAsString("NR_CEP", customConnection));
		reportCriterios.addParametros("MOB_CORREIOS_NR_CONTRATO", this.parametroRepository.getValorOfParametroAsString("nr_correios", customConnection));
		reportCriterios.addParametros("SG_ORGAO", this.parametroRepository.getValorOfParametroAsString("SG_ORGAO", customConnection));
		reportCriterios.addParametros("SG_DEPARTAMENTO", this.parametroRepository.getValorOfParametroAsString("SG_DEPARTAMENTO", customConnection));		
		reportCriterios.addParametros("MOB_PRAZOS_NR_DEFESA_PREVIA", this.parametroRepository.getValorOfParametroAsString("nr_dias_defesa_previa", customConnection));
		reportCriterios.addParametros("MOB_BLB_DEFESA_PREVIA_INDEFERIDA", new ConversorRichText().convert(new String(this.parametroRepository.getValorOfParametroAsByte("ds_mensagem_indeferido", customConnection), StandardCharsets.ISO_8859_1)));			
		reportCriterios.addParametros("NM_SUBORGAO", this.parametroRepository.getValorOfParametroAsString("NM_SUBORGAO", customConnection));
		reportCriterios.addParametros("MOB_APRESENTACAO_CONDUTOR", this.parametroRepository.getValorOfParametroAsString("MOB_APRESENTACAO_CONDUTOR", customConnection));	
		reportCriterios.addParametros("NM_COMPLEMENTO", this.parametroRepository.getValorOfParametroAsString("NM_COMPLEMENTO", customConnection));
		reportCriterios.addParametros("mob_apresentar_observacao", this.parametroRepository.getValorOfParametroAsString("mob_apresentar_observacao", customConnection));
		reportCriterios.addParametros("MOB_INFORMACOES_ADICIONAIS_NAI", this.parametroRepository.getValorOfParametroAsString("MOB_INFORMACOES_ADICIONAIS_NAI", customConnection));
		reportCriterios.addParametros("REPORT_LOCALE", new Locale("pt", "BR"));
		reportCriterios.addParametros("SG_UF", getEstado());
		reportCriterios.addParametros("E_CARTAS", true);
		reportCriterios.addParametros("MOB_NOME_COORDENADOR_IMPRESSAO", colaboradorService.buscaNomeAutoridadeTransito());
		reportCriterios.addParametros("NR_CD_FEBRABAN", this.parametroRepository.getValorOfParametroAsInt(("NR_CD_FEBRABAN")));
		reportCriterios.addParametros("NR_BANCO_CREDITO", this.parametroRepository.getValorOfParametroAsString("NR_BANCO_CREDITO", customConnection));
        reportCriterios.addParametros("NR_AGENCIA_CREDITO", this.parametroRepository.getValorOfParametroAsString("NR_AGENCIA_CREDITO", customConnection));
        reportCriterios.addParametros("NR_CONTA_CREDITO", this.parametroRepository.getValorOfParametroAsString("NR_CONTA_CREDITO", customConnection));
		return reportCriterios;
	}
	
	public String getEstado() throws Exception {
		Orgao orgao = this.orgaoService.getOrgaoUnico();
		Cidade cidade = this.cidadeRepository.get(orgao.getCdCidade());
		return estadoRepository.get(cidade.getCdEstado()).getSgEstado();
	}
	
}
