package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class AitReportServicesDAO {
	private Connection connect;
	
	public AitReportServicesDAO(Connection connect){
		this.connect = connect;
	}
	

	@SuppressWarnings("deprecation")
	public ResultSetMap find(ArrayList<ItemComparator> criterios){
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA"); 
		int qtLimite = 0;
		boolean last = false;
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		for (int i=0; criterios!=null && i<criterios.size(); i++){
			if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite"))
				qtLimite = Integer.parseInt(criterios.get(i).getValue());
			else if(criterios.get(i).getColumn().equalsIgnoreCase("last"))
				last = true; //buscar ultimos "qtLimite" registros
			else
				crt.add(criterios.get(i));
		}
		
		String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(qtLimite, 0);
		String sql = "";
		if(lgBaseAntiga) {
			sql = "SELECT "+sqlLimit[0]+" "
					+ " A.*, A.CODIGO_AIT AS CD_AIT , A.COD_AGENTE as CD_AGENTE, A.UF_VEICULO AS SG_UF_VEICULO, A.CD_RENAVAN AS NR_RENAVAN, "
					+ " A.nr_ait as id_ait, "
					+ " B.nr_cod_detran, B.DS_INFRACAO2 AS DS_INFRACAO, B.nr_artigo, B.nr_inciso, "
					+ "		B.nr_paragrafo, B.nr_alinea, B.nm_natureza, B.nr_pontuacao, B.vl_infracao,"
					+ " C.nm_municipio, C.nm_uf, "
					+ " D.nm_bairro, "
					+ " E.nm_categoria, "
					+ " F.nm_cor, "
					+ " G.ds_especie, "
					+ " H.nm_marca, H.nm_modelo,"
					+ " I.nm_equipamento, I.id_equipamento, I.nm_marca AS marca_equipamento, "
					+ " I.nm_modelo AS modelo_equipamento, I.tp_equipamento, " 
					+ " J.nm_agente, J.nr_matricula, "
					+ " K.dt_movimento, K.tp_status, K.nr_processo, "
					+ " M.dt_afericao, M.id_identificacao_inmetro, M.nm_sentido_rodovia, M.nr_pista, "
					+ " P.nm_bairro, "
					+ " Q.cod_municipio, Q.nm_municipio AS CIDADE_PROPRIETARIO "
					+ " "
					+ " FROM ait A "
					+ " LEFT OUTER JOIN infracao 		 	B ON (A.cod_infracao = B.cod_infracao) "
					+ " LEFT OUTER JOIN municipio 		 	C ON (A.cod_municipio = C.cod_municipio) "
					+ " LEFT OUTER JOIN bairro 			 	D ON (A.cod_bairro = D.cod_bairro) "
					+ " LEFT OUTER JOIN categoria_veiculo	E ON (A.cod_categoria = E.cod_categoria) "
					+ " LEFT OUTER JOIN cor 				F ON (A.cod_cor = F.cod_cor) "
					+ " LEFT OUTER JOIN especie_veiculo 	G ON (A.cod_especie = G.cod_especie) "
					+ " LEFT OUTER JOIN marca_modelo 	 	H ON (A.cod_marca = H.cod_marca) "
					+ " LEFT OUTER JOIN grl_equipamento 	I ON (A.cd_equipamento = I.cd_equipamento) "
					+ " LEFT OUTER JOIN agente 			 	J ON (A.cod_agente = J.cod_agente) "
					+ " LEFT OUTER JOIN ait_movimento		K  ON (A.codigo_ait = K.codigo_ait AND A.tp_status = K.tp_status)"
					+ " LEFT OUTER JOIN mob_ait_evento      L  ON (A.codigo_ait = L.cd_ait) "
					+ " LEFT OUTER JOIN mob_evento_equipamento M  ON (L.cd_evento = M.cd_evento) "
					+ " LEFT OUTER JOIN bairro             P  ON (A.cod_bairro = P.cod_bairro)"
					+ " LEFT OUTER JOIN municipio             Q  ON (Q.cod_municipio = P.cod_municipio)"
					+ " WHERE 1=1";
			
					return Search.findAndLog(sql, "ORDER BY A.CODIGO_AIT LIMIT 50", crt, connect!=null ? connect : Conexao.conectar(), connect==null);
					
		}
		else
		{
		
				sql = "SELECT "+sqlLimit[0]+" "
				+ " K.dt_movimento, K.tp_status, K.nr_processo, A.*,"
				+ " B.nr_cod_detran, B.ds_infracao, B.nr_artigo, nr_inciso, "
				+ "		nr_paragrafo, nr_alinea, nm_natureza, nr_pontuacao, "
				+ "		B.lg_suspensao_cnh, "
				+ " C.nm_cidade, C.nm_cidade AS nm_municipio,"
				+ " 	C1.sg_estado, C1.sg_estado AS nm_uf, C1.nm_estado,"
				+ " E.nm_categoria,"
				+ " F.nm_cor,"
				+ " G.ds_especie,"
				+ " H.nm_marca, H.nm_modelo,"
				+ " I.nm_equipamento, I.id_equipamento, I.nm_marca AS marca_equipamento, I.nm_modelo AS modelo_equipamento, I.tp_equipamento, " 
				+ " J.nm_agente, J.nr_matricula, "
				+ " M.dt_afericao, M.id_identificacao_inmetro, "
				+ " N.ds_logradouro, "
				+ " P.nm_bairro "
				+ " "
				+ " FROM 			mob_ait 			   A"
				+ " LEFT OUTER JOIN mob_infracao 		   B  ON (A.cd_infracao = B.cd_infracao)"
				+ " LEFT OUTER JOIN grl_cidade 			   C  ON (A.cd_cidade = C.cd_cidade)"
				+ " LEFT OUTER JOIN grl_estado 			   C1 ON (C.cd_estado = C1.cd_estado)"
				+ " LEFT OUTER JOIN fta_categoria_veiculo  E  ON (A.cd_categoria_veiculo = E.cd_categoria)"
				+ " LEFT OUTER JOIN fta_cor 			   F  ON (A.cd_cor_veiculo = F.cd_cor)"
				+ " LEFT OUTER JOIN fta_especie_veiculo    G  ON (A.cd_especie_veiculo = G.cd_especie)"
				+ " LEFT OUTER JOIN fta_marca_modelo	   H  ON (A.cd_marca_veiculo = H.cd_marca)"
				+ " "
				+ " LEFT OUTER JOIN grl_equipamento		   I  ON (A.cd_equipamento = I.cd_equipamento)"
 			    + " LEFT OUTER JOIN mob_agente			   J  ON (A.cd_agente = J.cd_agente)"
 			    + " LEFT OUTER JOIN mob_ait_movimento	   K  ON (A.cd_ait = K.cd_ait AND A.cd_movimento_atual = K.cd_movimento)"
 			    + " "
 			    + " LEFT OUTER JOIN mob_ait_evento         L  ON (A.cd_ait = L.cd_ait) "
 			    + " LEFT OUTER JOIN mob_evento_equipamento M  ON (L.cd_evento = M.cd_evento) "
 			    + " LEFT OUTER JOIN mob_ait_proprietario   N  ON (A.cd_ait = N.cd_ait) "
 			    + " LEFT OUTER JOIN grl_bairro             P  ON (A.cd_bairro = P.cd_bairro) "
				+ " WHERE 1=1";
		
				return Search.find(sql, (last ? " ORDER BY A.cd_ait DESC ": "") + sqlLimit[1], crt, connect!=null ? connect : Conexao.conectar(), connect==null);
				
		}
	}
	
	protected ResultSet getImgLogosBaseOlg(){
		
		try 
		{
			String sql = "SELECT img_logo_orgao, img_logo_departamento FROM PARAMETRO";
			PreparedStatement pst;
			pst = connect.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			
			return rs;
		} catch (SQLException e) 
		{
			System.out.println("Error in AitReportServicesDAO > getImgLogosBaseOlg()");
			e.printStackTrace();
			return null;
		}
		
	}
	
	protected String getSgEstadoOrgao()
	{
		
		try
		{
			String sql =  "SELECT A.cd_orgao, A.nm_orgao, " 
						+ "B.nm_cidade as Cidade, " 
						+ "C.sg_estado as sg " 
						+ "FROM mob_orgao A "  
						+ "INNER JOIN grl_cidade B on (A.cd_cidade = B.cd_cidade) " 
						+ "INNER join grl_estado C on (B.cd_estado = C.cd_estado) ";
			
			PreparedStatement ps = connect.prepareStatement(sql);	
			ResultSetMap rsmEstado = new ResultSetMap(ps.executeQuery());
		
			String sgEstado = null;
		
			if (rsmEstado.next()) 
			{
				sgEstado = rsmEstado.getString("SG");
			}
			
			return sgEstado;
			
		}
		catch(Exception e)
		{
			System.out.println("Erro in AitReportNipDAO > getUfEstadoOrgao()");
			e.printStackTrace();
			return null;
		}
	}
	
	protected String getSgEstadoOrgaoBaseAntiga()
	{
		
		try
		{
			String sql = "SELECT A.cd_municipio_autuador, B.nm_uf AS sg "
					   + "FROM parametro A "
					   + "LEFT JOIN municipio B ON (B.cod_municipio = A.cd_municipio_autuador) ";
		
			PreparedStatement ps = connect.prepareStatement(sql);	
			ResultSetMap rsmEstado = new ResultSetMap(ps.executeQuery());
	
			String sgEstado = null;
	
			if (rsmEstado.next()) 
			{
				sgEstado = rsmEstado.getString("SG");
			}
		
			return sgEstado;
		}
		catch (Exception e)
		{
			System.out.println("Erro in AitReportNipDAO > getSgEstadoOrgaoBaseAntiga()");
			e.printStackTrace();
			return null;
		}
		
	}
	
	@SuppressWarnings("static-access")
	protected ArquivoMovimento pegarArquivoRetorno(int cdAit, int tpStatus, Connection connect) throws ValidacaoException, AitReportErrorException
	{
		
		ArquivoMovimento arquivoMovimento = new ArquivoMovimento();
		ArquivoMovimentoDAO arquivoMovimentoDAO = new ArquivoMovimentoDAO();
		
		try
		{
			String sql = "SELECT cd_arquivo_movimento, cd_movimento FROM mob_arquivo_movimento "
					   + "	WHERE cd_ait = ? AND tp_status = ? AND nr_erro is null";
			
			PreparedStatement ps = connect.prepareStatement(sql);	
			ps.setInt(1, cdAit);
			ps.setInt(2, tpStatus);
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			if (rsm.next())
			{
				arquivoMovimento = arquivoMovimentoDAO.get(rsm.getInt("cd_arquivo_movimento"), rsm.getInt("cd_movimento"), cdAit);
			}
			else
			{
				throw new  AitReportErrorException("Não fui possivel obter a data limite recurso, verifique os envios do movimento de fim prazo defesa.");
			}
			
			return arquivoMovimento;
		}
		catch (Exception e)
		{
			System.out.println("Erro in AitReportNaiDAO > pegarArquivoRetorno()");
			e.printStackTrace();
			throw new AitReportErrorException("Erro: " + e.getMessage());
		}
		
	}
}
