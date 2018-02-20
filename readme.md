@echo off


# How to generate initial architype 

mvn -v
mvn archetype:generate -DarchetypeArtifactId=wildfly-subsystem -DarchetypeGroupId=org.wildfly.archetypes -DarchetypeVersion=8.0.0.Final -DarchetypeRepository=http://repository.jboss.org/nexus/content/groups/public

mvn archetype:generate -DarchetypeArtifactId=jboss-as-subsystem -DarchetypeGroupId=org.jboss.as.archetypes -DarchetypeVersion=7.1.1.Final -DarchetypeRepository=http://repository.jboss.org/nexus/content/groups/public


# Prepare Local-dev EAP Server 

## Add a Management User

bin/add-user.bat -u "admin" -p "admin123!" -g "guest,mgmtgroup"

## Stop/Start a Server

## Start JBoss Eap in a debug mode

### Variant 1

standalone.bat --debug 9797

### Variant 2

to add jvm_optons: -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,address=8787, server=y, suspend=n

# How to install Subsystem

## Links

http://docs.wildfly.org/Extending_WildFly.html#the-role-of-transformers

## Install Sample Keycloak on existing WildFly Server 

Download and unpack  keycloak-overlay-xxxx.Final.zip
 To start WildFly with Keycloak run
bin/standalone.sh --server-config=standalone-keycloak.xml
or if you are on Windows
bin/standalone.bat --server-config=standalone-keycloak.xml

## a Sample how to execute install command

./jboss-cli.[sh|bat] --file=keycloak-install.cli
./jboss-cli.sh --connect --file="adapter-install.cli"

# How to check TockenManagement Subsystem on Server

/subsystem=tokenmanagement:read-resource-description

# Run and test 

mvn install -Dmaven.test.skip=true

# Jboss-cli 

## Subsystema configuration

https://docs.jboss.org/author/display/WFLY10/Subsystem+configuration

help --commands

C:\Users\EH2KSEI\Dev\keycloak-demo-3.4.3.Final\keycloak\modules\system\layers\keycloak\org\keycloak

an example a files for automatic configuration are here :\Users\EH2KSEI\Dev\keycloak-343Final-Official\wildfly\server-subsystem\src\main\resources\subsystem-templates\keycloak-server.xml

## Sample how to read recources

/subsystem=datasources:read-resource(recursive=true)

## How view RESTEASY endpoints

/deployment=DEPLOYMENT_NAME/subsystem=jaxrs/restresource=
org.jboss.as.quickstarts.rshelloworld.HelloWorld:readresource(
include-runtime=true)


/deployment=webservices/subsystem=jaxrs/restresource=
org.jboss.as.quickstarts.rshelloworld.HelloWorld:readresource(
include-runtime=true)



## WebServices and EndPoints

/subsystem=webservices:read-resource

## Sample how to get res names
 
/subsystem=logging:read-operation-names

## Sample how to deploy recources 

deploy ~/sourcecontrol/temp/archetype-test/test2/target/test2.war

## Sample how to get db domain configuration

/profile=full/subsystem=datasources/data-source=ExampleDS:write-attribute(name=enabled,value=false)

# Hot to deploy das Anwendung  als Web 
Start point a Keycloak deployment shema 

## Install a extention

/extension=org.keycloak.keycloak-server-subsystem/:add(module=org.keycloak.keycloak-server-subsystem)

    Mod: org.keycloak.keycloak-server-subsystem.dependencies
    	<module name="org.keycloak.keycloak-services" 		export="true"/>
        <module name="org.jboss.msc"/>
        <module name="org.infinispan" export="true"/>
        
## Install subsystema 
/subsystem=keycloak-server:add(web-context=auth,master-realm-name=master,scheduled-task-interval=900)


