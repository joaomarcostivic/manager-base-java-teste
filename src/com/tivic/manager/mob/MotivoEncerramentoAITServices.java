package com.tivic.manager.mob;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.TipoArquivo;
import com.tivic.manager.util.Util;

public class MotivoEncerramentoAITServices {

	public static Result save(MotivoEncerramentoAIT motivoEncerramentoAIT){
		return save(motivoEncerramentoAIT, null);
	}

	public static Result save(MotivoEncerramentoAIT motivoEncerramentoAIT, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(motivoEncerramentoAIT==null)
				return new Result(-1, "Erro ao salvar. MotivoEncerramentoAIT é nulo");

			int retorno;
			if(motivoEncerramentoAIT.getCdMotivoEncerramento()==0){
				retorno = MotivoEncerramentoAITDAO.insert(motivoEncerramentoAIT, connect);
				motivoEncerramentoAIT.setCdMotivoEncerramento(retorno);
			}
			else {
				retorno = MotivoEncerramentoAITDAO.update(motivoEncerramentoAIT, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "MOTIVOENCERRAMENTOAIT", motivoEncerramentoAIT);
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
	public static Result remove(int cdMotivoEncerramento){
		return remove(cdMotivoEncerramento, false, null);
	}
	public static Result remove(int cdMotivoEncerramento, boolean cascade){
		return remove(cdMotivoEncerramento, cascade, null);
	}
	public static Result remove(int cdMotivoEncerramento, boolean cascade, Connection connect){
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
			retorno = MotivoEncerramentoAITDAO.delete(cdMotivoEncerramento, connect);
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
	
	public static MotivoEncerramentoAIT get(int cdMotivoEncerramento) {
		return get(cdMotivoEncerramento, null);
	}
	
	public static MotivoEncerramentoAIT get(int cdMotivoEncerramento, Connection connect) {
		boolean isConnectionNull = connect==null;
		if(isConnectionNull)
			connect = Conexao.conectar();
		
		PreparedStatement pstmt;
		ResultSet rs;
		
		try {
			if(!Util.isStrBaseAntiga()) {
				pstmt = connect.prepareStatement("SELECT * FROM mob_motivos_encerramento WHERE cd_motivo_encerramento=?");
				pstmt.setInt(1, cdMotivoEncerramento);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					return new MotivoEncerramentoAIT(rs.getInt("cd_motivo_encerramento"),
							rs.getString("nm_motivo_encerramento"));
				}
			} else {
				pstmt = connect.prepareStatement("SELECT * FROM motivo_encerramento WHERE cd_motivo_encerramento=?");
				pstmt.setInt(1, cdMotivoEncerramento);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					return new MotivoEncerramentoAIT(rs.getInt("cd_motivo_encerramento"),
							rs.getString("nm_motivo_encerramento"));
				}
			}
			return null;
		} catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotivoEncerramentoAITDAO.get: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MotivoEncerramentoAITDAO.get: " + e);
			return null;
		}
		finally {
			if(isConnectionNull)
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_motivos_encerramento");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotivoEncerramentoAITServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MotivoEncerramentoAITServices.getAll: " + e);
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
		int qtRegistros = 0;
		
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
				qtRegistros = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
				i--;
			}
		}
		
		ResultSetMap rsm = Search.find("SELECT * FROM mob_motivos_encerramento", (qtRegistros > 0? " LIMIT " + qtRegistros : ""),
				criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		
		return rsm;
	}

}
