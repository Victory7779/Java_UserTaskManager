import jdk.javadoc.doclet.Doclet;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Scanner;

class Task
{
    int id;
    String nameTask;
    String textTask;
    LocalDateTime createTask = LocalDateTime.now();
    public boolean status=false;
    public  Task (int id, String nameTask, String textTask)
    {
        this.id = id;
        this.nameTask= nameTask;
        this.textTask=textTask;
    }

    public String toString()
    {
        String statusString = "not completed";
        if(status) statusString="completed";

        return  "Id: " + id+ "\n"+
                "Name: "+ nameTask + "\n"
                + "Task: " + textTask + "\n"
                + createTask+"\n"
                + "Execution status: "+  statusString + "\n";
    }

}

/*Список всех опций*/
enum Option{Read, Create, Update, Delete, Sort, Filter, Search}

class Server
{
    LinkedList<Task> tasks = new LinkedList<>();
    public void start()
    {
        tasks.add(new Task(0,"Work", "Write text for student"));
        tasks.add(new Task(1, "Family", "Buy present for family"));
        tasks.add(new Task(2, "School", "Do homework"));
    }

    public void read ()
    {
        if(tasks.isEmpty())System.out.println("No tasks");
        tasks.forEach(n-> System.out.println(n.toString()));
    }

    public  void create(Scanner scanner)
    {
        System.out.print("Enter name of task: ");
        String name = scanner.nextLine();
        System.out.print("Enter text of task: ");
        String text = scanner.nextLine();
        int id=tasks.size();
        boolean result = tasks.add(new Task(id, name, text));
        if (result) System.out.println("Create task");
    }

    public void update(Scanner scanner)
    {
        try{
            enum Menu {Name, Text, Status}
            System.out.print("Enter id task for update: ");
            String idString = scanner.nextLine();
            int id = Integer.parseInt(idString);
            if(id > tasks.size()-1)
            {
                System.out.println("Error!!! Not Found");
                return;
            }
            Task task =  tasks.get(id);
            if(task==null)
            {
                System.out.println("Error!!! Not Found");
                return;
            }
            System.out.println(task.toString());

            boolean start = true;
            while (start)
            {
                System.out.println("What do you want to change?");
                short i=0;
                for (Menu m : Menu.values())
                {
                    System.out.println(i + " " + m);
                    i++;
                }

                String choice = scanner.nextLine();
                switch (choice)
                {
                    case "0"->{
                        System.out.print("Enter new name: ");
                        task.nameTask=scanner.nextLine();
                    }
                    case "1"->{
                        System.out.print("Enter new text: ");
                        task.textTask=scanner.nextLine();
                    }
                    case "2"->{
                        task.status=true;
                        System.out.println("Save new status");
                    }
                    default -> {System.out.println("Error!!! This option is missing.");}
                }

                //Выход
                System.out.println("Save? Yes No");
                String answer = scanner.nextLine();
                if (answer.equalsIgnoreCase("Yes"))
                {
                    start = false;
                }
            }
            task.createTask=LocalDateTime.now();
            Task result = tasks.set(id, task);
            if(result!=null) System.out.println("Successfully update!!!");
        }
        catch (NumberFormatException e){
            System.out.println("Error!!! Incorrect input format");
        }

    }

    public void delete(Scanner scanner)
    {
        try
        {
            System.out.print("Enter id task fo delete: ");
            int id = Integer.parseInt(scanner.nextLine());
            boolean result = tasks.removeIf(t-> t.id==id);
            if(result) System.out.println("Successfuly delete task!");
            else System.out.println("Error!");
        }
        catch (NumberFormatException e){
            System.out.println("Error!!! Incorrect input format");
        }

    }

