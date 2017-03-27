package pub.ayada.jEvalExp.funcs;

import pub.ayada.jEvalExp.ds.node.Node;
import pub.ayada.jEvalExp.ds.node.NodeTypeEnum;
import pub.ayada.jEvalExp.ds.token.Token;
import pub.ayada.jEvalExp.ds.token.TokenTypeEnum;
import pub.ayada.jEvalExp.opers.Operator;

public class Func {

	public static NodeTypeEnum getFuncTypeEnum(String funcName) {
		switch (funcName.toLowerCase()) {
		case "random":
		case "sysdate":
		case "systime":
		case "sysdatetime":
			return NodeTypeEnum.FUNCTION_NO_ARG;
		case "abs":
		case "sin":
		case "asin":
		case "sinh":
		case "cos":
		case "acos":
		case "cosh":
		case "tan":
		case "atan":
		case "tanh":
		case "cbrt":
		case "ceil":
		case "floor":
		case "exp":
		case "expm1":
		case "getexponent":
		case "log":
		case "log10":
		case "nextup":
		case "round":
		case "rint":
		case "signum":
		case "sqrt":
		case "todegrees":
		case "toradians":
		case "ulp":
			return NodeTypeEnum.FUNCTION_1_ARG;
		case "date":
		case "atan2":
		case "copysign":
		case "hypot":
		case "ieeeremainder":
		case "pow":
		case "nextafter":
		case "scalb":
		case "ifnull":
		case "nullif":
		case "cmpr":
		case "toDate":
		case "dayofweek":
		case "addyears":
		case "addmonths":
		case "addweeks":
		case "adddays":
			return NodeTypeEnum.FUNCTION_2_ARG;
		case "max":
		case "min":
		case "maxof":
		case "minof":
		case "ifnull2":
		case "substr":
		default:
			return NodeTypeEnum.FUNCTION_N_ARG;
		}
	}

	public static TokenTypeEnum getFuncEnum(String funcName) {
		switch (funcName.toLowerCase()) {
		case "abs":
			return TokenTypeEnum.ABS;
		case "sin":
			return TokenTypeEnum.SIN;
		case "substr":
			return TokenTypeEnum.SUBSTR;
		case "asin":
			return TokenTypeEnum.ASIN;
		case "sinh":
			return TokenTypeEnum.SINH;
		case "cos":
			return TokenTypeEnum.COS;
		case "acos":
			return TokenTypeEnum.ACOS;
		case "cosh":
			return TokenTypeEnum.COSH;
		case "date":
			return TokenTypeEnum.DATE;
		case "tan":
			return TokenTypeEnum.TAN;
		case "atan":
			return TokenTypeEnum.ATAN;
		case "tanh":
			return TokenTypeEnum.TANH;
		case "atan2":
			return TokenTypeEnum.ATAN2;
		case "cbrt":
			return TokenTypeEnum.CBRT;
		case "ceil":
			return TokenTypeEnum.CEIL;
		case "copysign":
			return TokenTypeEnum.COPYSIGN;
		case "exp":
			return TokenTypeEnum.EXP;
		case "expm1":
			return TokenTypeEnum.EXPM1;
		case "floor":
			return TokenTypeEnum.FLOOR;
		case "getexponent":
			return TokenTypeEnum.GETEXPONENT;
		case "hypot":
			return TokenTypeEnum.HYPOT;
		case "ieeeremainder":
			return TokenTypeEnum.IEEEREMAINDER;
		case "log":
			return TokenTypeEnum.LOG;
		case "log10":
			return TokenTypeEnum.LOG10;
		case "max":
			return TokenTypeEnum.MAX;
		case "maxof":
			return TokenTypeEnum.MAX_OF;
		case "min":
			return TokenTypeEnum.MIN;
		case "minof":
			return TokenTypeEnum.MIN_OF;
		case "decode":
			return TokenTypeEnum.DECODE;
		case "nextafter":
			return TokenTypeEnum.NEXTAFTER;
		case "nextup":
			return TokenTypeEnum.NEXTUP;
		case "pow":
			return TokenTypeEnum.POW;
		case "random":
			return TokenTypeEnum.RANDOM;
		case "rint":
			return TokenTypeEnum.RINT;
		case "round":
			return TokenTypeEnum.ROUND;
		case "scalb":
			return TokenTypeEnum.SCALB;
		case "signum":
			return TokenTypeEnum.SIGNUM;
		case "sqrt":
			return TokenTypeEnum.SQRT;
		case "todegrees":
			return TokenTypeEnum.TODEGREES;
		case "toradians":
			return TokenTypeEnum.TORADIANS;
		case "ulp":
			return TokenTypeEnum.ULP;
		case "ifnull":
			return TokenTypeEnum.IFNULL;
		case "ifnull2":
			return TokenTypeEnum.IFNULL2;
		case "nullif":
			return TokenTypeEnum.NULLIF;
		case "cmpr":
			return TokenTypeEnum.CMPR;
		case "todate":
			return TokenTypeEnum.TODATE;
		case "adddays":
			return TokenTypeEnum.ADDDAYS;
		case "addmonths":
			return TokenTypeEnum.ADDMONTHS;
		case "addweeks":
			return TokenTypeEnum.ADDWEEKS;
		case "addyears":
			return TokenTypeEnum.ADDYEARS;
		case "dayofweek":
			return TokenTypeEnum.DAYOFWEEK;
		case "dayofmonth":
			return TokenTypeEnum.DAYOFMONTH;
		case "sysdate":
			return TokenTypeEnum.SYSDATE;
		case "systime":
			return TokenTypeEnum.SYSDATE;
		case "sysdatetime":
			return TokenTypeEnum.SYSDATETIME;
		case "todatetime":
			return TokenTypeEnum.TODATETIME;
		default:
			return TokenTypeEnum.UNKN_FN;
		}
	}

