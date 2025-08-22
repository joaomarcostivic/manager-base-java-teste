package com.tivic.manager.mob.guiapagamento.np;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.banco.IBancoService;
import com.tivic.manager.grl.cidade.CidadeRepository;
import com.tivic.manager.grl.estado.EstadoRepository;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.EquipamentoEnum.EquipamentoEnum;
import com.tivic.manager.mob.lote.impressao.DadosNotificacao;
import com.tivic.manager.mob.lote.impressao.codigobarras.CodigoBarras;
import com.tivic.manager.mob.lote.impressao.codigobarras.GerarCodigoBarrasFactory;
import com.tivic.manager.mob.orgao.IOrgaoService;
import com.tivic.manager.util.ContextManager;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.util.date.DateUtil;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import sol.util.ConfManager;

public class GeraNpFicticia {
    private IOrgaoService orgaoService;
    private CidadeRepository cidadeRepository;
    private EstadoRepository estadoRepository;
    private IParametroRepository parametroRepository;
	private IBancoService bancoService;

    public GeraNpFicticia() throws Exception {
        this.orgaoService = (IOrgaoService) BeansFactory.get(IOrgaoService.class);
        this.cidadeRepository = (CidadeRepository) BeansFactory.get(CidadeRepository.class);
        this.estadoRepository = (EstadoRepository) BeansFactory.get(EstadoRepository.class);
        this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
		this.bancoService = (IBancoService) BeansFactory.get(IBancoService.class);
    }

    public byte[] gerarDocumento(CustomConnection customConnection) throws Exception {
        ReportCriterios criterios = montarReportCriterios(customConnection);
        List<DadosNotificacao> dadosFakes = gerarDadosFicticios(criterios);
        setParamsSubreport(dadosFakes, criterios);
        Report report = new ReportBuilder()
                .list(dadosFakes)
                .reportCriterios(criterios)
                .build();
        return report.getReportPdf("mob/np_ficticia");
    }
    
    private List<DadosNotificacao> gerarDadosFicticios(ReportCriterios criterios) throws Exception {
        List<DadosNotificacao> lista = new ArrayList<>();
        DadosNotificacao dadosNotificacao = criarDadosFicticios();
        dadosNotificacao.setBancosConveniados(bancoService.pegarBancoConveniado());
        CodigoBarras codigoBarras = new GerarCodigoBarrasFactory()
            .getStrategy()
            .gerarCodigoBarras(dadosNotificacao, criterios);
        dadosNotificacao.setLinhaDigitavel(codigoBarras.getLinhaDigitavel());
        criterios.addParametros("BARRAS", codigoBarras.getBarcodeImage());
        dadosNotificacao.setBarCodeImageCep(new CodigoBarrasGenerator().gerar(dadosNotificacao.getNrCep()));
        lista.add(dadosNotificacao);
        return lista;
    }

