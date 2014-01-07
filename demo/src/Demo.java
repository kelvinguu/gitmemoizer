import com.github.memoize.aspect.MemoConfig;
import com.github.memoize.aspect.Memoizable;

public class Demo {

    @MemoConfig(repoPath="../.git", cachePath="memocache", logPath="memolog.txt")
    public static void main(String[] args) throws Exception {

        System.out.println(exclaim("end the war"));
        // prints END THE WAR

    }

    @Memoizable
    public static String exclaim(String input) {
        return input.toUpperCase();
    }

}