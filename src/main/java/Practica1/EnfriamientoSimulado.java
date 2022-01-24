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
public class EnfriamientoSimulado {
    int coste;
    int costeFinal;
    int costeSolucionVecina;
    LeerFichero lf = new LeerFichero();
    ArrayList<Integer> ciudades;
    int[][] matrizCostes;
    Random aleatorio;
    int evaluaciones;
    String ruta;
    
    public EnfriamientoSimulado(String ruta){
        this.ruta = ruta;
        evaluaciones = 0;
        coste = 0;
        costeSolucionVecina = 0;
        try {
            lf.leerfichero(ruta);
            this.ciudades = lf.getCiudades();
            this.matrizCostes = lf.getMatrizCostes();
        } catch (IOException ex) {
            Logger.getLogger(Greedy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<Integer> enfriamientoSimulado(long semilla){
        aleatorio = new Random(semilla);
        ArrayList<Integer> solucionActual, solucionMejor, solucionCandidata = new ArrayList<>();
        int costeSolucionCandidata, costeSolucionActual, costeSolucionMejor;
        
        solucionActual = generarSolucionAleatoria(); //solucion inicial = aleatoria
        costeSolucionActual = this.costeFinal;
        
        solucionMejor = (ArrayList<Integer>) solucionActual.clone();
        costeSolucionMejor = this.costeFinal;
        
        Greedy g = new Greedy(solucionActual, ruta); //se necesita el greedy para una solución cercana a la óptima
        g.greedy();
        double T0 = (0.3/-Math.log(0.3))* g.getCoste(); //temperatura inicial
        double T = T0;
        
        int Lt = 20; //numero de soluciones generadas en cada temperatura
        int K = 0; //cte Boltzmann (iteraciones)
        
        
        
        do{
            for (int i = 0; i < Lt; i++) {
                solucionCandidata = generaVecinoAleatorio(solucionActual); //genera solucion candidata aleatoria
                costeSolucionCandidata = costeSolucionVecina;
                int diferenciaCostos = costeSolucionCandidata - costeSolucionMejor; //diferencia de costes
                if (aleatorio.nextDouble() < Math.exp(-diferenciaCostos/T) || diferenciaCostos<0) { //si es mejor o si la probabilidad lo quiere
                    solucionActual = solucionCandidata; //se actualiza la solucion actual por la candidata, sea mejor o peor
                    costeSolucionActual = costeSolucionCandidata;
                    if (costeSolucionActual < costeSolucionMejor) { //si la acutal mejora a la global
                        solucionMejor = (ArrayList<Integer>) solucionActual.clone();
                        costeSolucionMejor = costeSolucionActual;
                    }
                }
            }
            T = T0 / (1 + K); //Esquema de Cauchy
            K++;
        }while(K<80*ciudades.size()); //criterio parada
        
        setCosteFinal(costeSolucionActual);
        return solucionMejor;
    }
    
     private ArrayList<Integer> generaVecinoAleatorio(ArrayList<Integer> solucionActual) {
        costeSolucionVecina = 0;
        ArrayList<Integer> solucionVecina = (ArrayList<Integer>) solucionActual.clone();
        int n=ciudades.size();
        
        int i = aleatorio.nextInt(n);
        int j = aleatorio.nextInt(n);
        
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
            int ciudad2 = solucion.get(i+1); //System.out.println("ciudad1 + ciudad2: "+ciudad1+" + "+ + ciudad2);
            //System.out.println("Coste: "+mCostes[ciudad1-1][ciudad2-1]);
            costeDevolver+=matrizCostes[ciudad1-1][ciudad2-1];
        }
        //System.out.println("CosteSolucionVecina: "+costeDevolver);
        return costeDevolver;
    }
    

    private void setCosteFinal(int coste) {
        this.costeFinal = coste;
    }
    
    public int getCosteFinal(){
        return this.costeFinal;
    }
    
    public int getEvaluaciones(){
        return this.evaluaciones;
    }
}
