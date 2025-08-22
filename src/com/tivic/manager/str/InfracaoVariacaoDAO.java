package com.tivic.manager.str;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class InfracaoVariacaoDAO{

	public static int insert(InfracaoVariacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(InfracaoVariacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("STR_INFRACAO_VARIACAO", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdVariacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO STR_INFRACAO_VARIACAO (CD_VARIACAO,"+
			                                  "CD_INFRACAO,"+
			                                  "CD_INFRACAO_TRANSPORTE,"+
			                                  "TP_VARIACAO,"+
			                                  "VL_VARIACAO,"+
			                                  "TP_MULTIPLICADOR,"+
			                                  "NR_ORDEM,"+
			                                  "TXT_PROCEDIMENTO) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdInfracao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdInfracao());
			if(objeto.getCdInfracaoTransporte()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdInfracaoTransporte());
			pstmt.setInt(4,objeto.getTpVariacao());
			pstmt.setDouble(5,objeto.getVlVariacao());
			pstmt.setInt(6,objeto.getTpMultiplicador());
			pstmt.setInt(7,objeto.getNrOrdem());
			pstmt.setString(8,objeto.getTxtProcedimento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoVariacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoVariacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(InfracaoVariacao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(InfracaoVariacao objeto, int cdVariacaoOld) {
		return update(objeto, cdVariacaoOld, null);
	}

	public static int update(InfracaoVariacao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(InfracaoVariacao objeto, int cdVariacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE STR_INFRACAO_VARIACAO SET CD_VARIACAO=?,"+
												      		   "CD_INFRACAO=?,"+
												      		   "CD_INFRACAO_TRANSPORTE=?,"+
												      		   "TP_VARIACAO=?,"+
												      		   "VL_VARIACAO=?,"+
												      		   "TP_MULTIPLICADOR=?,"+
												      		   "NR_ORDEM=?,"+
												      		   "TXT_PROCEDIMENTO=? WHERE CD_VARIACAO=?");
			pstmt.setInt(1,objeto.getCdVariacao());
			if(objeto.getCdInfracao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdInfracao());
			if(objeto.getCdInfracaoTransporte()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdInfracaoTransporte());
			pstmt.setInt(4,objeto.getTpVariacao());
			pstmt.setDouble(5,objeto.getVlVariacao());
			pstmt.setInt(6,objeto.getTpMultiplicador());
			pstmt.setInt(7,objeto.getNrOrdem());
			pstmt.setString(8,objeto.getTxtProcedimento());
			pstmt.setInt(9, cdVariacaoOld!=0 ? cdVariacaoOld : objeto.getCdVariacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoVariacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoVariacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdVariacao) {
		return delete(cdVariacao, null);
	}

	public static int delete(int cdVariacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM STR_INFRACAO_VARIACAO WHERE CD_VARIACAO=?");
			pstmt.setInt(1, cdVariacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoVariacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoVariacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static InfracaoVariacao get(int cdVariacao) {
		return get(cdVariacao, null);
	}

	public static InfracaoVariacao get(int cdVariacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM STR_INFRACAO_VARIACAO WHERE CD_VARIACAO=?");
			pstmt.setInt(1, cdVariacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new InfracaoVariacao(rs.getInt("CD_VARIACAO"),
						rs.getInt("CD_INFRACAO"),
						rs.getInt("CD_INFRACAO_TRANSPORTE"),
						rs.getInt("TP_VARIACAO"),
						rs.getDouble("VL_VARIACAO"),
						rs.getInt("TP_MULTIPLICADOR"),
						rs.getInt("NR_ORDEM"),
						rs.getString("TXT_PROCEDIMENTO"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoVariacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoVariacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM STR_INFRACAO_VARIACAO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoVariacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoVariacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<InfracaoVariacao> getList() {
		return getList(null);
	}

	public static ArrayList<InfracaoVariacao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<InfracaoVariacao> list = new ArrayList<InfracaoVariacao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				InfracaoVariacao obj = InfracaoVariacaoDAO.get(rsm.getInt("CD_VARIACAO"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoVariacaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM STR_INFRACAO_VARIACAO", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
