package com.tivic.manager.grl;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;

public class FiltroRelatorioServices {

	public static Result save(FiltroRelatorio filtroRelatorio){
		return save(filtroRelatorio, null, null);
	}

	public static Result save(FiltroRelatorio filtroRelatorio, AuthData authData){
		return save(filtroRelatorio, authData, null);
	}

	public static Result save(FiltroRelatorio filtroRelatorio, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(filtroRelatorio==null)
				return new Result(-1, "Erro ao salvar. FiltroRelatorio é nulo");

			int retorno;
			if(filtroRelatorio.getCdFiltroRelatorio()==0){
				retorno = FiltroRelatorioDAO.insert(filtroRelatorio, connect);
				filtroRelatorio.setCdFiltroRelatorio(retorno);
			}
			else {
				retorno = FiltroRelatorioDAO.update(filtroRelatorio, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "FILTRORELATORIO", filtroRelatorio);
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
	public static Result remove(FiltroRelatorio filtroRelatorio) {
		return remove(filtroRelatorio.getCdFiltroRelatorio());
	}
	public static Result remove(int cdFiltroRelatorio){
		return remove(cdFiltroRelatorio, false, null, null);
	}
	public static Result remove(int cdFiltroRelatorio, boolean cascade){
		return remove(cdFiltroRelatorio, cascade, null, null);
	}
	public static Result remove(int cdFiltroRelatorio, boolean cascade, AuthData authData){
		return remove(cdFiltroRelatorio, cascade, authData, null);
	}
	public static Result remove(int cdFiltroRelatorio, boolean cascade, AuthData authData, Connection connect){
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
			retorno = FiltroRelatorioDAO.delete(cdFiltroRelatorio, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_filtro_relatorio");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FiltroRelatorioServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FiltroRelatorioServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllByPessoa(int cdPessoa) {
		return getAllByPessoa(cdPessoa, null);
	}

	public static ResultSetMap getAllByPessoa(int cdPessoa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("CD_PESSOA", Integer.toString(cdPessoa), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("ST_RELATORIO", Integer.toString(1), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("TP_RELATORIO", Integer.toString(0), ItemComparator.EQUAL, Types.INTEGER));
			return find(criterios, connect);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FiltroRelatorioServices.getAll: " + e);
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
		try {
			ResultSetMap rsm = Search.find("SELECT * FROM grl_filtro_relatorio", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			while(rsm.next()) {
				JSONArray jsonCampos = new JSONArray(rsm.getString("json_filtro_relatorio"));
				for (int i=0; i<jsonCampos.length(); i++) {
					JSONObject obj = jsonCampos.getJSONObject(i);
					
					if(obj.get("CD_CAMPO")==null)
						continue;
					
					FiltroCampo campo = FiltroCampoDAO.get((Integer)obj.get("CD_CAMPO"), connect);
					if(campo==null)
						continue;
					obj.put("ID_CAMPO", campo.getIdCampo());
					obj.put("TP_CAMPO", campo.getTpCampo());
				}
				rsm.setValueToField("JSON_FILTRO_RELATORIO", jsonCampos.toString());
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FiltroRelatorioServices.getAll: " + e);
			return null;
		}
	}

}