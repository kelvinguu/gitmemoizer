import com.github.memoize.aspect.MemoConfig;
import com.github.memoize.aspect.Memoizable;

public class Demo {

    @MemoConfig(repoPath="../.git", cachePath="memocache", logPath="memolog.txt")
    public static void main(String[] args) throws Exception {

        System.out.println(exclaim("end the war"));
        // prints END THE WAR!!!

    }

    // Add a 'version' argument to @Memoizable to use results from a previous commit
    // The older version should return input.toUpperCase()
    // (without exclamation marks)

    // @Memoizable(version="0b834c49dee1de5c2c86d0bcda6a1b9b2f77e971")
    @Memoizable
    public static String exclaim(String input) {
        return input.toUpperCase() + "!!!";
    }

}