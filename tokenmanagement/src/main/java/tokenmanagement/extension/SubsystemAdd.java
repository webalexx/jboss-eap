package tokenmanagement.extension;

import static tokenmanagement.extension.SubsystemDefinition.ALL_ATTRIBUTES;
import static tokenmanagement.extension.SubsystemDefinition.WEB_CONTEXT;

import org.jboss.as.controller.AbstractBoottimeAddStepHandler;
import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.server.AbstractDeploymentChainStep;
import org.jboss.as.server.DeploymentProcessorTarget;
import org.jboss.as.server.deployment.Phase;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;

/**
 * Handler responsible for adding the subsystem resource to the model
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
class SubsystemAdd extends AbstractBoottimeAddStepHandler {

	Logger logger = Logger.getLogger(SubsystemAdd.class);
	static final SubsystemAdd INSTANCE = new SubsystemAdd();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void performBoottime(OperationContext context, ModelNode operation, ModelNode model)
			throws OperationFailedException {
		System.out.println("--- performBoottime ---");
		// Add deployment processors here
		// Remove this if you don't need to hook into the deployers, or you can add as
		// many as you like
		// see SubDeploymentProcessor for explanation of the phases

		context.addStep(new AbstractDeploymentChainStep() {
			@Override
			protected void execute(DeploymentProcessorTarget processorTarget) {
				processorTarget.addDeploymentProcessor(SubsystemExtension.SUBSYSTEM_NAME, Phase.DEPENDENCIES, 0,
						new ProviderDependencyProcessor());
				processorTarget.addDeploymentProcessor(SubsystemExtension.SUBSYSTEM_NAME, Phase.POST_MODULE, // PHASE
						Phase.POST_MODULE_VALIDATOR_FACTORY - 2, // PRIORITY
						new ProviderDeploymentProcessor());
				processorTarget.addDeploymentProcessor(SubsystemExtension.SUBSYSTEM_NAME, Phase.POST_MODULE, // PHASE
						Phase.POST_MODULE_VALIDATOR_FACTORY - 1, // PRIORITY
						new ServerDeploymentProcessor());
			}
		}, OperationContext.Stage.RUNTIME);
		context.addStep(new AbstractDeploymentChainStep() {
			@Override
			protected void execute(DeploymentProcessorTarget processorTarget) {
			}
		}, OperationContext.Stage.RUNTIME);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void populateModel(final OperationContext context, final ModelNode operation, final Resource resource)
			throws OperationFailedException {
		logger.info("---- populateModel  -----");
		ModelNode model = resource.getModel();

		// set attribute values from parsed model
		for (AttributeDefinition attrDef : ALL_ATTRIBUTES) {
			attrDef.validateAndSet(operation, model);
		}

		// returns early if on domain controller
		if (!requiresRuntime(context)) {
			return;
		}

		// don't want to try to start server on host controller
		if (!context.isNormalServer()) {
			return;
		}

		ModelNode webContextNode = resource.getModel().get(WEB_CONTEXT.getName());
		if (!webContextNode.isDefined()) {
			webContextNode = WEB_CONTEXT.getDefaultValue();
		}
		String webContext = webContextNode.asString();

		ServerUtil serverUtil = new ServerUtil(operation);
		serverUtil.addStepToUploadServerWar(context);
		AdapterConfigService.INSTANCE.setWebContext(webContext);

		AdapterConfigService.INSTANCE.updateConfig(operation, model);
	}
}
