import junit.framework.TestCase;
import model.TranslateUnit;
import org.junit.Test;
import util.TranslateUnitFactory;

public class MainTest extends TestCase {
    @Test
    public void testMain() {
        String fileName = "src/main/resources/benchmark/t3.ssa";
        TranslateUnit translateUnit = TranslateUnitFactory.make(fileName);

        System.out.println(translateUnit);
    }
}
