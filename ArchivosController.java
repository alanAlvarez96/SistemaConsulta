import java.io.*;
import java.util.*;
public class ArchivosController {
    int llave,posicion;
    int configuraciones;
    StringBuffer nombre;
    String conn_weight="",name;
    int registro_inicial=0;
    RandomAccessFile Maestro;
    RandomAccessFile indice;
    String  datos;
    String[]nodos;
    String[]conexion=new String[configuraciones];
    String[]peso=new String[configuraciones];
    String[]aux;
    public ArchivosController()throws IOException{
        indice=new RandomAccessFile("indice","r");
        Maestro=new RandomAccessFile("Aguacatitos","rw");
    }
    public void escribir_maestro()throws IOException{
        int n;

        long lreg,desplazamiento;
        Scanner entrada_datos=new Scanner(System.in);
        do{
            System.out.println("ingrese la llave clave del libro");
            llave=entrada_datos.nextInt();
            if(escribir_indice(llave)){
                Maestro.writeInt(llave);
                System.out.println("ingrese el nodo al que se conecta y su peso nodo/peso si son mas de uno separe por una coma ejemplo 2/5,3/1,4/3");
                datos=entrada_datos.next();
                obtenerNodos(datos);
                for(n=0;n<configuraciones;n++) {
                    if(conexion[n]!=""){
                        Maestro.writeInt(Integer.parseInt(conexion[n]));
                        Maestro.writeInt(Integer.parseInt(peso[n]));
                    }
                    else {
                        Maestro.writeInt(0);
                        Maestro.writeInt(0);
                    }
                }
                System.out.println("ingrese el nombre de la estacion");
                name=entrada_datos.next();
                nombre=new StringBuffer(name);
                nombre.setLength(30);
                Maestro.writeChars(nombre.toString());
                Maestro.writeChar('a');//este ultimo es el estado para saber si ya fue borrado o no
               System.out.println("Presione 0 para terminar o 1 para ingresar otro registro");
               registro_inicial=entrada_datos.nextInt();
            }
            else {
                System.out.println("archivo ya existente");
                System.out.println("Presione 0 para terminar o 1 para ingresar otro registro");
                registro_inicial=entrada_datos.nextInt();
            }

        }while(registro_inicial!=0);
    }
    public void obtenerNodos(String datos){
        int n;
        nodos=datos.split(",");
        for (n=0;n<configuraciones;n++){
            aux=nodos[n].split("/");
            conexion[n]=aux[0];
            peso[n]=aux[1];
        }
    }
    public boolean escribir_indice(int llave)throws IOException{
        boolean continuar=false;
        if(buscarIndice(llave)){
            indice.writeInt(llave);
            indice.writeInt(posicion);
            continuar=true;
        }
        return continuar;
    }
    public boolean buscarIndice (int llave) throws IOException{
        long pointer,fpointer;
        int count_pos=1;
        int llave_indice;
        boolean existe=true;
        if(indice.length() == 0){
            return true;
        }
        else {
            while ((pointer=indice.getFilePointer())!=(fpointer=indice.length())){
                llave_indice=indice.readInt();
                count_pos++;
                if(llave==llave_indice)
                        existe=false;
            }
            if(existe)
                posicion=count_pos;
        }
        return existe;
    }
}
