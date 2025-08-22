package com.tivic.manager.mob.ecarta.services;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoBuilder.ArquivoBuilder;
import com.tivic.manager.grl.arquivo.IArquivoService;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.CorreiosEtiqueta;
import com.tivic.manager.mob.ecarta.builders.CorreiosEtiquetaBuilder;
import com.tivic.manager.mob.ecarta.dtos.ArquivoEcartaDTO;
import com.tivic.manager.mob.ecarta.dtos.StatusLeituraArquivoRetornoDTO;
import com.tivic.manager.mob.ecarta.enums.CodigoCorreiosCepInvalidoEnum;
import com.tivic.manager.mob.ecarta.enums.TipoArquivoRetornoEnum;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoAitRepository;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoArquivoRepository;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoRepository;
import com.tivic.manager.mob.lote.impressao.LoteImpressaoArquivo;
import com.tivic.manager.mob.lote.impressao.LoteImpressaoBuilder;
import com.tivic.manager.mob.lote.impressao.TipoLoteNotificacaoEnum;
import com.tivic.manager.mob.lote.impressao.TipoRemessaCorreiosEnum;
import com.tivic.manager.mob.lote.impressao.TipoSituacaoLoteImpressaoEnum;
import com.tivic.manager.mob.lote.impressao.builders.LoteImpressaoArquivoBuilder;
import com.tivic.manager.mob.lote.impressao.enums.LoteImpressaoSituacaoEnum;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class ArquivoRetornoServiceEDI implements IArquivoRetornoServiceEDI {
	private CustomConnection customConnection;
	private ILoteImpressaoRepository loteImpressaoRepository;
	private ILoteImpressaoAitRepository loteImpressaoAitRepository;
	private IParametroRepository parametroRepository;
	private IArquivoService arquivoService;
	private ILoteImpressaoArquivoRepository loteImpressaoArquivoRepository;
	private int cdLoteImpressao;
	private int tpRemessaCorreios;
	private int tpDocumento;
	private EtiquetaCorreiosECT etiquetaCorreios;
	private LoteImpressao loteImpressaoInconsistete;
	private List<LoteImpressaoAit> loteImpressaoAitConsistenteList;
	private List<LoteImpressaoAit> loteImpressaoAitInonsistenteteList;
	private static final String NOME_DOCUMENTO_NAI = "NAI";
	private static final String NOME_DOCUMENTO_NIP = "NIP";
	private static final String TXT_ARQUIVO_RETORNO_INCONSISTENTE = "LOTE GERADO A PARTIR DA LEITURA DO ARQUIVO DE INCONSISTÊNCIAS DO CORREIO";
	
	public ArquivoRetornoServiceEDI() throws Exception {
		this.loteImpressaoRepository = (ILoteImpressaoRepository) BeansFactory.get(ILoteImpressaoRepository.class);
		this.loteImpressaoAitRepository = (ILoteImpressaoAitRepository) BeansFactory.get(ILoteImpressaoAitRepository.class);
		this.arquivoService = (IArquivoService) BeansFactory.get(IArquivoService.class);
		this.loteImpressaoArquivoRepository = (ILoteImpressaoArquivoRepository) BeansFactory.get(ILoteImpressaoArquivoRepository.class);
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
		this.etiquetaCorreios = new EtiquetaCorreiosECT();
		this.loteImpressaoInconsistete = new LoteImpressao();
	}

	@Override
	public StatusLeituraArquivoRetornoDTO processar(List<ArquivoEcartaDTO> arquivosRetorno, int cdLoteImpressao,
			int cdUsuario) throws Exception {
		this.customConnection = new CustomConnection();
		StatusLeituraArquivoRetornoDTO statusLeituraArquivoRetornoDTO;
		try {
			this.customConnection.initConnection(true);
			this.cdLoteImpressao = cdLoteImpressao;
			this.loteImpressaoAitConsistenteList = new ArrayList<LoteImpressaoAit>();
			this.loteImpressaoAitInonsistenteteList = new ArrayList<LoteImpressaoAit>();
			pegarModeloDocumento();
			lerArquivos(arquivosRetorno);
			atualizarLoteImpressaoAit(cdUsuario);
			statusLeituraArquivoRetornoDTO = atualizarLoteImpressao();
			salvarArquivoRetorno(arquivosRetorno, cdLoteImpressao, cdUsuario);
			this.customConnection.finishConnection();
		} finally {
			this.customConnection.closeConnection();
		}
		return statusLeituraArquivoRetornoDTO;
	}

	private void lerArquivos(List<ArquivoEcartaDTO> arquivosRetorno) throws Exception {
		for (ArquivoEcartaDTO arquivoRetorno : arquivosRetorno) {
			try (InputStream zipFileInputStream = new ByteArrayInputStream(arquivoRetorno.getBlbArquivo());
					ZipInputStream zipInputStream = new ZipInputStream(zipFileInputStream)) {
				lerArquivosZip(zipInputStream, arquivoRetorno.getTpRetorno());
			}
		}
	}

	private void lerArquivosZip(ZipInputStream zipInputStream, int tipoRetorno) throws Exception {
		ZipEntry entry;
		while ((entry = zipInputStream.getNextEntry()) != null) {
			if (!entry.isDirectory() && entry.getName().endsWith(".txt")) {
				lerConteudo(zipInputStream, tipoRetorno);
				zipInputStream.closeEntry();
			}
		}
	}

	private void lerConteudo(InputStream inputStream, int tpRetorno) throws IOException, Exception {
		List<CorreiosEtiqueta> correiosEtiqueta = new ArrayList<CorreiosEtiqueta>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		boolean possueEtiquetas = this.tpRemessaCorreios != TipoRemessaCorreiosEnum.CARTA_SIMPLES.getKey();
		String linha;
		while ((linha = reader.readLine()) != null && !linha.isEmpty()) {
			if (tpRetorno == TipoArquivoRetornoEnum.RECIBO.getKey()) {
				if (possueEtiquetas) {
					correiosEtiqueta.add(criarCorreiosEtiqueta(linha));
				}
				lerLinhaRecibo(linha);
			} else if (tpRetorno == TipoArquivoRetornoEnum.INCONSISTENCIA.getKey() && possuiInconsistencia(linha)) {
				lerLinhaInconsistencia(linha);
			}
		}
		if (!correiosEtiqueta.isEmpty()) {
			salvarEtiqueta(correiosEtiqueta);
		}
	}

	private CorreiosEtiqueta criarCorreiosEtiqueta(String linha) {
		return new CorreiosEtiquetaBuilder().build(linha, this.tpDocumento, this.cdLoteImpressao);
	}

	private boolean possuiInconsistencia(String linha) throws Exception {
		if (linha.startsWith("1")) {
			List<CodigoCorreiosCepInvalidoEnum> listaCodigoErros = Arrays
					.asList(CodigoCorreiosCepInvalidoEnum.values());
			String codInconsistencia = linha.split("\\p{Punct}")[2];
			boolean lgCepInvalido = this.parametroRepository
					.getValorOfParametroAsInt("ECT_LG_AUTORIZACAO_CEP_INVALIDO") == 1;
			return !(lgCepInvalido
					&& listaCodigoErros.stream().anyMatch(codErro -> codErro.getKey().equals(codInconsistencia)));
		}
		return false;
	}

	private void lerLinhaRecibo(String linha) throws Exception {
		int nrAit = Integer.parseInt(linha.split("\\p{Punct}")[1]);
		String txtErro = null;
		int stImpressao = TipoSituacaoLoteImpressaoEnum.ARQUIVO_CONSISTENTE.getKey();
		adicionarAitConsistente(nrAit, txtErro, stImpressao);
	}

	private void adicionarAitConsistente(int nrAit, String txtErro, int stImpressao) throws Exception {
		LoteImpressaoAit loteImpressaoAit = loteImpressaoAitRepository.get(this.cdLoteImpressao, nrAit,
				this.customConnection);
		if (loteImpressaoAit != null) {
			loteImpressaoAit.setTxtErro(txtErro);
			loteImpressaoAit.setStImpressao(stImpressao);
			loteImpressaoAit.setStImpressao(TipoSituacaoLoteImpressaoEnum.ARQUIVO_CONSISTENTE.getKey());
			this.loteImpressaoAitConsistenteList.add(loteImpressaoAit);
		}
	}

	private void lerLinhaInconsistencia(String linha) throws Exception {
		int nrAit = Integer.parseInt(linha.split("\\p{Punct}")[1]);
		String txtErro = linha.split("\\p{Punct}")[3];
		int stImpressao = TipoSituacaoLoteImpressaoEnum.ARQUIVO_INCONSISTENTE.getKey();
		adicionarAitInconsistente(nrAit, txtErro, stImpressao);
	}

	private void adicionarAitInconsistente(int nrAit, String txtErro, int stImpressao) throws Exception {
		LoteImpressaoAit loteImpressaoAit = this.loteImpressaoAitRepository.get(this.cdLoteImpressao, nrAit,
				this.customConnection);
		if (loteImpressaoAit != null) {
			loteImpressaoAit.setTxtErro(txtErro);
			loteImpressaoAit.setStImpressao(stImpressao);
			this.loteImpressaoAitInonsistenteteList.add(loteImpressaoAit);
		}

	}

	private void atualizarLoteImpressaoAit(int cdUsuario) throws Exception {
		if (!this.loteImpressaoAitConsistenteList.isEmpty()) {
			for (LoteImpressaoAit loteImpressaoAit : this.loteImpressaoAitConsistenteList) {
				this.loteImpressaoAitRepository.update(loteImpressaoAit, this.customConnection);
			}
		}
		if (!this.loteImpressaoAitConsistenteList.isEmpty() && !this.loteImpressaoAitInonsistenteteList.isEmpty()) {
			criarLoteInconsistente(cdUsuario);
			for (LoteImpressaoAit loteImpressaoAit : this.loteImpressaoAitInonsistenteteList) {
				this.loteImpressaoAitRepository.delete(loteImpressaoAit, this.customConnection);
				loteImpressaoAit.setCdLoteImpressao(this.loteImpressaoInconsistete.getCdLoteImpressao());
				this.loteImpressaoAitRepository.insert(loteImpressaoAit, this.customConnection);
			}
		}
	}

	private StatusLeituraArquivoRetornoDTO atualizarLoteImpressao() throws Exception {
		LoteImpressao loteImpressao = this.loteImpressaoRepository.get(this.cdLoteImpressao, this.customConnection);
		StatusLeituraArquivoRetornoDTO statusLeituraArquivoRetornoDTO = new StatusLeituraArquivoRetornoDTO();
		statusLeituraArquivoRetornoDTO.setCdLoteImpressao(this.cdLoteImpressao);
		if (!this.loteImpressaoAitConsistenteList.isEmpty()) {
			loteImpressao.setStLoteImpressao(LoteImpressaoSituacaoEnum.ECARTAS_LOTE_CONSISTENTE.getKey());
			statusLeituraArquivoRetornoDTO
					.setStLoteImpressao(LoteImpressaoSituacaoEnum.ECARTAS_LOTE_CONSISTENTE.getKey());
			if (this.loteImpressaoAitInonsistenteteList.isEmpty()) {
				statusLeituraArquivoRetornoDTO.setTxtStLoteImpressao("Status: Lote Consistente");
			} else {
				statusLeituraArquivoRetornoDTO.setTxtStLoteImpressao("Status: Um novo lote inconsistente foi gerado");
			}
		} else if (!this.loteImpressaoAitInonsistenteteList.isEmpty()) {
			loteImpressao.setStLoteImpressao(LoteImpressaoSituacaoEnum.ECARTAS_LOTE_INVALIDO.getKey());
			statusLeituraArquivoRetornoDTO.setStLoteImpressao(LoteImpressaoSituacaoEnum.ECARTAS_LOTE_INVALIDO.getKey());
			statusLeituraArquivoRetornoDTO.setTxtStLoteImpressao("Status: Lote Inválido");
		}
		this.loteImpressaoRepository.update(loteImpressao, this.customConnection);
		return statusLeituraArquivoRetornoDTO;
	}

	private void salvarEtiqueta(List<CorreiosEtiqueta> correiosEtiqueta) throws Exception {
		this.etiquetaCorreios.salvar(correiosEtiqueta, this.cdLoteImpressao, this.customConnection);
	}

	private void pegarModeloDocumento() throws Exception {
		LoteImpressao loteImpressao = this.loteImpressaoRepository.get(this.cdLoteImpressao, this.customConnection);
		this.tpDocumento = loteImpressao.getTpDocumento();
		this.tpRemessaCorreios = this.tpDocumento == TipoLoteNotificacaoEnum.LOTE_NAI.getKey()
				? this.parametroRepository.getValorOfParametroAsInt("MOB_TIPO_ENVIO_CORREIOS_NAI")
				: this.parametroRepository.getValorOfParametroAsInt("MOB_TIPO_ENVIO_CORREIOS_NIP");
	}

	private void criarLoteInconsistente(int cdUsuario) throws Exception {
		LoteImpressao loteImpressao = this.loteImpressaoRepository.get(this.cdLoteImpressao, this.customConnection);
		String nomeDocumento = loteImpressao.getTpDocumento() == TipoLoteNotificacaoEnum.LOTE_NAI.getKey()
				? NOME_DOCUMENTO_NAI
				: NOME_DOCUMENTO_NIP;
		String idLoteImpressao = criarIdLoteImpressao(nomeDocumento);
		this.loteImpressaoInconsistete = new LoteImpressaoBuilder()
				.addCdUsuario(cdUsuario)
				.addDtCriacao(new GregorianCalendar())
				.addIdLoteImpressao(idLoteImpressao)
				.addTpDestino(loteImpressao.getTpDestino())
				.addTpLoteImpressao(loteImpressao.getTpLoteImpressao())
				.addTxtObservacao(TXT_ARQUIVO_RETORNO_INCONSISTENTE)
				.addStLoteImpressao(LoteImpressaoSituacaoEnum.ECARTAS_LOTE_INCONSISTENTE.getKey())
				.addTpDocumento(loteImpressao.getTpDocumento())
				.build();
		this.loteImpressaoInconsistete = this.loteImpressaoRepository.insert(loteImpressaoInconsistete,
				this.customConnection);
	}

	private String criarIdLoteImpressao(String nomeDocumento) {
		LocalDateTime now = LocalDateTime.now();
		return String.format(nomeDocumento + "/%s", now.format(DateTimeFormatter.ofPattern("ddMMHHmm")));
	}

	private void salvarArquivoRetorno(List<ArquivoEcartaDTO> arquivosRetorno, int cdLoteImpressao, int cdUsuario)
			throws Exception {
		for (ArquivoEcartaDTO arquivo : arquivosRetorno) {
			int cdTipoArquivo = arquivo.getTpRetorno() == TipoArquivoRetornoEnum.RECIBO.getKey()
					? parametroRepository.getValorOfParametroAsInt("TP_ARQUIVO_RECIBO", this.customConnection)
					: parametroRepository.getValorOfParametroAsInt("TP_ARQUIVO_INCONSISTENCIA", this.customConnection);
			String nomeArquivo = arquivo.getNmArquivo();
			String nomeSemExtensao = nomeArquivo.replaceAll("\\.zip$", "");
			Arquivo arquivoRetorno = new ArquivoBuilder()
					.setBlbArquivo(arquivo.getBlbArquivo())
					.setCdUsuario(cdUsuario)
					.setNmArquivo(nomeArquivo)
					.setNmDocumento(nomeSemExtensao)
					.setDtArquivamento(new GregorianCalendar())
					.setDtCriacao(new GregorianCalendar())
					.setCdTipoArquivo(cdTipoArquivo)
					.build();
			this.arquivoService.save(arquivoRetorno);
			vincularArquivoRetornoLote(cdLoteImpressao, arquivoRetorno);
		}
	}

	private void vincularArquivoRetornoLote(int cdLoteImpressao, Arquivo arquivoNotificacao) throws Exception {
		LoteImpressaoArquivo loteImpressaoArquivo = new LoteImpressaoArquivoBuilder()
				.setCdArquivo(arquivoNotificacao.getCdArquivo())
				.setCdLoteImpressao(cdLoteImpressao).build();
		loteImpressaoArquivoRepository.insert(loteImpressaoArquivo, this.customConnection);
	}
}
