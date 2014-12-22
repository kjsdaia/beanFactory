package step14.beanfactory;

import step14.ResourceUtil;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Objects;
import java.util.Properties;

/**
 * 1. singleton instance 관리(동일한 타입이 들어오면 동일한 인스턴스가 항상 리턴) - 생성된 bean 캐싱(멤버변수 어딘가에 저장/map을 멤버변수로, key로 스트링이나 타입잡고)
 * 2. 고려 내용 추후 다른 scope 생성이 용이한 구조가 되도록.(현재는 singleton만 고려하면 되는데 어떻게 클래스를 나눠야 나중에 다른 것을 고려해서 할 수 있을지)
 * 유연성과 확장성을 잘 고려해서 심플 빈팩토리를 잘 나누고 추상화를 하기.
 * 100점 - 감동받으면 / 90점 - 스펙 완성 / 70점 - 일부 완성 / 30점 - 하나도 못하면
 *
 * 12월 19일까지
 * step 14만 따로 작업을 한 뒤 깃에 올리기 - 메일로 깃 유알엘 드리기
 * */

/**
 * Dead Line : 12 / 19 (Fri)
 * Step 14만 따로 해서 git에 올린 후, 메일로 주소를 보냄.
 *
 * 전제조건:
 *  - 기본 생성자가 있는 bean만 취급한다. declaredConstro를 사용.
 * 1. Singleton instance 관리 - 생성된 bean 캐싱 : Map 사용(멤버 변수)
 * 1-1. 고려 내용 추후 다른 scope 생성이 용이한 구조가 되도록. : 다른 scope을 구현하라는 얘기는 아님. OOP로 개발한다는 것은 어떻게 하는 것인가?
 *
 * 유연성과 확장성을 잘 고려해서 심플 빈팩토리를 잘 나누고 추상화를 해줘야 함.
 * 점수기준 - 감동을 받으면 100점, 스펙 완성 : 90점, 일부 완성 : 70점, 미구현 : 30점
 * 16번에서 scope로 풀고 있음.
 * 새 method와 return으로 해결하시오. 불필요한 else문을 만들지 마시오.
 * 다중 루프문은 각 스탭별로 메소드를 분리.
 * 인터페이스를 만들고 이것을 기준으로 움직이는 것이 유리
 *
 * 목적: 인스턴스의 생성을 위임하고 싶어서 만든것.
 */


public class SimpleBeanFactory {
	private String propertyPath;
    private HashMap<Class<?>, Object> hMap = new HashMap<Class<?>, Object>();

	public SimpleBeanFactory(String propertyPath){
		this.propertyPath = propertyPath;

	}
	
	public <T> T getInstance(Class<T> type){
		try {
			Constructor<T> declaredConstructor = type.getDeclaredConstructor();
			type.getDeclaredConstructor().setAccessible(true);
            return (T) getSingletonInstance(type, declaredConstructor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object getInstance(String beanName){
		try{
			String className = parser(ResourceUtil.resourceAsInputStream(propertyPath), beanName);
            Class<?> forName = Class.forName(className);
			Constructor declaredConstructor = forName.getDeclaredConstructor();

			return getSingletonInstance(forName, declaredConstructor);
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
    public String parser(InputStream inputStream, String beanName) throws IOException{
        String[] lines = ResourceUtil.readFully(inputStream);
        String result = null;
        if(lines != null){
            for(String line : lines){
                String[] parsed = line.split("=");
                if(parsed[0].equals(beanName))
                    return parsed[1];
            }
        }
        return result;
    }

    private Object getSingletonInstance(Class<?> cls, Constructor constructor) throws Exception{
        if(hMap.containsKey(cls)){
            return hMap.get(cls);
        }
        constructor.setAccessible(true);
        Object instance = constructor.newInstance();
        hMap.put(cls, instance);
        return instance;
    }

}


