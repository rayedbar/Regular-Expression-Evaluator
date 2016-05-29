package regexpeval;

/**
 * Created by moham on 17-Feb-16.
 */
public class ParseNode {

    public NodeType nodeType;
    public char data;
    public ParseNode leftNode;
    public ParseNode rightNode;

    public ParseNode(NodeType nodeType, char data, ParseNode leftNode, ParseNode rightNode){
        this.nodeType = nodeType;
        this.data = data;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

}
