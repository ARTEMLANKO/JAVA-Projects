package expression;

import java.util.Map;
import java.util.NoSuchElementException;

public class Variable extends Operand {
    String name;
    public Variable(String s1) {
        this.name = s1;
    }

    public int evaluate(int x) {
        return x;
    }

    public boolean equals(Object oper1) {
        if (oper1 != null && getClass() == oper1.getClass()) {
            Variable oper3 = (Variable) oper1;
            return this.name.equals(oper3.name);
        }
        return false;
    }
    public int evaluate(int x, int y, int z) throws NoSuchElementException {
       if (name.charAt(name.length() - 1) == 'x') {
           return x;
       } else if (name.charAt(name.length() - 1) == 'y') {
           return y;
       } else if (name.charAt(name.length() - 1) == 'z') {
           return z;
       }
       throw new NoSuchElementException("It is not x, y, z");
    }

    public double evaluateD(Map<String, Double> dict) throws NoSuchElementException {
        if (dict.get(name) == null) {
            throw new NoSuchElementException("no value in map");
        } else {
            return dict.get(name);
        }
    }

    public String toString() {
        return name;
    }

    public int getpriority() {
        return -100;
    }

    public boolean isassociative() {
        return true;
    }

    public boolean iscommutative() {
        return true;
    }
}