    public  void sort(Scanner scanner)
    {
        int i=0;
        enum OptionSort{Name, Date, Status}
        System.out.println("How you want to compare: ");
        for (OptionSort s: OptionSort.values())
        {
            System.out.println( i+ " "+ s);
            i++;
        }
        String choice = scanner.nextLine();
        switch (choice){
            case "0"->{
                tasks.sort((a, b)->{
                    int a_lenght = a.nameTask.length();
                    int b_lenght = b.nameTask.length();
                    Task buffer;
                    int compareLength = Math.min(a_lenght, b_lenght);
                    for (int j = 0; j < compareLength; j++) {
                        if (a.nameTask.charAt(j)<b.nameTask.charAt(j))
                        {
                            return -1;
                        }
                        if (a.nameTask.charAt(j)>b.nameTask.charAt(j))
                        {
                            return 1;
                        }
                    }
                    return 0;
                });
                tasks.forEach(n-> System.out.println(n.toString()));
            }
            case"1"->{
                tasks.sort((a, b)->{
                    return a.createTask.compareTo(b.createTask);
                });
                tasks.forEach(n-> System.out.println(n.toString()));
            }
            case"2"->{
                tasks.sort((a, b)->{
                    if(a.status && b.status==false) return -1;
                    if(a.status==false && b.status) return 1;
                    return 0;
                });
                tasks.forEach(n-> System.out.println(n.toString()));
            }
            default -> {System.out.println("Error!!! This option is missing.");}
        }

    }
    public  void filter(Scanner scanner)
    {
        enum OptionFilter{ Completed , NotCompleted}
        int i=0;
        System.out.println("Select what criteria to filter by: ");
        for (OptionFilter f: OptionFilter.values())
        {
            System.out.println(i+ " " + f);
            i++;
        }
        String choice = scanner.nextLine();
        switch (choice)
        {
            case "0"->{
                tasks.forEach(t->{
                    if(t.status) System.out.println(t.toString());
                });
            }
            case "1"->{
                tasks.forEach(t->{
                    if(!t.status) System.out.println(t.toString());
                });
            }
            default -> {System.out.println("Error!!! This option is missing.");}
        }
    }

    public void search(Scanner scanner)
    {
        enum OptionSearch{ Name , Text}
        int i=0;
        System.out.println("Select the item qou will search for: ");
        for (OptionSearch f: OptionSearch.values())
        {
            System.out.println(i+ " " + f);
            i++;
        }
        String choice = scanner.nextLine();
        switch (choice)
        {
            case "0"->{
               System.out.print("Enter search world: ");
               String wordSearch = scanner.nextLine();
               tasks.forEach(t->{
                   if(t.nameTask.contains(wordSearch)) System.out.println(t.toString());
               });
            }
            case "1"->{
                System.out.print("Enter search world: ");
                String wordSearch = scanner.nextLine();
                tasks.forEach(t->{
                    if(t.textTask.contains(wordSearch)) System.out.println(t.toString());
                });
            }
            default -> {System.out.println("Error!!! This option is missing.");}
        }
    }

}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Server myServer = new Server();
        myServer.start();
        boolean startWork= true;
        while (startWork)
        {
            System.out.println("Select your option: ");
            int i = 0;
            for (Option option: Option.values())
            {
                if (option == Option.Read || option == Option.Sort || option == Option.Filter || option == Option.Search )
                {
                    System.out.println(String.valueOf(i)+ " " +option+" tasks");
                    i++;
                    continue;
                }
                System.out.println(String.valueOf(i)+ " " +option+" task");
                i++;
            }
            String numberOption = scanner.nextLine();
            switch (numberOption)
            {
                case "0" -> {myServer.read();}
                case "1" -> {myServer.create(scanner);}
                case "2" -> {myServer.update(scanner);}
                case "3" -> {myServer.delete(scanner);}
                case "4" -> {myServer.sort(scanner);}
                case "5" -> {myServer.filter(scanner);}
                case "6" -> {myServer.search(scanner);}
                default -> {System.out.println("Error!!! This option is missing.");}
            }

            /* выход с меню */
            System.out.println("Let's continue? Yes No");
            String answer = scanner.nextLine();
            if (answer.equalsIgnoreCase("No"))
            {
                startWork = false;
            }

        }

    }
}