import junit.framework.TestCase;
import model.TranslateUnit;
import org.junit.Test;
import util.TranslateUnitFactory;
import util.math.Interval;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainTest extends TestCase {
    final String benchmarkPath = "src/main/resources/benchmark/";

    /*
     * file: t1.c
     * input:
     * output: k in [100, 100]
     */
    @Test
    public void test1() {
        String fileName = "t" + 1;
        File file = new File(benchmarkPath + fileName + ".ssa");
        TranslateUnit translateUnit = TranslateUnitFactory.make(fileName, file);
        String funcitonName = "foo";
        List<Interval> arguments = new ArrayList<>();

        translateUnit.drawAllCFG();
        translateUnit.drawAllConstraintGraph();

        Interval answer = new Interval(100, 100);
        Interval result = translateUnit.getReturnRange(funcitonName, arguments, true);
        System.out.println(translateUnit);
        assertEquals(answer, result);
    }

    /*
     * file: t2.c
     * input: k in [200, 300]
     * output: k in [200, 300]
     */
    @Test
    public void test2() {
        String fileName = "t" + 2;
        File file = new File(benchmarkPath + fileName + ".ssa");
        TranslateUnit translateUnit = TranslateUnitFactory.make(fileName, file);
        String funcitonName = "foo";
        List<Interval> arguments = new ArrayList<>();
        arguments.add(new Interval(200, 300));

        translateUnit.drawAllCFG();
        translateUnit.drawAllConstraintGraph();

        Interval answer = new Interval(200, 300);
        Interval result = translateUnit.getReturnRange(funcitonName, arguments, true);
        System.out.println(translateUnit);
        assertEquals(answer, result);
    }

    /*
     * file: t3.c
     * input: k in [0, 10]
     *        N in [20, 50]
     * output: k in [20, 50]
     */
    @Test
    public void test3() {
        String fileName = "t" + 3;
        File file = new File(benchmarkPath + fileName + ".ssa");
        TranslateUnit translateUnit = TranslateUnitFactory.make(fileName, file);
        String funcitonName = "foo";
        List<Interval> arguments = new ArrayList<>();
        arguments.add(new Interval(0, 10));
        arguments.add(new Interval(20, 50));

        translateUnit.drawAllCFG();
        translateUnit.drawAllConstraintGraph();

        Interval answer = new Interval(20, 50);
        Interval result = translateUnit.getReturnRange(funcitonName, arguments, true);
        System.out.println(translateUnit);
        assertEquals(answer, result);
    }

    /*
     * file: t4.c
     * input: argc in [-inf, +inf]
     * output: k in [0, +inf]
     */
    @Test
    public void test4() {
        String fileName = "t" + 4;
        File file = new File(benchmarkPath + fileName + ".ssa");
        TranslateUnit translateUnit = TranslateUnitFactory.make(fileName, file);
        String funcitonName = "foo";
        List<Interval> arguments = new ArrayList<>();
        arguments.add(new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));

        translateUnit.drawAllCFG();
        translateUnit.drawAllConstraintGraph();

        Interval answer = new Interval(0, Double.POSITIVE_INFINITY);
        Interval result = translateUnit.getReturnRange(funcitonName, arguments, true);
        System.out.println(translateUnit);
        assertEquals(answer, result);
    }

    /*
     * file: t5.c
     * input:
     * output: ret in [210.0, 210.0]
     */
    @Test
    public void test5() {
        String fileName = "t" + 5;
        File file = new File(benchmarkPath + fileName + ".ssa");
        TranslateUnit translateUnit = TranslateUnitFactory.make(fileName, file);
        String funcitonName = "foo";
        List<Interval> arguments = new ArrayList<>();

        translateUnit.drawAllCFG();
        translateUnit.drawAllConstraintGraph();

        Interval answer = new Interval(210, 210);
        Interval result = translateUnit.getReturnRange(funcitonName, arguments, true);
        System.out.println(translateUnit);
        assertEquals(answer, result);
    }

    /*
     * file: t6.c
     * input: argc in [-inf, +inf]
     * output: sum in [-9, 10]
     */
    @Test
    public void test6() {
        String fileName = "t" + 6;
        File file = new File(benchmarkPath + fileName + ".ssa");
        TranslateUnit translateUnit = TranslateUnitFactory.make(fileName, file);
        String funcitonName = "foo";
        List<Interval> arguments = new ArrayList<>();
        arguments.add(new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));

        translateUnit.drawAllCFG();
        translateUnit.drawAllConstraintGraph();

        Interval answer = new Interval(-9, 10);
        Interval result = translateUnit.getReturnRange(funcitonName, arguments, true);
        System.out.println(translateUnit);
        assertEquals(answer, result);
    }

    /*
     * file: t7.c
     * input: i in [-10, 10]
     * output: k in [16, 30]
     */
    @Test
    public void test7() {
        String fileName = "t" + 7;
        File file = new File(benchmarkPath + fileName + ".ssa");
        TranslateUnit translateUnit = TranslateUnitFactory.make(fileName, file);
        String funcitonName = "foo";
        List<Interval> arguments = new ArrayList<>();
        arguments.add(new Interval(-10, 10));

        translateUnit.drawAllCFG();
        translateUnit.drawAllConstraintGraph();

        Interval answer = new Interval(16, 30);
        Interval result = translateUnit.getReturnRange(funcitonName, arguments, true);
        System.out.println(translateUnit);
        assertEquals(answer, result);
    }

    /*
     * file: t8.c
     * input: a in [1, 100]
     *        b in [-2, 2]
     * output: ret in [-3.2192308, 5.94230769]
     */
    @Test
    public void test8() {
        String fileName = "t" + 8;
        File file = new File(benchmarkPath + fileName + ".ssa");
        TranslateUnit translateUnit = TranslateUnitFactory.make(fileName, file);
        String funcitonName = "foo";
        List<Interval> arguments = new ArrayList<>();
        arguments.add(new Interval(1, 100));
        arguments.add(new Interval(-2, 2));

        translateUnit.drawAllCFG();
        translateUnit.drawAllConstraintGraph();

        Interval answer = new Interval(-3.2192308, 5.94230769);
        Interval result = translateUnit.getReturnRange(funcitonName, arguments, true);
        System.out.println(translateUnit);
        assertEquals(answer, result);
    }

    /*
     * file: t9.c
     * input:
     * output: sum in [9791, 9791]
     */
    @Test
    public void test9() {
        String fileName = "t" + 9;
        File file = new File(benchmarkPath + fileName + ".ssa");
        TranslateUnit translateUnit = TranslateUnitFactory.make(fileName, file);
        String funcitonName = "foo";
        List<Interval> arguments = new ArrayList<>();

        translateUnit.drawAllCFG();
        translateUnit.drawAllConstraintGraph();

        Interval answer = new Interval(9791, 9791);
        Interval result = translateUnit.getReturnRange(funcitonName, arguments, true);
        System.out.println(translateUnit);
        assertEquals(answer, result);
    }

    /*
     * file: t10.c
     * input: a in [30, 50]
     *        b in [90, 100]
     * output: j - i in [-10, 40]
     */
    @Test
    public void test10() {
        String fileName = "t" + 10;
        File file = new File(benchmarkPath + fileName + ".ssa");
        TranslateUnit translateUnit = TranslateUnitFactory.make(fileName, file);
        String funcitonName = "foo";
        List<Interval> arguments = new ArrayList<>();
        arguments.add(new Interval(30, 50));
        arguments.add(new Interval(90, 100));

        translateUnit.drawAllCFG();
        translateUnit.drawAllConstraintGraph();

        Interval answer = new Interval(-10, 40);
        Interval result = translateUnit.getReturnRange(funcitonName, arguments, true);
        System.out.println(translateUnit);
        assertEquals(answer, result);
    }

}
