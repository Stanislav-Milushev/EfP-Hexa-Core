/**
 */
package metaFeatureModel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Group</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metaFeatureModel.Group#getGroupType <em>Group Type</em>}</li>
 *   <li>{@link metaFeatureModel.Group#getFeature <em>Feature</em>}</li>
 * </ul>
 *
 * @see metaFeatureModel.MetaFeatureModelPackage#getGroup()
 * @model
 * @generated
 */
public interface Group extends EObject {
	/**
	 * Returns the value of the '<em><b>Group Type</b></em>' attribute.
	 * The literals are from the enumeration {@link metaFeatureModel.GroupType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group Type</em>' attribute.
	 * @see metaFeatureModel.GroupType
	 * @see #setGroupType(GroupType)
	 * @see metaFeatureModel.MetaFeatureModelPackage#getGroup_GroupType()
	 * @model
	 * @generated
	 */
	GroupType getGroupType();

	/**
	 * Sets the value of the '{@link metaFeatureModel.Group#getGroupType <em>Group Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Group Type</em>' attribute.
	 * @see metaFeatureModel.GroupType
	 * @see #getGroupType()
	 * @generated
	 */
	void setGroupType(GroupType value);

	/**
	 * Returns the value of the '<em><b>Feature</b></em>' reference list.
	 * The list contents are of type {@link metaFeatureModel.Feature}.
	 * It is bidirectional and its opposite is '{@link metaFeatureModel.Feature#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Feature</em>' reference list.
	 * @see metaFeatureModel.MetaFeatureModelPackage#getGroup_Feature()
	 * @see metaFeatureModel.Feature#getGroup
	 * @model opposite="group" lower="2"
	 * @generated
	 */
	EList<Feature> getFeature();

} // Group
