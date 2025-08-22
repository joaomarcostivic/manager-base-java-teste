package com.tivic.manager.acd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class InstituicaoTipoMantenedoraServices {

	public static Result save(InstituicaoTipoMantenedora instituicaoTipoMantenedora){
		return save(instituicaoTipoMantenedora, null);
	}

	public static Result save(InstituicaoTipoMantenedora instituicaoTipoMantenedora, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(instituicaoTipoMantenedora==null)
				return new Result(-1, "Erro ao salvar. InstituicaoTipoMantenedora é nulo");

			int retorno;
			if(InstituicaoTipoMantenedoraDAO.get(instituicaoTipoMantenedora.getCdInstituicao(), instituicaoTipoMantenedora.getCdTipoMantenedora(), instituicaoTipoMantenedora.getCdPeriodoLetivo(), connect) == null){
				retorno = InstituicaoTipoMantenedoraDAO.insert(instituicaoTipoMantenedora, connect);
				instituicaoTipoMantenedora.setCdInstituicao(retorno);
			}
			else {
				retorno = InstituicaoTipoMantenedoraDAO.update(instituicaoTipoMantenedora, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "INSTITUICAOTIPOMANTENEDORA", instituicaoTipoMantenedora);
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
	public static Result remove(int cdInstituicao, int cdTipoMantenedora, int cdPeriodoLetivo){
		return remove(cdInstituicao, cdTipoMantenedora, cdPeriodoLetivo, false, null);
	}
	public static Result remove(int cdInstituicao, int cdTipoMantenedora, int cdPeriodoLetivo, boolean cascade){
		return remove(cdInstituicao, cdTipoMantenedora, cdPeriodoLetivo, cascade, null);
	}
	public static Result remove(int cdInstituicao, int cdTipoMantenedora, int cdPeriodoLetivo, boolean cascade, Connection connect){
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
			retorno = InstituicaoTipoMantenedoraDAO.delete(cdInstituicao, cdTipoMantenedora, cdPeriodoLetivo, connect);
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
			ResultSetMap rsm = InstituicaoTipoMantenedoraDAO.find(criterios, connect);
			while (rsm.next()) {
				int ret = InstituicaoTipoMantenedoraDAO.delete(cdInstituicao, rsm.getInt("cd_tipo_mantenedora"), cdPeriodoLetivo, connect);
				if(ret<0){
					Conexao.rollback(connect);
					return new Result(ret, "Erro ao excluir instituição tipo mantenedora");
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_tipo_mantenedora");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoTipoMantenedoraServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoTipoMantenedoraServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_tipo_mantenedora", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
