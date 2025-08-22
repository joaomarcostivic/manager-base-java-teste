package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class PessoaOcorrenciaDAO{

	public static int insert(PessoaOcorrencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(PessoaOcorrencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_ocorrencia");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_pessoa");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdPessoa()));
			int code = Conexao.getSequenceCode("grl_pessoa_ocorrencia", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdOcorrencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_ocorrencia (cd_ocorrencia,"+
			                                  "cd_pessoa,"+
			                                  "dt_ocorrencia,"+
			                                  "dt_lancamento,"+
			                                  "txt_ocorrencia,"+
			                                  "tp_visibilidade,"+
			                                  "cd_tipo_ocorrencia,"+
			                                  "cd_usuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			if(objeto.getDtLancamento()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtLancamento().getTimeInMillis()));
			pstmt.setString(5,objeto.getTxtOcorrencia());
			pstmt.setInt(6,objeto.getTpVisibilidade());
			if(objeto.getCdTipoOcorrencia()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdTipoOcorrencia());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdUsuario());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaOcorrenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaOcorrenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PessoaOcorrencia objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(PessoaOcorrencia objeto, int cdOcorrenciaOld, int cdPessoaOld) {
		return update(objeto, cdOcorrenciaOld, cdPessoaOld, null);
	}

	public static int update(PessoaOcorrencia objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(PessoaOcorrencia objeto, int cdOcorrenciaOld, int cdPessoaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_pessoa_ocorrencia SET cd_ocorrencia=?,"+
												      		   "cd_pessoa=?,"+
												      		   "dt_ocorrencia=?,"+
												      		   "dt_lancamento=?,"+
												      		   "txt_ocorrencia=?,"+
												      		   "tp_visibilidade=?,"+
												      		   "cd_tipo_ocorrencia=?,"+
												      		   "cd_usuario=? WHERE cd_ocorrencia=? AND cd_pessoa=?");
			pstmt.setInt(1,objeto.getCdOcorrencia());
			pstmt.setInt(2,objeto.getCdPessoa());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			if(objeto.getDtLancamento()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtLancamento().getTimeInMillis()));
			pstmt.setString(5,objeto.getTxtOcorrencia());
			pstmt.setInt(6,objeto.getTpVisibilidade());
			if(objeto.getCdTipoOcorrencia()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdTipoOcorrencia());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdUsuario());
			pstmt.setInt(9, cdOcorrenciaOld!=0 ? cdOcorrenciaOld : objeto.getCdOcorrencia());
			pstmt.setInt(10, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaOcorrenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaOcorrenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOcorrencia, int cdPessoa) {
		return delete(cdOcorrencia, cdPessoa, null);
	}

	public static int delete(int cdOcorrencia, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_pessoa_ocorrencia WHERE cd_ocorrencia=? AND cd_pessoa=?");
			pstmt.setInt(1, cdOcorrencia);
			pstmt.setInt(2, cdPessoa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaOcorrenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaOcorrenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PessoaOcorrencia get(int cdOcorrencia, int cdPessoa) {
		return get(cdOcorrencia, cdPessoa, null);
	}

	public static PessoaOcorrencia get(int cdOcorrencia, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_ocorrencia WHERE cd_ocorrencia=? AND cd_pessoa=?");
			pstmt.setInt(1, cdOcorrencia);
			pstmt.setInt(2, cdPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PessoaOcorrencia(rs.getInt("cd_ocorrencia"),
						rs.getInt("cd_pessoa"),
						(rs.getTimestamp("dt_ocorrencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ocorrencia").getTime()),
						(rs.getTimestamp("dt_lancamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_lancamento").getTime()),
						rs.getString("txt_ocorrencia"),
						rs.getInt("tp_visibilidade"),
						rs.getInt("cd_tipo_ocorrencia"),
						rs.getInt("cd_usuario"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaOcorrenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaOcorrenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_ocorrencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaOcorrenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaOcorrenciaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PessoaOcorrencia> getList() {
		return getList(null);
	}

	public static ArrayList<PessoaOcorrencia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PessoaOcorrencia> list = new ArrayList<PessoaOcorrencia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PessoaOcorrencia obj = PessoaOcorrenciaDAO.get(rsm.getInt("cd_ocorrencia"), rsm.getInt("cd_pessoa"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaOcorrenciaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_pessoa_ocorrencia", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
