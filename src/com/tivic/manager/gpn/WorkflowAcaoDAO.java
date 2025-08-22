package com.tivic.manager.gpn;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class WorkflowAcaoDAO{

	public static int insert(WorkflowAcao objeto) {
		return insert(objeto, null);
	}

	public static int insert(WorkflowAcao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_acao");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_regra");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdRegra()));
			int code = Conexao.getSequenceCode("gpn_workflow_acao", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAcao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO gpn_workflow_acao (cd_acao,"+
			                                  "cd_regra,"+
			                                  "tp_acao,"+
			                                  "lg_sugestao,"+
			                                  "nr_dias,"+
			                                  "vl_servico,"+
			                                  "cd_tipo_andamento,"+
			                                  "cd_tipo_prazo,"+
			                                  "cd_tipo_situacao,"+
			                                  "cd_produto_servico,"+
			                                  "cd_modelo,"+
			                                  "cd_pessoa,"+
			                                  "cd_grupo_trabalho,"+
			                                  "tp_responsavel_agenda,"+
			                                  "tp_contagem_prazo,"+
			                                  "cd_tipo_documento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdRegra()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdRegra());
			pstmt.setInt(3,objeto.getTpAcao());
			pstmt.setInt(4,objeto.getLgSugestao());
			pstmt.setInt(5,objeto.getNrDias());
			pstmt.setDouble(6,objeto.getVlServico());
			if(objeto.getCdTipoAndamento()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdTipoAndamento());
			if(objeto.getCdTipoPrazo()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdTipoPrazo());
			if(objeto.getCdTipoSituacao()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdTipoSituacao());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdProdutoServico());
			if(objeto.getCdModelo()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdModelo());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdPessoa());
			if(objeto.getCdGrupoTrabalho()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdGrupoTrabalho());
			pstmt.setInt(14,objeto.getTpResponsavelAgenda());
			pstmt.setInt(15,objeto.getTpContagemPrazo());
			if(objeto.getCdTipoDocumento()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdTipoDocumento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAcaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAcaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(WorkflowAcao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(WorkflowAcao objeto, int cdAcaoOld, int cdRegraOld) {
		return update(objeto, cdAcaoOld, cdRegraOld, null);
	}

	public static int update(WorkflowAcao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(WorkflowAcao objeto, int cdAcaoOld, int cdRegraOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE gpn_workflow_acao SET cd_acao=?,"+
												      		   "cd_regra=?,"+
												      		   "tp_acao=?,"+
												      		   "lg_sugestao=?,"+
												      		   "nr_dias=?,"+
												      		   "vl_servico=?,"+
												      		   "cd_tipo_andamento=?,"+
												      		   "cd_tipo_prazo=?,"+
												      		   "cd_tipo_situacao=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_modelo=?,"+
												      		   "cd_pessoa=?,"+
												      		   "cd_grupo_trabalho=?,"+
												      		   "tp_responsavel_agenda=?,"+
												      		   "tp_contagem_prazo=?,"+
												      		   "cd_tipo_documento=? WHERE cd_acao=? AND cd_regra=?");
			pstmt.setInt(1,objeto.getCdAcao());
			pstmt.setInt(2,objeto.getCdRegra());
			pstmt.setInt(3,objeto.getTpAcao());
			pstmt.setInt(4,objeto.getLgSugestao());
			pstmt.setInt(5,objeto.getNrDias());
			pstmt.setDouble(6,objeto.getVlServico());
			if(objeto.getCdTipoAndamento()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdTipoAndamento());
			if(objeto.getCdTipoPrazo()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdTipoPrazo());
			if(objeto.getCdTipoSituacao()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdTipoSituacao());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdProdutoServico());
			if(objeto.getCdModelo()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdModelo());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdPessoa());
			if(objeto.getCdGrupoTrabalho()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdGrupoTrabalho());
			pstmt.setInt(14,objeto.getTpResponsavelAgenda());
			pstmt.setInt(15,objeto.getTpContagemPrazo());
			if(objeto.getCdTipoDocumento()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdTipoDocumento());
			pstmt.setInt(17, cdAcaoOld!=0 ? cdAcaoOld : objeto.getCdAcao());
			pstmt.setInt(18, cdRegraOld!=0 ? cdRegraOld : objeto.getCdRegra());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAcaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAcaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAcao, int cdRegra) {
		return delete(cdAcao, cdRegra, null);
	}

	public static int delete(int cdAcao, int cdRegra, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM gpn_workflow_acao WHERE cd_acao=? AND cd_regra=?");
			pstmt.setInt(1, cdAcao);
			pstmt.setInt(2, cdRegra);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAcaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAcaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static WorkflowAcao get(int cdAcao, int cdRegra) {
		return get(cdAcao, cdRegra, null);
	}

	public static WorkflowAcao get(int cdAcao, int cdRegra, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM gpn_workflow_acao WHERE cd_acao=? AND cd_regra=?");
			pstmt.setInt(1, cdAcao);
			pstmt.setInt(2, cdRegra);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new WorkflowAcao(rs.getInt("cd_acao"),
						rs.getInt("cd_regra"),
						rs.getInt("tp_acao"),
						rs.getInt("lg_sugestao"),
						rs.getInt("nr_dias"),
						rs.getDouble("vl_servico"),
						rs.getInt("cd_tipo_andamento"),
						rs.getInt("cd_tipo_prazo"),
						rs.getInt("cd_tipo_situacao"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_modelo"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_grupo_trabalho"),
						rs.getInt("tp_responsavel_agenda"),
						rs.getInt("tp_contagem_prazo"),
						rs.getInt("cd_tipo_documento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAcaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAcaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM gpn_workflow_acao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAcaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAcaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<WorkflowAcao> getList() {
		return getList(null);
	}

	public static ArrayList<WorkflowAcao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<WorkflowAcao> list = new ArrayList<WorkflowAcao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				WorkflowAcao obj = WorkflowAcaoDAO.get(rsm.getInt("cd_acao"), rsm.getInt("cd_regra"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAcaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM gpn_workflow_acao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}