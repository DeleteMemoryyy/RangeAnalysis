package model.instructioin;

public class GotoExpression extends Expression {
    private String gotoBlockId;

    public GotoExpression(String name, String gotoBlockId) {
        super(name);

        this.gotoBlockId = gotoBlockId;
    }

    public String getGotoBlockId() {
        return gotoBlockId;
    }
}
