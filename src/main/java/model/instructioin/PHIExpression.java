package model.instructioin;

public class PHIExpression  extends Expression{
    private SingleVariable leftExpr;
    private SingleExpression fromExpr_1;
    private  SingleExpression fromExpr_2;
    private String fromBlockId_1;
    private String fromBlockId_2;

    public PHIExpression(String name, SingleVariable leftExpr, SingleExpression fromExpr_1, SingleExpression fromExpr_2,int fromBlockId_1,int fromBlockId_2) {
        super(name);

        this.leftExpr = leftExpr;
        this.fromExpr_1 = fromExpr_1;
        this.fromExpr_2 = fromExpr_2;
        this.fromBlockId_1 = "bb " + fromBlockId_1;
        this.fromBlockId_2 = "bb " + fromBlockId_2;
    }

    public SingleVariable getLeftExpr() {
        return leftExpr;
    }

    public SingleExpression getFromExpr_1() {
        return fromExpr_1;
    }

    public SingleExpression getFromExpr_2() {
        return fromExpr_2;
    }

    public String getfromBlockId_1() {
        return fromBlockId_1;
    }

    public String getfromBlockId_2() {
        return fromBlockId_2;
    }

}
