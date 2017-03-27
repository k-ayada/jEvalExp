package pub.ayada.jEvalExp.funcs;

import pub.ayada.jEvalExp.ds.token.Token;
import pub.ayada.jEvalExp.ds.token.TokenTypeEnum;

public class MathFuncs {

	public static Token<?> exe_1(Token<?> ValIn, TokenTypeEnum FuncType) throws IllegalArgumentException {

		if (ValIn.getValue() == null)
			throw new IllegalArgumentException(FuncType.getTokenLiteral() + " can't handle the null argument");

		switch (ValIn.getTokenType()) {
		case INT:
			switch (FuncType) {
			case ABS:
				return new Token<Long>(TokenTypeEnum.INT, Math.abs((Long) ValIn.getValue()));
			case INCR_PRE:
			case INCR_POST:
				return new Token<Long>(TokenTypeEnum.INT, ((Long) ValIn.getValue()) + 1);
			case DECR_PRE:
			case DECR_POST:
				return new Token<Long>(TokenTypeEnum.INT, ((Long) ValIn.getValue()) - 1);
			default:
				break;
			}
		case DEC:
			switch (FuncType) {
			case ABS:
				return new Token<Double>(TokenTypeEnum.DEC,
						new Double(Math.abs(((Number) ValIn.getValue()).doubleValue())));
			case INCR_PRE:
			case INCR_POST:
				return new Token<Double>(TokenTypeEnum.DEC, ((Double) ValIn.getValue()) + 1);
			case DECR_PRE:
			case DECR_POST:
				return new Token<Double>(TokenTypeEnum.DEC, ((Double) ValIn.getValue()) - 1);
			default:
				break;

			}
		default:
			throw new IllegalArgumentException(FuncType.getTokenLiteral() + " can't handle the argument type "
					+ ValIn.getTokenType().toString() + " for value:" + ValIn.getValue().toString());
		}
	}

	public static Token<?> exe_2(Token<?> ValIn1, Token<?> ValIn2, TokenTypeEnum FuncType)
			throws IllegalArgumentException {

		if (ValIn1.getValue() == null)
			throw new IllegalArgumentException(FuncType.getTokenLiteral() + " can't handle the null argument 1");

		if (ValIn2.getValue() == null)
			throw new IllegalArgumentException(FuncType.getTokenLiteral() + " can't handle the null argument 2");

		switch (ValIn1.getTokenType()) {
		case INT:
			switch (FuncType) {
			case MIN:
				if (ValIn2.getTokenType() == TokenTypeEnum.INT) {
					return min(((Long) ValIn1.getValue()).longValue(), ((Long) ValIn2.getValue()).longValue());
				} else {
					return min(new Double((Long) ValIn1.getValue()).doubleValue(),
							((Double) ValIn2.getValue()).doubleValue());
				}
			case MAX:
				if (ValIn2.getTokenType() == TokenTypeEnum.INT) {
					return max(((Long) ValIn1.getValue()).longValue(), ((Long) ValIn2.getValue()).longValue());
				} else {
					return max(new Double((Long) ValIn1.getValue()).doubleValue(),
							((Double) ValIn2.getValue()).doubleValue());
				}
			default:
				return null;
			}
		case DEC:
			switch (FuncType) {
			case MIN:
				if (ValIn2.getTokenType() == TokenTypeEnum.DEC) {
					return min(((Double) ValIn1.getValue()).doubleValue(), ((Double) ValIn2.getValue()).doubleValue());
				} else {
					return min(((Double) ValIn1.getValue()).doubleValue(),
							new Double((Long) ValIn2.getValue()).doubleValue());
				}
			case MAX:
				if (ValIn2.getTokenType() == TokenTypeEnum.DEC) {
					return max(((Double) ValIn1.getValue()).doubleValue(), ((Double) ValIn2.getValue()).doubleValue());
				} else {
					return max(((Double) ValIn1.getValue()).doubleValue(),
							new Double((Long) ValIn2.getValue()).doubleValue());
				}
			default:
				return null;
			}
		case STR:
		case CHR:
			switch (FuncType) {
			case MIN:
				return (ValIn1.getValue().toString().compareTo(ValIn2.getValue().toString())) <= 0 ? ValIn1 : ValIn2;
			case MAX:
				return (ValIn1.getValue().toString().compareTo(ValIn2.getValue().toString())) > 0 ? ValIn1 : ValIn2;
			default:
				return null;
			}
		default:
			throw new IllegalArgumentException(FuncType.getTokenLiteral() + " can't handle the argument type "
					+ ValIn1.getTokenType().toString() + " for value:" + ValIn1.getValue().toString());
		}
	}

