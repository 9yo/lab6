package collectible;

public class Hero implements Comparable<Hero> {

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

	public void synchronized set_age(Integer age) {
		this.age = age;
	}

	public Integer synchronized get_age() {
		return this.age;
	}

	public void synchronized set_name(String name) {
		this.name = name;
	}

	public String synchronized get_name() {
		return this.name;
	}

	public void synchronized set_description(String description) {
		this.description = description;
	}

	public String synchronized get_description() {
		return this.description;
	}

	public void synchronized set_location(Location location) {
		this.location = location;
	}

	public Location synchronized get_location() {
		return this.location;
	}

	public int synchronized compareTo(Hero hero) {

		return age.compareTo(hero.get_age());
	}

}

protected class Location {
	protected String name;

	public void set_name(String name) {
		this.name = name;
	}

	public String get_name() {
		return this.name;
	}
}
	

	