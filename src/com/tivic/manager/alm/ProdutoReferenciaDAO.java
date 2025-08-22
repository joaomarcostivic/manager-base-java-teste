package com.tivic.manager.alm;

import java.sql.*;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;

import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ProdutoReferenciaDAO{

	public static int insert(ProdutoReferencia objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(ProdutoReferencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_referencia");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_produto_servico");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdProdutoServico()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_empresa");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdEmpresa()));
			int code = Conexao.getSequenceCode("alm_produto_referencia", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdReferencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO alm_produto_referencia (cd_referencia,"+
			                                  "cd_produto_servico,"+
			                                  "cd_empresa,"+
			                                  "nm_referencia,"+
			                                  "id_referencia,"+
			                                  "dt_validade,"+
			                                  "dt_chegada,"+
			                                  "tp_referencia,"+
			                                  "st_referencia,"+
			                                  "cd_referencia_superior,"+
			                                  "nr_nivel,"+
			                                  "id_reduzido," +
			                                  "cd_local_armazenamento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProdutoServico());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			pstmt.setString(4,objeto.getNmReferencia());
			pstmt.setString(5,objeto.getIdReferencia());
			if(objeto.getDtValidade()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtValidade().getTimeInMillis()));
			if(objeto.getDtChegada()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtChegada().getTimeInMillis()));
			pstmt.setInt(8,objeto.getTpReferencia());
			pstmt.setInt(9,objeto.getStReferencia());
			if(objeto.getCdReferenciaSuperior()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdReferenciaSuperior());
			pstmt.setInt(11,objeto.getNrNivel());
			pstmt.setString(12,objeto.getIdReduzido());
			if(objeto.getCdLocalArmazenamento()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdLocalArmazenamento());
			pstmt.executeUpdate();
			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoReferenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProdutoReferencia objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(ProdutoReferencia objeto, int cdReferenciaOld, int cdProdutoServicoOld, int cdEmpresaOld) {
		return update(objeto, cdReferenciaOld, cdProdutoServicoOld, cdEmpresaOld, null);
	}

	public static int update(ProdutoReferencia objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(ProdutoReferencia objeto, int cdReferenciaOld, int cdProdutoServicoOld, int cdEmpresaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE alm_produto_referencia SET cd_referencia=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_empresa=?,"+
												      		   "nm_referencia=?,"+
												      		   "id_referencia=?,"+
												      		   "dt_validade=?,"+
												      		   "dt_chegada=?,"+
												      		   "tp_referencia=?,"+
												      		   "st_referencia=?,"+
												      		   "cd_referencia_superior=?,"+
												      		   "nr_nivel=?,"+
												      		   "id_reduzido=?," +
												      		   "cd_local_armazenamento=? WHERE cd_referencia=? AND cd_produto_servico=? AND cd_empresa=?");
			pstmt.setInt(1,objeto.getCdReferencia());
			pstmt.setInt(2,objeto.getCdProdutoServico());
			pstmt.setInt(3,objeto.getCdEmpresa());
			pstmt.setString(4,objeto.getNmReferencia());
			pstmt.setString(5,objeto.getIdReferencia());
			if(objeto.getDtValidade()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtValidade().getTimeInMillis()));
			if(objeto.getDtChegada()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtChegada().getTimeInMillis()));
			pstmt.setInt(8,objeto.getTpReferencia());
			pstmt.setInt(9,objeto.getStReferencia());
			if(objeto.getCdReferenciaSuperior()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdReferenciaSuperior());
			pstmt.setInt(11,objeto.getNrNivel());
			pstmt.setString(12,objeto.getIdReduzido());
			if(objeto.getCdLocalArmazenamento()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdLocalArmazenamento());
			pstmt.setInt(14, cdReferenciaOld!=0 ? cdReferenciaOld : objeto.getCdReferencia());
			pstmt.setInt(15, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.setInt(16, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoReferenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdReferencia, int cdProdutoServico, int cdEmpresa) {
		return delete(cdReferencia, cdProdutoServico, cdEmpresa, null);
	}

	public static int delete(int cdReferencia, int cdProdutoServico, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_produto_referencia WHERE cd_referencia=? AND cd_produto_servico=? AND cd_empresa=?");
			pstmt.setInt(1, cdReferencia);
			pstmt.setInt(2, cdProdutoServico);
			pstmt.setInt(3, cdEmpresa);
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProdutoReferencia get(int cdReferencia, int cdProdutoServico, int cdEmpresa) {
		return get(cdReferencia, cdProdutoServico, cdEmpresa, null);
	}
	
	public static ProdutoReferencia get(int cdReferencia, int cdProdutoServico, int cdEmpresa, String nmReferencia){
		return get(cdReferencia, cdProdutoServico, cdEmpresa, nmReferencia, null);
	}
	
	public static ProdutoReferencia get(int cdReferencia, int cdProdutoServico, int cdEmpresa, String nmReferencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSetMap rs;
		try {
			if(nmReferencia == null || nmReferencia == "") {
				pstmt = connect.prepareStatement("SELECT * FROM alm_produto_referencia WHERE cd_referencia=? AND cd_produto_servico=? AND cd_empresa=?");
				pstmt.setInt(1, cdReferencia);
				pstmt.setInt(2, cdProdutoServico);
				pstmt.setInt(3, cdEmpresa);
			}
			else {
				pstmt = connect.prepareStatement("SELECT * FROM alm_produto_referencia WHERE cd_produto_servico=? AND cd_empresa=? AND nm_referencia like ('%"+nmReferencia+"%')");
				pstmt.setInt(1, cdProdutoServico);
				pstmt.setInt(2, cdEmpresa);
			}
			rs = new ResultSetMap(pstmt.executeQuery());
			if(rs.next()){
				return new ProdutoReferencia(rs.getInt("cd_referencia"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_empresa"),
						rs.getString("nm_referencia"),
						rs.getString("id_referencia"),
						(rs.getTimestamp("dt_validade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_validade").getTime()),
						(rs.getTimestamp("dt_chegada")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_chegada").getTime()),
						rs.getInt("tp_referencia"),
						rs.getInt("st_referencia"),
						rs.getInt("cd_referencia_superior"),
						rs.getInt("nr_nivel"),
						rs.getString("id_reduzido"),
						rs.getInt("cd_local_armazenamento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoReferenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoReferenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM alm_produto_referencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoReferenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoReferenciaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM alm_produto_referencia", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
