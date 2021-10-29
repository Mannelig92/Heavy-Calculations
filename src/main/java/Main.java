import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Thread server = new Thread(Main::server, "SERVER");
        Thread client = new Thread(Main::client, "CLIENT");

        server.start();
        client.start();
    }

    public static void server() {
        try {
            ServerSocket serverSocket = new ServerSocket(23444);
            while (true) {
                try (Socket socket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String line;
                    while ((line = in.readLine()) != null) {
                        if (line.equals("end")) {
                            return;
                        }
                        out.println(fibonacciCalculation(Integer.parseInt(line)));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }

    public static void client() {
        try (Socket socket = new Socket("127.0.0.1", 23444);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(
                     new OutputStreamWriter(socket.getOutputStream()), true);
             Scanner scanner = new Scanner(System.in)) {

            String msg;
            while (true) {
                System.out.println("Введите число для рассчёта Фибоначчи или \"end\" что бы закончить вычисления:");
                msg = scanner.nextLine();
                out.println(msg);
                if ("end".equals(msg)) {
                    break;
                }
                System.out.println("Число Фибоначчи: " + in.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long fibonacciCalculation(Integer n) {
        long a = 1;
        long b = 1;
        for (int i = 3; i <= n; i++) {
            long c = a + b;
            a = b;
            b = c;
        }
        return b;
    }
}
