import java.io.*;
import java.util.*;
public class ArchivosController {
    int llave,posicion,nodos[];
    int configuraciones;
    StringBuffer nombre;
    String conn_weight="",name;
    int registro_inicial=0;
    RandomAccessFile Maestro;
    RandomAccessFile indice;
    public ArchivosController()throws IOException{
        indice=new RandomAccessFile("indice","r");
        Maestro=new RandomAccessFile("Aguacatitos","rw");
    }
    public void escribir_maestro()throws IOException{
        int n;
        long lreg,desplazamiento;

        DataOutputStream indice=new DataOutputStream(new FileOutputStream("indice"));
        Scanner entrada_datos=new Scanner(System.in);
        do{
            System.out.println("ingrese la llave clave del libro");
            llave=entrada_datos.nextInt();
            if(escribir_indice(llave)){

            }
            else {
                System.out.println("archivo ya existente");
            }

        }while(registro_inicial!=0);
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
