package collectible;

public class Hero implements Comparable<Hero> {

	public static class Location {
		protected String name;

		public Location(String name) {
			this.name = name;
		}

		public String toString() {
			return this.name;
		}
	}

	protected String name; //name 
	protected Integer age; // size analog
	protected String description; // object description
	protected Location location; // object location

	public Hero() {}

	public Hero(String name) {

		this.name = name;
	}

	public Hero(String name, Integer age) {

		this.name = name;
		this.age = age;
	}

	public Hero(String name, Integer age, String description, Location location) {

		this.name = name;
		this.age = age;
		this.description = description;
		this.location = location;
	}

	public synchronized void set_age(Integer age) {
		this.age = age;
	}

	public synchronized Integer get_age() {
		return this.age;
	}

	public synchronized void set_name(String name) {
		this.name = name;
	}

	public synchronized String get_name() {
		return this.name;
	}

	public synchronized void set_description(String description) {
		this.description = description;
	}

	public synchronized String get_description() {
		return this.description;
	}

	public synchronized void set_location(Location location) {
		this.location = location;
	}

	public synchronized Location get_location() {
		return this.location;
	}

	public synchronized int compareTo(Hero hero) {

		return age.compareTo(hero.get_age());
	}

}

	

	