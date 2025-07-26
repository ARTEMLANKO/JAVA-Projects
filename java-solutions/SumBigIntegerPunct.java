import java.math.BigInteger;

public class SumBigIntegerPunct {

    public static void main(String[] args) {
        BigInteger result = BigInteger.ZERO;
        for (String arg : args) {
            arg += " ";
            //System.err.println(arg);
            StringBuilder currentNumber = new StringBuilder();
            for (int i = 0; i < arg.length(); i++) {
                if (Character.getType(arg.charAt(i)) == Character.START_PUNCTUATION || Character.getType(arg.charAt(i)) == Character.END_PUNCTUATION || Character.isWhitespace(arg.charAt(i))) {
                    if (!currentNumber.isEmpty()) {
                        result = result.add(new BigInteger(currentNumber.toString()));
                        currentNumber.setLength(0);
                    }
                } else {
                    currentNumber.append(arg.charAt(i));
                }
            }
        }
        //System.err.println(result);
        System.out.println(result);
    }
}
