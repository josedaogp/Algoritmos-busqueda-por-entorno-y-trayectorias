package Practica1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author josed
 */
public class Main2 {

     
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Greedy gr;
        BusquedaAleatoria BA;
        BusquedaLocalMejor BLM;
        BusquedaLocalPrimerMejorBreak BLPM;
        EnfriamientoSimulado ES ;
        TabuV4 tabu;
        
        //ficheros
        ArrayList<String> ficheros = new ArrayList<>();
        ficheros.add("./st70.tsp");
        ficheros.add("./ch130.tsp");
        ficheros.add("./a280.tsp");
        ficheros.add("./p654.tsp");
        ficheros.add("./vm1084.tsp");
        ficheros.add("./vm1748.tsp");
        
        //semillas
        Random aleatorio = new Random();
        int[] semillas = new int[10];
        for (int i = 0; i < 10; i++) {
            semillas[i] = aleatorio.nextInt(999999)+1;
        }
        
        File f = new File("Resultados.csv");
        FileWriter fWriter;
        try {
            fWriter = new FileWriter(f);
            
            //GREEDY
            System.out.println("GREEDY-----------");
            fWriter.write("greedy;ST70;ST70;ST70;CH130;CH130;CH130;A280;A280;A280;PA654;PA654;PA654;VM1084;VM1084;VM1084;VM1748;VM1748;VM1748");
            fWriter.write("\n");
            fWriter.write("N Ejecucion;Semilla;Coste;EV;Semilla;Coste ;Ev;Semilla;Coste;EV;Semilla;Coste ;Ev;Semilla;Coste;EV;Semilla;Coste ;Ev");
            
            for (int j = 0; j < semillas.length; j++) {
                System.out.println(j+" Semilla: "+semillas[j]);
                fWriter.write("\n");
                fWriter.write(j+";");
                for (String fichero : ficheros) {
                    System.out.println("FICHERO: "+fichero);
                    fWriter.write(semillas[j]+";");
                    gr = new Greedy(fichero);
                    gr.greedy();
                    int coste = gr.getCoste();
                    fWriter.write(coste+";");
                    int evaluaciones = gr.getEvaluaciones();
                    fWriter.write(evaluaciones+";");
                    
                }
                fWriter.flush();
            }
            System.out.println("FIN GREEDY***********");
            fWriter.write(";;;;;;;;;;;;;;;;;;");
            fWriter.write(";;;;;;;;;;;;;;;;;;");
            
            
            //BUSQUEDA ALEATORIA
            System.out.println("ALEATORIA-----------");
            fWriter.write("\n");
            fWriter.write("Aleatoria;ST70;ST70;ST70;CH130;CH130;CH130;A280;A280;A280;PA654;PA654;PA654;VM1084;VM1084;VM1084;VM1748;VM1748;VM1748");
            fWriter.write("\n");
            fWriter.write("N Ejecucion;Semilla;Coste;EV;Semilla;Coste ;Ev;Semilla;Coste;EV;Semilla;Coste ;Ev;Semilla;Coste;EV;Semilla;Coste ;Ev");
            
            for (int j = 0; j < semillas.length; j++) {
                System.out.println(j+" Semilla: "+semillas[j]);
                fWriter.write("\n");
                fWriter.write(j+";");
                for (String fichero : ficheros) {
                    System.out.println("FICHERO: "+fichero);
                    fWriter.write(semillas[j]+";");
                    BA = new BusquedaAleatoria(fichero);
                    BA.busquedaAleatoria(semillas[j]);
                    int coste = BA.getCoste();
                    fWriter.write(coste+";");
                    int evaluaciones = BA.getEvaluaciones();
                    fWriter.write(evaluaciones+";");
                    
                }
                fWriter.flush();
            }
            System.out.println("FIN ALEATORIA***********");
            fWriter.write(";;;;;;;;;;;;;;;;;;");
            fWriter.write(";;;;;;;;;;;;;;;;;;");
            
            //BUSQUEDA LOCAL EL MEJOR
            System.out.println("LOCAL MEJOR-----------");
            fWriter.write("\n");
            fWriter.write("El mejor;ST70;ST70;ST70;CH130;CH130;CH130;A280;A280;A280;PA654;PA654;PA654;VM1084;VM1084;VM1084;VM1748;VM1748;VM1748");
            fWriter.write("\n");
            fWriter.write("N Ejecucion;Semilla;Coste;EV;Semilla;Coste ;Ev;Semilla;Coste;EV;Semilla;Coste ;Ev;Semilla;Coste;EV;Semilla;Coste ;Ev");
            
            for (int j = 0; j < semillas.length; j++) {
                System.out.println(j+" Semilla: "+semillas[j]);
                fWriter.write("\n");
                fWriter.write(j+";");
                for (String fichero : ficheros) {
                    System.out.println("FICHERO: "+fichero);
                    fWriter.write(semillas[j]+";");
                    BLM = new BusquedaLocalMejor(fichero);
                    BLM.busquedaElMejor(semillas[j]);
                    int coste = BLM.getCosteFinal();
                    fWriter.write(coste+";");
                    int evaluaciones = BLM.getEvaluaciones();
                    fWriter.write(evaluaciones+";");
                    
                }
                fWriter.flush();
            }
            System.out.println("FIN LOCAL MEJOR***********");
            fWriter.write(";;;;;;;;;;;;;;;;;;");
            fWriter.write(";;;;;;;;;;;;;;;;;;");
            
            //BUSQUEDA LOCAL EL PRIMER MEJOR
            System.out.println("LOCAL PRIMER MEJOR-----------");
            fWriter.write("\n");
            fWriter.write("El Primer mejor;ST70;ST70;ST70;CH130;CH130;CH130;A280;A280;A280;PA654;PA654;PA654;VM1084;VM1084;VM1084;VM1748;VM1748;VM1748");
            fWriter.write("\n");
            fWriter.write("N Ejecucion;Semilla;Coste;EV;Semilla;Coste ;Ev;Semilla;Coste;EV;Semilla;Coste ;Ev;Semilla;Coste;EV;Semilla;Coste ;Ev");
            
            for (int j = 0; j < semillas.length; j++) {
                System.out.println(j+" Semilla: "+semillas[j]);
                fWriter.write("\n");
                fWriter.write(j+";");
                for (String fichero : ficheros) {
                    System.out.println("FICHERO: "+fichero);
                    fWriter.write(semillas[j]+";");
                    BLPM = new BusquedaLocalPrimerMejorBreak(fichero);
                    BLPM.busquedaElPrimerMejor(semillas[j]);
                    int coste = BLPM.getCosteFinal();
                    fWriter.write(coste+";");
                    int evaluaciones = BLPM.getEvaluaciones();
                    fWriter.write(evaluaciones+";");
                    
                }
                fWriter.flush();
            }
            System.out.println("FIN LOCAL EL PRIMER MEJOR***********");
             fWriter.write(";;;;;;;;;;;;;;;;;;");
            fWriter.write(";;;;;;;;;;;;;;;;;;");
            
            //ENFRIAMIENTO SIMULADO
            System.out.println("ENFRIAMIENTO SIMULADO-----------");
            fWriter.write("\n");
            fWriter.write("Enfriamiento Simulado;ST70;ST70;ST70;CH130;CH130;CH130;A280;A280;A280;PA654;PA654;PA654;VM1084;VM1084;VM1084;VM1748;VM1748;VM1748");
            fWriter.write("\n");
            fWriter.write("N Ejecucion;Semilla;Coste;EV;Semilla;Coste ;Ev;Semilla;Coste;EV;Semilla;Coste ;Ev;Semilla;Coste;EV;Semilla;Coste ;Ev");
            
            for (int j = 0; j < semillas.length; j++) {
                System.out.println(j+" Semilla: "+semillas[j]);
                fWriter.write("\n");
                fWriter.write(j+";");
                for (String fichero : ficheros) {
                    System.out.println("FICHERO: "+fichero);
                    fWriter.write(semillas[j]+";");
                    ES = new EnfriamientoSimulado(fichero);
                    ES.enfriamientoSimulado(semillas[j]);
                    int coste = ES.getCosteFinal();
                    fWriter.write(coste+";");
                    int evaluaciones = ES.getEvaluaciones();
                    fWriter.write(evaluaciones+";");
                    
                }
                fWriter.flush();
            }
            System.out.println("FIN ENFRIAMIENTO SIMULADO***********");
            fWriter.write(";;;;;;;;;;;;;;;;;;");
            fWriter.write(";;;;;;;;;;;;;;;;;;");
            
            //TABU
            System.out.println("TABU-----------");
            fWriter.write("\n");
            fWriter.write("TABU;ST70;ST70;ST70;CH130;CH130;CH130;A280;A280;A280;PA654;PA654;PA654;VM1084;VM1084;VM1084;VM1748;VM1748;VM1748");
            fWriter.write("\n");
            fWriter.write("N Ejecucion;Semilla;Coste;EV;Semilla;Coste ;Ev;Semilla;Coste;EV;Semilla;Coste ;Ev;Semilla;Coste;EV;Semilla;Coste ;Ev");
            
            for (int j = 0; j < semillas.length; j++) {
                System.out.println(j+" Semilla: "+semillas[j]);
                fWriter.write("\n");
                fWriter.write(j+";");
                for (String fichero : ficheros) {
                    System.out.println("FICHERO: "+fichero);
                    fWriter.write(semillas[j]+";");
                    tabu = new TabuV4(fichero);
                    tabu.busquedaTabu(semillas[j]);
                    int coste = tabu.getCosteFinal();
                    fWriter.write(coste+";");
                    int evaluaciones = tabu.getEvaluaciones();
                    fWriter.write(evaluaciones+";");
                    
                }
                fWriter.flush();
            }
            System.out.println("FIN TABU***********");

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

}
