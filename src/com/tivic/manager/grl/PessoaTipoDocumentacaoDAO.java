package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class PessoaTipoDocumentacaoDAO{

	public static int insert(PessoaTipoDocumentacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(PessoaTipoDocumentacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_tipo_documentacao (cd_pessoa,"+
			                                  "cd_tipo_documentacao,"+
			                                  "nr_documento,"+
			                                  "folha,"+
			                                  "livro,"+
			                                  "dt_emissao,"+
			                                  "cd_orgao_emissor,"+
			                                  "tp_modelo,"+
			                                  "cd_cartorio) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPessoa());
			if(objeto.getCdTipoDocumentacao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoDocumentacao());
			pstmt.setString(3,objeto.getNrDocumento());
			pstmt.setString(4,objeto.getFolha());
			pstmt.setString(5,objeto.getLivro());
			if(objeto.getDtEmissao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtEmissao().getTimeInMillis()));
			if(objeto.getCdOrgaoEmissor()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdOrgaoEmissor());
			pstmt.setInt(8,objeto.getTpModelo());
			if(objeto.getCdCartorio()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdCartorio());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaTipoDocumentacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaTipoDocumentacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PessoaTipoDocumentacao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(PessoaTipoDocumentacao objeto, int cdPessoaOld, int cdTipoDocumentacaoOld) {
		return update(objeto, cdPessoaOld, cdTipoDocumentacaoOld, null);
	}

	public static int update(PessoaTipoDocumentacao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(PessoaTipoDocumentacao objeto, int cdPessoaOld, int cdTipoDocumentacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_pessoa_tipo_documentacao SET cd_pessoa=?,"+
												      		   "cd_tipo_documentacao=?,"+
												      		   "nr_documento=?,"+
												      		   "folha=?,"+
												      		   "livro=?,"+
												      		   "dt_emissao=?,"+
												      		   "cd_orgao_emissor=?,"+
												      		   "tp_modelo=?,"+
												      		   "cd_cartorio=? WHERE cd_pessoa=? AND cd_tipo_documentacao=?");
			pstmt.setInt(1,objeto.getCdPessoa());
			pstmt.setInt(2,objeto.getCdTipoDocumentacao());
			pstmt.setString(3,objeto.getNrDocumento());
			pstmt.setString(4,objeto.getFolha());
			pstmt.setString(5,objeto.getLivro());
			if(objeto.getDtEmissao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtEmissao().getTimeInMillis()));
			if(objeto.getCdOrgaoEmissor()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdOrgaoEmissor());
			pstmt.setInt(8,objeto.getTpModelo());
			if(objeto.getCdCartorio()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdCartorio());
			pstmt.setInt(10, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.setInt(11, cdTipoDocumentacaoOld!=0 ? cdTipoDocumentacaoOld : objeto.getCdTipoDocumentacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaTipoDocumentacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaTipoDocumentacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPessoa, int cdTipoDocumentacao) {
		return delete(cdPessoa, cdTipoDocumentacao, null);
	}

	public static int delete(int cdPessoa, int cdTipoDocumentacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_pessoa_tipo_documentacao WHERE cd_pessoa=? AND cd_tipo_documentacao=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdTipoDocumentacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaTipoDocumentacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaTipoDocumentacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PessoaTipoDocumentacao get(int cdPessoa, int cdTipoDocumentacao) {
		return get(cdPessoa, cdTipoDocumentacao, null);
	}

	public static PessoaTipoDocumentacao get(int cdPessoa, int cdTipoDocumentacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_tipo_documentacao WHERE cd_pessoa=? AND cd_tipo_documentacao=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdTipoDocumentacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PessoaTipoDocumentacao(rs.getInt("cd_pessoa"),
						rs.getInt("cd_tipo_documentacao"),
						rs.getString("nr_documento"),
						rs.getString("folha"),
						rs.getString("livro"),
						(rs.getTimestamp("dt_emissao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_emissao").getTime()),
						rs.getInt("cd_orgao_emissor"),
						rs.getInt("tp_modelo"),
						rs.getInt("cd_cartorio"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaTipoDocumentacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaTipoDocumentacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_tipo_documentacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaTipoDocumentacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaTipoDocumentacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PessoaTipoDocumentacao> getList() {
		return getList(null);
	}

	public static ArrayList<PessoaTipoDocumentacao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PessoaTipoDocumentacao> list = new ArrayList<PessoaTipoDocumentacao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PessoaTipoDocumentacao obj = PessoaTipoDocumentacaoDAO.get(rsm.getInt("cd_pessoa"), rsm.getInt("cd_tipo_documentacao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaTipoDocumentacaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_pessoa_tipo_documentacao", "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
