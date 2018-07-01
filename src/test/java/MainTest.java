import junit.framework.TestCase;
import model.TranslateUnit;
import org.junit.Test;
import util.TranslateUnitFactory;

import java.io.File;

public class MainTest extends TestCase {
    final String benchmarkPath = "src/main/resources/benchmark/";

    @Test
    public void testMain() {

        String fileName = "t10";
        File file = new File(benchmarkPath + fileName + ".ssa");
        TranslateUnit translateUnit = TranslateUnitFactory.make(fileName, file);

        translateUnit.drawAllCFG();

        System.out.println(translateUnit);

//        CFGFactory factory = CFGFactory.getInstance();
//        Expression expression = factory.resolveIfGotoExpression("if (_8 < b_9(D))");
//        System.out.println(expression.getName());
    }
}
