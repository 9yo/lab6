import java.util.Stack;
import java.util.Scanner;
import java.util.Date;
import java.io.File;
import org.json.*;
import java.io.FileNotFoundException;

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
        


        //hero_stack = ReadXMLFile.read(file_name);
        hero_stack = SortStack.sort_stack(hero_stack);

        boolean running = true;
        Scanner input = new Scanner(System.in);

        while (running) {

            WriteXMLFile.write(hero_stack, file_name);
            String[] command = input.nextLine().split(" ", 2);

            try {

	            switch (command[0]) {

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
	                    hero_stack = show(hero_stack);
	                    break;

	                case ("add"):
	                    hero_stack = add(hero_stack, command[1]);
	                    hero_stack = SortStack.sort_stack(hero_stack);
	                    break;

	                case ("remove"):
	                    hero_stack = remove(hero_stack, command[1]);
	                    hero_stack = SortStack.sort_stack(hero_stack);
	                    break;

	                case ("add_if_max"):
	                    hero_stack = add_if_max(hero_stack, command[1]);
	                    hero_stack = SortStack.sort_stack(hero_stack);
	                    break;

	            }
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

    public static Stack < Hero > show(Stack < Hero > hero_stack) {
        Stack < Hero > tmp_stack = new Stack < > ();
        while (!hero_stack.empty()) {
            Hero hero = hero_stack.pop();
            System.out.println("Имя:" + hero.get_name() + ". Возвраст:" + hero.get_age() + ".");
            tmp_stack.push(hero);
        }

        return tmp_stack;

    }

    /**
     * <p>Добовляет эллемент в коллекцию</p>
     *
     * @param hero_stack коллекция, в которую нужно добавить эллемент
     * @param json параметры объекта в формате json
     * @return возвращает коллекцию с новым эллементом
     */

    public static Stack < Hero > add(Stack < Hero > hero_stack, String json) {
        JSONObject obj = new JSONObject(json);
        String hero_name = obj.getJSONObject("hero").getString("firstname");
        String hero_age = obj.getJSONObject("hero").getString("age");
        Hero hero = new Hero(hero_name, Integer.parseInt(hero_age));
        hero_stack.push(hero);
        return hero_stack;

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
        String hero_name = obj.getJSONObject("hero").getString("firstname");
        String hero_age = obj.getJSONObject("hero").getString("age");
        Stack < Hero > new_stack = new Stack < > ();
        for (Hero hero: hero_stack) {
            if (!hero.get_name().equals(hero_name) || !hero.get_age().equals(Integer.parseInt(hero_age))) {
                new_stack.push(hero);
            }
        }
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
        String hero_name = obj.getJSONObject("hero").getString("firstname");
        String hero_age = obj.getJSONObject("hero").getString("age");
        Hero hero = hero_stack.peek();
        Hero json_hero = new Hero(hero_name, Integer.parseInt(hero_age));
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
        System.out.println("Тип коллекции: Stack");
        System.out.println("Время создания коллекции: " + stack_initialization_time);
        System.out.println("Колличество эллементов коллекции: " + hero_stack.size());
        Hero first_hero = hero_stack.peek();
        System.out.println("Значение минимального эллемента: " + Integer.toString(first_hero.get_age()));
        System.out.println("Коллекция хранится в файле: " + file_name + ".xml");


    }

}