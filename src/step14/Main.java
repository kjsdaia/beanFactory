package step14;


import step14.beanfactory.SimpleBeanFactory;
import step14.beans.Sample1;
import step14.beans.Sample2;

public class Main {
	public static void main(String[] args) {
		SimpleBeanFactory simpleBeanFactory = new SimpleBeanFactory("/step14/bean-definitions.properties");
		
		Sample1 instance = simpleBeanFactory.getInstance(Sample1.class);
		System.out.println(instance != null);
		Object instance2 = simpleBeanFactory.getInstance("sample2");
		System.out.println(instance2 != null);
		System.out.println(Sample2.class.isAssignableFrom(instance2.getClass()));
		System.out.println(instance2 instanceof Sample2);
		System.out.println("------------");

	}
}
