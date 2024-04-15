public class eOccurences {
    public static int count_e(String s) {
        int res = 0;
        for (int i = 0; i < s.length(); i++)
            if (s.charAt(i) == 'e')
                res++;
        return res;
    }
    public static void main(String[] args) {
        System.out.print(count_e(args[0]));
    }
}
