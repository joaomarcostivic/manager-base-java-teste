package com.tivic.manager.ord;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class OrdemServicoAtendimentoDAO{

	public static int insert(OrdemServicoAtendimento objeto) {
		return insert(objeto, null);
	}

	public static int insert(OrdemServicoAtendimento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ord_ordem_servico_atendimento (cd_atendimento,"+
			                                  "cd_ordem_servico,"+
			                                  "lg_principal) VALUES (?, ?, ?)");
			if(objeto.getCdAtendimento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdAtendimento());
			if(objeto.getCdOrdemServico()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdOrdemServico());
			pstmt.setInt(3,objeto.getLgPrincipal());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoAtendimentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoAtendimentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(OrdemServicoAtendimento objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(OrdemServicoAtendimento objeto, int cdAtendimentoOld, int cdOrdemServicoOld) {
		return update(objeto, cdAtendimentoOld, cdOrdemServicoOld, null);
	}

	public static int update(OrdemServicoAtendimento objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(OrdemServicoAtendimento objeto, int cdAtendimentoOld, int cdOrdemServicoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ord_ordem_servico_atendimento SET cd_atendimento=?,"+
												      		   "cd_ordem_servico=?,"+
												      		   "lg_principal=? WHERE cd_atendimento=? AND cd_ordem_servico=?");
			pstmt.setInt(1,objeto.getCdAtendimento());
			pstmt.setInt(2,objeto.getCdOrdemServico());
			pstmt.setInt(3,objeto.getLgPrincipal());
			pstmt.setInt(4, cdAtendimentoOld!=0 ? cdAtendimentoOld : objeto.getCdAtendimento());
			pstmt.setInt(5, cdOrdemServicoOld!=0 ? cdOrdemServicoOld : objeto.getCdOrdemServico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoAtendimentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoAtendimentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAtendimento, int cdOrdemServico) {
		return delete(cdAtendimento, cdOrdemServico, null);
	}

	public static int delete(int cdAtendimento, int cdOrdemServico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ord_ordem_servico_atendimento WHERE cd_atendimento=? AND cd_ordem_servico=?");
			pstmt.setInt(1, cdAtendimento);
			pstmt.setInt(2, cdOrdemServico);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoAtendimentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoAtendimentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static OrdemServicoAtendimento get(int cdAtendimento, int cdOrdemServico) {
		return get(cdAtendimento, cdOrdemServico, null);
	}

	public static OrdemServicoAtendimento get(int cdAtendimento, int cdOrdemServico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ord_ordem_servico_atendimento WHERE cd_atendimento=? AND cd_ordem_servico=?");
			pstmt.setInt(1, cdAtendimento);
			pstmt.setInt(2, cdOrdemServico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new OrdemServicoAtendimento(rs.getInt("cd_atendimento"),
						rs.getInt("cd_ordem_servico"),
						rs.getInt("lg_principal"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoAtendimentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoAtendimentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ord_ordem_servico_atendimento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoAtendimentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoAtendimentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<OrdemServicoAtendimento> getList() {
		return getList(null);
	}

	public static ArrayList<OrdemServicoAtendimento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<OrdemServicoAtendimento> list = new ArrayList<OrdemServicoAtendimento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				OrdemServicoAtendimento obj = OrdemServicoAtendimentoDAO.get(rsm.getInt("cd_atendimento"), rsm.getInt("cd_ordem_servico"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoAtendimentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ord_ordem_servico_atendimento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
