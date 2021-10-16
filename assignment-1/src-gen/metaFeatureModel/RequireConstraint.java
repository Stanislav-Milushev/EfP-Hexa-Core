/**
 */
package metaFeatureModel;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Require Constraint</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metaFeatureModel.RequireConstraint#getFeature <em>Feature</em>}</li>
 *   <li>{@link metaFeatureModel.RequireConstraint#getRequiredFeature <em>Required Feature</em>}</li>
 * </ul>
 *
 * @see metaFeatureModel.MetaFeatureModelPackage#getRequireConstraint()
 * @model
 * @generated
 */
public interface RequireConstraint extends Constraint {
	/**
	 * Returns the value of the '<em><b>Feature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Feature</em>' reference.
	 * @see #setFeature(Feature)
	 * @see metaFeatureModel.MetaFeatureModelPackage#getRequireConstraint_Feature()
	 * @model required="true"
	 * @generated
	 */
	Feature getFeature();

	/**
	 * Sets the value of the '{@link metaFeatureModel.RequireConstraint#getFeature <em>Feature</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Feature</em>' reference.
	 * @see #getFeature()
	 * @generated
	 */
	void setFeature(Feature value);

	/**
	 * Returns the value of the '<em><b>Required Feature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Required Feature</em>' reference.
	 * @see #setRequiredFeature(Feature)
	 * @see metaFeatureModel.MetaFeatureModelPackage#getRequireConstraint_RequiredFeature()
	 * @model required="true"
	 * @generated
	 */
	Feature getRequiredFeature();

	/**
	 * Sets the value of the '{@link metaFeatureModel.RequireConstraint#getRequiredFeature <em>Required Feature</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Required Feature</em>' reference.
	 * @see #getRequiredFeature()
	 * @generated
	 */
	void setRequiredFeature(Feature value);

} // RequireConstraint
