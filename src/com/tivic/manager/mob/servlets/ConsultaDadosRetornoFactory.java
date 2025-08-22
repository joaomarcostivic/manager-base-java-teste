package com.tivic.manager.mob.servlets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tivic.manager.util.XmlServices;
import com.tivic.manager.wsdl.detran.ba.consultarplaca.ConsultarPlacaDadosRetorno;

import sol.util.Result;

public class ConsultaDadosRetornoFactory {

	public static ConsultarPlacaDadosRetorno gerar(SOAPMessage soapResponse) throws IOException, SOAPException, Exception {
		ConsultarPlacaDadosRetorno consultarPlacaDadosRetorno = new ConsultarPlacaDadosRetorno();
		JSONObject jsonData = getData(soapResponse);
		consultarPlacaDadosRetorno.setCodigoUsuario(jsonData.getInt("codigoUsuario"));
		consultarPlacaDadosRetorno.setCodigoOperacao(jsonData.getString("codigoOperacao"));
		consultarPlacaDadosRetorno.setParamPlaca(jsonData.getString("paramPlaca"));
		consultarPlacaDadosRetorno.setParamUF(jsonData.getString("paramUF"));
		consultarPlacaDadosRetorno.setParamChassi(jsonData.getString("paramChassi"));
		consultarPlacaDadosRetorno.setParamRenavam(jsonData.getString("paramRenavam"));
		consultarPlacaDadosRetorno.setCodigoRetExec(jsonData.getInt("codigoRetExec"));
		consultarPlacaDadosRetorno.setPlacaVeiculo(jsonData.getString("placaVeiculo"));
		consultarPlacaDadosRetorno.setCodSituacaoVeic(jsonData.getString("codSituacaoVeic"));
		consultarPlacaDadosRetorno.setSituacaoVeiculo(jsonData.getString("situacaoVeiculo"));
		consultarPlacaDadosRetorno.setNovaPlaca(jsonData.getString("novaPlaca"));
		consultarPlacaDadosRetorno.setCodigoMunicipio(jsonData.getString("codigoMunicipio"));
		consultarPlacaDadosRetorno.setNomeMunicipio(jsonData.getString("nomeMunicipio"));
		consultarPlacaDadosRetorno.setUnidadeFederacao(jsonData.getString("unidadeFederacao"));
		consultarPlacaDadosRetorno.setNomeUf(jsonData.getString("nomeUf"));
		consultarPlacaDadosRetorno.setIndRouboFurto(jsonData.getString("indRouboFurto"));
		consultarPlacaDadosRetorno.setCodigoMarcaMod(jsonData.getInt("codigoMarcaMod"));
		consultarPlacaDadosRetorno.setMarcaModelo(jsonData.getString("marcaModelo"));
		consultarPlacaDadosRetorno.setMotorDifAcesso(jsonData.getString("motorDifAcesso"));
		consultarPlacaDadosRetorno.setComunicacaoVenda(jsonData.getString("comunicacaoVenda"));
		consultarPlacaDadosRetorno.setCodigoCarroceria(jsonData.getInt("codigoCarroceria"));
		consultarPlacaDadosRetorno.setCarroceria(jsonData.getString("carroceria"));
		consultarPlacaDadosRetorno.setCodigoCor(jsonData.getInt("codigoCor"));
		consultarPlacaDadosRetorno.setCor(jsonData.getString("cor"));
		consultarPlacaDadosRetorno.setCodigoCategoria(jsonData.getInt("codigoCategoria"));
		consultarPlacaDadosRetorno.setCategoriaVeiculo(jsonData.getString("categoriaVeiculo"));
		consultarPlacaDadosRetorno.setCodigoEspecie(jsonData.getInt("codigoEspecie"));
		consultarPlacaDadosRetorno.setEspecie(jsonData.getString("especie"));
		consultarPlacaDadosRetorno.setCodigoTipVeic(jsonData.getInt("codigoTipVeic"));
		consultarPlacaDadosRetorno.setTipoVeiculo(jsonData.getString("tipoVeiculo"));
		consultarPlacaDadosRetorno.setAnoFabricacao(jsonData.getInt("anoFabricacao"));
		consultarPlacaDadosRetorno.setAnoModelo(jsonData.getInt("anoModelo"));
		consultarPlacaDadosRetorno.setPotencia(jsonData.getInt("potencia"));
		consultarPlacaDadosRetorno.setCilindrada(jsonData.getInt("cilindrada"));
		consultarPlacaDadosRetorno.setCodigoUsuario(jsonData.getInt("codigoCombustive"));
		consultarPlacaDadosRetorno.setCombustivel(jsonData.getString("combustivel"));
		consultarPlacaDadosRetorno.setNumeroMotor(jsonData.getString("numeroMotor"));
		consultarPlacaDadosRetorno.setTracaoMax(jsonData.getDouble("tracaoMax"));
		consultarPlacaDadosRetorno.setPesoBrutoTotal(jsonData.getDouble("pesoBrutoTotal"));
		consultarPlacaDadosRetorno.setAnoLicenciamento(jsonData.getInt("anoLicenciamento"));
		consultarPlacaDadosRetorno.setCapacidadeCarga(jsonData.getDouble("capacidadeCarga"));
		consultarPlacaDadosRetorno.setProcedencia(jsonData.getString("procedencia"));
		consultarPlacaDadosRetorno.setCapacidadePassag(jsonData.getInt("capacidadePassag"));
		consultarPlacaDadosRetorno.setNumeroEixos(jsonData.getInt("numeroEixos"));
		consultarPlacaDadosRetorno.setRestricao1(jsonData.getString("restricao1"));
		consultarPlacaDadosRetorno.setRestricao2(jsonData.getString("restricao2"));
		consultarPlacaDadosRetorno.setRestricao3(jsonData.getString("restricao3"));
		consultarPlacaDadosRetorno.setRestricao4(jsonData.getString("restricao4"));
		consultarPlacaDadosRetorno.setRestricao5(jsonData.getString("restricao5"));
		consultarPlacaDadosRetorno.setRestricao6(jsonData.getString("restricao6"));
		consultarPlacaDadosRetorno.setTipoDocumento(jsonData.getInt("tipoDocumento"));
		consultarPlacaDadosRetorno.setNumeroCpfCgc(jsonData.getString("numeroCpfCgc"));
		consultarPlacaDadosRetorno.setNomeProprietario(jsonData.getString("nomeProprietario"));
		consultarPlacaDadosRetorno.setEndereco(jsonData.getString("endereco"));
		consultarPlacaDadosRetorno.setNumeroEndereco(jsonData.getString("numeroEndereco"));
		consultarPlacaDadosRetorno.setComplemEndereco(jsonData.getString("complemEndereco"));
		consultarPlacaDadosRetorno.setNomeBairro(jsonData.getString("nomeBairro"));
		consultarPlacaDadosRetorno.setNumeroCep(jsonData.getString("numeroCep"));
		consultarPlacaDadosRetorno.setNumeroTelefone(jsonData.getString("numeroTelefone"));
		consultarPlacaDadosRetorno.setRegravChassi(jsonData.getString("regravChassi"));
		//consultarPlacaDadosRetorno.setDataAtualizacao(jsonData.getString("dataAtualizacao"));
		consultarPlacaDadosRetorno.setValorDebIpva(jsonData.getDouble("valorDebIpva"));
		consultarPlacaDadosRetorno.setValorDebLicenc(jsonData.getDouble("valorDebLicenc"));
		consultarPlacaDadosRetorno.setValorDebMulta(jsonData.getDouble("valorDebMulta"));
		consultarPlacaDadosRetorno.setValorDebDpvat(jsonData.getDouble("valorDebDpvat"));
		consultarPlacaDadosRetorno.setValorDebInfTrami(jsonData.getDouble("valorDebInfTrami"));
		
		
		return consultarPlacaDadosRetorno;
		
	}
	
