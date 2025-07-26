public class Sum {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println(0);
            return;
        }
        int ans = 0;
        for (String arg : args) {
            int current_sign = 1;
            int current_number = 0;
            for (int i = 0; i < arg.length(); i++) {
                if (Character.isWhitespace(arg.charAt(i)) || arg.charAt(i) == '+')  {
                    ans += current_sign * current_number;
                    current_sign = 1;
                    current_number = 0;
                } else if (arg.charAt(i) == '-') {
                    ans += current_sign * current_number;
                    current_sign = -1;
                    current_number = 0;
                } else {
                    int TEN = 10;
                    current_number = current_number * TEN;
                    int last_number = arg.charAt(i) - '0';
                    current_number += last_number;
                }
            }
            ans += current_sign * current_number;
        }
        System.out.println(ans);
    }
}