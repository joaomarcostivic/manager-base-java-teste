package com.tivic.manager.mob.pagamento;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.ArquivoBuilder.ArquivoBuilder;
import com.tivic.manager.grl.arquivo.IArquivoRepository;
import com.tivic.manager.mob.AitPagamento;
import com.tivic.manager.mob.ArquivoBancoDTO;
import com.tivic.manager.mob.RetornoBancoDTO;
import com.tivic.manager.mob.aitpagamento.AitPagamentoRepository;
import com.tivic.manager.mob.aitpagamento.enums.TipoRecebimentoPagamentoEnum;
import com.tivic.manager.mob.pagamento.builders.BancoDadosRetornoBuilder;
import com.tivic.manager.mob.pagamento.builders.RetornoPagamentoBuilder;
import com.tivic.manager.util.Util;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.util.date.DateUtil;

import sol.dao.ItemComparator;

public class AitPagamentoArquivo {

	private List<RetornoBancoDTO> retornosBancoDTO;
	private IArquivoRepository arquivoRepository;
	private BancoDadosRetorno bancoDadosRetorno;
	public SimpleDateFormat sdf;
	private String nrBanco = "";
	private String line = "";
	private String nmArquivo;
	private byte[] decoded;
	private AitPagamentoRepository aitPagamentoRepository;
	private IRealizaPagamento realizaPagamento;
	private ManagerLog managerLog;

