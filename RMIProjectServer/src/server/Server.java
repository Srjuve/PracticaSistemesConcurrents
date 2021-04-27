package server;
import java.io.PrintStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class Server {

    public static boolean started = false;

    private static class Interrupt extends Thread {
        String interrupt_key = null;
        Object semaphore = null;

        //semaphore must be the syncronized object
        private Interrupt(Object semaphore, String interrupt_key){
            this.semaphore = semaphore;
            this.interrupt_key = interrupt_key;
        }

        public void run(){
            while (true) {
                //read the key
                Scanner scanner = new Scanner(System.in);
                String x = scanner.nextLine();
                if (x.equals(this.interrupt_key)) {
                    //if is the key we expect, change the variable, notify and return(finish thread)
                    synchronized (this.semaphore) {
                        started = true;
                        this.semaphore.notify();
                        return;
                    }
                }
            }
        }
    }

    private static Registry startRegistry(Integer port)
            throws RemoteException {
        if(port == null) {
            port = 1099;
        }
        try {
            Registry registry = LocateRegistry.getRegistry(port);
            registry.list( );
            // The above call will throw an exception
            // if the registry does not already exist
            return registry;
        }
        catch (RemoteException ex) {
            // No valid registry at that port.
            System.out.println("RMI registry cannot be located ");
            Registry registry= LocateRegistry.createRegistry(port);
            System.out.println("RMI registry created at port ");
            return registry;
        }
    }

    private static void waitForOpenRoom(RoomImplementation room){//Millorar el codi.
        String openWord="Open";
        Object semaphore = new Object();
        synchronized (semaphore) {
            Interrupt interrupt = new Interrupt(semaphore, openWord);
            interrupt.start();
            while (!started) {
                System.out.println("Write " + openWord + " to open the Room to the Students");
                try {
                    semaphore.wait();
                } catch (java.lang.InterruptedException ex) {
                    System.exit(-1);
                }
            }
        }
        System.out.println("Room opened");
        room.startAcceptingStudents();
        started=false;
    }

    private static void waitForStartExam(RoomImplementation room){
        try {
            String startWord = "Start";
            Object semaphore = new Object();
            synchronized (semaphore) {
                Interrupt interrupt = new Interrupt(semaphore, startWord);
                interrupt.start();
                while (!started) {
                    System.out.println("Write " + startWord + " to Start the Exam");
                    try {
                        semaphore.wait();
                    } catch (java.lang.InterruptedException ex) {
                        System.exit(-1);
                    }
                }
            }
            room.startExam();
            System.out.println("Exam started");
            started = false;
        }catch (Exception ex){
            System.exit(-1);
        }
    }

    private static void waitForEndExam(RoomImplementation room){
        try {
            String endWord = "End";
            Object semaphore = new Object();
            synchronized (semaphore) {
                Interrupt interrupt = new Interrupt(semaphore, endWord);
                interrupt.start();
                while (!started) {
                    System.out.println("Write " + endWord + " to End the Exam");
                    try {
                        semaphore.wait();
                    } catch (java.lang.InterruptedException ex) {
                        System.exit(-1);
                    }
                }
            }
            room.finishExam();
            System.out.println("The exam has ended");
            started = false;
        }catch (Exception ex){
            System.exit(-1);
        }
    }

    public static void showGrades(RoomImplementation room){
        HashMap<Integer,Double> grades = room.returnGrades();
        Iterator<Integer> students = grades.keySet().iterator();
        System.out.println("Grades:");
        while(students.hasNext()){
            Integer id=students.next();
            System.out.println("The student with id: "+String.valueOf(id)+" has a grade of: "+String.valueOf(grades.get(id)));
        }
    }
    public static void main(String[] args){
        try {
            Registry registry = startRegistry(null);
            RoomImplementation room = new RoomImplementation();
            registry.bind("room",room);
            System.out.println("Room binded");
            waitForOpenRoom(room);
            waitForStartExam(room);
            waitForEndExam(room);
            showGrades(room);
            System.exit(0);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
