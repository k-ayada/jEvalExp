package pub.ayada.jEvalExp.opers;

import pub.ayada.jEvalExp.ds.node.Node;
import pub.ayada.jEvalExp.ds.node.NodeTypeEnum;
import pub.ayada.jEvalExp.ds.token.Token;
import pub.ayada.jEvalExp.ds.token.TokenTypeEnum;
import pub.ayada.jEvalExp.utils.ObjectUtils;

public class Operator {

	public static boolean isOperator(char OperChar1) {
		switch (OperChar1) {
		case '*':
		case '/':
		case '%':
		case '+':
		case '-':
		case '=':
		case '!':
		case '<':
		case '>':
		case '&':
		case '|':
			return true;
		default:
			return false;
		}
	}

	public static boolean is2ByteOperator(char OperChar1, char OperChar2) {
		switch (OperChar1) {
		case '+':
			if (OperChar2 == '+')
				return true;
			break;
		case '-':
			if (OperChar2 == '-')
				return true;
			break;
		case '&':
			if (OperChar2 == '&')
				return true;
			break;
		case '|':
			if (OperChar2 == '|')
				return true;
			break;
		case '=':
		case '!':
		case '<':
		case '>':
			if (OperChar2 == '=')
				return true;
			break;
		default:
			break;
		}

		return false;
	}

	public static boolean isUnaryEligible(String Oper, char prevNodeType) {
		switch (Oper) {
		case "--":
		case "++":
		case "!":
		case "+":
		case "-":
			if (prevNodeType == 'O' || prevNodeType == 'F' || prevNodeType == 'G')
				return true;
		default:
			return false;
		}
	}

	public static String getUnaryOperValue(String Oper, char prevNodeType) {
		switch (Oper) {
		case "+":
			return "u+";
		case "-":
			return "u-";
		case "!":
			return "u!";
		case "++":
			if (prevNodeType == 'V')
				return "_++";
			return "++_";
		case "--":
			if (prevNodeType == 'V')
				return "_--";
			return "--_";
		default:
			throw new RuntimeException("Not able to decide the if the operator ' " + Oper + "is unary or binary");
		}
	}

	public static TokenTypeEnum getOperatorToken(String op) {
		switch (op.toUpperCase()) {
		case "*":
			return TokenTypeEnum.MUL;
		case "/":
			return TokenTypeEnum.DIV;
		case "+":
			return TokenTypeEnum.ADD;
		case "-":
			return TokenTypeEnum.SUB;
		case "<":
			return TokenTypeEnum.LT1;
		case "LT":
			return TokenTypeEnum.LT2;
		case "==":
			return TokenTypeEnum.EQ1;
		case "EQ":
			return TokenTypeEnum.EQ2;
		case "!=":
			return TokenTypeEnum.NE1;
		case "NE":
			return TokenTypeEnum.NE2;
		case "<=":
			return TokenTypeEnum.LE1;
		case "LE":
			return TokenTypeEnum.LE2;
		case ">":
			return TokenTypeEnum.GT1;
		case "GT":
			return TokenTypeEnum.GT2;
		case ">=":
			return TokenTypeEnum.GE1;
		case "GE":
			return TokenTypeEnum.GE2;
		case "&&":
			return TokenTypeEnum.AND1;
		case "AND":
			return TokenTypeEnum.AND2;
		case "||":
			return TokenTypeEnum.OR1;
		case "OR":
			return TokenTypeEnum.OR2;
		case "?:":
			return TokenTypeEnum.TERNARY;
		case "U-":
			return TokenTypeEnum.NEGATIVE;
		case "U+":
			return TokenTypeEnum.POSITIVE;
		case "U!":
			return TokenTypeEnum.NEGATE;
		case "_++":
			return TokenTypeEnum.INCR_POST;
		case "++_":
			return TokenTypeEnum.INCR_PRE;
		case "_--":
			return TokenTypeEnum.DECR_POST;
		case "--_":
			return TokenTypeEnum.DECR_PRE;
		default:
			return TokenTypeEnum.UNKN_OP;
		}
	}

