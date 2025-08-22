package com.tivic.manager.mob.lotes.impressao.ait;

import java.awt.Image;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.AitImagem;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitReportServicesNip;
import com.tivic.manager.mob.AitReportValidatorsNIP;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.EquipamentoEnum.EquipamentoEnum;
import com.tivic.manager.mob.aitimagem.IAitImagemService;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.mob.colaborador.IColaboradorService;
import com.tivic.manager.mob.lotes.builders.impressao.viaunica.ReportDadosNotificacaoBuilder;
import com.tivic.manager.mob.lotes.dto.impressao.Notificacao;
import com.tivic.manager.mob.lotes.enums.correios.TipoRemessaCorreiosEnum;
import com.tivic.manager.mob.lotes.impressao.pix.AutenticacaoUsuarioPix;
import com.tivic.manager.mob.lotes.impressao.pix.DadosAutenticacaoEnvio;
import com.tivic.manager.mob.lotes.impressao.pix.DadosAutenticacaoRetorno;
import com.tivic.manager.mob.lotes.impressao.pix.ImagemQrCodePixGenerator;
import com.tivic.manager.mob.lotes.impressao.pix.builders.DadosAutenticacaoEnvioBuilder;
import com.tivic.manager.mob.lotes.impressao.strategy.NomeTipoImpressaoNipStrategy;
import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

public abstract class DadosNotificacaoComJurosBase {
	
	private AitReportServicesNip aitReportServicesNip;

	private IParametroRepository parametroRepository;
	private CustomConnection customConnection;
	private IAitMovimentoService aitMovimentoService;
	private Image imgMulta;
	private AitImagem aitImagem;
	private IAitImagemService aitImagemService;
	private IColaboradorService colaboradorService;
	
	protected abstract String getReportPath();
	protected abstract Search<Notificacao> searchPenalidade(SearchCriterios searchCriterios) throws Exception;
	
	public DadosNotificacaoComJurosBase() throws Exception {
		this.aitReportServicesNip = (AitReportServicesNip) BeansFactory.get(AitReportServicesNip.class);
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
		aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		this.aitImagemService = (IAitImagemService) BeansFactory.get(IAitImagemService.class);
		this.colaboradorService = (IColaboradorService) BeansFactory.get(IColaboradorService.class);
	}

	public byte[] gerarDocumento(int cdAit, Boolean printPortal, CustomConnection customConnection) throws Exception, ValidacaoException {
		this.customConnection = customConnection;
		ReportDadosNotificacaoBuilder reportBuilder = new ReportDadosNotificacaoBuilder(customConnection);
	    reportBuilder.addParameter("PRINT_PORTAL", printPortal);
	    ReportCriterios reportCriterios = reportBuilder.getParametros();
	    adicionarParametrosEspecificosNip(reportBuilder);
		return findDocumentoNip(cdAit, reportCriterios, reportBuilder);
	}
	
	private void adicionarParametrosEspecificosNip(ReportDadosNotificacaoBuilder reportBuilder) throws Exception {
	    List<String> criterios = Arrays.asList(
			"MOB_IMPRESSOS_DS_TEXTO_NIP",
	        "NR_BANCO_CREDITO",
	        "NR_AGENCIA_CREDITO",
	        "NR_CONTA_CREDITO",
	        "MOB_PRAZOS_NR_RECURSO_JARI",
	        "MOB_NOME_COORDENADOR_IMPRESSAO",
	        "MOB_INFORMACOES_ADICIONAIS_NIP",
	        "NR_CD_FEBRABAN",
	        "MOB_CD_ORGAO_AUTUADOR"
	    );
	    reportBuilder.addParameters(criterios, customConnection);
	}

	private byte[] findDocumentoNip(int cdAit, ReportCriterios reportCriterios, ReportDadosNotificacaoBuilder reportBuilder) throws Exception {
		SearchCriterios searchCriterios = montarCriterios(cdAit);
	    Search<Notificacao> searchNip = searchPenalidade(searchCriterios);
	    List<Notificacao> listaNotificacoes = searchNip.getList(Notificacao.class);
	    Notificacao notificacao = listaNotificacoes.get(0);
		montarDadosDocumento(searchNip, cdAit, reportCriterios);
		List<Notificacao> dadosSubreport = Arrays.asList(notificacao);
		reportBuilder.setParamsSubreport(dadosSubreport);
		Report report = new ReportBuilder()
				.search(searchNip)
				.reportCriterios(reportCriterios)
				.build();
		return report.getReportPdf(new NomeTipoImpressaoNipStrategy().buscar(getMovimentoPenalidade(cdAit), TipoRemessaCorreiosEnum.CARTA_SIMPLES));
	}

