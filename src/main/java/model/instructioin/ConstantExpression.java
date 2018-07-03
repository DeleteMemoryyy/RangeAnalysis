package model.instructioin;

public abstract class ConstantExpression extends SingleExpression {
    public ConstantExpression(String name) {
        super(name);
    }

    public abstract double doubleValue();
}
