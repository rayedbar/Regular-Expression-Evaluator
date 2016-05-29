package regexpeval;

/**
 * Created by moham on 17-Feb-16.
 */
public class Scan {

    private int next;
    private String regex;

    public Scan(String s){
        regex = s;
        next = 0;
        preprocess();
    }

    private void preprocess() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < regex.length() - 1; i++) {
            char current = regex.charAt(i);
            char nextt = regex.charAt(i + 1);
            stringBuilder.append(current);
            if ((Character.isAlphabetic(current) || current == ')' || current == '*' || current == '?') &&
                    (nextt != ')' && nextt != '|' && nextt != '*' && nextt != '?' )){
                stringBuilder.append('.');
            }
        }
        stringBuilder.append(regex.charAt(regex.length() - 1));
        regex = stringBuilder.toString();
        System.out.println(regex);
    }

    public char peek(){
        return (next < regex.length()) ? regex.charAt(next) : '0';
    }

    public char pop(){
        char current = peek();
        if (next < regex.length()){
            next++;
        }
        return current;
    }

    public int getPos(){
        return next;
    }
}
