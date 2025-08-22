package com.tivic.manager.mob.lote.impressao.remessacorreios.arquivospostagem;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Base64;
import java.util.GregorianCalendar;
import java.util.List;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.ArquivoBuilder.ArquivoBuilder;
import com.tivic.manager.grl.arquivo.IArquivoRepository;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.CorreiosEtiqueta;
import com.tivic.manager.mob.ServicoDetranDTO;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.mob.aitmovimento.aitmovimentobuilder.AitMovimentoBuilder;
import com.tivic.manager.mob.correios.ArquivoTipoEnum;
import com.tivic.manager.mob.correios.ICorreiosEtiquetaRepository;
import com.tivic.manager.mob.lote.impressao.TipoArDigitalEnum;
import com.tivic.manager.mob.lote.impressao.remessacorreios.builders.RetornoCorreios;
import com.tivic.manager.wsdl.ServicoDetranServicesFactory;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.util.date.DateUtil;

import sol.dao.ItemComparator;

public class ArquivoRetornoPostagem implements IArquivoRetorno {
	
	private List<DadosRetornoCorreioDto> dadosRetornoCorreioDtoList;
	private AitMovimentoRepository aitMovimentoRepository;
	private CustomConnection customConnection;
	private IArquivoRepository arquivoRepository;
	private byte[] arquivoBase64;
	private ServicoDetranServices servicoDetranServices;
	int tpMovimentoDadosCorreios;
	private ICorreiosEtiquetaRepository correiosEtiquetaRepository;
	
	public ArquivoRetornoPostagem() throws Exception {
		this.dadosRetornoCorreioDtoList = new ArrayList<DadosRetornoCorreioDto>();
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		this.arquivoRepository = (IArquivoRepository) BeansFactory.get(IArquivoRepository.class);
		this.correiosEtiquetaRepository = (ICorreiosEtiquetaRepository) BeansFactory.get(ICorreiosEtiquetaRepository.class);
	}

	@Override
	public List<DadosRetornoCorreioDto> ler(ArquivoRetornoCorreiosDTO arquivoRetornoCorreios, int cdUsuario, CustomConnection customConnection) throws Exception {
		this.customConnection = customConnection;
		byte[] decoded = Base64.getDecoder().decode(arquivoRetornoCorreios.getArquivoRetorno().split(",")[1]);
		BufferedReader reader = decoded == null ? null : new BufferedReader(new InputStreamReader(new ByteArrayInputStream(decoded)));
		int tpRemessa = pegarTipoRemessa();
		this.dadosRetornoCorreioDtoList = new RetornoCorreios().obterDados(tpRemessa, reader);
		if (this.dadosRetornoCorreioDtoList.isEmpty()) {
			throw new ValidacaoException("Não foi possivel ler os dados de retorno, verifique o arquivo.");
		}
		processarDados(cdUsuario);
		arquivoRetornoCorreios.setNmArquivo(removeFileExtension(arquivoRetornoCorreios.getNmArquivo()));
		this.arquivoBase64 = Base64.getMimeDecoder().decode(arquivoRetornoCorreios.getArquivoRetorno());
		this.saveArquivo(arquivoBase64, arquivoRetornoCorreios, this.customConnection);
		return this.dadosRetornoCorreioDtoList;
	}
	
	private String removeFileExtension(String nmFile) throws Exception {
		return nmFile.substring(0, nmFile.lastIndexOf('.'));
	}
	
	private int pegarTipoRemessa() {
		String sgCliente = ParametroServices.getValorOfParametro("MOB_CORREIOS_SG_CLIENTE", this.customConnection.getConnection());
		int tpRemessa = sgCliente.length() > 2 ? TipoArDigitalEnum.AR_DIGITAL_2D.getKey() : TipoArDigitalEnum.AR_DIGITAL.getKey();
		return tpRemessa;
	}

	private void processarDados(int cdUsuario) throws Exception {
		for (DadosRetornoCorreioDto dadosRetornoCorreioDto : this.dadosRetornoCorreioDtoList) {
			CorreiosEtiqueta etiqueta = buscarEtiqueta(dadosRetornoCorreioDto.getNrEtiqueta(), dadosRetornoCorreioDto.getSgServico());
			if(etiqueta.getCdAit() == 0) {
				throw new ValidacaoException("A etiqueta " + etiqueta.getNrEtiqueta() + " não tem AIT vinculado." );
			}
			atualizarEtiqueta(etiqueta, dadosRetornoCorreioDto);
			SearchCriterios searchCriterios = monstarCriteriosDadosCorreios(etiqueta.getCdAit());
			List<AitMovimento> aitMovimentos = getMovimentos(searchCriterios);
			if (aitMovimentos.isEmpty()) {
				criarMovimentoDadosCorreios(etiqueta.getCdAit(), cdUsuario, dadosRetornoCorreioDto);
				enviarMovimentoDadosCorreio(etiqueta.getCdAit(), this.tpMovimentoDadosCorreios);
			} else if(aitMovimentos.get(0).getLgEnviadoDetran() == TipoLgEnviadoDetranEnum.NAO_ENVIADO.getKey()) {
				enviarMovimentoDadosCorreio(etiqueta.getCdAit(), aitMovimentos.get(0).getTpStatus());
			}
		}
	}
	
