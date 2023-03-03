package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main2 {

    private static ArrayList<String> contraseñas = new ArrayList<>();
    private static ArrayList<String> palabras = new ArrayList<>();
    public static void main(String[] args) throws IOException {

        imprimirArchivo("/Users/alejandroarias/Desktop/contraseñas.txt");

        System.out.println(" El total de contrasenias = " +contraseñas.size() );
        System.out.println(" El total de palabras = "+palabras.size());
    }


    public static void imprimirArchivo(String rutaArchivo) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(rutaArchivo));
        String linea;
        while ((linea = br.readLine()) != null )  {
            if(linea.length() >= 8){
               contraseñas.add(linea);
            }
            palabras.add(linea);
        }
        br.close();
    }


}
