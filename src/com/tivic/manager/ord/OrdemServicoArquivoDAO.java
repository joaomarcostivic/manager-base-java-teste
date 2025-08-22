package com.tivic.manager.ord;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class OrdemServicoArquivoDAO{

	public static int insert(OrdemServicoArquivo objeto) {
		return insert(objeto, null);
	}

	public static int insert(OrdemServicoArquivo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ord_ordem_servico_arquivo (cd_ordem_servico,"+
			                                  "cd_arquivo) VALUES (?, ?)");
			if(objeto.getCdOrdemServico()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdOrdemServico());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdArquivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoArquivoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoArquivoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(OrdemServicoArquivo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(OrdemServicoArquivo objeto, int cdOrdemServicoOld, int cdArquivoOld) {
		return update(objeto, cdOrdemServicoOld, cdArquivoOld, null);
	}

	public static int update(OrdemServicoArquivo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(OrdemServicoArquivo objeto, int cdOrdemServicoOld, int cdArquivoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ord_ordem_servico_arquivo SET cd_ordem_servico=?,"+
												      		   "cd_arquivo=? WHERE cd_ordem_servico=? AND cd_arquivo=?");
			pstmt.setInt(1,objeto.getCdOrdemServico());
			pstmt.setInt(2,objeto.getCdArquivo());
			pstmt.setInt(3, cdOrdemServicoOld!=0 ? cdOrdemServicoOld : objeto.getCdOrdemServico());
			pstmt.setInt(4, cdArquivoOld!=0 ? cdArquivoOld : objeto.getCdArquivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoArquivoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoArquivoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOrdemServico, int cdArquivo) {
		return delete(cdOrdemServico, cdArquivo, null);
	}

	public static int delete(int cdOrdemServico, int cdArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ord_ordem_servico_arquivo WHERE cd_ordem_servico=? AND cd_arquivo=?");
			pstmt.setInt(1, cdOrdemServico);
			pstmt.setInt(2, cdArquivo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoArquivoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoArquivoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static OrdemServicoArquivo get(int cdOrdemServico, int cdArquivo) {
		return get(cdOrdemServico, cdArquivo, null);
	}

	public static OrdemServicoArquivo get(int cdOrdemServico, int cdArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ord_ordem_servico_arquivo WHERE cd_ordem_servico=? AND cd_arquivo=?");
			pstmt.setInt(1, cdOrdemServico);
			pstmt.setInt(2, cdArquivo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new OrdemServicoArquivo(rs.getInt("cd_ordem_servico"),
						rs.getInt("cd_arquivo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoArquivoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoArquivoDAO.get: " + e);
			return null;
		}
		finally {
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
			pstmt = connect.prepareStatement("SELECT * FROM ord_ordem_servico_arquivo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoArquivoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoArquivoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<OrdemServicoArquivo> getList() {
		return getList(null);
	}

	public static ArrayList<OrdemServicoArquivo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<OrdemServicoArquivo> list = new ArrayList<OrdemServicoArquivo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				OrdemServicoArquivo obj = OrdemServicoArquivoDAO.get(rsm.getInt("cd_ordem_servico"), rsm.getInt("cd_arquivo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoArquivoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ord_ordem_servico_arquivo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}