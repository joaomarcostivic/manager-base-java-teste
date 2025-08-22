package com.tivic.manager.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TimerTask;

import sol.util.Util;

public class Task extends TimerTask{
	String className;
	String methodCall;
	GregorianCalendar dtTask;
	
	private HashMap<String,Object> objectList = new HashMap<String,Object>();
	
	@Override
	public String toString() {
		return "Task [className=" + className + ", methodCall=" + methodCall
				+ ", dtTask=" + dtTask + "]";
	}

	public Task(String className, String methodCall, GregorianCalendar dtTask) {
		super();
		this.className = className;
		this.methodCall = methodCall;
		this.dtTask = dtTask;
	}

	public void run() {
		try	{
			callMethod(className, methodCall);
		}
  		catch(Exception e)	{
  			System.out.println("ERROR: Task: "+className+"."+methodCall+"\n"+e);
			e.printStackTrace(System.out);
  		}
    }
	
	@SuppressWarnings("unused")
	private void callMethod(String className, String methodCall)	{
		try	{
    		// Classes e Métodos
			Object objectClass = null;
			Method method = null;
			objectClass = Class.forName(className).newInstance();
			String methodName = methodCall.substring(0, methodCall.indexOf("(")).trim();
			
			// Criando a lista de Classes dos Parametros do Método
			String listParam  = methodCall.substring(methodCall.indexOf("(")+1, methodCall.lastIndexOf(")")).trim();
			int index = -1;
			int objectCount = 0;
			do	{
				index = listParam.indexOf("new ", index+1);
				if (index>=0)
					objectCount++;
			}while(index>=0);
			//System.out.println("Objetos: " + objectCount);
			String[][] objetos = new String[objectCount][3];
			String parametros = "";
			StringTokenizer tokens = new StringTokenizer(listParam, ",()", true);
			int p = -1;
			objectCount = 0;
			ArrayList<Integer> idsOfObjectsPendentes = new ArrayList<Integer>();
			int lastOcorrenciaNew = -1;
			String tokenTemp = "";
			while(tokens.hasMoreTokens() || !tokenTemp.equals(""))	{
				String token = tokenTemp.equals("") ? tokens.nextToken().trim() : tokenTemp;
				tokenTemp = "";
				if(token.equals(",") || token.equals("") || token.equals("(") || token.trim().charAt(0)==':')
					continue;
				if(token.indexOf("const")>=0)	{
					while(tokens.hasMoreTokens())	{
						String temp = tokens.nextToken();
						if(!temp.trim().equals(",") && !temp.trim().equals(")"))
							token += temp;
						if(temp.trim().equals(")"))
							tokenTemp = temp;
						if(temp.indexOf(":")>=0 || temp.trim().equals(",") || temp.trim().equals(")"))
							break;
					}
				}
				if(token.indexOf("new")>=0)	{
					StringTokenizer t = new StringTokenizer(token, " ");
					lastOcorrenciaNew++;
					//System.out.println("Token: [" + token + "]");
					if(t.hasMoreTokens())
						t.nextToken().trim();
					String nmClasse = "";
					String nmClasseParametros = "";
					if(t.hasMoreTokens()) {
						nmClasse = t.nextToken().trim();
						nmClasseParametros = nmClasse;
						int lastOcorrenciaNewTemp = -1;
						String listParamTemp = listParam;
						while (lastOcorrenciaNewTemp < lastOcorrenciaNew) {
							listParamTemp = listParamTemp.substring(listParamTemp.indexOf("new") + 3).trim();
							lastOcorrenciaNewTemp++;
						}
						listParamTemp = listParamTemp.substring(1);
						if (listParamTemp.indexOf('(')!=-1)
							listParamTemp = listParamTemp.substring(listParamTemp.indexOf('(') + 1);
						int nrAbreParenteses = 1;
						int index1 = -1;
						int index2 = -1;
						while (nrAbreParenteses>0) {
							index1 = listParamTemp.indexOf('(');
							index2 = listParamTemp.indexOf(')');
							if (index1!=-1 && index1<index2) {
								nrAbreParenteses++;
								listParamTemp = listParamTemp.substring(index1+1);
							}
							else {
								nrAbreParenteses--;
								listParamTemp = listParamTemp.substring(index2+1);
							}
						}
						if(listParamTemp.length()>0)
							listParamTemp = listParamTemp.trim().substring(1);
						StringTokenizer tokenTemps = new StringTokenizer(listParamTemp, ",()", true);
						if (tokenTemps.hasMoreTokens())
							nmClasseParametros = tokenTemps.nextToken().trim();
					}
					if (idsOfObjectsPendentes.size()>0) {
						int i = ((Integer)idsOfObjectsPendentes.get(idsOfObjectsPendentes.size()-1)).intValue();
						objetos[i][1] += (objetos[i][1]=="" ? "" : ", ") + "@object" + (objectCount+1) + ":" + nmClasse;
					}
					else 
						parametros += (parametros.equals("") ? "" : ", ")+ "@object" + (objectCount+1) + ":" + nmClasseParametros;
					p++;
					idsOfObjectsPendentes.add(new Integer(objectCount));
					objetos[objectCount][0]  = nmClasse;
					objetos[objectCount][1]  = "";
					objetos[objectCount][2]  = "@object"+(objectCount+1);
					objectCount++;
					continue;
				}
				else if (idsOfObjectsPendentes.size()>0) {
					int i = ((Integer)idsOfObjectsPendentes.get(idsOfObjectsPendentes.size()-1)).intValue();
					//System.out.println("Reduz Token para " + i + ": [" + token + "]");
					if (!token.trim().equals(")"))		
						objetos[i][1] += (objetos[i][1].equals("")?" ":", ")+token;						
				}
				else
					parametros += (parametros.equals("") ? "" : ", ") + token;
				if(token.trim().equals(")"))	{
					p--;
					if (idsOfObjectsPendentes.size()>0) {
						int i = ((Integer)idsOfObjectsPendentes.get(idsOfObjectsPendentes.size()-1)).intValue();
						//System.out.println("Instanciando " + objetos[i][0] + " com " + objetos[i][1] + " e nome " + objetos[i][2]);
						//objectList.put(objetos[i][2], getObject(objetos[i][0], objetos[i][1]));
						objectList.put(objetos[i][2], objetos[i]);
						idsOfObjectsPendentes.remove(idsOfObjectsPendentes.size()-1);
					}
					/*if(objectCount>=0)	{
						objectList.put(objetos[objectCount-1][2], objetos[objectCount-1]);
						endObject=true;
					}*/
					continue;
				}
				/*if(p>=0)	{
					objetos[objectCount-1][1] += (objetos[objectCount-1][1].equals("")?"":",")+token;
					continue;
				}*/
			}
			Object[] paramArgs = getParamList(parametros);
			method = ((Class<?>)Class.forName(className)).getDeclaredMethod(methodName, (Class[])paramArgs[0]);
			Object objectReturn = method.invoke(objectClass, (Object[])paramArgs[1]);
  		}
  		catch(Exception e)	{
  			System.out.println("ERRO ao tentar executar: "+className+"."+methodCall+"\n"+e);
			e.printStackTrace(System.out);
  		}
	}
	
