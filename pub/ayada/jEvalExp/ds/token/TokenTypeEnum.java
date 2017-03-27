package pub.ayada.jEvalExp.ds.token;

import java.io.Serializable;

public enum TokenTypeEnum implements Cloneable, Serializable {	
	NULL("java.lang.Object"),
	UNKN_DT("java.lang.Object"),
	INT("java.lang.Integer"),
	LONG("java.lang.Long"), 
	FLOAT ("java.math.Float") ,
	DEC ("java.math.Double") , 
	DATE("java.time.LocalDate"),
	TIME("java.time.LocalTime"),
	DATETIME("java.time.LocalDateTime"),
	STR("java.lang.String"), 
	CHR("java.math.BigDecimal"), 
	BOOL("java.lang.Boolean") , 
	OPEN ( "{") , 
	CLOSE("}"), 
	LEN("java.lang.String") , 
	ABS("java.math.abs"), 
	SIN("java.math.sin"), 
	ASIN("java.math.asin") , 
	SINH("java.math.sinh"), 
	COS("java.math.cos"), 
	ACOS("java.math.acos"), 
	COSH("java.math.cosh"), 
	TAN("java.math.tan"), 
	ATAN("java.math.atan"), 
	TANH("java.math.tanh"), 
	ATAN2("java.math.atan2"), 
	CBRT("java.math.cbrt"), 
	CEIL("java.math.ceil"), 
	COPYSIGN ("java.math.copySign") , 
	EXP("java.math.exp") , 
	EXPM1("java.math.expm1"), 
	FLOOR("java.math.floor"), 
	GETEXPONENT("java.math.getExponent"), 
	HYPOT("java.math.hypot"), 
	IEEEREMAINDER("java.math.IEEEremainder"), 
	LOG("java.math.log"), 
	LOG10("java.math.loglO"), 
	MAX("java.math.max") , 
	MIN("java.math.min"), 
	NEXTAFTER("java.math.nextAfter"), 
	NEXTUP("java.math.nextUp"), 
	POW("java.math.pow"), 
	RANDOM("java.math.random") , 
	RINT("java.math.rint"), 
	ROUND("java.math.round"), 
	SCALB("java.math.scalb"), 
	SIGNUM("java.math.signum") , 
	SQRT("java.math.sqrt") , 
	TODEGREES("java.math.toDegrees"), 
	TORADIANS("java.math.toRadians"), 
	ULP("java.math.ulp"), 
	UNKN_FN("java.math.unknown"), 
	UNKN_OP(""),
	INCR_POST ("_++") , 
	DECR_POST("_--"), 
	POSITIVE ("u+"), 
	NEGATIVE ("u-"), 
	NEGATE ("u!"), 
	BITWISE_COMPLEMENT("u~"), 
	INCR_PRE ("++_"), 
	DECR_PRE ("--_"), 
	MUL("*") , 
	DIV ("/"), 
	MOD("%") , 
	ADD("+") , 
	SUB ("-"), 	
	LEFT_SHIFT("<<"), 
	RIGHT_SHIFT (">>"), 
	RIGHT_SHIFT_OEXT("�>") , 
	LT1 ("<"), 
	LT2 ("LT"), 
	LE1("<="), 
	LE2("LE") , 
	GT1 (">"), 
	GT2 ("GT"), 
	GE1 (">=") , 
	GE2("GE"), 
	EQ1 ("=="), 
	EQ2 ("EQ"), 
	NE1 ("!="), 
	NE2 ("NE"), 
	BITWISE_AND ("u&") , 
	BITWISE_OR_EXCLUSIVE ("u^"), 
	BITWISE_OR_INCLUSIVE("u|"), 
	AND1 ("&&"), 
	AND2 ("AND") , 
	OR1 ("||"), 
	OR2("OR") , 
	TERNARY ("?:"), 
	ASSIGN ("=") , 
	ADDNASSIGN ("+=") , 
	SUBNASSIGN("-="), 
	MULNASSIGN("*="), 
	DIVNASSIGN("/="), 
	MODNASSIGN("%="),
	EOF_FUNC("eofFunc"), 
	MAX_OF("CustomFuncs.maxOf"),
	MIN_OF("CustomFuncs.minOf"),
	DECODE("CustomFuncs.decode"), 
	IFNULL("CustomFuncs.ifNull"),
	IFNULL2("CustomFuncs.ifNull2"),
	NULLIF("CustomFuncs.nullIf"),
	CMPR("CustomFuncs.cmpr"),
	TODATE("CustomFuncs.toDate"),
	ADDDAYS("CustomFuncs.toDate"),
	ADDMONTHS("CustomFuncs.addMonths"),
	ADDWEEKS("CustomFuncs.addWeeks"),
	ADDYEARS("CustomFuncs.addYears"),
	TODATETIME("CustomFuncs.toDateTime"),
    DAYOFMONTH("CustomFuncs.dayOfMonth"),
    DAYOFWEEK("CustomFuncs.dayOfWeek"),
    SYSDATE("CustomFuncs.sysDate"),
    SYSTIME("CustomFuncs.sysTime"),
    SYSDATETIME("CustomFuncs.sysTimestamp"),
    SUBSTR("CustomFuncs.substr")
	; 

	private String tkn;

	private TokenTypeEnum(String Value) {
		this.tkn = Value;
	}

	public String getTokenLiteral() {
		return this.tkn;
	}

	public void set(String NewEnumValue) {
		this.tkn = NewEnumValue;
	}

	public static TokenTypeEnum getEnum4Value(String code) {
		for (TokenTypeEnum e : TokenTypeEnum.values()) {
			if (code == e.tkn)
				return e;
		}
		return null;
	}
     
	public static TokenTypeEnum copy(TokenTypeEnum token) {
		return TokenTypeEnum.values()[token.ordinal()];
	}
}
