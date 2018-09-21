import java.io.*;
import java.util.*;
public class ArchivosController {
    //Nota super importante para que funcione las configuraciones tienen que ser un numero par
    //si configuraciones no es un numero par entonces va a variar el tamaño o se pueden perder el peso del ultimo nodo
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
    public void escribir_maestro()throws IOException{
        int n;
        long lreg,desplazamiento;
        Maestro=new RandomAccessFile("Aguacatitos","rw");
        Scanner entrada_datos=new Scanner(System.in);
        do{
            System.out.println("ingrese la llave clave del nodo");
            llave=entrada_datos.nextInt();
            if(escribir_indice(llave)){//vamos al metodo escribir indice
                Maestro.writeInt(llave);//si todo sale bien al escribir el indice comensamos a escribir nuestro registro
                System.out.println("ingrese el nodo al que se conecta y su peso nodo/peso si son mas de uno separe por una coma ejemplo 2/5,3/1,4/3");
                datos=entrada_datos.next();
                obtenerNodos(datos);//obtenemos una lista donde nos proporcionan el nodo a que se conecta el nuevo registro y con que peso
                for(n=0;n<configuraciones;n++) {
                    if(conexion[n]!=""){//es posible que no nos ingresen suficientes datos para esto es esta condicion para escribir todos los datos de los arreglos
                        Maestro.writeInt(Integer.parseInt(conexion[n]));
                        Maestro.writeInt(Integer.parseInt(peso[n]));
                    }
                    else {//pero si no hay suficientes datos entonces rellenamos con 0 hasta llegar al final
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
        Maestro.close();
    }
    public void obtenerNodos(String datos){
        int n;
        nodos=datos.split(",");//separamos los nodos y los pesos
        for (n=0;n<configuraciones;n++){
            aux=nodos[n].split("/");//separamos de cada nodo la conexion y el peso
            conexion[n]=aux[0];//guardamos el nodo al que se conecta
            peso[n]=aux[1];//guardamos el peso al que se conecta
        }
    }
    public boolean escribir_indice(int llave)throws IOException{
        boolean continuar=false;//partimos de la idea de que no se puede escribir
        if(buscarIndice(llave)){//nos movemos a comprobar si el indice que quiere ingresar ya existe en nuestro archivo
            indice=new RandomAccessFile("indice","rw");
            indice.writeInt(llave);
            indice.writeInt(posicion);
            continuar=true;
        }
        indice.close();
        return continuar;
    }
    public boolean buscarIndice (int llave) throws IOException{
        indice=new RandomAccessFile("indice","r");
        long pointer,fpointer;
        int count_pos=1;
        int llave_indice;
        boolean existe=true;
        if(indice.length() == 0){
            //si la longitud de nuestro archivo es 0 quiere decir que no tiene nada y por tanto se puede escribir la llave
            return true;
        }
        else {
            //si tiene algo entonces pasamos a buscar registro por  registro
            while ((pointer=indice.getFilePointer())!=(fpointer=indice.length())){
                llave_indice=indice.readInt();//obtenemos la llave
                count_pos=indice.readInt();//obtenemos la posicion de esa llave
                if(llave==llave_indice)//si la llave que buscamos cambiamos existe a false para decir que el registro ya existe
                        existe=false;
            }
            if(existe)
                posicion=count_pos+1;//como obtenemos la posicion de cada registro le sumamos 1 para saber la posicion siguiente
        }
        indice.close();
        return existe;
    }
}
