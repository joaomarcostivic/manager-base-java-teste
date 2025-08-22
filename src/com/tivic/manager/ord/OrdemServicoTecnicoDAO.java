package com.tivic.manager.ord;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class OrdemServicoTecnicoDAO{

	public static int insert(OrdemServicoTecnico objeto) { 
		return insert(objeto, null);
	}

	public static int insert(OrdemServicoTecnico objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ord_ordem_servico_tecnico (cd_ordem_servico,"+
			                                  "cd_pessoa,"+
			                                  "lg_responsavel) VALUES (?, ?, ?)");
			if(objeto.getCdOrdemServico()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdOrdemServico());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setInt(3,objeto.getLgResponsavel());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoTecnicoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoTecnicoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(OrdemServicoTecnico objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(OrdemServicoTecnico objeto, int cdOrdemServicoOld, int cdPessoaOld) {
		return update(objeto, cdOrdemServicoOld, cdPessoaOld, null);
	}

	public static int update(OrdemServicoTecnico objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(OrdemServicoTecnico objeto, int cdOrdemServicoOld, int cdPessoaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ord_ordem_servico_tecnico SET cd_ordem_servico=?,"+
												      		   "cd_pessoa=?,"+
												      		   "lg_responsavel=? WHERE cd_ordem_servico=? AND cd_pessoa=?");
			pstmt.setInt(1,objeto.getCdOrdemServico());
			pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setInt(3,objeto.getLgResponsavel());
			pstmt.setInt(4, cdOrdemServicoOld!=0 ? cdOrdemServicoOld : objeto.getCdOrdemServico());
			pstmt.setInt(5, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoTecnicoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoTecnicoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOrdemServico, int cdPessoa) {
		return delete(cdOrdemServico, cdPessoa, null);
	}

	public static int delete(int cdOrdemServico, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ord_ordem_servico_tecnico WHERE cd_ordem_servico=? AND cd_pessoa=?");
			pstmt.setInt(1, cdOrdemServico);
			pstmt.setInt(2, cdPessoa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoTecnicoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoTecnicoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static OrdemServicoTecnico get(int cdOrdemServico, int cdPessoa) {
		return get(cdOrdemServico, cdPessoa, null);
	}

	public static OrdemServicoTecnico get(int cdOrdemServico, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ord_ordem_servico_tecnico WHERE cd_ordem_servico=? AND cd_pessoa=?");
			pstmt.setInt(1, cdOrdemServico);
			pstmt.setInt(2, cdPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new OrdemServicoTecnico(rs.getInt("cd_ordem_servico"),
						rs.getInt("cd_pessoa"),
						rs.getInt("lg_responsavel"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoTecnicoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoTecnicoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ord_ordem_servico_tecnico");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoTecnicoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoTecnicoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<OrdemServicoTecnico> getList() {
		return getList(null);
	}

	public static ArrayList<OrdemServicoTecnico> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<OrdemServicoTecnico> list = new ArrayList<OrdemServicoTecnico>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				OrdemServicoTecnico obj = OrdemServicoTecnicoDAO.get(rsm.getInt("cd_ordem_servico"), rsm.getInt("cd_pessoa"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoTecnicoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ord_ordem_servico_tecnico", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