	private static Token<Double> min(double doubleValue1, double doubleValue2) {
		return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.min(doubleValue1, doubleValue2)));
	}

	private static Token<Long> min(long l1, long l2) {
		return new Token<Long>(TokenTypeEnum.INT, new Long(Math.min(l1, l2)));
	}

	private static Token<Double> max(double doubleValue1, double doubleValue2) {
		return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.max(doubleValue1, doubleValue2)));
	}

	private static Token<Long> max(long l1, long l2) {
		return new Token<Long>(TokenTypeEnum.INT, new Long(Math.max(l1, l2)));
	}

	public static Token<?> exeAsDecimalArgs_1(Token<?> ValIn, TokenTypeEnum FuncType)
			throws IllegalArgumentException {

		if (ValIn.getValue() == null)
			throw new IllegalArgumentException(FuncType.getTokenLiteral() + " can't handle the null argument");

		double val = ((Number) ValIn.getValue()).doubleValue();

		switch (FuncType) {
		case SIN:
			 return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.sin(val)));
		case ASIN:
			return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.asin(val)));
		case SINH:
			return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.sinh(val)));
		case COS:
			return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.cos(val)));
		case ACOS:
			return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.acos(val)));
		case COSH:
			return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.cosh(val)));
		case TAN:
			return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.tan(val)));
		case ATAN:
			return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.atan(val)));
		case TANH:
			return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.tanh(val)));
		case CBRT:
			return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.cbrt(val)));
		case CEIL:
			return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.ceil(val)));
		case EXP:			
			return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.exp(val)));
		case EXPM1:			
			return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.expm1(val)));
		case FLOOR:			
			return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.floor(val)));
		case GETEXPONENT:
			return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.getExponent(val)));
		case LOG:
			return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.log(val)));
		case LOG10:
			return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.log10(val)));
		case NEXTUP:
			return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.nextUp(val)));
		case RINT:
			return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.rint(val)));
		case ROUND:
			return new Token<Long>(TokenTypeEnum.DEC, new Long(Math.round(val)));
		case SIGNUM:
			return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.signum(val)));
		case SQRT:
			return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.sqrt(val)));
		case TODEGREES:
			return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.toDegrees(val)));
		case TORADIANS:			
			return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.toRadians(val)));
		case ULP:
			return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.toRadians(((Number) ValIn.getValue()).floatValue())));
		default:
			break;
		}
		return null;
	}

	public static Token<?> exeAsDecimalArgs_2(Token<?> ValIn1, Token<?> ValIn2, TokenTypeEnum FuncType)
			throws IllegalArgumentException {

		if (ValIn1.getValue() == null)
			throw new IllegalArgumentException(
					FuncType.getTokenLiteral() + " can't handle the null value for argument 1");
		if (ValIn2.getValue() == null)
			throw new IllegalArgumentException(
					FuncType.getTokenLiteral() + " can't handle the null value for argument 2");

		double val1 = ((Number) ValIn1.getValue()).doubleValue();
		double val2 = ((Number) ValIn2.getValue()).doubleValue();
	
		switch (FuncType) {
		case ATAN2:
			return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.atan2(val1, val2)));
		case COPYSIGN:	
			return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.copySign(val1, val2)));
		case HYPOT:
			return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.hypot(val1, val2)));
		case IEEEREMAINDER:
			return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.IEEEremainder(val1, val2)));
		case NEXTAFTER:
			return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.nextAfter(val1, val2)));
		case POW:
			return new Token<Double>(TokenTypeEnum.DEC, new Double(Math.pow(val1, val2)));
		default:
			break;
		}
		return null;
	}

}
