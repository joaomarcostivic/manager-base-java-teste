package com.tivic.manager.fsc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import sol.dao.ResultSetMap;

import com.tivic.sol.connection.Conexao;

public class TipoCreditoServices {
	
	public static final int TP_CREDITO_TRIBUTADA_MERCADO_INTERNO 	 = 0;
	public static final int TP_CREDITO_NAO_TRIBUTADA_MERCADO_INTERNO = 1;
	public static final int TP_CREDITO_EXPORTACAO 					 = 2;

	public static final String[] tiposCreditoGrupo = {"CÓDIGOS VINCULADOS À RECEITA TRIBUTADA NO MERCADO INTERNO", "CÓDIGOS VINCULADOS À RECEITA NÃO TRIBUTADA NO MERCADO INTERNO", "CÓDIGOS VINCULADOS À RECEITA DE EXPORTAÇÃO"};
	
	public static void init() {
		/*
		 * TIPOS DE DOCUMENTOS
		 */
		ArrayList<TipoCredito> tipoCredito = new ArrayList<TipoCredito>();
		tipoCredito.add(new TipoCredito(0, "Alíquota Básica", "101", TipoCreditoServices.TP_CREDITO_TRIBUTADA_MERCADO_INTERNO));
		tipoCredito.add(new TipoCredito(0, "Alíquotas Diferenciadas", "102", TipoCreditoServices.TP_CREDITO_TRIBUTADA_MERCADO_INTERNO));
		tipoCredito.add(new TipoCredito(0, "Alíquota por Unidade de Produto", "103", TipoCreditoServices.TP_CREDITO_TRIBUTADA_MERCADO_INTERNO));
		tipoCredito.add(new TipoCredito(0, "Estoque de Abertura", "104", TipoCreditoServices.TP_CREDITO_TRIBUTADA_MERCADO_INTERNO));
		tipoCredito.add(new TipoCredito(0, "Aquisição Embalagens para revenda", "105", TipoCreditoServices.TP_CREDITO_TRIBUTADA_MERCADO_INTERNO));
		tipoCredito.add(new TipoCredito(0, "Presumido da Agroindústria", "106", TipoCreditoServices.TP_CREDITO_TRIBUTADA_MERCADO_INTERNO));
		tipoCredito.add(new TipoCredito(0, "Outros Créditos Presumidos", "107", TipoCreditoServices.TP_CREDITO_TRIBUTADA_MERCADO_INTERNO));
		tipoCredito.add(new TipoCredito(0, "Importação", "108", TipoCreditoServices.TP_CREDITO_TRIBUTADA_MERCADO_INTERNO));
		tipoCredito.add(new TipoCredito(0, "Atividade Imobiliária", "109", TipoCreditoServices.TP_CREDITO_TRIBUTADA_MERCADO_INTERNO));
		tipoCredito.add(new TipoCredito(0, "Outros", "199", TipoCreditoServices.TP_CREDITO_TRIBUTADA_MERCADO_INTERNO));
		tipoCredito.add(new TipoCredito(0, "Alíquota Básica", "201", TipoCreditoServices.TP_CREDITO_NAO_TRIBUTADA_MERCADO_INTERNO));
		tipoCredito.add(new TipoCredito(0, "Alíquotas Diferenciadas", "202", TipoCreditoServices.TP_CREDITO_NAO_TRIBUTADA_MERCADO_INTERNO));
		tipoCredito.add(new TipoCredito(0, "Alíquota por Unidade de Produto", "203", TipoCreditoServices.TP_CREDITO_NAO_TRIBUTADA_MERCADO_INTERNO));
		tipoCredito.add(new TipoCredito(0, "Estoque de Abertura", "204", TipoCreditoServices.TP_CREDITO_NAO_TRIBUTADA_MERCADO_INTERNO));
		tipoCredito.add(new TipoCredito(0, "Aquisição Embalagens para revenda", "205", TipoCreditoServices.TP_CREDITO_NAO_TRIBUTADA_MERCADO_INTERNO));
		tipoCredito.add(new TipoCredito(0, "Presumido da Agroindústria", "206", TipoCreditoServices.TP_CREDITO_NAO_TRIBUTADA_MERCADO_INTERNO));
		tipoCredito.add(new TipoCredito(0, "Outros Créditos Presumidos", "207", TipoCreditoServices.TP_CREDITO_NAO_TRIBUTADA_MERCADO_INTERNO));
		tipoCredito.add(new TipoCredito(0, "Importação", "208", TipoCreditoServices.TP_CREDITO_NAO_TRIBUTADA_MERCADO_INTERNO));
		tipoCredito.add(new TipoCredito(0, "Outros", "299", TipoCreditoServices.TP_CREDITO_NAO_TRIBUTADA_MERCADO_INTERNO));
		tipoCredito.add(new TipoCredito(0, "Alíquota Básica", "301", TipoCreditoServices.TP_CREDITO_EXPORTACAO));
		tipoCredito.add(new TipoCredito(0, "Alíquotas Diferenciadas", "302", TipoCreditoServices.TP_CREDITO_EXPORTACAO));
		tipoCredito.add(new TipoCredito(0, "Alíquota por Unidade de Produto", "303", TipoCreditoServices.TP_CREDITO_EXPORTACAO));
		tipoCredito.add(new TipoCredito(0, "Estoque de Abertura", "304", TipoCreditoServices.TP_CREDITO_EXPORTACAO));
		tipoCredito.add(new TipoCredito(0, "Aquisição Embalagens para revenda", "305", TipoCreditoServices.TP_CREDITO_EXPORTACAO));
		tipoCredito.add(new TipoCredito(0, "Presumido da Agroindústria", "306", TipoCreditoServices.TP_CREDITO_EXPORTACAO));
		tipoCredito.add(new TipoCredito(0, "Outros Créditos Presumidos", "307", TipoCreditoServices.TP_CREDITO_EXPORTACAO));
		tipoCredito.add(new TipoCredito(0, "Importação", "308", TipoCreditoServices.TP_CREDITO_EXPORTACAO));
		tipoCredito.add(new TipoCredito(0, "Outros", "399", TipoCreditoServices.TP_CREDITO_EXPORTACAO));
		//
		Connection connect = Conexao.conectar();
		try	{
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM fsc_tipo_credito WHERE nr_tipo_credito = ? AND tp_grupo_tipo_credito= ?");
			for(int i=0; i<tipoCredito.size(); i++)	{
				pstmt.setString(1, tipoCredito.get(i).getNrTipoCredito());
				pstmt.setInt(2, tipoCredito.get(i).getTpGrupoTipoCredito());
				if(!pstmt.executeQuery().next())
					TipoCreditoDAO.insert(tipoCredito.get(i), connect);
			}
			System.out.println("Inicialização de Tipos de Créditos concluida!");
			return;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(!isConnectionNull);
			}
			
			ResultSetMap rsm = TipoCreditoDAO.getAll();
			while(rsm.next()){
				rsm.setValueToField("CL_TIPO_CREDITO_GRUPO", tiposCreditoGrupo[rsm.getInt("TP_GRUPO_TIPO_CREDITO")]);
			}
			rsm.beforeFirst();
			
			return rsm;
			
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoCreditoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoCreditoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
