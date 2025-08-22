package com.tivic.manager.wsdl.detran.mg;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitDAO;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitPagamentoDAO;
import com.tivic.manager.mob.ServicoDetranDTO;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.IAitService;
import com.tivic.manager.mob.ait.SituacaoAitEnum;
import com.tivic.manager.mob.ait.circuitoait.CircuitoAit;
import com.tivic.manager.mob.aitmovimento.AitMovimentoAtualizaStatusAit;
import com.tivic.manager.mob.aitmovimento.AitMovimentoDTO;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.lotes.builders.impressao.LoteImpressaoAitBuilder;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressaoAit;
import com.tivic.manager.mob.lotes.service.impressao.ILoteImpressaoService;
import com.tivic.manager.util.DateUtil;
import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.ServicoDetranConsultaFactory;
import com.tivic.manager.wsdl.ServicoDetranFactory;
import com.tivic.manager.wsdl.detran.mg.alterardatalimiterecurso.AlterarDataLimiteRecurso;
import com.tivic.manager.wsdl.detran.mg.builders.AitBuilderSearchBaseEstadual;
import com.tivic.manager.wsdl.detran.mg.builders.AitBuilderSearchBaseNacional;
import com.tivic.manager.wsdl.detran.mg.builders.AitBuilderSearchMovimentacoes;
import com.tivic.manager.wsdl.detran.mg.builders.AitBuilderSearchPlaca;
import com.tivic.manager.wsdl.detran.mg.builders.AitBuilderSearchRecursos;
import com.tivic.manager.wsdl.detran.mg.builders.ServicoDetranObjetoMGBuilder;
import com.tivic.manager.wsdl.detran.mg.consultainfracoes.ConsultarInfracoesDadosRetorno;
import com.tivic.manager.wsdl.detran.mg.consultainfracoes.DadosInfratorConsultaEntrada;
import com.tivic.manager.wsdl.detran.mg.consultapontuacaodadoscondutor.ConsultarPontuacaoDadosCondutorDadosRetorno;
import com.tivic.manager.wsdl.detran.mg.consultapontuacaodadoscondutor.DadosCondutorDocumentoEntrada;
import com.tivic.manager.wsdl.detran.mg.consultapontuacaodadoscondutor.PontuacaoDadosCondutorDTO;
import com.tivic.manager.wsdl.detran.mg.consultapontuacaodadoscondutor.PontuacaoDadosCondutorDTOBuilder;
import com.tivic.manager.wsdl.detran.mg.enviorenainf.EnvioRenainf;
import com.tivic.manager.wsdl.detran.mg.geradorlotenotificacao.GeraLoteNotificacaoImportFactory;
import com.tivic.manager.wsdl.detran.mg.geradormovimentodocumento.GeradorMovimentoDocumentoFactory;
import com.tivic.manager.wsdl.detran.mg.notificacao.Notificacao;
import com.tivic.manager.wsdl.detran.mg.notificacao.alterarprazorecurso.AlteraPrazoRecursoDTO;
import com.tivic.manager.wsdl.detran.mg.notificacao.alterarprazorecurso.AlterarPrazoRecursoFactory;
import com.tivic.manager.wsdl.detran.mg.soap.ArquivoConfiguracaoMg;
import com.tivic.manager.wsdl.detran.mg.soap.ArquivoConfiguracaoMgHomologacao;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.DadosRetorno;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.search.SearchCriterios;

public class ServicoDetranServicesMG implements ServicoDetranServices {

	private IAitService aitService;
	private IAitMovimentoService aitMovimentoService;
	private CircuitoAit circuitoAit;
	private ILoteImpressaoService loteImpressaoService;
	private ManagerLog managerLog;
	private AitMovimentoAtualizaStatusAit aitMovimentoAtualizaStatusAit;
	
	public static final int CODIGO_RETORNO_SUCESSO 			= 0;
	public static final int CODIGO_RETORNO_PRAZO_VENCIDO 	= 100;
	public static final int CODIGO_RETORNO_ERRO_VALIDACAO	= 6009;
	public static final int CODIGO_RETORNO_ERRO_SISTEMA		= 9999;
	
