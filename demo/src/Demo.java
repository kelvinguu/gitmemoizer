import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;

public class Demo {

    private static Logger logger;

    @MemoizeConfig()
    public static void main(String[] args) throws Exception {

        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.DEBUG);
        logger = Logger.getLogger(Tester.class);

        Calculator calc = new Calculator();
        logger.debug("First result: " + calc.sum(1, 2));
        logger.debug("Second result: " + calc.sum(1, 2));

        logger.debug("Object name: " + calc.getName());
    }
}