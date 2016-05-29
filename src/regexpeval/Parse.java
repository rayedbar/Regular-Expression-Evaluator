package regexpeval;

/**
 * Created by moham on 17-Feb-16.
 */
public class Parse {

    private regexpeval.Scan myScanner;

    public Parse(regexpeval.Scan myScanner){
        this.myScanner = myScanner;
    }

    public ParseNode creatTree() {
        ParseNode root = expr();
        //printTree(root, 0);
        return root;
    }

    // expr ::= concat '|' expr
    //      |   concat
    //
    private ParseNode expr() {
        ParseNode left = concat();
        if (myScanner.peek() == '|'){
            myScanner.pop();
            ParseNode right = expr();
            ParseNode exprNode = new ParseNode(NodeType.ALTER, '0', left, right);
            return exprNode;
        } else {
            return left;
        }
    }

    // concat   ::= rep . concat
    //          |   rep
    //
    private ParseNode concat() {
        ParseNode left = rep();
        if (myScanner.peek() == '.'){
            myScanner.pop();
            ParseNode right = concat();
            ParseNode concatNode = new ParseNode(NodeType.CONCAT, '0', left, right);
            return concatNode;
        } else {
            return left;
        }
    }

    // rep  ::= atom '*'
    //      |   atom '?'
    //      |   atom
    //
    private ParseNode rep() {
        ParseNode atomNode = atom();
        ParseNode repNode;
        switch (myScanner.peek()){
            case '*':
                myScanner.pop();
                repNode = new ParseNode(NodeType.STAR, '0', atomNode, null);
                return  repNode;
            case '?':
                myScanner.pop();
                repNode = new ParseNode(NodeType.QUESTION, '0', atomNode, null);
                return repNode;
            default:
                return atomNode;    
        }
//        if (myScanner.peek() == '*'){
//            myScanner.pop();
//            ParseNode repNode = new ParseNode(NodeType.STAR, '0', atomNode, null);
//            return  repNode;
//        } else if (myScanner.peek() == '?'){
//            
//        } else {
//            return atomNode;
//        }
    }

    // atom ::= chr
    //      |   '(' expr ')'
    //
    private ParseNode atom() {
        ParseNode atomNode = null;
        if (myScanner.peek() == '('){
            myScanner.pop();
            atomNode = expr();
            if (myScanner.pop() != ')'){
                System.err.println("Parse Error: Expected ')'");
                Runtime.getRuntime().exit(0);
            }
        } else {
            atomNode = chr();
        }
        return  atomNode;
    }

    private ParseNode chr() {
        char value = myScanner.peek();
        if (Character.isAlphabetic(value) || value == '0'){
            return new ParseNode(NodeType.CHR, myScanner.pop(), null, null);
        }
        System.err.println("Parse Erro: Expected Alphabetic, got " + myScanner.peek() + "at position" + myScanner.getPos());
        Runtime.getRuntime().exit(0);
        return null;
    }

    private static void printTree(ParseNode node, int offset) {
//        for (int i = 0; i < offset; i++) {
//            System.out.print(" ");
//        }
        if (node != null){
            printTree(node.leftNode, 0);
            printTree(node.rightNode, 0);
            //System.out.print(node.data);
            switch (node.nodeType){
                case CHR:
                    System.out.print(node.data);
                    break;
                case ALTER:
                    System.out.print("|");
                    break;
                case CONCAT:
                    System.out.print(".");
                    break;
                case STAR:
                    System.out.print("*");
                    break;
                case QUESTION:
                    System.out.println("?");
                    break;
            }
//            System.out.println();
//            printTree(node.leftNode, offset + 0);
//            printTree(node.rightNode, offset + 0);
            //System.out.print(node.data);
        }
    }

    public Nfa treeToNfa(ParseNode tree){
        switch (tree.nodeType){
            case CHR:
                return NfaBuilder.build_nfa_basic(tree.data);
            case ALTER:
                return NfaBuilder.build_nfa_alter(treeToNfa(tree.leftNode), treeToNfa(tree.rightNode));
            case CONCAT:
                return NfaBuilder.build_nfa_concat(treeToNfa(tree.leftNode), treeToNfa(tree.rightNode));
            case STAR:;
                return NfaBuilder.build_nfa_star(treeToNfa(tree.leftNode));
            case QUESTION:
                return NfaBuilder.build_nfa_alter(treeToNfa(tree.leftNode), NfaBuilder.build_nfa_basic(RegexConstants.EPS));
            default:
                return null;
        }
    }

}
