package util;

import model.Function;
import model.TranslateUnit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class TranslateUnitFactory {
    private static TranslateUnitFactory instance;


    private TranslateUnitFactory() {
    }


    public static TranslateUnit make(String name, File file) {
        if (instance == null)
            instance = new TranslateUnitFactory();

        return instance._make(name, file);
    }


    private TranslateUnit _make(String name, File file) {
        if (!file.exists())
            return null;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (!"".equals(line))
                    lines.add(line.trim());
            }

            TranslateUnit translateUnit = new TranslateUnit(name, file);
            translateUnit.setLines(lines);

            List<Function> functions = new ArrayList<>();
            FunctionFactory functionFactory = FunctionFactory.getFactory();
            functionFactory.init(translateUnit);
            while (functionFactory.hasNextFunction()) {
                Function function = functionFactory.make();
                if (function == null)
                    throw new NullPointerException("Resolve funciton error");
                functions.add(function);
            }
            translateUnit.setFunctions(functions);

            return translateUnit;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
