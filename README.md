# RangeAnalysis

This project is a part of Compiling Technique, Peking University. 

### Introduction

This pacage provides a simple implementation  to MiniC program range analysis method. 

All of the apporach is following this paper: [A Fast and Low-Overhead Technique to Secure Programs Against Integer Overflows](https://ieeexplore.ieee.org/stamp/stamp.jsp?tp=&arnumber=6494996)

### Dependency

- JRE or JDK >= 8.0

### Usage

######  Directly use jar package

~~~shell
cd RangeAnalysis
# First enter the directory where RangeAnalysis.jar is in. 

# java -jar RangeAnalysis.jar FILE FUNC [l1,u1] [l2,u2]
# There should not be any blank character inside every interval representation '[u,l]'.
# Positive infinity and negative infinity are represented as 'inf' and '-inf'

# Example with default cases:
java -jar RangeAnalysis.jar src/main/resources/benchmark/t1.ssa foo
java -jar RangeAnalysis.jar src/main/resources/benchmark/t2.ssa foo [200,300]
java -jar RangeAnalysis.jar src/main/resources/benchmark/t3.ssa foo [0,10] [20,50]
java -jar RangeAnalysis.jar src/main/resources/benchmark/t4.ssa foo [-inf,inf]
java -jar RangeAnalysis.jar src/main/resources/benchmark/t5.ssa foo
java -jar RangeAnalysis.jar src/main/resources/benchmark/t6.ssa foo [-inf,inf]
java -jar RangeAnalysis.jar src/main/resources/benchmark/t7.ssa foo [-10,10]
java -jar RangeAnalysis.jar src/main/resources/benchmark/t8.ssa foo [1,100] [-2,2]
java -jar RangeAnalysis.jar src/main/resources/benchmark/t9.ssa foo
java -jar RangeAnalysis.jar src/main/resources/benchmark/t10.ssa foo [30,50] [90,100]

~~~

###### Import as Intellij Idea project

Import the whole directory and project.iml file to Intellij Idea, then you can test with Junit test cases in MainTest.java.

~~~java
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
}
~~~

### Graph

> Several graphs will be shown in img/, like CFG, ConstraintGraph (uncomputed), ComstraintGraph (computed).
>
> If you want to generate your own image, please set graphviz bin path in DrawGraph.java and the rebuild the project.
>
> ~~~java
> public abstract class DrawGraph {
>     /**
>      * dot (graphviz) path
>      */
>     protected static String DOT_BIN_PATH = "dot"; // Change to your own graphviz/bin/dot path
> 	......
> }
> ~~~



###### CFG

![CFG](https://github.com/DeleteMemoryyy/RangeAnalysis/blob/master/img/t1_foo_CFG.png?raw=true)

###### ConstraintGraph

![ConstraintGraph](https://github.com/DeleteMemoryyy/RangeAnalysis/blob/master/img/t1_foo_final_ConstraintGraph.png?raw=true)

