import org.apache.log4j.Logger;
import com.github.memoize.aspect.MemoConfig;

public class Demo {

    private static Logger logger;

    @MemoConfig(repoPath="/Users/Kelvin/Dropbox/projects/memoize/code/.git",
        cachePath ="/Users/Kelvin/Desktop/memocache")
    public static void main(String[] args) throws Exception {

        Calculator calc = new Calculator();
        System.out.println("First result: " + calc.sum(1, 2));
        System.out.println("Second result: " + calc.sum(1, 2));

        System.out.println("Object name: " + calc.getName());
    }
}