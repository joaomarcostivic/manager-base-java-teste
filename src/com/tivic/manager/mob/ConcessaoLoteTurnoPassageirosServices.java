package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.manager.acd.InstituicaoHorarioServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;

public class ConcessaoLoteTurnoPassageirosServices {

	public static Result save(ConcessaoLoteTurnoPassageiros concessaoLoteTurnoPassageiros){
		return save(concessaoLoteTurnoPassageiros, null, null);
	}

	public static Result save(ConcessaoLoteTurnoPassageiros concessaoLoteTurnoPassageiros, AuthData authData){
		return save(concessaoLoteTurnoPassageiros, authData, null);
	}

	public static Result save(ConcessaoLoteTurnoPassageiros concessaoLoteTurnoPassageiros, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(concessaoLoteTurnoPassageiros==null)
				return new Result(-1, "Erro ao salvar. ConcessaoLoteTurnoPassageiros é nulo");

			int retorno;
			if(concessaoLoteTurnoPassageiros.getCdConcessaoTurnoPassageiros()==0){
				retorno = ConcessaoLoteTurnoPassageirosDAO.insert(concessaoLoteTurnoPassageiros, connect);
				concessaoLoteTurnoPassageiros.setCdConcessaoTurnoPassageiros(retorno);
			}
			else {
				retorno = ConcessaoLoteTurnoPassageirosDAO.update(concessaoLoteTurnoPassageiros, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CONCESSAOLOTETURNOPASSAGEIROS", concessaoLoteTurnoPassageiros);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static Result remove(ConcessaoLoteTurnoPassageiros concessaoLoteTurnoPassageiros) {
		return remove(concessaoLoteTurnoPassageiros.getCdConcessaoTurnoPassageiros(), concessaoLoteTurnoPassageiros.getCdConcessao(), concessaoLoteTurnoPassageiros.getTpTurno());
	}
	public static Result remove(int cdConcessaoTurnoPassageiros, int cdConcessaoLote, int cdConcessao){
		return remove(cdConcessaoTurnoPassageiros, cdConcessaoLote, cdConcessao, false, null, null);
	}
	public static Result remove(int cdConcessaoTurnoPassageiros, int cdConcessaoLote, int cdConcessao, boolean cascade){
		return remove(cdConcessaoTurnoPassageiros, cdConcessaoLote, cdConcessao, cascade, null, null);
	}
	public static Result remove(int cdConcessaoTurnoPassageiros, int cdConcessaoLote, int cdConcessao, boolean cascade, AuthData authData){
		return remove(cdConcessaoTurnoPassageiros, cdConcessaoLote, cdConcessao, cascade, authData, null);
	}
	public static Result remove(int cdConcessaoTurnoPassageiros, int cdConcessaoLote, int cdConcessao, boolean cascade, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = ConcessaoLoteTurnoPassageirosDAO.delete(cdConcessaoTurnoPassageiros, cdConcessaoLote, cdConcessao, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_concessao_lote_turno_passageiros");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteTurnoPassageirosServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteTurnoPassageirosServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		ResultSetMap rsm = Search.find("SELECT * FROM mob_concessao_lote_turno_passageiros A", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		while(rsm.next()){
			rsm.setValueToField("NM_TP_TURNO", InstituicaoHorarioServices.tiposTurno[rsm.getInt("tp_turno")]);
		}
		rsm.beforeFirst();
		
		return rsm;
	}

}