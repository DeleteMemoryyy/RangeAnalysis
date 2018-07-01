package model.instructioin;

import java.util.List;

public class FunctionCall extends SingleExpression {
    private String simpleName;

    private List<SingleExpression> arguments;

    public FunctionCall(String name, String simpleName) {
        super(name);

        this.simpleName = simpleName;
    }

    public List<SingleExpression> getArguments() {
        return arguments;
    }

    public void setArguments(List<SingleExpression> arguments) {
        this.arguments = arguments;
    }
}
