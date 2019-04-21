import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.InetAddress;
import java.lang.Thread;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import collectible.Hero;



public class Server {

	private static DatagramSocket socket;
	private static boolean running;
	private static StackHandler stack;

	public Server() {
		try {
			socket = new DatagramSocket(4444);
		} catch (SocketException e){
			System.out.println("Unable to create socket!");
			return;
		}
	}

	public static void send_msg(String msg, InetAddress address, int port) {
        MrSender sender = new MrSender(msg, address, port, socket);
        Thread message_sender_thread = new Thread(sender);
        message_sender_thread.start();
	}

	public static void import_recieve() {
		DatagramSocket file_socket = null;
		try{
			file_socket = new DatagramSocket(5555);
			//file_socket.setSoTimeout(1000);
		} catch (SocketException e) {
			System.out.println("Unable to create file import socket!");
		}
		byte[] buf = new byte[4096];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		try {
            file_socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		byte[] byte_riceived = packet.getData();
		ObjectInputStream objectInputStream = null;
		try {
			objectInputStream = new ObjectInputStream(
                new ByteArrayInputStream(byte_riceived));
		} catch (IOException e) {
			e.printStackTrace();
		} 

		stack.to_empty();
		while (true){
			try {
				stack.add_hero((Hero) objectInputStream.readObject());
			} catch (IOException e) {
				send_msg("Данные были успешно загружены", packet.getAddress(), packet.getPort());
            	file_socket.close();
            	break;

            } catch (ClassNotFoundException e) {
				e.printStackTrace();
				}
		}

	}

	public void run() {
		stack = new StackHandler("data");
		running = true;

		while (running) {
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try {
            	socket.receive(packet);
            } catch (IOException e) {
            	e.printStackTrace();
            }
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
           	String received = new String(packet.getData(), 0, packet.getLength()).trim();
           	String[] command = received.split(" ", 2);

            try {
	            switch (command[0]) {

	               	case ("import"):
	               		this.import_recieve();
	               		break;

	            	case ("menu"):
						String menu = "";
        				menu+= ">info: вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n";
       					menu+= ">clear: очистить коллекцию\n";
        				menu+= ">remove_last: удалить последний элемент из коллекции\n";
       					menu+= ">add {element}: добавить новый элемент в коллекцию\n";
       					menu+= ">add_if_max {element}: добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции\n";
        				menu+= ">remove {element}: удалить элемент из коллекции по его значению\n";
        				menu+= ">show: вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n";	            	
	            		this.send_msg(menu, address, port);
	            		break;

	                case ("clear"):
	                    this.stack.clear();
	                    this.send_msg("Стак отчищен", address, port);
	                    break;

	                case ("info"):
	                	this.send_msg(stack.info(), address, port);
	                    break;

	                case ("remove_last"):
	                    this.stack.remove_last();
	                    this.send_msg("Эллемент успешно удален", address, port);
	                    break;

	                case ("show"):
	                    this.send_msg(this.stack.show(), address, port);
	                    break;

	                case ("add"):
	                	if (command.length == 2) {
	                    	this.stack.add(command[1]);
	                   		this.send_msg("Эллемент успешно добавлен", address, port);
	                   	}
	                    break;

	                case ("remove"): // removeadd {"hero": {"firstname":"bb", "age":"17", "description":"rasd", "location": "Doca2"}}
	                	if (command.length == 2) {
	                    	this.stack.remove(command[1]);
	                 		this.send_msg("Эллемент успешно удален", address, port);
	                 	}
	                    break;

	                case ("add_if_max"):
	                	if (command.length == 2) {
	                    	if (this.stack.add_if_max(command[1])) {
	                    		this.send_msg("Эллемент успешно добавлен", address, port);
	                    	}
	                    	else {
	                    		this.send_msg("Эллемент не был добавлен", address, port);
	                    	}
	                    }
	                    break;

	                case ("load"):
	                	if (command.length == 2) {
	                		stack.load_from_file(command[1]);
	                		this.send_msg(String.format("Стак был загружен из файла: %s", command[1]) , address, port);
	                	}
	                	break; 

	               	case ("save"):
	               		if (command.length == 2) {
	               			stack.save_to_file(); 
	               			this.send_msg(String.format("Стак был сохранене в файл: %s", command[1]) , address, port);
	               		}
	               		break;

	                case ("end"):
	                	this.running = false;
	                	this.send_msg("Сервер закончил работу", address, port);
	                	break;

	                default:
	                	this.send_msg("Такой комманды не существует", address, port);
	                	break;

	            }

	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	    }
	}
	

	public static void main(String[] args) {
		Server server = new Server();
		server.run();

	}
}