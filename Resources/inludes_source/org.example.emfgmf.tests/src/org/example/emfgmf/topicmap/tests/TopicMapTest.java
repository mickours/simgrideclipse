/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.example.emfgmf.topicmap.tests;

import junit.framework.TestCase;

import junit.textui.TestRunner;

import org.example.emfgmf.topicmap.TopicMap;
import org.example.emfgmf.topicmap.TopicmapFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Topic Map</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class TopicMapTest extends TestCase {

	/**
	 * The fixture for this Topic Map test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TopicMap fixture = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(TopicMapTest.class);
	}

	/**
	 * Constructs a new Topic Map test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TopicMapTest(String name) {
		super(name);
	}

	/**
	 * Sets the fixture for this Topic Map test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void setFixture(TopicMap fixture) {
		this.fixture = fixture;
	}

	/**
	 * Returns the fixture for this Topic Map test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TopicMap getFixture() {
		return fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(TopicmapFactory.eINSTANCE.createTopicMap());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#tearDown()
	 * @generated
	 */
	@Override
	protected void tearDown() throws Exception {
		setFixture(null);
	}

} //TopicMapTest
