package model.instructioin;

public class ConvertExpression extends SingleExpression {
    String toType;
    Expression expr;

    public ConvertExpression(String name, String toType, Expression expr) {
        super(name);

        this.toType = toType;
        this.expr = expr;
    }
}
