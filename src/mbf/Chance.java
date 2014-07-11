package mbf;

import java.util.Objects;

/**
 * The chance that the value was seen in the BF.
 * Value is in range between 0 and 1.
 */
public final class Chance {

    private static final Chance NONE = new Chance(0.0f);

    public static Chance none() {
        return NONE;
    }

    public static Chance of(float chance) {
        if (chance < 0 || chance > 1) {
            throw new IllegalArgumentException("Chance should be in range [0..1]");
        }
        if (chance == 0.0f) {
            return NONE;
        }
        return new Chance(chance);
    }

    private final float chance;

    private Chance(float chance) {
        this.chance = chance;
    }

    public float get() {
        return chance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chance);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (!(other instanceof Chance)) return false;
        if (this == other) return true;
        return Objects.equals(chance, ((Chance) other).chance);
    }

}
