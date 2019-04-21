import java.util.Stack;
import java.util.Date;
import java.io.File;
import java.io.FileNotFoundException;
import org.json.*;
import collectible.Hero;
import xml.*;
import java.io.Serializable;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.ArrayList;



public class StackHandler implements Serializable {

	private static Stack <Hero> hero_stack;
	private static String file_name;
	private static Date stack_initialization_time;
    private static String folder;

	public StackHandler(String file_name, String folder) { 
		this.hero_stack = new Stack <> ();
		this.stack_initialization_time = new Date();
		this.file_name = file_name;
        this.folder = folder;
        load_from_file(file_name);
        

	}

    public StackHandler(String folder) { 
        this.hero_stack = new Stack <> ();
        this.stack_initialization_time = new Date();
        this.folder = folder;

    }

	/**
     * <p>Удаляет из коллекции последний эллемент</p>
     *
     * @param hero_stack коллекция, эллемент которого должен быть удален
     */

    public static synchronized void remove_last() {
        hero_stack.pop();
    }

    /**
     * <p>Очищает коллекцию</p>
     *
     * @param hero_stack коллекция, которая должна быть очищена
     */

    public static synchronized void clear() {
        hero_stack.clear();
    }

    /**
     * <p>Экранизирует коллекцию</p>
     *
     * @param hero_stack коллекция, эллементы которой должны быть экранизированы
     * @return информацию о коллекции
     */

    public static synchronized String show() {
        StringBuilder output = new StringBuilder();
        hero_stack.stream().forEach(h -> output.append(h.toString() + '\n'));
        return(output.toString());

    }

    /**
     * <p>Добавляет эллемент в коллекцию</p>
     *
     * @param hero_stack коллекция, в которую нужно добавить эллемент
     * @param json параметры объекта в формате json
     */

    public static synchronized void add(String json) {
        JSONObject obj = new JSONObject(json);
        Hero hero = new Hero(
                obj.getJSONObject("hero").getString("firstname"),
                Integer.parseInt(obj.getJSONObject("hero").getString("age")),
                obj.getJSONObject("hero").getString("description"),
                new Hero.Location(obj.getJSONObject("hero").getString("location"))
                );
        hero_stack.push(hero);
       	hero_stack = SortStack.sort_stack(hero_stack);

    }


    /**
     * <p>Добавляет эллемент в коллекцию, если он больше других объектов</p>
     *
     * @param hero_stack коллекция, в которую нужно добавить эллемент
     * @param json параметры объекта в формате json
     * @return возвращает коллекцию с новым эллементом
     */


    public static synchronized boolean add_if_max(String json) {
    	boolean status = false;
        JSONObject obj = new JSONObject(json);
        Hero hero = hero_stack.peek();
        Hero json_hero = new Hero(obj.getJSONObject("hero").getString("firstname"), 
                                    Integer.parseInt(obj.getJSONObject("hero").getString("age")));

        if (hero.compareTo(json_hero) > 0) {
            hero_stack.push(json_hero);
            status = true;
        }
       	hero_stack = SortStack.sort_stack(hero_stack);
       	return status;
    }

    /**
     * <p>Выводит информацию о коллекции</p>
     *
     * @param hero_stack коллекция, о которой нужно вывести информацию
     * @param file_name имя, файла с которым работает программа
     * @param stack_initialization_time время создания коллекции
     */

    public static synchronized String info() {
        StringBuilder output = new StringBuilder();
        output.append("Тип коллекции: Stack\n");
        output.append("Время создания коллекции: " + stack_initialization_time + "\n");
        output.append("Колличество эллементов коллекции: " + hero_stack.size() + "\n");
        try {
       		Hero first_hero = hero_stack.peek();
        	output.append("Значение минимального эллемента: " + Integer.toString(first_hero.get_age()) + "\n");
        	output.append("Коллекция хранится в файле: " + file_name + ".xml");
        } 
        catch(java.util.EmptyStackException er) {}

        finally {
            return output.toString();
        }
    }

    public static synchronized void load_from_file(String file_name) {
        StackHandler.file_name = file_name; 

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s/", StackHandler.folder));
        sb.append(file_name);
        sb.append(".xml");
        String file_path = sb.toString();

        File file = new File(file_path);
        if (!file.exists()) {
            try {
                file.createNewFile();
                to_empty();
            } catch (java.io.IOException e) {
                e.printStackTrace();
                }
            }
        else { 
            hero_stack = ReadXMLFile.read(file_path);
        }
    }

    public static synchronized void save_to_file() {
        WriteXMLFile.write(hero_stack, file_name);

    }

    public static synchronized void to_empty() {
        while (!hero_stack.empty()) {
            hero_stack.pop();
        }
    }

     /**
     * <p>Удаляет эллемент из коллекции </p>
     *
     * @param hero_stack коллекция, из которо нудно удалить эллемент
     * @param json параметры объекта в формате json
     * @return возвращает коллекцию с удаленным из него эллементом
     */


    public static synchronized void remove(String json) {
        JSONObject obj = new JSONObject(json);
        final Stack < Hero > new_stack = new Stack < > ();
        hero_stack.stream().forEach(hero -> {  
            if (!hero.get_name().equals(
                    obj.getJSONObject("hero").getString("firstname")) || 
                !hero.get_age().equals(
                    Integer.parseInt(obj.getJSONObject("hero").getString("age"))) ||
                !hero.get_description().equals(
                    obj.getJSONObject("hero").getString("description")) ||
                !hero.get_location().equals(
                    new Hero.Location(obj.getJSONObject("hero").getString("location")))) {

                new_stack.push(hero);
  
                }
            });
        hero_stack = SortStack.sort_stack(new_stack);

    }    

    public static synchronized byte[] serialize() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;

        try{
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        }  catch(IOException e) {
            e.printStackTrace();
        }
         
        for (Hero hero : hero_stack) {
            try {
                objectOutputStream.writeObject(hero);
                } catch (IOException e) {
                    e.printStackTrace();
               }
        }

        try{    
            objectOutputStream.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();

    }

    public static synchronized void add_hero(Hero hero) {
        hero_stack.push(hero);     
        hero_stack = SortStack.sort_stack(hero_stack);
    }
}
