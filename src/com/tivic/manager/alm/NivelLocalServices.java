package com.tivic.manager.alm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Setor;
import com.tivic.manager.grl.SetorDAO;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

public class NivelLocalServices {

	public static Result save(NivelLocal nivelLocal){
		return save(nivelLocal, null);
	}
	
	public static Result save(NivelLocal nivelLocal, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(nivelLocal==null)
				return new Result(-1, "Erro ao salvar. Setor é nulo");
			
			if(nivelLocal.getCdNivelLocal() == nivelLocal.getCdNivelLocalSuperior())
				return new Result(-1, "O nível " + nivelLocal.getNmNivelLocal() + " não pode ser vinculado a ele mesmo.");
			
			int retorno;
			if(nivelLocal.getCdNivelLocal()==0){
				retorno = NivelLocalDAO.insert(nivelLocal, connect);
				nivelLocal.setCdNivelLocal(retorno);
			}
			else {
				retorno = NivelLocalDAO.update(nivelLocal, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "SETOR", nivelLocal);
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
	
	public static Result remove(int cdNivelLocal){
		return remove(cdNivelLocal, false, null);
	}
	
	public static Result remove(int cdNivelLocal, boolean cascade){
		return remove(cdNivelLocal, cascade, null);
	}
	
	public static Result remove(int cdNivelLocal, boolean cascade, Connection connect){
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
				retorno = NivelLocalDAO.delete(cdNivelLocal, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este local está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Local excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir local!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getSubNiveis(int cdNivelLocal) {
		return getSubNiveis(cdNivelLocal, null);
	}

	public static ResultSetMap getSubNiveis(int cdNivelLocal, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM alm_nivel_local A " +
																  "WHERE cd_nivel_local_superior " + (cdNivelLocal > 0 ? " = ? " : " IS NULL"));
			if (cdNivelLocal>0)
				pstmt.setInt(1, cdNivelLocal);
		return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAll() {
		return getAll(null);
	}
	public static ResultSetMap getAll(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM alm_nivel_local A");
		return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllHierarquia() {
		return getAllHierarquia(null);
	}

	public static ResultSetMap getAllHierarquia(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			ResultSetMap rsm = NivelLocalDAO.find(criterios, connect);
			while (rsm != null && rsm.next()) {
				if (rsm.getInt("cd_nivel_local_superior") != 0) {
					int pointer = rsm.getPointer();
					int cdNivelLocal = rsm.getInt("cd_nivel_local_superior");
					HashMap<String,Object> register = rsm.getRegister();
					if (rsm.locate("cd_nivel_local", new Integer(rsm.getInt("cd_nivel_local_superior")), false, true)) {
						HashMap<String,Object> parentNode = rsm.getRegister();
						boolean isFound = rsm.getInt("cd_nivel_local")==cdNivelLocal;
						ResultSetMap subRsm = (ResultSetMap)parentNode.get("subResultSetMap");
						while (!isFound && subRsm!=null) {
							subRsm = parentNode==null ? null : (ResultSetMap)parentNode.get("subResultSetMap");
							parentNode = subRsm==null ? null : subRsm.getRegister();
							isFound = subRsm==null ? false : subRsm.getInt("cd_nivel_local")==cdNivelLocal;
						}
						subRsm = (ResultSetMap)parentNode.get("subResultSetMap");
						if (subRsm==null) {
							subRsm = new ResultSetMap();
							parentNode.put("subResultSetMap", subRsm);
						}
						subRsm.addRegister(register);
						rsm.getLines().remove(register);
						pointer--;
					}
					rsm.goTo(pointer);
				}
			}
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}