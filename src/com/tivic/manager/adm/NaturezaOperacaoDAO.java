package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class NaturezaOperacaoDAO{

	public static int insert(NaturezaOperacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(NaturezaOperacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_natureza_operacao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdNaturezaOperacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_natureza_operacao (cd_natureza_operacao,"+
			                                  "nm_natureza_operacao,"+
			                                  "nr_codigo_fiscal," +
			                                  "cd_natureza_superior," +
			                                  "cd_natureza_entrada," +
			                                  "cd_natureza_entrada_substituicao) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmNaturezaOperacao());
			pstmt.setString(3,objeto.getNrCodigoFiscal());
			if(objeto.getCdNaturezaSuperior()>0)
				pstmt.setInt(4, objeto.getCdNaturezaSuperior());
			else
				pstmt.setNull(4, java.sql.Types.INTEGER);
			if(objeto.getCdNaturezaEntrada()>0)
				pstmt.setInt(5, objeto.getCdNaturezaEntrada());
			else
				pstmt.setNull(5, java.sql.Types.INTEGER);
			if(objeto.getCdNaturezaEntradaSubstituicao()>0)
				pstmt.setInt(6, objeto.getCdNaturezaEntradaSubstituicao());
			else
				pstmt.setNull(6, java.sql.Types.INTEGER);
			pstmt.executeUpdate();
			return code;
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

	public static int update(NaturezaOperacao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(NaturezaOperacao objeto, int cdNaturezaOperacaoOld) {
		return update(objeto, cdNaturezaOperacaoOld, null);
	}

	public static int update(NaturezaOperacao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(NaturezaOperacao objeto, int cdNaturezaOperacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_natureza_operacao SET cd_natureza_operacao=?,"+
												      		   "nm_natureza_operacao=?,"+
												      		   "nr_codigo_fiscal=?," +
												      		   "cd_natureza_superior=?," +
												      		   "cd_natureza_entrada=?," +
												      		   "cd_natureza_entrada_substituicao=? WHERE cd_natureza_operacao=?");
			pstmt.setInt(1,objeto.getCdNaturezaOperacao());
			pstmt.setString(2,objeto.getNmNaturezaOperacao());
			pstmt.setString(3,objeto.getNrCodigoFiscal());
			if(objeto.getCdNaturezaSuperior()>0)
				pstmt.setInt(4, objeto.getCdNaturezaSuperior());
			else
				pstmt.setNull(4, java.sql.Types.INTEGER);
			if(objeto.getCdNaturezaEntrada()>0)
				pstmt.setInt(5, objeto.getCdNaturezaEntrada());
			else
				pstmt.setNull(5, java.sql.Types.INTEGER);
			if(objeto.getCdNaturezaEntradaSubstituicao()>0)
				pstmt.setInt(6, objeto.getCdNaturezaEntradaSubstituicao());
			else
				pstmt.setNull(6, java.sql.Types.INTEGER);
			pstmt.setInt(7, cdNaturezaOperacaoOld!=0 ? cdNaturezaOperacaoOld : objeto.getCdNaturezaOperacao());
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

	public static int delete(int cdNaturezaOperacao) {
		return delete(cdNaturezaOperacao, null);
	}

	public static int delete(int cdNaturezaOperacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_natureza_operacao WHERE cd_natureza_operacao=?");
			pstmt.setInt(1, cdNaturezaOperacao);
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

	public static NaturezaOperacao get(int cdNaturezaOperacao) {
		return get(cdNaturezaOperacao, null);
	}

	public static NaturezaOperacao get(int cdNaturezaOperacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_natureza_operacao WHERE cd_natureza_operacao=?");
			pstmt.setInt(1, cdNaturezaOperacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new NaturezaOperacao(rs.getInt("cd_natureza_operacao"),
						rs.getString("nm_natureza_operacao"),
						rs.getString("nr_codigo_fiscal"),
						rs.getInt("cd_natureza_superior"),
						rs.getInt("cd_natureza_entrada"),
						rs.getInt("cd_natureza_entrada_substituicao"));
			}
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
		try {
			return new ResultSetMap(connect.prepareStatement("SELECT * FROM adm_natureza_operacao ORDER BY nr_codigo_fiscal").executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
		return Search.find("SELECT * FROM adm_natureza_operacao", "ORDER BY nr_codigo_fiscal", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
