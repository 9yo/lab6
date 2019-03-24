import java.util.Stack;
import java.util.Scanner;
import java.util.Date;
import java.io.File;
import java.io.FileNotFoundException;
import org.json.*;
import collectible.Hero;
import xml.*;

    

public class Main {

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Введите имя файла! \nЗакрываюсь..");
            return;
        }

        String file_name = args[0];
        StringBuilder sb = new StringBuilder();
        sb.append("data/");
        sb.append(file_name);
        sb.append(".xml");
      	String file_path = sb.toString();

        Stack < Hero > hero_stack = new Stack < > ();
        Date stack_initialization_time = new Date();

        File file = new File(file_path);
        if (!file.exists()) {
        	try {
        		file.createNewFile();
        	} catch (java.io.IOException e) {
        		e.printStackTrace();
                }
        	}
        else { 
            hero_stack = ReadXMLFile.read(file_name);
        }

        boolean running = true;
        Scanner input = new Scanner(System.in);

        String menu = "";
        menu+= ">info: вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n";
        menu+= ">clear: очистить коллекцию\n";
        menu+= ">remove_last: удалить последний элемент из коллекции\n";
        menu+= ">add {element}: добавить новый элемент в коллекцию\n";
        menu+= ">add_if_max {element}: добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции\n";
        menu+= ">remove {element}: удалить элемент из коллекции по его значению\n";
        menu+= ">show: вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n";

        while (running) {

            WriteXMLFile.write(hero_stack, file_name);
            String[] command = input.nextLine().split(" ", 2);

            try {

	            switch (command[0]) {

	            	case ("menu"):
	            		System.out.println(menu);

	                case ("clear"):
	                    clear(hero_stack);
	                    break;

	                case ("info"):
	                	print_info(hero_stack, file_name, stack_initialization_time.toString());
	                    break;

	                case ("remove_last"):
	                    remove_last(hero_stack);
	                    break;

	                case ("show"):
	                    show(hero_stack);
	                    break;

	                case ("add"):
	                    add(hero_stack, command[1]);
	                    hero_stack = SortStack.sort_stack(hero_stack);
	                    break;

	                case ("remove"): // remove {"hero": {"firstname":"bb", "age":"17", "description":"rasd", "location": "Doca2"}}
	                    hero_stack = remove(hero_stack, command[1]);
	                    hero_stack = SortStack.sort_stack(hero_stack);
	                    break;

	                case ("add_if_max"):
	                    hero_stack = add_if_max(hero_stack, command[1]);
	                    hero_stack = SortStack.sort_stack(hero_stack);
	                    break;        
	            }
	        System.out.println("\n");   
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
        }
    }

    /**
     * <p>Удаляет из коллекции последний эллемент</p>
     *
     * @param hero_stack коллекция, эллемент которого должен быть удален
     */

    public static void remove_last(Stack < Hero > hero_stack) {
        //System.out.println(hero_stack.stream().findFirst().get());
        hero_stack.pop();
    }

    /**
     * <p>Очищает коллекцию</p>
     *
     * @param hero_stack коллекция, которая должна быть очищена
     */

    public static void clear(Stack < Hero > hero_stack) {
        hero_stack.clear();
    }

    /**
     * <p>Экранизирует коллекцию</p>
     *
     * @param hero_stack коллекция, эллементы которой должны быть экранизированы
     * @return возвращает эту же коллекцию
     */

    public static void show(Stack < Hero > hero_stack) {
        StringBuilder output = new StringBuilder();
        hero_stack.stream().forEach(h -> output.append(h.toString() + '\n'));
        System.out.println(output.toString());

    }

    /**
     * <p>Добавляет эллемент в коллекцию</p>
     *
     * @param hero_stack коллекция, в которую нужно добавить эллемент
     * @param json параметры объекта в формате json
     * @return возвращает коллекцию с новым эллементом
     */

    public static void add(Stack < Hero > hero_stack, String json) {
        JSONObject obj = new JSONObject(json);
        Hero hero = new Hero(
                obj.getJSONObject("hero").getString("firstname"),
                Integer.parseInt(obj.getJSONObject("hero").getString("age")),
                obj.getJSONObject("hero").getString("description"),
                new Hero.Location(obj.getJSONObject("hero").getString("location"))
                );
        hero_stack.push(hero);

    }

    /**
     * <p>Удаляет эллемент из коллекции </p>
     *
     * @param hero_stack коллекция, из которо нудно удалить эллемент
     * @param json параметры объекта в формате json
     * @return возвращает коллекцию с удаленным из него эллементом
     */


    public static Stack < Hero > remove(Stack < Hero > hero_stack, String json) {
        JSONObject obj = new JSONObject(json);
        Stack < Hero > new_stack = new Stack < > ();
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
        return new_stack;

    }


    /**
     * <p>Добавляет эллемент в коллекцию, если он больше других объектов</p>
     *
     * @param hero_stack коллекция, в которую нужно добавить эллемент
     * @param json параметры объекта в формате json
     * @return возвращает коллекцию с новым эллементом
     */


    public static Stack < Hero > add_if_max(Stack < Hero > hero_stack, String json) {
        JSONObject obj = new JSONObject(json);
        Hero hero = hero_stack.peek();
        Hero json_hero = new Hero(obj.getJSONObject("hero").getString("firstname"), 
                                    Integer.parseInt(obj.getJSONObject("hero").getString("age")));

        if (hero.compareTo(json_hero) > 0) {
            hero_stack.push(json_hero);
        }
        return hero_stack;
    }

    /**
     * <p>Выводит информацию о коллекции</p>
     *
     * @param hero_stack коллекция, о которой нужно вывести информацию
     * @param file_name имя, файла с которым работает программа
     * @param stack_initialization_time время создания коллекции
     */

    public static void print_info(Stack < Hero > hero_stack, String file_name, String stack_initialization_time) {
        StringBuilder output = new StringBuilder();
        output.append("Тип коллекции: Stack\n");
        output.append("Время создания коллекции: " + stack_initialization_time + "\n");
        output.append("Колличество эллементов коллекции: " + hero_stack.size() + "\n");
        try {
       		Hero first_hero = hero_stack.peek();
        	output.append("Значение минимального эллемента: " + Integer.toString(first_hero.get_age()));
        	output.append("Коллекция хранится в файле: " + file_name + ".xml");
        } 
        catch(java.util.EmptyStackException er) {}

        finally {
            System.out.println(output.toString());
        }
    }

}
  
class SortStack 
{ 
    // This function return the sorted stack 
    public static Stack<Hero> sort_stack(Stack<Hero>  
                                             input) 
    { 
        Stack<Hero> tmpStack = new Stack<Hero>(); 
        while(!input.isEmpty()) 
        { 
            // pop out the first element 
            Hero tmp = input.pop(); 
          
            // while temporary stack is not empty and 
            // top of stack is greater than temp 
            while(!tmpStack.isEmpty() && tmpStack.peek().compareTo(tmp) > 0) 
            { 
                // pop from temporary stack and  
                // push it to the input stack 
                input.push(tmpStack.pop()); 
            } 
              
            // push temp in tempory of stack 
            tmpStack.push(tmp); 
        } 
        return tmpStack; 
    } 
}