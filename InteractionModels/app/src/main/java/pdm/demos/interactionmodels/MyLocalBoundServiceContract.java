package pdm.demos.interactionmodels;

/**
 * Contract to be supported by the Bound Service that supports local accesses.
 */
public interface MyLocalBoundServiceContract {

    /**
     * An example of a bound service operation.
     * @param value An input value.
     */
    void doSomething(int value);
}
