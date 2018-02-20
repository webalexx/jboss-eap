package tokenmanagement.extension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.operations.common.GenericSubsystemDescribeHandler;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * @author <a href="mailto:tcerar@redhat.com">Tomaz Cerar</a>
 */
public class SubsystemDefinition extends SimpleResourceDefinition {
	
    public static final SubsystemDefinition INSTANCE = new SubsystemDefinition();
    
    private SubsystemDefinition() {
    	super(SubsystemExtension.PATH_SUBSYSTEM,
                SubsystemExtension.getResourceDescriptionResolver(null),
                //We always need to add an 'add' operation
                SubsystemAdd.INSTANCE,
                //Every resource that is added, normally needs a remove operation
                SubsystemRemove.INSTANCE);
    }
    
    static final SimpleAttributeDefinition WEB_CONTEXT =
            new SimpleAttributeDefinitionBuilder("web-context", ModelType.STRING, true)
                .setAllowExpression(true)
                .setDefaultValue(new ModelNode("pseudo"))
                .setRestartAllServices()
                .build();

    @Override
    public void registerOperations(ManagementResourceRegistration resourceRegistration) {
        super.registerOperations(resourceRegistration);
        System.out.println("---------registerOperations-----------");
        resourceRegistration.registerOperationHandler(GenericSubsystemDescribeHandler.DEFINITION, GenericSubsystemDescribeHandler.INSTANCE);
    }

    static final List<AttributeDefinition> ALL_ATTRIBUTES = new ArrayList<AttributeDefinition>();
    static {
        ALL_ATTRIBUTES.add(WEB_CONTEXT);
    }
    
    private static final Map<String, AttributeDefinition> DEFINITION_LOOKUP = new HashMap<String, AttributeDefinition>();
    static {
        for (AttributeDefinition def : ALL_ATTRIBUTES) {
            DEFINITION_LOOKUP.put(def.getXmlName(), def);
        }
    }
    
    private static SubsystemWriteAttributeHandler attrHandler = new SubsystemWriteAttributeHandler(ALL_ATTRIBUTES);
    
    @Override
    public void registerAttributes(ManagementResourceRegistration resourceRegistration) {
    	System.out.println("---------registerAttributes-----------");
    	   super.registerAttributes(resourceRegistration);
           for (AttributeDefinition attrDef : ALL_ATTRIBUTES) {
               resourceRegistration.registerReadWriteAttribute(attrDef, null, attrHandler);
           }
    }
    
    public static AttributeDefinition lookup(String name) {
        return DEFINITION_LOOKUP.get(name);
    }
}
