package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

import sol.dao.ItemComparator;

public class AitReportGetCriteriosNic {
	static AitReportGetParamns getParamns = new AitReportGetParamns();
	static AitReportValidatorsNic validatorsNic = new AitReportValidatorsNic();
	static AitReportValidatorsNIP validators = new AitReportValidatorsNIP();
	
	public static ArrayList<ItemComparator> primeiraVia(int cdAit,  boolean lote, Connection connect) throws ValidacaoException, SQLException, ParseException
	{

		boolean isConnectionNull = connect==null;
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();		
		HashMap<String, Object> paramns = getParamns.getParamnsNic(connect);
		validatorsNic.verificarParamns(paramns);
		
		if (isConnectionNull)
		{
			connect = Conexao.conectar();
		}
			
		criterios.add(new ItemComparator("A.cd_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
				
		if (!lote)
		{
			validatorsNic.verificarCriterios (cdAit, connect);
		}
			
		criterios.add(new ItemComparator("A.tp_pessoa_proprietario", String.valueOf(AitServices.TP_PESSOA_PROPRIETARIO_JURIDICO), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.nr_controle", "", ItemComparator.NOTISNULL, Types.CHAR));
		criterios.add(new ItemComparator("A.nr_controle", "0", ItemComparator.GREATER, Types.CHAR));
		criterios.add(new ItemComparator("A.NM_PROPRIETARIO", "", ItemComparator.NOTISNULL, Types.CHAR));
		criterios.add(new ItemComparator(("A.NR_CPF_CNPJ_PROPRIETARIO").trim(), "", ItemComparator.NOTISNULL, Types.CHAR));
		criterios.add(new ItemComparator("A.NR_CPF_CNPJ_PROPRIETARIO", "0", ItemComparator.GREATER, Types.CHAR));
		criterios.add(new ItemComparator("H.NM_MODELO", "", ItemComparator.NOTISNULL, Types.CHAR));
		criterios.add(new ItemComparator("G.DS_ESPECIE", "", ItemComparator.NOTISNULL, Types.CHAR));
		criterios.add(new ItemComparator("A.NR_RENAVAN", "", ItemComparator.NOTISNULL, Types.CHAR));
			
		if(isConnectionNull)
		{
			Conexao.desconectar(connect);
		}
			
		return criterios;
	}
	
	public static ArrayList<ItemComparator> gerarAitNic(int cdAit,  boolean lote, Connection connect) throws ValidacaoException, SQLException, ParseException
	{

		boolean isConnectionNull = connect==null;
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		HashMap<String, Object> paramns = getParamns.getParamnsNic(connect);
		validatorsNic.verificarParamns(paramns);
		
		if (isConnectionNull)
		{
			connect = Conexao.conectar();
		}
			
		criterios.add(new ItemComparator("A.cd_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
				
		if (!lote)
		{
			validatorsNic.verificarCriterios (cdAit, connect);
		}
			
		criterios.add(new ItemComparator("K.tp_status", String.valueOf(AitMovimentoServices.NIP_ENVIADA), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.st_ait", String.valueOf(AitServices.ST_CONFIRMADO), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.st_ait", String.valueOf(AitServices.ST_CONFIRMADO), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.tp_pessoa_proprietario", String.valueOf(AitServices.TP_PESSOA_PROPRIETARIO_JURIDICO), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.nr_controle", "", ItemComparator.NOTISNULL, Types.CHAR));
		criterios.add(new ItemComparator("A.nr_controle", "0", ItemComparator.GREATER, Types.CHAR));
		criterios.add(new ItemComparator("A.NM_PROPRIETARIO", "", ItemComparator.NOTISNULL, Types.CHAR));
		criterios.add(new ItemComparator(("A.NR_CPF_CNPJ_PROPRIETARIO").trim(), "", ItemComparator.NOTISNULL, Types.CHAR));
		criterios.add(new ItemComparator("A.NR_CPF_CNPJ_PROPRIETARIO", "0", ItemComparator.GREATER, Types.CHAR));
		criterios.add(new ItemComparator("H.NM_MODELO", "", ItemComparator.NOTISNULL, Types.CHAR));
		criterios.add(new ItemComparator("G.DS_ESPECIE", "", ItemComparator.NOTISNULL, Types.CHAR));
		criterios.add(new ItemComparator("A.NR_RENAVAN", "", ItemComparator.NOTISNULL, Types.CHAR));
		criterios.add(new ItemComparator("B.NR_COD_DETRAN", String.valueOf(InfracaoServices.MULTA_NAO_INDENTIFICACAO_CONDUTOR), ItemComparator.DIFFERENT, Types.INTEGER));
		criterios.add(new ItemComparator("B.NR_COD_DETRAN", String.valueOf(InfracaoServices.MULTA_NAO_INDENTIFICACAO_CONDUTOR_2), ItemComparator.DIFFERENT, Types.INTEGER));
		criterios.add(new ItemComparator("K.tp_status", String.valueOf(AitMovimentoServices.DEFESA_DEFERIDA), ItemComparator.DIFFERENT, Types.INTEGER));
		criterios.add(new ItemComparator("B.tp_responsabilidade ", String.valueOf(InfracaoServices.MULTA_RESPONSABILIDADE_CONDUTOR), ItemComparator.EQUAL, Types.INTEGER));	
		criterios.add(new ItemComparator("A.CD_AIT_ORIGEM", "", ItemComparator.ISNULL, Types.INTEGER));	
		
		if(isConnectionNull)
		{
			Conexao.desconectar(connect);
		}
			
		return criterios;
	}
	
	@SuppressWarnings("static-access")
	protected ArrayList<ItemComparator> segundaVia(int cdAit) throws ValidacaoException  
	{
		
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		HashMap<String, Object> paramns = getParamns.getParamnsNIP(null);
		AitMovimentoServices movimentoServices = new AitMovimentoServices();
		AitMovimento movimento = new AitMovimento();
		
		validators.verificarParamns(paramns);
		movimento = movimentoServices.getMovimentoTpStatus(cdAit, movimentoServices.NIP_ENVIADA);
		
		if(movimento != null)
		{
			criterios.add(new ItemComparator("A.cd_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
			return criterios;
		}
		else
		{
			throw new ValidacaoException("Para emissão da segunda via de NIC deve ter emitido a primeira.");
		}
		
	}
	
	public static ArrayList<ItemComparator> buscarAitNic(int cdAit) throws ValidacaoException, SQLException, ParseException
	{
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();		
		criterios.add(new ItemComparator("A.cd_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
		return criterios;
	}
}
