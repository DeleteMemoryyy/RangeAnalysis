package model.instructioin;

public class ConstantFloat extends ConstantExpression {
    private float factor;
    private int exponent;
    private float floatValue;

    public ConstantFloat(String name, float factor, int exponent) {
        super(name);

        this.factor = factor;
        this.exponent = exponent;
        floatValue = factor * (float) Math.pow(10.0, exponent);
    }

    public float getFactor() {
        return factor;
    }

    public int getExponent() {
        return exponent;
    }

    public float getFloatValue() {
        return floatValue;
    }

    @Override
    public double doubleValue() {
        return floatValue;
    }
}
