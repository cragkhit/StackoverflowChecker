package exec;

/**
 * Created by Chaiyong on 9/3/16.
 */
public class RegExpTest {
    public static void main(String[] args) {
        // String s = "         result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );";
        String s = "     public int hashCode() {";
        // System.out.println(s.matches("\\s*result = prime \\* result \\+ ( ( id == null ) \\? 0 : id\\.hashCode() );"));
        System.out.println(s.matches("\\s*public\\s*int\\s*hashCode\\(\\)\\s*\\{\\s*"));
    }
}
