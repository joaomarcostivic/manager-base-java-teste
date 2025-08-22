package com.tivic.manager.mob;

import java.sql.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InfracaoTransporteDAO{

	public static int insert(InfracaoTransporte objeto) {
		return insert(objeto, null);
	}

	public static int insert(InfracaoTransporte objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_infracao_transporte", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdInfracao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_infracao_transporte (cd_infracao,"+
			                                  "ds_infracao,"+
			                                  "nr_pontuacao,"+
			                                  "nr_valor_ufir,"+
			                                  "nm_natureza,"+
			                                  "nr_artigo,"+
			                                  "nr_paragrafo,"+
			                                  "nr_inciso,"+
			                                  "nr_alinea,"+
			                                  "tp_concessao,"+
			                                  "nr_infracao,"+
			                                  "lg_prioritario,"+
			                                  "tp_grupo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
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
			pstmt.setInt(12,objeto.getLgPrioritario());
			pstmt.setInt(13,objeto.getTpGrupo());
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
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_infracao_transporte SET cd_infracao=?,"+
												      		   "ds_infracao=?,"+
												      		   "nr_pontuacao=?,"+
												      		   "nr_valor_ufir=?,"+
												      		   "nm_natureza=?,"+
												      		   "nr_artigo=?,"+
												      		   "nr_paragrafo=?,"+
												      		   "nr_inciso=?,"+
												      		   "nr_alinea=?,"+
												      		   "tp_concessao=?,"+
												      		   "nr_infracao=?,"+
												      		   "lg_prioritario=?,"+
												      		   "tp_grupo=? WHERE cd_infracao=?");
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
			pstmt.setInt(12,objeto.getLgPrioritario());
			pstmt.setInt(13,objeto.getTpGrupo());
			pstmt.setInt(14, cdInfracaoOld!=0 ? cdInfracaoOld : objeto.getCdInfracao());
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
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_infracao_transporte WHERE cd_infracao=?");
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_infracao_transporte WHERE cd_infracao=?");
			pstmt.setInt(1, cdInfracao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new InfracaoTransporte(rs.getInt("cd_infracao"),
						rs.getString("ds_infracao"),
						rs.getInt("nr_pontuacao"),
						rs.getFloat("nr_valor_ufir"),
						rs.getString("nm_natureza"),
						rs.getString("nr_artigo"),
						rs.getString("nr_paragrafo"),
						rs.getString("nr_inciso"),
						rs.getString("nr_alinea"),
						rs.getInt("tp_concessao"),
						rs.getString("nr_infracao"),
						rs.getInt("lg_prioritario"),
						rs.getInt("tp_grupo"));
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_infracao_transporte");
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
				InfracaoTransporte obj = InfracaoTransporteDAO.get(rsm.getInt("cd_infracao"), connect);
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
		return Search.find("SELECT * FROM mob_infracao_transporte", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
