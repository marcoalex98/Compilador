package Modelos;


import Modelos.NodoToken;


public class OperToken {
    int tam=0;
    NodoToken prim = null;
    NodoToken ult = null;
    boolean r = false;
    
    public boolean insertarUltimo(int posicion, String minilexem, int numlinea){
        NodoToken nuevo=new NodoToken(posicion, minilexem, numlinea);
        if(prim==null){
            prim=nuevo;
            ult=nuevo;
            System.out.println("Estaba vacia, se inserto 🙂");
            r=true;
        }
          else{
            ult.sig=nuevo;
            ult=nuevo;
            r=true;
           }
        tam++;
        return r;
    }
    public String mostrarLexemaPrimero(){
        return prim.getLexema();
    }
    
    public int mostrarLineaPrimero(){      
        return prim.getNumlinea();
    }
    
    public int mostrarTokenPrimero(){
        return prim.getPosicion();
    }
    
    public NodoToken obtenerPrimero(){
        return prim;
    }

    public boolean InsertarInicio(int posicion, String minilexem, int numlinea) {
        tam++;
        NodoToken nuevo = new NodoToken(posicion, minilexem, numlinea);
        if (prim == null) {
            prim = nuevo;
            ult = nuevo;
            System.out.println("Estaba vacia, se inserto :)");
            r = true;
        } else {
            nuevo.sig = prim;
            prim = nuevo;
            r = true;
        }
        return r;
    }

    public void mostrarDatos() {
        NodoToken aux = prim;
        if (prim == null) {
            System.out.println("No hay ningun elemento en la lista :(");
        } else {
            System.out.println("Elementos de la lista");
            while (aux != null) {
                System.out.println(aux.getPosicion() + "   " + aux.getLexema() + "    " + aux.getNumlinea());
                aux = aux.sig;

            }
        }
    }

    public boolean listaVacia() {
        if (prim == null) {
            return true;
        } else {
            return false;
        }
    }
    public int mostrarPrimero(){
        //return (int) Integer.parseInt(prim.getLexema());
        return prim.getPosicion();
    }

    public boolean eliminarInicio() {
        NodoToken aux = prim;
        if (prim == ult) {
            prim = null;
            ult = null;
            r = true;
        }
        if (prim != null) {
            prim = prim.sig;
            //auxiliar.siguiente es la direccion, solo el apuntador
            aux.sig = null;
            r = true;
        } else {
            System.out.println("Esta vacio");
        }
        return r;
    }
}