	protected SearchBuilder<Notificacao> searchBuilder(SearchCriterios searchCriterios) throws Exception {
        return new SearchBuilder<Notificacao>("mob_ait A")
        		.fields(  " A.cd_ait, A.id_ait, A.nr_renainf, A.nr_controle, A.dt_infracao, A.dt_prazo_defesa, A.ds_observacao, A.nr_cpf_condutor, A.dt_afericao,"
						+ " A.nr_lacre, A.nr_placa, A.sg_uf_veiculo, A.nr_renavan, A.ds_observacao, A.nm_proprietario, A.nr_cpf_cnpj_proprietario, "
						+ " A.nm_condutor, A.nr_cnh_condutor, A.uf_cnh_condutor, A.ds_local_infracao, A.ds_ponto_referencia, A.vl_velocidade_permitida, "
						+ " A.vl_velocidade_aferida, A.vl_velocidade_penalidade, A.ds_logradouro, A.ds_nr_imovel, A.nr_cep, A.dt_vencimento, "
						+ " A.dt_vencimento as DT_LIMITE_RECURSO, "
						+ " B.nr_cod_detran, B.ds_infracao, B.nr_artigo, B.nr_inciso, B.nr_paragrafo, B.nr_alinea, B.nm_natureza, B.nr_pontuacao, "
						+ " C.nm_cidade, C.nm_cidade AS nm_municipio, "
						+ " C1.sg_estado, C1.sg_estado AS nm_uf, C1.nm_estado, "
						+ " E.nm_categoria,"
						+ " F.nm_cor,"
						+ " G.ds_especie, "
						+ " H.nm_marca, H.nm_modelo, "
						+ " I.nm_equipamento, I.id_equipamento, I.nm_marca AS marca_equipamento, I.nm_modelo AS modelo_equipamento, I.tp_equipamento, I.cd_equipamento, "
						+ " J.nm_agente, J.nr_matricula,"
						+ " K.dt_movimento, K.tp_status, K.nr_processo, K.lg_enviado_detran, "
						+ " M.dt_afericao, M.id_identificacao_inmetro, "
						+ " N.ds_logradouro, "
						+ " P.nm_bairro, "
						+ " Q.cd_cidade, Q.nm_cidade AS CIDADE_PROPRIETARIO, "
						+ " R.sg_estado AS uf_proprietario, R.nm_estado AS ESTADO_PROPRIETARIO ")
        		.addJoinTable(" LEFT OUTER JOIN mob_infracao 		   B  ON (A.cd_infracao = B.cd_infracao)")
				.addJoinTable(" LEFT OUTER JOIN grl_cidade 			   C  ON (A.cd_cidade = C.cd_cidade)")
				.addJoinTable(" LEFT OUTER JOIN grl_estado 			   C1 ON (C.cd_estado = C1.cd_estado)")
				.addJoinTable(" LEFT OUTER JOIN fta_categoria_veiculo  E  ON (A.cd_categoria_veiculo = E.cd_categoria)")
				.addJoinTable(" LEFT OUTER JOIN fta_cor 			   F  ON (A.cd_cor_veiculo = F.cd_cor)")
				.addJoinTable(" LEFT OUTER JOIN fta_especie_veiculo    G  ON (A.cd_especie_veiculo = G.cd_especie)")
				.addJoinTable(" LEFT OUTER JOIN fta_marca_modelo	   H  ON (A.cd_marca_veiculo = H.cd_marca)")
				.addJoinTable(" LEFT OUTER JOIN grl_equipamento		   I  ON (A.cd_equipamento = I.cd_equipamento)")
				.addJoinTable(" LEFT OUTER JOIN mob_agente			   J  ON (A.cd_agente = J.cd_agente)")
				.addJoinTable(" LEFT OUTER JOIN mob_ait_movimento	   K  ON (A.cd_ait = K.cd_ait AND A.cd_movimento_atual = K.cd_movimento)")
				.addJoinTable(" LEFT OUTER JOIN mob_ait_evento         L  ON (A.cd_ait = L.cd_ait)")
				.addJoinTable(" LEFT OUTER JOIN mob_evento_equipamento M  ON (L.cd_evento = M.cd_evento)")
				.addJoinTable(" LEFT OUTER JOIN mob_ait_proprietario   N  ON (A.cd_ait = N.cd_ait)")
				.addJoinTable(" LEFT OUTER JOIN grl_bairro             P  ON (A.cd_bairro = P.cd_bairro)")
				.addJoinTable(" LEFT OUTER JOIN grl_cidade             Q  ON (Q.cd_cidade = A.cd_cidade_proprietario)")
				.addJoinTable(" LEFT OUTER JOIN grl_estado             R  ON (R.cd_estado = Q.cd_estado)")
                .searchCriterios(searchCriterios)
                .orderBy(" B.dt_fim_vigencia DESC");
    }
	
	
	private void montarDadosDocumento(Search<Notificacao> searchNip, int cdAit, ReportCriterios reportCriterios) throws Exception {
		if (searchNip.getRsm().next()) {
			int cd_equipamento = searchNip.getList(Notificacao.class).get(0).getCdEquipamento();
			searchNip.getRsm().setValueToField("NOME_EQUIPAMENTO", cd_equipamento > 0 ? EquipamentoEnum.valueOf(searchNip.getRsm().getInt("tp_equipamento")) : EquipamentoEnum.NENHUM.getValue());
		}
		Timestamp dtVencimentoOriginal = searchNip.getRsm().getTimestamp("DT_VENCIMENTO");
		searchNip.getRsm().setValueToField("DT_EMISSAO", Util.getDataAtual());
		aitReportServicesNip.calcularJuros(searchNip.getRsm(), cdAit, this.customConnection.getConnection());
		aitReportServicesNip.pegarBancoConveniado(this.customConnection.getConnection(), searchNip.getRsm());
		AitReportValidatorsNIP.trataDefesaPrevia(searchNip.getRsm(), reportCriterios.getParametros(), this.customConnection.getConnection());
		if (reportCriterios.getParametros().get("NR_CD_FEBRABAN") != null) {	
			aitReportServicesNip.gerarCodigoBarras(searchNip.getRsm(), reportCriterios.getParametros(), this.customConnection.getConnection());	 
		}
		boolean orgaoOptanteRecebimentoViaPix = this.parametroRepository.getValorOfParametroAsBoolean("LG_PIX");
		String tokenAutenticacao = null;
		if(orgaoOptanteRecebimentoViaPix) {
			tokenAutenticacao = obterTokenPix();
		}
		ResultSetMap rsm = searchNip.getRsm();
		rsm.beforeFirst();
		if(orgaoOptanteRecebimentoViaPix && tokenAutenticacao != null && rsm.next()) {
			ImagemQrCodePixGenerator qrCodeGenerator = new ImagemQrCodePixGenerator();
			Timestamp timeStamp = rsm.getTimestamp("DT_VENCIMENTO_BOLETO");
			searchNip.getRsm().setValueToField("DT_VENCIMENTO", timeStamp);
			Notificacao notificacao = qrCodeGenerator.registrarArrecadacaoPix(searchNip.getList(Notificacao.class).get(0), searchNip.getRsm().getString("CODIGO_BARRAS_COM_DV"), tokenAutenticacao);
			if(notificacao != null && notificacao.getPixQrCodeImage() != null) {
				searchNip.getRsm().setValueToField("PIX_QR_CODE_IMAGE", notificacao.getPixQrCodeImage());
			}
			searchNip.getRsm().setValueToField("DT_VENCIMENTO", dtVencimentoOriginal);
		}
		ApresentacaoCondutor apresentacaoCondutor = getCondutorFici(cdAit);
		if (apresentacaoCondutor != null) {
			searchNip.getRsm().setValueToField("NM_CONDUTOR", apresentacaoCondutor.getNmCondutor() != null ? apresentacaoCondutor.getNmCondutor() : null);
			searchNip.getRsm().setValueToField("NR_CNH_CONDUTOR", apresentacaoCondutor.getNrCnh() != null ? apresentacaoCondutor.getNrCnh() : null);
			searchNip.getRsm().setValueToField("UF_CNH_CONDUTOR", apresentacaoCondutor.getUfCnh() != null ? apresentacaoCondutor.getUfCnh() : null);
		}
		Map<String, Object> assinaturaAutoridade = colaboradorService.buscarAssinaturaAutoridade();
		reportCriterios.addParametros("MOB_IMAGEM_VEICULO", pegarImagemVeiculo(cdAit));
		reportCriterios.addParametros("LG_GERACAO_VIA_UNICA", true);
		reportCriterios.addParametros("MOB_TP_DOCUMENTO", TipoStatusEnum.NIP_ENVIADA.getKey());
		reportCriterios.addParametros("TP_PENALIDADE", TipoStatusEnum.NIP_ENVIADA.getKey());	
		reportCriterios.addParametros("ASSINATURA_AUTORIDADE", (assinaturaAutoridade != null && !assinaturaAutoridade.isEmpty()) 
				? (byte[]) assinaturaAutoridade.get("assinaturaAutoridade") : null);
		reportCriterios.addParametros("NM_AUTORIDADE", (assinaturaAutoridade != null && !assinaturaAutoridade.isEmpty()) 
				? (String) assinaturaAutoridade.get("nomeAutoridade") : null);
	}
	
