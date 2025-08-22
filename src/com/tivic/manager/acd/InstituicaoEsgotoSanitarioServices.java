package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class InstituicaoEsgotoSanitarioServices {

	public static Result save(InstituicaoEsgotoSanitario instituicaoEsgotoSanitario){
		return save(instituicaoEsgotoSanitario, null);
	}

	public static Result save(InstituicaoEsgotoSanitario instituicaoEsgotoSanitario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(instituicaoEsgotoSanitario==null)
				return new Result(-1, "Erro ao salvar. InstituicaoEsgotoSanitario é nulo");

			int retorno;
			if(InstituicaoEsgotoSanitarioDAO.get(instituicaoEsgotoSanitario.getCdInstituicao(), instituicaoEsgotoSanitario.getCdEsgotoSanitario(), instituicaoEsgotoSanitario.getCdPeriodoLetivo())==null){
				retorno = InstituicaoEsgotoSanitarioDAO.insert(instituicaoEsgotoSanitario, connect);
				instituicaoEsgotoSanitario.setCdInstituicao(retorno);
			}
			else {
				retorno = InstituicaoEsgotoSanitarioDAO.update(instituicaoEsgotoSanitario, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "INSTITUICAOESGOTOSANITARIO", instituicaoEsgotoSanitario);
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
	public static Result remove(int cdInstituicao, int cdEsgotoSanitario, int cdPeriodoLetivo){
		return remove(cdInstituicao, cdEsgotoSanitario, cdPeriodoLetivo, false, null);
	}
	public static Result remove(int cdInstituicao, int cdEsgotoSanitario, int cdPeriodoLetivo, boolean cascade){
		return remove(cdInstituicao, cdEsgotoSanitario, cdPeriodoLetivo, cascade, null);
	}
	public static Result remove(int cdInstituicao, int cdEsgotoSanitario, int cdPeriodoLetivo, boolean cascade, Connection connect){
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
			retorno = InstituicaoEsgotoSanitarioDAO.delete(cdInstituicao, cdEsgotoSanitario, cdPeriodoLetivo, connect);
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
	
	public static Result removeByInstituicaoPeriodo(int cdInstituicao, int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_instituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_periodo_letivo", "" + cdPeriodoLetivo, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = InstituicaoEsgotoSanitarioDAO.find(criterios, connect);
			while (rsm.next()) {
				int ret = InstituicaoEsgotoSanitarioDAO.delete(cdInstituicao, rsm.getInt("cd_esgoto_sanitario"), cdPeriodoLetivo, connect);
				if(ret<0){
					Conexao.rollback(connect);
					return new Result(ret, "Erro ao excluir instituição esgoto sanitario");
				}
			}
			
			if (isConnectionNull)
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_esgoto_sanitario");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEsgotoSanitarioServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEsgotoSanitarioServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_esgoto_sanitario", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
