import java.io.*;
import java.util.*;
public class ArchivosController {
    //Nota super importante para que funcione las configuraciones tienen que ser un numero par
    //si configuraciones no es un numero par entonces va a variar el tamaño o se pueden perder el peso del ultimo nodo
    int llave,posicion=1;
    int configuraciones=16;
    StringBuffer nombre;
    String conn_weight="",name;
    int registro_inicial=0;
    RandomAccessFile Maestro;
    String  datos;
    String[]nodos;
    String[]conexion=new String[configuraciones];
    String[]peso=new String[configuraciones];
    String[]aux;
    public void escribir_maestro()throws IOException{
        int n ,w,c;
        long lreg,desplazamiento;
        //indice=new RandomAccessFile("indice","rw");
        Scanner entrada_datos=new Scanner(System.in);
        do{
            Maestro=new RandomAccessFile("Maestro","rw");
            desplazamiento=Maestro.length();
            System.out.println(desplazamiento);
            Maestro.seek(desplazamiento);
            System.out.println("ingrese la llave clave del nodo");
            llave=entrada_datos.nextInt();
            if(escribir_indice(llave)){//vamos al metodo escribir indice
                Maestro.writeInt(llave);
                /*si sale bien al escribir el indice comensamos a escribir nuestro registro*/
                System.out.println("ingrese el nodo al que se conecta y su peso nodo/peso si son mas de uno separe por una coma ejemplo 2/5,3/1,4/3");
                datos=entrada_datos.next();
                obtenerNodos(datos);//obtenemos una lista donde nos proporcionan el nodo a que se conecta el nuevo registro y con que peso
                for(n=0;n<configuraciones;n++) {
                    if(conexion[n]!=null ){//es posible que no nos ingresen suficientes datos para esto es esta condicion para escribir todos los datos de los arreglos
                        c=Integer.parseInt(conexion[n]);
                        w=Integer.parseInt(peso[n]);
                        System.out.println("peso: "+w+" "+"conexion: "+c);
                        Maestro.writeInt(c);
                        Maestro.writeInt(w);
                    }
                    else {//pero si no hay suficientes datos entonces rellenamos con 0 hasta llegar al final
                        Maestro.writeInt(1);
                        Maestro.writeInt(1);
                    }
                }
                System.out.println("ingrese el nombre de la estacion");
                name=entrada_datos.next();
                nombre=new StringBuffer(name);
                nombre.setLength(15);
                Maestro.writeChars(nombre.toString());
                System.out.println("Presione 0 para terminar o 1 para ingresar otro registro");
                registro_inicial=entrada_datos.nextInt();
                Maestro.close();
            }
            else {
                System.out.println("archivo ya existente");
                System.out.println("Presione 0 para terminar o 1 para ingresar otro registro");
                registro_inicial=entrada_datos.nextInt();
                Maestro.close();
            }
        }while(registro_inicial!=0);

    }

    public void obtenerNodos(String datos){
        int n;
        nodos=datos.split(",");//separamos los nodos y los pesos
        for (n=0;n<nodos.length;n++){
            aux=nodos[n].split("/");//separamos de cada nodo la conexion y el peso
            conexion[n]=aux[0];//guardamos el nodo al que se conecta
            peso[n]=aux[1];//guardamos el peso al que se conecta
        }
    }

    public boolean escribir_indice(int llave)throws IOException{
        boolean continuar=false;//partimos de la idea de que no se puede escribir
        long longitud;
        RandomAccessFile file;
        if(!(buscarIndice(llave))){//nos movemos a comprobar si el indice que quiere ingresar ya existe en nuestro archivo
            file=new RandomAccessFile("indice","rw");
            longitud=file.length();
            file.seek(longitud);
            file.writeInt(llave);
            file.writeInt(posicion);
            file.close();
            continuar=true;
            posicion++;
        }
        return continuar;
    }

    public boolean buscarIndice (int llave) throws IOException{
        RandomAccessFile file;
        int llaveLeida,posi,i;
        long tam;
        boolean existe=false;
        file=new RandomAccessFile("indice","rw");
        tam=file.length();
        if(tam==0)
            existe=false;
        else{
            for(i=0;i<tam/8;i++){
                llaveLeida=file.readInt();
                if(llave==llaveLeida){
                    posicion=file.readInt();
                    existe=true;
                }
                else
                    file.readInt();
        }
        file.close();
        }
        return existe;
    }
    public int buscarIndice2 (int llave) throws IOException{
        RandomAccessFile file;
        int llaveLeida,posi=-1,i;
        long tam;
        boolean existe=false;
        file=new RandomAccessFile("indice","rw");
        tam=file.length();
            for(i=0;i<tam/8;i++){
                llaveLeida=file.readInt();
                if(llave==llaveLeida){
                    posi=file.readInt();
                    //System.out.println("posicion: "+posi);
                }
                else
                    file.readInt();
            }

            file.close();
        System.out.println("posicion: "+posi);
        return posi;
    }
    public long rango_desp()throws IOException{
        long desp;
        char[] nomb=new char[15];
        RandomAccessFile file=new RandomAccessFile("Maestro","rw");
        for(int i=0;i<configuraciones*2+1;i++)
            file.readInt();
        for(int c=0;c<nomb.length;c++)
            nomb[c]=file.readChar();
        desp=file.getFilePointer();
        file.close();
        return desp;
    }
    public void buscarNodo(int llave)throws IOException{
        RandomAccessFile file;
        System.out.println(llave);
        long longitud_registro=rango_desp();
        System.out.println("longitud: "+longitud_registro);
        int pos=buscarIndice2(llave),i,dt,key;
        long desplazamiento;
        System.out.println("=)la posicion es:"+pos);
        String nombre,nodos="";
        String cadena;
        char temp,estado;
        char []nomb=new char[15];
        if(pos<0)
            System.out.println("El registro no existe");
        else{
            file=new RandomAccessFile("Maestro","rw");
            desplazamiento=(pos-1)*longitud_registro;
            System.out.println("el desplazamiento es :"+desplazamiento);
            file.seek(desplazamiento);
            key=file.readInt();
            System.out.println("la llave es: "+key);
            for(i=0;i<configuraciones*2;i++){
                dt=file.readInt();
                nodos=nodos+dt+"/,";
            }
            for(i=0;i<nomb.length;i++){
                temp=file.readChar();
                nomb[i]=temp;
            }
            nombre=nomb.toString();
            System.out.println("\n el nombre es: "+nombre);
            System.out.println(nodos);
            System.out.println("su estado es: ");
            file.close();
        }
    }
}
