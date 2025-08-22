package com.tivic.manager.crt;

import java.sql.*;
import sol.util.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.manager.conexao.*;
import com.tivic.sol.connection.Conexao;

public class ContratoDAO{

	public static int insert(Contrato objeto) {
		return insert(objeto, null);
	}

	public static int insert(Contrato objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			int code = Conexao.getSequenceCode("SCE_CONTRATO");
			pstmt = connect.prepareStatement("INSERT INTO SCE_CONTRATO (CD_CONTRATO_EMPRESTIMO,"+
			                                  "CD_CONTRATANTE,"+
			                                  "CD_AGENTE,"+
			                                  "CD_EMPRESA,"+
			                                  "CD_PRODUTO,"+
			                                  "CD_PLANO,"+
			                                  "ST_CONTRATO,"+
			                                  "QT_PARCELAS,"+
			                                  "VL_PARCELAS,"+
			                                  "DT_PAGAMENTO,"+
			                                  "VL_FINANCIADO,"+
			                                  "VL_TAC,"+
			                                  "VL_LIBERADO,"+
			                                  "CD_VINCULO,"+
			                                  "CD_SITUACAO,"+
			                                  "PR_JUROS,"+
			                                  "DT_CONTRATO,"+
			                                  "CD_OPERACAO,"+
			                                  "CD_MOTIVO,"+
			                                  "CD_USUARIO,"+
			                                  "DT_OPERACAO,"+
			                                  "NR_CONTRATO,"+
			                                  "CD_SUBAGENTE,"+
			                                  "LG_EXPONTANEO,"+
			                                  "CD_ORGAO,"+
			                                  "NR_INSCRICAO,"+
			                                  "CD_TABELA_COMISSAO,"+
			                                  "DT_CADASTRO,"+
			                                  "CD_ATENDENTE,"+
			                                  "DS_OBSERVACAO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdContratante()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdContratante());
			if(objeto.getCdAgente()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAgente());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdEmpresa());
			if(objeto.getCdProduto()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdProduto());
			if(objeto.getCdPlano()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdPlano());
			pstmt.setInt(7,objeto.getStContrato());
			pstmt.setInt(8,objeto.getQtParcelas());
			pstmt.setFloat(9,objeto.getVlParcelas());
			if(objeto.getDtPagamento()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtPagamento().getTimeInMillis()));
			pstmt.setFloat(11,objeto.getVlFinanciado());
			pstmt.setFloat(12,objeto.getVlTac());
			pstmt.setFloat(13,objeto.getVlLiberado());
			if(objeto.getCdVinculo()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdVinculo());
			if(objeto.getCdSituacao()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdSituacao());
			pstmt.setFloat(16,objeto.getPrJuros());
			if(objeto.getDtContrato()==null)
				pstmt.setNull(17, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(17,new Timestamp(objeto.getDtContrato().getTimeInMillis()));
			if(objeto.getCdOperacao()<=0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdOperacao());
			if(objeto.getCdMotivo()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdMotivo());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(20, Types.INTEGER);
			else
				pstmt.setInt(20,objeto.getCdUsuario());
			if(objeto.getDtOperacao()==null)
				pstmt.setNull(21, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(21,new Timestamp(objeto.getDtOperacao().getTimeInMillis()));
			pstmt.setString(22,objeto.getNrContrato());
			if(objeto.getCdSubagente()==0)
				pstmt.setNull(23, Types.INTEGER);
			else
				pstmt.setInt(23,objeto.getCdSubagente());
			pstmt.setInt(24,objeto.getLgExpontaneo());
			if(objeto.getCdOrgao()==0)
				pstmt.setNull(25, Types.INTEGER);
			else
				pstmt.setInt(25,objeto.getCdOrgao());
			pstmt.setString(26,objeto.getNrInscricao());
			if(objeto.getCdTabelaComissao()==0)
				pstmt.setNull(27, Types.INTEGER);
			else
				pstmt.setInt(27,objeto.getCdTabelaComissao());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(28, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(28,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			pstmt.setInt(29,objeto.getCdAtendente());
			pstmt.setString(30,objeto.getDsObservacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Contrato objeto) {
		return update(objeto, null);
	}

	public static int update(Contrato objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE SCE_CONTRATO SET CD_CONTRATANTE=?,"+
			                                  "CD_AGENTE=?,"+
			                                  "CD_EMPRESA=?,"+
			                                  "CD_PRODUTO=?,"+
			                                  "CD_PLANO=?,"+
			                                  "ST_CONTRATO=?,"+
			                                  "QT_PARCELAS=?,"+
			                                  "VL_PARCELAS=?,"+
			                                  "DT_PAGAMENTO=?,"+
			                                  "VL_FINANCIADO=?,"+
			                                  "VL_TAC=?,"+
			                                  "VL_LIBERADO=?,"+
			                                  "CD_VINCULO=?,"+
			                                  "CD_SITUACAO=?,"+
			                                  "PR_JUROS=?,"+
			                                  "DT_CONTRATO=?,"+
			                                  "CD_OPERACAO=?,"+
			                                  "CD_MOTIVO=?,"+
			                                  "CD_USUARIO=?,"+
			                                  "DT_OPERACAO=?,"+
			                                  "NR_CONTRATO=?,"+
			                                  "CD_SUBAGENTE=?,"+
			                                  "LG_EXPONTANEO=?,"+
			                                  "CD_ORGAO=?,"+
			                                  "NR_INSCRICAO=?,"+
			                                  "CD_TABELA_COMISSAO=?,"+
			                                  "DT_CADASTRO=?,"+
			                                  "CD_ATENDENTE=?, "+
			                                  "DS_OBSERVACAO=? WHERE CD_CONTRATO_EMPRESTIMO=?");
			if(objeto.getCdContratante()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdContratante());
			if(objeto.getCdAgente()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAgente());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			if(objeto.getCdProduto()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdProduto());
			if(objeto.getCdPlano()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdPlano());
			pstmt.setInt(6,objeto.getStContrato());
			pstmt.setInt(7,objeto.getQtParcelas());
			pstmt.setFloat(8,objeto.getVlParcelas());
			if(objeto.getDtPagamento()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtPagamento().getTimeInMillis()));
			pstmt.setFloat(10,objeto.getVlFinanciado());
			pstmt.setFloat(11,objeto.getVlTac());
			pstmt.setFloat(12,objeto.getVlLiberado());
			if(objeto.getCdVinculo()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdVinculo());
			if(objeto.getCdSituacao()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdSituacao());
			pstmt.setFloat(15,objeto.getPrJuros());
			if(objeto.getDtContrato()==null)
				pstmt.setNull(16, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(16,new Timestamp(objeto.getDtContrato().getTimeInMillis()));
			if(objeto.getCdOperacao()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdOperacao());
			if(objeto.getCdMotivo()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdMotivo());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdUsuario());
			if(objeto.getDtOperacao()==null)
				pstmt.setNull(20, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(20,new Timestamp(objeto.getDtOperacao().getTimeInMillis()));
			pstmt.setString(21,objeto.getNrContrato());
			if(objeto.getCdSubagente()==0)
				pstmt.setNull(22, Types.INTEGER);
			else
				pstmt.setInt(22,objeto.getCdSubagente());
			pstmt.setInt(23,objeto.getLgExpontaneo());
			if(objeto.getCdOrgao()==0)	{
				pstmt.setNull(24, Types.INTEGER);
				pstmt.setNull(25, Types.VARCHAR);
			}
			else	{
				pstmt.setInt(24,objeto.getCdOrgao());
				pstmt.setString(25,objeto.getNrInscricao());
			}
			if(objeto.getCdTabelaComissao()==0)
				pstmt.setNull(26, Types.INTEGER);
			else
				pstmt.setInt(26,objeto.getCdTabelaComissao());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(27, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(27,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			pstmt.setInt(28,objeto.getCdAtendente());
			pstmt.setString(29,objeto.getDsObservacao());
			pstmt.setInt(30,objeto.getCdContratoEmprestimo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContratoEmprestimo) {
		return delete(cdContratoEmprestimo, null);
	}

	public static int delete(int cdContratoEmprestimo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM SCE_CONTRATO WHERE CD_CONTRATO_EMPRESTIMO=?");
			pstmt.setInt(1, cdContratoEmprestimo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Contrato get(int cdContratoEmprestimo) {
		return get(cdContratoEmprestimo, null);
	}

	public static Contrato get(int cdContratoEmprestimo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSetMap rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM SCE_CONTRATO WHERE CD_CONTRATO_EMPRESTIMO=?");
			pstmt.setInt(1, cdContratoEmprestimo);
			rs = new ResultSetMap(pstmt.executeQuery());
			if(rs.next()){
				return new Contrato(rs.getInt("CD_CONTRATO_EMPRESTIMO"),
						rs.getInt("CD_CONTRATANTE"),
						rs.getInt("CD_AGENTE"),
						rs.getInt("CD_EMPRESA"),
						rs.getInt("CD_PRODUTO"),
						rs.getInt("CD_PLANO"),
						rs.getInt("ST_CONTRATO"),
						rs.getInt("QT_PARCELAS"),
						rs.getFloat("VL_PARCELAS"),
						(rs.getTimestamp("DT_PAGAMENTO")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_PAGAMENTO").getTime()),
						rs.getFloat("VL_FINANCIADO"),
						rs.getFloat("VL_TAC"),
						rs.getFloat("VL_LIBERADO"),
						rs.getInt("CD_VINCULO"),
						rs.getInt("CD_SITUACAO"),
						rs.getFloat("PR_JUROS"),
						(rs.getTimestamp("DT_CONTRATO")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_CONTRATO").getTime()),
						rs.getInt("CD_OPERACAO"),
						rs.getInt("CD_MOTIVO"),
						rs.getInt("CD_USUARIO"),
						(rs.getTimestamp("DT_OPERACAO")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_OPERACAO").getTime()),
						rs.getString("NR_CONTRATO"),
						rs.getInt("CD_SUBAGENTE"),
						rs.getInt("LG_EXPONTANEO"),
						rs.getInt("CD_ORGAO"),
						rs.getString("NR_INSCRICAO"),
						rs.getInt("CD_TABELA_COMISSAO"),
						(rs.getTimestamp("DT_CADASTRO")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_CADASTRO").getTime()),
						rs.getInt("CD_ATENDENTE"),
						rs.getString("DS_OBSERVACAO"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM SCE_CONTRATO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT * FROM SCE_CONTRATO", " ORDER BY dt_contrato", criterios, Conexao.conectar(), true, false);
	}

}