package com.tivic.manager.str;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.Search;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.util.Util;

public class CorServices {
	public static ResultSetMap getSyncData() {
		return getSyncData(null);
	}

	public static ResultSetMap getSyncData(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			boolean str2mob = ManagerConf.getInstance().getAsBoolean("STR_TO_MOB");
			if(str2mob) {
				return com.tivic.manager.fta.CorServices.getSyncData(connect);
			}
			
			String sql = "SELECT * FROM str_cor";
					
			if(Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1"))
				sql = "SELECT A.*, A.cod_cor as cd_cor FROM COR A";
			
			pstmt = connect.prepareStatement(sql);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		int qtLimite = 0;
		int tpRelatorio = 0;
		String ordenacao = "ASC";
		for (int i=0; criterios!=null && i<criterios.size(); i++){
			if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
				qtLimite = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
				break;
			}
			if (criterios.get(i).getColumn().equalsIgnoreCase("tpRelatorio")) {
				tpRelatorio = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
				break;
			}
			if (criterios.get(i).getColumn().equalsIgnoreCase("ordenacao")) {
				ordenacao = criterios.get(i).getValue();
				criterios.remove(i);
				if( ordenacao.equals("ASC") || !ordenacao.equals("DESC")  )
					ordenacao = "ASC";
				break;
			}
		}
		String[] limitSkip = Util.getLimitAndSkip(qtLimite, 0);
		return Search.find("SELECT "+limitSkip[0]+" A.*, H.nm_razao_social AS nm_empresa, I.nm_pessoa AS nm_fantasia, C.nm_pessoa, " +
				           "       C.nm_pessoa AS nm_favorecido, C.st_cadastro, D.nr_conta, D.nr_dv, D.nm_conta, E.nr_agencia, " +
				           "       F.nr_banco, F.nm_banco, G.nm_tipo_documento, G.sg_tipo_documento, J.nm_arquivo " +
				           "FROM adm_conta_pagar A " +
				           "JOIN grl_empresa                          B   ON (A.cd_empresa = B.cd_empresa) " +
				           "LEFT OUTER JOIN grl_pessoa                C   ON (A.cd_pessoa  = C.cd_pessoa) " +
				           "LEFT OUTER JOIN grl_pessoa_juridica       H   ON (B.cd_empresa  = H.cd_pessoa) " +
				           "LEFT OUTER JOIN grl_pessoa                I   ON (H.cd_pessoa  = I.cd_pessoa) " +
				           "LEFT OUTER JOIN adm_conta_financeira      D   ON (A.cd_conta = D.cd_conta) " +
				           "LEFT OUTER JOIN grl_agencia               E   ON (D.cd_agencia = E.cd_agencia)" +
				           "LEFT OUTER JOIN grl_banco                 F   ON (E.cd_banco = F.cd_banco) "+
				           "LEFT OUTER JOIN adm_tipo_documento        G   ON (A.cd_tipo_documento = G.cd_tipo_documento) " +
				           "LEFT OUTER JOIN grl_arquivo               J   ON (A.cd_arquivo = J.cd_arquivo) " +
           ((tpRelatorio == 0) ? "" : "JOIN adm_conta_factoring       K   ON (A.cd_conta_pagar = K.cd_conta_pagar) "),
				           " ORDER BY A.DT_VENCIMENTO "+ordenacao+" "+limitSkip[1],
				           criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	
	
	public static Cor getByNmCor(String nmCor) {
		return getByNmCor(nmCor, null);
	}

	public static Cor getByNmCor(String nmCor, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			
			String sql = "SELECT * FROM fta_cor WHERE nm_cor=? ";
			
			if(lgBaseAntiga)
				sql = "SELECT * FROM cor WHERE nm_cor=?";
			
			pstmt = connect.prepareStatement(sql);
			pstmt.setString(1, nmCor);
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				if(lgBaseAntiga) {
					return new Cor(rs.getInt("cod_cor"),
							rs.getString("nm_cor"));
				}
				else {
					return new Cor(rs.getInt("cd_cor"),
							rs.getString("nm_cor"));
				}
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CorServices.getByNmCor: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CorServices.getByNmCor: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}