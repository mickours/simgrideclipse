package org.example.emfgmf.topicmap.diagram.edit.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.commands.EditElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;
import org.example.emfgmf.topicmap.Association;
import org.example.emfgmf.topicmap.Topic;
import org.example.emfgmf.topicmap.TopicMap;
import org.example.emfgmf.topicmap.diagram.edit.policies.TopicMapBaseItemSemanticEditPolicy;

/**
 * @generated
 */
public class AssociationReorientCommand extends EditElementCommand {

	/**
	 * @generated
	 */
	private final int reorientDirection;

	/**
	 * @generated
	 */
	private final EObject oldEnd;

	/**
	 * @generated
	 */
	private final EObject newEnd;

	/**
	 * @generated
	 */
	public AssociationReorientCommand(ReorientRelationshipRequest request) {
		super(request.getLabel(), request.getRelationship(), request);
		reorientDirection = request.getDirection();
		oldEnd = request.getOldRelationshipEnd();
		newEnd = request.getNewRelationshipEnd();
	}

	/**
	 * @generated
	 */
	public boolean canExecute() {
		if (!(getElementToEdit() instanceof Association)) {
			return false;
		}
		if (reorientDirection == ReorientRelationshipRequest.REORIENT_SOURCE) {
			return canReorientSource();
		}
		if (reorientDirection == ReorientRelationshipRequest.REORIENT_TARGET) {
			return canReorientTarget();
		}
		return false;
	}

	/**
	 * @generated
	 */
	protected boolean canReorientSource() {
		if (!(oldEnd instanceof Topic && newEnd instanceof Topic)) {
			return false;
		}
		Topic target = getLink().getRight();
		if (!(getLink().eContainer() instanceof TopicMap)) {
			return false;
		}
		TopicMap container = (TopicMap) getLink().eContainer();
		return TopicMapBaseItemSemanticEditPolicy.LinkConstraints
				.canExistAssociation_3001(container, getNewSource(), target);
	}

	/**
	 * @generated
	 */
	protected boolean canReorientTarget() {
		if (!(oldEnd instanceof Topic && newEnd instanceof Topic)) {
			return false;
		}
		Topic source = getLink().getLeft();
		if (!(getLink().eContainer() instanceof TopicMap)) {
			return false;
		}
		TopicMap container = (TopicMap) getLink().eContainer();
		return TopicMapBaseItemSemanticEditPolicy.LinkConstraints
				.canExistAssociation_3001(container, source, getNewTarget());
	}

	/**
	 * @generated
	 */
	protected CommandResult doExecuteWithResult(IProgressMonitor monitor,
			IAdaptable info) throws ExecutionException {
		if (!canExecute()) {
			throw new ExecutionException(
					"Invalid arguments in reorient link command"); //$NON-NLS-1$
		}
		if (reorientDirection == ReorientRelationshipRequest.REORIENT_SOURCE) {
			return reorientSource();
		}
		if (reorientDirection == ReorientRelationshipRequest.REORIENT_TARGET) {
			return reorientTarget();
		}
		throw new IllegalStateException();
	}

	/**
	 * @generated
	 */
	protected CommandResult reorientSource() throws ExecutionException {
		getLink().setLeft(getNewSource());
		return CommandResult.newOKCommandResult(getLink());
	}

	/**
	 * @generated
	 */
	protected CommandResult reorientTarget() throws ExecutionException {
		getLink().setRight(getNewTarget());
		return CommandResult.newOKCommandResult(getLink());
	}

	/**
	 * @generated
	 */
	protected Association getLink() {
		return (Association) getElementToEdit();
	}

	/**
	 * @generated
	 */
	protected Topic getOldSource() {
		return (Topic) oldEnd;
	}

	/**
	 * @generated
	 */
	protected Topic getNewSource() {
		return (Topic) newEnd;
	}

	/**
	 * @generated
	 */
	protected Topic getOldTarget() {
		return (Topic) oldEnd;
	}

	/**
	 * @generated
	 */
	protected Topic getNewTarget() {
		return (Topic) newEnd;
	}
}