	private CorreiosEtiqueta buscarEtiqueta(String nrEtiqueta, String sgServico) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("A.nr_etiqueta", nrEtiqueta);
		searchCriterios.addCriteriosEqualString("A.sg_servico", sgServico);
		Search<CorreiosEtiqueta> search = new SearchBuilder<CorreiosEtiqueta>("mob_correios_etiqueta A")
				.fields("A.*")
				.searchCriterios(searchCriterios)
				.customConnection(this.customConnection)
				.build();
		List<CorreiosEtiqueta> etiquetaList = search.getList(CorreiosEtiqueta.class);
		if (etiquetaList.isEmpty()) {
			throw new ValidacaoException("Etiqueta: " + nrEtiqueta + " não encontrada");
		}
		CorreiosEtiqueta etiqueta = etiquetaList.get(0);
		return etiqueta;
	}

	public Arquivo saveArquivo(byte[] decodificando, ArquivoRetornoCorreiosDTO arquivoRetornoCorreios, CustomConnection customConnection) throws Exception {
		Arquivo arquivoBaixaBancaria = new ArquivoBuilder()
				.setBlbArquivo(decodificando)
				.setDtArquivamento(DateUtil.getDataAtual())
				.setDtCriacao(DateUtil.getDataAtual())
				.setNmDocumento("Retorno correios")
				.setNmArquivo(arquivoRetornoCorreios.getNmArquivo())
				.setNrRegistros(dadosRetornoCorreioDtoList.size())
				.setCdTipoArquivo(ArquivoTipoEnum.RETORNO_CORREIOS.getKey())
				.build();
		this.arquivoRepository.insert(arquivoBaixaBancaria, customConnection);
		return arquivoBaixaBancaria;
	}
	
	private void atualizarEtiqueta(CorreiosEtiqueta etiqueta, DadosRetornoCorreioDto dadosRetornoCorreioDto) throws Exception {
		etiqueta.setStAvisoRecebimento(dadosRetornoCorreioDto.getStEntrega());
		etiqueta.setDtAvisoRecebimento(dadosRetornoCorreioDto.getDataEntrega());
		this.correiosEtiquetaRepository.update(etiqueta, this.customConnection);
	}
	
	private void criarMovimentoDadosCorreios(int cdAit, int cdUsuario, DadosRetornoCorreioDto dadosRetornoCorreioDto) throws Exception {
		this.tpMovimentoDadosCorreios = getTpMovimento(cdAit);
		AitMovimento movimentoDadosCorreio = new AitMovimentoBuilder()
				.setCdAit(cdAit)
				.setDtMovimento(DateUtil.getDataAtual())
				.setTpStatus(this.tpMovimentoDadosCorreios)
				.setLgEnviadoDetran(TipoLgEnviadoDetranEnum.NAO_ENVIADO.getKey())
				.setDtDigitacao(new GregorianCalendar())
				.setCdUsuario(cdUsuario)
			.build();
		this.aitMovimentoRepository.insert(movimentoDadosCorreio, this.customConnection);
	}
	
	private int getTpMovimento(int cdAit) throws Exception {
		SearchCriterios searchCriterios = montarCriteriosNaNpAdvertencia(cdAit);
		List<AitMovimento> aitMovimentos = getMovimentos(searchCriterios);
		return obterTpStatusDadosCorreio(aitMovimentos.get(0).getTpStatus());
	}
	
	private int obterTpStatusDadosCorreio(int tpStatus) {
		if (tpStatus == TipoStatusEnum.NIP_ENVIADA.getKey() || tpStatus == TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey()) {
			return TipoStatusEnum.DADOS_CORREIO_NP.getKey();
		} else {
			return TipoStatusEnum.DADOS_CORREIO_NA.getKey();
		}
	}

	private List<AitMovimento> getMovimentos(SearchCriterios searchCriterios) throws Exception {
		Search<AitMovimento> search = new SearchBuilder<AitMovimento>("mob_ait_movimento")
				.searchCriterios(searchCriterios)
				.orderBy("dt_movimento DESC")
				.customConnection(this.customConnection)
				.build();
		List<AitMovimento> aitMovimento = search.getList(AitMovimento.class);
		return aitMovimento;
	}
	
	private void enviarMovimentoDadosCorreio(int cdAit, int tpStatus) throws Exception {
		this.servicoDetranServices = ServicoDetranServicesFactory.gerarServico();
		List<AitMovimento> aitMovimentoList = new ArrayList<AitMovimento>();
		AitMovimento aitMovimento = aitMovimentoRepository.getByStatus(cdAit, tpStatus, this.customConnection);
		if(aitMovimento == null) {
			throw new ValidacaoException("O movimento dados correio não foi encontrado.");
		}
		aitMovimentoList.add(aitMovimento);
		List<ServicoDetranDTO> remessa = servicoDetranServices.remessa(aitMovimentoList);
		verificarRetornoDetran(remessa.get(0));
	}
	
	private void verificarRetornoDetran(ServicoDetranDTO servicoDetranObjeto) throws ValidacaoException {
		if (servicoDetranObjeto.getCodigoRetorno() > 0)
			throw new ValidacaoException("Erro ao enviar movimento. Por favor verifique os envios ao Detran.");
	}
	
	private SearchCriterios montarCriteriosNaNpAdvertencia(int cdAit) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit);
		String tpStatus = String.valueOf(TipoStatusEnum.NAI_ENVIADO.getKey()) + ", "
				+ String.valueOf(TipoStatusEnum.NIP_ENVIADA.getKey()) + ", " + String.valueOf(TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey());
		searchCriterios.addCriterios("tp_status", tpStatus , ItemComparator.IN, Types.INTEGER);
		return searchCriterios;
	}
	
	private SearchCriterios monstarCriteriosDadosCorreios(int cdAit) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit);
		String tpStatus = String.valueOf(TipoStatusEnum.DADOS_CORREIO_NA.getKey()) + ", " + String.valueOf(TipoStatusEnum.DADOS_CORREIO_NP.getKey());
		searchCriterios.addCriterios("tp_status", tpStatus , ItemComparator.IN, Types.INTEGER);
		return searchCriterios;
	}
}