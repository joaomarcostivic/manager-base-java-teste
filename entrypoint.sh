#!/bin/bash

# Renomear o arquivo WAR de acordo com o contexto da aplicação
if [ "$APP_CONTEXT" == "ROOT" ]; then
  # Se o contexto for ROOT, não precisa renomear
  mv /usr/local/tomcat/webapps/manager-base-java.war /usr/local/tomcat/webapps/ROOT.war
else
  # Se for outro contexto, renomeia de acordo
  mv /usr/local/tomcat/webapps/manager-base-java.war /usr/local/tomcat/webapps/${APP_CONTEXT}.war
  echo "Aplicação configurada para rodar no contexto: /${APP_CONTEXT}"
fi

# Iniciar o Tomcat
exec catalina.sh run
