package collectible;

import java.io.Serializable;


public class Hero implements Comparable<Hero>, Serializable  {

	public static class Location implements Serializable {
		protected String name;

		public Location(String name) {
			this.name = name;
		}

		public String toString() {
			return this.name;
		}

		public Boolean equals(Location location) {
			return this.name.equals(location.toString()); 
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

	public void set_description(String description) {
		this.description = description;
	}

	public String get_description() {
		return this.description;
	}

	public void set_location(Location location) {
		this.location = location;
	}

	public Location get_location() {
		return this.location;
	}

	public int compareTo(Hero hero) {

		return age.compareTo(hero.get_age());
	}

	public String toString() {
		return "Имя:" + this.name + ". Возвраст:" + this.age + ". Описание:" + this.description + ". Расположение:" + this.location.toString();
	}	

}

	

	