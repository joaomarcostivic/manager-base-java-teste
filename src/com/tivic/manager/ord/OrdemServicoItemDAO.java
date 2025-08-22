package com.tivic.manager.ord;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class OrdemServicoItemDAO{

	public static int insert(OrdemServicoItem objeto) {
		return insert(objeto, null);
	}

	public static int insert(OrdemServicoItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_ordem_servico_item");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_ordem_servico");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdOrdemServico()));
			int code = Conexao.getSequenceCode("ord_ordem_servico_item", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdOrdemServicoItem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ord_ordem_servico_item (cd_ordem_servico_item,"+
			                                  "cd_ordem_servico,"+
			                                  "cd_contrato,"+
			                                  "cd_produto_servico,"+
			                                  "cd_marca,"+
			                                  "cd_garantia,"+
			                                  "dt_admissao,"+
			                                  "dt_inicio,"+
			                                  "dt_fechamento,"+
			                                  "dt_entrega,"+
			                                  "txt_diagnostico,"+
			                                  "txt_observacao,"+
			                                  "nr_controle,"+
			                                  "st_ordem_servico_item,"+
			                                  "nm_item,"+
			                                  "nm_marca,"+
			                                  "nm_modelo,"+
			                                  "nr_serie,"+
			                                  "cd_tipo_produto_servico,"+
			                                  "cd_tecnico_responsavel,"+
			                                  "dt_previsao_entrega,"+
			                                  "cd_unidade_medida,"+
			                                  "cd_referencia) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdOrdemServico()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdOrdemServico());
			if(objeto.getCdContrato()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdContrato());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdProdutoServico());
			if(objeto.getCdMarca()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdMarca());
			if(objeto.getCdGarantia()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdGarantia());
			if(objeto.getDtAdmissao()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtAdmissao().getTimeInMillis()));
			if(objeto.getDtInicio()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtInicio().getTimeInMillis()));
			if(objeto.getDtFechamento()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtFechamento().getTimeInMillis()));
			if(objeto.getDtEntrega()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtEntrega().getTimeInMillis()));
			pstmt.setString(11,objeto.getTxtDiagnostico());
			pstmt.setString(12,objeto.getTxtObservacao());
			pstmt.setString(13,objeto.getNrControle());
			pstmt.setInt(14,objeto.getStOrdemServicoItem());
			pstmt.setString(15,objeto.getNmItem());
			pstmt.setString(16,objeto.getNmMarca());
			pstmt.setString(17,objeto.getNmModelo());
			pstmt.setString(18,objeto.getNrSerie());
			if(objeto.getCdTipoProdutoServico()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdTipoProdutoServico());
			if(objeto.getCdTecnicoResponsavel()==0)
				pstmt.setNull(20, Types.INTEGER);
			else
				pstmt.setInt(20,objeto.getCdTecnicoResponsavel());
			if(objeto.getDtPrevisaoEntrega()==null)
				pstmt.setNull(21, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(21,new Timestamp(objeto.getDtPrevisaoEntrega().getTimeInMillis()));
			if(objeto.getCdUnidadeMedida()==0)
				pstmt.setNull(22, Types.INTEGER);
			else
				pstmt.setInt(22,objeto.getCdUnidadeMedida());
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(23, Types.INTEGER);
			else
				pstmt.setInt(23,objeto.getCdReferencia());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoItemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoItemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(OrdemServicoItem objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(OrdemServicoItem objeto, int cdOrdemServicoItemOld, int cdOrdemServicoOld) {
		return update(objeto, cdOrdemServicoItemOld, cdOrdemServicoOld, null);
	}

	public static int update(OrdemServicoItem objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(OrdemServicoItem objeto, int cdOrdemServicoItemOld, int cdOrdemServicoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ord_ordem_servico_item SET cd_ordem_servico_item=?,"+
												      		   "cd_ordem_servico=?,"+
												      		   "cd_contrato=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_marca=?,"+
												      		   "cd_garantia=?,"+
												      		   "dt_admissao=?,"+
												      		   "dt_inicio=?,"+
												      		   "dt_fechamento=?,"+
												      		   "dt_entrega=?,"+
												      		   "txt_diagnostico=?,"+
												      		   "txt_observacao=?,"+
												      		   "nr_controle=?,"+
												      		   "st_ordem_servico_item=?,"+
												      		   "nm_item=?,"+
												      		   "nm_marca=?,"+
												      		   "nm_modelo=?,"+
												      		   "nr_serie=?,"+
												      		   "cd_tipo_produto_servico=?,"+
												      		   "cd_tecnico_responsavel=?,"+
												      		   "dt_previsao_entrega=?,"+
												      		   "cd_unidade_medida=?,"+
												      		   "cd_referencia=? WHERE cd_ordem_servico_item=? AND cd_ordem_servico=?");
			pstmt.setInt(1,objeto.getCdOrdemServicoItem());
			pstmt.setInt(2,objeto.getCdOrdemServico());
			if(objeto.getCdContrato()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdContrato());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdProdutoServico());
			if(objeto.getCdMarca()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdMarca());
			if(objeto.getCdGarantia()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdGarantia());
			if(objeto.getDtAdmissao()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtAdmissao().getTimeInMillis()));
			if(objeto.getDtInicio()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtInicio().getTimeInMillis()));
			if(objeto.getDtFechamento()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtFechamento().getTimeInMillis()));
			if(objeto.getDtEntrega()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtEntrega().getTimeInMillis()));
			pstmt.setString(11,objeto.getTxtDiagnostico());
			pstmt.setString(12,objeto.getTxtObservacao());
			pstmt.setString(13,objeto.getNrControle());
			pstmt.setInt(14,objeto.getStOrdemServicoItem());
			pstmt.setString(15,objeto.getNmItem());
			pstmt.setString(16,objeto.getNmMarca());
			pstmt.setString(17,objeto.getNmModelo());
			pstmt.setString(18,objeto.getNrSerie());
			if(objeto.getCdTipoProdutoServico()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdTipoProdutoServico());
			if(objeto.getCdTecnicoResponsavel()==0)
				pstmt.setNull(20, Types.INTEGER);
			else
				pstmt.setInt(20,objeto.getCdTecnicoResponsavel());
			if(objeto.getDtPrevisaoEntrega()==null)
				pstmt.setNull(21, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(21,new Timestamp(objeto.getDtPrevisaoEntrega().getTimeInMillis()));
			if(objeto.getCdUnidadeMedida()==0)
				pstmt.setNull(22, Types.INTEGER);
			else
				pstmt.setInt(22,objeto.getCdUnidadeMedida());
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(23, Types.INTEGER);
			else
				pstmt.setInt(23,objeto.getCdReferencia());
			pstmt.setInt(24, cdOrdemServicoItemOld!=0 ? cdOrdemServicoItemOld : objeto.getCdOrdemServicoItem());
			pstmt.setInt(25, cdOrdemServicoOld!=0 ? cdOrdemServicoOld : objeto.getCdOrdemServico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoItemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoItemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOrdemServicoItem, int cdOrdemServico) {
		return delete(cdOrdemServicoItem, cdOrdemServico, null);
	}

	public static int delete(int cdOrdemServicoItem, int cdOrdemServico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ord_ordem_servico_item WHERE cd_ordem_servico_item=? AND cd_ordem_servico=?");
			pstmt.setInt(1, cdOrdemServicoItem);
			pstmt.setInt(2, cdOrdemServico);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoItemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static OrdemServicoItem get(int cdOrdemServicoItem, int cdOrdemServico) {
		return get(cdOrdemServicoItem, cdOrdemServico, null);
	}

	public static OrdemServicoItem get(int cdOrdemServicoItem, int cdOrdemServico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ord_ordem_servico_item WHERE cd_ordem_servico_item=? AND cd_ordem_servico=?");
			pstmt.setInt(1, cdOrdemServicoItem);
			pstmt.setInt(2, cdOrdemServico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new OrdemServicoItem(rs.getInt("cd_ordem_servico_item"),
						rs.getInt("cd_ordem_servico"),
						rs.getInt("cd_contrato"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_marca"),
						rs.getInt("cd_garantia"),
						(rs.getTimestamp("dt_admissao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_admissao").getTime()),
						(rs.getTimestamp("dt_inicio")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio").getTime()),
						(rs.getTimestamp("dt_fechamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_fechamento").getTime()),
						(rs.getTimestamp("dt_entrega")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_entrega").getTime()),
						rs.getString("txt_diagnostico"),
						rs.getString("txt_observacao"),
						rs.getString("nr_controle"),
						rs.getInt("st_ordem_servico_item"),
						rs.getString("nm_item"),
						rs.getString("nm_marca"),
						rs.getString("nm_modelo"),
						rs.getString("nr_serie"),
						rs.getInt("cd_tipo_produto_servico"),
						rs.getInt("cd_tecnico_responsavel"),
						(rs.getTimestamp("dt_previsao_entrega")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_previsao_entrega").getTime()),
						rs.getInt("cd_unidade_medida"),
						rs.getInt("cd_referencia"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoItemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoItemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ord_ordem_servico_item");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoItemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoItemDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<OrdemServicoItem> getList() {
		return getList(null);
	}

	public static ArrayList<OrdemServicoItem> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<OrdemServicoItem> list = new ArrayList<OrdemServicoItem>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				OrdemServicoItem obj = OrdemServicoItemDAO.get(rsm.getInt("cd_ordem_servico_item"), rsm.getInt("cd_ordem_servico"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoItemDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ord_ordem_servico_item", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
