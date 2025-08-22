package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoPrazoDAO{

	public static int insert(TipoPrazo objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoPrazo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("prc_tipo_prazo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoPrazo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO prc_tipo_prazo (cd_tipo_prazo,"+
			                                  "nm_tipo_prazo,"+
			                                  "cd_tipo_andamento,"+
			                                  "tp_agenda_item,"+
			                                  "cd_tipo_andamento_cumprimento,"+
			                                  "lg_documento_obrigatorio,"+
			                                  "lg_utiliza_modelo,"+
			                                  "cd_modelo,"+
			                                  "lg_email,"+
			                                  "cd_empresa,"+
			                                  "cd_tipo_documento_cumprimento,"+
			                                  "cd_pessoa_padrao,"+
			                                  "cd_grupo_trabalho_padrao,"+
			                                  "lg_agenda_editavel,"+
			                                  "st_tipo_prazo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoPrazo());
			if(objeto.getCdTipoAndamento()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoAndamento());
			pstmt.setInt(4,objeto.getTpAgendaItem());
			if(objeto.getCdTipoAndamentoCumprimento()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdTipoAndamentoCumprimento());
			pstmt.setInt(6,objeto.getLgDocumentoObrigatorio());
			pstmt.setInt(7,objeto.getLgUtilizaModelo());
			if(objeto.getCdModelo()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdModelo());
			pstmt.setInt(9,objeto.getLgEmail());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdEmpresa());
			if(objeto.getCdTipoDocumentoCumprimento()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdTipoDocumentoCumprimento());
			if(objeto.getCdPessoaPadrao()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdPessoaPadrao());
			if(objeto.getCdGrupoTrabalhoPadrao()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdGrupoTrabalhoPadrao());
			pstmt.setInt(14,objeto.getLgAgendaEditavel());
			pstmt.setInt(15,objeto.getStTipoPrazo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoPrazoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoPrazoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoPrazo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoPrazo objeto, int cdTipoPrazoOld) {
		return update(objeto, cdTipoPrazoOld, null);
	}

	public static int update(TipoPrazo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoPrazo objeto, int cdTipoPrazoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE prc_tipo_prazo SET cd_tipo_prazo=?,"+
												      		   "nm_tipo_prazo=?,"+
												      		   "cd_tipo_andamento=?,"+
												      		   "tp_agenda_item=?,"+
												      		   "cd_tipo_andamento_cumprimento=?,"+
												      		   "lg_documento_obrigatorio=?,"+
												      		   "lg_utiliza_modelo=?,"+
												      		   "cd_modelo=?,"+
												      		   "lg_email=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_tipo_documento_cumprimento=?,"+
												      		   "cd_pessoa_padrao=?,"+
												      		   "cd_grupo_trabalho_padrao=?,"+
												      		   "lg_agenda_editavel=?,"+
												      		   "st_tipo_prazo=? WHERE cd_tipo_prazo=?");
			pstmt.setInt(1,objeto.getCdTipoPrazo());
			pstmt.setString(2,objeto.getNmTipoPrazo());
			if(objeto.getCdTipoAndamento()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoAndamento());
			pstmt.setInt(4,objeto.getTpAgendaItem());
			if(objeto.getCdTipoAndamentoCumprimento()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdTipoAndamentoCumprimento());
			pstmt.setInt(6,objeto.getLgDocumentoObrigatorio());
			pstmt.setInt(7,objeto.getLgUtilizaModelo());
			if(objeto.getCdModelo()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdModelo());
			pstmt.setInt(9,objeto.getLgEmail());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdEmpresa());
			if(objeto.getCdTipoDocumentoCumprimento()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdTipoDocumentoCumprimento());
			if(objeto.getCdPessoaPadrao()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdPessoaPadrao());
			if(objeto.getCdGrupoTrabalhoPadrao()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdGrupoTrabalhoPadrao());
			pstmt.setInt(14,objeto.getLgAgendaEditavel());
			pstmt.setInt(15,objeto.getStTipoPrazo());
			pstmt.setInt(16, cdTipoPrazoOld!=0 ? cdTipoPrazoOld : objeto.getCdTipoPrazo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoPrazoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoPrazoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoPrazo) {
		return delete(cdTipoPrazo, null);
	}

	public static int delete(int cdTipoPrazo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_tipo_prazo WHERE cd_tipo_prazo=?");
			pstmt.setInt(1, cdTipoPrazo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoPrazoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoPrazoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoPrazo get(int cdTipoPrazo) {
		return get(cdTipoPrazo, null);
	}

	public static TipoPrazo get(int cdTipoPrazo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_tipo_prazo WHERE cd_tipo_prazo=?");
			pstmt.setInt(1, cdTipoPrazo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoPrazo(rs.getInt("cd_tipo_prazo"),
						rs.getString("nm_tipo_prazo"),
						rs.getInt("cd_tipo_andamento"),
						rs.getInt("tp_agenda_item"),
						rs.getInt("cd_tipo_andamento_cumprimento"),
						rs.getInt("lg_documento_obrigatorio"),
						rs.getInt("lg_utiliza_modelo"),
						rs.getInt("cd_modelo"),
						rs.getInt("lg_email"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_tipo_documento_cumprimento"),
						rs.getInt("cd_pessoa_padrao"),
						rs.getInt("cd_grupo_trabalho_padrao"),
						rs.getInt("lg_agenda_editavel"),
						rs.getInt("st_tipo_prazo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoPrazoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoPrazoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_tipo_prazo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoPrazoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoPrazoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoPrazo> getList() {
		return getList(null);
	}

	public static ArrayList<TipoPrazo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoPrazo> list = new ArrayList<TipoPrazo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoPrazo obj = TipoPrazoDAO.get(rsm.getInt("cd_tipo_prazo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoPrazoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM prc_tipo_prazo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}