@echo off

mvn -v

mvn archetype:generate -DarchetypeArtifactId=wildfly-subsystem -DarchetypeGroupId=org.wildfly.archetypes -DarchetypeVersion=8.0.0.Final -DarchetypeRepository=http://repository.jboss.org/nexus/content/groups/public

mvn archetype:generate -DarchetypeArtifactId=jboss-as-subsystem -DarchetypeGroupId=org.jboss.as.archetypes -DarchetypeVersion=7.1.1.Final -DarchetypeRepository=http://repository.jboss.org/nexus/content/groups/public