package com.tivic.manager.mob.lote.impressao.remessacorreios.arquivospostagem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.ArquivoBuilder.ArquivoBuilder;
import com.tivic.manager.grl.arquivo.IArquivoRepository;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.lote.impressao.DadosNotificacao;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoArquivoRepository;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoRepository;
import com.tivic.manager.mob.lote.impressao.LoteImpressaoArquivo;
import com.tivic.manager.mob.lote.impressao.builders.LoteImpressaoArquivoBuilder;
import com.tivic.manager.mob.lote.impressao.remessacorreios.DadosDocumento;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.manipulatefiles.IGenerateFile;
import com.tivic.manager.util.manipulatefiles.IManipulateFolder;
import com.tivic.manager.util.manipulatefiles.PathFolderBuilder;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.util.date.DateUtil;

public class ArquivoPrevisaoPostagem implements IArquivoPostagem{
	
	private CustomConnection customConnection;
	private DadosDocumento dadosDocumento;
	private IManipulateFolder manipulateFolder;
	private IGenerateFile generateFile;
	private IArquivoRepository arquivoRepository;
	private ILoteImpressaoRepository loteImpressaoRepository;
	private ILoteImpressaoArquivoRepository loteImpressaoArquivoRepository;
	private String path;
	private Runtime runtime;
	private int nrSequencial;
	private BufferedWriter bufferedWrite;
	private String pathFile;
	private String nmArquivo;
	private FileOutputStream file;
	private String codigoCliente;
	private String sgCliente;
	
	public ArquivoPrevisaoPostagem(DadosDocumento dadosDocumento, CustomConnection customConnection) throws Exception {
		this.customConnection = customConnection;
		this.dadosDocumento = dadosDocumento;
		this.manipulateFolder = (IManipulateFolder) BeansFactory.get(IManipulateFolder.class);
		this.generateFile = (IGenerateFile) BeansFactory.get(IGenerateFile.class);
		this.arquivoRepository = (IArquivoRepository) BeansFactory.get(IArquivoRepository.class);
		this.loteImpressaoRepository = (ILoteImpressaoRepository) BeansFactory.get(ILoteImpressaoRepository.class);
		this.loteImpressaoArquivoRepository = (ILoteImpressaoArquivoRepository) BeansFactory.get(ILoteImpressaoArquivoRepository.class);
		this.runtime = Runtime.getRuntime();
	}
	
	@Override
	public Arquivo gerar() throws Exception {
		prepararDadosArquivo();
		prepararArquivo();
		escreverCabeçalho();
		escreverDados();
		fecharArquivo();
		byte[] arquivoPostagemBytes = this.generateFile.convertFileToByte(new File(this.pathFile));
		Arquivo arquivoPostagem = new ArquivoBuilder()
				.setBlbArquivo(arquivoPostagemBytes)
				.setDtArquivamento(new GregorianCalendar())
				.setDtCriacao(new GregorianCalendar())
				.setNmDocumento("Arquivo De Previsão Postagem")
				.setNmArquivo(this.nmArquivo)
				.setCdTipoArquivo(Integer.valueOf(ParametroServices.getValorOfParametro("MOB_TIPO_ARQUIVO_PREVISAO_POSTAGEM_CORREIOS")))
				.build();
		this.arquivoRepository.insert(arquivoPostagem, customConnection);
		vincularArquivoPrevisaoPostagem(arquivoPostagem);
		this.runtime.gc();
		this.manipulateFolder.removeAllFiles(new File(this.path));
		return arquivoPostagem;
	}
	
	private void prepararDadosArquivo() throws IllegalArgumentException, Exception {
		this.nrSequencial = pegarNrSequencialRemessa();
		this.codigoCliente = ParametroServices.getValorOfParametro("MOB_CORREIOS_NR_CODIGO_CLIENTE", this.customConnection.getConnection());
		this.sgCliente = ParametroServices.getValorOfParametro("MOB_CORREIOS_SG_CLIENTE", this.customConnection.getConnection());
		this.nmArquivo = nomearArquivo();
	}
	
	private void prepararArquivo() throws FileNotFoundException {
		this.path = ManagerConf.getInstance().get("TOMCAT_WORK_DIR", new PathFolderBuilder()
						.add("")
						.add("tivic")
						.add("work")
						.getPath())
				  + new PathFolderBuilder()
				  		.add("PrevisaoPostagem_" + this.dadosDocumento.getCdLoteNotificacao())
				  		.getPath();
		this.manipulateFolder.createFolder(this.path);
		this.pathFile = path + this.nmArquivo;
		this.file = new FileOutputStream(this.pathFile);
		this.bufferedWrite = new BufferedWriter(new OutputStreamWriter(this.file));
	}
	
