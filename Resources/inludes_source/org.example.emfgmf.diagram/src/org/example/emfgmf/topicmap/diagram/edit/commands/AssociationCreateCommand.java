package org.example.emfgmf.topicmap.diagram.edit.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.example.emfgmf.topicmap.Association;
import org.example.emfgmf.topicmap.Topic;
import org.example.emfgmf.topicmap.TopicMap;
import org.example.emfgmf.topicmap.TopicmapFactory;
import org.example.emfgmf.topicmap.TopicmapPackage;
import org.example.emfgmf.topicmap.diagram.edit.policies.TopicMapBaseItemSemanticEditPolicy;

/**
 * @generated
 */
public class AssociationCreateCommand extends CreateElementCommand {

	/**
	 * @generated
	 */
	private final EObject source;

	/**
	 * @generated
	 */
	private final EObject target;

	/**
	 * @generated
	 */
	private TopicMap container;

	/**
	 * @generated
	 */
	public AssociationCreateCommand(CreateRelationshipRequest request,
			EObject source, EObject target) {
		super(request);
		this.source = source;
		this.target = target;
		if (request.getContainmentFeature() == null) {
			setContainmentFeature(TopicmapPackage.eINSTANCE
					.getTopicMap_Associations());
		}

		// Find container element for the new link.
		// Climb up by containment hierarchy starting from the source
		// and return the first element that is instance of the container class.
		for (EObject element = source; element != null; element = element
				.eContainer()) {
			if (element instanceof TopicMap) {
				container = (TopicMap) element;
				super.setElementToEdit(container);
				break;
			}
		}
	}

	/**
	 * @generated
	 */
	public boolean canExecute() {
		if (source == null && target == null) {
			return false;
		}
		if (source != null && !(source instanceof Topic)) {
			return false;
		}
		if (target != null && !(target instanceof Topic)) {
			return false;
		}
		if (getSource() == null) {
			return true; // link creation is in progress; source is not defined yet
		}
		// target may be null here but it's possible to check constraint
		if (getContainer() == null) {
			return false;
		}
		return TopicMapBaseItemSemanticEditPolicy.LinkConstraints
				.canCreateAssociation_3001(getContainer(), getSource(),
						getTarget());
	}

	/**
	 * @generated
	 */
	protected EObject doDefaultElementCreation() {
		// org.example.emfgmf.topicmap.Association newElement = (org.example.emfgmf.topicmap.Association) super.doDefaultElementCreation();
		Association newElement = TopicmapFactory.eINSTANCE.createAssociation();
		getContainer().getAssociations().add(newElement);
		newElement.setLeft(getSource());
		newElement.setRight(getTarget());
		return newElement;
	}

	/**
	 * @generated
	 */
	protected EClass getEClassToEdit() {
		return TopicmapPackage.eINSTANCE.getTopicMap();
	}

	/**
	 * @generated
	 */
	protected CommandResult doExecuteWithResult(IProgressMonitor monitor,
			IAdaptable info) throws ExecutionException {
		if (!canExecute()) {
			throw new ExecutionException(
					"Invalid arguments in create link command"); //$NON-NLS-1$
		}
		return super.doExecuteWithResult(monitor, info);
	}

	/**
	 * @generated
	 */
	protected ConfigureRequest createConfigureRequest() {
		ConfigureRequest request = super.createConfigureRequest();
		request.setParameter(CreateRelationshipRequest.SOURCE, getSource());
		request.setParameter(CreateRelationshipRequest.TARGET, getTarget());
		return request;
	}

	/**
	 * @generated
	 */
	protected void setElementToEdit(EObject element) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @generated
	 */
	protected Topic getSource() {
		return (Topic) source;
	}

	/**
	 * @generated
	 */
	protected Topic getTarget() {
		return (Topic) target;
	}

	/**
	 * @generated
	 */
	public TopicMap getContainer() {
		return container;
	}
}
