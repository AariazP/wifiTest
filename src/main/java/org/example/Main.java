package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    private static final String SSID = "Alejandro Arias"; //red
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors(); //maximo de hilos disponibles para usar
    private static final ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS); //pool de hilos
    private static final List<String> passwords = Collections.synchronizedList(new ArrayList<>()); //contraseñas

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        imprimirArchivo("/Users/alejandroarias/Desktop/contraseñas.txt");
        tryConnect();
        executorService.shutdown();
    }

    public static void imprimirArchivo(String rutaArchivo) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(rutaArchivo));
        String linea;
        while ((linea = br.readLine()) != null) {
            if (linea.length() >= 8) {
                passwords.add(linea);
            }
        }
        br.close();
    }

    public static void tryConnect() throws InterruptedException, ExecutionException {
        List<Future<Boolean>> futures = new ArrayList<>();
        for (String password : passwords) {
            Callable<Boolean> callable = () -> {
                connectToWifi(SSID, password);
                System.out.println("Usando: " + password);
                return estaConectadoARed();
            };
            Future<Boolean> future = executorService.submit(callable);
            futures.add(future);
        }

        for (Future<Boolean> future : futures) {
            if (future.get()) {
                System.out.println("Conectado a " + SSID + " con password " + passwords.get(futures.indexOf(future)));
                break;
            }
        }
    }

    public static boolean estaConectadoARed() {
        try {
            InetAddress address = InetAddress.getByName(SSID);
            return address.isReachable(5000); // 5000 ms = 5 segundos
        } catch (IOException e) {
            return false;
        }
    }

    public static void connectToWifi(String ssid, String password) throws IOException {
        String[] command = {"/usr/sbin/networksetup", "-setairportnetwork", "en0", ssid, password};
        Process process = Runtime.getRuntime().exec(command);
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (process.exitValue() != 0) {
            throw new IOException("No se pudo conectar a la red Wi-Fi " + ssid);
        }
    }
}
