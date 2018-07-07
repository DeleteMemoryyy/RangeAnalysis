import model.TranslateUnit;
import util.TranslateUnitFactory;
import util.math.Interval;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RangeAnalysis {
    private static Pattern patternInterval = Pattern.compile("\\[([0-9a-zA-Z.+\\-]*),([0-9a-zA-Z.+\\-]*)\\]");

    public static void main(String[] args) {
        int size = args.length;
        if (size < 2) {
            System.out.println("Parameter error: ssa file path and funciton name need to be provided.");
            return;
        }

        String fileName = args[0];
        String funcName = args[1];
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("Parameter error: file does not exist.");
            return;
        }

        TranslateUnit translateUnit = TranslateUnitFactory.make(fileName, file);
        translateUnit.drawAllCFG();
        translateUnit.drawAllConstraintGraph();
        System.out.println(translateUnit);

        List<Interval> arguments = new ArrayList<>();
        for (int i = 2; i < size; ++i) {
            String intervalString = args[i];
            Matcher matcher = patternInterval.matcher(intervalString);
            if (!matcher.find()) {
                System.out.println("Parameter error: interval format error. Please try to do with [l,u].");
                return;
            } else {
                Double lower;
                if(matcher.group(1).equals("-inf"))
                    lower = Double.NEGATIVE_INFINITY;
                else if (matcher.group(1).equals("inf"))
                    lower = Double.POSITIVE_INFINITY;
                else
                    lower = Double.valueOf(matcher.group(1));

                Double upper;
                if(matcher.group(2).equals("-inf"))
                    upper = Double.NEGATIVE_INFINITY;
                else if (matcher.group(2).equals("inf"))
                    upper = Double.POSITIVE_INFINITY;
                else
                    upper = Double.valueOf(matcher.group(2));

                if (upper == null || upper == null) {
                    System.out.println("Parameter error: number format error.");
                    return;
                }

                arguments.add(new Interval(lower, upper));
            }
        }

        Interval result = translateUnit.getReturnRange(funcName, arguments, true);
        System.out.println("Result: " + result);
    }
}