	private int pegarNrSequencialRemessa() throws IllegalArgumentException, Exception {
		SearchCriterios searchCriterios = criteriosArquivosPrevisaoPostagem();
		List<Arquivo> arquivoList = this.arquivoRepository.find(searchCriterios);
		int nrSequencialRemessa = arquivoList.size() + 1;
		return nrSequencialRemessa;
	}
	
	private SearchCriterios criteriosArquivosPrevisaoPostagem() {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_tipo_arquivo", Integer.valueOf(ParametroServices.getValorOfParametro("MOB_TIPO_ARQUIVO_PREVISAO_POSTAGEM_CORREIOS")));
		searchCriterios.addCriteriosGreaterDate("dt_criacao", DateUtil.formatDate(new GregorianCalendar(), "yyyy-MM-dd"));
		searchCriterios.addCriteriosMinorDate("dt_criacao", DateUtil.formatDate(new GregorianCalendar(), "yyyy-MM-dd"));
		return searchCriterios;
	}
	
	private String nomearArquivo() {
		String diaMes = DateUtil.formatDate(DateUtil.getDataAtual(), "ddMM");
		String extensao = ".SD1";
		String nameFile = this.sgCliente 
						+ ArquivoPrevisaoPostagemEnum.FIXO_NOME_ARQUIVO.getKey() 
						+ diaMes 
						+ this.nrSequencial 
						+ extensao;
		return nameFile;
	}
	
	private void escreverCabeçalho() throws IOException {
		String zerosFillerPosicao6 = gerarZerosFiller(15);
		String dataGeracao = DateUtil.formatDate(new GregorianCalendar(), "yyyyMMdd");
		int quantidadeRegistros = this.dadosDocumento.getDadosNotificacaoList().size() + ArquivoPrevisaoPostagemEnum.NR_SEQUENCIAL_HEADER.getKey();
		int qtdzerosFillerPosicao75 =  this.sgCliente.length() > 2 ? 100 : 94;
		String zerosFillerPosicao75 = gerarZerosFiller(qtdzerosFillerPosicao75);
		String header = ArquivoPrevisaoPostagemEnum.TP_REGISTRO_HEADER.getKey()
					  + this.codigoCliente
					  + zerosFillerPosicao6
					  + pegarNmCliente()
					  + dataGeracao
					  + Util.fillNum(quantidadeRegistros, 6)
					  + zerosFillerPosicao75
					  + Util.fillNum(this.nrSequencial, 5)
					  + Util.fillNum(ArquivoPrevisaoPostagemEnum.NR_SEQUENCIAL_HEADER.getKey(), 7);
		this.bufferedWrite.append(header + "\n");
	}
	
	private String gerarZerosFiller(int quantidadeZeros) {
		String zerosFiller = "";
		for (int i = 0; i < quantidadeZeros; i++) {
			zerosFiller += "0";
		}
		return zerosFiller;
	}
	
	private String pegarNmCliente() {
		String nmCliente = ParametroServices.getValorOfParametro("NM_DEPARTAMENTO", this.customConnection.getConnection());
		if (nmCliente.length() < 40) {
			nmCliente = completarDadoComEspaco(nmCliente, 40);
		}
		else if (nmCliente.length() > 40) {
			nmCliente = nmCliente.substring(0, 40);
		}
		return nmCliente;
	}
	
	private void escreverDados() throws Exception {
		String identificaoCliente = pegarIdentificacaoCliente();
		String zerosFillerPosicao161 = gerarZerosFiller(8);
		int nrSequencialRegistro = ArquivoPrevisaoPostagemEnum.NR_INICIAL_SEQUENCIAL_DADOS.getKey();
		for (DadosNotificacao dadosNotificacao : this.dadosDocumento.getDadosNotificacaoList()) {
			String dados = ArquivoPrevisaoPostagemEnum.TP_REGISTRO_DADOS.getKey()
					     + this.codigoCliente
					     + identificaoCliente
						 + dadosNotificacao.getCodeSG()
						 + ArquivoPrevisaoPostagemEnum.CODIGO_OPERACAO.getKey()
						 + identificarNotificacao(dadosNotificacao)
						 + pegarNomeDestinatario(dadosNotificacao)
						 + pegarEnderecoDestinatario(dadosNotificacao)
						 + pegarCidadeProprietario(dadosNotificacao)
						 + pegarUFDestinatario(dadosNotificacao)
						 + pegarCEPDestinatario(dadosNotificacao)
						 + zerosFillerPosicao161
						 + Util.fillNum(this.nrSequencial, 5)
						 + Util.fillNum(nrSequencialRegistro, 7);
			nrSequencialRegistro++;
			this.bufferedWrite.append(dados + "\n");
		}
	}
	
