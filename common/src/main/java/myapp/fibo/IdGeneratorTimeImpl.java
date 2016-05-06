package myapp.fibo;

/**
 * Quick solution. No guarantee to be global unique
 */
public class IdGeneratorTimeImpl implements IdGenerator {
    @Override
    public String gen() {
        return new Long(System.nanoTime()).toString();
    }
}
