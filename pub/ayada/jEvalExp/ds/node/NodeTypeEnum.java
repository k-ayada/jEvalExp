package pub.ayada.jEvalExp.ds.node;

import java.io.Serializable;

public enum NodeTypeEnum implements Cloneable, Serializable {
	  VARIABLE("VARIABLE")
	, LITERAL("LITERAL")
	, FUNCTION_1_ARG("FUNCTION_1_ARG")
	, FUNCTION_2_ARG("FUNCTION_2_ARG")
	, FUNCTION_N_ARG("FUNCTION_N_ARG")
	, FUNCTION_NO_ARG("FUNCTION_NO_ARG")
	, UNARY_OPER("UNARY_OPER")
	, BINARY_OPER("BINARY_OPER")
	, TERNARY_OPER("TERNARY_OPER")
	, GROUP("GROUP")
	, UNKN("UNKN");

	private String val;

	private NodeTypeEnum(String Value) {
		this.val = Value;
	}

	public String getTokenLiteral() {
		return this.val;
	}

	public void set(String NewEnumValue) {
		this.val = NewEnumValue;
	}

	public static NodeTypeEnum copy(NodeTypeEnum Enum) {
		return NodeTypeEnum.values()[Enum.ordinal()];
	}

}
