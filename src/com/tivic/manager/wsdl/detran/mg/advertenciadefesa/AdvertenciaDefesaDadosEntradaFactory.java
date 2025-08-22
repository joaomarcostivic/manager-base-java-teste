package com.tivic.manager.wsdl.detran.mg.advertenciadefesa;

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

public class AdvertenciaDefesaDadosEntradaFactory {

	static TabelasAuxiliaresMG tabelasAuxiliaresMG = new TabelasAuxiliaresMG();
	
	public static AdvertenciaDefesaDadosEntrada fazerDadoEntrada(AitDetranObject aitDetranObject, boolean lgHomologacao) throws Exception{
		Ait ait = aitDetranObject.getAit();
		AitMovimento aitMovimento = aitDetranObject.getAitMovimento();
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Infracao infracao = InfracaoDAO.get(ait.getCdInfracao());
		
		AdvertenciaDefesaDadosEntrada advertenciaDefesaDadosEntrada = new AdvertenciaDefesaDadosEntrada();
		advertenciaDefesaDadosEntrada.setOrigemSolicitacao(3);//Prefeitura
		advertenciaDefesaDadosEntrada.setOrgao(lgHomologacao ? 253710 : orgao.getCdOrgao());
		advertenciaDefesaDadosEntrada.setPlaca(ait.getNrPlaca());
		advertenciaDefesaDadosEntrada.setAit(ait.getIdAit());
		advertenciaDefesaDadosEntrada.setNumeroProcessamento(ait.getNrControle());
		advertenciaDefesaDadosEntrada.setCodigoRenainf(String.valueOf(ait.getNrRenainf()));
		advertenciaDefesaDadosEntrada.setCodigoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(0, 4));
		advertenciaDefesaDadosEntrada.setCodigoDesdobramentoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(4));
		advertenciaDefesaDadosEntrada.setCodigoMovimentacao(tabelasAuxiliaresMG.getMovimentoAdvertencia(aitMovimento.getTpStatus()));
		advertenciaDefesaDadosEntrada.setNumeroProcessoDefesa(aitMovimento.getNrProcesso());
		adicionarDataEntrada(aitMovimento, advertenciaDefesaDadosEntrada);
		adicionarDataEncerramento(aitMovimento, advertenciaDefesaDadosEntrada);
		advertenciaDefesaDadosEntrada.setDataMovimentacao(aitMovimento.getDtMovimento());
		//advertenciaDefesaDadosEntrada.setNumeroDocumento(null);
		advertenciaDefesaDadosEntrada.setNmServico("ADVERTENCIA DEFESA");
		
		return advertenciaDefesaDadosEntrada;
	}
	
	private static void adicionarDataEntrada(AitMovimento aitMovimento, AdvertenciaDefesaDadosEntrada advertenciaDefesaDadosEntrada) throws Exception{
		if (verificaFimDefesa (aitMovimento))
			return;
		
		AitMovimento advertenciaDefesa = AitMovimentoServices.getAdvertenciaDefesa(aitMovimento);
		if(advertenciaDefesa == null)
			throw new ValidacaoException("Advertência de defesa não encontrada");
		advertenciaDefesaDadosEntrada.setDataEntradaDefesa(advertenciaDefesa.getDtMovimento());
	}
	
	private static boolean verificaFimDefesa(AitMovimento aitMovimento)
	{
		boolean isDefesa = false;
		if (aitMovimento.getTpStatus() == AitMovimentoServices.FIM_PRAZO_DEFESA)
			isDefesa = true;
		
		return isDefesa;
	}
	
	private static void adicionarDataEncerramento(AitMovimento aitMovimento, AdvertenciaDefesaDadosEntrada advertenciaDefesaDadosEntrada) {
		switch(aitMovimento.getTpStatus()) {
			case AitMovimentoServices.ADVERTENCIA_DEFESA_DEFERIDA:
			case AitMovimentoServices.ADVERTENCIA_DEFESA_INDEFERIDA:
				advertenciaDefesaDadosEntrada.setDataEncerramentoDefesa(aitMovimento.getDtMovimento());
		}
	}
}