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
        //Read keyboard class
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
                    //If is the key we expect, change the variable, notify and return(finish thread)
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

    private static void waitForRoomEvent(RoomImplementation room, String event) throws RemoteException,exceptions.noQuestionsLeft{
        //This method waits for the Professor to start a event by writing the asked word
        Object semaphore = new Object();
        synchronized (semaphore) {
            Interrupt interrupt = new Interrupt(semaphore, event);
            interrupt.start();
            while (!started) {
                System.out.println("Write " + event + " to "+ event.toLowerCase() +" the Exam");
                try {
                    semaphore.wait();
                } catch (java.lang.InterruptedException ex) {
                    System.exit(-1);
                }
            }
        }
        if(event=="Open") {
            room.startAcceptingStudents();
            System.out.println("Room opened");
        }else if(event == "Start"){
            room.startExam();
            System.out.println("Exam started");
        }else{
            room.finishExam();
            System.out.println("The Exam has ended");
        }
        started=false;
    }

    public static void showGrades(RoomImplementation room){
        //Show the grades of every student on the terminal
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
            System.out.println("Enter csv questions file route");
            Scanner scan = new Scanner(System.in);
            String route = scan.nextLine();
            room.setCSVFile(route);
            waitForRoomEvent(room,"Open");
            waitForRoomEvent(room,"Start");
            waitForRoomEvent(room,"End");
            showGrades(room);
            System.exit(0);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            System.exit(-1);
        }
    }
}
