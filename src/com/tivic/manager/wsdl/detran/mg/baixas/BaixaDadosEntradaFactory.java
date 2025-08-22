package com.tivic.manager.wsdl.detran.mg.baixas;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.AitPagamento;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.InfracaoDAO;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.TabelasAuxiliaresMG;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.util.date.DateUtil;
public class BaixaDadosEntradaFactory {

	private static TabelasAuxiliaresMG tabelasAuxiliares = new TabelasAuxiliaresMG();
	
	public static BaixaDadosEntrada compor(AitDetranObject aitDetranObject)  throws ValidacaoException {	
		
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Ait ait = aitDetranObject.getAit();
		AitMovimento aitMovimento = aitDetranObject.getAitMovimento();
		Infracao infracao = InfracaoDAO.get(ait.getCdInfracao());
		if(infracao == null)
			throw new ValidacaoException("Movimento sem ocorrência");
		
		BaixaDadosEntrada dados = new BaixaDadosEntrada();
		dados.setOrigemSolicitacao(3);//Prefeitura
		dados.setOrgao(orgao.getCdOrgao());
		dados.setPlaca(ait.getNrPlaca());
		dados.setAit(ait.getIdAit());		
		dados.setCodigoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(0, 4));
		dados.setCodigoDesdobramentoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(4));
		dados.setNumeroProcessamento(ait.getNrControle());
		dados.setCodigoRenainf(ait.getNrRenainf());
		dados.setDataMovimentacao(setDataMovimentacao(aitDetranObject, dados));
		dados.setCodigoMovimentacao(tabelasAuxiliares.getMovimentoBaixa(aitMovimento.getTpStatus()));
		setValorPago(aitDetranObject, dados);
		dados.setNumeroDocumento(String.valueOf(aitMovimento.getCdAit()) + String.valueOf(aitMovimento.getCdMovimento()));
		dados.setNmServico("BAIXA");
		
		return dados;		
	}
	
	private static void setValorPago(AitDetranObject aitDetranObject, BaixaDadosEntrada dados) {
		if(aitDetranObject.getAitMovimento().getTpStatus() != AitMovimentoServices.CANCELAMENTO_NIP && aitDetranObject.getAitPagamento() != null
				&& aitDetranObject.getAitMovimento().getTpStatus() != AitMovimentoServices.CANCELAMENTO_DIVIDA_ATIVA) {
			AitPagamento aitPagamento = aitDetranObject.getAitPagamento();
			dados.setValorPago(aitPagamento.getVlPago());	
		}
	}
	
	private static GregorianCalendar setDataMovimentacao(AitDetranObject aitDetranObject, BaixaDadosEntrada dados) {
		if(aitDetranObject.getAitPagamento() != null && aitDetranObject.getAitMovimento().getTpStatus() == TipoStatusEnum.MULTA_PAGA.getKey()) {
			return aitDetranObject.getAitPagamento().getDtPagamento();
		}
		return DateUtil.getDataAtual();
	}

}
