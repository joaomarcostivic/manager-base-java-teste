package com.tivic.manager.fsc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.*;
import java.util.ArrayList;

public class NotaFiscalItemTributoDAO{

	public static int insert(NotaFiscalItemTributo objeto) {
		return insert(objeto, null);
	}

	public static int insert(NotaFiscalItemTributo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fsc_nota_fiscal_item_tributo (cd_nota_fiscal,"+
			                                  "cd_item,"+
			                                  "cd_tributo,"+
			                                  "cd_tributo_aliquota,"+
			                                  "tp_regime_tributario,"+
			                                  "tp_origem,"+
			                                  "tp_calculo,"+
			                                  "vl_base_calculo,"+
			                                  "vl_outras_despesas,"+
			                                  "vl_outros_impostos,"+
			                                  "pr_aliquota,"+
			                                  "vl_tributo,"+
			                                  "pr_credito,"+
			                                  "vl_credito,"+
			                                  "nr_classe,"+
			                                  "nr_enquadramento," +
			                                  "cd_situacao_tributaria," +
			                                  "vl_base_retencao," +
			                                  "vl_retido) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdNotaFiscal()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdNotaFiscal());
			if(objeto.getCdItem()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdItem());
			if(objeto.getCdTributo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTributo());
			if(objeto.getCdTributoAliquota()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTributoAliquota());
			pstmt.setInt(5,objeto.getTpRegimeTributario());
			pstmt.setInt(6,objeto.getTpOrigem());
			pstmt.setInt(7,objeto.getTpCalculo());
			pstmt.setFloat(8,objeto.getVlBaseCalculo());
			pstmt.setFloat(9,objeto.getVlOutrasDespesas());
			pstmt.setFloat(10,objeto.getVlOutrosImpostos());
			pstmt.setFloat(11,objeto.getPrAliquota());
			pstmt.setFloat(12,objeto.getVlTributo());
			pstmt.setFloat(13,objeto.getPrCredito());
			pstmt.setFloat(14,objeto.getVlCredito());
			pstmt.setString(15,objeto.getNrClasse());
			pstmt.setString(16,objeto.getNrEnquadramento());
			if(objeto.getCdSituacaoTributaria()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdSituacaoTributaria());
			pstmt.setFloat(18, objeto.getVlBaseRetencao());
			pstmt.setFloat(19, objeto.getVlRetido());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalItemTributoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalItemTributoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(NotaFiscalItemTributo objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(NotaFiscalItemTributo objeto, int cdNotaFiscalOld, int cdItemOld, int cdTributoOld) {
		return update(objeto, cdNotaFiscalOld, cdItemOld, cdTributoOld, null);
	}

	public static int update(NotaFiscalItemTributo objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(NotaFiscalItemTributo objeto, int cdNotaFiscalOld, int cdItemOld, int cdTributoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fsc_nota_fiscal_item_tributo SET cd_nota_fiscal=?,"+
												      		   "cd_item=?,"+
												      		   "cd_tributo=?,"+
												      		   "cd_tributo_aliquota=?,"+
												      		   "tp_regime_tributario=?,"+
												      		   "tp_origem=?,"+
												      		   "tp_calculo=?,"+
												      		   "vl_base_calculo=?,"+
												      		   "vl_outras_despesas=?,"+
												      		   "vl_outros_impostos=?,"+
												      		   "pr_aliquota=?,"+
												      		   "vl_tributo=?,"+
												      		   "pr_credito=?,"+
												      		   "vl_credito=?,"+
												      		   "nr_classe=?,"+
												      		   "nr_enquadramento=?," +
												      		   "cd_situacao_tributaria=?," +
												      		   "vl_base_retencao=?," +
												      		   "vl_retido=? WHERE cd_nota_fiscal=? AND cd_item=? AND cd_tributo=?");
			pstmt.setInt(1,objeto.getCdNotaFiscal());
			pstmt.setInt(2,objeto.getCdItem());
			pstmt.setInt(3,objeto.getCdTributo());
			if(objeto.getCdTributoAliquota()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTributoAliquota());
			pstmt.setInt(5,objeto.getTpRegimeTributario());
			pstmt.setInt(6,objeto.getTpOrigem());
			pstmt.setInt(7,objeto.getTpCalculo());
			pstmt.setFloat(8,objeto.getVlBaseCalculo());
			pstmt.setFloat(9,objeto.getVlOutrasDespesas());
			pstmt.setFloat(10,objeto.getVlOutrosImpostos());
			pstmt.setFloat(11,objeto.getPrAliquota());
			pstmt.setFloat(12,objeto.getVlTributo());
			pstmt.setFloat(13,objeto.getPrCredito());
			pstmt.setFloat(14,objeto.getVlCredito());
			pstmt.setString(15,objeto.getNrClasse());
			pstmt.setString(16,objeto.getNrEnquadramento());
			if(objeto.getCdSituacaoTributaria()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdSituacaoTributaria());
			pstmt.setFloat(18, objeto.getVlBaseRetencao());
			pstmt.setFloat(19, objeto.getVlRetido());
			pstmt.setInt(20, cdNotaFiscalOld!=0 ? cdNotaFiscalOld : objeto.getCdNotaFiscal());
			pstmt.setInt(21, cdItemOld!=0 ? cdItemOld : objeto.getCdItem());
			pstmt.setInt(22, cdTributoOld!=0 ? cdTributoOld : objeto.getCdTributo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalItemTributoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalItemTributoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdNotaFiscal, int cdItem, int cdTributo) {
		return delete(cdNotaFiscal, cdItem, cdTributo, null);
	}

	public static int delete(int cdNotaFiscal, int cdItem, int cdTributo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fsc_nota_fiscal_item_tributo WHERE cd_nota_fiscal=? AND cd_item=? AND cd_tributo=?");
			pstmt.setInt(1, cdNotaFiscal);
			pstmt.setInt(2, cdItem);
			pstmt.setInt(3, cdTributo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalItemTributoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalItemTributoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static NotaFiscalItemTributo get(int cdNotaFiscal, int cdItem, int cdTributo) {
		return get(cdNotaFiscal, cdItem, cdTributo, null);
	}

	public static NotaFiscalItemTributo get(int cdNotaFiscal, int cdItem, int cdTributo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fsc_nota_fiscal_item_tributo WHERE cd_nota_fiscal=? AND cd_item=? AND cd_tributo=?");
			pstmt.setInt(1, cdNotaFiscal);
			pstmt.setInt(2, cdItem);
			pstmt.setInt(3, cdTributo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new NotaFiscalItemTributo(rs.getInt("cd_nota_fiscal"),
						rs.getInt("cd_item"),
						rs.getInt("cd_tributo"),
						rs.getInt("cd_tributo_aliquota"),
						rs.getInt("tp_regime_tributario"),
						rs.getInt("tp_origem"),
						rs.getInt("tp_calculo"),
						rs.getFloat("vl_base_calculo"),
						rs.getFloat("vl_outras_despesas"),
						rs.getFloat("vl_outros_impostos"),
						rs.getFloat("pr_aliquota"),
						rs.getFloat("vl_tributo"),
						rs.getFloat("pr_credito"),
						rs.getFloat("vl_credito"),
						rs.getString("nr_classe"),
						rs.getString("nr_enquadramento"),
						rs.getInt("cd_situacao_tributaria"),
						rs.getFloat("vl_base_retencao"),
						rs.getFloat("vl_retido"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalItemTributoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalItemTributoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fsc_nota_fiscal_item_tributo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalItemTributoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalItemTributoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fsc_nota_fiscal_item_tributo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
