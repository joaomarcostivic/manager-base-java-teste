package com.tivic.manager.adm;

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

public class OrcamentoServices {

	public static Result save(Orcamento orcamento){
		return save(orcamento, null, null);
	}

	public static Result save(Orcamento orcamento, AuthData authData){
		return save(orcamento, authData, null);
	}

	public static Result save(Orcamento orcamento, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(orcamento==null)
				return new Result(-1, "Erro ao salvar. Orcamento é nulo");

			int retorno;
			if(orcamento.getCdOrcamento()==0){
				retorno = OrcamentoDAO.insert(orcamento, connect);
				orcamento.setCdOrcamento(retorno);
			}
			else {
				retorno = OrcamentoDAO.update(orcamento, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ORCAMENTO", orcamento);
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
	public static Result remove(int cdOrcamento){
		return remove(cdOrcamento, false, null, null);
	}
	public static Result remove(int cdOrcamento, boolean cascade){
		return remove(cdOrcamento, cascade, null, null);
	}
	public static Result remove(int cdOrcamento, boolean cascade, AuthData authData){
		return remove(cdOrcamento, cascade, authData, null);
	}
	public static Result remove(int cdOrcamento, boolean cascade, AuthData authData, Connection connect){
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
			retorno = OrcamentoDAO.delete(cdOrcamento, connect);
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
	
	public static Result getByEmpresaExercicio(int cdEmpresa, int cdExercicio ) {
		return getByEmpresaExercicio(cdEmpresa, cdExercicio, null);
	}

	public static Result getByEmpresaExercicio(int cdEmpresa, int cdExercicio, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			Result result = new Result(0, "Nenhum Planejamento encontrado");
			ResultSetMap rsm = new ResultSetMap( connect.prepareStatement("select * from adm_orcamento "+
												 " where cd_empresa = "+cdEmpresa+
												 " and   cd_exercicio = "+cdExercicio
												 ).executeQuery());
			if( rsm != null && rsm.next() ){
				result.setCode(1);
				result.getObjects().put("ORCAMENTO", OrcamentoDAO.get( rsm.getInt("CD_ORCAMENTO") , connect) );
				result.getObjects().put("RSM_CATEGORIAS", OrcamentoCategoriaServices.getAllByOrcamento(rsm.getInt("CD_ORCAMENTO")) );
			}
			return result;	
		}catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrcamentoCategoriaServices.getAllByExercicio: " + e);
			return null;
		}finally {
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_orcamento");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrcamentoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrcamentoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_orcamento A "+
						" JOIN ctb_exercicio B on ( A.cd_exercicio = B.cd_exercicio )"+
						" LEFT JOIN ctb_centro_custo C on ( A.cd_centro_custo = C.cd_centro_custo )",
						criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}