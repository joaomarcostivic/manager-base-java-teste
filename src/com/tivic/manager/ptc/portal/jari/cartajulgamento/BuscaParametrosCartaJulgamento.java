package com.tivic.manager.ptc.portal.jari.cartajulgamento;

import java.util.Locale;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.cidade.CidadeRepository;
import com.tivic.manager.grl.estado.EstadoRepository;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.orgao.IOrgaoService;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.report.ReportCriterios;

public class BuscaParametrosCartaJulgamento {
	private CidadeRepository cidadeRepository;
	private EstadoRepository estadoRepository;
	private IOrgaoService orgaoService;
	private boolean lgBaseAntiga; 
	private IParametroRepository parametroRepository;
	
	public BuscaParametrosCartaJulgamento() throws Exception {
		this.cidadeRepository = (CidadeRepository) BeansFactory.get(CidadeRepository.class);
		this.estadoRepository = (EstadoRepository) BeansFactory.get(EstadoRepository.class);
		this.orgaoService = (IOrgaoService) BeansFactory.get(IOrgaoService.class);
		this.lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");	
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
	}

	public ReportCriterios buscar(CustomConnection customConnection) throws Exception {
		return popularParametros(customConnection);
	}
	
	private ReportCriterios popularParametros(CustomConnection customConnection) throws Exception {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("IS_BASE_OLD", lgBaseAntiga);
		reportCriterios.addParametros("DS_TITULO_1", this.parametroRepository.getValorOfParametroAsString("DS_TITULO_1"));
		reportCriterios.addParametros("DS_TITULO_2", this.parametroRepository.getValorOfParametroAsString("DS_TITULO_2"));
		reportCriterios.addParametros("DS_TITULO_3", this.parametroRepository.getValorOfParametroAsString("DS_TITULO_3"));
		reportCriterios.addParametros("LOGO_1", this.parametroRepository.byteasImgBuffer("img_logo_orgao"));
		reportCriterios.addParametros("LOGO_2", this.parametroRepository.byteasImgBuffer("img_logo_departamento"));
		reportCriterios.addParametros("NR_TELEFONE", this.parametroRepository.getValorOfParametroAsString("NR_TELEFONE"));
		reportCriterios.addParametros("NM_EMAIL", this.parametroRepository.getValorOfParametroAsString("NM_EMAIL"));
		reportCriterios.addParametros("NM_CIDADE", this.parametroRepository.getValorOfParametroAsString("nm_municipio"));
		reportCriterios.addParametros("NM_LOGRADOURO", this.parametroRepository.getValorOfParametroAsString("NM_LOGRADOURO"));
		reportCriterios.addParametros("NR_ENDERECO", this.parametroRepository.getValorOfParametroAsString("NR_ENDERECO"));
		reportCriterios.addParametros("NM_BAIRRO", this.parametroRepository.getValorOfParametroAsString("NM_BAIRRO"));
		reportCriterios.addParametros("NR_CEP", this.parametroRepository.getValorOfParametroAsString("NR_CEP").replaceAll("[^\\d]", ""));
		reportCriterios.addParametros("SG_ORGAO", this.parametroRepository.getValorOfParametroAsString("SG_ORGAO"));
		reportCriterios.addParametros("MOB_CORREIOS_NR_CONTRATO", this.parametroRepository.getValorOfParametroAsString("nr_correios"));
		reportCriterios.addParametros("SG_DEPARTAMENTO", this.parametroRepository.getValorOfParametroAsString("SG_DEPARTAMENTO"));
		reportCriterios.addParametros("NM_COMPLEMENTO", this.parametroRepository.getValorOfParametroAsString("NM_COMPLEMENTO"));
		reportCriterios.addParametros("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS", this.parametroRepository.recImageToString("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS"));
		reportCriterios.addParametros("DS_SUBTITULO", "INDEFERIMENTO DO RECURSO - 1ª INSTÂNCIA - JARI");
		reportCriterios.addParametros("REPORT_LOCALE", new Locale("pt", "BR"));
		reportCriterios.addParametros("SG_UF", getEstado());
		return reportCriterios;
	}
	
	public String getEstado() throws Exception {
		Orgao orgao = this.orgaoService.getOrgaoUnico();
		Cidade cidade = this.cidadeRepository.get(orgao.getCdCidade());
		return estadoRepository.get(cidade.getCdEstado()).getSgEstado();
	}
	
}
