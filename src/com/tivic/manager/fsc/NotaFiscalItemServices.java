package com.tivic.manager.fsc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import sol.dao.ResultSetMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

public class NotaFiscalItemServices {

	
	public static ArrayList<NotaFiscalItem> getAllByCdNfe(int cdNotaFiscal){
		
		try{
			Connection connect = Conexao.conectar();
			ArrayList<NotaFiscalItem> itens = new ArrayList<NotaFiscalItem>();
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM fsc_nota_fiscal_item " +
	                											"WHERE cd_nota_fiscal = "+cdNotaFiscal);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				itens.add(NotaFiscalItemDAO.get(cdNotaFiscal, rs.getInt("cd_item")));
			}
		
			return itens;
		}
		
		catch(Exception e){return null;}
	}
	
	
	public static ResultSetMap getAllByCdNfe(int cdNotaFiscal, int cdEmpresa){
		return getAllByCdNfe(cdNotaFiscal, cdEmpresa, null);
	}
	
	public static ResultSetMap getAllByCdNfe(int cdNotaFiscal, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, (A.vl_unitario * A.qt_tributario) AS vl_total, D.nr_ncm, E.sg_unidade_medida, E.nr_precisao_medida, C.id_reduzido, C.qt_precisao_custo, B.nm_produto_servico, B.txt_especificacao, B.cd_categoria_economica, B.cd_classificacao_fiscal, B.id_produto_servico, C.pr_desconto_maximo, J.nr_codigo_fiscal, A.txt_informacao_adicional " +
                    "FROM fsc_nota_fiscal_item A " +
                    "JOIN grl_produto_servico B ON (A.cd_produto_servico = B.cd_produto_servico) " +
                    ((cdEmpresa != 0) ? "JOIN grl_produto_servico_empresa  C ON (A.cd_produto_servico = C.cd_produto_servico AND C.cd_empresa = "+cdEmpresa+") " : "") +
                    "LEFT OUTER JOIN grl_ncm  D ON (D.cd_ncm = B.cd_ncm) "+
					"LEFT OUTER JOIN grl_unidade_medida  E ON (C.cd_unidade_medida = E.cd_unidade_medida) "+
					"LEFT OUTER JOIN adm_natureza_operacao J ON (J.cd_natureza_operacao = A.cd_natureza_operacao) " +
					"WHERE A.cd_nota_fiscal = "+cdNotaFiscal);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				rsm.setValueToField("vl_unitario", (Util.arredondar(rsm.getFloat("vl_unitario"), (rsm.getInt("qt_precisao_custo") > 10 ? 10 : (rsm.getInt("qt_precisao_custo") <= 0 ? 2 : rsm.getInt("qt_precisao_custo"))))));
				rsm.setValueToField("qt_tributario", (Util.arredondar(rsm.getFloat("qt_tributario"), (rsm.getInt("nr_precisao_medida") > 4 ? 4 : (rsm.getInt("nr_precisao_medida") <= 0 ? 0 : rsm.getInt("nr_precisao_medida"))))));
				rsm.setValueToField("vl_total", (Util.arredondar(rsm.getFloat("vl_unitario"), (rsm.getInt("qt_precisao_custo") > 10 ? 10 : (rsm.getInt("qt_precisao_custo") <= 0 ? 2 : rsm.getInt("qt_precisao_custo")))) * Util.arredondar(rsm.getFloat("qt_tributario"), (rsm.getInt("nr_precisao_medida") > 4 ? 4 : (rsm.getInt("nr_precisao_medida") <= 0 ? 0 : rsm.getInt("nr_precisao_medida"))))));
			}
			rsm.beforeFirst();
			return rsm;
		}
		
		catch(Exception e){System.out.println("Erro: " + e);return null;}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
}