	private String obterTokenPix() {
	    try {
	        String email = ManagerConf.getInstance().get("EMAIL_AUTENTICACAO_PIX");
	        String senha = ManagerConf.getInstance().get("SENHA_AUTENTICACAO_PIX");
	        DadosAutenticacaoEnvio dadosEnvio = new DadosAutenticacaoEnvioBuilder()
	            .addEmail(email)
	            .addSenha(senha)
	            .build();
	        AutenticacaoUsuarioPix registro = new AutenticacaoUsuarioPix(dadosEnvio);
	        DadosAutenticacaoRetorno dadosRetorno = registro.autenticar();

	        return dadosRetorno.getToken();
	    } catch (Exception e) {
	        System.err.println("Erro ao obter token Pix: " + e.getMessage());
	        return null; 
	    }
	}
	
	private Image pegarImagemVeiculo(int cdAit) throws Exception {
		this.imgMulta = null;
		this.aitImagem = new AitImagem();
		this.aitImagem = aitImagemService.getFromAit(cdAit);
		if(aitImagem.getBlbImagem() != null ) {
			this.imgMulta = new ImageIcon(aitImagem.getBlbImagem()).getImage();
		}
		return this.imgMulta;
	}
	
	private SearchCriterios montarCriterios(int cdAit) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit, true);
		return searchCriterios;
	}
	
	private ApresentacaoCondutor getCondutorFici(int cdAit) throws Exception {
		int codigoFiciAceita = this.parametroRepository.getValorOfParametroAsInt("CD_OCORRENCIA_APRESENTACAO_CONDUTOR_ACEITO");
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit);
		searchCriterios.addCriteriosEqualInteger("A.tp_status", TipoStatusEnum.APRESENTACAO_CONDUTOR.getKey());
		searchCriterios.addCriteriosEqualInteger("A.cd_ocorrencia", codigoFiciAceita, true);
		Search<ApresentacaoCondutor> searchCondutor = new SearchBuilder<ApresentacaoCondutor>("mob_ait_movimento A")
				.fields("D.nm_condutor, D.nr_cnh, D.uf_cnh").addJoinTable("JOIN mob_ait_movimento_documento B ON (A.cd_ait = B.cd_ait)")
				.addJoinTable("JOIN ptc_documento C ON (B.cd_documento = C.cd_documento)")
				.addJoinTable("JOIN ptc_apresentacao_condutor D ON (C.cd_documento = D.cd_documento)")
				.searchCriterios(searchCriterios).build();
		List<ApresentacaoCondutor> apresentacaoCondutorList = searchCondutor.getList(ApresentacaoCondutor.class);
	    if (!apresentacaoCondutorList.isEmpty()) {
	        return apresentacaoCondutorList.get(0);
	    }
	    return null;
	}

	private int getMovimentoPenalidade(int cdAit) throws Exception, ValidacaoException {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit);
		searchCriterios.addCriteriosEqualInteger("lg_enviado_detran", TipoLgEnviadoDetranEnum.REGISTRADO.getKey(), true);
		searchCriterios.addCriterios("tp_status", String.valueOf(TipoStatusEnum.NIP_ENVIADA.getKey()) + ", " +
	            String.valueOf(TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey()) + ", " +
	            String.valueOf(TipoStatusEnum.NIC_ENVIADA.getKey()), ItemComparator.IN, Types.INTEGER);
		List<AitMovimento> aitMovimentoList = this.aitMovimentoService.find(searchCriterios, new CustomConnection());
		if(aitMovimentoList.isEmpty()) {
	        throw new ValidacaoException("Movimento de penalidade não localizado ou não registrado.");
		}
		for (AitMovimento movimento : aitMovimentoList) {
	        if (movimento.getTpStatus() == TipoStatusEnum.NIC_ENVIADA.getKey()) {
	            return TipoStatusEnum.NIC_ENVIADA.getKey();
	        }
	    }
	    return aitMovimentoList.get(0).getTpStatus();
	}
}
