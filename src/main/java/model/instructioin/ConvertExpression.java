package model.instructioin;

public class ConvertExpression extends SingleExpression {
    private String toType;
    private SingleVariable singleVariable;

    public ConvertExpression(String name, String toType, SingleVariable singleVariable) {
        super(name);

        this.toType = toType;
        this.singleVariable = singleVariable;
    }

    public String getToType() {
        return toType;
    }

    public SingleVariable getSingleVariable() {
        return singleVariable;
    }

}
