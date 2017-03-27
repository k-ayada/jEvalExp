package pub.ayada.jEvalExp.ds.token;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;


public class Token<E extends Object > implements Cloneable, Serializable {
	private static final long serialVersionUID = -927561505280969559L;
	private TokenTypeEnum tokenTyp;
	private E value;

	public Token() {
	}

	public Token(TokenTypeEnum TokenType, E Value) {
		this.tokenTyp = TokenType;
		this.value = Value;
	}	

	@Override
	public String toString() {
		return this.value == null ? "(null)" : this.value.toString();
	}

	public E getValue() {
		if (this.value == null)
			return (E) null;
		return value;
	}

	public TokenTypeEnum getTokenType() {
		return this.tokenTyp;
	}

	public void setTokenTyp(TokenTypeEnum tokenTyp) {
		this.tokenTyp = tokenTyp;
	}

	@SuppressWarnings("unchecked")
	public void setValue(Object value) {
		this.value = (E) value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Token<E> clone() throws CloneNotSupportedException {				
		Token<E> cloned = new Token<E>();
		cloned.setTokenTyp(TokenTypeEnum.copy(this.tokenTyp));
		
		if(this.value instanceof String) {
			 String s = (String) this.value;
			cloned.setValue((E) new String(s));
		} else if(this.value instanceof Long) {
			cloned.setValue((E) new Long(((Long)this.value).longValue()));
		}else if(this.value instanceof Double) {
			cloned.setValue((E) new Double(((Double)this.value).doubleValue()));				
		}else if(this.value instanceof Boolean) {
			cloned.setValue((E) new Boolean(((Boolean)this.value).booleanValue()));	
		}else if(this.value instanceof Date) {
			cloned.setValue((E) new Date(((Date)this.value).getTime()));	
		}else {
			try {
				cloned.setValue((E) this.value.getClass().getMethod("clone").invoke(this.value));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
				e.printStackTrace();
				throw new CloneNotSupportedException("Failed to clone value '"+ this.value.toString() + "'");
			}
		}		
	    return cloned;
	}	
	
	
/*	public static void main(String[] args) throws CloneNotSupportedException {
		
		 Token<String> o = new Token<String>(TokenTypeEnum.STR, "Original");
		 
		 Token<String> c = o.clone();
		 System.out.println("Colne Before: " + c.getValue());
		 c.setValue("Clone");
		 
		 System.out.println("Colne After: " + c.getValue());
		 System.out.println("Original   : " + o.getValue());
		
	}*/
	
}
