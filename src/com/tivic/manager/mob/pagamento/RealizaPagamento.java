package com.tivic.manager.mob.pagamento;

import java.sql.Types;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.AitPagamento;
import com.tivic.manager.mob.RetornoBancoDTO;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.aitmovimentobuilder.AitMovimentoBuilder;
import com.tivic.manager.mob.aitpagamento.AitPagamentoRepository;
import com.tivic.manager.mob.aitpagamento.builders.AitPagamentoBuilder;
import com.tivic.manager.mob.aitpagamento.enums.SituacaoPagamentoEnum;
import com.tivic.manager.mob.aitpagamento.enums.TipoPagamentoEnum;
import com.tivic.manager.mob.pagamento.builders.RetornoPagamentoBuilder;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class RealizaPagamento implements IRealizaPagamento {

	private AitMovimentoRepository aitMovimentoRepository;
	private AitPagamentoRepository aitPagamentoRepository;
	private AitRepository aitRepository;
	private ManagerLog managerLog;

	public RealizaPagamento() throws Exception {
		aitPagamentoRepository = (AitPagamentoRepository) BeansFactory.get(AitPagamentoRepository.class);
		aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}

	public RetornoBancoDTO pagar(PagamentoDTO pagamentoDTO, BancoDadosRetorno bancoDadosRetorno, int cdUsuario,	CustomConnection customConnection) throws Exception {
		Ait ait = aitRepository.get(pagamentoDTO.getCdAit(), customConnection);
		AitPagamento aitPagamento = montarAitPagamento(pagamentoDTO, bancoDadosRetorno);
		aitPagamentoRepository.insert(aitPagamento, customConnection);
		ait.setLgDetranFebraban(TipoPagamentoEnum.PAGO_VIA_BOLETO.getKey());
		ait.setNrCodigoBarras(bancoDadosRetorno.getNrCodigoBarra());
		aitRepository.update(ait, customConnection);
		realizarMovimento(aitPagamento, pagamentoDTO, cdUsuario, customConnection);
		managerLog.info("IMPORTAÇÃO DE PAGAMENTO REALIZADA: ", pagamentoDTO.getIdAit());
		return montarRetornoBancoDTO(ait, bancoDadosRetorno, customConnection);
	}	

	private AitPagamento montarAitPagamento(PagamentoDTO pagamentoDTO, BancoDadosRetorno bancoDadosRetorno)	throws Exception {
		AitPagamento aitPagamento = new AitPagamentoBuilder()
				.setVlTarifa(bancoDadosRetorno.getVlTarifa())
				.setNrBanco(bancoDadosRetorno.getNrBanco())
				.setNrAgencia(bancoDadosRetorno.getNrAgencia())
				.setNrAgenciaCredito(bancoDadosRetorno.getNrAgenciaCredito())
				.setNrContaCredito(bancoDadosRetorno.getNrContaCredito())
				.setTpArrecadacao(bancoDadosRetorno.getTpArrecadacao())
				.setTpPagamento(bancoDadosRetorno.getTpPagamento())
				.setVlPago(bancoDadosRetorno.getVlPago())
				.setDtPagamento(bancoDadosRetorno.getDtPagamento())
				.setDtCredito(bancoDadosRetorno.getDtCredito())
				.setCdArquivo(pagamentoDTO.getCdArquivo())
				.setCdAit(pagamentoDTO.getCdAit())
				.setStPagamento(SituacaoPagamentoEnum.RECEBIDO.getKey())
				.build();
		return aitPagamento;
	}
	
	private void realizarMovimento(AitPagamento aitPagamento, PagamentoDTO pagamentoDTO, int cdUsuario, CustomConnection customConnection) throws Exception {
		GregorianCalendar dataAtual = new GregorianCalendar();
		AitMovimento aitMovimento = new AitMovimentoBuilder()
				.setLgEnviadoDetran(AitMovimentoServices.NAO_ENVIADO)
				.setCdUsuario(cdUsuario)
				.setCdAit(aitPagamento.getCdAit())
				.setDtDigitacao(dataAtual)
				.setTpStatus(TipoStatusEnum.MULTA_PAGA.getKey())
				.setDtMovimento(dataAtual)
				.build();
		aitMovimentoRepository.insert(aitMovimento, customConnection);
	}

	public RetornoBancoDTO montarRetornoBancoDTO(Ait ait, BancoDadosRetorno bancoDadosRetorno, CustomConnection customConnection) throws Exception {
		String pagamentoUnico = "Baixa bancária para o AIT: " + ait.getIdAit() + " realizada com sucesso"; 
		String pagamentoDuplicado = "Registro de pagamento duplicado";
		List<AitPagamento> pagamentosList = getPagamentoAit(ait.getCdAit(), customConnection);
		RetornoBancoDTO retornoBancoDTO = new RetornoPagamentoBuilder()
				.setStImportacao(pagamentosList.size() > 0 && pagamentosList.size() == 1 ? pagamentoUnico : pagamentoDuplicado)
				.setNrControle(bancoDadosRetorno.getNrControleSemZeros())
				.setDtPagamento(bancoDadosRetorno.getDtPagamento())
				.setNmBanco(bancoDadosRetorno.getNrBanco())
				.setVlPago(bancoDadosRetorno.getVlPago())
				.setIdAit(ait.getIdAit())
				.build();
		return retornoBancoDTO;
	}
	
	private List<AitPagamento> getPagamentoAit(int cdAit, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit);
		return aitPagamentoRepository.find(searchCriterios, customConnection);
	}

	public void registrarDuplicado(PagamentoDTO pagamentoDTO, BancoDadosRetorno bancoDadosRetorno, int cdUsuario ,CustomConnection customConnection) throws Exception {
		AitPagamento aitPagamento = montarAitPagamento(pagamentoDTO, bancoDadosRetorno);
		aitPagamentoRepository.insert(aitPagamento, customConnection);
		this.criarMovimento(aitPagamento, pagamentoDTO, cdUsuario, customConnection);
	}
	
	private void criarMovimento(AitPagamento aitPagamento, PagamentoDTO pagamentoDTO, int cdUsuario, CustomConnection customConnection) throws Exception {
		if(pagamentoDTO.getStPagamento() == SituacaoPagamentoEnum.CANCELADO.getKey()) {
			this.realizarMovimento(aitPagamento, pagamentoDTO, cdUsuario, customConnection);
		}
	}
}