	public static int getOperPriority(String oper) {
		switch (oper) {
		case "{":
		case "}":
			return 15;
		case "_++":
		case " _--":
		case "u+":
		case "u-":
		case "u! ":
		case "u~":
			return 14;
		case "++_":
		case "--_":
			return 13;
		case "*":
		case "/":
		case "%":
			return 12;
		case "+":
		case "-":
			return 11;
		case "<<":
		case ">>":
		case ">>>":
			return 10;
		case "<":
		case "<=":
		case ">":
		case ">=":
			return 9;
		case "==":
		case "!=":
			return 8;
		case "&":
			return 7;
		case "^":
			return 6;
		case "|":
			return 5;
		case "&&":
			return 4;
		case "||":
			return 3;
		case "?:":
			return 2;
		case "=":
		case "+=":
		case "-=":
		case "*=":
		case "/=":
		case "%=":
			return 1;
		}
		return 0;
	}

	public static Token<?> exe(Token<?> data, TokenTypeEnum tokenType) {
		switch (tokenType) {
		case NEGATIVE:
			if (data.getTokenType() == TokenTypeEnum.INT) {
				return new Token<Long>(TokenTypeEnum.INT, ((Long) data.getValue()) * -1);
			}
			return new Token<Double>(TokenTypeEnum.DEC, ((Double) data.getValue()) * -1);
		case NEGATE:
			return new Token<Boolean>(TokenTypeEnum.BOOL, (!(Boolean) data.getValue()));
		case POSITIVE:
			return data;
		case BITWISE_COMPLEMENT:
			int d = ((Number) data.getValue()).intValue();
			return new Token<Long>(TokenTypeEnum.INT, (new Long(~d)));
		default:
			throw new RuntimeException(" Yet to code for the opertor type: '" + tokenType + "'");
		}
	}

	public static Token<?> applyOper(Node root, boolean debug) {

		Token<?> res = null;

		Node lChild = null, rChild = null;

		if (root.getNodeType() != NodeTypeEnum.FUNCTION_NO_ARG) {
			lChild = root.getLeftChild();
			if (root.getNodeType() != NodeTypeEnum.UNARY_OPER && root.getNodeType() != NodeTypeEnum.FUNCTION_1_ARG)
				rChild = root.getRightChild();
		}
		if (debug)
			report(root, lChild, rChild);

		switch (root.getData().getTokenType()) {
		case EQ1:
		case EQ2:
			int eq = ObjectUtils.compare(lChild.getData().getValue(), rChild.getData().getValue(),
					lChild.getData().getTokenType(), rChild.getData().getTokenType());
			res = new Token<Boolean>(TokenTypeEnum.BOOL, new Boolean(eq == 0));
			break;
		case NE1:
		case NE2:
			int ne = ObjectUtils.compare(lChild.getData().getValue(), rChild.getData().getValue(),
					lChild.getData().getTokenType(), rChild.getData().getTokenType());
			res = new Token<Boolean>(TokenTypeEnum.BOOL, new Boolean(ne != 0));
			break;
		case GT1:
		case GT2:
			int gt = ObjectUtils.compare(lChild.getData().getValue(), rChild.getData().getValue(),
					lChild.getData().getTokenType(), rChild.getData().getTokenType());
			res = new Token<Boolean>(TokenTypeEnum.BOOL, new Boolean(gt > 0));
			break;
		case GE1:
		case GE2:
			int ge = ObjectUtils.compare(lChild.getData().getValue(), rChild.getData().getValue(),
					lChild.getData().getTokenType(), rChild.getData().getTokenType());
			res = new Token<Boolean>(TokenTypeEnum.BOOL, new Boolean(ge >= 0));
			break;
		case LT1:
		case LT2:
			int lt = ObjectUtils.compare(lChild.getData().getValue(), rChild.getData().getValue(),
					lChild.getData().getTokenType(), rChild.getData().getTokenType());
			res = new Token<Boolean>(TokenTypeEnum.BOOL, new Boolean(lt < 0));
			break;
		case LE1:
		case LE2:
			int le = ObjectUtils.compare(lChild.getData().getValue(), rChild.getData().getValue(),
					lChild.getData().getTokenType(), rChild.getData().getTokenType());
			res = new Token<Boolean>(TokenTypeEnum.BOOL, new Boolean(le <= 0));
			break;
		case ADD:
		case SUB:
		case DIV:
		case MUL:
		case MOD:
			res = MathOperator.exeOper(root);
			break;
		case AND1:
		case AND2:
			if (lChild.getData().getTokenType() == TokenTypeEnum.BOOL
					&& rChild.getData().getTokenType() == TokenTypeEnum.BOOL)
				res = new Token<Boolean>(TokenTypeEnum.BOOL,
						new Boolean((Boolean) lChild.getData().getValue() && (Boolean) rChild.getData().getValue()));
			else
				throw new RuntimeException(
						"Can't run the logical operation '" + root.getData().getTokenType().getTokenLiteral()
								+ "'  against operands (left)" + lChild.getData().getValue().toString()
								+ " and (right) " + rChild.getData().getValue().toString());
			break;
		case INCR_PRE:
		case INCR_POST:
			if (lChild.getData().getTokenType() == TokenTypeEnum.INT)
				res = new Token<Long>(TokenTypeEnum.INT, ((Long) lChild.getData().getValue()) + 1);
			else
				res = new Token<Double>(TokenTypeEnum.DEC, ((Double) lChild.getData().getValue()) + 1);
			break;
		case DECR_PRE:
		case DECR_POST:
			if (lChild.getData().getTokenType() == TokenTypeEnum.INT)
				res = new Token<Long>(TokenTypeEnum.INT, ((Long) lChild.getData().getValue()) - 1);
			else
				res = new Token<Double>(TokenTypeEnum.DEC, ((Double) lChild.getData().getValue()) - 1);

			break;
		case TERNARY:
			if (((Boolean) root.getChildren().get(1).getData().getValue()).booleanValue()) {
				return root.getChildren().get(0).getData();
			}
			return root.getChildren().get(2).getData();

		default:
			throw new RuntimeException(" Yet to code for the opertor type: '" + root.getNodeType() + "'  value: '"
					+ String.valueOf(root.getData().getValue()) + "'");
		}
		return res;
	}

