package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoAndamentoDAO{

	public static int insert(TipoAndamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoAndamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("prc_tipo_andamento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoAndamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO prc_tipo_andamento (cd_tipo_andamento,"+
			                                  "nm_tipo_andamento,"+
			                                  "id_tipo_andamento,"+
			                                  "st_tipo_andamento,"+
			                                  "vl_padrao,"+
			                                  "tp_visibilidade,"+
			                                  "cd_tipo_situacao,"+
			                                  "tp_fato_gerador,"+
			                                  "lg_email,"+
			                                  "lg_email_cliente,"+
			                                  "lg_email_correspondente,"+
			                                  "lg_email_grupo_processo,"+
			                                  "st_cadastro,"+
			                                  "cd_tipo_andamento_superior) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoAndamento());
			pstmt.setString(3,objeto.getIdTipoAndamento());
			pstmt.setInt(4,objeto.getStTipoAndamento());
			pstmt.setDouble(5,objeto.getVlPadrao());
			pstmt.setInt(6,objeto.getTpVisibilidade());
			if(objeto.getCdTipoSituacao()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdTipoSituacao());
			pstmt.setInt(8,objeto.getTpFatoGerador());
			pstmt.setInt(9,objeto.getLgEmail());
			pstmt.setInt(10,objeto.getLgEmailCliente());
			pstmt.setInt(11,objeto.getLgEmailCorrespondente());
			pstmt.setInt(12,objeto.getLgEmailGrupoProcesso());
			pstmt.setInt(13,objeto.getStCadastro());
			if(objeto.getCdTipoAndamentoSuperior()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdTipoAndamentoSuperior());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAndamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAndamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoAndamento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoAndamento objeto, int cdTipoAndamentoOld) {
		return update(objeto, cdTipoAndamentoOld, null);
	}

	public static int update(TipoAndamento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoAndamento objeto, int cdTipoAndamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE prc_tipo_andamento SET cd_tipo_andamento=?,"+
												      		   "nm_tipo_andamento=?,"+
												      		   "id_tipo_andamento=?,"+
												      		   "st_tipo_andamento=?,"+
												      		   "vl_padrao=?,"+
												      		   "tp_visibilidade=?,"+
												      		   "cd_tipo_situacao=?,"+
												      		   "tp_fato_gerador=?,"+
												      		   "lg_email=?,"+
												      		   "lg_email_cliente=?,"+
												      		   "lg_email_correspondente=?,"+
												      		   "lg_email_grupo_processo=?,"+
												      		   "st_cadastro=?,"+
												      		   "cd_tipo_andamento_superior=? WHERE cd_tipo_andamento=?");
			pstmt.setInt(1,objeto.getCdTipoAndamento());
			pstmt.setString(2,objeto.getNmTipoAndamento());
			pstmt.setString(3,objeto.getIdTipoAndamento());
			pstmt.setInt(4,objeto.getStTipoAndamento());
			pstmt.setDouble(5,objeto.getVlPadrao());
			pstmt.setInt(6,objeto.getTpVisibilidade());
			if(objeto.getCdTipoSituacao()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdTipoSituacao());
			pstmt.setInt(8,objeto.getTpFatoGerador());
			pstmt.setInt(9,objeto.getLgEmail());
			pstmt.setInt(10,objeto.getLgEmailCliente());
			pstmt.setInt(11,objeto.getLgEmailCorrespondente());
			pstmt.setInt(12,objeto.getLgEmailGrupoProcesso());
			pstmt.setInt(13,objeto.getStCadastro());
			if(objeto.getCdTipoAndamentoSuperior()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdTipoAndamentoSuperior());
			pstmt.setInt(15, cdTipoAndamentoOld!=0 ? cdTipoAndamentoOld : objeto.getCdTipoAndamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAndamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAndamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoAndamento) {
		return delete(cdTipoAndamento, null);
	}

	public static int delete(int cdTipoAndamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_tipo_andamento WHERE cd_tipo_andamento=?");
			pstmt.setInt(1, cdTipoAndamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAndamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAndamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoAndamento get(int cdTipoAndamento) {
		return get(cdTipoAndamento, null);
	}

	public static TipoAndamento get(int cdTipoAndamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_tipo_andamento WHERE cd_tipo_andamento=?");
			pstmt.setInt(1, cdTipoAndamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoAndamento(rs.getInt("cd_tipo_andamento"),
						rs.getString("nm_tipo_andamento"),
						rs.getString("id_tipo_andamento"),
						rs.getInt("st_tipo_andamento"),
						rs.getDouble("vl_padrao"),
						rs.getInt("tp_visibilidade"),
						rs.getInt("cd_tipo_situacao"),
						rs.getInt("tp_fato_gerador"),
						rs.getInt("lg_email"),
						rs.getInt("lg_email_cliente"),
						rs.getInt("lg_email_correspondente"),
						rs.getInt("lg_email_grupo_processo"),
						rs.getInt("st_cadastro"),
						rs.getInt("cd_tipo_andamento_superior"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAndamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAndamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_tipo_andamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAndamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAndamentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoAndamento> getList() {
		return getList(null);
	}

	public static ArrayList<TipoAndamento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoAndamento> list = new ArrayList<TipoAndamento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoAndamento obj = TipoAndamentoDAO.get(rsm.getInt("cd_tipo_andamento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAndamentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM prc_tipo_andamento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}