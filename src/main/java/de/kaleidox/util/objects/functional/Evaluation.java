package de.kaleidox.util.objects.functional;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * This class can be used to run different actions after a predicate. Boolean evaulations can be simplified using {@link
 * #of(boolean)}.
 *
 * @param <T> The type variable value the result.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Evaluation<T> {
    private final T            value;
    private final Predicate<T> tester;
    private final boolean      result;
    
    /**
     * Creates a new Evaluation instance.
     *
     * @param of     The object to test.
     * @param tester A predicate to decide whether to fail or to succeed.
     */
    public Evaluation(T of, Predicate<T> tester) {
        this.value = of;
        this.tester = tester;
        this.result = tester.test(of);
    }
    
    // Override Methods
    @Override
    public String toString() {
        return "Evaluation [" + value.toString() + "; tested " + (result ? "TRUE" : "FALSE") + "]";
    }
    
    /**
     * Gets the actual value.
     *
     * @return The value.
     */
    public T getValue() {
        return value;
    }
    
    /**
     * Gets whether the evaluation has succeeded.
     *
     * @return Whether the evaluation has succeeded.
     */
    public boolean getResult() {
        return result;
    }
    
    /**
     * Performs an action if the pre-defined Predicate has tested {@code TRUE}.
     *
     * @param successAction The action to perform if the predicate succeeds.
     * @return Whether the action could be performed. Works similar to {@link #getResult()}.
     */
    public boolean onSuccess(Consumer<T> successAction) {
        if (result) successAction.accept(value);
        return result;
    }
    
    /**
     * Performs an action if a given Predicate tests {@code TRUE}.
     *
     * @param tester        The predicate to test with.
     * @param successAction The action to perform if the predicate succeeds.
     * @return Whether the action could be performed. Works similar to {@link #getResult()}.
     */
    public boolean onSuccess(Predicate<T> tester, Consumer<T> successAction) {
        if (tester.test(value)) {
            successAction.accept(value);
            return true;
        }
        return false;
    }
    
    /**
     * Performs an action if the pre-defined Predicate tests {@code FALSE}.
     *
     * @param failureAction The action to perform is the predicate succeeds.
     * @return Whether the action was performed.
     */
    public boolean onFailure(Consumer<T> failureAction) {
        if (!result) failureAction.accept(value);
        return result;
    }
    
    /**
     * Performs an action if a given Predicate tests {@code FALSE}.
     *
     * @param tester        The predicate to test with.
     * @param failureAction The action to perform if the predicate fails.
     * @return Whether the action was performed.
     */
    public boolean onFailure(Predicate<T> tester, Consumer<T> failureAction) {
        if (tester.test(value)) {
            failureAction.accept(value);
            return true;
        }
        return false;
    }
    
    /**
     * Performs one of the given action, depending on whether the predicate failed or succeeded.
     *
     * @param successAction The action to perform if the predicate succeeds.
     * @param failureAction The action to perform if the predicate fails.
     * @return Whether the action could be performed. Works similar to {@link #getResult()}.
     */
    public boolean evaluate(Consumer<T> successAction, Consumer<T> failureAction) {
        boolean b = onSuccess(successAction);
        if (!b) failureAction.accept(value);
        return b;
    }
    
    // Static membe
    
// Static members
    /**
     * Creates a new Evaluation instance for a predefined boolean.
     *
     * @param bool The boolean to use for testing.
     * @return The Evaluation instance.
     */
    public static Evaluation<Boolean> of(boolean bool) {
        return new Evaluation<>(bool, n -> n);
    }
}
