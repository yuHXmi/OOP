public class CircularShift {
    public static boolean isCircularShift(String s, String t) {
        String s2 = s + s;
        return s.length() == t.length() && s2.contains(t);
    }
    public static void main(String[] args) {
        if (isCircularShift(args[0], args[1]))
            System.out.print("They're circular shift!");
        else
            System.out.print("They're not circular shift!");
    }
}
