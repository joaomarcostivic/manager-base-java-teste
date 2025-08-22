package com.tivic.manager.wsdl.detran.mg.recursodefesa.cancelamento;

import java.util.GregorianCalendar;
import javax.ws.rs.core.NoContentException;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.InfracaoDAO;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.TabelasAuxiliaresMG;
import com.tivic.manager.wsdl.detran.mg.recursodefesa.RecursoDefesaDadosEntrada;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class CancelarDefesaDadosEntradaFactory {

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
		defesaPreviaDadosEntrada.setCodigoMovimentacao(aitMovimento.getTpStatus());
		defesaPreviaDadosEntrada.setNumeroProcessoDefesa(aitMovimento.getNrProcesso());
		adicionarDataEntrada(aitMovimento, defesaPreviaDadosEntrada);
		defesaPreviaDadosEntrada.setDataMovimentacao(aitMovimento.getDtMovimento());
		//defesaPreviaDadosEntrada.setNumeroDocumento(null);
		defesaPreviaDadosEntrada.setNmServico("CANCELAR RECURSO DEFESA");
		return defesaPreviaDadosEntrada;
	}
	
	private static void adicionarDataEntrada(AitMovimento aitMovimento, RecursoDefesaDadosEntrada defesaPreviaDadosEntrada) throws Exception{
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
	
	private static GregorianCalendar ultimoMovimento(int cdAit, int cdMovimentoCancelamento, CustomConnection customConnection) throws Exception {
		AitMovimento aitMovimento = new AitMovimento();
		SearchCriterios criterios = new SearchCriterios();
		criterios.addCriteriosEqualInteger("A.cd_ait", cdAit);
		criterios.addCriteriosEqualInteger("A.cd_movimento", cdMovimentoCancelamento);
		Search<AitMovimento> search = new SearchBuilder<AitMovimento>("mob_ait_movimento A")
				.fields("A.dt_movimento")
				.searchCriterios(criterios)
				.additionalCriterias("A.lg_enviado_detran NOT IN (" + TipoLgEnviadoDetranEnum.NAO_ENVIAR_CANCELADO.getKey() + ")")
				.customConnection(customConnection)
				.build();
		
		aitMovimento = search.getList(AitMovimento.class).get(0);
	    if (aitMovimento == null) {
	        throw new NoContentException("Nenhum movimento encontrado para este AIT.");
	    }
	    
	    return aitMovimento.getDtMovimento();
	}
	
}
