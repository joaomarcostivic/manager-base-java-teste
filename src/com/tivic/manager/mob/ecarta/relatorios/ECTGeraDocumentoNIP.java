package com.tivic.manager.mob.ecarta.relatorios;

import java.sql.Types;
import java.util.List;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.banco.IBancoService;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.EquipamentoEnum.EquipamentoEnum;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.AitMovimentoService;
import com.tivic.manager.mob.colaborador.IColaboradorService;
import com.tivic.manager.mob.lote.impressao.DadosNotificacao;
import com.tivic.manager.mob.lote.impressao.TipoRemessaCorreiosEnum;
import com.tivic.manager.mob.lote.impressao.codigobarras.CodigoBarras;
import com.tivic.manager.mob.lote.impressao.codigobarras.GerarCodigoBarrasFactory;
import com.tivic.manager.mob.lote.impressao.dadosnotificacao.IDadosNotificacaoService;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.SearchCriterios;
import edu.emory.mathcs.backport.java.util.Arrays;
import sol.dao.ItemComparator;

public class ECTGeraDocumentoNIP implements IECTGeraDocumento {
	
	private CustomConnection customConnection;
	private IBancoService bancoService;
	private int tpRemessa;
	private IDadosNotificacaoService dadosNotificacaoService;
	private IParametroRepository parametroRepository;
	private ReportCriterios reportCriterios;
	private AitMovimentoRepository aitMovimentoRepository;
	private IColaboradorService colaboradorService;
	
	public ECTGeraDocumentoNIP() throws Exception {
		this.bancoService = (IBancoService) BeansFactory.get(IBancoService.class); 
		this.dadosNotificacaoService = (IDadosNotificacaoService) BeansFactory.get(IDadosNotificacaoService.class);
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		this.colaboradorService = (IColaboradorService) BeansFactory.get(IColaboradorService.class);
	}

	@Override
	public byte[] gerar(int cdAit, CustomConnection customConnection) throws Exception, ValidacaoException {
		this.customConnection = customConnection;
		this.reportCriterios = new BuscaParametrosNotificacao().buscar(this.customConnection);
		this.tpRemessa = this.parametroRepository.getValorOfParametroAsInt(("MOB_TIPO_ENVIO_CORREIOS_NIP"));
		String nmReport = this.tpRemessa == TipoRemessaCorreiosEnum.CARTA_SIMPLES.getKey() ? "mob/nip_carta_simples_frente": "mob/nip_ar_digital_frente";
		return findDocumentoNip(cdAit, reportCriterios).getReportPdf(nmReport);
	}
	
	private Report findDocumentoNip(int cdAit, ReportCriterios reportCriterios) throws Exception {
		List<DadosNotificacao> listDadosNotificacao = this.dadosNotificacaoService.buscarDadosNIP(cdAit, customConnection);
		montarDadosDocumento(listDadosNotificacao, reportCriterios);
		Report report = new ReportBuilder()
				.list(listDadosNotificacao)
				.reportCriterios(reportCriterios)
				.build();
		return report;
	}
		
	private void montarDadosDocumento(List<DadosNotificacao> listDadosNotificacao, ReportCriterios reportCriterios) throws Exception {
		for (DadosNotificacao dadosNotificacao : listDadosNotificacao) {
			dadosNotificacao.setNomeEquipamento(EquipamentoEnum.valueOf(dadosNotificacao.getTpEquipamento()));
			dadosNotificacao.setVlMultaComDesconto((dadosNotificacao.getVlMulta() - (Double.valueOf(20) * dadosNotificacao.getVlMulta()) / 100));
			dadosNotificacao.setBancosConveniados(bancoService.pegarBancoConveniado());
			trataDefesaPrevia(dadosNotificacao);
			if (reportCriterios.getParametros().get("NR_CD_FEBRABAN") != null) {
				CodigoBarras codigoBarras = new GerarCodigoBarrasFactory()
						.getStrategy()
						.gerarCodigoBarras(dadosNotificacao, reportCriterios);
				dadosNotificacao.setLinhaDigitavel(codigoBarras.getLinhaDigitavel());
				reportCriterios.addParametros("BARRAS", codigoBarras.getBarcodeImage());
			}
		}
	}
	
	private void trataDefesaPrevia(DadosNotificacao dadosNotificacao) throws Exception {
		AitMovimento movimentoDefesaIndeferida = verificarDefesaIndeferida(dadosNotificacao.getCdAit());
		if (movimentoDefesaIndeferida.getNrProcesso() != null) {
			String defesaPreviaIndeferida = reportCriterios.getParametros().get("MOB_BLB_DEFESA_PREVIA_INDEFERIDA").toString();
			String coodernador = colaboradorService.buscaNomeAutoridadeTransito();
			String cidade = reportCriterios.getParametros().get("NM_CIDADE").toString();
			String proprietario =  dadosNotificacao.getNmProprietario() != null ? dadosNotificacao.getNmProprietario() : " ";
			String nrAit = dadosNotificacao.getIdAit() != null ? dadosNotificacao.getIdAit() : " ";
			String nrRenavan = dadosNotificacao.getNrRenavan() != null ? dadosNotificacao.getNrRenavan() : " ";
			String nrPlaca = dadosNotificacao.getNrPlaca() != null ? dadosNotificacao.getNrPlaca() : " ";
			defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#PROPRIETARIO>", proprietario);
			defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#Nr PROCESSO>", movimentoDefesaIndeferida.getNrProcesso());
			defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#COORDENADOR>", coodernador);
			defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#Nr AIT>", nrAit);
			defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#Nr RENAVAN>", nrRenavan);
			defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#Nr PLACA>", nrPlaca);
			defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#CIDADE>", cidade);
			dadosNotificacao.setDefesaPreviaIndeferida(defesaPreviaIndeferida);
		}
	}
	
	private AitMovimento verificarDefesaIndeferida(int cdAit) throws Exception {
		List<AitMovimento> listDefesas = this.aitMovimentoRepository.find(searchCriteriosGetAllDefesas(cdAit), this.customConnection); 
		for(AitMovimento movimentoDefesaIndeferida : listDefesas) {
			if(movimentoDefesaIndeferida.getTpStatus() == TipoStatusEnum.DEFESA_INDEFERIDA.getKey() ||
				movimentoDefesaIndeferida.getTpStatus() == TipoStatusEnum.ADVERTENCIA_DEFESA_INDEFERIDA.getKey()) {
				return movimentoDefesaIndeferida;
			}
		}
		return new AitMovimento();
	}
	
	private SearchCriterios searchCriteriosGetAllDefesas(int cdAit) throws Exception {
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger(lgBaseAntiga ? "codigo_ait" : "cd_ait", cdAit, true);
		searchCriterios.addCriterios("tp_status", Arrays.toString(new AitMovimentoService().DEFESAS).replace("[", "").replace("]", ""), ItemComparator.IN, Types.INTEGER);
		return searchCriterios;
	}

}
