import com.github.memoize.aspect.Memoizable;

public class Calculator {

    @Memoizable
    public int sum(int a, int b) throws InterruptedException {
        Thread.sleep(500);
        return a + b;
    }

    @Memoizable
    public String getName() {
        return "Calculator";
    }

}