	private static void report(Node root, Node lChild, Node rChild) {

		switch (root.getNodeType()) {
		case UNARY_OPER:
			System.out.print(root.getData().getTokenType().getTokenLiteral()
					+ ((lChild == null || lChild.getData().getValue() == null) ? " <nvl>"
							: " <" + lChild.getData().getValue().toString().replaceAll("\n", "\\\\n") + "> "));
			break;
		case BINARY_OPER:
			System.out.print("<"
					+ ((lChild == null || lChild.getData().getValue() == null) ? "<nvl>"
							: lChild.getData().getValue().toString().replaceAll("\n", "\\\\n"))
					+ "> " + root.getData().getTokenType().getTokenLiteral() + " <"
					+ ((rChild == null || rChild.getData().getValue() == null) ? "<nvl>"
							: rChild.getData().getValue().toString().replaceAll("\n", "\\\\n"))
					+ ">");
			break;
		case TERNARY_OPER:
			System.out
					.print((root.getChildren().get(1) == null || root.getChildren().get(1).getData().getValue() == null
							? "<nvl>"
							: root.getChildren().get(1).getData().getValue().toString().replaceAll("\n", "\\\\n")));

			System.out.print(" ? " + ((lChild == null || lChild.getData().getValue() == null) ? ""
					: lChild.getData().getValue().toString().replaceAll("\n", "\\\\n")));

			System.out.print(" : " + (root.getChildren().get(2) == null ? ""
					: root.getChildren().get(2).getData().getValue().toString().replaceAll("\n", "\\\\n")));
			break;
		default:
			System.out.print("<"
					+ ((lChild == null || lChild.getData().getValue() == null) ? ""
							: lChild.getData().getValue().toString().replaceAll("\n", "\\\\n"))
					+ "> " + root.getData().getTokenType().getTokenLiteral() + " <"
					+ ((rChild == null || rChild.getData().getValue() == null) ? ""
							: rChild.getData().getValue().toString().replaceAll("\n", "\\\\n"))
					+ ">");
		}

	}
}
