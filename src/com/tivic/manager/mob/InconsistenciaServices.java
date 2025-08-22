package com.tivic.manager.mob;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.AuthData;

public class InconsistenciaServices {
	
	public static final int TP_INDEFERIMENTO = 1;
	public static final int TP_CORRETIVO = 2;
	public static final int TP_SEM_NOME_PROPRIETARIO = 3;
	public static final int TP_SITUACAO_AIT_NAO_CONFIRMADA = 4;
	public static final int TP_SEM_NUMERO_CONTROLE = 5;
	public static final int TP_SEM_CPF_CNPJ_PROPRIETARIO = 6;
	public static final int TP_SEM_MODELO_VEICULO = 7;
	public static final int TP_SEM_ESPECIE_VEICULO = 8;
	public static final int TP_SEM_NR_RENAVAN = 9;
	public static final int TP_AIT_NAO_REGISTRADO = 10;
	public static final int TP_NAI_NAO_EMITIDA = 11;
	public static final int TP_NAI_COM_DEFESA_VIGENTE = 12;
	public static final int TP_NAI_INTEMPESTIVO = 13;
	
	public static Result save(Inconsistencia inconsistencia){
		return save(inconsistencia, null, null);
	}

	public static Result save(Inconsistencia inconsistencia, AuthData authData){
		return save(inconsistencia, authData, null);
	}

	public static Result save(Inconsistencia inconsistencia, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(inconsistencia==null)
				return new Result(-1, "Erro ao salvar. Inconsistencia � nulo");

			int retorno;
			if(inconsistencia.getCdInconsistencia()==0){
				retorno = InconsistenciaDAO.insert(inconsistencia, connect);
				inconsistencia.setCdInconsistencia(retorno);
			}
			else {
				retorno = InconsistenciaDAO.update(inconsistencia, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "INCONSISTENCIA", inconsistencia);
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
	public static Result remove(Inconsistencia inconsistencia) {
		return remove(inconsistencia.getCdInconsistencia());
	}
	public static Result remove(int cdInconsistencia){
		return remove(cdInconsistencia, false, null, null);
	}
	public static Result remove(int cdInconsistencia, boolean cascade){
		return remove(cdInconsistencia, cascade, null, null);
	}
	public static Result remove(int cdInconsistencia, boolean cascade, AuthData authData){
		return remove(cdInconsistencia, cascade, authData, null);
	}
	public static Result remove(int cdInconsistencia, boolean cascade, AuthData authData, Connection connect){
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
			retorno = InconsistenciaDAO.delete(cdInconsistencia, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_inconsistencia");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InconsistenciaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InconsistenciaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_inconsistencia", "order by tp_inconsistencia ASC, cd_inconsistencia DESC", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}