public class DNA {
    static public boolean isValidDNA(String s) {
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch != 'A' && ch != 'T' && ch != 'C' && ch != 'G')
                return false;
        }
        return true;
    }
    public static String complementWatsonCrick(String s) {
        s = s.replaceAll("A", "t");
        s = s.replaceAll("T", "a");
        s = s.replaceAll("C", "g");
        s = s.replaceAll("G", "c");
        return s.toUpperCase();
    }
    static public void main(String[] args) {
        if (isValidDNA(args[0]) == true) {
            System.out.println("It's valid!");
            System.out.println("Watson–Crick complement: " + complementWatsonCrick(args[0]));
        }
        else
            System.out.print("Not valid!");
    }
}
