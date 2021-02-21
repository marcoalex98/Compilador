package Contadores;


public class ContadorAmbito {

    public int Decimal, Binario, Octal, Hexadecimal, Flotante, Cadena, Caracter, Compleja,
            Booleana, None, Arreglo, Tupla, Lista, Registro, Rango, Conjuntos, Diccionario, 
            DatoEstructura;

    public int getDatoEstructura() {
        return DatoEstructura;
    }

    public void setDatoEstructura(int DatoEstructura) {
        this.DatoEstructura = DatoEstructura;
    }

    public ContadorAmbito() {
        this.Decimal = 0;
        this.Binario = 0;
        this.Octal = 0;
        this.Hexadecimal = 0;
        this.Flotante = 0;
        this.Cadena = 0;
        this.Caracter = 0;
        this.Compleja = 0;
        this.Booleana = 0;
        this.None = 0;
        this.Arreglo = 0;
        this.Tupla = 0;
        this.Lista = 0;
        this.Registro = 0;
        this.Rango = 0;
        this.Diccionario = 0;
        this.DatoEstructura = 0;
    }

    public int getTotalAmbito() {
        return Decimal + Binario + Octal + Hexadecimal + Flotante + Cadena + Caracter + 
                Compleja + Booleana + None + Arreglo + Tupla + Lista + Registro + Rango + Conjuntos + Diccionario;
    }

    public int getDecimal() {
        return Decimal;
    }

    public void setDecimal(int Decimal) {
        this.Decimal = Decimal;
    }

    public int getBinario() {
        return Binario;
    }

    public void setBinario(int Binario) {
        this.Binario = Binario;
    }

    public int getOctal() {
        return Octal;
    }

    public void setOctal(int Octal) {
        this.Octal = Octal;
    }

    public int getHexadecimal() {
        return Hexadecimal;
    }

    public void setHexadecimal(int Hexadecimal) {
        this.Hexadecimal = Hexadecimal;
    }

    public int getFlotante() {
        return Flotante;
    }

    public void setFlotante(int Flotante) {
        this.Flotante = Flotante;
    }

    public int getCadena() {
        return Cadena;
    }

    public void setCadena(int Cadena) {
        this.Cadena = Cadena;
    }

    public int getCaracter() {
        return Caracter;
    }

    public void setCaracter(int Caracter) {
        this.Caracter = Caracter;
    }

    public int getCompleja() {
        return Compleja;
    }

    public void setCompleja(int Compleja) {
        this.Compleja = Compleja;
    }

    public int getBooleana() {
        return Booleana;
    }

    public void setBooleana(int Booleana) {
        this.Booleana = Booleana;
    }

    public int getNone() {
        return None;
    }

    public void setNone(int None) {
        this.None = None;
    }

    public int getArreglo() {
        return Arreglo;
    }

    public void setArreglo(int Arreglo) {
        this.Arreglo = Arreglo;
    }

    public int getTupla() {
        return Tupla;
    }

    public void setTupla(int Tupla) {
        this.Tupla = Tupla;
    }

    public int getLista() {
        return Lista;
    }

    public void setLista(int Lista) {
        this.Lista = Lista;
    }

    public int getRegistro() {
        return Registro;
    }

    public void setRegistro(int Registro) {
        this.Registro = Registro;
    }

    public int getRango() {
        return Rango;
    }

    public void setRango(int Rango) {
        this.Rango = Rango;
    }

    public int getConjuntos() {
        return Conjuntos;
    }

    public void setConjuntos(int Conjuntos) {
        this.Conjuntos = Conjuntos;
    }

    public int getDiccionario() {
        return Diccionario;
    }

    public void setDiccionario(int Diccionario) {
        this.Diccionario = Diccionario;
    }

}
