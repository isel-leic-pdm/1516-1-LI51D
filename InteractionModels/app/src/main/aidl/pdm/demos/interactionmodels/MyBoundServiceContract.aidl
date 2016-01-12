// MyBoundServiceContract.aidl
package pdm.demos.interactionmodels;

// Declare any non-default types here with import statements

/**
 * Contract to be supported by the Bound Service that supports remote accesses.
 */
interface MyBoundServiceContract {

    /**
     * An example of a bound service operation.
     * @param value An input value.
     */
    void doSomething(int value);
}
