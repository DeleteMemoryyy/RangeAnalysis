package model.instructioin;

public class SingleVariable extends SingleExpression {
    private String simpleName;
    private int version;

    public SingleVariable(String name, String simpleName, int version) {
        super(name);

        this.simpleName = simpleName;
        this.version = version;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public int getVersion() {
        return version;
    }

}