	private Object[] getParamList(String listParams)	{
		try	{
			StringTokenizer tokens = new StringTokenizer(listParams, ",");
			ArrayList<String[]> parametros = new ArrayList<String[]>();
			while(tokens.hasMoreTokens())	{
				String token = tokens.nextToken().trim();
				if(token.lastIndexOf(":")<0)
					continue;
				String nmCampo  = token.substring(0, token.lastIndexOf(":")).trim();
				String nmClasse = token.substring(token.lastIndexOf(":") + 1).trim();
				parametros.add(new String[] {nmCampo, nmClasse});
			}
			// Criando a lista de argumentos
			Class<?>[] params = new Class[parametros.size()];
			Object[] args = new Object[parametros.size()];
			for(int i=0; i<parametros.size(); i++)	{
				String nmCampo  = ((String[])parametros.get(i))[0];
				String nmClasse = ((String[])parametros.get(i))[1];
				int nrDimensoesOfArray = 0;
				if (nmClasse.indexOf('[') != -1) {
					int indexOfInicial = nmClasse.indexOf('[');
					for (int l=indexOfInicial; l<nmClasse.length(); l+=2) {
						if (!(l<nmClasse.length() - 1 && nmClasse.charAt(l)=='[' && nmClasse.charAt(l+1)==']'))
							break;
						else
							nrDimensoesOfArray++;
					}
					nmClasse = nmClasse.substring(0, nmClasse.indexOf('['));
				}
				// Substitui alias para classes comuns
				if(nmClasse.equals("GregorianCalendar")&&!nmClasse.equals("java.util.GregorianCalendar"))
					nmClasse="java.util.GregorianCalendar";
				if(nmClasse.equals("ArrayList")&&!nmClasse.equals("java.util.ArrayList"))
					nmClasse="java.util.ArrayList";
				if(nmClasse.equals("Object")&&!nmClasse.equals("java.lang.Object"))
					nmClasse="java.lang.Object";
				if(nmClasse.equals("String")&&!nmClasse.equals("java.lang.String"))
					nmClasse="java.lang.String";
				
				// Se for um OBJETO tenta instancia-lo
				if(nmCampo.indexOf("@object")>=0)	{
					args[i]   = getObject(((String[])objectList.get(nmCampo))[0], ((String[])objectList.get(nmCampo))[1]);
					params[i] = Class.forName(nmClasse);
				}
				// Se for um OBJETO INSTANCIADO acessa-o
				else if(nmCampo.indexOf("*")>=0)	{
					args[i] = objectList.get(nmCampo);
					if (nmClasse.equals("char"))
						params[i] = char.class;
					else if (nmClasse.equals("byte"))
						params[i] = byte.class;
					else if (nmClasse.equals("short"))
						params[i] = short.class;
					else if (nmClasse.equals("int"))
						params[i] = int.class;
					else if (nmClasse.equals("long"))
						params[i] = long.class;
					else if (nmClasse.equals("boolean"))
						params[i] = boolean.class;
					else if (nmClasse.equals("float"))
						params[i] = float.class;
					else if (nmClasse.equals("double"))
						params[i] = double.class;
					else if (nmClasse.equals("GregorianCalendar"))
						params[i] = GregorianCalendar.class;
					else if (nmClasse.equals("String"))
						params[i] = String.class;
					else
						params[i] = Class.forName(nmClasse);
				}
				// Se for do tipo CHAR
				else if(nmClasse.equals("char"))	{
					try	{ // Sendo uma constante atribui ao valor
						args[i] = nmCampo.indexOf("const")>=0 ? nmCampo.replaceAll("const", "").trim():'\u0000';
					}
					catch(Exception e)	{
						args[i] = '\u0000';
					}
					params[i] = char.class;
				}
				// Se for do tipo BYTE
				else if(nmClasse.equals("byte"))	{
					try	{
						// Sendo uma constante atribui ao valor
						args[i] = nmCampo.indexOf("const")>=0 ? nmCampo.replaceAll("const", "").getBytes():null;
					}
					catch(Exception e)	{
						args[i] = null;
					}
					params[i] = byte.class;
				}	// Se for do tipo BYTE
				else if(nmClasse.equals("short"))	{
					try	{ // Sendo uma constante atribui ao valor
						args[i] = nmCampo.indexOf("const")>=0 ? new Short(nmCampo.replaceAll("const", "").trim()):new Short("0");
					}
					catch(Exception e)	{
						args[i] = new Integer(0);
					}
					params[i] = short.class;
				}	// Se for do tipo INT
				else if(nmClasse.equals("int"))	{
					try	{ // Sendo uma constante atribui ao valor
						args[i] = nmCampo.indexOf("const")>=0 ? new Integer(nmCampo.replaceAll("const", "").trim()):new Integer(0);
					}
					catch(Exception e)	{
						args[i] = new Integer(0);
					}
					params[i] = int.class;
				}	// Se for do tipo LONG
				else if(nmClasse.equals("long"))	{
					try	{ // Sendo uma constante atribui ao valor
						args[i] = nmCampo.indexOf("const")>=0 ? new Long(nmCampo.replaceAll("const", "").trim()):new Long(0);
					}
					catch(Exception e)	{
						args[i] = new Long(0);
					}
					params[i] = long.class;
				}	// Se for FLOAT
				else if(nmClasse.equals("float"))	{
					try	{
						// Sendo uma constante atribui ao valor
						args[i] = nmCampo.indexOf("const")>=0 ? new Float(nmCampo.replaceAll("const", "").trim()):new Float(0);
					}
					catch(Exception e)	{
						args[i] = new Float(0);
					}
					params[i] = float.class;
				}	// Se for DOUBLE
				else if(nmClasse.equals("double"))	{
					try	{
						// Sendo uma constante atribui ao valor
						args[i] = nmCampo.indexOf("const")>=0 ? new Double(nmCampo.replaceAll("const", "").trim()):new Double(0);
					}
					catch(Exception e)	{
						args[i] = new Double(0);
					}
					params[i] = double.class;
				}	// Se for booleano
				else if(nmClasse.equals("boolean"))	{
					try	{
						// Sendo uma constante atribui ao valor
						args[i] = nmCampo.indexOf("const")>=0 ? new Boolean(nmCampo.replaceAll("const", "").trim().equals("true")):new Boolean(false);
					}
					catch(Exception e)	{
						args[i] = new Boolean(false);
					}
					params[i] = boolean.class;

				}
				// Se for GregorianCalendar
				else if(nmClasse.indexOf("GregorianCalendar")>=0)	{
					try	{
						// Sendo uma constante atribui ao valor
						args[i] = nmCampo.indexOf("const")>=0 ? Util.stringToCalendar(nmCampo.replaceAll("const", "").trim()):null;
					}
					catch(Exception e)	{
						args[i] = null;
					}
					params[i] = Class.forName(nmClasse);
				}
				else	{
					args[i] = nmCampo.indexOf("const")>=0 ? nmCampo.replaceAll("const", "").trim():"";
					params[i] = Class.forName(nmClasse);
				}
				if (nrDimensoesOfArray > 0) {
					int[] dimensoes = new int[nrDimensoesOfArray];
					for (int l=0; l<nrDimensoesOfArray; l++)
						dimensoes[l] = 1;
					params[i] = Array.newInstance(params[i], dimensoes).getClass();
				}
			}
			return new Object[] {params, args};
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("Task.getParamList "+e);
		}
		return new Object[] {null, null};
	}
	
	 private Object getObject(String className, String objectStream)	{
			try	{
				Object[] paramArgs = getParamList(objectStream);
				Constructor<?> construtor = Class.forName(className).getConstructor((Class[])paramArgs[0]);
				return construtor.newInstance((Object[])paramArgs[1]);
			}
			catch(Exception e){
				e.printStackTrace();
				System.out.print("Task.getObject "+e);
			}
			return null;
	  }
}
