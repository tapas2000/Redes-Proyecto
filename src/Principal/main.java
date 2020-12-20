package Principal;

import java.util.ArrayList;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {

        // TODO Auto-generated method stub
        Scanner sc = new Scanner(System.in);

        System.out.println("Ingrese IP ");

        String num = sc.nextLine();
        String[] informacion = num.split("-");

        ArrayList<String> datos = procesarIp(informacion[0]);
        datos.add(informacion[1]);

        ArrayList<String> datosS = transformarSubred(datos);

        System.out.println("host deseada 24, Subred 128 : " + tranformarIpDecimal(subredEspecifica(datosS, 24, 128)));
        System.out.println("Ips de la Subred 128 : ");
        String []a =  obteneSubredCantidad(datosS,24,258).split(";");

        for (int i = 0; i < a.length; i++) {
            System.out.println("Ip " + i + " : " + tranformarIpDecimal(a[i]));
        }


    }

    /**
     * Metodo que me obtiene las subredes de una ip con la cantidad que se requiere
     * <p>
     * datosSubred 0 : Dirección de red
     * datosSubred 1 : Mascara de Red
     * datosSubred 2 : BroadCast de Red
     * datosSubred 3 : Numero de bits para las subredes
     * datosSubred 4 : Cantidad de subredes que se pueden utilizar
     * datosSubred 5 : Numero de bits para las host
     * datosSubred 6 : Cantidad de host que se pueden utilizar
     * datosSubred 7 : Subredes con su broadcast, separdas por ;
     * datosSubred 8 : Concatenación de bit para las subred
     * datosSubred 9 : Concatenación de bit para los host
     * datosSubred 10 : Guardamos el indice donde termina la mascara
     *
     * @param datos Es el Arraylist con la información de la red.
     * @return Un arraylist con toda la información de las subredes
     */
    public static ArrayList<String> transformarSubred(ArrayList<String> datos) {

        ArrayList<String> datosSubred = new ArrayList<String>();
        //Agregamos la direccion de red, y su mascara, broadacast
        datosSubred.add(datos.get(0));
        datosSubred.add(datos.get(1));
        datosSubred.add(datos.get(2));

        // Encontramos la cantidad de bits que necesitamos para la subredes Log10 (x)/ Log10 (2)
        int bitsRed = Integer.parseInt(datos.get(datos.size() - 1));
        int canSop = (int) (Math.pow(bitsRed, 2));

        // Cantidad de subredes que se pueden utilizar
        datosSubred.add((bitsRed) + "");
        datosSubred.add((canSop - 2) + "");

        //Obtenemos la cantidad de bits para identificar los hosts
        int bitsHosts = 32 - (Integer.parseInt(datos.get(3)) + bitsRed);
        datosSubred.add((bitsHosts) + "");
        datosSubred.add(((int) (Math.pow(bitsHosts, 2))) + "");

        //Ordenamos los bits necesarios para las redes
        String subred = "";
        int i = 0;

        while (i < bitsRed) {
            subred += "0";
            i = i + 1;
        }

        String host = "";

        i = 0;
        //Ordenamos los bits necesarios para los host
        while (i < bitsHosts) {
            host += "0";
            i = i + 1;
        }

        //hallamos el indice donde empieza la subred
        int indice = 0;

        for (indice = 0; indice < datos.get(1).length(); indice++) {
            if (datos.get(1).charAt(indice) == '0') {
                indice = indice - 1;
                break;
            }
        }

        //Definimos las subredes con su recpectivo broadcast
        ArrayList<String> subredes = obtenerSubred(datos.get(0), datos.get(2), indice, bitsRed, subred, host);
        //Guardamos todas las subredes en un string donde estaran separadas por ;
        String subredesCompacatas = "";

        for (i = 0; i < subredes.size(); i++) {
            System.out.println("Subred " + i + " : " + subredes.get(i));

            if (i != subredes.size() - 1) {
                subredesCompacatas += subredes.get(i) + ";";
            } else {
                subredesCompacatas += subredes.get(i);
            }
        }
        //Guardamos todas las subredes en el arrayList
        datosSubred.add(subredesCompacatas + "");
        //Guardamos todas los bits para los subredes
        datosSubred.add(subred);
        //Guardamos todos los bits para las host
        datosSubred.add(host);
        //Guardamos el indice donde empieza la subred
        datosSubred.add(indice + "");

        return datosSubred;
    }

    /**
     * Metodo que halla las subredes de una red, dada sus condiciones, es decir la cantidad de subredes que se necesita
     * los bits que se le asignan al mismo.
     *
     * @param ip        Es la ip original donde se hallara las subredes
     * @param broadcast Es el broadcast de la red completa
     * @param indice    Es el indice donde empieza la subred
     * @param subred    Es la cantidad de digitos en 0 que se necesitan para identificar la subred
     * @return ArrayList con las direcciones de subred y su respectiva broadcast
     */

    public static ArrayList<String> obtenerSubred(String ip, String broadcast, int indice, int bitsSubred, String subred, String host) {

        ArrayList<String> subredes = new ArrayList<String>();

        String subredCompletaI = ip.substring(0, indice + 1);
        String subredCompletaD = "";

        //Como indice, me indica, donde termina la mascara de red, significa que la parte derecha empeiza indice + 1
        // después de esta
        // Se debe tener en cuenta que, 8 - 17 - 26, son los indices donde hay un '.' el cual debe ser ignorado
        if ((indice + 1) != 8 && (indice + 1) != 17 && (indice + 1) != 26) {
            subredCompletaD = ip.substring(indice + 1, 35);
        } else {
            subredCompletaD = ip.substring(indice + 2, 35);
            subredCompletaI += ".";
        }

        System.out.println("\n Parte izquierda : " + subredCompletaI);
        System.out.println("Parte derecha : " + subredCompletaD);

        String auxiliarInicio = "";
        String auxiliarSubred = "";
        String auxiliarSubredInicio = "";
        String auxiliarSubredFinal = "";

        String subredTope = subred.replace("0", "1");


        int i = 0;
        int a = 0;
        int b = 0;

        a = Integer.parseInt(tranformarIpDecimal(subred).replace(".", ""));
        b = Integer.parseInt(tranformarIpDecimal(subredTope).replace(".", ""));

        while (a <= b) {

            System.out.println("Esta es la subred: " + subred);
            System.out.println("Esta son los host: " + host);
            auxiliarInicio = "";
            auxiliarSubred = "";
            auxiliarSubredInicio = "";
            auxiliarSubredFinal = "";


            System.out.println("Valor del subfijo de la subred: " + a);
            subred = convertirBinario(Long.parseLong(a + ""));

            while (subred.length() < bitsSubred) {
                subred = "0" + subred;
            }

            auxiliarSubredInicio = subred + host + "";
            auxiliarSubredFinal = subred + host.replaceAll("0", "1") + "";

            for (i = 0; i < auxiliarSubredInicio.length(); i++) {
                if (subredCompletaD.charAt(i) != '.') {
                    auxiliarSubred += auxiliarSubredInicio.charAt(i);
                } else {
                    auxiliarSubred += ".";
                    auxiliarSubred += auxiliarSubredInicio.charAt(i);
                }
            }

            auxiliarSubred = subredCompletaI + auxiliarSubred + "-" + subredCompletaI;

            for (i = 0; i < auxiliarSubredFinal.length(); i++) {
                if (subredCompletaD.charAt(i) != '.') {
                    auxiliarSubred += auxiliarSubredFinal.charAt(i);
                } else {
                    auxiliarSubred += ".";
                    auxiliarSubred += auxiliarSubredFinal.charAt(i);
                }
            }
            //Agregamos el rango de subredes
            subredes.add(auxiliarSubred + "");
            //convertimos los bits para los host, es su posición inicial
            host = host.replace('1', '0');

            //empezamos en la sigueinte subred
            a = Integer.parseInt(tranformarIpDecimal(subred).replace(".", "")) + 1;
        }

        System.out.println("\n Ultima subred:" + subredCompletaI + auxiliarInicio + "-" + broadcast + "\n");

        return subredes;
    }

    /**
     * Metodo que me retorna una ip especifica dado el numero del host y subred
     *
     * @param datos        Es el Arraylist de datos con la información
     * @param indiceSubred Es el indice de la subred deseada
     * @param host         Es el host que se desea calcular
     * @return La ip en binario de la subred deseada
     */

    public static String subredEspecifica(ArrayList<String> datos, int indiceSubred, int host) {

        int indice = Integer.parseInt(datos.get(10));
        int bitSubred = datos.get(8).length();
        int cont = 0;
        int i = indice;

        String[] ipSubredEscogida = datos.get(7).split(";")[indiceSubred].split("-");
        String ipOriginal = ipSubredEscogida[0];
        System.out.println(ipOriginal);
        String auxiliarSubred = "";

        //Encontramos donde termina la subred, y avanzamos 1 posicion mas adelante de esta
        while (cont < (bitSubred)) {
            if (ipOriginal.charAt(i) != '.') {
                i = i + 1;
                cont = cont + 1;
            } else {
                i = i + 1;
            }
        }

        //Hacemos la aprtición con el i
        String subredCompletaI = ipOriginal.substring(0, i);
        String subredCompletaD = "";

        if ((i) != 8 && (i) != 17 && (i) != 26) {
            subredCompletaD = ipOriginal.substring(i, 35);
        } else {
            subredCompletaD = ipOriginal.substring(i + 1, 35);
            subredCompletaI += ".";
        }

        //Convertimos el numero host bucado a binario
        String hosts = convertirBinario(Long.parseLong(host + ""));
        int bitsHost = datos.get(9).length();

        //Agregamos los 0 que falten para completar la cantidad de host
        while (hosts.length() < bitsHost) {
            hosts = "0" + hosts;
        }

        for (i = 0; i < hosts.length(); i++) {
            if (subredCompletaD.charAt(i) != '.') {
                auxiliarSubred += hosts.charAt(i);
            } else {
                auxiliarSubred += ".";
                auxiliarSubred += hosts.charAt(i);
            }
        }

        System.out.println(subredCompletaI);

        return subredCompletaI + auxiliarSubred;
    }

    /**
     * Metodo que retona "cantidad" de ips pertenecientes a uns susbred
     * @param datos Es el Arraylist de datos con la información
     * @param indiceSubred Es el indice de la subred deseada
     * @param cantidad Es la cantidad de ips que queremos
     * @return Una concatenación con las ips en binario pertenecienes a una subred
     */
    public static String obteneSubredCantidad(ArrayList<String> datos, int indiceSubred, int cantidad) {

        int indice = Integer.parseInt(datos.get(10));
        int bitSubred = datos.get(8).length();
        int bitsHost = datos.get(9).length();
        int cont = 0;
        int i = indice;

        String[] ipSubredEscogida = datos.get(7).split(";")[indiceSubred].split("-");
        String ipOriginal = ipSubredEscogida[0];
        System.out.println(ipOriginal);
        String auxiliarSubred = "";

        //Encontramos donde termina la subred, y avanzamos 1 posicion mas adelante de esta
        while (cont < (bitSubred)) {

            if (ipOriginal.charAt(i) != '.') {
                i = i + 1;
                cont = cont + 1;
            } else {
                i = i + 1;
            }
        }

        //Hacemos la aprtición con el i
        String subredCompletaI = ipOriginal.substring(0, i);
        String subredCompletaD = "";

        if ((i) != 8 && (i) != 17 && (i) != 26) {
            subredCompletaD = ipOriginal.substring(i, 35);
        } else {
            subredCompletaD = ipOriginal.substring(i + 1, 35);
            subredCompletaI += ".";
        }

        //Convertimos el numero host bucado a binario
        String hosts = datos.get(9);
        String ips = "";
        int j = 0;
        int aux = 0;

        while (j < cantidad) {
            auxiliarSubred = "";

            for (i = 0; i < hosts.length(); i++) {
                if (subredCompletaD.charAt(i) != '.') {
                    auxiliarSubred += hosts.charAt(i);
                } else {
                    auxiliarSubred += ".";
                    auxiliarSubred += hosts.charAt(i);
                }
            }

            aux += 1;
            hosts = convertirBinario(Long.parseLong(aux + ""));

            //Agregamos los 0 que falten para completar la cantidad de host
            while (hosts.length() < bitsHost) {
                hosts = "0" + hosts;
            }
            j = j + 1;

            if (j != cantidad) {
                ips += subredCompletaI + auxiliarSubred + ";";
            }else{
                ips += subredCompletaI + auxiliarSubred ;
            }
        }


        return ips;
    }

    /**
     * Metodo que retorna la subred en binario, a la que pertenece una ip dada
     *
     * @param subredes Es un arreglos con todas las subredes incluyendo su broadcast
     * @param ip       Es la ip que se queire buscar
     * @return La información de la subred a la que pertenece
     */
    public static String verificarPertenecia(String[] subredes, String ip) {
        String subredPropietaria = "No pertenece a ninguna subred";

        for (int i = 0; i < subredes.length; i++) {
            String[] subredesP = subredes[i].split("-");
            if (verificarSubred(subredesP[0], subredesP[1], ip)) {
                subredPropietaria = subredesP[0];
            }
        }

        return subredPropietaria;
    }

    /**
     * Metodo que me permire saber si una ip esta dentro de una red o subred
     *
     * @param ip        Es la ip de inicio
     * @param broadcast Es el broadcas de la red o subred
     * @param ipNueva   Es la ip que se va a buscar
     * @return True si la ipNueva pertecec a la red, false sino lo determina
     */
    public static boolean verificarSubred(String ip, String broadcast, String ipNueva) {

        String[] ipPartida = ip.split("\\.");
        String[] ipBPartida = broadcast.split("\\.");
        String[] ipNPartida = ipNueva.split("\\.");

        ArrayList<Integer> valor = new ArrayList<Integer>();
        ArrayList<Integer> valorB = new ArrayList<Integer>();
        ArrayList<Integer> valorN = new ArrayList<Integer>();


        int i = 0;

        for (i = 0; i < ipPartida.length; i++) {

            valor.add(Integer.parseInt(tranformarIpDecimal(ipPartida[i])));
            valorB.add(Integer.parseInt(tranformarIpDecimal(ipBPartida[i])));
            valorN.add(Integer.parseInt(tranformarIpDecimal(ipNPartida[i])));

        }

        //Primer octeto
        if ((valorN.get(0) >= valor.get(0)) && (valorN.get(0) <= valorB.get(0))) {
            //Segundo octeto
            if ((valorN.get(1) >= valor.get(1)) && (valorN.get(1) <= valorB.get(1))) {
                //Tercer octeto
                if ((valorN.get(2) >= valor.get(2)) && (valorN.get(2) <= valorB.get(2))) {
                    //Cuarto octeto
                    if ((valorN.get(3) >= valor.get(3)) && (valorN.get(3) <= valorB.get(3))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


//<--------------------- Punto 1 --------------------->

    /**
     * Metodo que dada una ip retorna un arraylista con la  informacion donde:
     * datos[0] : Ip de red
     * datos[1] : Mascara de red es
     * datos[2] : Broadcast de red es:
     * datos[3] : Numero de bit para identificar los red
     * datos[4] : Numero de bit para identificar la host
     * datos[5] : Numero hosts
     *
     * @param num Es la ip que se le busca toda su info
     * @return un array list con toda la información pertinente de la ip
     */
    public static ArrayList<String> procesarIp(String num) {

        ArrayList<String> datos = new ArrayList<String>();

        String[] informacion = num.split("/");
        String ipBinaria = tranformarIpBinario(informacion[0]);
        String mascaraBinaria = encontrarMascara(Integer.parseInt(informacion[1]));
        String broadcastBinaria = encontrarBroadcast(ipBinaria, mascaraBinaria);
        //Long decimal = Long.parseLong(num);

        //Tranformar Ip de Binario a Decimal, y viceversa
        System.out.println("Ip de red es: " + tranformarIpDecimal((ipBinaria)) + " " + ipBinaria);
        datos.add(ipBinaria);

        System.out.println("Mascara de red es: " + tranformarIpDecimal((mascaraBinaria)) + " " + mascaraBinaria);
        datos.add(mascaraBinaria);

        System.out.println("Broadcast de red es: " + tranformarIpDecimal(broadcastBinaria) + " " + broadcastBinaria);
        datos.add(broadcastBinaria);

        System.out.println("Numero de bit para identificar los red: " + informacion[1]);
        datos.add(informacion[1]);

        System.out.println("Numero de bit para identificar la host: " + (32 - Integer.parseInt(informacion[1])));
        datos.add((32 - Integer.parseInt(informacion[1])) + "");

        System.out.println("Numero de direcciones que se pueden asignar a los hosts en la red: " + (Math.pow(2, 32 - Integer.parseInt(informacion[1]))));
        datos.add(Math.pow(2, 32 - Integer.parseInt(informacion[1])) + "");
        System.out.println("Rango de direcciones: " + establecerRango(ipBinaria, broadcastBinaria));

        return datos;
    }

    /**
     * Metodo que dada una ip decimal, este la tranforma a su versión binaria
     *
     * @param ip es la ip que se quiere transformar
     * @return la dirección ip transformada en su version binaria
     */
    public static String tranformarIpBinario(String ip) {

        String[] octetos = ip.split("\\.");
        String ipBinaria = "";

        for (int i = 0; i < octetos.length; i++) {
            String ipAxiliar = convertirBinario(Long.parseLong(octetos[i]));

            while (ipAxiliar.length() < 8) {
                ipAxiliar = "0" + ipAxiliar;
            }

            if (i != 3) {
                ipBinaria += ipAxiliar + ".";
            } else {
                ipBinaria += ipAxiliar;
            }
        }
        return ipBinaria;
    }

    /**
     * Metodo que encuentra el rango de dirrecines dada una dirección ip y su mascara de red ambas en forma decimal
     *
     * @param ip        Esla ip de la red en su versión decimal
     * @param broadcast Es la broadcast de red en su verisión decimal
     * @return El rango de direcciones de la red
     */
    public static String establecerRango(String ip, String broadcast) {
        String[] parcicionA = tranformarIpDecimal(ip).split("\\.");
        String[] parcicionB = tranformarIpDecimal(broadcast).split("\\.");

        ip = parcicionA[0] + "." + parcicionA[1] + "." + parcicionA[2] + "." + (Integer.parseInt(parcicionA[3]) + 1);
        broadcast = parcicionB[0] + "." + parcicionB[1] + "." + parcicionB[2] + "." + (Integer.parseInt(parcicionB[3]) - 1);
        return ip + " - " + broadcast;
    }

    /**
     * Metodo que dada una ip Binaria, este la tranforma a su versión Decimal
     *
     * @param ip es la ip que se quiere transformar
     * @return la dirección ip transformada en su version Decimal
     */
    public static String tranformarIpDecimal(String ip) {

        String[] octetos = ip.split("\\.");
        String ipDecimal = "";

        for (int i = 0; i < octetos.length; i++) {
            String ipAxiliar = convertirDecimal(octetos[i]).toString();

            if (i != 3) {
                ipDecimal += ipAxiliar + ".";
            } else {
                ipDecimal += ipAxiliar;
            }
        }
        return ipDecimal;
    }

    /**
     * Metodo que retorna la mascar de red en binario, dada un numero entero
     *
     * @param num Es la cantidad de bits desgnada para hacer el broadcast
     * @return una cadena en binario del broadcast de red
     */
    public static String encontrarMascara(int num) {

        String broadscast = "";
        int cont = 0;
        System.out.println("Lo que llega " + num);

        for (int i = 0; i < num; i++) {

            if (cont < 8) {
                broadscast += "1";
                cont += 1;
            } else {
                broadscast += ".";
                cont = 0;
                i = i - 1;
            }
        }

        //Ahora compleatamos, como . es un caracter especial debe ir precesido de  \\
        String[] particion = broadscast.split("\\.");

        if (particion.length == 4) {

            while (particion[3].length() < 8) {
                particion[3] = particion[3] + "0";
            }

            return particion[0] + "." + particion[1] + "." + particion[2] + "." + particion[3];

        } else {

            if (particion.length == 3) {

                while (particion[2].length() < 8) {
                    particion[2] = particion[2] + "0";
                }

                return particion[0] + "." + particion[1] + "." + particion[2] + "." + "00000000";

            } else {

                if (particion.length == 2) {

                    while (particion[1].length() < 8) {
                        particion[1] = particion[1] + "0";
                    }

                    return particion[0] + "." + particion[1] + "." + "00000000.00000000";

                } else {

                    if (particion.length == 1) {

                        while (particion[0].length() < 8) {
                            particion[0] = particion[0] + "0";
                        }

                        return particion[0] + ".00000000.00000000.00000000";

                    } else {

                        return "Fallo";

                    }
                }
            }
        }


    }

    /**
     * Metodo que enceuntra el broadcast dada una dirección ip y su mascara de red ambas en forma binaria
     *
     * @param ip      Esla ip de la red en su versión binaria
     * @param mascara Es la mascara de red en su verisión binaria
     * @return EL broadcast de red en su version  binaria
     */
    public static String encontrarBroadcast(String ip, String mascara) {
        int i = 0;
        for (i = 0; i < mascara.length(); i++) {

            if (mascara.charAt(i) == '0') {
                break;
            }
        }

        String broadCast = ip.substring(0, i);

        String[] particion = broadCast.split("\\.");

        if (particion.length == 4) {

            while (particion[3].length() < 8) {
                particion[3] = particion[3] + "1";
            }

            return particion[0] + "." + particion[1] + "." + particion[2] + "." + particion[3];

        } else {

            if (particion.length == 3) {

                while (particion[2].length() < 8) {
                    particion[2] = particion[2] + "1";
                }

                return particion[0] + "." + particion[1] + "." + particion[2] + "." + "11111111";

            } else {

                if (particion.length == 2) {

                    while (particion[1].length() < 8) {
                        particion[1] = particion[1] + "1";
                    }

                    return particion[0] + "." + particion[1] + "." + "11111111.11111111";

                } else {

                    if (particion.length == 1) {

                        while (particion[0].length() < 8) {
                            particion[0] = particion[0] + "0";
                        }

                        return particion[0] + ".11111111.11111111.11111111";

                    } else {

                        return "Fallo";

                    }
                }
            }
        }
    }

    /**
     * Metodo que transforma de decimal a uno binario
     *
     * @param decimal es el numero que se quiere transforma a binario
     * @return el numero resultante de la tranformación
     */
    public static String convertirBinario(Long decimal) {
        return Long.toBinaryString(decimal);
    }

    /**
     * Metodo que tranforma de binario a decimal
     *
     * @param binario es el numero decimal que se va a tranformar
     * @return el numero resultante de la tranformación
     */
    public static Long convertirDecimal(String binario) {
        return Long.parseLong(binario, 2);
    }
}
