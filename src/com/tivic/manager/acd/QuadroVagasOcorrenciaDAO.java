package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class QuadroVagasOcorrenciaDAO{

	public static int insert(QuadroVagasOcorrencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(QuadroVagasOcorrencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_quadro_vagas_ocorrencia (cd_quadro_vagas,"+
			                                  "cd_instituicao,"+
			                                  "cd_ocorrencia) VALUES (?, ?, ?)");
			if(objeto.getCdQuadroVagas()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdQuadroVagas());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdInstituicao());
			if(objeto.getCdOcorrencia()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdOcorrencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasOcorrenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasOcorrenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(QuadroVagasOcorrencia objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(QuadroVagasOcorrencia objeto, int cdQuadroVagasOld, int cdInstituicaoOld, int cdOcorrenciaOld) {
		return update(objeto, cdQuadroVagasOld, cdInstituicaoOld, cdOcorrenciaOld, null);
	}

	public static int update(QuadroVagasOcorrencia objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(QuadroVagasOcorrencia objeto, int cdQuadroVagasOld, int cdInstituicaoOld, int cdOcorrenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_quadro_vagas_ocorrencia SET cd_quadro_vagas=?,"+
												      		   "cd_instituicao=?,"+
												      		   "cd_ocorrencia=? WHERE cd_quadro_vagas=? AND cd_instituicao=? AND cd_ocorrencia=?");
			pstmt.setInt(1,objeto.getCdQuadroVagas());
			pstmt.setInt(2,objeto.getCdInstituicao());
			pstmt.setInt(3,objeto.getCdOcorrencia());
			pstmt.setInt(4, cdQuadroVagasOld!=0 ? cdQuadroVagasOld : objeto.getCdQuadroVagas());
			pstmt.setInt(5, cdInstituicaoOld!=0 ? cdInstituicaoOld : objeto.getCdInstituicao());
			pstmt.setInt(6, cdOcorrenciaOld!=0 ? cdOcorrenciaOld : objeto.getCdOcorrencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasOcorrenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasOcorrenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdQuadroVagas, int cdInstituicao, int cdOcorrencia) {
		return delete(cdQuadroVagas, cdInstituicao, cdOcorrencia, null);
	}

	public static int delete(int cdQuadroVagas, int cdInstituicao, int cdOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_quadro_vagas_ocorrencia WHERE cd_quadro_vagas=? AND cd_instituicao=? AND cd_ocorrencia=?");
			pstmt.setInt(1, cdQuadroVagas);
			pstmt.setInt(2, cdInstituicao);
			pstmt.setInt(3, cdOcorrencia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasOcorrenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasOcorrenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static QuadroVagasOcorrencia get(int cdQuadroVagas, int cdInstituicao, int cdOcorrencia) {
		return get(cdQuadroVagas, cdInstituicao, cdOcorrencia, null);
	}

	public static QuadroVagasOcorrencia get(int cdQuadroVagas, int cdInstituicao, int cdOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_quadro_vagas_ocorrencia WHERE cd_quadro_vagas=? AND cd_instituicao=? AND cd_ocorrencia=?");
			pstmt.setInt(1, cdQuadroVagas);
			pstmt.setInt(2, cdInstituicao);
			pstmt.setInt(3, cdOcorrencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new QuadroVagasOcorrencia(rs.getInt("cd_quadro_vagas"),
						rs.getInt("cd_instituicao"),
						rs.getInt("cd_ocorrencia"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasOcorrenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasOcorrenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_quadro_vagas_ocorrencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasOcorrenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasOcorrenciaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<QuadroVagasOcorrencia> getList() {
		return getList(null);
	}

	public static ArrayList<QuadroVagasOcorrencia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<QuadroVagasOcorrencia> list = new ArrayList<QuadroVagasOcorrencia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				QuadroVagasOcorrencia obj = QuadroVagasOcorrenciaDAO.get(rsm.getInt("cd_quadro_vagas"), rsm.getInt("cd_instituicao"), rsm.getInt("cd_ocorrencia"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasOcorrenciaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_quadro_vagas_ocorrencia", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
