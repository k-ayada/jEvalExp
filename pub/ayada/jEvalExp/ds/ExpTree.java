package pub.ayada.jEvalExp.ds;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import java.util.TreeMap;
import java.util.Map.Entry;

import pub.ayada.genutils.string.StringUtils;
import pub.ayada.dataStructures.chararray.CharArr;


import pub.ayada.jEvalExp.ds.node.Node;
import pub.ayada.jEvalExp.ds.node.NodeTypeEnum;
import pub.ayada.jEvalExp.ds.token.Token;
import pub.ayada.jEvalExp.ds.token.TokenTypeEnum;

import pub.ayada.jEvalExp.funcs.Func;
import pub.ayada.jEvalExp.opers.Operator;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ExpTree implements Cloneable, Serializable {
	private static final long serialVersionUID = -2542217747475855407L;

	private TreeMap<String, Node> nodesMap = new TreeMap<String, Node>();
	private Node root;
	private Node firstLeaf;
	private HashMap<String, Token<?>> variables = new HashMap<String, Token<?>>();
	private char enclosureChar = '"', escapeChar = '\\';
	private int nodeCount = 1, closeBrakets = 0, openBrakets = 0;
	private boolean init = false;
	
	private char prevNodeType = '\0';

	public ExpTree() {
		super();
	}

	public ExpTree(String ExprString) throws Exception {
		this(ExprString, '"', '\\');
	}

	public ExpTree(String ExprString, char EnclosureChar, char EscapeChar)
			throws Exception {
		this.enclosureChar = EnclosureChar;
		this.escapeChar = EscapeChar;
		this.root = createBTreeNodes(StringUtils.RemoveWhitSpaceChars(ExprString, EnclosureChar, EscapeChar));
		setFirstLeaf(reachtoFirstLeaf(this.root));
		assignParents(this.root);
		this.init = true;
	}	
	
	public boolean isBuilt() {
		  return this.init; 
		//return (this.root == null)?  false :  true;
	}
	
	public String getVariableList() {
		Iterator<Entry<String, Token<?>>> it = this.variables.entrySet().iterator();
		StringBuilder sb = new StringBuilder("\n\t{");
		while (it.hasNext()) {
			Entry<String, Token<?>> pair = it.next();
			sb.append("[\"" + pair.getKey() + "\" = " );
			
			sb.append(pair.getValue().getValue()  != null?  pair.getValue().toString().replaceAll("\n", "\\\\n") + " (Type:"
					+ pair.getValue().getTokenType() + ")]," : "(null) ]" );
		}
		sb.setLength(sb.length() - 1);
		if (sb.length() > 3 )
			sb.append("}\n");	
		return sb.toString();
	}

	public TreeMap<String, Node> getNodenMap() {
		return this.nodesMap;
	}
	public void clearVarValues() {
		Iterator<Entry<String, Token<?>>> it = this.variables.entrySet().iterator();
		while (it.hasNext()) {
			it.next().setValue(null);
		}		
	}
	
	public void setNodenMap(TreeMap<String, Node> nodenMap) {
		this.nodesMap = nodenMap;
	}

	public void addToNodeMap(String key, Node value) {
		this.nodesMap.put(key, value);
	}

	public void updateNodeMapValue(String key, Node value) throws RuntimeException {
		if (this.nodesMap.containsKey(key))
			addToNodeMap(key, value);
		else
			throw new RuntimeException("Key '" + key + " not found to update the NodeMap");
	}

	public Node getRoot() {
		return this.root;
	}

	public Node getFirstLeaf() {
		return this.firstLeaf;
	}

	public void setFirstLeaf(Node firstLeaf) {
		this.firstLeaf = firstLeaf;
	}

	public void assignParents() {
		assignParents(this.root);
	}

	public void setVarValue(String VariableName, Object Value) throws RuntimeException {

		if (!this.variables.containsKey(VariableName))
			this.variables.put(VariableName, new Token(TokenTypeEnum.UNKN_DT, null));
		
		Token<?> varVal = this.variables.get(VariableName);

		switch (varVal.getTokenType()) {
		case INT:
			this.variables.get(VariableName).setValue(new Long (((Number) Value).longValue()));
			break;
		case DEC:
			this.variables.get(VariableName).setValue(new Double(((Number) Value).doubleValue()));
			break;
		case BOOL:
			this.variables.get(VariableName).setValue((Boolean) Value);
			break;
		case DATE:
			this.variables.get(VariableName).setValue((java.util.Date) Value);
			break;
		case CHR:
			this.variables.get(VariableName).setValue((Character) Value);
			break;
		case STR:
			this.variables.get(VariableName).setValue((String) Value);
			break;
		case UNKN_DT:
			if (Value == null) {
				this.variables.get(VariableName).setTokenTyp(TokenTypeEnum.NULL);
				this.variables.get(VariableName).setValue(null);
			} else {
				String dt = Value.getClass().getName();
				switch (dt.toLowerCase()) {
				case "java.lang.long":
					this.variables.get(VariableName).setTokenTyp(TokenTypeEnum.INT);
					this.variables.get(VariableName).setValue(Value);
					break;
				case "java.lang.integer":  
					this.variables.get(VariableName).setTokenTyp(TokenTypeEnum.INT);
					this.variables.get(VariableName).setValue(((Number)Value).longValue());									
					break;
				case "java.lang.float":
					this.variables.get(VariableName).setTokenTyp(TokenTypeEnum.DEC);
					this.variables.get(VariableName).setValue(((Number)Value).longValue());
				case "java.lang.double":
					this.variables.get(VariableName).setTokenTyp(TokenTypeEnum.DEC);
					this.variables.get(VariableName).setValue(Value);
					break;
				case "java.lang.string" :
					this.variables.get(VariableName).setTokenTyp(TokenTypeEnum.STR);
					this.variables.get(VariableName).setValue((String)Value);
					break;
				default:
					throw new RuntimeException(
						"Failed to determine the datatype for '$" + VariableName + "'. type found: " + dt);
				}
			}	
			break;
		default:
			throw new RuntimeException(
					"Failed to set the value of variable '$" + VariableName + "' in " + getVariableList());
		}

	}

	private void assignParents(Node curRoot) {
		
		if (curRoot.getChildren().isEmpty())
			return; 
		
		Node lChild = curRoot.getLeftChild();
		Node rChild = curRoot.getRightChild();
		if (lChild != null) {
			lChild.setParent(curRoot);
			if (!lChild.isLeafNode())
				assignParents(lChild);
		}
		if (rChild != null) {
			rChild.setParent(curRoot);
			if (!rChild.isLeafNode())
				assignParents(rChild);
		}
	}

	public void reportTtreeStructure() {
		assignParents();
		System.out.println(" Tree Structure :");
		for (Entry<String, Node> entry : getNodenMap().entrySet()) {
			
			System.out.println("\tKey: " + entry.getKey() + "--> '" + entry.getValue().getData().getValue().toString()
			          + "'\t (type: " + entry.getValue().getData().getTokenType().toString() + ").");			
			
			if (entry.getValue().isLeafNode()) {				
				System.out.println("\t\t No children. This is a leaf Node.");
			}else {				
				switch(entry.getValue().getNodeType()) {				

				case FUNCTION_NO_ARG:
				break;
			    case UNARY_OPER:
				case FUNCTION_1_ARG:
						System.out.println("\t\tChild      : "
						+ (   entry.getValue().getLeftChild().getNodeType() 
						   == NodeTypeEnum.VARIABLE
						     ?   "Variable: " + entry.getValue().getLeftChild().getData().getValue()
							   + "  (type:"  + entry.getValue().getLeftChild().getData().getTokenType() + ")."
						     :   "'" + entry.getValue().getLeftChild().getData().getValue().toString().replaceAll("\n", "\\\\n")
							   + "'\t(type:" + entry.getValue().getLeftChild().getData().getTokenType().toString()+ ").")
						);
				break;		
				case BINARY_OPER:
				case FUNCTION_2_ARG:
					System.out.println("\t\tLeftChild  : " +
						(   entry.getValue().getLeftChild().getNodeType() 
						 == NodeTypeEnum.VARIABLE
					      ?   "Variable: " + entry.getValue().getLeftChild().getData().getValue()
						   + "  (type:"  + entry.getValue().getLeftChild().getData().getTokenType() + ")."
					      :   "'" + entry.getValue().getLeftChild().getData().getValue().toString().replaceAll("\n", "\\\\n")
						   + "'\t(type:" + entry.getValue().getLeftChild().getData().getTokenType()+ ").")
					);	
					System.out.println("\t\tRightChild : " +
							(   entry.getValue().getRightChild().getNodeType() 
									 == NodeTypeEnum.VARIABLE
								      ?   "Variable: " + entry.getValue().getRightChild().getData().getValue()
									   + "  (type:"  + entry.getValue().getRightChild().getData().getTokenType() + ")."
								      :   "'" + entry.getValue().getRightChild().getData().getValue().toString().replaceAll("\n", "\\\\n")
									   + "'\t(type:" + entry.getValue().getRightChild().getData().getTokenType().toString()+ ").")
				   );
				

				break;
				case FUNCTION_N_ARG:
				case TERNARY_OPER:
					for (int i = 0; i < entry.getValue().getChildren().size(); i++) {
						Node x = entry.getValue().getChildren().get(i) ;
						
						System.out.println("\t\tChild"+(i+1) +"   : " +
						     (x.getNodeType() == NodeTypeEnum.VARIABLE
						      ?  "Variable: " + x.getData().getValue()						        
						      : "'" + (x.getData().getValue() == null? "null" : x.getData().getValue().toString().replaceAll("\n", "\\\\n"))+ "'"    
						     )+  "  (type:"  + x.getData().getTokenType()+	").");						
					}					
				break;
				default:
					System.out.println("Not Handled :" + entry.getValue().getNodeType());
					
			    }					
			}	
		}		
	}

	public Node createBTreeNodes(String Expression) throws Exception {

		int i = 0;
		char c = '\0';
		Stack<Node> operStack = new Stack<>();
		Stack<Node> valStack = new Stack<Node>();
		CharArr cExp = new CharArr(Expression);
		
		boolean inTernaryExp= false;

		while (i < cExp.getPos() && cExp.charAt(i) != '\0') {
			c = cExp.charAt(i);
			if (c == '{' || c == '(') {
				this.openBrakets++;
				operStack.push(new Node(new Token<String>(TokenTypeEnum.OPEN, "{"), NodeTypeEnum.GROUP));
				this.prevNodeType = 'G' ;
			} else if (c == '}' || c == ')') {
				this.closeBrakets++;
				handleClosingBrakets(operStack, valStack);
				this.prevNodeType = 'G' ;
			}
			// Handle hard coded true
			else if ((i + 3) <= cExp.getCapacity() && c == 't' && cExp.charAt(i + 1) == 'r' && cExp.charAt(i + 2) == 'u'
					&& cExp.charAt(i + 3) == 'e') {
				i += 3;
				valStack.push(
						new Node(new Token<Boolean>(TokenTypeEnum.BOOL, new Boolean(true)), NodeTypeEnum.LITERAL));
			}
			// Handle hard coded false
			else if ((i + 4) <= cExp.getCapacity() && c == 'f' && cExp.charAt(i + 1) == 'a' && cExp.charAt(i + 2) == 'l'
					&& cExp.charAt(i + 3) == 's' && cExp.charAt(i + 4) == 'e') {
				i += 4;
				valStack.push(
						new Node(new Token<Boolean>(TokenTypeEnum.BOOL, new Boolean(false)), NodeTypeEnum.LITERAL));
			}else if ((i + 4) <= cExp.getCapacity() && c == 'n' && cExp.charAt(i + 1) == 'u' && cExp.charAt(i + 2) == 'l'
					&& cExp.charAt(i + 3) == 'l') {
				i += 4;
				valStack.push(
						new Node(new Token<>(TokenTypeEnum.NULL, null), NodeTypeEnum.LITERAL));
			}  else if (c == this.enclosureChar) {
				i = handleStringLiteral(cExp, i, valStack);
				this.prevNodeType = 'S';
			} else if (c == '\'') {
				i = handleCharLiteral(cExp, i, valStack);
				this.prevNodeType = 'C';
			} else if (Character.isDigit(c)) {
				i = handleDigits(cExp, i, valStack);
				this.prevNodeType = 'D';
			} else if (c == '@') {
				i = handleFunctionCall(cExp, i, valStack);
				this.prevNodeType = 'F';
			} else if (c == '$') {
				i = handleVariable(cExp, i, valStack);
				this.prevNodeType = 'V';
			} else if (Operator.isOperator(c)) {
				i = handleOperator(cExp, i, operStack, valStack);
				this.prevNodeType = 'O' ;
			}  else if (c == '?') {
				inTernaryExp= true;		
				handleTernaryOperQuestion(operStack,valStack);
				this.prevNodeType = 'T';
			} else if (c == ':' && inTernaryExp) {
				i = handleTernaryOperColen(cExp, i, operStack,valStack);
				inTernaryExp= false;
				this.prevNodeType = 'T';
			}else if (c == ',') {
				// nop
			} else {
				throw new RuntimeException("Failed to parse the expression @ " + i + ". Character '" + c + "' Expr : '"
						+ Expression + "'");
			}
			i++;
		}
		operStack.trimToSize();
		valStack.trimToSize();
		while (!operStack.empty()) {
			Node operator = operStack.pop();
			
			if (operator.getNodeType() == NodeTypeEnum.TERNARY_OPER) {
				Node rt = valStack.pop();
				operator.addLeftChild(valStack.pop());  // this will be the result of boolean operation
				operator.addRightChild(valStack.pop());	// this left of : 				
				operator.addRightChild(rt);             //this is right of :  
			} else 
			if (operator.getNodeType() == NodeTypeEnum.UNARY_OPER) {
				if (!valStack.empty())
					operator.addLeftChild(valStack.pop());	
			}else {
				operator.addRightChild(valStack.pop());				
				if (!valStack.empty())
					operator.addLeftChild(valStack.pop());				
			}
			addToNodeMap(String.format("#Exp_%03d", this.nodeCount), operator);
			this.nodeCount++;
			valStack.push(operator);
		}
		return valStack.pop();
	}

	private int handleOperator(CharArr cExp, int i, Stack<Node> operStack, Stack<Node> valStack) {
		char c = cExp.charAt(i);		
		StringBuilder oper = new StringBuilder(2);
		
		oper.append(c);
		
		if (i + 1 <= cExp.getCapacity() && Operator.is2ByteOperator(c, cExp.charAt(i + 1)))
			oper.append(cExp.charAt(++i));
		
		Node operNode = new Node(new Token<String>(Operator.getOperatorToken(oper.toString()), oper.toString()),NodeTypeEnum.BINARY_OPER);

		if (Operator.isUnaryEligible(oper.toString(), this.prevNodeType)) {
			oper = new StringBuilder(Operator.getUnaryOperValue(oper.toString(), this.prevNodeType));
			operNode = new Node(new Token<String>(Operator.getOperatorToken(oper.toString()), oper.toString() ),	NodeTypeEnum.UNARY_OPER);
			operNode.setNodeType(NodeTypeEnum.UNARY_OPER);		
 		}	
		while (!operStack.empty() && !operStack.peek().getData().getValue().equals("{")
				&& !hasPresedence(oper.toString(), (String) operStack.peek().getData().getValue())) {
			Node operator = operStack.pop();
			if (operator.getNodeType() == NodeTypeEnum.TERNARY_OPER) {
				Node rt = valStack.pop();
				operator.addLeftChild(valStack.pop());  // this will be the result of boolean operation
				operator.addRightChild(valStack.pop());	// this left of : 				
				operator.addRightChild(rt);             //this is right of :  
			} else 
			if (operator.getNodeType() == NodeTypeEnum.UNARY_OPER) {
				if (!valStack.empty())
					operator.addLeftChild(valStack.pop());	
			} else {
				operator.addRightChild(valStack.pop());
				operator.addLeftChild(valStack.pop());
			}
			addToNodeMap(String.format("#Exp_%03d", this.nodeCount), operator);
			this.nodeCount++;
			valStack.push(operator);
		}
		
		operStack.push(operNode);	
	 

		return i;
	}
	
	private void handleTernaryOperQuestion(Stack<Node> operStack, Stack<Node> valStack) {
		
		while (!operStack.empty() && !operStack.peek().getData().getValue().equals("{")
				&& !hasPresedence(":?", (String) operStack.peek().getData().getValue())) {
			Node operator = operStack.pop();
			if (operator.getNodeType() == NodeTypeEnum.TERNARY_OPER) {
				Node rt = valStack.pop();
				operator.addLeftChild(valStack.pop());  // this will be the result of boolean operation
				operator.addRightChild(valStack.pop());	// this left of : 				
				operator.addRightChild(rt);             //this is right of :  
			} else 
			if (operator.getNodeType() == NodeTypeEnum.UNARY_OPER) {
				if (!valStack.empty())
					operator.addLeftChild(valStack.pop());	
			} else {
				operator.addRightChild(valStack.pop());
				operator.addLeftChild(valStack.pop());
			}	
			addToNodeMap(String.format("#Exp_%03d", this.nodeCount), operator);
			this.nodeCount++;
			valStack.push(operator);
		}
	}
	
	private int handleTernaryOperColen(CharArr cExp, int i, Stack<Node> operStack, Stack<Node> valStack) {
		
		while (!operStack.empty() && !operStack.peek().getData().getValue().equals("{")
				&& !hasPresedence(":?", (String) operStack.peek().getData().getValue())) {
			Node operator = operStack.pop();
			if (operator.getNodeType() == NodeTypeEnum.TERNARY_OPER) {
				Node rt = valStack.pop();
				operator.addLeftChild(valStack.pop());  // this will be the result of boolean operation
				operator.addRightChild(valStack.pop());	// this left of : 				
				operator.addRightChild(rt);             //this is right of :  
			} else 
			if (operator.getNodeType() == NodeTypeEnum.UNARY_OPER) {
				if (!valStack.empty())
					operator.addLeftChild(valStack.pop());	
			} else {
				operator.addRightChild(valStack.pop());
				operator.addLeftChild(valStack.pop());
			}	
			addToNodeMap(String.format("#Exp_%03d", this.nodeCount), operator);
			this.nodeCount++;
			valStack.push(operator);
		}
		
		operStack.push(new Node(new Token<String>(Operator.getOperatorToken("?:"), "?:"),
				NodeTypeEnum.TERNARY_OPER));
		return i;
	}

	private int handleVariable(CharArr cExp, int i, Stack<Node> valStack) throws ParseException {
		String varName = null;
		TokenTypeEnum datatype = TokenTypeEnum.UNKN_DT;
		StringBuilder varData = new StringBuilder();
		// case when we have data type defined as $(var_name, data_type).
		char c = cExp.charAt(i + 1);
		if (c == '(') {
			i++;

			while (true) {
				if (cExp.charAt(++i) == ')')
					break;
				varData.append(cExp.charAt(i));
			}
			varName = varData.toString().split(",")[0];
			switch (varData.toString().split(",")[1].toUpperCase()) {
			case "INT":
				datatype = TokenTypeEnum.INT;
				break;
			case "DEC":
				datatype = TokenTypeEnum.DEC;
				break;
			case "DATE":
				datatype = TokenTypeEnum.DATE;
				break;
			case "BOOL":
				datatype = TokenTypeEnum.BOOL;
				break;
			case "STR":
				datatype = TokenTypeEnum.STR;
				break;
			case "CHR":
				datatype = TokenTypeEnum.CHR;
				break;
			default:
				throw new RuntimeException("Unknown Datatype '" + varData.toString().split(",")[1]
						+ "'.\n Valid [int|dec|date|bool|str|chr");
			}
		}
		// Variable with unknown data type. Defined as $var_name
		else if (Character.isJavaIdentifierStart(c)) {
			while (true) {
				varData.append(cExp.charAt(++i));
				if (i >= cExp.getCapacity()-1 || 
				   !Character.isJavaIdentifierPart(cExp.charAt(i + 1)))
					break;
			}
			varName = varData.toString();
		} else {
			throw new RuntimeException(
					"Invalid variable name afetr $. " + " Varable should be declared with java's standards "
							+ "\n Either as $<var_name> for declaring variable with unknows datatype "
							+ "or $(<var_name>, <data type>) for variable with predefined datatype");
		}

		if (!this.variables.containsKey(varName))
			this.variables.put(varName, new Token(datatype, null));

		valStack.push(new Node(this.variables.get(varName), NodeTypeEnum.VARIABLE));
		return i;
	}

	private void handleClosingBrakets(Stack<Node> operStack, Stack<Node> valStack) {
		validateBraketsCount();
		while (!operStack.peek().getData().getValue().equals("{")) {
			Node operator = operStack.pop();
			if (operator.getNodeType() == NodeTypeEnum.TERNARY_OPER) {
				Node rt = valStack.pop();
				operator.addLeftChild(valStack.pop());  // this will be the result of boolean operation
				operator.addRightChild(valStack.pop());	// this left of : 				
				operator.addRightChild(rt);             //this is right of :  
			} else 
			if (operator.getNodeType() == NodeTypeEnum.UNARY_OPER) {
				if (!valStack.empty())
					operator.addLeftChild(valStack.pop());	
			} else {
				operator.addRightChild(valStack.pop());
				operator.addLeftChild(valStack.pop());
			}	
			addToNodeMap(String.format("#Exp_%03d", this.nodeCount), operator);
			this.nodeCount++;
			valStack.push(operator);
		}
		operStack.pop(); // pop the { corresponding to the current}
	}

	private boolean hasPresedence(String operCurrent, String operStored) {
		if (Operator.getOperPriority(operCurrent) - Operator.getOperPriority(operStored) > 0)
			return true;
		return false;
	}


	private int handleFunctionCall(CharArr expression, int i, Stack<Node> valStack) throws Exception {
		int startBlock = i;
		CharArr text = new CharArr();
		char c = '\0';
		// Get the function name @<func_name>(parms..)
		while (true) {
			if ((c = expression.charAt(++i)) == '(')
				break;
			text.appendChar(c);
		}
		String funcName = text.toString();
		TokenTypeEnum funcEnum = Func.getFuncEnum(funcName);
		if (funcEnum == TokenTypeEnum.UNKN_FN)
			throw new ParseException("Unknown Function: " + funcName, startBlock);

		NodeTypeEnum funcTypeEnum = Func.getFuncTypeEnum(funcName);
		Node funcNode = new Node(new Token<String>(funcEnum, funcName), funcTypeEnum);

		// get the expression for function parms
		text = new CharArr();
		int openBrakets = 0;
		while (true) {
			if ((c = expression.charAt(++i)) == ')') {
				if (openBrakets == 0)
					break;
				else
					openBrakets--;
			}
			if (c == '(')
				openBrakets++;

			text.appendChar(c);
		}
		if (funcTypeEnum == NodeTypeEnum.FUNCTION_1_ARG) {
			Node Child = createBTreeNodes(text.toString());
			funcNode.addLeftChild(Child);
		}else if (funcTypeEnum == NodeTypeEnum.FUNCTION_NO_ARG){ 
		   // nop;
		}else {
			ArrayList<CharArr> args = text.split2('(', ')', ',', false);			  			
			for (CharArr cr : args) {
				Node Child = createBTreeNodes(cr.toString());
				funcNode.addRightChild(Child);
			}
		}
		addToNodeMap(String.format("#Exp_%03d", this.nodeCount), funcNode);
		this.nodeCount++;
		valStack.push(funcNode);
		return i;
	}

	private int handleDigits(CharArr expression, int i, Stack<Node> valStack) {
		StringBuilder numberBuilder = new StringBuilder();
		numberBuilder.append(expression.charAt(i));
		boolean integer = true;
		char c = '\0';
		while (true) {
			if (!(i < expression.getCapacity() - 1))
				break;
			c = expression.charAt(i + 1);
			if (!(Character.isDigit(c) || c == '.' || c == 'e' || c == 'E')) {
				break;
			}
			numberBuilder.append(expression.charAt(++i));
			integer &= !(c == '.');
		}
		// if (i < expression.getCapacity() - 1) i--;
		if (integer)
			valStack.push(new Node(new Token<Long>(TokenTypeEnum.INT, Long.parseLong(numberBuilder.toString())),
					NodeTypeEnum.LITERAL));
		else {
			valStack.add(new Node(new Token<Double>(TokenTypeEnum.DEC, Double.parseDouble(numberBuilder.toString())),
					NodeTypeEnum.LITERAL));
		}
		return i;
	}

	private int handleCharLiteral(CharArr expression, int i, Stack<Node> valStack) throws ParseException {
		i++;
		char c = expression.charAt(i);
		if (c == '\\') {
			switch (expression.charAt(i + 1)) {
			case '\\':
				c = '\\';
				break;
			case '0':
				c = '\0';
				break;
			case 'r':
				c = '\r';
				break;
			case 'n':
				c = '\n';
				break;
			case 't':
				c = '\t';
				break;
			case 'b':
				c = '\b';
				break;
			case 'u':
				StringBuilder uChar = new StringBuilder(4);
				uChar.append(expression.charAt(i + 2)).append(expression.charAt(i + 3)).append(expression.charAt(i + 4))
						.append(expression.charAt(i + 5));
				c = (char) Integer.parseInt(uChar.toString(), 16);
				i += 4;
				break;
			default:
				throw new ParseException("Unknown escape sequence '" + c + expression.charAt(i + 1) + " Found at -", i);

			}
			i++;			
		}
		valStack.push(new Node(new Token<Character>(TokenTypeEnum.CHR, new Character(c)), NodeTypeEnum.LITERAL));
		return ++i;
	}

	private int handleStringLiteral(CharArr expression, int i, Stack<Node> valStack) {
		boolean escape = false;
		StringBuilder StringLiteral = new StringBuilder();
		char c = '\0';

		while (i + 1 <= expression.getCapacity()) {
			c = expression.charAt(++i);
			if (c == this.escapeChar && expression.charAt(i + 1) == this.enclosureChar) {
				escape = true;
			} else if (c == this.enclosureChar) {
				if (!escape)
					break;
				escape = false;
			} else {
				StringLiteral.append(c);
			}
		}
		valStack.push(new Node(new Token<String>(TokenTypeEnum.STR, StringLiteral.toString()), NodeTypeEnum.LITERAL));
		return i;
	}

	private void validateBraketsCount() {
		if (this.closeBrakets > this.openBrakets)
			throw new IllegalArgumentException(
					"Unbalanced Expression. Found " + (closeBrakets - openBrakets) + " additional closing Brackets.");
	}

	public void inOrder() {
		System.out.println("InOrder traversal result :");
		inOrder(this.root);
	}	

	private void inOrder(Node cur) {
		if (cur == null)
			return;
		if (cur.isLeafNode()) {
			System.out.println("*Value:" + cur.getData().getValue());
			return;
		}
		if (cur.getLeftChild() != null)
			inOrder(cur.getLeftChild());
		System.out.println(" Value:" + cur.getData().getValue());

		if (cur.getLeftChild() != null)
			inOrder(cur.getRightChild());
	}

	private Node reachtoFirstLeaf(Node cur) {
		while (true) {
			if (cur.getLeftChild() == null)
				break;
			cur = cur.getLeftChild();
		}
		return cur;
	}
    @Override
	public ExpTree clone() throws CloneNotSupportedException {
		//return (ExpTree) ObjectUtils.getDeepCopy(this); // Uses a lot of CPU :(		
		ExpTree res = new ExpTree();
		res.enclosureChar = this.enclosureChar;
		res.escapeChar = this.escapeChar;
		res.nodeCount = this.nodeCount;
		res.closeBrakets = this.closeBrakets;
		res.openBrakets  = this.openBrakets;
		res.root = (Node)this.root.clone();
		res.firstLeaf= (Node)this.firstLeaf.clone();
		res.nodesMap = (TreeMap<String, Node>) this.nodesMap.clone();
		res.variables = ( HashMap<String, Token<?>>) this.variables.clone();
		return res;		
	}

	public void clearResVals() {		
		clearNode(this.root);	
	}
	
	private void clearNode(Node n) {
		n.clearResInfo();
		if (n.getChildren() == null) 
			return;
		for (Node c : n.getChildren()) {
				clearNode(c);
		}
	}
}
