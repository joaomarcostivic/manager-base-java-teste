package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class QuadroVagasCursoDAO{

	public static int insert(QuadroVagasCurso objeto) {
		return insert(objeto, null);
	}

	public static int insert(QuadroVagasCurso objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_quadro_vagas_curso (cd_quadro_vagas,"+
			                                  "cd_instituicao,"+
			                                  "cd_curso,"+
			                                  "tp_turno,"+
			                                  "qt_turmas,"+
			                                  "qt_vagas) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdQuadroVagas()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdQuadroVagas());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdInstituicao());
			if(objeto.getCdCurso()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdCurso());
			pstmt.setInt(4, objeto.getTpTurno());
			pstmt.setInt(5,objeto.getQtTurmas());
			pstmt.setInt(6,objeto.getQtVagas());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasCursoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasCursoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(QuadroVagasCurso objeto) {
		return update(objeto, 0, 0, 0, 0, null);
	}

	public static int update(QuadroVagasCurso objeto, int cdQuadroVagasOld, int cdInstituicaoOld, int cdCursoOld, int tpTurnoOld) {
		return update(objeto, cdQuadroVagasOld, cdInstituicaoOld, cdCursoOld, tpTurnoOld, null);
	}

	public static int update(QuadroVagasCurso objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, connect);
	}

	public static int update(QuadroVagasCurso objeto, int cdQuadroVagasOld, int cdInstituicaoOld, int cdCursoOld, int tpTurnoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_quadro_vagas_curso SET cd_quadro_vagas=?,"+
												      		   "cd_instituicao=?,"+
												      		   "cd_curso=?,"+
												      		   "tp_turno=?,"+
												      		   "qt_turmas=?,"+
												      		   "qt_vagas=? WHERE cd_quadro_vagas=? AND cd_instituicao=? AND cd_curso=? AND tp_turno=?");
			pstmt.setInt(1,objeto.getCdQuadroVagas());
			pstmt.setInt(2,objeto.getCdInstituicao());
			pstmt.setInt(3,objeto.getCdCurso());
			pstmt.setInt(4,objeto.getTpTurno());
			pstmt.setInt(5,objeto.getQtTurmas());
			pstmt.setInt(6,objeto.getQtVagas());
			pstmt.setInt(7, cdQuadroVagasOld!=0 ? cdQuadroVagasOld : objeto.getCdQuadroVagas());
			pstmt.setInt(8, cdInstituicaoOld!=0 ? cdInstituicaoOld : objeto.getCdInstituicao());
			pstmt.setInt(9, cdCursoOld!=0 ? cdCursoOld : objeto.getCdCurso());
			pstmt.setInt(10, tpTurnoOld!=0 ? tpTurnoOld : objeto.getTpTurno());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasCursoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasCursoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdQuadroVagas, int cdInstituicao, int cdCurso, int tpTurno) {
		return delete(cdQuadroVagas, cdInstituicao, cdCurso, tpTurno, null);
	}

	public static int delete(int cdQuadroVagas, int cdInstituicao, int cdCurso, int tpTurno, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_quadro_vagas_curso WHERE cd_quadro_vagas=? AND cd_instituicao=? AND cd_curso=? AND tp_turno=?");
			pstmt.setInt(1, cdQuadroVagas);
			pstmt.setInt(2, cdInstituicao);
			pstmt.setInt(3, cdCurso);
			pstmt.setInt(4, tpTurno);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasCursoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasCursoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static QuadroVagasCurso get(int cdQuadroVagas, int cdInstituicao, int cdCurso, int tpTurno) {
		return get(cdQuadroVagas, cdInstituicao, cdCurso, tpTurno, null);
	}

	public static QuadroVagasCurso get(int cdQuadroVagas, int cdInstituicao, int cdCurso, int tpTurno, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_quadro_vagas_curso WHERE cd_quadro_vagas=? AND cd_instituicao=? AND cd_curso=? AND tp_turno=?");
			pstmt.setInt(1, cdQuadroVagas);
			pstmt.setInt(2, cdInstituicao);
			pstmt.setInt(3, cdCurso);
			pstmt.setInt(4, tpTurno);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new QuadroVagasCurso(rs.getInt("cd_quadro_vagas"),
						rs.getInt("cd_instituicao"),
						rs.getInt("cd_curso"),
						rs.getInt("tp_turno"),
						rs.getInt("qt_turmas"),
						rs.getInt("qt_vagas"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasCursoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasCursoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_quadro_vagas_curso");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasCursoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasCursoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<QuadroVagasCurso> getList() {
		return getList(null);
	}

	public static ArrayList<QuadroVagasCurso> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<QuadroVagasCurso> list = new ArrayList<QuadroVagasCurso>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				QuadroVagasCurso obj = QuadroVagasCursoDAO.get(rsm.getInt("cd_quadro_vagas"), rsm.getInt("cd_instituicao"), rsm.getInt("cd_curso"), rsm.getInt("tp_turno"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasCursoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_quadro_vagas_curso", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
