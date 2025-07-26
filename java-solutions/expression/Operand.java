package expression;

public abstract class Operand implements Expression, TripleExpression, DoubleMapExpression {

    public int hashCode() {
        return this.toString().hashCode();
    }

    public abstract boolean equals(Object operand1);
    public abstract int getpriority();
    public abstract boolean isassociative();
    public abstract boolean iscommutative();
}