	public ServicoDetranServicesMG() throws Exception {
		this.aitService = (IAitService) BeansFactory.get(IAitService.class);
		this.aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		this.circuitoAit = (CircuitoAit) BeansFactory.get(CircuitoAit.class);
		this.loteImpressaoService = (ILoteImpressaoService) BeansFactory.get(ILoteImpressaoService.class);
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.aitMovimentoAtualizaStatusAit = new AitMovimentoAtualizaStatusAit();
	}
	
	@Override
	public List<ServicoDetranDTO> remessa(List<AitMovimento> aitsMovimento) throws Exception {
		boolean isProducao = ManagerConf.getInstance().getAsBoolean("PRODEMGE_PRODUCAO", false);
		this.managerLog.info("PRODUCÃO: ", String.valueOf(isProducao));
		List<ServicoDetranObjeto> servicosDetranObject = new ArrayList<ServicoDetranObjeto>();
		int count = 0;
		for(AitMovimento aitMovimento : aitsMovimento){
			Ait ait = this.aitService.get(aitMovimento.getCdAit());
			this.managerLog.info("\n\n\n ATUALIZAÇÃO DE Nº " + count++, "Ait: " + ait.getCdAit());
			try {
				this.managerLog.info("VALIDAÇÃO  ", " CircuitAit ");
				this.circuitoAit.validarEnvioDetran(aitMovimento);
				this.managerLog.info("GERANDO SERVICO ", " Servico DETRAN sendo gerado ");
				ServicoDetran servicoDetran = ServicoDetranFactoryMG.gerarServico(aitMovimento.getTpStatus(), isProducao);
				this.managerLog.info("MONTANDO SERVICO", " Iniciando montagem de serviço.");
				AitDetranObject detranObject = new AitDetranObject(ait, aitMovimento);
				detranObject.setAitPagamento(AitPagamentoDAO.get(aitMovimento.getCdAit()));
				this.managerLog.info("ENVIANDO", " Enviando movimento ao DETRAN");
				ServicoDetranObjeto servicoDetranObjeto = servicoDetran.executar(detranObject);
				servicosDetranObject.add(servicoDetranObjeto);
				this.managerLog.info("AIT FINALIZADO", "Movimento enviado ao DETRAN");
			} catch(Exception e) {
				e.printStackTrace();
				ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoMGBuilder(ait, aitMovimento)
					.dadosRetornoErro(e.getMessage())
				.build();
				servicosDetranObject.add(servicoDetranObjeto);
			} 
		}
		this.managerLog.info("ENVIOS FINALIZADOS", "Envios realizados");
		return createRetornoDTO(servicosDetranObject);
	}
	
	@Override
	public List<ServicoDetranDTO> renainf(List<AitMovimento> aisMovimento) throws Exception {
		List<ServicoDetranObjeto> servicosDetranObject = new ArrayList<ServicoDetranObjeto>();
		for(AitMovimento aitMovimento : aisMovimento){
			Ait ait = AitDAO.get(aitMovimento.getCdAit());
			ServicoDetran servicoDetran = new EnvioRenainf();
			setarArquivoConfiguracao(servicoDetran);
			ServicoDetranObjeto servicoDetranObjeto = servicoDetran.executar(new AitDetranObject(ait, aitMovimento));
			servicosDetranObject.add(servicoDetranObjeto);
		}
		return createRetornoDTO(servicosDetranObject);
	}