    private DadosNotificacao criarDadosFicticios() throws Exception {
        DadosNotificacao dados = new DadosNotificacao();
        dados.setIdAit("AIT123456");
        dados.setNrRenainf("REN123456");
        dados.setNrControle("012345678");
        dados.setDtInfracao(GregorianCalendar.from(LocalDate.now().minusMonths(2).atStartOfDay(ZoneId.systemDefault())));
        dados.setDtPrazoDefesa(GregorianCalendar.from(LocalDate.now().plusMonths(1).atStartOfDay(ZoneId.systemDefault())));
        dados.setDsObservacao("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        dados.setNrCpfCondutor("12345678900");
        dados.setDtAfericao(GregorianCalendar.from(LocalDate.now().minusMonths(2).atStartOfDay(ZoneId.systemDefault())));
        dados.setNrLacre("LACRE123");
        dados.setNrPlaca("ABC1D23");
        dados.setSgUfVeiculo("SP");
        dados.setNrRenavan("98765432100");
        dados.setNmProprietario("JOÃO DA SILVA TESTE");
        dados.setNrCpfCnpjProprietario("98765432100");
        dados.setNmCondutor("MARIA DA SILVA TESTE");
        dados.setNrCnhCondutor("12345678901");
        dados.setUfCnhCondutor("SP");
        dados.setDsLocalInfracao("Av. Exemplo, 100 - Centro");
        dados.setDsPontoReferencia("PRÓXIMO AO POSTO");
        dados.setVlVelocidadePermitida(60.0);
        dados.setVlVelocidadeAferida(75.0);
        dados.setVlVelocidadePenalidade(72.0);
        dados.setDsLogradouro("Av. Exemplo");
        dados.setDsNrImovel("100");
        dados.setNrCep("12345678");
        dados.setDtVencimento(GregorianCalendar.from(LocalDate.now().plusMonths(1).atStartOfDay(ZoneId.systemDefault())));
        dados.setNrCodDetran(55411);
        dados.setDsInfracao("Excesso de velocidade");
        dados.setNrArtigo("218");
        dados.setNrInciso("I");
        dados.setNrParagrafo("§1");
        dados.setNrAlinea("a");
        dados.setNmNatureza("Gravíssima");
        dados.setNrPontuacao(7);
        dados.setNmMunicipio("São Paulo");
        dados.setSgUfVeiculo("SP");
        dados.setNmUf("SP");
        dados.setDsEspecie("PASSEIO");
        dados.setNmModelo("ARGO");
        dados.setIdEquipamento("EQ123");
        dados.setMarcaEquipamento("MarcaX");
        dados.setModeloEquipamento("ModeloY");
        dados.setNomeEquipamento("RADAR-00123456");
        dados.setTpEquipamento(EquipamentoEnum.RADAR_FIXO.getKey());
        dados.setCdEquipamento(1);
        dados.setNrMatricula("MAT123");
        dados.setDtMovimento(GregorianCalendar.from(LocalDate.now().minusMonths(1).atStartOfDay(ZoneId.systemDefault())));
        dados.setTpStatus(5);
        dados.setNrInventarioInmetro("INM2024");
        dados.setDsLogradouro("RUA PRINCIPAL");
        dados.setNmBairro("CENTRO");
        dados.setCidadeProprietario("CAMPINAS");
        dados.setUfProprietario("SP");
        dados.setEstadoProprietario("SÃO PAULO");
        dados.setVlMulta(1.0);
        dados.setVlMultaComDesconto(1.0);
        dados.setDtEmissao(DateUtil.getDataAtual());
        return dados;
    }

    private ReportCriterios montarReportCriterios(CustomConnection customConnection) throws Exception {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("LOGO_1", this.parametroRepository.recImageToString("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA", customConnection));
		reportCriterios.addParametros("LOGO_2", this.parametroRepository.recImageToString("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO", customConnection));
		reportCriterios.addParametros("DS_TITULO_1", this.parametroRepository.getValorOfParametroAsString("DS_TITULO_1", customConnection));
		reportCriterios.addParametros("DS_TITULO_2", this.parametroRepository.getValorOfParametroAsString("DS_TITULO_2", customConnection));
		reportCriterios.addParametros("DS_TITULO_3", this.parametroRepository.getValorOfParametroAsString("DS_TITULO_3", customConnection));
		reportCriterios.addParametros("MOB_IMPRESSOS_DS_TEXTO_NIP", this.parametroRepository.getValorOfParametroAsString("MOB_IMPRESSOS_DS_TEXTO_NIP", customConnection));
		reportCriterios.addParametros("MOB_CD_ORGAO_AUTUADOR", this.parametroRepository.getValorOfParametroAsString("MOB_CD_ORGAO_AUTUADOR", customConnection));
		reportCriterios.addParametros("NM_CIDADE", this.parametroRepository.getValorOfParametroAsString("NM_CIDADE", customConnection));
		reportCriterios.addParametros("NR_BANCO_CREDITO", this.parametroRepository.getValorOfParametroAsString("NR_BANCO_CREDITO", customConnection));
		reportCriterios.addParametros("NR_AGENCIA_CREDITO", this.parametroRepository.getValorOfParametroAsString("NR_AGENCIA_CREDITO", customConnection));
		reportCriterios.addParametros("NR_CONTA_CREDITO", this.parametroRepository.getValorOfParametroAsString("NR_CONTA_CREDITO", customConnection));
		reportCriterios.addParametros("MOB_CORREIOS_NR_CONTRATO", this.parametroRepository.getValorOfParametroAsString("MOB_CORREIOS_NR_CONTRATO", customConnection));
		reportCriterios.addParametros("SG_ORGAO", this.parametroRepository.getValorOfParametroAsString("SG_ORGAO", customConnection));
		reportCriterios.addParametros("SG_DEPARTAMENTO", this.parametroRepository.getValorOfParametroAsString("SG_DEPARTAMENTO", customConnection));
		reportCriterios.addParametros("NM_LOGRADOURO", this.parametroRepository.getValorOfParametroAsString("NM_LOGRADOURO", customConnection));
		reportCriterios.addParametros("NR_ENDERECO", this.parametroRepository.getValorOfParametroAsString("NR_ENDERECO", customConnection));
		reportCriterios.addParametros("NM_BAIRRO", this.parametroRepository.getValorOfParametroAsString("NM_BAIRRO", customConnection));
		reportCriterios.addParametros("NR_CEP", this.parametroRepository.getValorOfParametroAsString("NR_CEP", customConnection).replaceAll("[^\\d]", ""));
		reportCriterios.addParametros("NR_TELEFONE", this.parametroRepository.getValorOfParametroAsString("NR_TELEFONE", customConnection));
		reportCriterios.addParametros("NR_TELEFONE_2", this.parametroRepository.getValorOfParametroAsString("NR_TELEFONE2", customConnection));
		reportCriterios.addParametros("MOB_BLB_MENSAGEM_EDUCATIVA_NAI", this.parametroRepository.getValorOfParametroAsString("MOB_BLB_MENSAGEM_EDUCATIVA_NAI", customConnection));
		reportCriterios.addParametros("MOB_PRAZOS_NR_RECURSO_JARI", this.parametroRepository.getValorOfParametroAsString("MOB_PRAZOS_NR_RECURSO_JARI", customConnection));
		reportCriterios.addParametros("MOB_BLB_DEFESA_PREVIA_INDEFERIDA", this.parametroRepository.getValorOfParametroAsString("MOB_BLB_DEFESA_PREVIA_INDEFERIDA", customConnection));
		reportCriterios.addParametros("MOB_NOME_COORDENADOR_IMPRESSAO", this.parametroRepository.getValorOfParametroAsString("MOB_NOME_COORDENADOR_IMPRESSAO", customConnection));
		reportCriterios.addParametros("NR_CD_FEBRABAN", this.parametroRepository.getValorOfParametroAsString("NR_CD_FEBRABAN", customConnection));
		reportCriterios.addParametros("MOB_PRAZOS_NR_DEFESA_PREVIA", this.parametroRepository.getValorOfParametroAsString("MOB_PRAZOS_NR_DEFESA_PREVIA", customConnection));
		reportCriterios.addParametros("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS", this.parametroRepository.recImageToString("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS", customConnection));
		reportCriterios.addParametros("mob_local_impressao",this.parametroRepository.getValorOfParametroAsString("mob_local_impressao", customConnection));
		reportCriterios.addParametros("NM_COMPLEMENTO", this.parametroRepository.getValorOfParametroAsString("NM_COMPLEMENTO", customConnection));
		reportCriterios.addParametros("MOB_INFORMACOES_ADICIONAIS_NIP", this.parametroRepository.getValorOfParametroAsString("MOB_INFORMACOES_ADICIONAIS_NIP", customConnection));
		reportCriterios.addParametros("NM_EMAIL", this.parametroRepository.getValorOfParametroAsString("NM_EMAIL", customConnection));
		reportCriterios.addParametros("REPORT_LOCALE", new Locale("pt", "BR"));
		reportCriterios.addParametros("SG_UF", getEstado(customConnection));
		reportCriterios.addParametros("TP_PENALIDADE", TipoStatusEnum.NIP_ENVIADA.getKey());
		return reportCriterios;
	}
    
    private void setParamsSubreport(List<DadosNotificacao> dadosSubreport, ReportCriterios parametros) throws Exception {
        ConfManager conf = ManagerConf.getInstance();
        String reportPath = conf.getProps().getProperty("REPORT_PATH");
        String path = ContextManager.getRealPath() + "/" + reportPath;
        parametros.addParametros("SUBREPORT_DIR", path + "//mob");
        parametros.addParametros("REPORT_LOCALE", new Locale("pt", "BR"));
        parametros.addParametros("SUBREPORT_NAMES", "mob//verso_notificacao_carta_simples_subreport");
        parametros.addParametros("SUBREPORT_DADOS_FIELDS", new JRBeanCollectionDataSource(dadosSubreport));
        parametros.addParametros("SUBREPORT_MAP_PARAMETERS", parametros.getParametros());
    }
    
    private String getEstado(CustomConnection customConnection) throws Exception {
        Orgao orgao = this.orgaoService.getOrgaoUnico();
        Cidade cidade = this.cidadeRepository.get(orgao.getCdCidade(), customConnection);
        return this.estadoRepository.get(cidade.getCdEstado(), customConnection).getSgEstado();
    }
}