	public AitPagamentoArquivo() throws Exception {
		this.aitPagamentoRepository = (AitPagamentoRepository) BeansFactory.get(AitPagamentoRepository.class);
		this.arquivoRepository = (IArquivoRepository) BeansFactory.get(IArquivoRepository.class);
		this.retornosBancoDTO = new ArrayList<RetornoBancoDTO>();
		this.bancoDadosRetorno = new BancoDadosRetorno();
		this.sdf = new SimpleDateFormat("yyyyMMdd");
		this.realizaPagamento = (IRealizaPagamento) BeansFactory.get(IRealizaPagamento.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}

	public List<RetornoBancoDTO> lerArquivo(ArquivoBancoDTO arquivoBanco, int cdUsuario) throws Exception {
		managerLog.info("LEITURA DE ARQUIVO INICIADA: ", arquivoBanco.getNmArquivo() + " EM: " 
	+ new GregorianCalendar().getTime().toString());
		if (arquivoBanco.getArquivo().contains("application/octet-stream"))
			arquivoBanco.setArquivo(arquivoBanco.getArquivo().replace("data:application/octet-stream;base64,", ""));
		else
			arquivoBanco.setArquivo(arquivoBanco.getArquivo().replace("data:text/plain;base64,", ""));
		this.decoded = Base64.getMimeDecoder().decode(arquivoBanco.getArquivo());
		List<RetornoBancoDTO> retornoBancoList = processar(cdUsuario);
		managerLog.info("LEITURA DE ARQUIVO FINALIZADA: ", arquivoBanco.getNmArquivo() + " EM: " + new GregorianCalendar().getTime().toString());
		return retornoBancoList;
	}

	private List<RetornoBancoDTO> processar(int cdUsuario) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			BufferedReader reader = byteToBuffered();
			Arquivo arquivoBaixaBancaria = saveArquivo(decoded, customConnection);
			while (reader != null && (line = reader.readLine()) != null) {
				lerLinhaPagamento(arquivoBaixaBancaria.getCdArquivo(), cdUsuario, customConnection);
			}
			customConnection.finishConnection();
			
			return this.retornosBancoDTO;
		} finally {
			customConnection.closeConnection();
		}
	}

	public BufferedReader byteToBuffered() {
		BufferedReader reader = this.decoded == null ? null
				: new BufferedReader(new InputStreamReader(new ByteArrayInputStream(this.decoded)));
		return reader;
	}
	
	public Arquivo saveArquivo(byte[] decoded, CustomConnection customConnection) throws Exception {
		Arquivo arquivoBaixaBancaria = new ArquivoBuilder()
				.setBlbArquivo(decoded)
				.setDtArquivamento(new GregorianCalendar())
				.setDtCriacao(new GregorianCalendar())
				.setNmDocumento("Arquivo de Baixa Bancária")
				.setNmArquivo(this.nmArquivo)
				.setCdTipoArquivo(Integer.valueOf(ParametroServices.getValorOfParametro("MOB_TIPO_ARQUIVO_BAIXA_BANCARIA")))
			.build();
		managerLog.info("SALVANDO ARQUIVO", "");
		this.arquivoRepository.insert(arquivoBaixaBancaria, customConnection);
		return arquivoBaixaBancaria;
	}

	public void lerLinhaPagamento(int cdArquivo, int cdUsuario, CustomConnection customConnection) throws Exception {
		if (line.charAt(0) == ('A')) {
			nrBanco = line.substring(42, 45);
			managerLog.info("LENDO NÚMERO DE BANCO:  ", nrBanco);
		}
		if (line.charAt(0) == ('G')) {
			this.retornosBancoDTO.add(lerRetornoPagamento(cdArquivo, cdUsuario, customConnection));
		}
	}

	public RetornoBancoDTO lerRetornoPagamento(int cdArquivo, int cdUsuario, CustomConnection customConnection) throws Exception {
		setBancoDadosRetorno(customConnection);
	    RetornoBancoDTO retornoBancoDTO = new RetornoBancoDTO();
		managerLog.info("PROCESSANDO PAGAMENTO PARA O NÚMERO DE CONTROLE: ", bancoDadosRetorno.getNrControleComZeros());
		List<PagamentoDTO> aitPagamentos = buscarAitPagamento(this.bancoDadosRetorno);
		verificarAitPagamentos(aitPagamentos, retornoBancoDTO);
		if (retornoBancoDTO.getStImportacao() != null) {
	        return retornoBancoDTO;
	    }
		PagamentoDTO aitPagamentoDTO = aitPagamentos.get(0);
		aitPagamentoDTO.setCdArquivo(cdArquivo);
		retornoBancoDTO = aitPagamentoDTO.getCdAit() <= 0
	            ? nrControleNaoEncontrado(this.bancoDadosRetorno)
	            : pagar(aitPagamentoDTO, this.bancoDadosRetorno, cdUsuario, customConnection);
	    return retornoBancoDTO;
	}
	
	private void setBancoDadosRetorno(CustomConnection customConnection) throws Exception {
		this.bancoDadosRetorno = new BancoDadosRetornoBuilder()
				.setVlTarifa(Double.parseDouble(line.substring(93, 98) + "." + line.substring(98, 100)))
				.setNrBanco(nrBanco).setNrAgencia(line.substring(108, 116).trim().replace(" ", ""))
				.setNrAgenciaCredito(line.substring(1, 6).trim().replace(" ", ""))
				.setNrContaCredito((line.substring(6, 21).trim().replace(" ", "")))
				.setTpArrecadacao(Integer.valueOf((line.substring(116, 117))))
				.setTpPagamento(TipoRecebimentoPagamentoEnum.PAGAMENTO_FINALIZADO.getKey())
				.setVlPago(Double.parseDouble(line.substring(81, 91) + "." + line.substring(91, 93)))
				.setNrControleSemZeros(line.substring(62, 71).replaceAll("\\b0+(?!\\b)", ""))
				.setDtPagamento(Util.convDateToCalendar(sdf.parse(line.substring(21, 29))))
				.setDtCredito(Util.convDateToCalendar(sdf.parse(line.substring(29, 37))))
				.setNrCodigoBarras(line.substring(37, 81).replaceAll("\\b0+(?!\\b)", ""))
				.setNrControleComZeros(line.substring(62, 71))
				.setNrPlaca(String.valueOf(line.charAt(61)))
			.build();
	}
	
	private List<PagamentoDTO> buscarAitPagamento(BancoDadosRetorno bancoDadosRetorno) throws Exception {
		managerLog.info("BUSCANDO PAGAMENTO: ", bancoDadosRetorno.getNrControleComZeros());
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriterios("A.nr_placa", "%" + bancoDadosRetorno.getNrPlaca(), ItemComparator.LIKE, Types.VARCHAR);
		Search<PagamentoDTO> search = new SearchBuilder<PagamentoDTO>("mob_ait A")
				.addJoinTable("LEFT JOIN mob_ait_pagamento B ON (B.cd_ait = A.cd_ait)")
				.searchCriterios(searchCriterios)
				.additionalCriterias(" A.nr_controle = '" + bancoDadosRetorno.getNrControleSemZeros() + "' "
									+" OR A.nr_controle = '" + bancoDadosRetorno.getNrControleComZeros() + "' ")
				.build();
		return search.getList(PagamentoDTO.class);
	}
	
	private void verificarAitPagamentos(List<PagamentoDTO> aitPagamentos, RetornoBancoDTO retornoBancoDTO) {
	    if (aitPagamentos.isEmpty()) {
	        managerLog.info("NENHUM AIT ENCONTRADO COM O NÚMERO DE CONTROLE: ", bancoDadosRetorno.getNrControleComZeros());
	        retornoBancoDTO.setStImportacao("Nenhum AIT encontrado com o número de controle: " + bancoDadosRetorno.getNrControleComZeros());
	        retornoBancoDTO.setNrControle(bancoDadosRetorno.getNrControleSemZeros());
	    } else if(aitPagamentos.size() > 1	&& aitPagamentos.get(0).cdAit != aitPagamentos.get(1).cdAit) {
	        managerLog.info("AIT COM NÚMERO DE CONTROLE DUPLICADO: ", bancoDadosRetorno.getNrControleComZeros());
	        retornoBancoDTO.setIdAit(aitPagamentos.get(0).idAit + "/" + aitPagamentos.get(1).idAit);
			retornoBancoDTO.setStImportacao("AIT com número de controle duplicado: "
					+ bancoDadosRetorno.getNrControleComZeros() + ". Favor fazer baixa manual.");
	        retornoBancoDTO.setNrControle(bancoDadosRetorno.getNrControleSemZeros());
	    }
	}
	
	
	private RetornoBancoDTO nrControleNaoEncontrado(BancoDadosRetorno bancoDadosRetorno) {
		RetornoBancoDTO nrControleNaoEncontrado = new RetornoBancoDTO();
		managerLog.info("NÚMERO DE CONTROLE NÃO ENCONTRADO: ", bancoDadosRetorno.getNrControleSemZeros());
		nrControleNaoEncontrado.setStImportacao("Numero de controle não encontrado: " + bancoDadosRetorno.getNrControleSemZeros());
		nrControleNaoEncontrado.setNrControle(bancoDadosRetorno.getNrControleSemZeros());
		return nrControleNaoEncontrado;
	}
	
	private RetornoBancoDTO pagar(PagamentoDTO pagamentoDTO, BancoDadosRetorno bancoDadosRetorno, int cdUsuario, CustomConnection customConnection) throws Exception {
		managerLog.info("PROCESSANDO PAGAMENTO PARA O AIT: ", pagamentoDTO.getIdAit());
		if (pagamentoDTO.getCdPagamento() == 0) {
			return realizaPagamento.pagar(pagamentoDTO, bancoDadosRetorno, cdUsuario, customConnection);
		} else {
			return existePagamento(pagamentoDTO, bancoDadosRetorno, cdUsuario, customConnection);
		}
	}

	private RetornoBancoDTO existePagamento(PagamentoDTO pagamentoDTO,BancoDadosRetorno bancoDadosRetorno, int cdUsuario, CustomConnection customConnection) throws Exception {
		boolean isDuplicado = verificarDuplicidade(pagamentoDTO, bancoDadosRetorno, customConnection);
		if (isDuplicado) {
			managerLog.info("PAGAMENTO DUPLICADO: ", pagamentoDTO.getIdAit());
			this.realizaPagamento.registrarDuplicado(pagamentoDTO, bancoDadosRetorno, cdUsuario, customConnection);
		}
		RetornoBancoDTO existePagamentoBuilder = new RetornoPagamentoBuilder()
				.setStImportacao(isDuplicado ? "Registro de pagamento duplicado" : "Já existe pagamento para o AIT")
				.setNrControle(bancoDadosRetorno.getNrControleSemZeros())
				.setDtPagamento(bancoDadosRetorno.getDtPagamento())
				.setNmBanco(bancoDadosRetorno.getNrBanco())
				.setVlPago(bancoDadosRetorno.getVlPago())
				.setIdAit(pagamentoDTO.getIdAit())
				.build();
		managerLog.info("O PAGAMENTO JÁ EXISTE: ", pagamentoDTO.getIdAit());
		return existePagamentoBuilder;
	}

	private boolean verificarDuplicidade(PagamentoDTO pagamentoDTO, BancoDadosRetorno bancoDadosRetorno, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("nr_agencia", bancoDadosRetorno.getNrAgencia());
		searchCriterios.addCriteriosGreaterDate("dt_pagamento", DateUtil.formatDate(bancoDadosRetorno.getDtPagamento(), "yyyy-MM-dd"));
		searchCriterios.addCriteriosMinorDate("dt_pagamento", DateUtil.formatDate(bancoDadosRetorno.getDtPagamento(), "yyyy-MM-dd"));
		searchCriterios.addCriteriosEqualInteger("cd_ait", pagamentoDTO.getCdAit());
		searchCriterios.addCriterios("vl_pago", String.valueOf(bancoDadosRetorno.getVlPago()), ItemComparator.EQUAL, Types.DOUBLE);
		List<AitPagamento> listPagamento = aitPagamentoRepository.find(searchCriterios, customConnection);
		return listPagamento.isEmpty();
	}
}