	private List<ServicoDetranDTO> createRetornoDTO(List<ServicoDetranObjeto> servicosDetranObject) {
        List<ServicoDetranDTO> servicosDetranDTO = new ArrayList<ServicoDetranDTO>();
        for(ServicoDetranObjeto servicoDetranObjeto : servicosDetranObject) {
            ServicoDetranDTO detranDTO = new ServicoDetranDTO();
            detranDTO.setNrAit(servicoDetranObjeto.getAit().getIdAit());
            detranDTO.setCodigoRetorno(((DadosRetornoMG)servicoDetranObjeto.getDadosRetorno()).getCodigoRetorno());
            detranDTO.setMensagemRetorno(construirMensagemRetorno(servicoDetranObjeto));
            detranDTO.setTipoRetorno(TipoStatusEnum.valueOf(servicoDetranObjeto.getAitMovimento().getTpStatus()));
            detranDTO.setDataMovimento(servicoDetranObjeto.getAitMovimento().getDtMovimento());
            servicosDetranDTO.add(detranDTO);
        }
        return servicosDetranDTO;
    }
	
	private String construirMensagemRetorno(ServicoDetranObjeto servicoDetranObjeto) {
		DadosRetornoMG dadosRetorno = (DadosRetornoMG)servicoDetranObjeto.getDadosRetorno();
		if(dadosRetorno.getCodigoRetorno() == CODIGO_RETORNO_ERRO_VALIDACAO) {
			String mensagens = "";
			for(String mensagemValidacao : dadosRetorno.getMensagens()) {
				mensagens += mensagemValidacao.replaceAll("\"", "") + ", ";
			}
			return (mensagens.length() > 0 ? mensagens.substring(0, mensagens.length()-2) : dadosRetorno.getMensagemRetorno());
		}
		return dadosRetorno.getMensagemRetorno();
	}
	
	@Override
	public AitSyncDTO verificarAitSync(int cdAit) throws Exception {
		return verificarAitSync(cdAit, new CustomConnection());
	}
	
