package regexpeval;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Enter Regular Expression");
            String regex = sc.nextLine();
            System.out.println("Enter String");
            String input = sc.nextLine();
            Scan myScanner = new Scan(regex);
            Parse parse = new Parse(myScanner);
            ParseNode tree = parse.creatTree();
            Nfa nfa = parse.treeToNfa(tree);
            nfa.show();
            nfa.showMatrix();
            Dfa dfa = new Dfa(nfa);
            dfa.subset_construct();
            dfa.print();
            dfa.verify(input);
            System.out.println("Enter y to continue and n to close application");
            char ch = sc.nextLine().charAt(0);
            if (ch == 'n'){
                break;
            } 
        }

    }

}
