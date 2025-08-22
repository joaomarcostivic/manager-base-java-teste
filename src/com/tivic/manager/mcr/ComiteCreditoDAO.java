package com.tivic.manager.mcr;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ComiteCreditoDAO{

	public static int insert(ComiteCredito objeto) {
		return insert(objeto, null);
	}

	public static int insert(ComiteCredito objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mcr_comite_credito (cd_ata_comite,"+
			                                  "cd_pessoa,"+
			                                  "cd_proposta,"+
			                                  "vl_sugerido) VALUES (?, ?, ?, ?)");
			if(objeto.getCdAtaComite()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdAtaComite());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			if(objeto.getCdProposta()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdProposta());
			pstmt.setFloat(4,objeto.getVlSugerido());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ComiteCreditoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ComiteCreditoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ComiteCredito objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(ComiteCredito objeto, int cdAtaComiteOld, int cdPessoaOld, int cdPropostaOld) {
		return update(objeto, cdAtaComiteOld, cdPessoaOld, cdPropostaOld, null);
	}

	public static int update(ComiteCredito objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(ComiteCredito objeto, int cdAtaComiteOld, int cdPessoaOld, int cdPropostaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mcr_comite_credito SET cd_ata_comite=?,"+
												      		   "cd_pessoa=?,"+
												      		   "cd_proposta=?,"+
												      		   "vl_sugerido=? WHERE cd_ata_comite=? AND cd_pessoa=? AND cd_proposta=?");
			pstmt.setInt(1,objeto.getCdAtaComite());
			pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setInt(3,objeto.getCdProposta());
			pstmt.setFloat(4,objeto.getVlSugerido());
			pstmt.setInt(5, cdAtaComiteOld!=0 ? cdAtaComiteOld : objeto.getCdAtaComite());
			pstmt.setInt(6, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.setInt(7, cdPropostaOld!=0 ? cdPropostaOld : objeto.getCdProposta());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ComiteCreditoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ComiteCreditoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAtaComite, int cdPessoa, int cdProposta) {
		return delete(cdAtaComite, cdPessoa, cdProposta, null);
	}

	public static int delete(int cdAtaComite, int cdPessoa, int cdProposta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mcr_comite_credito WHERE cd_ata_comite=? AND cd_pessoa=? AND cd_proposta=?");
			pstmt.setInt(1, cdAtaComite);
			pstmt.setInt(2, cdPessoa);
			pstmt.setInt(3, cdProposta);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ComiteCreditoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ComiteCreditoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ComiteCredito get(int cdAtaComite, int cdPessoa, int cdProposta) {
		return get(cdAtaComite, cdPessoa, cdProposta, null);
	}

	public static ComiteCredito get(int cdAtaComite, int cdPessoa, int cdProposta, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mcr_comite_credito WHERE cd_ata_comite=? AND cd_pessoa=? AND cd_proposta=?");
			pstmt.setInt(1, cdAtaComite);
			pstmt.setInt(2, cdPessoa);
			pstmt.setInt(3, cdProposta);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ComiteCredito(rs.getInt("cd_ata_comite"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_proposta"),
						rs.getFloat("vl_sugerido"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ComiteCreditoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ComiteCreditoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mcr_comite_credito");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ComiteCreditoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ComiteCreditoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM mcr_comite_credito", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
