package com.tivic.manager.ptc.fix;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tivic.manager.grl.Estado;
import com.tivic.manager.grl.EstadoServices;
import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.ptc.fici.ApresentacaoCondutorDAO;
import com.tivic.manager.util.cdi.InjectApplicationBuilder;
import com.tivic.sol.cdi.InicializationBeans;
import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;

public class FixApresentacaoCondutor {
	
	public static void main(String[] args) {
		try {
			InicializationBeans.init(new InjectApplicationBuilder());
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	
		fixApresentacaoCondutorTable();
	}
	
	/**
	 * Migração dos dados de Apresentação de Condutor para nova tabela
	 * 
	 * @since 07/03/2022
	 * @author Paulo Lima
	 * @throws IOException 
	 * @return {@link Optional<String>}
	 */
	public static void  fixApresentacaoCondutorTable() {
		Connection conn = Conexao.conectar();
		
		try {
			conn.setAutoCommit(false);
			
			String queryDocumento = 
					"SELECT A.* " +
					"FROM ptc_documento A " +
					"WHERE A.cd_tipo_documento = 16 " +
					"  AND EXISTS (SELECT cd_documento FROM grl_formulario_atributo WHERE cd_documento = A.cd_documento)";
			
			ResultSetMap rsmDocumento = new ResultSetMap(conn.prepareStatement(queryDocumento).executeQuery());
			
			while (rsmDocumento.next()) {
				int cdDocumento = rsmDocumento.getInt("cd_documento");
				
				HashMap<String, Object> atributosValor = getFormularioByDocumento(cdDocumento, conn);			
				
				if(atributosValor == null)
					continue;
				
				System.out.println("######### Processando Documento " + rsmDocumento.getString("NR_DOCUMENTO") + " ###########");
				String atributoCpfValue = getHashValue(String.valueOf(atributosValor.get("nrCpfCondutor")));
				
				String[] values = {"m","mg"};
				String rgNumber = getHashValue(String.valueOf(atributosValor.get("nrRgCondutor")));
				String sgRgEstado = "";
				String rgNumero = rgNumber;
				Estado estado = null;
				
				if(rgNumber != null && !rgNumber.trim().equals("")) {
					Matcher match = Pattern.compile("^(mg|MG|mG|Mg|m|M)(\\w+)").matcher(rgNumber);					

					if(match.find()) {
						sgRgEstado = Arrays.stream(values).anyMatch(sg -> sg.equals(match.group(1).toLowerCase())) ? "MG" : "";
						estado = EstadoServices.getBySg(sgRgEstado, conn);
						rgNumero = match.group(2);
					}
				}
				
				String nrCpfCnpj = atributoCpfValue != null ? 
						getHashValue(String.valueOf(atributosValor.get("nrCpfCondutor"))) :
						getHashValue(String.valueOf(atributosValor.get("nrCnpjCondutor")));

						nrCpfCnpj = nrCpfCnpj != null ? nrCpfCnpj.replaceAll("[\\D]", "") : null;
				
				String tel1 = getHashValue(String.valueOf(atributosValor.get("nrTelefone1Condutor")));
				String tel2 = getHashValue(String.valueOf(atributosValor.get("nrTelefone2Condutor")));
				
				tel1 = tel1 != null ? tel1.replaceAll("[\\D]", "") : null;
				tel2 = tel2 != null ? tel2.replaceAll("[\\D]", "") : null;
				
				int cdEstado = estado != null && estado.getSgEstado().equals("MG") ? estado.getCdEstado() : 0;
				ApresentacaoCondutor apresentacaoCondutor = new ApresentacaoCondutor(
						0, 
						cdDocumento,
						getHashValue(String.valueOf(atributosValor.get("nrCnhCondutor"))),
						null,
						0, 
						getHashValue(String.valueOf(atributosValor.get("nmCondutor"))),
						getHashValue(String.valueOf(atributosValor.get("dsEnderecoCondutor"))),
						getHashValue(String.valueOf(atributosValor.get("dsComplementoEnderecoCondutor"))),
						getHashValue(String.valueOf(atributosValor.get("nmCidadeCondutor"))),
						tel1,
						tel2,
						nrCpfCnpj,
						rgNumero,
						4,
						getHashValue(String.valueOf(atributosValor.get("paisCnhCondutor"))),
						null,
						cdEstado,
						null
						);

				ApresentacaoCondutorDAO.insert(apresentacaoCondutor,conn);
			}
			
			conn.commit();
		} catch (SQLException e) {
			Conexao.rollback(conn);
			System.out.println(e.getMessage());
			
		} finally {
			Conexao.desconectar(conn);
		}
	}
	
	private static HashMap<String, Object> getFormularioByDocumento(int cdDocumento, Connection conn) {
		try {
			String queryForm = 
					"SELECT cd_documento, nm_formulario, nm_atributo, txt_atributo_valor " +
					"FROM grl_formulario A, grl_formulario_atributo B, grl_formulario_atributo_valor C " +
					"WHERE A.cd_formulario = B.cd_formulario " +
					"AND B.cd_formulario_atributo = C.cd_formulario_atributo " +
					"AND C.cd_documento = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(queryForm);
			pstmt.setInt(1, cdDocumento);
			
			HashMap<String, Object> atributosValor = new HashMap<String, Object>();
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.size() == 0) {
				return null;
			}
			
			while(rsm.next()) {
				atributosValor.put(rsm.getString("NM_ATRIBUTO"), rsm.getString("TXT_ATRIBUTO_VALOR"));
			}
			
			return atributosValor;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	private static String getHashValue(String value) {
		return value.equals("null") ? null : value;
	}
}
