package org.example.emfgmf.topicmap.diagram.edit.parts;

import org.eclipse.draw2d.Connection;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel;
import org.eclipse.gmf.runtime.notation.View;
import org.example.emfgmf.topicmap.diagram.edit.policies.AssociationItemSemanticEditPolicy;

/**
 * @generated
 */
public class AssociationEditPart extends ConnectionNodeEditPart {

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 3001;

	/**
	 * @generated
	 */
	public AssociationEditPart(View view) {
		super(view);
	}

	/**
	 * @generated
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
				new AssociationItemSemanticEditPolicy());
	}

	/**
	 * @generated
	 */
	protected boolean addFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof AssociationLabelEditPart) {
			((AssociationLabelEditPart) childEditPart)
					.setLabel(getPrimaryShape()
							.getFigureAssociationLabelFigure());
			return true;
		}
		return false;
	}

	/**
	 * @generated
	 */
	protected void addChildVisual(EditPart childEditPart, int index) {
		if (addFixedChild(childEditPart)) {
			return;
		}
		super.addChildVisual(childEditPart, -1);
	}

	/**
	 * Creates figure for this edit part.
	 * 
	 * Body of this method does not depend on settings in generation model
	 * so you may safely remove <i>generated</i> tag and modify it.
	 * 
	 * @generated
	 */

	protected Connection createConnectionFigure() {
		return new AssociationFigure();
	}

	/**
	 * @generated
	 */
	public AssociationFigure getPrimaryShape() {
		return (AssociationFigure) getFigure();
	}

	/**
	 * @generated
	 */
	public class AssociationFigure extends PolylineConnectionEx {

		/**
		 * @generated
		 */
		private WrapLabel fFigureAssociationLabelFigure;

		/**
		 * @generated
		 */
		public AssociationFigure() {

			createContents();
		}

		/**
		 * @generated
		 */
		private void createContents() {

			fFigureAssociationLabelFigure = new WrapLabel();
			fFigureAssociationLabelFigure.setText("<...>");

			this.add(fFigureAssociationLabelFigure);

		}

		/**
		 * @generated
		 */
		public WrapLabel getFigureAssociationLabelFigure() {
			return fFigureAssociationLabelFigure;
		}

	}

}
