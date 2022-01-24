/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Practica1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author josed
 */
public class BusquedaAleatoria {
    int costeFinal;
    LeerFichero lf = new LeerFichero();
    ArrayList<Integer> ciudades; //ciudades del fichero
    int[][] matrizCostes; //costes entre ciudades
    Random aleatorio;
    int evaluaciones;
    
    public BusquedaAleatoria(String ruta){
        evaluaciones = 0;
        costeFinal = 0;
        try {
            lf.leerfichero(ruta);
            this.ciudades = lf.getCiudades();
            this.matrizCostes = lf.getMatrizCostes();
        } catch (IOException ex) {
            Logger.getLogger(Greedy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public ArrayList<Integer> busquedaAleatoria(long semilla){
        aleatorio = new Random(semilla);
        ArrayList<Integer> solucionFinal = new ArrayList<>(); //Array con solucion final
        ArrayList<Integer> solucionBest = new ArrayList<>(); //Array con la mejor solucion hasta el momento
        
        int costeSolucionBest; //mejor coste hasta el momento
        int n = ciudades.size()*1600; //repeticiones que se harán
        
        solucionBest = generarVecinoAleatorio(); //Se genera una solucion aleatoria
        costeSolucionBest=this.costeFinal;        
      
        for (int i = 0; i < n; i++) {
            costeFinal = 0;
            solucionFinal = generarVecinoAleatorio(); //genera un nuevo vecino aleatorio
            if (costeSolucionBest > costeFinal) { //si el nuevo vecino mejora al mejor global se intercambian
                solucionBest = new ArrayList<>(solucionFinal);
                costeSolucionBest = costeFinal;
            }
        }
        
        costeFinal=costeSolucionBest; //para devolver el coste de la solucion
        return solucionBest;
    }

    private ArrayList<Integer> generarVecinoAleatorio() {
        
        int pos; //posición que se generara aleatoriamente
        
        ArrayList<Integer> conjuntoNodos = (ArrayList<Integer>) this.ciudades.clone(); //nodos candidatos 
        ArrayList<Integer> vecino = new ArrayList<>(); //vecino que se generará
        
         do{            
            pos = aleatorio.nextInt(conjuntoNodos.size()); //se genera una nueva posición
            vecino.add(conjuntoNodos.get(pos)); //se añade a la solución la ciudad correspondiente
            conjuntoNodos.remove(pos);  //se elimina de los candidatos
        }while (!conjuntoNodos.isEmpty()); //se repite mientras queden candidatos por elegir
         
        vecino.add(vecino.get(0)); //para tener en cuenta que vuelve hasta la posición origen de nuevo
        costeFinal=calcularCosteSolucion(vecino); //se calcula el coste del vecino generado
        
        return vecino;
    }
    
     private int calcularCosteSolucion(ArrayList<Integer> solucion){
        evaluaciones++;
        int costeDevolver=0;
        for (int i = 0; i < solucion.size()-1; i++) {
            int ciudad1 = solucion.get(i); 
            int ciudad2 = solucion.get(i+1); 
            costeDevolver+=matrizCostes[ciudad1-1][ciudad2-1];
        }
        return costeDevolver;
    }
     
    public int getCoste() {
        return this.costeFinal;
    }
    
    public int getEvaluaciones() {
        return this.evaluaciones;
    }
}
