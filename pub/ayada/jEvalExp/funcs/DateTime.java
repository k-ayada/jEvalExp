package pub.ayada.jEvalExp.funcs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

import pub.ayada.jEvalExp.ds.node.Node;
import pub.ayada.jEvalExp.ds.token.Token;
import pub.ayada.jEvalExp.ds.token.TokenTypeEnum;

public class DateTime {
	
	private static Token<LocalDate> toDate(ArrayList<Node> argsList) {
		if (argsList.size() == 2)
			return toDate((String) argsList.get(0).getData().getValue(), (String) argsList.get(1).getData().getValue(),
					Locale.getDefault());

		return toDate((String) argsList.get(0).getData().getValue(), (String) argsList.get(1).getData().getValue(),
				new Locale((String) argsList.get(2).getData().getValue()));

	}

	private static Token<LocalDate> toDate(String val, String fmt, Locale locale) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(fmt, locale);
		LocalDate d = LocalDate.parse(val, formatter);
		return new Token<LocalDate>(TokenTypeEnum.DATE, d);
	}

	private static Token<LocalDateTime> toDateTime(ArrayList<Node> args) {
		if (args.size() == 2)
			return toDateTime((String) args.get(0).getData().getValue(), (String) args.get(1).getData().getValue(),
					Locale.getDefault());

		return toDateTime((String) args.get(0).getData().getValue(), (String) args.get(1).getData().getValue(),
				new Locale((String) args.get(2).getData().getValue()));
	}

	private static Token<LocalDateTime> toDateTime(String val, String fmt, Locale locale) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(fmt, locale);
		LocalDateTime dt = LocalDateTime.parse(val, formatter);
		return new Token<LocalDateTime>(TokenTypeEnum.DATETIME, dt);
	}
	
	private static Token<?> addDays(Node date, Node count) {
		if (date.getData().getTokenType() == TokenTypeEnum.DATE) {
			LocalDate dt = (LocalDate) date.getData().getValue();
			long cnt = ((Number) count.getData().getValue()).longValue();
			if (cnt >= 0)
				return new Token<LocalDate>(TokenTypeEnum.DATE, dt.plusDays(cnt));
			return new Token<LocalDate>(TokenTypeEnum.DATE, dt.minusDays(cnt * -1));
		}else {
			LocalDateTime dt = (LocalDateTime) date.getData().getValue();
			long cnt = ((Number) count.getData().getValue()).longValue();
			if (cnt >= 0)
				return new Token<LocalDateTime>(TokenTypeEnum.DATETIME, dt.plusDays(cnt));
			return new Token<LocalDateTime>(TokenTypeEnum.DATETIME, dt.minusDays(cnt * -1));	
		}
	}
	
	private static Token<?> addMonths(Node date, Node count) {
		if (date.getData().getTokenType() == TokenTypeEnum.DATE) {
			LocalDate dt = (LocalDate) date.getData().getValue();
			long cnt = ((Number) count.getData().getValue()).longValue();
			if (cnt >= 0)
				return new Token<LocalDate>(TokenTypeEnum.DATE, dt.plusMonths(cnt));
			return new Token<LocalDate>(TokenTypeEnum.DATE, dt.minusMonths(cnt * -1));
		}else {
			LocalDateTime dt = (LocalDateTime) date.getData().getValue();
			long cnt = ((Number) count.getData().getValue()).longValue();
			if (cnt >= 0)
				return new Token<LocalDateTime>(TokenTypeEnum.DATETIME, dt.plusMonths(cnt));
			return new Token<LocalDateTime>(TokenTypeEnum.DATETIME, dt.minusMonths(cnt * -1));
		}
	}
	
	private static Token<?> addWeeks(Node date, Node count) {
		if (date.getData().getTokenType() == TokenTypeEnum.DATE) {
			LocalDate dt = (LocalDate) date.getData().getValue();
			long cnt = ((Number) count.getData().getValue()).longValue();
			if (cnt >= 0)
				return new Token<LocalDate>(TokenTypeEnum.DATE, dt.plusWeeks(cnt));
			return new Token<LocalDate>(TokenTypeEnum.DATE, dt.minusWeeks(cnt * -1));
		}else {
			LocalDateTime dt = (LocalDateTime) date.getData().getValue();
			long cnt = ((Number) count.getData().getValue()).longValue();
			if (cnt >= 0)
				return new Token<LocalDateTime>(TokenTypeEnum.DATETIME, dt.plusWeeks(cnt));
			return new Token<LocalDateTime>(TokenTypeEnum.DATETIME, dt.minusWeeks(cnt * -1));
		}
	}
	private static Token<?> addYears(Node date, Node count) {
		
		if (date.getData().getTokenType() == TokenTypeEnum.DATE) {
			LocalDate dt = (LocalDate) date.getData().getValue();	
			long cnt = ((Number)count.getData().getValue()).longValue();
			if (cnt >= 0)
				return new Token<LocalDate>(TokenTypeEnum.DATE, dt.plusYears(cnt));
			return new Token<LocalDate>(TokenTypeEnum.DATE, dt.minusYears(cnt*-1));
		} else {
			LocalDateTime dt = (LocalDateTime) date.getData().getValue();	
			long cnt = ((Number)count.getData().getValue()).longValue();
			if (cnt >= 0)
				return new Token<LocalDateTime>(TokenTypeEnum.DATETIME, dt.plusYears(cnt));
			return new Token<LocalDateTime>(TokenTypeEnum.DATETIME, dt.minusYears(cnt*-1));			
		}
	}
	private static Token<Long> dayOfMonth(Node date) {
		
		if (date.getData().getTokenType() == TokenTypeEnum.DATE) {
			LocalDate dt = (LocalDate) date.getData().getValue();	
			return new Token<Long>(TokenTypeEnum.INT, new Long(dt.getDayOfMonth()));
		} else {
			LocalDateTime dt = (LocalDateTime) date.getData().getValue();	
			return new Token<Long>(TokenTypeEnum.INT, new Long(dt.getDayOfMonth()));			
		}
	}

	private static Token<?> dayOfWeek(ArrayList<Node> argsList) {

		if (argsList.get(0).getData().getTokenType() == TokenTypeEnum.DATE) {
			LocalDate dt = (LocalDate) argsList.get(0).getData().getValue();
			
			if (argsList.size() == 1 ) {
				return new Token<Long>(TokenTypeEnum.INT, new Long(dt.getDayOfWeek().getValue()));
			}
			
			switch(((String) argsList.get(1).getData().getValue()).toLowerCase()) {
			case "num":
				 return new Token<Long>(TokenTypeEnum.INT, new Long(dt.getDayOfWeek().getValue()));
			case "short":	 
				return new Token<String>(TokenTypeEnum.STR,
						dt.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault()));
			case "full":
				return new Token<String>(TokenTypeEnum.STR,
						dt.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()));
			default : 
				throw new RuntimeException("dayOfWeek can't handle the format " + ((String) argsList.get(1).getData().getValue()) + 
						  "valid values num|short|full (case insencitive) default is num");
			}
		} else {
			LocalDateTime dt = (LocalDateTime) argsList.get(0).getData().getValue();
			
			if (argsList.size() == 1 ) {
				return new Token<Long>(TokenTypeEnum.INT, new Long(dt.getDayOfWeek().getValue()));
			}
			
			switch(((String) argsList.get(1).getData().getValue()).toLowerCase()) {
			case "num":
				 return new Token<Long>(TokenTypeEnum.INT, new Long(dt.getDayOfWeek().getValue()));
			case "short":	 
				return new Token<String>(TokenTypeEnum.STR,
						dt.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault()));
			case "full":
				return new Token<String>(TokenTypeEnum.STR,
						dt.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()));
			default : 
				throw new RuntimeException("dayOfWeek can't handle the format " + ((String) argsList.get(1).getData().getValue()) + 
						  "valid values num|short|full (case insencitive)  default is num");
			}
		}
	}
	private static Token<LocalDate> sysDate() {		
		return new Token<LocalDate>(TokenTypeEnum.DATE,LocalDate.now());		
	}
	private static Token<LocalTime> sysTime() {		
		return new Token<LocalTime>(TokenTypeEnum.TIME,LocalTime.now());		
	}
	private static Token<LocalDateTime> sysDateTime() {		
		return new Token<LocalDateTime>(TokenTypeEnum.DATETIME,LocalDateTime.now());		
	}
	public static Token<?> exe(TokenTypeEnum TokenType, ArrayList<Node> argsList) {
	
		switch (TokenType) {
		case SYSDATE:    return sysDate();
		case SYSTIME:    return sysTime();
		case SYSDATETIME: return sysDateTime();
		case TODATE: 	 return toDate(argsList);
		case TODATETIME: return toDateTime(argsList);
		case ADDDAYS:    return addDays(argsList.get(0),argsList.get(1));
		case ADDMONTHS:  return addMonths(argsList.get(0),argsList.get(1));
		case ADDWEEKS:   return addWeeks(argsList.get(0),argsList.get(1));
		case ADDYEARS:   return addYears(argsList.get(0),argsList.get(1));
		case DAYOFMONTH: return dayOfMonth(argsList.get(0));
		case DAYOFWEEK: return dayOfWeek(argsList);
		default:
			return null;
		}
	}

}
