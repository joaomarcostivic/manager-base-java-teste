package com.tivic.manager.blb;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class PublicacaoDAO{

	public static int insert(Publicacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(Publicacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("blb_publicacao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPublicacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO blb_publicacao (cd_publicacao,"+
			                                  "cd_editora,"+
			                                  "nm_publicacao,"+
			                                  "nr_isbn,"+
			                                  "nr_edicao,"+
			                                  "nr_ano_publicacao,"+
			                                  "tp_publicacao,"+
			                                  "nr_volume,"+
			                                  "txt_sinopse,"+
			                                  "id_publicacao,"+
			                                  "nm_subtitulo,"+
			                                  "txt_observacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdEditora()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEditora());
			pstmt.setString(3,objeto.getNmPublicacao());
			pstmt.setString(4,objeto.getNrIsbn());
			pstmt.setString(5,objeto.getNrEdicao());
			pstmt.setString(6,objeto.getNrAnoPublicacao());
			pstmt.setInt(7,objeto.getTpPublicacao());
			pstmt.setInt(8,objeto.getNrVolume());
			pstmt.setString(9,objeto.getTxtSinopse());
			pstmt.setString(10,objeto.getIdPublicacao());
			pstmt.setString(11,objeto.getNmSubtitulo());
			pstmt.setString(12,objeto.getTxtObservacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Publicacao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Publicacao objeto, int cdPublicacaoOld) {
		return update(objeto, cdPublicacaoOld, null);
	}

	public static int update(Publicacao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Publicacao objeto, int cdPublicacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE blb_publicacao SET cd_publicacao=?,"+
												      		   "cd_editora=?,"+
												      		   "nm_publicacao=?,"+
												      		   "nr_isbn=?,"+
												      		   "nr_edicao=?,"+
												      		   "nr_ano_publicacao=?,"+
												      		   "tp_publicacao=?,"+
												      		   "nr_volume=?,"+
												      		   "txt_sinopse=?,"+
												      		   "id_publicacao=?,"+
												      		   "nm_subtitulo=?,"+
												      		   "txt_observacao=? WHERE cd_publicacao=?");
			pstmt.setInt(1,objeto.getCdPublicacao());
			if(objeto.getCdEditora()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEditora());
			pstmt.setString(3,objeto.getNmPublicacao());
			pstmt.setString(4,objeto.getNrIsbn());
			pstmt.setString(5,objeto.getNrEdicao());
			pstmt.setString(6,objeto.getNrAnoPublicacao());
			pstmt.setInt(7,objeto.getTpPublicacao());
			pstmt.setInt(8,objeto.getNrVolume());
			pstmt.setString(9,objeto.getTxtSinopse());
			pstmt.setString(10,objeto.getIdPublicacao());
			pstmt.setString(11,objeto.getNmSubtitulo());
			pstmt.setString(12,objeto.getTxtObservacao());
			pstmt.setInt(13, cdPublicacaoOld!=0 ? cdPublicacaoOld : objeto.getCdPublicacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPublicacao) {
		return delete(cdPublicacao, null);
	}

	public static int delete(int cdPublicacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM blb_publicacao WHERE cd_publicacao=?");
			pstmt.setInt(1, cdPublicacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Publicacao get(int cdPublicacao) {
		return get(cdPublicacao, null);
	}

	public static Publicacao get(int cdPublicacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM blb_publicacao WHERE cd_publicacao=?");
			pstmt.setInt(1, cdPublicacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Publicacao(rs.getInt("cd_publicacao"),
						rs.getInt("cd_editora"),
						rs.getString("nm_publicacao"),
						rs.getString("nr_isbn"),
						rs.getString("nr_edicao"),
						rs.getString("nr_ano_publicacao"),
						rs.getInt("tp_publicacao"),
						rs.getInt("nr_volume"),
						rs.getString("txt_sinopse"),
						rs.getString("id_publicacao"),
						rs.getString("nm_subtitulo"),
						rs.getString("txt_observacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM blb_publicacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Publicacao> getList() {
		return getList(null);
	}

	public static ArrayList<Publicacao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Publicacao> list = new ArrayList<Publicacao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Publicacao obj = PublicacaoDAO.get(rsm.getInt("cd_publicacao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM blb_publicacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
