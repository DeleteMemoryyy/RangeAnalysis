package util.math;

public class Interval {
    public double lower;
    public double upper;

    public Interval() {
        lower = Double.NEGATIVE_INFINITY;
        upper = Double.POSITIVE_INFINITY;
    }

    public Interval(double lower, double upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public static Interval intersection(Interval i1, Interval i2) {
        if ((i1.lower <= i2.lower) && (i2.lower <= i1.upper))
            return new Interval(Double.max(i1.lower, i2.lower), Double.min(i1.upper, i2.upper));
        else if ((i2.lower <= i1.lower) && (i1.lower <= i2.upper))
            return new Interval(Double.max(i1.lower, i2.lower), Double.min(i1.upper, i2.upper));
        return null;
    }

    public static Interval union(Interval i1, Interval i2) {
        return new Interval(Double.min(i1.lower, i2.lower), Double.max(i1.upper, i2.upper));
    }

    public static Interval plus(Interval i1, Interval i2) {
        return new Interval(i1.lower + i2.lower, i1.upper + i2.upper);
    }

    public static Interval plus(Interval i, double d) {
        return new Interval(i.lower + d, i.upper + d);
    }

    public static Interval plus(double d, Interval i) {
        return plus(i, d);
    }

    public static Interval minus(Interval i1, Interval i2) {
        return new Interval(i1.lower - i2.upper, i1.upper - i2.lower);
    }

    public static Interval minus(Interval i, double d) {
        return plus(i, -d);
    }

    public static Interval minus(double d, Interval i) {
        return minus(new Interval(d, d), i);
    }

    public static Interval time(Interval i1, Interval i2) {
        double[] e = new double[4];
        e[0] = i1.lower * i2.lower;
        e[1] = i1.upper * i2.lower;
        e[2] = i1.lower * i2.upper;
        e[3] = i1.upper * i2.upper;

        double eMin = e[0];
        double eMax = e[0];
        for (double eNumber : e) {
            if (eNumber < eMin)
                eMin = eNumber;
            else if (eNumber > eMax)
                eMax = eNumber;
        }

        return new Interval(eMin, eMax);
    }

    public static Interval time(Interval i, double d) {
        double e1 = i.lower * d;
        double e2 = i.upper * d;

        return new Interval(Double.min(e1, e2), Double.max(e1, e2));
    }

    public static Interval time(double d, Interval i) {
        return time(i, d);
    }

    public static Interval divide(Interval i1, Interval i2) {
        double[] e = new double[4];
        e[0] = i1.lower / i2.lower;
        e[1] = i1.upper / i2.lower;
        e[2] = i1.lower / i2.upper;
        e[3] = i1.upper / i2.upper;

        double eMin = e[0];
        double eMax = e[0];
        for (double eNumber : e) {
            if (eNumber < eMin)
                eMin = eNumber;
            else if (eNumber > eMax)
                eMax = eNumber;
        }

        return new Interval(eMin, eMax);
    }

    public static Interval divide(Interval i, double d) {
        double e1 = i.lower / d;
        double e2 = i.upper / d;

        return new Interval(Double.min(e1, e2), Double.max(e1, e2));
    }

    public static Interval divide(double d, Interval i) {
        return divide(new Interval(d, d), i);
    }

    @Override
    public String toString() {
        return "[" + lower + ", " + upper + "]";

    }
}
