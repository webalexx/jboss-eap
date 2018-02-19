package tokenmanagement.extension;

import org.jboss.as.controller.AbstractBoottimeAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.server.AbstractDeploymentChainStep;
import org.jboss.as.server.DeploymentProcessorTarget;
import org.jboss.dmr.ModelNode;

import tokenmanagement.deployment.SubsystemDeploymentProcessor;

/**
 * Handler responsible for adding the subsystem resource to the model
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
class SubsystemAdd extends AbstractBoottimeAddStepHandler {

    static final SubsystemAdd INSTANCE = new SubsystemAdd();

    private SubsystemAdd() {
    	
    	System.out.println("----- SubsystemAdd --------");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void populateModel(ModelNode operation, ModelNode model) throws OperationFailedException {
    	System.out.println("---------populateModel-----------");
    	
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performBoottime(OperationContext context, ModelNode operation, ModelNode model)
            throws OperationFailedException {
    	
        //Add deployment processors here
        //Remove this if you don't need to hook into the deployers, or you can add as many as you like
        //see SubDeploymentProcessor for explanation of the phases
    	
//        context.addStep(new AbstractDeploymentChainStep() {
//            @Override
//            protected void execute(DeploymentProcessorTarget processorTarget) {
//                processorTarget.addDeploymentProcessor(SubsystemExtension.SUBSYSTEM_NAME, Phase.DEPENDENCIES, 0, new ProviderDependencyProcessor());
//                processorTarget.addDeploymentProcessor(SubsystemExtension.SUBSYSTEM_NAME,
//                        Phase.POST_MODULE, // PHASE
//                        Phase.POST_MODULE_VALIDATOR_FACTORY - 2, // PRIORITY
//                        new ProviderDeploymentProcessor());
//                processorTarget.addDeploymentProcessor(SubsystemExtension.SUBSYSTEM_NAME,
//                        Phase.POST_MODULE, // PHASE
//                        Phase.POST_MODULE_VALIDATOR_FACTORY - 1, // PRIORITY
//                        new ServerDeploymentProcessor());
//            }
//        }, OperationContext.Stage.RUNTIME);
//        context.addStep(new AbstractDeploymentChainStep() {
//            @Override
//            protected void execute(DeploymentProcessorTarget processorTarget) {
//            }
//        }, OperationContext.Stage.RUNTIME);

        context.addStep(new AbstractDeploymentChainStep() {
            public void execute(DeploymentProcessorTarget processorTarget) {
                processorTarget.addDeploymentProcessor(SubsystemExtension.SUBSYSTEM_NAME, SubsystemDeploymentProcessor.PHASE, SubsystemDeploymentProcessor.PRIORITY, new SubsystemDeploymentProcessor());
            }
        }, OperationContext.Stage.RUNTIME);

    }
}
