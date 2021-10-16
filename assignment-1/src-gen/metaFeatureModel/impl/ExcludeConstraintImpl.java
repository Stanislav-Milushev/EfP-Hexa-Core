/**
 */
package metaFeatureModel.impl;

import metaFeatureModel.ExcludeConstraint;
import metaFeatureModel.Feature;
import metaFeatureModel.MetaFeatureModelPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Exclude Constraint</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link metaFeatureModel.impl.ExcludeConstraintImpl#getExcludedFeatureA <em>Excluded Feature A</em>}</li>
 *   <li>{@link metaFeatureModel.impl.ExcludeConstraintImpl#getExcludedFeatureB <em>Excluded Feature B</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ExcludeConstraintImpl extends ConstraintImpl implements ExcludeConstraint {
	/**
	 * The cached value of the '{@link #getExcludedFeatureA() <em>Excluded Feature A</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExcludedFeatureA()
	 * @generated
	 * @ordered
	 */
	protected Feature excludedFeatureA;

	/**
	 * The cached value of the '{@link #getExcludedFeatureB() <em>Excluded Feature B</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExcludedFeatureB()
	 * @generated
	 * @ordered
	 */
	protected Feature excludedFeatureB;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ExcludeConstraintImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetaFeatureModelPackage.Literals.EXCLUDE_CONSTRAINT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Feature getExcludedFeatureA() {
		if (excludedFeatureA != null && excludedFeatureA.eIsProxy()) {
			InternalEObject oldExcludedFeatureA = (InternalEObject) excludedFeatureA;
			excludedFeatureA = (Feature) eResolveProxy(oldExcludedFeatureA);
			if (excludedFeatureA != oldExcludedFeatureA) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							MetaFeatureModelPackage.EXCLUDE_CONSTRAINT__EXCLUDED_FEATURE_A, oldExcludedFeatureA,
							excludedFeatureA));
			}
		}
		return excludedFeatureA;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Feature basicGetExcludedFeatureA() {
		return excludedFeatureA;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setExcludedFeatureA(Feature newExcludedFeatureA) {
		Feature oldExcludedFeatureA = excludedFeatureA;
		excludedFeatureA = newExcludedFeatureA;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MetaFeatureModelPackage.EXCLUDE_CONSTRAINT__EXCLUDED_FEATURE_A, oldExcludedFeatureA,
					excludedFeatureA));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Feature getExcludedFeatureB() {
		if (excludedFeatureB != null && excludedFeatureB.eIsProxy()) {
			InternalEObject oldExcludedFeatureB = (InternalEObject) excludedFeatureB;
			excludedFeatureB = (Feature) eResolveProxy(oldExcludedFeatureB);
			if (excludedFeatureB != oldExcludedFeatureB) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							MetaFeatureModelPackage.EXCLUDE_CONSTRAINT__EXCLUDED_FEATURE_B, oldExcludedFeatureB,
							excludedFeatureB));
			}
		}
		return excludedFeatureB;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Feature basicGetExcludedFeatureB() {
		return excludedFeatureB;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setExcludedFeatureB(Feature newExcludedFeatureB) {
		Feature oldExcludedFeatureB = excludedFeatureB;
		excludedFeatureB = newExcludedFeatureB;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MetaFeatureModelPackage.EXCLUDE_CONSTRAINT__EXCLUDED_FEATURE_B, oldExcludedFeatureB,
					excludedFeatureB));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case MetaFeatureModelPackage.EXCLUDE_CONSTRAINT__EXCLUDED_FEATURE_A:
			if (resolve)
				return getExcludedFeatureA();
			return basicGetExcludedFeatureA();
		case MetaFeatureModelPackage.EXCLUDE_CONSTRAINT__EXCLUDED_FEATURE_B:
			if (resolve)
				return getExcludedFeatureB();
			return basicGetExcludedFeatureB();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case MetaFeatureModelPackage.EXCLUDE_CONSTRAINT__EXCLUDED_FEATURE_A:
			setExcludedFeatureA((Feature) newValue);
			return;
		case MetaFeatureModelPackage.EXCLUDE_CONSTRAINT__EXCLUDED_FEATURE_B:
			setExcludedFeatureB((Feature) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case MetaFeatureModelPackage.EXCLUDE_CONSTRAINT__EXCLUDED_FEATURE_A:
			setExcludedFeatureA((Feature) null);
			return;
		case MetaFeatureModelPackage.EXCLUDE_CONSTRAINT__EXCLUDED_FEATURE_B:
			setExcludedFeatureB((Feature) null);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case MetaFeatureModelPackage.EXCLUDE_CONSTRAINT__EXCLUDED_FEATURE_A:
			return excludedFeatureA != null;
		case MetaFeatureModelPackage.EXCLUDE_CONSTRAINT__EXCLUDED_FEATURE_B:
			return excludedFeatureB != null;
		}
		return super.eIsSet(featureID);
	}

} //ExcludeConstraintImpl
