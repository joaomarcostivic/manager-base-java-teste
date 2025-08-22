package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoOperacaoCfopDAO{

	public static int insert(TipoOperacaoCfop objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoOperacaoCfop objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_tipo_operacao_cfop (cd_tipo_operacao,"+
			                                  "cd_natureza_operacao,"+
			                                  "cd_empresa,"+
			                                  "cd_estado,"+
			                                  "cd_ncm,"+
			                                  "cd_classificacao_fiscal) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdTipoOperacao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdTipoOperacao());
			if(objeto.getCdNaturezaOperacao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdNaturezaOperacao());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			if(objeto.getCdEstado()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdEstado());
			if(objeto.getCdNcm()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdNcm());
			if(objeto.getCdClassificacaoFiscal()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdClassificacaoFiscal());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOperacaoCfopDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOperacaoCfopDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoOperacaoCfop objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(TipoOperacaoCfop objeto, int cdTipoOperacaoOld, int cdNaturezaOperacaoOld) {
		return update(objeto, cdTipoOperacaoOld, cdNaturezaOperacaoOld, null);
	}

	public static int update(TipoOperacaoCfop objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(TipoOperacaoCfop objeto, int cdTipoOperacaoOld, int cdNaturezaOperacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_tipo_operacao_cfop SET cd_tipo_operacao=?,"+
												      		   "cd_natureza_operacao=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_estado=?,"+
												      		   "cd_ncm=?,"+
												      		   "cd_classificacao_fiscal=? WHERE cd_tipo_operacao=? AND cd_natureza_operacao=?");
			pstmt.setInt(1,objeto.getCdTipoOperacao());
			pstmt.setInt(2,objeto.getCdNaturezaOperacao());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			if(objeto.getCdEstado()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdEstado());
			if(objeto.getCdNcm()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdNcm());
			if(objeto.getCdClassificacaoFiscal()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdClassificacaoFiscal());
			pstmt.setInt(7, cdTipoOperacaoOld!=0 ? cdTipoOperacaoOld : objeto.getCdTipoOperacao());
			pstmt.setInt(8, cdNaturezaOperacaoOld!=0 ? cdNaturezaOperacaoOld : objeto.getCdNaturezaOperacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOperacaoCfopDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOperacaoCfopDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoOperacao, int cdNaturezaOperacao) {
		return delete(cdTipoOperacao, cdNaturezaOperacao, null);
	}

	public static int delete(int cdTipoOperacao, int cdNaturezaOperacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_tipo_operacao_cfop WHERE cd_tipo_operacao=? AND cd_natureza_operacao=?");
			pstmt.setInt(1, cdTipoOperacao);
			pstmt.setInt(2, cdNaturezaOperacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOperacaoCfopDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOperacaoCfopDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoOperacaoCfop get(int cdTipoOperacao, int cdNaturezaOperacao) {
		return get(cdTipoOperacao, cdNaturezaOperacao, null);
	}

	public static TipoOperacaoCfop get(int cdTipoOperacao, int cdNaturezaOperacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_tipo_operacao_cfop WHERE cd_tipo_operacao=? AND cd_natureza_operacao=?");
			pstmt.setInt(1, cdTipoOperacao);
			pstmt.setInt(2, cdNaturezaOperacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoOperacaoCfop(rs.getInt("cd_tipo_operacao"),
						rs.getInt("cd_natureza_operacao"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_estado"),
						rs.getInt("cd_ncm"),
						rs.getInt("cd_classificacao_fiscal"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOperacaoCfopDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOperacaoCfopDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_tipo_operacao_cfop");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOperacaoCfopDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOperacaoCfopDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_tipo_operacao_cfop", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
