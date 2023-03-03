package org.example;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    public static final String SSID = "Alejandro Arias";

    public static void main(String[] args) throws InterruptedException, IOException {

        imprimirArchivo("/Users/alejandroarias/Desktop/contraseñas.txt");

    }


    /**
     * Metodo que lee un txt linea por linea y devulve un string con la linea leida.
     * se debe usar buffered reader para leer el txt.
     */

    public static void imprimirArchivo(String rutaArchivo) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(rutaArchivo));
        String linea;
        while ((linea = br.readLine()) != null )  {
            if(linea.length() >= 8){

                connectToWifi(SSID, linea);
                if (estaConectadoARed(SSID)) {
                    System.out.println("Conectado a la red Wi-Fi " + SSID + " con la contraseña " + linea);
                    break;
                }

            }
        }
        br.close();
    }


    public static boolean estaConectadoARed(String nombreRed) {
        try {
            InetAddress address = InetAddress.getByName(nombreRed);
            return address.isReachable(5000); // 5000 ms = 5 segundos
        } catch (IOException e) {
            return false;
        }
    }




    /**
     * Creo un txt que contendrá  las contraseñas usadas para conectarse a la red Wi-Fi.
     */

    public static void createTxt(String red) {
        String[] command = { "touch", "/Users/alejandroarias/Desktop/"+red+".txt" };
        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }





    /**
     * Conecta a una red Wi-Fi.
     *
     * @param ssid     El nombre de la red Wi-Fi.
     * @param password La contraseña de la red Wi-Fi.
     * @throws IOException Si no se pudo conectar a la red Wi-Fi.
     */


    public static void connectToWifi(String ssid, String password) throws IOException {
        String[] command = { "/usr/sbin/networksetup", "-setairportnetwork", "en0", ssid, password };
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