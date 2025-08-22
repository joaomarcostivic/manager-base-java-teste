package com.tivic.manager.mob;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;

public class OcorrenciaTrafegoServices {
	
	public static final int TP_DETECCAO_PLACA = 0;

	public static Result save(OcorrenciaTrafego ocorrenciaTrafego){
		return save(ocorrenciaTrafego, null, null);
	}

	public static Result save(OcorrenciaTrafego ocorrenciaTrafego, AuthData authData){
		return save(ocorrenciaTrafego, authData, null);
	}

	public static Result save(OcorrenciaTrafego ocorrenciaTrafego, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(ocorrenciaTrafego==null)
				return new Result(-1, "Erro ao salvar. OcorrenciaTrafego é nulo");

			int retorno;
			if(ocorrenciaTrafego.getCdOcorrencia()==0){
				retorno = OcorrenciaTrafegoDAO.insert(ocorrenciaTrafego, connect);
				ocorrenciaTrafego.setCdOcorrencia(retorno);
			}
			else {
				retorno = OcorrenciaTrafegoDAO.update(ocorrenciaTrafego, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "OCORRENCIATRAFEGO", ocorrenciaTrafego);
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
	public static Result remove(OcorrenciaTrafego ocorrenciaTrafego) {
		return remove(ocorrenciaTrafego.getCdOcorrencia(), ocorrenciaTrafego.getCdMedicao(), ocorrenciaTrafego.getCdEquipamento(), ocorrenciaTrafego.getCdVia(), ocorrenciaTrafego.getCdFaixa());
	}
	public static Result remove(int cdOcorrencia, int cdMedicao, int cdEquipamento, int cdVia, int cdFaixa){
		return remove(cdOcorrencia, cdMedicao, cdEquipamento, cdVia, cdFaixa, false, null, null);
	}
	public static Result remove(int cdOcorrencia, int cdMedicao, int cdEquipamento, int cdVia, int cdFaixa, boolean cascade){
		return remove(cdOcorrencia, cdMedicao, cdEquipamento, cdVia, cdFaixa, cascade, null, null);
	}
	public static Result remove(int cdOcorrencia, int cdMedicao, int cdEquipamento, int cdVia, int cdFaixa, boolean cascade, AuthData authData){
		return remove(cdOcorrencia, cdMedicao, cdEquipamento, cdVia, cdFaixa, cascade, authData, null);
	}
	public static Result remove(int cdOcorrencia, int cdMedicao, int cdEquipamento, int cdVia, int cdFaixa, boolean cascade, AuthData authData, Connection connect){
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
			retorno = OcorrenciaTrafegoDAO.delete(cdOcorrencia, cdMedicao, cdEquipamento, cdVia, cdFaixa, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_ocorrencia_trafego");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTrafegoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTrafegoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_ocorrencia_trafego", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
