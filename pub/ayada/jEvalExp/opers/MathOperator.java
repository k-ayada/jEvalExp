package pub.ayada.jEvalExp.opers;

import java.text.ParseException;

import pub.ayada.jEvalExp.ds.node.Node;
import pub.ayada.jEvalExp.ds.token.Token;
import pub.ayada.jEvalExp.ds.token.TokenTypeEnum;

public class MathOperator {

	public static Token<?> exeOper(Node root) {
		return exeOper(root.getLeftChild().getData(), root.getRightChild().getData(), root.getData().getTokenType());
	}

	public static Token<?> exeOper(Token<?> lChild, Token<?> rChild, TokenTypeEnum tokenType) throws RuntimeException {
		try {
			if (lChild.getTokenType() == TokenTypeEnum.STR || rChild.getTokenType() == TokenTypeEnum.STR) {
				return new Token<String>(TokenTypeEnum.STR, String.valueOf(lChild.getValue()) + String.valueOf(rChild.getValue()));				
			} else 
			if (lChild.getTokenType() == TokenTypeEnum.INT && rChild.getTokenType() == TokenTypeEnum.INT) {
				Long left = (Long) lChild.getValue();
				Long right = (Long) rChild.getValue();
				switch (tokenType) {
				case ADD:
					return new Token<Long>(TokenTypeEnum.INT, left + right);
				case SUB:
					return new Token<Long>(TokenTypeEnum.INT, left - right);
				case MUL:
					return new Token<Long>(TokenTypeEnum.INT, left * right);
				case DIV:
					return new Token<Long>(TokenTypeEnum.INT, left / right);
				case MOD:
					return new Token<Long>(TokenTypeEnum.INT, left % right);
				default:
					throw new ParseException("Unknown operator " + tokenType.getTokenLiteral(), 0);
				}
			} else {
				Double left = null;
				Double right = null;
				if (lChild.getTokenType() == TokenTypeEnum.DEC && rChild.getTokenType() == TokenTypeEnum.DEC) {
					left = (Double) lChild.getValue();
					right = (Double) rChild.getValue();
				} else if (lChild.getTokenType() == TokenTypeEnum.DEC) {
					left = (Double) lChild.getValue();
					right = new Double(((Long) rChild.getValue()).longValue());
				} else if (lChild.getTokenType() == TokenTypeEnum.INT) {
					left = new Double(((Long) lChild.getValue()).longValue());
					right = (Double) rChild.getValue();
				} else {
					throw new RuntimeException("operands are not compatible for mathemet.i.cal Operation"
							+ tokenType.getTokenLiteral() + " Left: " + lChild.getValue().toString() + "("
							+ lChild.getTokenType().getTokenLiteral() + ") Right: " + rChild.getValue().toString() + "("
							+ rChild.getTokenType().getTokenLiteral() + ") ");
				}
				switch (tokenType) {
				case ADD:
					return new Token<Double>(TokenTypeEnum.DEC, left + right);
				case SUB:
					return new Token<Double>(TokenTypeEnum.DEC, left - right);
				case MUL:
					return new Token<Double>(TokenTypeEnum.DEC, left * right);
				case DIV:
					return new Token<Double>(TokenTypeEnum.DEC, left / right);
				case MOD:
					return new Token<Double>(TokenTypeEnum.DEC, left % right);
				default:
					throw new ParseException("Unknown operator " + tokenType.getTokenLiteral(), 0);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Error-" + e.getMessage() + "\n" + "During the the operation . "
					+ lChild.getValue().toString() + " " + tokenType.toString() + " " + rChild.getValue().toString());
		}
	}
}