	public static Token<?> exeFunc(Node root, boolean debug) {

		if (debug) {
			Node lChild = null, rChild = null;

			if (root.getNodeType() != NodeTypeEnum.FUNCTION_NO_ARG) {
				lChild = root.getLeftChild();
				if (root.getNodeType() != NodeTypeEnum.UNARY_OPER && root.getNodeType() != NodeTypeEnum.FUNCTION_1_ARG)
					rChild = root.getRightChild();
			}
			switch (root.getNodeType()) {
			case FUNCTION_NO_ARG:
				System.out.print(root.getData().getTokenType().getTokenLiteral() + "()");
				break;
			case FUNCTION_1_ARG:
				System.out.print(root.getData().getTokenType().getTokenLiteral() + "("
						+ ((lChild == null || lChild.getData().getValue() == null) ? ""
								: lChild.getData().getValue().toString().replaceAll("\n", "\\\\n"))
						+ ") ");
				break;
			case FUNCTION_2_ARG:
			case FUNCTION_N_ARG:
				System.out.print(root.getData().getTokenType().getTokenLiteral() + "("
						+ ((lChild == null || lChild.getData().getValue() == null) ? "<nvl>"
								: lChild.getData().getValue().toString().replaceAll("\n", "\\\\n")));

				for (int i = 1; i < root.getChildren().size(); i++) {

					System.out.print(" , " + (root.getChildren().get(i).getData().getValue() == null ? "<nvl>"
							: root.getChildren().get(i).getData().getValue().toString().replaceAll("\n", "\\\\n")));

				}
				System.out.print(") ");
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

		switch (root.getData().getTokenType()) {
		case ABS:
		case ROUND:
			return MathFuncs.exe_1(root.getLeftChild().getData(), root.getData().getTokenType());
		case MAX:
		case MIN:
			return MathFuncs.exe_2(root.getLeftChild().getData(), root.getRightChild().getData(),
					root.getData().getTokenType());
		case SIN:
		case ASIN:
		case SINH:
		case COS:
		case ACOS:
		case COSH:
		case TAN:
		case ATAN:
		case TANH:
		case CBRT:
		case CEIL:
		case EXP:
		case EXPM1:
		case FLOOR:
		case GETEXPONENT:
		case LOG:
		case LOG10:
		case NEXTUP:
		case RINT:
		case SIGNUM:
		case SQRT:
		case TODEGREES:
		case TORADIANS:
		case ULP:
			return MathFuncs.exeAsDecimalArgs_1(root.getLeftChild().getData(), root.getData().getTokenType());
		case ATAN2:
		case COPYSIGN:
		case HYPOT:
		case IEEEREMAINDER:
		case NEXTAFTER:
		case POW:
		case SCALB:
			return MathFuncs.exeAsDecimalArgs_2(root.getLeftChild().getData(), root.getRightChild().getData(),
					root.getData().getTokenType());
		case MAX_OF:
		case MIN_OF:

		case DECODE:
		case IFNULL:
		case NULLIF:
		case IFNULL2:
		case CMPR:
		case SUBSTR:
			return CustomFuncs.exe_n(root.getData().getTokenType(), root.getChildren());
		case TODATE:
		case TODATETIME:
		case ADDDAYS:
		case ADDMONTHS:
		case ADDWEEKS:
		case ADDYEARS:
		case DAYOFMONTH:
		case DAYOFWEEK:
		case SYSDATE:
		case SYSTIME:
		case SYSDATETIME:
			return DateTime.exe(root.getData().getTokenType(), root.getChildren());
		case POSITIVE:
		case NEGATIVE:
		case NEGATE:
		case BITWISE_COMPLEMENT:
			return Operator.exe(root.getLeftChild().getData(), root.getData().getTokenType());
		case RANDOM:
			return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.random()));
		case LEN:
			return new Token<Long>(TokenTypeEnum.INT,
					(new Long(((String) root.getLeftChild().getData().getValue()).length())));
		default:
			throw new RuntimeException(" Yet to code for the function : '" + root.getData().getTokenType() + "'");
		}
	}

}
