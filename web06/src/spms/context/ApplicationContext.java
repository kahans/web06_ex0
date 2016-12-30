package spms.context;

import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.reflections.Reflections;

import spms.annotation.Component;





public class ApplicationContext {
	Hashtable<String, Object> objTable = new Hashtable<String, Object>();
	//객체를 주는 오브젝트이다.
	public Object getBean(String key) {
		return objTable.get(key);
	}

	public ApplicationContext(String propertiesPath) throws Exception {
		Properties props = new Properties();//생성자 메서드 Properties를 생성한다.
		props.load(new FileReader(propertiesPath));//fileReader는 propertiesPath를 읽어 온다.
		//1. properties->hashTable로 등록한다.
		prepareObjects(props);
		
		//2.@component search-> hashTable
		prepareAnnotationObject();
		
		//3.setter를 가지고 의존성 주입
		injectDependency();
	}
	
	private void prepareAnnotationObject() throws InstantiationException, IllegalAccessException{
		//1.@component search : 외부 API를 이용
		Reflections reflector = new Reflections("spms");//로드 되어 있는 클래스를 검색을 한다.
														//매개변수 값이 'spms'이면 spms 패키지 및 그하위 패키지를 검색
		Set<Class<?>> list = reflector.getTypesAnnotatedWith(Component.class);
		
		//2.value->key, 객체 ->value
		String key = null;
		for(Class<?> clazz : list){
			key = clazz.getAnnotation(Component.class).value();
			objTable.put(key, clazz.newInstance());
		}
	}

	private void prepareObjects(Properties props) throws Exception {
		Context ctx = new InitialContext();
		String key = null;
		String value = null;

		for (Object item : props.keySet()) {
			key = (String) item;
			value = props.getProperty(key);
			//분기코드가 들어가 있다.
			if (key.startsWith("jndi.")) {
				objTable.put(key, ctx.lookup(value));
			} else {
				objTable.put(key, Class.forName(value).newInstance());
			}
		}
	}
	
	private void injectDependency() throws Exception {
		for (String key : objTable.keySet()) {//prepareObjects에서 key를 갖어온다.
			if (!key.startsWith("jndi.")) {//jndi로 시작하는것은 제외한다.
				callSetter(objTable.get(key));
			}
		}
	}

	private void callSetter(Object obj) throws Exception {
		Object dependency = null;
		for (Method m : obj.getClass().getMethods()) {
			if (m.getName().startsWith("set")) {//set인것을 실행을 한다.
				dependency = findObjectByType(m.getParameterTypes()[0]);
				if (dependency != null) {//set이 아닌것은 실행을 안한다.
					m.invoke(obj, dependency);
				}
			}
		}
	}
	//memberDao타입을 찾아 obj로 받아져서 반환한다. / 타입이 없으면 null로 반환된다.
	private Object findObjectByType(Class<?> type) {
		for (Object obj : objTable.values()) {
			if (type.isInstance(obj)) {
				return obj;
			}
		}
		return null;
	}
}
