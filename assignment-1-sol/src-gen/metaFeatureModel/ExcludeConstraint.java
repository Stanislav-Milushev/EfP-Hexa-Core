/**
 */
package metaFeatureModel;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Exclude Constraint</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metaFeatureModel.ExcludeConstraint#getExcludedFeatureA <em>Excluded Feature A</em>}</li>
 *   <li>{@link metaFeatureModel.ExcludeConstraint#getExcludedFeatureB <em>Excluded Feature B</em>}</li>
 * </ul>
 *
 * @see metaFeatureModel.MetaFeatureModelPackage#getExcludeConstraint()
 * @model
 * @generated
 */
public interface ExcludeConstraint extends Constraint {
	/**
	 * Returns the value of the '<em><b>Excluded Feature A</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Excluded Feature A</em>' reference.
	 * @see #setExcludedFeatureA(Feature)
	 * @see metaFeatureModel.MetaFeatureModelPackage#getExcludeConstraint_ExcludedFeatureA()
	 * @model required="true"
	 * @generated
	 */
	Feature getExcludedFeatureA();

	/**
	 * Sets the value of the '{@link metaFeatureModel.ExcludeConstraint#getExcludedFeatureA <em>Excluded Feature A</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Excluded Feature A</em>' reference.
	 * @see #getExcludedFeatureA()
	 * @generated
	 */
	void setExcludedFeatureA(Feature value);

	/**
	 * Returns the value of the '<em><b>Excluded Feature B</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Excluded Feature B</em>' reference.
	 * @see #setExcludedFeatureB(Feature)
	 * @see metaFeatureModel.MetaFeatureModelPackage#getExcludeConstraint_ExcludedFeatureB()
	 * @model required="true"
	 * @generated
	 */
	Feature getExcludedFeatureB();

	/**
	 * Sets the value of the '{@link metaFeatureModel.ExcludeConstraint#getExcludedFeatureB <em>Excluded Feature B</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Excluded Feature B</em>' reference.
	 * @see #getExcludedFeatureB()
	 * @generated
	 */
	void setExcludedFeatureB(Feature value);

} // ExcludeConstraint
