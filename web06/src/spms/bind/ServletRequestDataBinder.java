package spms.bind;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Set;

import javax.servlet.ServletRequest;
/*
 * 파악된 dataName과 dataType을 알맞게 생성하는 .bind라는 메서드가 있다.
 * 이 bind 메서드를 이 클래스에서 구현한다.
 * 1.필요한 데이터 타입이 primitive Type의 데이터인 경우에는 그에 맞는 객체를 바로 생성
 * 2.member객체를 생성하고, findsetter 메서드를 통해, 주입해줘야 할 메서드를 찾는다.
 * 3.method.invoke를 이용하여, 역으로 접근하여 set 메서드를 실행시켜 필요한 data를 준비한다.
*/
public class ServletRequestDataBinder {
	//프런트 컨트롤러(DispatcherServlet)에서는 요청에 맞는 Data를 준비하기 위한 메서드를 구현한다.
	// ServletRequestDataBinder.bind(request, dataType, dataName); 요청에 맞는 vo를 생성하기 위한
	//bind 메서드를 구현하기 위해 ServletRequestDataBinder클래스를 생성한다.
	public static Object bind(ServletRequest request, Class<?> dataType, String dataName) throws Exception {

		//1.dataType의 빈 객체를 만든다.
		//기본타입(String / data)이면 if조건문이 true면 실행이 되고 바로 생성을 한다.
		if (isPrimitiveType(dataType)) {
			return createValueObject(dataType, request.getParameter(dataName));
		}
		//jsp/html에서 form 통해서  입력받은 값들이 request.getParameterMap()안에 대입하게 되고, keyset()메서드를 통해서 map안에 있는 값을 가져올수가 있다.
		Set<String> paramNames = request.getParameterMap().keySet();
		//newInstance()메서드는 기본 생성자를 호출해서 객체를 생성하기 때문에 반드시 클래스에 기본 생성자가 존재해야 한다
		Object dataObject = dataType.newInstance();
		Method m = null;
		// (name, email, password) : keySet
		for (String paramName : paramNames) {// 배열의 내용을 출력할 때 향상된 for문으로 간단하게 출력
			//데이터타입과 매개변수명을 주면 set 메서드를 찾아서 반환합니다. set메서드를 찾았으면 이전에 생성한 dataObject에 대해 호출을 한다.
			m = findSetter(dataType, paramName);
			//
			if (m != null) {
				//method 클래스의 메서드, 동적으로 호출된 메서드의 결과(Objdect)리턴
				m.invoke(dataObject,
						createValueObject(m.getParameterTypes()[0],//셋터 메서드의 매개변수 타입
						request.getParameter(paramName)));//요청 매개변수의 값
				
			}
		}
		return dataObject;
	}
	// .bind(request, dataName, dataType)를 통해서 필요한 DataName, DataType을 분석을 한다.
	//매개변수로 주어진 타입이 기본 타입인지 검사하는 메서드이다.
	//if조건문을 사용하여 데이터타입이 int인지 아니면 Integer 클래스 인지 검사를 한다. 
	private static boolean isPrimitiveType(Class<?> type) {
		if (type.getName().equals("int") || type == Integer.class
			|| type.getName().equals("long") || type == Long.class
			|| type.getName().equals("float") || type == Float.class
			|| type.getName().equals("double") || type == Double.class 
			|| type.getName().equals("boolean") || type == Boolean.class
			|| type == Date.class || type == String.class) {
			return true;
		}
		return false;
	}
	//createValueObject 메서드는 셋터로 값을 할당 할 수 없는 기본타입에 대해 객체를 생성하는 메서드이다.
	private static Object createValueObject(Class<?> type, String value) {
		if (type.getName().equals("int") || type == Integer.class) {
			return new Integer(value);
		} else if (type.getName().equals("float") || type == Float.class) {
			return new Float(value);
		} else if (type.getName().equals("double") || type == Double.class) {
			return new Double(value);
		} else if (type.getName().equals("long") || type == Long.class) {
			return new Long(value);
		} else if (type.getName().equals("boolean") || type == Boolean.class) {
			return new Boolean(value);
		} else if (type == Date.class) {
			return java.sql.Date.valueOf(value);
		} else {
			return value;
		}
	}
	//findSetter()는 클래스(type)를 조사하여 주어진 이름(name)과 일치하는 set메서드를 찾습니다.
	private static Method findSetter(Class<?> type, String name) {
		Method[] methods = type.getMethods();

		String propName = null;
		//메서드 목록을 반복하면서 set메서드를에 대해서만 작업을 수행하고, 메서드 목록안에 set으로 시작하지 않는다면 무시한다.
		for (Method m : methods) {
			if (!m.getName().startsWith("set"))
				continue;
			
			//set메서드일 경우 요청 매개변수의 이름과 일치하는지 검사한다.
			//substring()함수는 set메서드 이름에서 앞글자를 제외하고 출력한다.즉 setxxxx에서 set를 제외하고 xxxx만 출력한다.
			propName = m.getName().substring(3);		
			//propName에 있는 문자열중에 toLowerCase()함수를 통해서 대문자를 소문자로 변환한다.
			
			if (propName.toLowerCase().equals(name.toLowerCase())) {
				return m;
				//일치하는 set메서드를 찾았다면 즉시 반환을 한다.
			}
		}
		return null;
	}
}