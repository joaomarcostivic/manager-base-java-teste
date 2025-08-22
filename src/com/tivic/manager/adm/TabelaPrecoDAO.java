package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class TabelaPrecoDAO{

	public static int insert(TabelaPreco objeto) {
		return insert(objeto, null);
	}

	public static int insert(TabelaPreco objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_tabela_preco", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTabelaPreco(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_tabela_preco (cd_tabela_preco,"+
			                                  "cd_empresa,"+
			                                  "cd_moeda,"+
			                                  "nr_versao,"+
			                                  "nm_tabela_preco,"+
			                                  "dt_inicio_validade,"+
			                                  "dt_final_validade,"+
			                                  "lg_ativo,"+
			                                  "id_tabela_preco,"+
			                                  "img_tabela_preco,"+
			                                  "tp_aplicacao_regras,"+
			                                  "gn_tabela_preco,"+
			                                  "st_tabela_preco,"+
			                                  "lg_padrao,"+
			                                  "lg_preco_venda,"+
			                                  "lg_imposto_incluso,"+
			                                  "nr_precisao_preco," + 
			                                  "txt_descricao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			if(objeto.getCdMoeda()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdMoeda());
			pstmt.setInt(4,objeto.getNrVersao());
			pstmt.setString(5,objeto.getNmTabelaPreco());
			if(objeto.getDtInicioValidade()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtInicioValidade().getTimeInMillis()));
			if(objeto.getDtFinalValidade()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtFinalValidade().getTimeInMillis()));
			pstmt.setInt(8,objeto.getLgAtivo());
			pstmt.setString(9,objeto.getIdTabelaPreco());
			if(objeto.getImgTabelaPreco()==null)
				pstmt.setNull(10, Types.BINARY);
			else
				pstmt.setBytes(10,objeto.getImgTabelaPreco());
			pstmt.setInt(11,objeto.getTpAplicacaoRegras());
			pstmt.setInt(12,objeto.getGnTabelaPreco());
			pstmt.setInt(13,objeto.getStTabelaPreco());
			pstmt.setInt(14,objeto.getLgPadrao());
			pstmt.setInt(15,objeto.getLgPrecoVenda());
			pstmt.setInt(16,objeto.getLgImpostoIncluso());
			pstmt.setInt(17,objeto.getNrPrecisaoPreco());
			pstmt.setString(18,objeto.getTxtDescricao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaPrecoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaPrecoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TabelaPreco objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TabelaPreco objeto, int cdTabelaPrecoOld) {
		return update(objeto, cdTabelaPrecoOld, null);
	}

	public static int update(TabelaPreco objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TabelaPreco objeto, int cdTabelaPrecoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_tabela_preco SET cd_tabela_preco=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_moeda=?,"+
												      		   "nr_versao=?,"+
												      		   "nm_tabela_preco=?,"+
												      		   "dt_inicio_validade=?,"+
												      		   "dt_final_validade=?,"+
												      		   "lg_ativo=?,"+
												      		   "id_tabela_preco=?,"+
												      		   "img_tabela_preco=?,"+
												      		   "tp_aplicacao_regras=?,"+
												      		   "gn_tabela_preco=?,"+
												      		   "st_tabela_preco=?,"+
												      		   "lg_padrao=?,"+
												      		   "lg_preco_venda=?,"+
												      		   "lg_imposto_incluso=?,"+
												      		   "nr_precisao_preco=?, " + 
												      		   "txt_descricao=? WHERE cd_tabela_preco=?");
			pstmt.setInt(1,objeto.getCdTabelaPreco());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			if(objeto.getCdMoeda()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdMoeda());
			pstmt.setInt(4,objeto.getNrVersao());
			pstmt.setString(5,objeto.getNmTabelaPreco());
			if(objeto.getDtInicioValidade()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtInicioValidade().getTimeInMillis()));
			if(objeto.getDtFinalValidade()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtFinalValidade().getTimeInMillis()));
			pstmt.setInt(8,objeto.getLgAtivo());
			pstmt.setString(9,objeto.getIdTabelaPreco());
			if(objeto.getImgTabelaPreco()==null)
				pstmt.setNull(10, Types.BINARY);
			else
				pstmt.setBytes(10,objeto.getImgTabelaPreco());
			pstmt.setInt(11,objeto.getTpAplicacaoRegras());
			pstmt.setInt(12,objeto.getGnTabelaPreco());
			pstmt.setInt(13,objeto.getStTabelaPreco());
			pstmt.setInt(14,objeto.getLgPadrao());
			pstmt.setInt(15,objeto.getLgPrecoVenda());
			pstmt.setInt(16,objeto.getLgImpostoIncluso());
			pstmt.setInt(17,objeto.getNrPrecisaoPreco());
			pstmt.setString(18,  objeto.getTxtDescricao());
			pstmt.setInt(19, cdTabelaPrecoOld!=0 ? cdTabelaPrecoOld : objeto.getCdTabelaPreco());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaPrecoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaPrecoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTabelaPreco) {
		return delete(cdTabelaPreco, null);
	}

	public static int delete(int cdTabelaPreco, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_tabela_preco WHERE cd_tabela_preco=?");
			pstmt.setInt(1, cdTabelaPreco);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaPrecoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaPrecoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TabelaPreco get(int cdTabelaPreco) {
		return get(cdTabelaPreco, null);
	}

	public static TabelaPreco get(int cdTabelaPreco, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_tabela_preco WHERE cd_tabela_preco=?");
			pstmt.setInt(1, cdTabelaPreco);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TabelaPreco(rs.getInt("cd_tabela_preco"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_moeda"),
						rs.getInt("nr_versao"),
						rs.getString("nm_tabela_preco"),
						(rs.getTimestamp("dt_inicio_validade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio_validade").getTime()),
						(rs.getTimestamp("dt_final_validade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final_validade").getTime()),
						rs.getInt("lg_ativo"),
						rs.getString("id_tabela_preco"),
						rs.getBytes("img_tabela_preco")==null?null:rs.getBytes("img_tabela_preco"),
						rs.getInt("tp_aplicacao_regras"),
						rs.getInt("gn_tabela_preco"),
						rs.getInt("st_tabela_preco"),
						rs.getInt("lg_padrao"),
						rs.getInt("lg_preco_venda"),
						rs.getInt("lg_imposto_incluso"),
						rs.getInt("nr_precisao_preco"),
						rs.getString("txt_descricao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaPrecoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaPrecoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_tabela_preco");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaPrecoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaPrecoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_tabela_preco", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
