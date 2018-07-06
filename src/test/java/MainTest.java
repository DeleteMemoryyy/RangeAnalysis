import junit.framework.TestCase;
import model.TranslateUnit;
import org.junit.Test;
import util.TranslateUnitFactory;

import java.io.File;

public class MainTest extends TestCase {
    final String benchmarkPath = "src/main/resources/benchmark/";

    @Test
    public void test1() {
        String fileName = "t" + 1;
        File file = new File(benchmarkPath + fileName + ".ssa");
        TranslateUnit translateUnit = TranslateUnitFactory.make(fileName, file);

        translateUnit.drawAllCFG();
        translateUnit.drawAllConstraintGraph();
        System.out.println(translateUnit);
    }

    public void test2() {
        String fileName = "t" + 2;
        File file = new File(benchmarkPath + fileName + ".ssa");
        TranslateUnit translateUnit = TranslateUnitFactory.make(fileName, file);

        translateUnit.drawAllCFG();
        translateUnit.drawAllConstraintGraph();
        System.out.println(translateUnit);
    }

    public void test3() {
        String fileName = "t" + 3;
        File file = new File(benchmarkPath + fileName + ".ssa");
        TranslateUnit translateUnit = TranslateUnitFactory.make(fileName, file);

        translateUnit.drawAllCFG();
        translateUnit.drawAllConstraintGraph();
        System.out.println(translateUnit);
    }

    public void test4() {
        String fileName = "t" + 4;
        File file = new File(benchmarkPath + fileName + ".ssa");
        TranslateUnit translateUnit = TranslateUnitFactory.make(fileName, file);

        translateUnit.drawAllCFG();
        translateUnit.drawAllConstraintGraph();
        System.out.println(translateUnit);
    }

    public void test5() {
        String fileName = "t" + 5;
        File file = new File(benchmarkPath + fileName + ".ssa");
        TranslateUnit translateUnit = TranslateUnitFactory.make(fileName, file);

        translateUnit.drawAllCFG();
        translateUnit.drawAllConstraintGraph();
        System.out.println(translateUnit);
    }

    public void test6() {
        String fileName = "t" + 6;
        File file = new File(benchmarkPath + fileName + ".ssa");
        TranslateUnit translateUnit = TranslateUnitFactory.make(fileName, file);

        translateUnit.drawAllCFG();
        translateUnit.drawAllConstraintGraph();
        System.out.println(translateUnit);
    }

    public void test7() {
        String fileName = "t" + 7;
        File file = new File(benchmarkPath + fileName + ".ssa");
        TranslateUnit translateUnit = TranslateUnitFactory.make(fileName, file);

        translateUnit.drawAllCFG();
        translateUnit.drawAllConstraintGraph();
        System.out.println(translateUnit);
    }

    public void test8() {
        String fileName = "t" + 8;
        File file = new File(benchmarkPath + fileName + ".ssa");
        TranslateUnit translateUnit = TranslateUnitFactory.make(fileName, file);

        translateUnit.drawAllCFG();
        translateUnit.drawAllConstraintGraph();
        System.out.println(translateUnit);
    }

    public void test9() {
        String fileName = "t" + 9;
        File file = new File(benchmarkPath + fileName + ".ssa");
        TranslateUnit translateUnit = TranslateUnitFactory.make(fileName, file);

        translateUnit.drawAllCFG();
        translateUnit.drawAllConstraintGraph();
        System.out.println(translateUnit);
    }

    public void test10() {
        String fileName = "t" + 10;
        File file = new File(benchmarkPath + fileName + ".ssa");
        TranslateUnit translateUnit = TranslateUnitFactory.make(fileName, file);

        translateUnit.drawAllCFG();
        translateUnit.drawAllConstraintGraph();
        System.out.println(translateUnit);
    }

    public void test11() {
        String fileName = "t" + 11;
        File file = new File(benchmarkPath + fileName + ".ssa");
        TranslateUnit translateUnit = TranslateUnitFactory.make(fileName, file);

        translateUnit.drawAllCFG();
        translateUnit.drawAllConstraintGraph();
        System.out.println(translateUnit);
    }
}
