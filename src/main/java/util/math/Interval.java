package util.math;

public class Interval {
    public ENumber lower;
    public ENumber upper;

    public Interval() {
        lower = new ENumber(-1);
        upper = new ENumber(1);
    }

    public Interval(ENumber lower, ENumber upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public Interval(Interval i) {
        lower = new ENumber(i.lower);
        upper = new ENumber(i.upper);
    }

    public static Interval intersection(Interval i1, Interval i2) {
        if (ENumber.le(i1.lower, i2.lower) && ENumber.le(i2.lower, i1.upper)) {
            return new Interval(ENumber.max(i1.lower, i2.lower), ENumber.min(i1.upper, i2.upper));
        } else if (ENumber.le(i2.lower, i1.lower) && ENumber.le(i1.lower, i2.upper)) {
            return new Interval(ENumber.max(i1.lower, i2.lower), ENumber.min(i1.upper, i2.upper));
        }
        return null;
    }

    public static Interval union(Interval i1, Interval i2) {
        return new Interval(ENumber.min(i1.lower, i2.lower), ENumber.max(i1.upper, i2.upper));
    }

    public static Interval plus(Interval i1, Interval i2) {
        return new Interval(ENumber.plus(i1.lower, i2.lower), ENumber.plus(i1.upper, i2.upper));
    }

    public static Interval plus(Interval i, double d) {
        return new Interval(i.lower.plus(d), i.upper.plus(d));
    }

    public static Interval plus(double d, Interval i) {
        return plus(i, d);
    }

    public static Interval minus(Interval i1, Interval i2) {
        return new Interval(ENumber.minus(i1.lower, i2.upper), ENumber.max(i1.upper, i2.lower));
    }

    public static Interval minus(Interval i, double d) {
        return plus(i, -d);
    }

    public static Interval minus(double d, Interval i) {
        return minus(new Interval(new ENumber(d), new ENumber(d)), i);
    }

    public static Interval time(Interval i1, Interval i2) {

        ENumber[] e = new ENumber[4];
        e[0] = ENumber.time(i1.lower, i2.lower);
        e[1] = ENumber.time(i1.upper, i2.lower);
        e[2] = ENumber.time(i1.lower, i2.upper);
        e[3] = ENumber.time(i1.upper, i2.upper);

        ENumber eMin = e[0];
        ENumber eMax = e[0];
        for (ENumber eNumber : e) {
            if (ENumber.lt(eNumber, eMin))
                eMin = eNumber;
            else if (ENumber.gt(eNumber, eMax))
                eMax = eNumber;
        }

        return new Interval(eMin, eMax);
    }

    public static Interval time(Interval i, double d) {
        ENumber e1 = i.lower.time(d);
        ENumber e2 = i.upper.time(d);

        return new Interval(ENumber.min(e1, e2), ENumber.max(e1, e2));
    }

    public static Interval time(double d, Interval i) {
        return time(i, d);
    }

    public static Interval divide(Interval i1, Interval i2) {
        ENumber[] e = new ENumber[4];
        e[0] = ENumber.divide(i1.lower, i2.lower);
        e[1] = ENumber.divide(i1.upper, i2.lower);
        e[2] = ENumber.divide(i1.lower, i2.upper);
        e[3] = ENumber.divide(i1.upper, i2.upper);

        ENumber eMin = e[0];
        ENumber eMax = e[0];
        for (ENumber eNumber : e) {
            if (ENumber.lt(eNumber, eMin))
                eMin = eNumber;
            else if (ENumber.gt(eNumber, eMax))
                eMax = eNumber;
        }

        return new Interval(eMin, eMax);
    }

    public static Interval divide(Interval i, double d) {
        ENumber e1 = i.lower.divide(d);
        ENumber e2 = i.upper.divide(d);

        return new Interval(ENumber.min(e1, e2), ENumber.max(e1, e2));
    }

    public static Interval divide(double d, Interval i) {
        return divide(new Interval(new ENumber(d), new ENumber(d)), i);
    }

    @Override
    public String toString() {
        return "[" + lower + ", " + upper + "]";
    }
}
