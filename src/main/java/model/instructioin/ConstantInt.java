package model.instructioin;

public class ConstantInt extends ConstantExpression {
    private int intValue;

    public ConstantInt(String name) {
        super(name);

        intValue = Integer.valueOf(name);
    }

    public int getIntValue() {
        return intValue;
    }

    @Override
    public double doubleValue() {
        return intValue;
    }

}
