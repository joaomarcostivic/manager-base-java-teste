package com.tivic.manager.str;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class InfracaoTransporteDAO{

	public static int insert(InfracaoTransporte objeto) {
		return insert(objeto, null);
	}

	public static int insert(InfracaoTransporte objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("INFRACAO_TRANSPORTE", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdInfracao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO INFRACAO_TRANSPORTE (COD_INFRACAO,"+
			                                  "DS_INFRACAO,"+
			                                  "NR_PONTUACAO,"+
			                                  "NR_VALOR_UFIR,"+
			                                  "NM_NATUREZA,"+
			                                  "NR_ARTIGO,"+
			                                  "NR_PARAGRAFO,"+
			                                  "NR_INCISO,"+
			                                  "NR_ALINEA,"+
			                                  "TP_CONCESSAO,"+
			                                  "NR_INFRACAO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getDsInfracao());
			pstmt.setInt(3,objeto.getNrPontuacao());
			pstmt.setFloat(4,objeto.getNrValorUfir());
			pstmt.setString(5,objeto.getNmNatureza());
			pstmt.setString(6,objeto.getNrArtigo());
			pstmt.setString(7,objeto.getNrParagrafo());
			pstmt.setString(8,objeto.getNrInciso());
			pstmt.setString(9,objeto.getNrAlinea());
			pstmt.setInt(10,objeto.getTpConcessao());
			pstmt.setString(11,objeto.getNrInfracao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoTransporteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoTransporteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(InfracaoTransporte objeto) {
		return update(objeto, 0, null);
	}

	public static int update(InfracaoTransporte objeto, int cdInfracaoOld) {
		return update(objeto, cdInfracaoOld, null);
	}

	public static int update(InfracaoTransporte objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(InfracaoTransporte objeto, int cdInfracaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE INFRACAO_TRANSPORTE SET COD_INFRACAO=?,"+
												      		   "DS_INFRACAO=?,"+
												      		   "NR_PONTUACAO=?,"+
												      		   "NR_VALOR_UFIR=?,"+
												      		   "NM_NATUREZA=?,"+
												      		   "NR_ARTIGO=?,"+
												      		   "NR_PARAGRAFO=?,"+
												      		   "NR_INCISO=?,"+
												      		   "NR_ALINEA=?,"+
												      		   "TP_CONCESSAO=?,"+
												      		   "NR_INFRACAO=? WHERE COD_INFRACAO=?");
			pstmt.setInt(1,objeto.getCdInfracao());
			pstmt.setString(2,objeto.getDsInfracao());
			pstmt.setInt(3,objeto.getNrPontuacao());
			pstmt.setFloat(4,objeto.getNrValorUfir());
			pstmt.setString(5,objeto.getNmNatureza());
			pstmt.setString(6,objeto.getNrArtigo());
			pstmt.setString(7,objeto.getNrParagrafo());
			pstmt.setString(8,objeto.getNrInciso());
			pstmt.setString(9,objeto.getNrAlinea());
			pstmt.setInt(10,objeto.getTpConcessao());
			pstmt.setString(11,objeto.getNrInfracao());
			pstmt.setInt(12, cdInfracaoOld!=0 ? cdInfracaoOld : objeto.getCdInfracao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoTransporteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoTransporteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdInfracao) {
		return delete(cdInfracao, null);
	}

	public static int delete(int cdInfracao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM INFRACAO_TRANSPORTE WHERE COD_INFRACAO=?");
			pstmt.setInt(1, cdInfracao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoTransporteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoTransporteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static InfracaoTransporte get(int cdInfracao) {
		return get(cdInfracao, null);
	}

	public static InfracaoTransporte get(int cdInfracao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM INFRACAO_TRANSPORTE WHERE COD_INFRACAO=?");
			pstmt.setInt(1, cdInfracao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new InfracaoTransporte(rs.getInt("COD_INFRACAO"),
						rs.getString("DS_INFRACAO"),
						rs.getInt("NR_PONTUACAO"),
						rs.getFloat("NR_VALOR_UFIR"),
						rs.getString("NM_NATUREZA"),
						rs.getString("NR_ARTIGO"),
						rs.getString("NR_PARAGRAFO"),
						rs.getString("NR_INCISO"),
						rs.getString("NR_ALINEA"),
						rs.getInt("TP_CONCESSAO"),
						rs.getString("NR_INFRACAO"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoTransporteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoTransporteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM INFRACAO_TRANSPORTE");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoTransporteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoTransporteDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<InfracaoTransporte> getList() {
		return getList(null);
	}

	public static ArrayList<InfracaoTransporte> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<InfracaoTransporte> list = new ArrayList<InfracaoTransporte>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				InfracaoTransporte obj = InfracaoTransporteDAO.get(rsm.getInt("COD_INFRACAO"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoTransporteDAO.getList: " + e);
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
		return Search.find("SELECT * FROM INFRACAO_TRANSPORTE", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
