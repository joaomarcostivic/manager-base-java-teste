package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

import sol.dao.ItemComparator;

public class AitReportGetCriteriosNAI {
	static AitReportValidatorsNAI validators = new AitReportValidatorsNAI();
	static AitReportGetParamns getParamns = new AitReportGetParamns();
	
	@SuppressWarnings("static-access")
	public static ArrayList<ItemComparator> primeiraVia(int cdAit, Connection connect, boolean isLote) throws ValidacaoException, SQLException, ParseException, AitReportErrorException{
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		boolean isConnectionNull = connect==null;	
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		
		if (isConnectionNull){
			connect = Conexao.conectar();
		}
		try {
			HashMap<String, Object> paramns = getParamns.getParamnsNAI(connect);
			validators.verificarParamns(paramns);
			
				if (lgBaseAntiga){
					criterios.add(new ItemComparator("A.CODIGO_AIT", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
				}
				else 
				{
					AitReportValidatorsNAI.verificarCriterios(cdAit, connect);
					
					criterios.add(new ItemComparator("A.cd_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("A.st_ait", String.valueOf(AitServices.ST_CONFIRMADO), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("K.tp_status", String.valueOf(AitMovimentoServices.REGISTRO_INFRACAO), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("A.nr_controle", "", ItemComparator.NOTISNULL, Types.CHAR));
					criterios.add(new ItemComparator("A.nr_controle", "0", ItemComparator.GREATER, Types.CHAR));
					criterios.add(new ItemComparator("A.NM_PROPRIETARIO", "", ItemComparator.NOTISNULL, Types.CHAR));
					criterios.add(new ItemComparator(("A.NR_CPF_CNPJ_PROPRIETARIO").trim(), "", ItemComparator.NOTISNULL, Types.CHAR));
					criterios.add(new ItemComparator("A.NR_CPF_CNPJ_PROPRIETARIO", "0", ItemComparator.GREATER, Types.CHAR));
					criterios.add(new ItemComparator("H.NM_MODELO", "", ItemComparator.NOTISNULL, Types.CHAR));
					criterios.add(new ItemComparator("G.DS_ESPECIE", "", ItemComparator.NOTISNULL, Types.CHAR));
					criterios.add(new ItemComparator("A.NR_RENAVAN", "", ItemComparator.NOTISNULL, Types.CHAR));
				}
				
				if (isConnectionNull)
				{
					Conexao.desconectar(connect);
				}
				
				return criterios;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public ArrayList<ItemComparator> segundaVia(int cdAit) throws ValidacaoException, SQLException {
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		HashMap<String, Object> paramns = getParamns.getParamnsNAI();
		validators.verificarParamns(paramns);
		if(validators.verificaPrimeiraVia(cdAit, null)){
			if (lgBaseAntiga){
				criterios.add(new ItemComparator("A.CODIGO_AIT", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
				return criterios;
			}
			criterios.add(new ItemComparator("A.cd_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.st_ait", String.valueOf(AitServices.ST_CONFIRMADO), ItemComparator.EQUAL, Types.INTEGER));
			return criterios;
		}
		else{
			throw new ValidacaoException("Para gerar a segunda via e preciso já ter emitido a primeira.");
		}
	}

}