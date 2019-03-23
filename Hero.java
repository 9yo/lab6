public class Hero implements Comparable<Hero> {

	protected String name;
	protected Integer age;

	public Hero() {}

	public Hero(String name) {

		this.name = name;
	}

	public Hero(String name, Integer age) {

		this.name = name;
		this.age = age;
	}

	public void set_age(Integer age) {
		this.age = age;
	}

	public Integer get_age() {
		return this.age;
	}

	public void set_name(String name) {
		this.name = name;
	}

	public String get_name() {
		return this.name;
	}

	public int compareTo(Hero hero) {

		return age.compareTo(hero.get_age());
	}

}

	