	private String pegarIdentificacaoCliente() {
		String sgCliente = this.sgCliente;
		if (sgCliente.length() > 2) {
			sgCliente = completarDadoComEspaco(sgCliente, 8);
		}
		return sgCliente;
	}
	
	private String identificarNotificacao(DadosNotificacao dadosNotificacao) throws Exception {
		LoteImpressao loteImpressao = this.loteImpressaoRepository.get(this.dadosDocumento.getCdLoteNotificacao(), this.customConnection);
		String idNotificacao = TipoNotificacaoPostagemEnum.valueOf(loteImpressao.getTpDocumento())
							 + " " 
							 + dadosNotificacao.getIdAit()
							 + "  ";
		return idNotificacao;
	}
	
	private String pegarNomeDestinatario(DadosNotificacao dadosNotificacao) {
		String nomeDestinatario = dadosNotificacao.getNmProprietario() != null ? dadosNotificacao.getNmProprietario() : "";
		if (nomeDestinatario.length() > 40) {
			nomeDestinatario = nomeDestinatario.substring(0, 40);
		}
		else if (nomeDestinatario.length() < 40) {
			nomeDestinatario = completarDadoComEspaco(nomeDestinatario, 40);
		}
		return nomeDestinatario;
	}
	
	private String pegarEnderecoDestinatario(DadosNotificacao dadosNotificacao) {
		String endereco = "";
		endereco += dadosNotificacao.getDsLogradouro() != null ? dadosNotificacao.getDsLogradouro() : "";
		endereco += ", ";
		endereco += dadosNotificacao.getDsNrImovel() != null ? dadosNotificacao.getDsNrImovel() : "";
		endereco += ", ";
		endereco += dadosNotificacao.getNmBairro() != null ? dadosNotificacao.getNmBairro() : "";
		if (endereco.length() > 40) {
			endereco = endereco.substring(0, 40);
		} 
		else if (endereco.length() < 40) {
			endereco = completarDadoComEspaco(endereco, 40);
		}
		return endereco;
	}
	
	private String pegarCidadeProprietario(DadosNotificacao dadosNotificacao) {
		String cidadeProprietario = dadosNotificacao.getCidadeProprietario() != null ? dadosNotificacao.getCidadeProprietario() : "";
		if (cidadeProprietario.length() > 30) {
			cidadeProprietario = cidadeProprietario.substring(0, 30);
		}
		else if (cidadeProprietario.length() < 30) {
			cidadeProprietario = completarDadoComEspaco(cidadeProprietario, 30);
		}
		return cidadeProprietario;
	}
	
	private String pegarUFDestinatario(DadosNotificacao dadosNotificacao) {
		String ufDestinario = dadosNotificacao.getUfProprietario() != null ? dadosNotificacao.getUfProprietario() : "";
		if (ufDestinario.length() < 2) {
			ufDestinario = completarDadoComEspaco(ufDestinario, 2);
		} else if (ufDestinario.length() > 2) {
			ufDestinario.substring(0, 2);
		}
		return ufDestinario;
	}
	
	private String pegarCEPDestinatario(DadosNotificacao dadosNotificacao) {
		String nrCep = dadosNotificacao.getNrCep() != null ? dadosNotificacao.getNrCep() : "";
		if (nrCep.length() > 8) {
			nrCep.substring(0, 8);
		} else if (nrCep.length() < 8) {
			nrCep = completarDadoComEspaco(nrCep, 8);
		}
		return nrCep;
	}
	
	private String completarDadoComEspaco(String dado, int tamanhoFinal) {
		String dadoComplementado = dado;
		for (int i = dado.length(); i < tamanhoFinal; i++) {
			dadoComplementado += " ";
		}
		return dadoComplementado;
	}
	
	private void fecharArquivo() throws IOException {
		this.bufferedWrite.flush();
		this.bufferedWrite.close();	
		this.file.flush();
		this.file.close();
		this.runtime.gc();
	}
	
	private void vincularArquivoPrevisaoPostagem(Arquivo arquivoPrevisaoPostagem) throws Exception {
		LoteImpressaoArquivo loteImpressaoArquivo = new LoteImpressaoArquivoBuilder()
				.setCdArquivo(arquivoPrevisaoPostagem.getCdArquivo())
				.setCdLoteImpressao(this.dadosDocumento.getCdLoteNotificacao())
				.build();
		loteImpressaoArquivoRepository.insert(loteImpressaoArquivo, this.customConnection);
	}
	
}