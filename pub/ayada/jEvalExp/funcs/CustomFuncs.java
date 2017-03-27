package pub.ayada.jEvalExp.funcs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import pub.ayada.jEvalExp.ds.node.Node;
import pub.ayada.jEvalExp.ds.token.Token;
import pub.ayada.jEvalExp.ds.token.TokenTypeEnum;
import pub.ayada.genutils.log.Log;

@SuppressWarnings("unchecked")
public class CustomFuncs {

	private static <E> E maxOf(E... Values) {
		E res = null;

		if (Values[0] instanceof Double) {
			double max = ((Number) Values[0]).doubleValue();
			for (int i = 1; i < Values.length; i++) {
				max = Math.max(max, ((Number) Values[i]).doubleValue());
			}
			res = (E) Double.valueOf(max);
		} else {
			long max = ((Number) Values[0]).longValue();
			for (int i = 1; i < Values.length; i++) {
				max = Math.max(max, ((Number) Values[i]).longValue());
			}
			res = (E) Long.valueOf(max);
		}
		return res;

	}
	
	private static <E> E substr(E... Values) {
		E res = null;
		res = (E) ((String) Values[0]).substring(((Long) Values[1]).intValue()-1, ((Long) Values[2]).intValue()); 
		return res;
	}

	private static <E> E minOf(E... Values) {
		E res = null;

		if (Values[0] instanceof Double) {
			double max = ((Number) Values[0]).doubleValue();
			for (int i = 1; i < Values.length; i++) {
				max = Math.min(max, ((Number) Values[i]).doubleValue());
			}

			res = (E) Double.valueOf(max);
		} else {
			long max = ((Number) Values[0]).longValue();
			for (int i = 1; i < Values.length; i++) {
				max = Math.min(max, ((Number) Values[i]).longValue());
			}
			res = (E) Long.valueOf(max);
		}

		return (E) res;

	}

	private static Token<?> ifNull(Node V1, Node V2) {
		if (V1.getData().getTokenType() == TokenTypeEnum.NULL || V1.getData().getValue() == null) {
			return V2.getData();
		}
		return V1.getData();
	}

	private static Token<?> ifNull2(Node V1, Node V2, Node V3) {
		if (V1.getData().getTokenType() == TokenTypeEnum.NULL || V1.getData().getValue() == null) {
			return V2.getData();
		}
		return V3.getData();
	}

	private static Token<?> nullIf(Node V1, Node V2) {
		if ((Boolean) cmpr(V1, V2).getValue()) {
			return new Token<Object>(TokenTypeEnum.NULL, null);
		}
		return V1.getData();
	}

	@SuppressWarnings("rawtypes")
	public static Token<?> cmpr(Node v1, Node v2) {
		if (v1.getData().getTokenType() != v2.getData().getTokenType())
			return new Token(TokenTypeEnum.BOOL, new Boolean(false));

		switch (v1.getData().getTokenType()) {
		case INT:
			if (((Long) v1.getData().getValue()).compareTo((Long) v2.getData().getValue()) == 0)
				return new Token(TokenTypeEnum.BOOL, new Boolean(true));
			break;
		case STR:
			if (((String) v1.getData().getValue()).compareTo((String) v2.getData().getValue()) == 0)
				return new Token(TokenTypeEnum.BOOL, new Boolean(true));
			break;
		case DEC:
			if (((Double) v1.getData().getValue()).compareTo((Double) v2.getData().getValue()) == 0)
				return new Token(TokenTypeEnum.BOOL, new Boolean(true));
			break;
		case BOOL:
			if (((Boolean) v1.getData().getValue()).compareTo((Boolean) v2.getData().getValue()) == 0)
				return new Token(TokenTypeEnum.BOOL, new Boolean(true));
			break;
		case DATE:
			if (((LocalDate) v1.getData().getValue()).compareTo((LocalDate) v2.getData().getValue()) == 0)
				return new Token(TokenTypeEnum.BOOL, new Boolean(true));
			break;
		case DATETIME:
			if (((LocalDateTime) v1.getData().getValue()).compareTo((LocalDateTime) v2.getData().getValue()) == 0)
				return new Token(TokenTypeEnum.BOOL, new Boolean(true));
			break;
		default:
			throw new RuntimeException(
					"CustomFunc.cmpr() is not yet implemented for type :" + v1.getData().getTokenType());
		}
		return new Token(TokenTypeEnum.BOOL, new Boolean(false));
	}

	public static Token<?> exe_n(TokenTypeEnum TokenType, ArrayList<Node> argsList) {

		switch (TokenType) {
		case SUBSTR:  
			 return new Token<String>(TokenTypeEnum.STR, (String) substr(argsList.get(0).getData().getValue(), argsList.get(1).getData().getValue(), argsList.get(2).getData().getValue())); 
		case DECODE:
			return decode(argsList);			
		case IFNULL:
			return (Token<?>) ifNull(argsList.get(0), argsList.get(1));
		case NULLIF:
			return (Token<?>) nullIf(argsList.get(0), argsList.get(1));
		case IFNULL2:
			return (Token<?>) ifNull2(argsList.get(0), argsList.get(1), argsList.get(2));
		case CMPR:
			return (Token<?>) cmpr(argsList.get(0), argsList.get(1));
		case MAX_OF:
			Object[] maxArr = getDataArray(argsList);
			if (maxArr instanceof Long[]) {
				return new Token<Long>(TokenTypeEnum.INT, (Long) maxOf(maxArr));
			}
			return new Token<Double>(TokenTypeEnum.DEC, (Double) maxOf(maxArr));
		case MIN_OF:
			Object[] minArr = getDataArray(argsList);
			if (minArr instanceof Long[]) {
				return new Token<Long>(TokenTypeEnum.INT, (Long) minOf(minArr));
			}
			return new Token<Double>(TokenTypeEnum.DEC, (Double) minOf(minArr));
		default:
			return null;
		}
	}

