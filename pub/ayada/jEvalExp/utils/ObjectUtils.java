package pub.ayada.jEvalExp.utils;

import java.util.Date;
import pub.ayada.jEvalExp.ds.token.TokenTypeEnum;

public class ObjectUtils {
	public static int compare(Object lO, Object rO, TokenTypeEnum lDataTypeEnum, TokenTypeEnum rDataTypeEnum) {
		int res = '\0';

		switch (lDataTypeEnum) {
		case CHR:
			return rDataTypeEnum == TokenTypeEnum.CHR ? ((Character) lO).compareTo((Character) rO)
					: (String.valueOf((Character) lO)).compareTo((String) rO);
		case STR:
			return rDataTypeEnum == TokenTypeEnum.STR ? ((String) lO).compareTo((String) rO)
					: ((String) lO).compareTo(String.valueOf((Character) rO));
		case INT:
			return rDataTypeEnum == TokenTypeEnum.INT ? ((Long) lO).compareTo((Long) rO)
					: new Double(((Number) lO).doubleValue()).compareTo((Double) rO);
		case DEC:
			return rDataTypeEnum == TokenTypeEnum.DEC ? ((Double) lO).compareTo((Double) rO)
					: ((Double) lO).compareTo(new Double(((Number) rO).longValue()));
		case BOOL:
			return ((Boolean) lO).compareTo((Boolean) rO);
		case DATE:
			return ((Date) lO).compareTo((Date) rO);
		default:
			return res;
		}
	}

}
