package org.mbe.sat.core.model.formula;

/**
 * Represents a boolean with three states. A tristate can be {@link Tristate#TRUE TRUE}, {@link Tristate#FALSE FALSE} or
 * {@link Tristate#UNDEFINED UNDEFINED}. It provides convenience methods to check its state:
 * <ul>
 *     <li>{@link Tristate#isTrue()}: Returns <code>true</code> if and only if the tristate is equal to
 *     {@link Tristate#TRUE Tristate.TRUE}</li>
 *     <li>{@link Tristate#isFalse()}: Returns <code>true</code> if and only if the tristate is equal to
 *     {@link Tristate#FALSE Tristate.FALSE}</li>
 *     <li>{@link Tristate#isUndefined()}: Returns <code>true</code> if and only if the tristate is equal to
 *     {@link Tristate#UNDEFINED Tristate.UNDEFINED}</li>
 * </ul>
 * IMPORTANT: Saying a tristate is <code>false</code> is not the same as saying it is not <code>true</code>. The
 * tristate {@link Tristate#UNDEFINED} e.g. returns <code>false</code> for {@link Tristate#isFalse()} and
 * {@link Tristate#isTrue()}.
 */
public enum Tristate {

    /**
     * Represents a positive {@link Tristate} constant that represents the boolean value <code>true</code>.
     */
    TRUE {
        @Override
        public boolean isTrue() {
            return true;
        }

        @Override
        public boolean isFalse() {
            return false;
        }

        @Override
        public boolean isUndefined() {
            return false;
        }
    },

    /**
     * Represents a negative {@link Tristate} constant that represents the boolean value <code>false</code>.
     */
    FALSE {
        @Override
        public boolean isTrue() {
            return false;
        }

        @Override
        public boolean isFalse() {
            return true;
        }

        @Override
        public boolean isUndefined() {
            return false;
        }
    },

    /**
     * Represents a undefined {@link Tristate} constant that represent an undefined boolean value.
     */
    UNDEFINED {
        @Override
        public boolean isTrue() {
            return false;
        }

        @Override
        public boolean isFalse() {
            return false;
        }

        @Override
        public boolean isUndefined() {
            return true;
        }
    };


    /**
     * Creates a {@link Tristate} constant for the given boolean value.
     *
     * @param value the boolean value that is represented by the returned tristate
     * @return {@link Tristate#TRUE} if the given boolean is <code>true</code> or {@link Tristate#FALSE} if the given
     * boolean is <code>false</code>
     */
    public static Tristate of(boolean value) {
        return value ? TRUE : FALSE;
    }

    /**
     * Returns <code>true</code>, if and only if, the tristate is <code>{@link Tristate#TRUE}</code>.
     *
     * @return <code>true</code>, if the tristate is {@link Tristate#TRUE}, <code>false</code> otherwise
     */
    public abstract boolean isTrue();

    /**
     * Returns <code>true</code>, if and only if, the tristate is <code>{@link Tristate#FALSE}</code>.
     *
     * @return <code>true</code>, if the tristate is {@link Tristate#FALSE}, <code>false</code> otherwise
     */
    public abstract boolean isFalse();

    /**
     * Returns <code>true</code>, if and only if, the tristate is <code>{@link Tristate#UNDEFINED}</code>.
     *
     * @return <code>true</code>, if the tristate is {@link Tristate#UNDEFINED}, <code>false</code> otherwise
     */
    public abstract boolean isUndefined();

    /**
     * Inverts the tristate value and returns the result. Returns {@link #TRUE} when the Tristate is {@link #FALSE} or
     * returns {@link #FALSE} when the tristate is {@link #TRUE}. When the Tristate is {@link #UNDEFINED} it remains
     * undefined.
     *
     * @return the inverted value of the tristate, or {@link #UNDEFINED} if it was undefined in the first place
     */
    public Tristate invert() {
        switch (this) {
            case FALSE:
                return TRUE;
            case TRUE:
                return FALSE;
            default:
                return UNDEFINED;
        }
    }
}