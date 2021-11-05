/**
 */
package metaFeatureModel;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see metaFeatureModel.MetaFeatureModelFactory
 * @model kind="package"
 * @generated
 */
public interface MetaFeatureModelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "metaFeatureModel";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.example.org/metaFeatureModel";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "metaFeatureModel";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MetaFeatureModelPackage eINSTANCE = metaFeatureModel.impl.MetaFeatureModelPackageImpl.init();

	/**
	 * The meta object id for the '{@link metaFeatureModel.impl.FeatureModelImpl <em>Feature Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metaFeatureModel.impl.FeatureModelImpl
	 * @see metaFeatureModel.impl.MetaFeatureModelPackageImpl#getFeatureModel()
	 * @generated
	 */
	int FEATURE_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Group</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_MODEL__GROUP = 0;

	/**
	 * The feature id for the '<em><b>Constraint</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_MODEL__CONSTRAINT = 1;

	/**
	 * The feature id for the '<em><b>Root</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_MODEL__ROOT = 2;

	/**
	 * The number of structural features of the '<em>Feature Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_MODEL_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Feature Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metaFeatureModel.impl.ConstraintImpl <em>Constraint</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metaFeatureModel.impl.ConstraintImpl
	 * @see metaFeatureModel.impl.MetaFeatureModelPackageImpl#getConstraint()
	 * @generated
	 */
	int CONSTRAINT = 1;

	/**
	 * The number of structural features of the '<em>Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metaFeatureModel.impl.ExcludeConstraintImpl <em>Exclude Constraint</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metaFeatureModel.impl.ExcludeConstraintImpl
	 * @see metaFeatureModel.impl.MetaFeatureModelPackageImpl#getExcludeConstraint()
	 * @generated
	 */
	int EXCLUDE_CONSTRAINT = 2;

	/**
	 * The feature id for the '<em><b>Excluded Feature A</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXCLUDE_CONSTRAINT__EXCLUDED_FEATURE_A = CONSTRAINT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Excluded Feature B</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXCLUDE_CONSTRAINT__EXCLUDED_FEATURE_B = CONSTRAINT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Exclude Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXCLUDE_CONSTRAINT_FEATURE_COUNT = CONSTRAINT_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Exclude Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXCLUDE_CONSTRAINT_OPERATION_COUNT = CONSTRAINT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link metaFeatureModel.impl.RequireConstraintImpl <em>Require Constraint</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metaFeatureModel.impl.RequireConstraintImpl
	 * @see metaFeatureModel.impl.MetaFeatureModelPackageImpl#getRequireConstraint()
	 * @generated
	 */
	int REQUIRE_CONSTRAINT = 3;

	/**
	 * The feature id for the '<em><b>Feature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUIRE_CONSTRAINT__FEATURE = CONSTRAINT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Required Feature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUIRE_CONSTRAINT__REQUIRED_FEATURE = CONSTRAINT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Require Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUIRE_CONSTRAINT_FEATURE_COUNT = CONSTRAINT_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Require Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUIRE_CONSTRAINT_OPERATION_COUNT = CONSTRAINT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link metaFeatureModel.impl.FeatureImpl <em>Feature</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metaFeatureModel.impl.FeatureImpl
	 * @see metaFeatureModel.impl.MetaFeatureModelPackageImpl#getFeature()
	 * @generated
	 */
	int FEATURE = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Mandatory</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE__MANDATORY = 1;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE__ABSTRACT = 2;

	/**
	 * The feature id for the '<em><b>Group</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE__GROUP = 3;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE__CHILDREN = 4;

	/**
	 * The feature id for the '<em><b>Number</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE__NUMBER = 5;

	/**
	 * The number of structural features of the '<em>Feature</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_FEATURE_COUNT = 6;

	/**
	 * The number of operations of the '<em>Feature</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metaFeatureModel.impl.GroupImpl <em>Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metaFeatureModel.impl.GroupImpl
	 * @see metaFeatureModel.impl.MetaFeatureModelPackageImpl#getGroup()
	 * @generated
	 */
	int GROUP = 5;

	/**
	 * The feature id for the '<em><b>Group Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP__GROUP_TYPE = 0;

	/**
	 * The feature id for the '<em><b>Feature</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP__FEATURE = 1;

	/**
	 * The number of structural features of the '<em>Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metaFeatureModel.GroupType <em>Group Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metaFeatureModel.GroupType
	 * @see metaFeatureModel.impl.MetaFeatureModelPackageImpl#getGroupType()
	 * @generated
	 */
	int GROUP_TYPE = 6;

	/**
	 * Returns the meta object for class '{@link metaFeatureModel.FeatureModel <em>Feature Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Feature Model</em>'.
	 * @see metaFeatureModel.FeatureModel
	 * @generated
	 */
	EClass getFeatureModel();

	/**
	 * Returns the meta object for the containment reference list '{@link metaFeatureModel.FeatureModel#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Group</em>'.
	 * @see metaFeatureModel.FeatureModel#getGroup()
	 * @see #getFeatureModel()
	 * @generated
	 */
	EReference getFeatureModel_Group();

	/**
	 * Returns the meta object for the containment reference list '{@link metaFeatureModel.FeatureModel#getConstraint <em>Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Constraint</em>'.
	 * @see metaFeatureModel.FeatureModel#getConstraint()
	 * @see #getFeatureModel()
	 * @generated
	 */
	EReference getFeatureModel_Constraint();

	/**
	 * Returns the meta object for the containment reference '{@link metaFeatureModel.FeatureModel#getRoot <em>Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Root</em>'.
	 * @see metaFeatureModel.FeatureModel#getRoot()
	 * @see #getFeatureModel()
	 * @generated
	 */
	EReference getFeatureModel_Root();

	/**
	 * Returns the meta object for class '{@link metaFeatureModel.Constraint <em>Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Constraint</em>'.
	 * @see metaFeatureModel.Constraint
	 * @generated
	 */
	EClass getConstraint();

	/**
	 * Returns the meta object for class '{@link metaFeatureModel.ExcludeConstraint <em>Exclude Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Exclude Constraint</em>'.
	 * @see metaFeatureModel.ExcludeConstraint
	 * @generated
	 */
	EClass getExcludeConstraint();

	/**
	 * Returns the meta object for the reference '{@link metaFeatureModel.ExcludeConstraint#getExcludedFeatureA <em>Excluded Feature A</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Excluded Feature A</em>'.
	 * @see metaFeatureModel.ExcludeConstraint#getExcludedFeatureA()
	 * @see #getExcludeConstraint()
	 * @generated
	 */
	EReference getExcludeConstraint_ExcludedFeatureA();

	/**
	 * Returns the meta object for the reference '{@link metaFeatureModel.ExcludeConstraint#getExcludedFeatureB <em>Excluded Feature B</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Excluded Feature B</em>'.
	 * @see metaFeatureModel.ExcludeConstraint#getExcludedFeatureB()
	 * @see #getExcludeConstraint()
	 * @generated
	 */
	EReference getExcludeConstraint_ExcludedFeatureB();

	/**
	 * Returns the meta object for class '{@link metaFeatureModel.RequireConstraint <em>Require Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Require Constraint</em>'.
	 * @see metaFeatureModel.RequireConstraint
	 * @generated
	 */
	EClass getRequireConstraint();

	/**
	 * Returns the meta object for the reference '{@link metaFeatureModel.RequireConstraint#getFeature <em>Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Feature</em>'.
	 * @see metaFeatureModel.RequireConstraint#getFeature()
	 * @see #getRequireConstraint()
	 * @generated
	 */
	EReference getRequireConstraint_Feature();

	/**
	 * Returns the meta object for the reference '{@link metaFeatureModel.RequireConstraint#getRequiredFeature <em>Required Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Required Feature</em>'.
	 * @see metaFeatureModel.RequireConstraint#getRequiredFeature()
	 * @see #getRequireConstraint()
	 * @generated
	 */
	EReference getRequireConstraint_RequiredFeature();

	/**
	 * Returns the meta object for class '{@link metaFeatureModel.Feature <em>Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Feature</em>'.
	 * @see metaFeatureModel.Feature
	 * @generated
	 */
	EClass getFeature();

	/**
	 * Returns the meta object for the attribute '{@link metaFeatureModel.Feature#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see metaFeatureModel.Feature#getName()
	 * @see #getFeature()
	 * @generated
	 */
	EAttribute getFeature_Name();

	/**
	 * Returns the meta object for the attribute '{@link metaFeatureModel.Feature#isMandatory <em>Mandatory</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Mandatory</em>'.
	 * @see metaFeatureModel.Feature#isMandatory()
	 * @see #getFeature()
	 * @generated
	 */
	EAttribute getFeature_Mandatory();

	/**
	 * Returns the meta object for the attribute '{@link metaFeatureModel.Feature#isAbstract <em>Abstract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Abstract</em>'.
	 * @see metaFeatureModel.Feature#isAbstract()
	 * @see #getFeature()
	 * @generated
	 */
	EAttribute getFeature_Abstract();

	/**
	 * Returns the meta object for the reference '{@link metaFeatureModel.Feature#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Group</em>'.
	 * @see metaFeatureModel.Feature#getGroup()
	 * @see #getFeature()
	 * @generated
	 */
	EReference getFeature_Group();

	/**
	 * Returns the meta object for the containment reference list '{@link metaFeatureModel.Feature#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Children</em>'.
	 * @see metaFeatureModel.Feature#getChildren()
	 * @see #getFeature()
	 * @generated
	 */
	EReference getFeature_Children();

	/**
	 * Returns the meta object for the attribute '{@link metaFeatureModel.Feature#getNumber <em>Number</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Number</em>'.
	 * @see metaFeatureModel.Feature#getNumber()
	 * @see #getFeature()
	 * @generated
	 */
	EAttribute getFeature_Number();

	/**
	 * Returns the meta object for class '{@link metaFeatureModel.Group <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Group</em>'.
	 * @see metaFeatureModel.Group
	 * @generated
	 */
	EClass getGroup();

	/**
	 * Returns the meta object for the attribute '{@link metaFeatureModel.Group#getGroupType <em>Group Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Group Type</em>'.
	 * @see metaFeatureModel.Group#getGroupType()
	 * @see #getGroup()
	 * @generated
	 */
	EAttribute getGroup_GroupType();

	/**
	 * Returns the meta object for the reference list '{@link metaFeatureModel.Group#getFeature <em>Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Feature</em>'.
	 * @see metaFeatureModel.Group#getFeature()
	 * @see #getGroup()
	 * @generated
	 */
	EReference getGroup_Feature();

	/**
	 * Returns the meta object for enum '{@link metaFeatureModel.GroupType <em>Group Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Group Type</em>'.
	 * @see metaFeatureModel.GroupType
	 * @generated
	 */
	EEnum getGroupType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	MetaFeatureModelFactory getMetaFeatureModelFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link metaFeatureModel.impl.FeatureModelImpl <em>Feature Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metaFeatureModel.impl.FeatureModelImpl
		 * @see metaFeatureModel.impl.MetaFeatureModelPackageImpl#getFeatureModel()
		 * @generated
		 */
		EClass FEATURE_MODEL = eINSTANCE.getFeatureModel();

		/**
		 * The meta object literal for the '<em><b>Group</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FEATURE_MODEL__GROUP = eINSTANCE.getFeatureModel_Group();

		/**
		 * The meta object literal for the '<em><b>Constraint</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FEATURE_MODEL__CONSTRAINT = eINSTANCE.getFeatureModel_Constraint();

		/**
		 * The meta object literal for the '<em><b>Root</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FEATURE_MODEL__ROOT = eINSTANCE.getFeatureModel_Root();

		/**
		 * The meta object literal for the '{@link metaFeatureModel.impl.ConstraintImpl <em>Constraint</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metaFeatureModel.impl.ConstraintImpl
		 * @see metaFeatureModel.impl.MetaFeatureModelPackageImpl#getConstraint()
		 * @generated
		 */
		EClass CONSTRAINT = eINSTANCE.getConstraint();

		/**
		 * The meta object literal for the '{@link metaFeatureModel.impl.ExcludeConstraintImpl <em>Exclude Constraint</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metaFeatureModel.impl.ExcludeConstraintImpl
		 * @see metaFeatureModel.impl.MetaFeatureModelPackageImpl#getExcludeConstraint()
		 * @generated
		 */
		EClass EXCLUDE_CONSTRAINT = eINSTANCE.getExcludeConstraint();

		/**
		 * The meta object literal for the '<em><b>Excluded Feature A</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXCLUDE_CONSTRAINT__EXCLUDED_FEATURE_A = eINSTANCE.getExcludeConstraint_ExcludedFeatureA();

		/**
		 * The meta object literal for the '<em><b>Excluded Feature B</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXCLUDE_CONSTRAINT__EXCLUDED_FEATURE_B = eINSTANCE.getExcludeConstraint_ExcludedFeatureB();

		/**
		 * The meta object literal for the '{@link metaFeatureModel.impl.RequireConstraintImpl <em>Require Constraint</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metaFeatureModel.impl.RequireConstraintImpl
		 * @see metaFeatureModel.impl.MetaFeatureModelPackageImpl#getRequireConstraint()
		 * @generated
		 */
		EClass REQUIRE_CONSTRAINT = eINSTANCE.getRequireConstraint();

		/**
		 * The meta object literal for the '<em><b>Feature</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REQUIRE_CONSTRAINT__FEATURE = eINSTANCE.getRequireConstraint_Feature();

		/**
		 * The meta object literal for the '<em><b>Required Feature</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REQUIRE_CONSTRAINT__REQUIRED_FEATURE = eINSTANCE.getRequireConstraint_RequiredFeature();

		/**
		 * The meta object literal for the '{@link metaFeatureModel.impl.FeatureImpl <em>Feature</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metaFeatureModel.impl.FeatureImpl
		 * @see metaFeatureModel.impl.MetaFeatureModelPackageImpl#getFeature()
		 * @generated
		 */
		EClass FEATURE = eINSTANCE.getFeature();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FEATURE__NAME = eINSTANCE.getFeature_Name();

		/**
		 * The meta object literal for the '<em><b>Mandatory</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FEATURE__MANDATORY = eINSTANCE.getFeature_Mandatory();

		/**
		 * The meta object literal for the '<em><b>Abstract</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FEATURE__ABSTRACT = eINSTANCE.getFeature_Abstract();

		/**
		 * The meta object literal for the '<em><b>Group</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FEATURE__GROUP = eINSTANCE.getFeature_Group();

		/**
		 * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FEATURE__CHILDREN = eINSTANCE.getFeature_Children();

		/**
		 * The meta object literal for the '<em><b>Number</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FEATURE__NUMBER = eINSTANCE.getFeature_Number();

		/**
		 * The meta object literal for the '{@link metaFeatureModel.impl.GroupImpl <em>Group</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metaFeatureModel.impl.GroupImpl
		 * @see metaFeatureModel.impl.MetaFeatureModelPackageImpl#getGroup()
		 * @generated
		 */
		EClass GROUP = eINSTANCE.getGroup();

		/**
		 * The meta object literal for the '<em><b>Group Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GROUP__GROUP_TYPE = eINSTANCE.getGroup_GroupType();

		/**
		 * The meta object literal for the '<em><b>Feature</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GROUP__FEATURE = eINSTANCE.getGroup_Feature();

		/**
		 * The meta object literal for the '{@link metaFeatureModel.GroupType <em>Group Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metaFeatureModel.GroupType
		 * @see metaFeatureModel.impl.MetaFeatureModelPackageImpl#getGroupType()
		 * @generated
		 */
		EEnum GROUP_TYPE = eINSTANCE.getGroupType();

	}

} //MetaFeatureModelPackage