	private static JSONObject getData(SOAPMessage response) throws IOException, SOAPException, Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		response.writeTo(baos);
	    String content = baos.toString();
	    Result   ret = XmlServices.loadXML(content, true);
		Document document = (Document)ret.getObjects().get("doc");
		NodeList nodeList = document.getElementsByTagName("consultaVeiculoDadosProprietResult");
	    Map<String, Object> data = new HashMap<String, Object>(); 
		for (int i = 0; i < nodeList.getLength(); i++) {
	    	Node n = nodeList.item(i);
	    	NodeList childNodes = n.getChildNodes();
    		for (int j = 0; j < childNodes.getLength(); j++) {
    			Node node = childNodes.item(j);
				Node childNode = node.getFirstChild();
				String value = "";
				if(childNode != null)
					value = childNode.getNodeValue();
				if(node.getNodeName().equals("codigoRetExec")) {
					if(!value.equals("0")) {
						throw new Exception("Erro na consulta do Detran da Bahia");
					}
					try {
						data.put("codigoRetExec", Integer.parseInt(value));
					} catch (NumberFormatException nfe) {
						data.put("codigoRetExec", value);
					}
				}
				else
					data.put(node.getNodeName(), value);
 			}
	    }
		JSONObject json =  new JSONObject(data);
		return json;
	}
}