	public AitSyncDTO verificarAitSync(int cdAit, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(true);
			Ait ait = aitService.get(cdAit, customConnection);
			List<AitMovimento> movimentos = aitMovimentoService.getByAit(ait.getCdAit(), customConnection);
			ait.setMovimentos(movimentos);
			Ait aitSync = (Ait) ait.clone();
			aitSync.setMovimentos(new ArrayList<AitMovimento>());
			searchDetran(aitSync);
			return new AitSyncDTO(ait, aitSync);
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public Ait incluirAitSync(String idAit, String nrPlaca, int cdUsuario) throws Exception {
		return incluirAitSync(idAit, nrPlaca, cdUsuario, new CustomConnection());
	}
	
	public Ait incluirAitSync(String idAit, String nrPlaca, int cdUsuario, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(true);
			if(aitService.hasAit(idAit)) {
				throw new ValidacaoException("O auto já existe.");
			}
			Ait ait = new Ait();
			ait.setIdAit(idAit);
			ait.setNrPlaca(nrPlaca);
			ait.setCdUsuario(cdUsuario);
			ait.setStAit(SituacaoAitEnum.ST_CONFIRMADO.getKey());
			searchDetran(ait);
			this.aitService.insert(ait, customConnection);
			List<AitMovimento> movimentos = ait.getMovimentos();	
			for (AitMovimento movimento : movimentos) {
	            movimento.setCdAit(ait.getCdAit());
	            aitMovimentoService.insert(movimento, customConnection);
	        }
	        if (!movimentos.isEmpty()) {
	            ait = atualizaStatusAit(movimentos.get(movimentos.size() - 1), customConnection);
	        }
			new GeraDocumentos(ait.getMovimentos(), ait.getCdAit(), customConnection).gerar();
			customConnection.finishConnection();
			criarLoteNotificacaoImport(ait);
			return ait;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private Ait atualizaStatusAit(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
		Ait ait = this.aitMovimentoAtualizaStatusAit.atualizaCancelado(aitMovimento, customConnection);
		return ait;
	}
  
	@Override
	public void searchDetran(Ait ait) throws Exception {
		searchFromBaseEstadual(ait);
		searchFromBaseNacional(ait);
		searchFromPlaca(ait);
		searchMovimentos(ait);
		searchRecursos(ait);
		ordenarMovimentos(ait);
	}
	
	private void ordenarMovimentos(Ait ait) throws Exception {
		List<AitMovimento> listaMovimentos = aitMovimentoService.getByAit(ait.getCdAit());
		for(int i = 0; i < listaMovimentos.size()-1; i++) {
			if(listaMovimentos.get(i).getDtMovimento().after(listaMovimentos.get(i+1).getDtMovimento())) {
				AitMovimento aitMovimentoMenor = listaMovimentos.get(i+1);
				listaMovimentos.set(i+1, listaMovimentos.get(i));
				listaMovimentos.set(i, aitMovimentoMenor);
				i = -1;
			}
		}
	}

	private void searchFromBaseEstadual(Ait ait) throws Exception {		
		AitDetranObject aitDetranObject = new AitDetranObject(ait);
		DadosRetorno  retorno = ServicoDetranConsultaFactory.gerarServico(ServicoDetranFactory.MG, ServicoDetranConsultaFactory.AUTO_BASE_ESTADUAL)
				.executar(aitDetranObject).getDadosRetorno();
		if(retorno == null)
			return;
		new AitBuilderSearchBaseEstadual().build(ait, retorno);		
	}
	
	private void searchFromBaseNacional(Ait ait) throws Exception {		
		AitDetranObject aitDetranObject = new AitDetranObject(ait);
		DadosRetorno retorno = ServicoDetranConsultaFactory.gerarServico(ServicoDetranFactory.MG, ServicoDetranConsultaFactory.AUTO_BASE_NACIONAL)
				.executar(aitDetranObject).getDadosRetorno();
		if(retorno == null)
			return;	
		new AitBuilderSearchBaseNacional().build(ait, retorno);	
	}
	
	private void searchFromPlaca(Ait ait) throws Exception {
		AitDetranObject aitDetranObject = new AitDetranObject(ait.getNrPlaca());
		DadosRetorno retorno = ServicoDetranConsultaFactory.gerarServico(ServicoDetranFactory.MG, ServicoDetranConsultaFactory.PLACA)
				.executar(aitDetranObject).getDadosRetorno();
		if(retorno == null)
			return;
		new AitBuilderSearchPlaca().build(ait, retorno);		
	}
	
	private void searchMovimentos(Ait ait) throws Exception {
		AitDetranObject aitDetranObject = new AitDetranObject(ait);
		DadosRetorno retorno = ServicoDetranConsultaFactory.gerarServico(ServicoDetranFactory.MG, ServicoDetranConsultaFactory.MOVIMENTACOES)
				.executar(aitDetranObject).getDadosRetorno();
		if(retorno == null)
			return;
		new AitBuilderSearchMovimentacoes().build(ait, retorno);		
	}

	private void searchRecursos(Ait ait) throws Exception {
		AitDetranObject aitDetranObject = new AitDetranObject(ait);
		DadosRetorno retorno = ServicoDetranConsultaFactory.gerarServico(ServicoDetranFactory.MG, ServicoDetranConsultaFactory.RECURSOS)
				.executar(aitDetranObject).getDadosRetorno();
		if(retorno == null)
			return;
		new AitBuilderSearchRecursos().build(ait, retorno);		
	}
	
	private void criarLoteNotificacaoImport(Ait ait) throws Exception {
		CustomConnection customConnection = new  CustomConnection();
		try {
			customConnection.initConnection(true);
			for(AitMovimento aitMovimento : ait.getMovimentos()) {
				new GeraLoteNotificacaoImportFactory().getStrategy(aitMovimento.getTpStatus(), ait, customConnection).build();
			}
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Ait atualizarAitSync(int cdAit) throws Exception {
		return atualizarAitSync(cdAit, new CustomConnection());
	}
	
	public Ait atualizarAitSync(int cdAit, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(true);
			Ait ait = aitService.get(cdAit, customConnection);
			ait.setMovimentos(aitMovimentoService.getByAit(ait.getCdAit(), customConnection));
			ordenarMovimentos(ait);
			Ait aitSync = (Ait) ait.clone();
			aitSync.setMovimentos(new ArrayList<AitMovimento>());
			searchDetran(aitSync);
			for(AitMovimento aitMovimentoSync : aitSync.getMovimentos()) {
				boolean movimentoEncontrado = false;
				for(AitMovimento aitMovimento : ait.getMovimentos()) {
					if(aitMovimentoSync.getTpStatus() == aitMovimento.getTpStatus()) {
						aitMovimento.setDtMovimento(aitMovimentoSync.getDtMovimento());
						aitMovimento.setLgEnviadoDetran(1);
						aitMovimento.setDtRegistroDetran(aitMovimentoSync.getDtRegistroDetran());
						this.aitMovimentoService.update(aitMovimento, customConnection);
						atualizarAitComMovimento(ait, aitMovimento);
						movimentoEncontrado = true;
					}
				}
				
				if(!movimentoEncontrado) {
					aitMovimentoSync.setCdAit(ait.getCdAit());
					this.aitMovimentoService.insert(aitMovimentoSync, customConnection);
					new GeradorMovimentoDocumentoFactory().factory(aitMovimentoSync, customConnection).build();
					atualizarAitComMovimento(ait, aitMovimentoSync);
				}
			}
			
			this.aitService.update(ait, customConnection);
			customConnection.finishConnection();
			return ait;
		} finally {
			customConnection.closeConnection();
		}
	}

	private void atualizarAitComMovimento(Ait ait, AitMovimento aitMovimento) {
		ait.setTpStatus(aitMovimento.getTpStatus());
		ait.setDtRegistroDetran(aitMovimento.getDtMovimento());
		ait.setCdMovimentoAtual(aitMovimento.getCdMovimento());
	}

	@Override
	public PontuacaoDadosCondutorDTO consultarPontuacaoDadosCondutor(String documento, int tpDocumento) throws Exception {
		AitDetranObject aitDetranObject = new AitDetranObject();
		aitDetranObject.setDadosCondutorDocumentoEntrada(new DadosCondutorDocumentoEntrada(documento, tpDocumento));
		ServicoDetranObjeto servicoDetranObjeto = ServicoDetranConsultaFactory
				.gerarServico(ServicoDetranFactory.MG, ServicoDetranConsultaFactory.PONTUACAO_DADOS_CONDUTOR)
				.executar(aitDetranObject);
		verificarRetorno(servicoDetranObjeto);
		ConsultarPontuacaoDadosCondutorDadosRetorno dadosRetorno = (ConsultarPontuacaoDadosCondutorDadosRetorno) servicoDetranObjeto
				.getDadosRetorno();
		return new PontuacaoDadosCondutorDTOBuilder(dadosRetorno).build();
	}
	
	@Override
	public ConsultarInfracoesDadosRetorno consultarInfracoes(String documento, int tpDocumento, String dtInicial, String dtFinal) throws Exception {
		AitDetranObject aitDetranObject = new AitDetranObject();
		aitDetranObject.setDadosInfratorConsultaEntrada(new DadosInfratorConsultaEntrada(documento, tpDocumento, dtInicial, dtFinal));
		ServicoDetranObjeto servicoDetranObjeto = ServicoDetranConsultaFactory
				.gerarServico(ServicoDetranFactory.MG, ServicoDetranConsultaFactory.INFRACOES)
				.executar(aitDetranObject);
		verificarRetorno(servicoDetranObjeto);
		return (ConsultarInfracoesDadosRetorno) servicoDetranObjeto.getDadosRetorno();
	}	
	
	@Override
	public ConsultarInfracoesDadosRetorno consultarInfrator(String documento, int tpDocumento, String dtInicial, String dtFinal) throws Exception {
		AitDetranObject aitDetranObject = new AitDetranObject();
		aitDetranObject.setDadosInfratorConsultaEntrada(new DadosInfratorConsultaEntrada(documento, tpDocumento, dtInicial, dtFinal));
		ServicoDetranObjeto servicoDetranObjeto = ServicoDetranConsultaFactory
				.gerarServico(ServicoDetranFactory.MG, ServicoDetranConsultaFactory.INFRACOES)
				.executar(aitDetranObject);
		return (ConsultarInfracoesDadosRetorno) servicoDetranObjeto.getDadosRetorno();
	}

	@Override
	public Ait alterarDataLimiteRecurso(int cdAit, GregorianCalendar novaDataLimiteRecurso) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		ServicoDetran servicoDetran = new AlterarDataLimiteRecurso();
		try {
			customConnection.initConnection(true);
			Ait ait = aitService.get(cdAit);
			ait.setDtVencimento(novaDataLimiteRecurso);
			setarArquivoConfiguracao(servicoDetran);
			ServicoDetranObjeto servicoDetranObjeto = servicoDetran.executar(new AitDetranObject(ait));
			verificarRetorno(servicoDetranObjeto);
			aitService.update(ait, customConnection);
			customConnection.finishConnection();
			return ait;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public Ait alterarPrazoRecurso(AlteraPrazoRecursoDTO alteraPrazoRecursoDTO) throws Exception {
		CustomConnection customConnection =  new CustomConnection();
		try {
			customConnection.initConnection(true);
			Ait ait = alterarPrazoRecurso(alteraPrazoRecursoDTO, new CustomConnection());
			customConnection.finishConnection();
			return ait;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public Ait alterarPrazoRecurso(AlteraPrazoRecursoDTO alteraPrazoRecursoDTO, CustomConnection customConnection) throws Exception {
		ServicoDetran servicoDetran = new Notificacao();
		AitDetranObject aitDetranObject = new AlterarPrazoRecursoFactory()
				.getStrategy(alteraPrazoRecursoDTO.getTipoRecurso())
				.alterar(alteraPrazoRecursoDTO);
		setarArquivoConfiguracao(servicoDetran);
		ServicoDetranObjeto servicoDetranObjeto = servicoDetran.executar(aitDetranObject);

		verificarRetorno(servicoDetranObjeto);

		aitService.update(aitDetranObject.getAit(), customConnection);
		return aitDetranObject.getAit();
	}
	
	@Override
	public void alterarPrazoRecursoLoteImpressao(AlteraPrazoRecursoDTO alteraPrazoRecursoDTO) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		List<LoteImpressaoAit> aitsComFalha = new ArrayList<LoteImpressaoAit>();
		try {
			customConnection.initConnection(true);
			List<Ait> aitsLoteNovoPrazo = this.loteImpressaoService.buscarAitLoteImpressao(alteraPrazoRecursoDTO.getCdLoteImpressao(), alteraPrazoRecursoDTO.getTipoRecurso(), customConnection);
			for (Ait ait : aitsLoteNovoPrazo) {
				alteraPrazoRecursoDTO.setCdAit(ait.getCdAit());
				try {
					gerarDtPrazo(alteraPrazoRecursoDTO, customConnection);
					alterarPrazoRecurso(alteraPrazoRecursoDTO, customConnection);
				} catch(Exception e) {
					if(e.getMessage().equals("Falha na comunicação com o servidor."))
							throw new ValidacaoException("Não foi possível atualizar os prazos no momento. Tente novamente mais tarde.");
					if(!e.getMessage().equals("Novo prazo FICI  def  ou rec  inform  n  maior q  prazo reg  anterior")) {
						this.managerLog.info("Falha no AIT " + ait.getCdAit(), "ERRO: " + e.getMessage());
						LoteImpressaoAit loteImpressaoAit = new LoteImpressaoAitBuilder()
								.setCdLoteImpressao(alteraPrazoRecursoDTO.getCdLoteImpressao())
								.setCdAit(ait.getCdAit())
								.setStImpressao(alteraPrazoRecursoDTO.getTipoRecurso())
								.build();
		
						aitsComFalha.add(loteImpressaoAit);
					}
				}
			}
			this.managerLog.info("AITs que foram impedidos de atualizar: ", aitsComFalha.toString());
			this.loteImpressaoService.atualizarLotePrazoRecurso(alteraPrazoRecursoDTO, aitsLoteNovoPrazo, aitsComFalha, customConnection);
			customConnection.finishConnection();
		} finally {
			String msgNovoLote = aitsComFalha.isEmpty()? "": " \n Novo lote criado para os AITs impedidos.";
			this.managerLog.info("FINALIZADO ", "Novo prazo para os AITs foram enviados." + msgNovoLote);
			customConnection.closeConnection();
		}			
	}
		
	public void alterarPrazoRecursoLoteAits(AlteraPrazoRecursoDTO alteraPrazoRecursoDTO) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			List<Integer> aitsImpedidos = new ArrayList<Integer>();
			for (int cdAit : alteraPrazoRecursoDTO.getCdsAit()) {
				alteraPrazoRecursoDTO.setCdAit(cdAit);
				try {
					gerarDtPrazo(alteraPrazoRecursoDTO, customConnection);
					alterarPrazoRecurso(alteraPrazoRecursoDTO, customConnection);
				} catch(Exception e) {
					if(!e.getMessage().equals("Novo prazo FICI  def  ou rec  inform  n  maior q  prazo reg  anterior")) {
						this.managerLog.info("Falha no AIT " + cdAit, "ERRO: " + e.getMessage());
						aitsImpedidos.add(cdAit);
					}
				}
			}
			this.managerLog.info("Aits que foram impedidos de atualizar: ", aitsImpedidos.toString());
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}		
	}	

	private void gerarDtPrazo(AlteraPrazoRecursoDTO alteraPrazoRecursoDTO, CustomConnection customConnection) throws Exception {
		GregorianCalendar dataPrazoAlterada = DateUtil.today();
		DateUtil.addDays(dataPrazoAlterada, alteraPrazoRecursoDTO.getQtdDiasPrazo());
		alteraPrazoRecursoDTO.setNovoPrazoRecurso(verificarFinalSemana(dataPrazoAlterada));
	}
	
	private GregorianCalendar verificarFinalSemana(GregorianCalendar dataPrazoAlterada) {
		if(dataPrazoAlterada.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
			DateUtil.addDays(dataPrazoAlterada, 2);
		if(dataPrazoAlterada.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY )
				DateUtil.addDays(dataPrazoAlterada, 1);			
		return dataPrazoAlterada;
	}
	
	private void setarArquivoConfiguracao(ServicoDetran servicoDetran) {
		boolean isProducao = ManagerConf.getInstance().getAsBoolean("PRODEMGE_PRODUCAO", false);
		if(isProducao)
			servicoDetran.setArquivoConfiguracao(new ArquivoConfiguracaoMg());
		else
			servicoDetran.setArquivoConfiguracao(new ArquivoConfiguracaoMgHomologacao());
	}
	
	private void verificarRetorno(ServicoDetranObjeto servicoDetranObjeto) throws ValidacaoException {
		if (((DadosRetornoMG)servicoDetranObjeto.getDadosRetorno()).getCodigoRetorno() > 0) {
			throw new ValidacaoException(((DadosRetornoMG)servicoDetranObjeto.getDadosRetorno()).getMensagemRetorno());
		}
	}

	@Override
	public void envioAutomativo(SearchCriterios searchCriterios, boolean lgNaoEnviado) throws Exception {
		List<AitMovimentoDTO> aitMovimentoDTOList = this.aitMovimentoService.findRemessa(searchCriterios, lgNaoEnviado).getItems();
		if(aitMovimentoDTOList.isEmpty())
			throw new NoContentException("Nenhum movimento encontrado para fazer o envio!");
		List<AitMovimento> aitMovimentoList = aitMovimentoDTOList.stream()
			    .map(aitMovimentoDto -> (AitMovimento) aitMovimentoDto)
			    .collect(Collectors.toList());
		remessa(aitMovimentoList);
	}

}
