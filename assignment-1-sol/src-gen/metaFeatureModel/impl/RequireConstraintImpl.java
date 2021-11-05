/**
 */
package metaFeatureModel.impl;

import metaFeatureModel.Feature;
import metaFeatureModel.MetaFeatureModelPackage;
import metaFeatureModel.RequireConstraint;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Require Constraint</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link metaFeatureModel.impl.RequireConstraintImpl#getFeature <em>Feature</em>}</li>
 *   <li>{@link metaFeatureModel.impl.RequireConstraintImpl#getRequiredFeature <em>Required Feature</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RequireConstraintImpl extends ConstraintImpl implements RequireConstraint {
	/**
	 * The cached value of the '{@link #getFeature() <em>Feature</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFeature()
	 * @generated
	 * @ordered
	 */
	protected Feature feature;

	/**
	 * The cached value of the '{@link #getRequiredFeature() <em>Required Feature</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRequiredFeature()
	 * @generated
	 * @ordered
	 */
	protected Feature requiredFeature;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RequireConstraintImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetaFeatureModelPackage.Literals.REQUIRE_CONSTRAINT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Feature getFeature() {
		if (feature != null && feature.eIsProxy()) {
			InternalEObject oldFeature = (InternalEObject) feature;
			feature = (Feature) eResolveProxy(oldFeature);
			if (feature != oldFeature) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							MetaFeatureModelPackage.REQUIRE_CONSTRAINT__FEATURE, oldFeature, feature));
			}
		}
		return feature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Feature basicGetFeature() {
		return feature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFeature(Feature newFeature) {
		Feature oldFeature = feature;
		feature = newFeature;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetaFeatureModelPackage.REQUIRE_CONSTRAINT__FEATURE,
					oldFeature, feature));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Feature getRequiredFeature() {
		if (requiredFeature != null && requiredFeature.eIsProxy()) {
			InternalEObject oldRequiredFeature = (InternalEObject) requiredFeature;
			requiredFeature = (Feature) eResolveProxy(oldRequiredFeature);
			if (requiredFeature != oldRequiredFeature) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							MetaFeatureModelPackage.REQUIRE_CONSTRAINT__REQUIRED_FEATURE, oldRequiredFeature,
							requiredFeature));
			}
		}
		return requiredFeature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Feature basicGetRequiredFeature() {
		return requiredFeature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRequiredFeature(Feature newRequiredFeature) {
		Feature oldRequiredFeature = requiredFeature;
		requiredFeature = newRequiredFeature;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MetaFeatureModelPackage.REQUIRE_CONSTRAINT__REQUIRED_FEATURE, oldRequiredFeature, requiredFeature));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case MetaFeatureModelPackage.REQUIRE_CONSTRAINT__FEATURE:
			if (resolve)
				return getFeature();
			return basicGetFeature();
		case MetaFeatureModelPackage.REQUIRE_CONSTRAINT__REQUIRED_FEATURE:
			if (resolve)
				return getRequiredFeature();
			return basicGetRequiredFeature();
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
		case MetaFeatureModelPackage.REQUIRE_CONSTRAINT__FEATURE:
			setFeature((Feature) newValue);
			return;
		case MetaFeatureModelPackage.REQUIRE_CONSTRAINT__REQUIRED_FEATURE:
			setRequiredFeature((Feature) newValue);
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
		case MetaFeatureModelPackage.REQUIRE_CONSTRAINT__FEATURE:
			setFeature((Feature) null);
			return;
		case MetaFeatureModelPackage.REQUIRE_CONSTRAINT__REQUIRED_FEATURE:
			setRequiredFeature((Feature) null);
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
		case MetaFeatureModelPackage.REQUIRE_CONSTRAINT__FEATURE:
			return feature != null;
		case MetaFeatureModelPackage.REQUIRE_CONSTRAINT__REQUIRED_FEATURE:
			return requiredFeature != null;
		}
		return super.eIsSet(featureID);
	}

} //RequireConstraintImpl
