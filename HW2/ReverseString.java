public class ReverseString {
    public static String reverse(String s) {
        int n = s.length();
        char[] arr = new char[n];
        for (int i = n; i > 0; i--)
            arr[n - i] = s.charAt(i - 1);
        return new String(arr);
    }
    public static void main(String[] args) {
        System.out.print(reverse(args[0]));
    }
}
