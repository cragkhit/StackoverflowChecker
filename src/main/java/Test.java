/**
 * Created by Chaiyong on 9/3/16.
 */
public class Test {
    public static void main(String[] args) {
        // String s = "         result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );";
        String s = "         return id;";
        // System.out.println(s.matches("\\s*result = prime \\* result \\+ ( ( id == null ) \\? 0 : id\\.hashCode() );"));
        System.out.println(s.matches("\\s*return\\s*[a-zA-Z0-9]*;"));
    }
}
