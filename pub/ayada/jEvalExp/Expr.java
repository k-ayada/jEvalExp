package pub.ayada.jEvalExp;

import java.text.ParseException;
import java.util.Set;

import pub.ayada.genutils.log.Log;

import pub.ayada.jEvalExp.ds.ExpTree;
import pub.ayada.jEvalExp.ds.node.Node;
import pub.ayada.jEvalExp.ds.node.NodeTypeEnum;
import pub.ayada.jEvalExp.ds.token.Token;
import pub.ayada.jEvalExp.funcs.Func;
import pub.ayada.jEvalExp.opers.Operator;


public class Expr {
	private String orgExpr;
	private ExpTree expTree;
	private int calcCount;
	private boolean debug = false;

	public Expr(String Expression) throws Exception {
		this(Expression, '"', '\\', false);
	}

	public Expr(String Expression, boolean Debug) throws Exception {
		this(Expression, '"', '\\', Debug);
	}

	public Expr(String Expression, char EnclosureChar, char EscapeChar) throws Exception {
		this(Expression, EnclosureChar, EscapeChar, false);
	}

	public Expr(String Expression, char EnclosureChar, char EscapeChar, boolean Debug) throws Exception {
		this.orgExpr = Expression;
		if (this.debug)
			Log.print("Expression :" + this.orgExpr.replaceAll("\n", "\\\\n"));
		this.expTree = new ExpTree(this.orgExpr, EnclosureChar, EscapeChar);
		this.debug = Debug;
	}
		
	public void setValue(String VariableName, Object Value) {
		this.expTree.setVarValue(VariableName, Value);
	}
	
	public Set<String> getVars() {
		return this.expTree.getVars();
	}
	

	public ExpTree getExpInstance() throws CloneNotSupportedException {
		  return this.expTree.clone();
	}
	
	public boolean isExpSet() {
		return (this.orgExpr.isEmpty()) ?  false : this.expTree.isBuilt();
	}

	public Object resolve() throws CloneNotSupportedException, ClassNotFoundException, ParseException {	    
		this.calcCount = 0;
		if (this.debug) {
			Log.print("Varibles and values:- " + this.expTree.printVars());
			this.expTree.reportTtreeStructure();
		}		
		Node res = applyOper(this.expTree.getRoot());
		
		if (this.debug) {
			Log.print("Result:- " + res.getData().getValue().toString().replaceAll("\n", "\\\\n") + "\n-------------------------------------------------------------------------");
		}
		
		Object val = res.getData().getValue();
		this.expTree.clearResVals();
		return val;
	}

	public Object clonExpNRresolve() throws CloneNotSupportedException, ClassNotFoundException, ParseException {
		ExpTree e = getExpInstance();
		this.calcCount = 0;
		if (this.debug) {
			Log.print("Varibles and values:- " + e.printVars());
			e.reportTtreeStructure();
		}
		Node res = applyOper(e.getRoot());
		return res.getData().getValue();
	}
	
	private Node applyOper(Node root) throws ParseException, ClassNotFoundException {
		if (root.isLeafNode()
				&& (root.getNodeType() == NodeTypeEnum.LITERAL || root.getNodeType() == NodeTypeEnum.VARIABLE)){				
			return root;			
		}
			
 		for (int i = 0; i < root.getChildren().size(); i++) {
		//	root.getChildren().set(i, (Node) applyOper(root.getChildren().get(i)));
 			Node c = root.getChildren().get(i); 			
 			Node n = (Node) applyOper(c);
 			c.updData(n.getData());
		}
		this.calcCount++;
		if (this.debug) {
			System.out.print(String.format("\n\n\tTask_%03d : ", this.calcCount));
		}

		Token<?> res = null;

		switch (root.getNodeType()) {
		case FUNCTION_1_ARG:
		case FUNCTION_2_ARG:
		case FUNCTION_N_ARG:
		case FUNCTION_NO_ARG:
			res = Func.exeFunc(root, this.debug);
			break;
		case BINARY_OPER:
		case UNARY_OPER:
		case TERNARY_OPER:
			res = Operator.applyOper(root, this.debug);
			break;
		default:
			throw new RuntimeException(" Unhandled Node Type : '" + root.getNodeType() + "'");
		}

		if (this.debug)
			System.out.println(" ==> "
					+ (res == null || res.getValue() == null ? "(null)" : res.getValue().toString().replaceAll("\n", "\\\\n") + " ( type:" + res.getTokenType())
					+ ")");
		root.updNode(res, NodeTypeEnum.LITERAL);
		return root;
		
	}

	private String getOriginlaExpression() {
		return this.orgExpr;
	}


	public static void main(String args[]) throws ClassNotFoundException {
		  
		String Expr = " {1 == 1 ? true : false} && @maxOf(11,12) != @minOf(11,12) &&  \"abcd\n\" != \"\nefgh\" "
				+ " && @sin($(xyz,dec)) != @cos($(wxy,dec) +1)  " + " && @atan2(10,11) >= 1 && \"abcd\" < 'a'"
		// + " && @max(\"abc\", \"ABC\")"
		;
		 Expr = "@decode( (1 == 1 ? @ifNull2(\"null\",true,false) : 300),\"Abc\",@ifNull2(null,true,false), 100,101, 200,201, 300,@ifNull(@ifNull($null,$null),1111), true, true, false, false)";
		 Expr = "@decode(@dayOfWeek(@addYears(@sysDate(),0-++$days),\"FULL\"),\"Thursday\", \"THU\",4)";
		 
		 //Expr = "$days";
		try {
			Expr exp = new Expr(Expr,true);
			exp.setValue("days", 2);

  			exp.setValue("xyz", 100);
			exp.setValue("wxy", 100);

			Log.print("Original - >>>>>" + exp.getOriginlaExpression().replaceAll("\n", "\\\\n") + "<<<<");
			Object res = exp.resolve();				
			
			exp.setValue("days", 1);

  			exp.setValue("xyz", 10);
			exp.setValue("wxy", 10);
			res = exp.resolve();		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
