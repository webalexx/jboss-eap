/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tokenmanagement.extension;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.PropertiesAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.controller.parsing.ParseUtils;
import org.jboss.as.controller.persistence.SubsystemMarshallingContext;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.staxmapper.XMLElementReader;
import org.jboss.staxmapper.XMLElementWriter;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import java.util.List;

import static tokenmanagement.extension.SubsystemExtension.PATH_SUBSYSTEM;
import static org.keycloak.subsystem.server.extension.KeycloakSubsystemDefinition.PROVIDERS;
import static tokenmanagement.extension.SubsystemDefinition.WEB_CONTEXT;

/**
 * The subsystem parser, which uses stax to read and write to and from xml
 */
class SubsystemParser implements XMLStreamConstants, XMLElementReader<List<ModelNode>>, XMLElementWriter<SubsystemMarshallingContext> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void readElement(final XMLExtendedStreamReader reader, final List<ModelNode> list) throws XMLStreamException {
        System.out.println("---------readElement-----------");
        ParseUtils.requireNoAttributes(reader);
        ModelNode addSubModel = Util.createAddOperation(PathAddress.pathAddress(PATH_SUBSYSTEM));
        list.add(addSubModel);
        
        while (reader.hasNext() && nextTag(reader) != END_ELEMENT) {
            if (reader.getLocalName().equals(WEB_CONTEXT.getXmlName())) {
                WEB_CONTEXT.parseAndSetParameter(reader.getElementText(), addSubModel, reader);
            } else {
                throw new XMLStreamException("Unknown tokenmanagement-server subsystem tag: " + reader.getLocalName());
            }
        }
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeContent(final XMLExtendedStreamWriter writer, final SubsystemMarshallingContext context) throws XMLStreamException {
    	System.out.println("--- writeContent ---");
        context.startSubsystemElement(SubsystemExtension.NAMESPACE, false);
        writeWebContext(writer, context);
        writeList(writer, context.getModelNode(), PROVIDERS, "provider");
        writer.writeEndElement();
    }
    
    
    private void writeWebContext(XMLExtendedStreamWriter writer, SubsystemMarshallingContext context) throws XMLStreamException {
        if (!context.getModelNode().get(WEB_CONTEXT.getName()).isDefined()) {
            return;
        }
        WEB_CONTEXT.marshallAsElement(context.getModelNode(), writer);
    }
    
    private void writeList(XMLExtendedStreamWriter writer, ModelNode context, AttributeDefinition def, String elementName) throws XMLStreamException {
        if (!context.get(def.getName()).isDefined()) {
            return;
        }
        writer.writeStartElement(def.getXmlName());
        ModelNode modules = context.get(def.getName());
        for (ModelNode module : modules.asList()) {
            writer.writeStartElement(elementName);
            writer.writeCharacters(module.asString());
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }
    
    // used for debugging
    private int nextTag(XMLExtendedStreamReader reader) throws XMLStreamException {
        return reader.nextTag();
    }
    
}