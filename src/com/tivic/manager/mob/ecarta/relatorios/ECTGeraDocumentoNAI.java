package com.tivic.manager.mob.ecarta.relatorios;

import java.util.List;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.EquipamentoEnum.EquipamentoEnum;
import com.tivic.manager.mob.aitimagem.repositories.IAitImagemRepository;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.lote.impressao.CalculaPrazoRecurso;
import com.tivic.manager.mob.lote.impressao.DadosNotificacao;
import com.tivic.manager.mob.lote.impressao.TipoRemessaCorreiosEnum;
import com.tivic.manager.mob.lote.impressao.dadosnotificacao.IDadosNotificacaoService;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.SearchCriterios;

public class ECTGeraDocumentoNAI implements IECTGeraDocumento {
	private CustomConnection customConnection;
	private AitMovimentoRepository aitMovimentoRepository;
	private IAitImagemRepository aitImagemRepository;
	private int tpRemessa;
	private IDadosNotificacaoService dadosNotificacaoService;

	public ECTGeraDocumentoNAI() throws Exception {
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		this.aitImagemRepository = (IAitImagemRepository) BeansFactory.get(IAitImagemRepository.class);
		this.dadosNotificacaoService = (IDadosNotificacaoService) BeansFactory.get(IDadosNotificacaoService.class);
	}

	@Override
	public byte[] gerar(int cdAit, CustomConnection connection) throws Exception {
		this.customConnection = connection;
		ReportCriterios reportCriterios = new BuscaParametrosNotificacao().buscar(connection);
		this.tpRemessa = Integer.parseInt(ParametroServices.getValorOfParametro("MOB_TIPO_ENVIO_CORREIOS_NAI"));
		String nmReport = this.tpRemessa == TipoRemessaCorreiosEnum.CARTA_SIMPLES.getKey()
				? "mob/nai_carta_simples_frente"
				: "mob/nai_ar_digital_frente";
		return findDocumentoNai(cdAit, reportCriterios).getReportPdf(nmReport);
	}

	private Report findDocumentoNai(int cdAit, ReportCriterios reportCriterios) throws Exception {
		List<DadosNotificacao> listDadosNotificacao = this.dadosNotificacaoService.buscarDadosNAI(cdAit,
				customConnection);
		montarDadosDocumento(listDadosNotificacao, reportCriterios);
		Report report = new ReportBuilder().list(listDadosNotificacao).reportCriterios(reportCriterios).build();
		return report;
	}

	private void montarDadosDocumento(List<DadosNotificacao> listDadosNotificacao, ReportCriterios reportCriterios)
			throws Exception {
		for (DadosNotificacao dadosNotificacao : listDadosNotificacao) {
			dadosNotificacao.setDtEmissao(Util.getDataAtual());
			SearchCriterios criteriosMovimento = criteriosMovimento(dadosNotificacao.getCdAit());
			AitMovimento aitMovimento = this.aitMovimentoRepository.find(criteriosMovimento, customConnection).get(0);
			dadosNotificacao.setDtMovimento(aitMovimento.getDtMovimento());
			dadosNotificacao.setNomeEquipamento(EquipamentoEnum.valueOf(dadosNotificacao.getTpEquipamento()));
			setarTxtCondutor(dadosNotificacao);
			setarDtPrazoDefesa(dadosNotificacao);
			reportCriterios.addParametros("MOB_IMAGEM_VEICULO",
					this.aitImagemRepository.pegarImagemVeiculo(dadosNotificacao.getCdAit(), this.customConnection));
		}
	}

	private SearchCriterios criteriosMovimento(int cdAit) {
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger(lgBaseAntiga ? "codigo_ait" : "cd_ait", cdAit, true);
		searchCriterios.addCriteriosEqualInteger("tp_status", TipoStatusEnum.NAI_ENVIADO.getKey(), true);
		return searchCriterios;
	}

	private void setarTxtCondutor(DadosNotificacao dadosNotificacao) throws ValidacaoException {
		if (this.tpRemessa != TipoRemessaCorreiosEnum.CARTA_SIMPLES.getKey()) {
			String informativo = ParametroServices.getValorOfParametro("MOB_TEXTO_IMFORMATIVO_CONDUTOR");
			informativo = (informativo != null) ? informativo
					: "APRESENTAÇÃO DO CONDUTOR EM ATÉ 30 DIAS A CONTAR DO RECEBIMENTO DESTA NOTIFICAÇÃO.";
			dadosNotificacao.setTxtCondutor(informativo);
		}
	}

	private void setarDtPrazoDefesa(DadosNotificacao dadosNotificacao) throws Exception {
		if (dadosNotificacao.getDtPrazoDefesa() == null) {
			dadosNotificacao
					.setDtPrazoDefesa(new CalculaPrazoRecurso().getStrategy(TipoStatusEnum.DEFESA_PREVIA.getKey())
							.gerarPrazo(dadosNotificacao.getCdAit(), customConnection));
		}
	}
}
