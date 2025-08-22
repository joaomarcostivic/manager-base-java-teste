package com.tivic.manager.fsc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import sol.dao.ResultSetMap;

import com.tivic.sol.connection.Conexao;

public class TipoContribuicaoSocialServices {
		
	public static void init() {
		/*
		 * TIPOS DE DOCUMENTOS
		 */
		ArrayList<TipoContribuicaoSocial> tipoContribuicaoSocial = new ArrayList<TipoContribuicaoSocial>();
		tipoContribuicaoSocial.add(new TipoContribuicaoSocial(0, "Contribuição não-cumulativa apurada a alíquota básica", "01"));
		tipoContribuicaoSocial.add(new TipoContribuicaoSocial(0, "Contribuição não-cumulativa apurada a alíquotas diferenciadas", "02"));
		tipoContribuicaoSocial.add(new TipoContribuicaoSocial(0, "Contribuição não-cumulativa apurada a alíquota por unidade de medida de produto", "03"));
		tipoContribuicaoSocial.add(new TipoContribuicaoSocial(0, "Contribuição não-cumulativa apurada a alíquota básica  Atividade Imobiliária", "04"));
		tipoContribuicaoSocial.add(new TipoContribuicaoSocial(0, "Contribuição apurada por substituição tributária", "31"));
		tipoContribuicaoSocial.add(new TipoContribuicaoSocial(0, "Contribuição apurada por substituição tributária  Vendas à Zona Franca de Manaus", "32"));
		tipoContribuicaoSocial.add(new TipoContribuicaoSocial(0, "Contribuição cumulativa apurada a alíquota básica", "51"));
		tipoContribuicaoSocial.add(new TipoContribuicaoSocial(0, "Contribuição cumulativa apurada a alíquotas diferenciadas", "52"));
		tipoContribuicaoSocial.add(new TipoContribuicaoSocial(0, "Contribuição cumulativa apurada a alíquota por unidade de medida de produto", "53"));
		tipoContribuicaoSocial.add(new TipoContribuicaoSocial(0, "Contribuição cumulativa apurada a alíquota básica  Atividade Imobiliária", "54"));
		tipoContribuicaoSocial.add(new TipoContribuicaoSocial(0, "Contribuição apurada da Atividade Imobiliária  RET", "70"));
		tipoContribuicaoSocial.add(new TipoContribuicaoSocial(0, "Contribuição apurada de SCP  Incidência Não Cumulativa", "71"));
		tipoContribuicaoSocial.add(new TipoContribuicaoSocial(0, "Contribuição apurada de SCP  Incidência Cumulativa", "72"));
		tipoContribuicaoSocial.add(new TipoContribuicaoSocial(0, "Contribuição para o PIS/Pasep  Folha de Salários", "99"));
		//
		Connection connect = Conexao.conectar();
		try	{
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM fsc_tipo_contribuicao_social WHERE nr_tipo_contribuicao_social = ?");
			for(int i=0; i<tipoContribuicaoSocial.size(); i++)	{
				pstmt.setString(1, tipoContribuicaoSocial.get(i).getNrTipoContribuicaoSocial());
				if(!pstmt.executeQuery().next())
					TipoContribuicaoSocialDAO.insert(tipoContribuicaoSocial.get(i), connect);
			}
			System.out.println("Inicialização de Tipos de Contribuição Social concluida!");
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
			
			ResultSetMap rsm = TipoContribuicaoSocialDAO.getAll();
			
			return rsm;
			
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoContribuicaoSocialDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoContribuicaoSocialDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
