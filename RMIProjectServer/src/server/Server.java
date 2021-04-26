package server;

public class Server {

    public static void main(String[] args){
        try {
            Exam examen1 = new Exam("/home/ajs/Escriptori/UDL/COMPUDIST/Practica1/questions.csv");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
