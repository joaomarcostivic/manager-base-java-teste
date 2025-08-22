package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.EstadoServices;
import com.tivic.manager.util.ResultSetMapper;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class ErroRetornoServices {

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_erro_retorno");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ErroRetornoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ErroRetornoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ErroRetorno getErroRetornoByMensagem(String dsErro) {
		return getErroRetornoByMensagem(dsErro, null);
	}
	
	public static ErroRetorno getErroRetornoByMensagem(String dsErro, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM mob_erro_Retorno WHERE ds_erro ILIKE ?");
			pstmt.setString(1, dsErro);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next()) { 
				List<ErroRetorno> erros = new ResultSetMapper<ErroRetorno>(rsm, ErroRetorno.class).toList();
				return erros.get(0);
			}
			
			return null;
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ErroRetorno getErroRetornoByCodigo(String nrErro) {
		return getErroRetornoByCodigo(nrErro, null);
	}
	
	public static ErroRetorno getErroRetornoByCodigo(String nrErro, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			String uf = EstadoServices.getEstadoOrgaoAutuador().getSgEstado();
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM mob_erro_Retorno WHERE (nr_erro = ? OR nr_erro = ? OR nr_erro = ?) AND uf ILIKE ?");
			pstmt.setString(1, nrErro);
			pstmt.setString(2, "0" + nrErro);
			pstmt.setString(3, "00" + nrErro);
			pstmt.setString(4, uf);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next()) { 
				List<ErroRetorno> erros = new ResultSetMapper<ErroRetorno>(rsm, ErroRetorno.class).toList();
				return erros.get(0);
			}
			
			return null;
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM mob_erro_retorno", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static Result updateRetorno(ErroRetorno erroRetorno) {
		return updateRetorno(erroRetorno, null);
	}
	
	public static Result updateRetorno(ErroRetorno erroRetorno, Connection connect) {
		boolean isConnectionNull = (connect == null);
		
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement("UPDATE MOB_ERRO_RETORNO SET tp_retorno = ?, ds_sugestao_correcao = ?, lg_correcao_automatica = ? "
					+ "WHERE nr_erro = ? AND uf = ?");
			pstmt.setInt(1, erroRetorno.getTpRetorno());
			pstmt.setString(2, erroRetorno.getDsSugestaoCorrecao());
			pstmt.setInt(3, erroRetorno.getLgCorrecaoAutomatica());
			pstmt.setString(4, erroRetorno.getNrErro());
			pstmt.setString(5, erroRetorno.getUf());
			
			int result = pstmt.executeUpdate();
			
			if(result > 0)
				return new Result(1, "Atualização efetuada com sucesso.");
			
			return new Result(0, "Erro no processo de atualização");			
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return new Result(-1, "Erro no processo de atualização de retorno");
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
