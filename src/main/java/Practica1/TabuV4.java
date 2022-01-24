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
public class TabuV4 {

    int costeFinal; //coste de la solucion tabu a devolver 
    int PosMov; //posicion del movimiento elegido
    int costeSolucionSPrima;
    int costeSolGenerarAleatorio;
    int costeSolucionVecinaBL; //coste de generar vecinos intercambiando posiciones
    //int costeSolucionVecina;
    LeerFichero lf = new LeerFichero();
    ArrayList<Integer> ciudades;
    int[][] matrizCostes;
    Random aleatorio;
    int evaluaciones;

    public TabuV4(String ruta) {
        evaluaciones=0;
        costeSolucionVecinaBL = 0;
        costeSolGenerarAleatorio = 0;
        PosMov = -1;
        costeSolucionSPrima = 0;
        try {
            lf.leerfichero(ruta);
            this.ciudades = lf.getCiudades();
            this.matrizCostes = lf.getMatrizCostes();
        } catch (IOException ex) {
            Logger.getLogger(Greedy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Integer> busquedaTabu(long semilla) {

        aleatorio = new Random(semilla);
        int n = ciudades.size();
        //Variables de coste
        int costeSolucionMejorGlobal;
        int costeSolucionActual;
        int costeSolucionMejorVecino = -1;

        //Variables de solucion
        ArrayList<Integer> solucionActual = generarSolucionAleatoria();
        ArrayList<Integer> solucionMejorGlobal = (ArrayList<Integer>) solucionActual.clone();
        ArrayList<Integer> solucionCandidata = new ArrayList<>();
        ArrayList<Integer> solucionMejorVecino = new ArrayList<>();

        costeSolucionActual = this.costeSolGenerarAleatorio;
        costeSolucionMejorGlobal = costeSolucionActual;

        //variables tabu
        ArrayList<ArrayList<Integer>> listaTabu = new ArrayList<>(); //lista tabú
        int posTabu = 0; //indica la posición donde habría que insertar el próximo movimiento
        int tamanioTabu = ciudades.size() / 2; //indica el tamaño de la lista tabu. Inicialmente n/2
        ArrayList<ArrayList<Integer>> movimientos; //movimientos. Cada movimiento es un array list con dos valores, i y j.
        ArrayList<ArrayList<Integer>> ListaCandidatos; //Lista de candidatos
        int[][] largoPlazo = new int[n][n]; //estructura de memoria a largo plazo. Se guardarán la frecuencia de LAS CIUDADES
        for (int i = 0; i < n; i++) { //inicializar matriz largo plazo a cero
            for (int j = 0; j < n; j++) {
                largoPlazo[i][j] = 0;
            }
        }
        int numeroCandidatos = 40; //candidatos a generar
        costeSolucionSPrima = 0;

        PosMov = -1;
        for (int i = 0; i < 40 * n; i++) { // 
            movimientos = new ArrayList<>();
            ListaCandidatos = new ArrayList<>();
            int hechos = 0;
            int prim, seg;
            while (hechos < numeroCandidatos) { //generar 40 vecinos candidatos (ListaCandidatos). Guardar los movimientos realizados. Ver que el movimiento no se haya realizado ya
                ArrayList<Integer> solucionAux = (ArrayList<Integer>) solucionActual.clone(); //para no sobreescribir la actual
                prim = aleatorio.nextInt(n);
                seg = aleatorio.nextInt(n);
                if (prim != seg) {
                    ArrayList<Integer> movimiento = new ArrayList<>();
                    if (prim < seg) {
                        movimiento.add(prim);
                        movimiento.add(seg);
                    } else {
                        movimiento.add(seg);
                        movimiento.add(prim);
                    }
                    if (!movimientos.contains(movimiento)) {
                        movimientos.add(movimiento);
                        Collections.swap(solucionAux, prim, seg);
                        ListaCandidatos.add(solucionAux);
                        hechos++;
                    }
                }
            }
            boolean encontrado = false;
            costeSolucionMejorVecino = Integer.MAX_VALUE;
            int posicionMovimiento = -1;
            int k = 0;
            do {//encontrar el mejor candidato entre la ListaCandidatos. O es el mejor entre todos o el mejor global 
                solucionCandidata = ListaCandidatos.get(k);
                int costeSolucionCandidata = calcularCosteSolucion(solucionCandidata);
                if (costeSolucionCandidata < costeSolucionMejorGlobal) { //si supera a la mejor global se actualiza aunque esté en la lista tabu
                    encontrado = true; //para que se salga ya del bucle. Mayor eficiencia
                    solucionMejorVecino = (ArrayList<Integer>) solucionCandidata.clone();
                    solucionMejorGlobal = (ArrayList<Integer>) solucionMejorVecino.clone();
                    costeSolucionMejorGlobal = costeSolucionCandidata;
                    ListaCandidatos.remove(solucionCandidata);
                    posicionMovimiento = k;
                } else if (costeSolucionCandidata < costeSolucionMejorVecino && !listaTabu.contains(solucionCandidata)) { //si no, sino esta en la lista tabu se busca el mejor entre los candidatos
                    solucionMejorVecino = (ArrayList<Integer>) solucionCandidata.clone();
                    ListaCandidatos.remove(solucionCandidata);
                    posicionMovimiento = k;
                }
                k++;
            } while (k < ListaCandidatos.size() && !encontrado);

            //actualizar lista tabu
            posTabu++;
            if (posTabu > tamanioTabu) {
                listaTabu.remove(0);
                posTabu--;
            }
            listaTabu.add(movimientos.get(posicionMovimiento));
            movimientos.remove(posicionMovimiento);

            solucionActual = (ArrayList<Integer>) solucionMejorVecino.clone();

            //actualizar memoria largo plazo con la solucion elegida
            for (int j = 0; i < solucionActual.size() - 1; i++) {
                int ciudad1 = solucionActual.get(j);
                int ciudad2 = solucionActual.get(j + 1);
                largoPlazo[ciudad1 - 1][ciudad2 - 1]++;
                largoPlazo[ciudad2 - 1][ciudad1 - 1]++;
            }

            //reinicio
            if (i > 0 && (i % 40 * n / 5) == 0) {
                Double probabilidad = aleatorio.nextDouble();

                if (probabilidad < 0.25) { //solucion aleatoria
                    solucionActual = generarSolucionAleatoria();
                    listaTabu.clear();
                    posTabu = 0;
                    
                    //actualizar tamaño de la lista tabu
                    Double probTamanioListaTabu = aleatorio.nextDouble();
                    if (probTamanioListaTabu >= 0.5) {
                        tamanioTabu = (int) (tamanioTabu * 1.5);
                    } else {
                        int tama = tamanioTabu / 2;
                        if (tama < 2) {
                            tamanioTabu = 2;
                        } else {
                            tamanioTabu = tama;
                        }

                    }
                }
                if (probabilidad >= 0.25 && probabilidad < 0.75) {//memoria largo plazo
                    ArrayList<Integer> solucionNueva = new ArrayList<>();
                    int ciudadElegida = aleatorio.nextInt(n - 1) + 1;
                    solucionNueva.add(ciudadElegida);

                    for (int z = 0; z < n - 1; z++) {
                        int min = Integer.MAX_VALUE;
                        for (int p = 0; p < n; p++) { //conseguir el menor valor de la fila
                            if (largoPlazo[ciudadElegida - 1][p] < min && ciudadElegida != p+1 && !solucionNueva.contains((Integer)p+1)) {
                                ciudadElegida = p + 1;
                                min = largoPlazo[ciudadElegida - 1][p];
                            }
                        }
                        solucionNueva.add(ciudadElegida);
                    }

                    if (solucionNueva.size() == n) {
                        solucionActual = (ArrayList<Integer>) solucionNueva.clone();
                        listaTabu.clear();
                        posTabu = 0;
                        
                        //actualizar tamaño de la lista tabu
                        Double probTamanioListaTabu = aleatorio.nextDouble();
                        if (probTamanioListaTabu >= 0.5) {
                            tamanioTabu = (int) (tamanioTabu * 1.5);
                        } else {
                            int tama = tamanioTabu / 2;
                            if (tama < 2) {
                                tamanioTabu = 2;
                            } else {
                                tamanioTabu = tama;
                            }

                        }
                    } else {
                        System.out.println("LA SOLUCION GENERADA POR MEMORIA LARGO PLAZO NO ESTÁ COMPLETA. Tamaño: " + solucionNueva.size());
                    }
                }
                if (probabilidad >= 0.75) { //solucion global
                    solucionActual = (ArrayList<Integer>) solucionMejorGlobal.clone();
                    listaTabu.clear();
                    posTabu = 0;
                    
                    //actualizar tamaño de la lista tabu
                    Double probTamanioListaTabu = aleatorio.nextDouble();
                    if (probTamanioListaTabu >= 0.5) {
                        tamanioTabu = (int) (tamanioTabu * 1.5);
                    } else {
                        int tama = tamanioTabu / 2;
                        if (tama < 2) {
                            tamanioTabu = 2;
                        } else {
                            tamanioTabu = tama;
                        }

                    }
                }
            }
        }
        
        costeFinal = calcularCosteSolucion(solucionMejorGlobal);
        return solucionMejorGlobal;
    }

    
    private int calcularCosteSolucion(ArrayList<Integer> solucion) {
        evaluaciones++;
        int costeDevolver = 0;
        for (int i = 0; i < solucion.size() - 1; i++) {
            int ciudad1 = solucion.get(i);
            int ciudad2 = solucion.get(i + 1); 
            costeDevolver += matrizCostes[ciudad1 - 1][ciudad2 - 1];
        }
        return costeDevolver;
    }

    private ArrayList<Integer> generarSolucionAleatoria() {
        costeSolGenerarAleatorio = 0;
        int pos; //posición que se generara aleatoriamente
        
        ArrayList<Integer> conjuntoNodos = (ArrayList<Integer>) this.ciudades.clone(); //nodos candidatos 
        ArrayList<Integer> vecino = new ArrayList<>(); //vecino que se generará
        
         do{            
            pos = aleatorio.nextInt(conjuntoNodos.size()); //se genera una nueva posición
            vecino.add(conjuntoNodos.get(pos)); //se añade a la solución la ciudad correspondiente
            conjuntoNodos.remove(pos);  //se elimina de los candidatos
        }while (!conjuntoNodos.isEmpty()); //se repite mientras queden candidatos por elegir
         
        vecino.add(vecino.get(0)); //para tener en cuenta que vuelve hasta la posición origen de nuevo
        costeSolGenerarAleatorio=calcularCosteSolucion(vecino); //se calcula el coste del vecino generado
        
        return vecino;
    }

    public int getCosteFinal() {
        return costeFinal;
    }

    public int getEvaluaciones() {
        return this.evaluaciones;
    }
}
