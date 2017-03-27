package pub.ayada.jEvalExp;

import pub.ayada.jEvalExp.Expr;
import pub.ayada.jEvalExp.ds.ExpTree;;

public class ExpFactory {
	Expr e = null;

	public ExpFactory() {
	}

	public ExpFactory(String Expression) throws Exception {
		this.e = new Expr(Expression);
	}
	
	public void init(String Expression) throws Exception {
		
		
		if (this.e == null || !this.e.isExpSet()) {
			this.e = new Expr(Expression); 
		}		
	}

	public ExpTree getExpInstance() throws CloneNotSupportedException {

		if (this.e != null)
			return this.e.getExpInstance();
		throw new RuntimeException("ExpFactory is not initialized with any expression.");
	}
}
