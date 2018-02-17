package tokenmanagement.extension;

import java.util.ArrayList;
import java.util.List;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * @author <a href="mailto:tcerar@redhat.com">Tomaz Cerar</a>
 */
public class SubsystemDefinition extends SimpleResourceDefinition {
	
    public static final SubsystemDefinition INSTANCE = new SubsystemDefinition();
    
    private SubsystemDefinition() {
    	super(SubsystemExtension.SUBSYSTEM_PATH,
                SubsystemExtension.getResourceDescriptionResolver(null),
                //We always need to add an 'add' operation
                SubsystemAdd.INSTANCE,
                //Every resource that is added, normally needs a remove operation
                SubsystemRemove.INSTANCE);
    }
    
    static final SimpleAttributeDefinition WEB_CONTEXT =
            new SimpleAttributeDefinitionBuilder("web-context", ModelType.STRING, true)
                .setAllowExpression(true)
                .setDefaultValue(new ModelNode("banan"))
                .setRestartAllServices()
                .build();

    @Override
    public void registerOperations(ManagementResourceRegistration resourceRegistration) {
        super.registerOperations(resourceRegistration);
        //you can register aditional operations here
    }

    static final List<AttributeDefinition> ALL_ATTRIBUTES = new ArrayList<AttributeDefinition>();

    static {
        ALL_ATTRIBUTES.add(WEB_CONTEXT);
//        ALL_ATTRIBUTES.add(PROVIDERS);
    }
    
    private static SubsystemWriteAttributeHandler attrHandler = new SubsystemWriteAttributeHandler(ALL_ATTRIBUTES);
    
    @Override
    public void registerAttributes(ManagementResourceRegistration resourceRegistration) {
        //you can register attributes here
    	   super.registerAttributes(resourceRegistration);
           for (AttributeDefinition attrDef : ALL_ATTRIBUTES) {
               resourceRegistration.registerReadWriteAttribute(attrDef, null, attrHandler);
           }
    }
}
