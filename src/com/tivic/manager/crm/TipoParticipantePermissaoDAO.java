package com.tivic.manager.crm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TipoParticipantePermissaoDAO{

	public static int insert(TipoParticipantePermissao objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoParticipantePermissao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO crm_tipo_participante_permissao (cd_tipo_participante,"+
			                                  "cd_tipo_ocorrencia,"+
			                                  "lg_ativo) VALUES (?, ?, ?)");
			if(objeto.getCdTipoParticipante()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdTipoParticipante());
			if(objeto.getCdTipoOcorrencia()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoOcorrencia());
			pstmt.setInt(3,objeto.getLgAtivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoParticipantePermissaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoParticipantePermissaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoParticipantePermissao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(TipoParticipantePermissao objeto, int cdTipoParticipanteOld, int cdTipoOcorrenciaOld) {
		return update(objeto, cdTipoParticipanteOld, cdTipoOcorrenciaOld, null);
	}

	public static int update(TipoParticipantePermissao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(TipoParticipantePermissao objeto, int cdTipoParticipanteOld, int cdTipoOcorrenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE crm_tipo_participante_permissao SET cd_tipo_participante=?,"+
												      		   "cd_tipo_ocorrencia=?,"+
												      		   "lg_ativo=? WHERE cd_tipo_participante=? AND cd_tipo_ocorrencia=?");
			pstmt.setInt(1,objeto.getCdTipoParticipante());
			pstmt.setInt(2,objeto.getCdTipoOcorrencia());
			pstmt.setInt(3,objeto.getLgAtivo());
			pstmt.setInt(4, cdTipoParticipanteOld!=0 ? cdTipoParticipanteOld : objeto.getCdTipoParticipante());
			pstmt.setInt(5, cdTipoOcorrenciaOld!=0 ? cdTipoOcorrenciaOld : objeto.getCdTipoOcorrencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoParticipantePermissaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoParticipantePermissaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoParticipante, int cdTipoOcorrencia) {
		return delete(cdTipoParticipante, cdTipoOcorrencia, null);
	}

	public static int delete(int cdTipoParticipante, int cdTipoOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM crm_tipo_participante_permissao WHERE cd_tipo_participante=? AND cd_tipo_ocorrencia=?");
			pstmt.setInt(1, cdTipoParticipante);
			pstmt.setInt(2, cdTipoOcorrencia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoParticipantePermissaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoParticipantePermissaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoParticipantePermissao get(int cdTipoParticipante, int cdTipoOcorrencia) {
		return get(cdTipoParticipante, cdTipoOcorrencia, null);
	}

	public static TipoParticipantePermissao get(int cdTipoParticipante, int cdTipoOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM crm_tipo_participante_permissao WHERE cd_tipo_participante=? AND cd_tipo_ocorrencia=?");
			pstmt.setInt(1, cdTipoParticipante);
			pstmt.setInt(2, cdTipoOcorrencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoParticipantePermissao(rs.getInt("cd_tipo_participante"),
						rs.getInt("cd_tipo_ocorrencia"),
						rs.getInt("lg_ativo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoParticipantePermissaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoParticipantePermissaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM crm_tipo_participante_permissao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoParticipantePermissaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoParticipantePermissaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM crm_tipo_participante_permissao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
