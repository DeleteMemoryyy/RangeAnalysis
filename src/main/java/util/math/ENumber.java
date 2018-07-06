package util.math;

@Deprecated
public class ENumber {
    public int value;
    /**
     * 0 for normal number
     * 1 for inf
     * -1 for -inf
     */
    public int inf;

    public ENumber(int value, int inf) {
        this.value = value;
        this.inf = inf;
    }

    public ENumber(ENumber e) {
        value = e.value;
        inf = e.inf;
    }

    public ENumber plus(int d) {
        if (inf != 0)
            return new ENumber(0, inf);
        return new ENumber(value + d, 0);
    }

    public static ENumber plus(ENumber e1, ENumber e2) {
        ENumber nInfE;
        ENumber e;

        if (e1.inf != 0) {
            if (e2.inf != 0) {
                if (e1.inf == e2.inf)
                    return new ENumber(0, e1.inf);
                else
                    return null;
            } else {
                nInfE = e2;
                e = e1;
            }
        } else {
            nInfE = e1;
            e = e1;
        }
        return nInfE.plus(e.value);
    }

    public ENumber minus(int d) {
        if (inf != 0)
            return new ENumber(0, -inf);
        return new ENumber(value + d, 0);
    }

    public static ENumber minus(ENumber e1, ENumber e2) {
        ENumber nInfE;
        ENumber e;

        if (e1.inf != 0) {
            if (e2.inf != 0) {
                if (e1.inf != e2.inf)
                    return new ENumber(0, e1.inf);
                else
                    return null;
            } else {
                nInfE = e2;
                e = e1;
            }
        } else {
            nInfE = e1;
            e = e1;
        }
        return nInfE.minus(e.value);
    }

    public ENumber time(int d) {
        if (inf != 0) {
            if (d > 0)
                return new ENumber(0, inf);
            else if (d < 0)
                return new ENumber(0, -inf);
            return new ENumber(0, 0);
        }
        return new ENumber(value * d, 0);
    }

    public static ENumber time(ENumber e1, ENumber e2) {
        ENumber nInfE;
        ENumber e;

        if (e1.inf != 0) {
            if (e2.inf != 0)
                return new ENumber(0, e1.inf * e2.inf);
            else {
                nInfE = e2;
                e = e1;
            }
        } else {
            nInfE = e1;
            e = e1;
        }
        return nInfE.time(e.value);
    }

    public ENumber divide(int d) {
        if (d == 0)
            return null;
        if (inf != 0) {
            if (d > 0)
                return new ENumber(0, inf);
            else if (d < 0)
                return new ENumber(0, -inf);
            return null;
        }
        return new ENumber(value / d, 0);
    }

    public static ENumber divide(ENumber e1, ENumber e2) {
        ENumber nInfE;
        ENumber e;

        if (e1.inf != 0) {
            if (e2.inf != 0)
                return new ENumber(0, e1.inf * e2.inf);
            else {
                nInfE = e2;
                e = e1;
            }
        } else {
            nInfE = e1;
            e = e1;
        }
        return nInfE.divide(e.value);
    }

    public boolean lt(int d) {
        if (inf == 0)
            return value < d;
        else if (inf == -1)
            return true;
        else
            return false;
    }

    public static boolean lt(ENumber e1, ENumber e2) {
        if (e1.inf == -1) {
            if (e2.inf == -1)
                return false;
            return true;
        } else if (e1.inf == 1) {
            return false;
        }
        return e2.lt(e1.value);
    }

    public boolean gt(int d) {
        if (inf == 0)
            return value > d;
        else if (inf == -1)
            return false;
        else
            return true;
    }

    public static boolean gt(ENumber e1, ENumber e2) {
        if (e1.inf == -1) {
            return false;
        } else if (e1.inf == 1) {
            if (e2.inf == 1)
                return false;
            return true;
        }
        return e2.gt(e1.value);
    }

    public boolean le(int d) {
        return !gt(d);
    }

    public static boolean le(ENumber e1, ENumber e2) {
        return !gt(e1, e2);
    }

    public boolean ge(int d) {
        return !lt(d);
    }

    public static boolean ge(ENumber e1, ENumber e2) {
        return !lt(e1, e2);
    }

    public boolean equals(int d) {
        if (inf == 0)
            return value == d;
        else
            return false;
    }

    public static ENumber min(ENumber e1, ENumber e2) {
        if (gt(e1, e2))
            return new ENumber(e2);

        return new ENumber(e1);
    }

    public static ENumber max(ENumber e1, ENumber e2) {
        if (lt(e1, e2))
            return new ENumber(e2);

        return new ENumber(e1);
    }

    @Override
    public String toString() {
        if (inf == -1)
            return "-inf";
        else if (inf == 1)
            return "inf";
        return Double.toString(value);
    }

}
