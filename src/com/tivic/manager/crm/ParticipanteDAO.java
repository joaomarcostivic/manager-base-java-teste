package com.tivic.manager.crm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ParticipanteDAO{

	public static int insert(Participante objeto) {
		return insert(objeto, null);
	}

	public static int insert(Participante objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO crm_participante (cd_central,"+
			                                  "cd_usuario,"+
			                                  "cd_atendimento,"+
			                                  "dt_admissao,"+
			                                  "cd_tipo_participante) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdCentral()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdCentral());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdUsuario());
			if(objeto.getCdAtendimento()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAtendimento());
			if(objeto.getDtAdmissao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtAdmissao().getTimeInMillis()));
			if(objeto.getCdTipoParticipante()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdTipoParticipante());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParticipanteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ParticipanteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Participante objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(Participante objeto, int cdCentralOld, int cdUsuarioOld, int cdAtendimentoOld) {
		return update(objeto, cdCentralOld, cdUsuarioOld, cdAtendimentoOld, null);
	}

	public static int update(Participante objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(Participante objeto, int cdCentralOld, int cdUsuarioOld, int cdAtendimentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE crm_participante SET cd_central=?,"+
												      		   "cd_usuario=?,"+
												      		   "cd_atendimento=?,"+
												      		   "dt_admissao=?,"+
												      		   "cd_tipo_participante=? WHERE cd_central=? AND cd_usuario=? AND cd_atendimento=?");
			pstmt.setInt(1,objeto.getCdCentral());
			pstmt.setInt(2,objeto.getCdUsuario());
			pstmt.setInt(3,objeto.getCdAtendimento());
			if(objeto.getDtAdmissao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtAdmissao().getTimeInMillis()));
			if(objeto.getCdTipoParticipante()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdTipoParticipante());
			pstmt.setInt(6, cdCentralOld!=0 ? cdCentralOld : objeto.getCdCentral());
			pstmt.setInt(7, cdUsuarioOld!=0 ? cdUsuarioOld : objeto.getCdUsuario());
			pstmt.setInt(8, cdAtendimentoOld!=0 ? cdAtendimentoOld : objeto.getCdAtendimento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParticipanteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ParticipanteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCentral, int cdUsuario, int cdAtendimento) {
		return delete(cdCentral, cdUsuario, cdAtendimento, null);
	}

	public static int delete(int cdCentral, int cdUsuario, int cdAtendimento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM crm_participante WHERE cd_central=? AND cd_usuario=? AND cd_atendimento=?");
			pstmt.setInt(1, cdCentral);
			pstmt.setInt(2, cdUsuario);
			pstmt.setInt(3, cdAtendimento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParticipanteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ParticipanteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Participante get(int cdCentral, int cdUsuario, int cdAtendimento) {
		return get(cdCentral, cdUsuario, cdAtendimento, null);
	}

	public static Participante get(int cdCentral, int cdUsuario, int cdAtendimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM crm_participante WHERE cd_central=? AND cd_usuario=? AND cd_atendimento=?");
			pstmt.setInt(1, cdCentral);
			pstmt.setInt(2, cdUsuario);
			pstmt.setInt(3, cdAtendimento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Participante(rs.getInt("cd_central"),
						rs.getInt("cd_usuario"),
						rs.getInt("cd_atendimento"),
						(rs.getTimestamp("dt_admissao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_admissao").getTime()),
						rs.getInt("cd_tipo_participante"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParticipanteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParticipanteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM crm_participante");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParticipanteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParticipanteDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM crm_participante", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
