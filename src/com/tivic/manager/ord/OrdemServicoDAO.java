package com.tivic.manager.ord;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class OrdemServicoDAO{

	public static int insert(OrdemServico objeto) {
		return insert(objeto, null);
	}

	public static int insert(OrdemServico objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ord_ordem_servico", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdOrdemServico(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ord_ordem_servico (cd_ordem_servico,"+
			                                  "cd_pessoa,"+
			                                  "cd_tipo_atendimento,"+
			                                  "dt_entrada,"+
			                                  "dt_saida,"+
			                                  "txt_solicitacao,"+
			                                  "txt_observacao,"+
			                                  "cd_situacao_servico,"+
			                                  "cd_modalidade,"+
			                                  "nr_ordem_servico,"+
			                                  "cd_empresa,"+
			                                  "cd_tecnico_responsavel,"+
			                                  "cd_documento,"+
			                                  "cd_setor,"+
			                                  "cd_usuario,"+
			                                  "vl_ordem_servico,"+
			                                  "cd_plano_trabalho,"+
			                                  "nr_nota_fiscal,"+
			                                  "cd_fornecedor) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			if(objeto.getCdTipoAtendimento()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoAtendimento());
			if(objeto.getDtEntrada()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtEntrada().getTimeInMillis()));
			if(objeto.getDtSaida()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtSaida().getTimeInMillis()));
			pstmt.setString(6,objeto.getTxtSolicitacao());
			pstmt.setString(7,objeto.getTxtObservacao());
			if(objeto.getCdSituacaoServico()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdSituacaoServico());
			if(objeto.getCdModalidade()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdModalidade());
			pstmt.setString(10,objeto.getNrOrdemServico());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdEmpresa());
			if(objeto.getCdTecnicoResponsavel()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdTecnicoResponsavel());
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdDocumento());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdSetor());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdUsuario());
			pstmt.setDouble(16,objeto.getVlOrdemServico());
			if(objeto.getCdPlanoTrabalho()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdPlanoTrabalho());
			pstmt.setString(18,objeto.getNrNotaFiscal());
			if(objeto.getCdFornecedor()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdFornecedor());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(OrdemServico objeto) {
		return update(objeto, 0, null);
	}

	public static int update(OrdemServico objeto, int cdOrdemServicoOld) {
		return update(objeto, cdOrdemServicoOld, null);
	}

	public static int update(OrdemServico objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(OrdemServico objeto, int cdOrdemServicoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ord_ordem_servico SET cd_ordem_servico=?,"+
												      		   "cd_pessoa=?,"+
												      		   "cd_tipo_atendimento=?,"+
												      		   "dt_entrada=?,"+
												      		   "dt_saida=?,"+
												      		   "txt_solicitacao=?,"+
												      		   "txt_observacao=?,"+
												      		   "cd_situacao_servico=?,"+
												      		   "cd_modalidade=?,"+
												      		   "nr_ordem_servico=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_tecnico_responsavel=?,"+
												      		   "cd_documento=?,"+
												      		   "cd_setor=?,"+
												      		   "cd_usuario=?,"+
												      		   "vl_ordem_servico=?,"+
												      		   "cd_plano_trabalho=?,"+
												      		   "nr_nota_fiscal=?,"+
												      		   "cd_fornecedor=? WHERE cd_ordem_servico=?");
			pstmt.setInt(1,objeto.getCdOrdemServico());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			if(objeto.getCdTipoAtendimento()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoAtendimento());
			if(objeto.getDtEntrada()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtEntrada().getTimeInMillis()));
			if(objeto.getDtSaida()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtSaida().getTimeInMillis()));
			pstmt.setString(6,objeto.getTxtSolicitacao());
			pstmt.setString(7,objeto.getTxtObservacao());
			if(objeto.getCdSituacaoServico()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdSituacaoServico());
			if(objeto.getCdModalidade()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdModalidade());
			pstmt.setString(10,objeto.getNrOrdemServico());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdEmpresa());
			if(objeto.getCdTecnicoResponsavel()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdTecnicoResponsavel());
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdDocumento());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdSetor());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdUsuario());
			pstmt.setDouble(16,objeto.getVlOrdemServico());
			if(objeto.getCdPlanoTrabalho()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdPlanoTrabalho());
			pstmt.setString(18,objeto.getNrNotaFiscal());
			if(objeto.getCdFornecedor()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdFornecedor());
			pstmt.setInt(20, cdOrdemServicoOld!=0 ? cdOrdemServicoOld : objeto.getCdOrdemServico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOrdemServico) {
		return delete(cdOrdemServico, null);
	}

	public static int delete(int cdOrdemServico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ord_ordem_servico WHERE cd_ordem_servico=?");
			pstmt.setInt(1, cdOrdemServico);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static OrdemServico get(int cdOrdemServico) {
		return get(cdOrdemServico, null);
	}

	public static OrdemServico get(int cdOrdemServico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ord_ordem_servico WHERE cd_ordem_servico=?");
			pstmt.setInt(1, cdOrdemServico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new OrdemServico(rs.getInt("cd_ordem_servico"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_tipo_atendimento"),
						(rs.getTimestamp("dt_entrada")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_entrada").getTime()),
						(rs.getTimestamp("dt_saida")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_saida").getTime()),
						rs.getString("txt_solicitacao"),
						rs.getString("txt_observacao"),
						rs.getInt("cd_situacao_servico"),
						rs.getInt("cd_modalidade"),
						rs.getString("nr_ordem_servico"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_tecnico_responsavel"),
						rs.getInt("cd_documento"),
						rs.getInt("cd_setor"),
						rs.getInt("cd_usuario"),
						rs.getDouble("vl_ordem_servico"),
						rs.getInt("cd_plano_trabalho"),
						rs.getString("nr_nota_fiscal"),
						rs.getInt("cd_fornecedor"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ord_ordem_servico");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<OrdemServico> getList() {
		return getList(null);
	}

	public static ArrayList<OrdemServico> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<OrdemServico> list = new ArrayList<OrdemServico>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				OrdemServico obj = OrdemServicoDAO.get(rsm.getInt("cd_ordem_servico"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ord_ordem_servico", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}