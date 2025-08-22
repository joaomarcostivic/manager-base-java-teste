package com.tivic.manager.wsdl.detran.mg.recursodefesa;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.InfracaoDAO;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.TabelasAuxiliaresMG;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class IncluirDefesaDadosEntradaFactory {

	static TabelasAuxiliaresMG tabelasAuxiliaresMG = new TabelasAuxiliaresMG();
	
	public static RecursoDefesaDadosEntrada fazerDadoEntrada(AitDetranObject aitDetranObject, boolean lgHomologacao) throws Exception {
		Ait ait = aitDetranObject.getAit();
		AitMovimento aitMovimento = aitDetranObject.getAitMovimento();
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Infracao infracao = InfracaoDAO.get(ait.getCdInfracao());
		
		RecursoDefesaDadosEntrada defesaPreviaDadosEntrada = new RecursoDefesaDadosEntrada();
		defesaPreviaDadosEntrada.setOrigemSolicitacao(3);//Prefeitura
		defesaPreviaDadosEntrada.setOrgao(lgHomologacao ? 253710 : orgao.getCdOrgao());
		defesaPreviaDadosEntrada.setPlaca(ait.getNrPlaca());
		defesaPreviaDadosEntrada.setAit(ait.getIdAit());
		defesaPreviaDadosEntrada.setNumeroProcessamento(ait.getNrControle());
		defesaPreviaDadosEntrada.setCodigoRenainf(String.valueOf(ait.getNrRenainf()));
		defesaPreviaDadosEntrada.setCodigoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(0, 4));
		defesaPreviaDadosEntrada.setCodigoDesdobramentoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(4));
		defesaPreviaDadosEntrada.setCodigoMovimentacao(tabelasAuxiliaresMG.getMovimentoDefesa(aitMovimento.getTpStatus()));
		adicionarNumeroProcessoDefesa(aitMovimento, defesaPreviaDadosEntrada);
		adicionarDataEntrada(aitMovimento, defesaPreviaDadosEntrada);
		adicionarDataEncerramento(aitMovimento, defesaPreviaDadosEntrada);
		defesaPreviaDadosEntrada.setDataMovimentacao(aitMovimento.getDtMovimento());
		//defesaPreviaDadosEntrada.setNumeroDocumento(null);
		defesaPreviaDadosEntrada.setNmServico("RECURSO DEFESA");
		return defesaPreviaDadosEntrada;
	}
	
	private static void adicionarDataEntrada(AitMovimento aitMovimento, RecursoDefesaDadosEntrada defesaPreviaDadosEntrada) throws Exception {
		if (verificaFimDefesa (aitMovimento))
			return;
		
		AitMovimento defesaPrevia = AitMovimentoServices.getDefesaPrevia(aitMovimento);
		if(defesaPrevia == null)
			throw new ValidacaoException("Defesa prévia não encontrada");
		defesaPreviaDadosEntrada.setDataEntradaDefesa(defesaPrevia.getDtMovimento());
	}
	
	private static boolean verificaFimDefesa(AitMovimento aitMovimento)
	{
		boolean isDefesa = false;
		if (aitMovimento.getTpStatus() == AitMovimentoServices.FIM_PRAZO_DEFESA)
			isDefesa = true;
		
		return isDefesa;
	}
	
	private static void adicionarDataEncerramento(AitMovimento aitMovimento, RecursoDefesaDadosEntrada defesaPreviaDadosEntrada) {
		switch(aitMovimento.getTpStatus()) {
			case AitMovimentoServices.DEFESA_DEFERIDA:
			case AitMovimentoServices.DEFESA_INDEFERIDA:
				defesaPreviaDadosEntrada.setDataEncerramentoDefesa(aitMovimento.getDtMovimento());
		}
	}
	
	private static void adicionarNumeroProcessoDefesa(AitMovimento aitMovimento, RecursoDefesaDadosEntrada recursoDefesaDadosEntrada) throws Exception {
        switch(aitMovimento.getTpStatus()) {
            case AitMovimentoServices.DEFESA_PREVIA:
                recursoDefesaDadosEntrada.setNumeroProcessoDefesa(aitMovimento.getNrProcesso());
                break;
                
            case AitMovimentoServices.DEFESA_INDEFERIDA:
            case AitMovimentoServices.DEFESA_DEFERIDA:
                AitMovimento defesaPrevia = AitMovimentoServices.getDefesaPreviaEnviado(aitMovimento);
                
                if(defesaPrevia == null)
                	throw new ValidacaoException("Movimento sem entrada de defesa registrado");
                
                recursoDefesaDadosEntrada.setNumeroProcessoDefesa(defesaPrevia.getNrProcesso());
                break;
        }
    }
}
