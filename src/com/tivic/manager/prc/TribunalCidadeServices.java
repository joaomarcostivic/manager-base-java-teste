package com.tivic.manager.prc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class TribunalCidadeServices {
	public static Result save(TribunalCidade cidade){
		return save(cidade, null);
	}
	
	public static Result save(TribunalCidade cidade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(cidade==null)
				return new Result(-1, "Erro ao salvar. Dados enviados são nulos");
			
			int retorno;
			
			if(TribunalCidadeDAO.get(cidade.getCdCidade(), cidade.getCdTribunal(), connect) == null){
				retorno = TribunalCidadeDAO.insert(cidade, connect);
			}
			else {
				retorno = TribunalCidadeDAO.update(cidade, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TRIBUNALCIDADE", cidade);
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
	
	public static Result remove(int cdCidade, int cdTribunal){
		return remove(cdCidade, cdTribunal, false, null);
	}
	
	public static Result remove(int cdCidade, int cdTribunal, boolean cascade){
		return remove(cdCidade, cdTribunal, cascade, null);
	}
	
	public static Result remove(int cdCidade, int cdTribunal, boolean cascade, Connection connect){
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
				retorno = TribunalCidadeDAO.delete(cdCidade, cdTribunal, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros registros e não pode ser excluído!");
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

	public static ResultSetMap getAllByTribunal(int cdTribunal) {
		return getAllByTribunal(cdTribunal, null, null);
	}

	public static ResultSetMap getAllByTribunalOrigem(int cdTribunal, String nrOrigem) {
		return getAllByTribunal(cdTribunal, nrOrigem, null);
	}
	
	public static ResultSetMap getAllByTribunal(int cdTribunal, String nrOrigem, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_cidade, B1.sg_estado, C.nm_tribunal FROM prc_tribunal_cidade A " +
					"LEFT OUTER JOIN grl_cidade B ON (B.cd_cidade = A.cd_cidade) " +
					"LEFT OUTER JOIN grl_estado B1 ON (B.cd_estado = B1.cd_estado) "+
					"LEFT OUTER JOIN prc_tribunal C ON (C.cd_tribunal = A.cd_tribunal) " +
					"WHERE A.cd_tribunal = ? " +
					((nrOrigem!=null)?" AND id_unidade = '"+nrOrigem+"'":"") +
					" ORDER BY B.nm_cidade");
			pstmt.setInt(1, cdTribunal);
			return new ResultSetMap(pstmt.executeQuery());
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
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return TribunalCidadeDAO.find(criterios, connect);
	}
}
