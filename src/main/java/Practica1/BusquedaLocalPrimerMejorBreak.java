/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Practica1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author josed
 */
public class BusquedaLocalPrimerMejorBreak {
    int coste;
    int costeFinal;
    int costeSolucionVecina;
    LeerFichero lf = new LeerFichero();
    ArrayList<Integer> ciudades;
    int[][] matrizCostes;
    Random aleatorio;
    int evaluaciones;
    
    public BusquedaLocalPrimerMejorBreak(String ruta){
        evaluaciones=0;
        coste = 0;
        costeFinal = 0;
        costeSolucionVecina = 0;
        try {
            lf.leerfichero(ruta);
            this.ciudades = lf.getCiudades();
            this.matrizCostes = lf.getMatrizCostes();
        } catch (IOException ex) {
            Logger.getLogger(Greedy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<Integer> busquedaElPrimerMejor(long semilla){
        /*Variables generales*/
        int n = ciudades.size()*1600;
        int contador = 0;
        aleatorio = new Random(semilla);
        
        /*Variables busqueda local*/
        int costeMejorVecino, costeSolucionActual;//, costeSolucionVecina;
        ArrayList<Integer> solucionActual = generarSolucionAleatoria(); 
        costeSolucionActual = costeFinal;
        ArrayList<Integer> mejorVecino = new ArrayList<>();
        ArrayList<Integer> solucionVecina = new ArrayList<>();
        ArrayList<Integer> solucionAntigua;
        int costeSolucionAntigua;
        
        do{ //repetir
             solucionAntigua = (ArrayList<Integer>) solucionActual.clone();
             costeSolucionAntigua= costeSolucionActual;
             costeMejorVecino = costeSolucionActual;
             
             externo: //etiqueta para el break
             for (int i = 0; i < ciudades.size(); i++) { //repetir para todas las soluciones vecinas posibles
                 for (int j = i+1; j < ciudades.size(); j++) {
                     solucionVecina = generaVecino(solucionActual, i, j); //solucionVecina <-- GeneraVecino(solucionActual). Utiliza el opt-2
                     //el coste de la solucion vecina se calcula en el generaVecino
                   
                     if (costeSolucionVecina < costeMejorVecino) {
                         mejorVecino = new ArrayList<>(solucionVecina);//(ArrayList<Integer>) solucionVecina.clone();
                         costeMejorVecino = costeSolucionVecina;
                         break externo;
                     }
                 }
            }
            if (costeMejorVecino < costeSolucionActual) {
                solucionActual = new ArrayList<>(mejorVecino);
                costeSolucionActual = costeMejorVecino;
            }
            contador++;
        }while(costeSolucionActual < costeSolucionAntigua  && contador <= n);
        
        setCosteFinal(costeSolucionActual);
        return solucionActual;
    }
    
    private ArrayList<Integer> generaVecino(ArrayList<Integer> solucionActual, int i, int j) {
        costeSolucionVecina = 0;
        ArrayList<Integer> solucionVecina = (ArrayList<Integer>) solucionActual.clone();
        
        Collections.swap(solucionVecina, i, j);
        
        costeSolucionVecina = calcularCosteSolucion(solucionVecina);
        
        return solucionVecina;
    }
    
    
    private ArrayList<Integer> generarSolucionAleatoria() {
        
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
    
    public int getCosteFinal() {
        return this.costeFinal;
    }
    
    public int getEvaluaciones() {
        return this.evaluaciones;
    }
    
    public void setCosteFinal(int coste1) {
        costeFinal = coste1;
    }
    
    public void verMCostes(){
        for (int i = 0; i < matrizCostes.length; i++) {
            for (int j = 0; j < matrizCostes[0].length; j++) {
                System.out.println("MCostes["+i+","+j+"] = "+matrizCostes[i][j]);
            }
        }
    }
}
