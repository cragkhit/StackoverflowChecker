/**
 * Created by Chaiyong on 9/3/16.
 */
public class Test {
    public static void main(String[] args) {
        String s = "     public String getFirstName() {";
        System.out.println(s.matches("\\s*public.*get[A-Z].*().*\\{"));
    }
}
