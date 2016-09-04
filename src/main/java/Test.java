/**
 * Created by Chaiyong on 9/3/16.
 */
public class Test {
    public static void main(String[] args) {
        String s = "        return id; ";
        System.out.println(s.matches("\\s*return\\s*.*;.*"));
    }
}