	private static <E> E[] getDataArray(ArrayList<Node> arrayList) {
		Double[] da = new Double[arrayList.size()];
		Long[] la = new Long[arrayList.size()];

		boolean noDouble = true;
		int li = 0;
		for (int i = 0; i < arrayList.size(); i++) {
			if (arrayList.get(i).getData().getTokenType() == TokenTypeEnum.INT) {
				if (noDouble) {
					la[i] = (Long) arrayList.get(i).getData().getValue();
					li = i;
				} else {
					da[i] = new Double(((Number) arrayList.get(i).getData().getValue()).doubleValue());
				}
			} else {
				da[i] = (Double) arrayList.get(i).getData().getValue();
				if (noDouble)
					noDouble = false;
			}
		}

		if (!noDouble) {
			for (int i = 0; i <= li; i++) {
				if (!(la[i] == null))
					da[i] = new Double(la[i].doubleValue());
			}
			return (E[]) da;
		}

		return (E[]) la;

	}

	private static Token<?> decode(ArrayList<Node> arrayList) {

		TokenTypeEnum keyType = arrayList.get(0).getData().getTokenType();
		switch (keyType) {
		case STR:
			String stringkey = (String) arrayList.get(0).getData().getValue();
			for (int i = 1; i < arrayList.size() - 1; i += 2) {
				if (arrayList.get(i).getData().getTokenType() == TokenTypeEnum.STR
						&& stringkey.compareTo((String) arrayList.get(i).getData().getValue()) == 0)
					return arrayList.get(i + 1).getData();
			}
			if (arrayList.size() % 2 == 0) {
				return arrayList.get(arrayList.size() - 1).getData();
			} else {
				return new Token<>(TokenTypeEnum.NULL, null);
			}
		case INT:
			Long intKey = (Long) arrayList.get(0).getData().getValue();
			for (int i = 1; i < arrayList.size() - 1; i += 2) {
				if (arrayList.get(i).getData().getTokenType() == TokenTypeEnum.INT
						&& (intKey.compareTo((Long) arrayList.get(i).getData().getValue()) == 0)) {
					return arrayList.get(i + 1).getData();
				} else if (arrayList.get(i).getData().getTokenType() == TokenTypeEnum.DEC
						&& (new Double(intKey.doubleValue())
								.compareTo((Double) arrayList.get(i).getData().getValue()) == 0)) {
					return arrayList.get(i + 1).getData();
				}
			}
			if (arrayList.size() % 2 == 0) {
				return arrayList.get(arrayList.size() - 1).getData();
			} else {
				return new Token<>(TokenTypeEnum.NULL, null);
			}
		case DEC:
			Double doubleKey = (Double) arrayList.get(0).getData().getValue();
			for (int i = 1; i < arrayList.size() - 1; i += 2) {
				if (arrayList.get(i).getData().getTokenType() == TokenTypeEnum.INT && (new Long(doubleKey.longValue())
						.compareTo((Long) arrayList.get(i).getData().getValue()) == 0)) {
					return arrayList.get(i + 1).getData();
				} else if (arrayList.get(i).getData().getTokenType() == TokenTypeEnum.DEC
						&& (doubleKey.compareTo((Double) arrayList.get(i).getData().getValue()) == 0)) {
					return arrayList.get(i + 1).getData();
				}
			}
			if (arrayList.size() % 2 == 0) {
				return arrayList.get(arrayList.size() - 1).getData();
			} else {
				return new Token<>(TokenTypeEnum.NULL, null);
			}
		case BOOL:
			Boolean boolKey = (Boolean) arrayList.get(0).getData().getValue();
			for (int i = 1; i < arrayList.size() - 1; i += 2) {
				if (arrayList.get(i).getData().getTokenType() == TokenTypeEnum.BOOL
						&& (boolKey.compareTo((Boolean) arrayList.get(i).getData().getValue()) == 0)) {
					return arrayList.get(i + 1).getData();
				}
			}
			if (arrayList.size() % 2 == 0) {
				return arrayList.get(arrayList.size() - 1).getData();
			} else {
				return new Token<>(TokenTypeEnum.NULL, null);
			}
		case NULL:
			for (int i = 1; i < arrayList.size() - 1; i += 2) {
				if (arrayList.get(i).getData().getTokenType() == TokenTypeEnum.NULL
						&& arrayList.get(i).getData().getValue() == null) {
					return arrayList.get(i + 1).getData();
				}
			}
			if (arrayList.size() % 2 == 0) {
				return arrayList.get(arrayList.size() - 1).getData();
			} else {
				return new Token<>(TokenTypeEnum.NULL, null);
			}

		default:
			throw new RuntimeException("Yet to implement code for handling type " + keyType.getTokenLiteral());
		}
	}

	public static void main(String[] args) {

		Double[] dr = { Double.valueOf(100.1D), Double.valueOf(100.2D), Double.valueOf(100.3D) };
		Log.print(CustomFuncs.maxOf(dr));

		Long[] lr = { Long.valueOf(100), Long.valueOf(101), Long.valueOf(100) };

		Log.print(CustomFuncs.maxOf(lr));

	}

}
