
package sudokucdrs;
import java.util.Iterator;

/**
 * @author Constanza Ybarra Trapote
 * @author Daniel Alejandro Nieto Barreto
 * @author Rodrigo Plauchú Rodríguez
 * @author Santiago Fernandez Gutiérrez Zamora
 */
public class Sku {

    private int[][] sudoku;// matriz principal
    /* Arreglo bidimensional en el que cada renglón representa:
       - Arreglo de conjuntos que contienen los elementos en cada renglón.
       - Arreglo de conjuntos que contienen los elementos en cada columna.
       - Arreglo de conjuntos que contienen los elementos en cada región
    */
    private ConjuntoADT<Integer>[][] conjuntos;
    private final ConjuntoADT<Integer> auxiliar;//conjunto que contiene los dígitos de 1 a 9.

    public Sku() {
        sudoku = new int[9][9];
        conjuntos = new ConjuntoA[3][9];
        auxiliar = new ConjuntoA();
        int i;
        for (i = 0; i <= 9; i++) {//Se llena el conjunto auxiliar con los dígitos del 1 al 9.
            auxiliar.Inserta(i);
        }
        for(i=0;i<3;i++) //se recorren los renglones de conjuntos
            for(int j=0;j<9;j++){
                conjuntos[i][j]=new ConjuntoA();//Instanciación de todos los conjuntos.
                for(int k=1;k<10;k++)
                    conjuntos[i][j].Inserta(k);//Se llenan los conjuntos de la matriz dígitos del 1 al 9.
            }
        
    }
    /**
     * <pre>
     * Devuelve el arreglo bidimensional de 9 x 9 que constituye un juego de sudoku.
     * </pre>
     * @return Atributo "sudoku", matriz de 9 x 9.
     */
    public int[][] getSudoku() {
        return sudoku;
    }
    
    /**
     * 
     * @return String con la matriz que constituye un juego de sudoku.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Sudoku\n");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sb.append("[" + sudoku[i][j] + "] ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * <pre>
     * Calcula y devuelve el índice de la región, una matriz de 3 x 3 casillas que
     * constituye una parte del juego de sudoku, análogo a lo que representa 
     * una casilla dentro de ésta.
     * </pre>
     * @param r: Renglón de la casilla de la que se quiere averiguar la región.
     * @param c: Columna de la casilla de la que se quiere averiguar la región.
     * @return Indice de la región, número del 1 al 8.
     */
    public int ObtenRegion(int r, int c) {
        return (3 * (r / 3)) + (c / 3); 
    }

    /**
     * <pre>
     * Determina si es posible insertar un dígito en una determinada casilla,
     * según las reglas del juego de sudoku.
     * </pre>
     * @param r: Renglón en el que se encuentra la casilla
     * @param c: Columna en la que se encuentra la casilla
     * @param digito: valor que se pretende insertar en la casilla
     * @return <ul>
     * <li>true: si es válido insertar el dígito en la casilla.</li>
     * <li>false: si no es válido insertar el dígito en la casilla.</li>
     * </ul>
     */
    private boolean sePuede(int r, int c, int reg,int digito) {
        boolean resp;
        /*Si el renglón, la columna y la región no contienen al dígito y la 
        casilla no está ocupada*/
        if (conjuntos[0][r].Contiene(digito) && conjuntos[1][c].Contiene(digito)
                && conjuntos[2][reg].Contiene(digito) && sudoku[r][c] == 0) {
            resp = true;
        } 
        else {
            resp = false;
        }
        return resp;
    }

    /**
     * <pre> 
     * Agrega un elemento a la matriz del sudoku y simultáneamente lo elimina
     * de los conjuntos de renglón, columna y región que contienen los posibles 
     * dígitos de la casilla donde se agregó.
     * </pre>
     * @param digito: Dígito que se quiere agregar a la matriz
     * @param r: Renglón de la casilla donde se quiere insertar el dígito.
     * @param c: Columna de la casilla donde se quiere insertar el dígito.
     * @return <ul>
     * <li>true: si el dígito se agregó.</li>
     * <li>false: si el dígito no se pudo agregar.</li>
     * </ul>
     */
    public boolean alta(int digito, int r, int c) {
        boolean resp = false;
        if (auxiliar.Contiene(digito)) { //si es un dígito válido (del 1 al 9)
            int reg = ObtenRegion(r, c);
            if (sePuede(r, c,reg, digito)) {//si el dígito es válido en la casilla.
                sudoku[r][c] = digito; 
                /*Eliminar el elemeto de los conjuntos, ya no es un dígito disponible 
                en la región, renglón y columna donde se encuentra la casilla*/
                conjuntos[0][r].Elimina(digito); 
                conjuntos[1][c].Elimina(digito);
                conjuntos[2][reg].Elimina(digito);
                resp = true;
            }
        }
        return resp;
    }


    /**
     * <pre>
     * Devuelve un conjunto con los dígitos que se pueden agregar a una casilla
     * dada de acuerdo a las reglas del sudoku.
     * </pre>
     * @param r: Renglón de la casilla de la que se quieren averiguar los dígitos posibles.
     * @param c: Columna de la casilla de la que se quieren averiguar los dígitos posibles.
     * @return Conjunto que contiene los dígitos disponibles para agregar.
     */
    private ConjuntoADT<Integer> digitosPosibles(int r, int c) {
        int reg = ObtenRegion(r, c);
        ConjuntoADT<Integer> opc = new ConjuntoA(),resp=new ConjuntoA();
        /* Elementos disponibles tanto en los renglones como en las regiones 
        y como en las columnas*/
        opc = conjuntos[0][r].Interseccion(conjuntos[2][reg]); 
        resp = opc.Interseccion(conjuntos[1][c]); 
        return resp;
    }

    /**
     * <pre>
     * Indica la posición que le sucede a una posición dada dentro de una matriz.
     * Se mueve de manera horizontal a la dercha y vertical hacia abajo.
     * </pre>
     * @param r: Renglón actual.
     * @param c: Columna actual.
     * @return Arreglo que contiene a la posición de los renglones en [0] y la de las columnas en [1].
     */
    private int[] posicionSig(int r, int c) {
        int[] pos = new int[2];
        if (c < 8) { //si no se encuentra al final de un renglón
            pos[0] = r;
            pos[1] = c + 1; //moverse a la derecha.
        } else {
            pos[0] = r + 1; //moverse hacia abajo.
            pos[1] = 0;
        }
        return pos;
    }

    /**
     * <pre>
     * Determina si la matriz que se tiene como sudoku se puede resolver, 
     * llama a un método recursivo que resuelve el sudoku. 
     * Altera la matriz existente de la clase para resolverla.
     * </pre>
     * @return <ul>
     * <li>true: el sudoku se pudo resolver.</li>
     * <li>false: el sudoku no se pudo resolver.</li>
     * </ul>
     * @see resuelve1
     */
    public boolean resuelve() {
        return resuelve1(0, 0);
    }

    /**
     * <pre>
     * (Método recursivo)
     * Resuelve un juego de sudoku.
     * Inserta los elementos, que corresponden con las reglas del juego de sudoku, 
     * en las casillas de la matriz de manera orgdenada.
     * (Back-Tracking)
     * </pre>
     * @param posR Posición actual en los renglones.
     * @param posC Posición actual en las columnas.
     * @return <ul>
     * <li>true: La matriz se ha recorrido por completo, acabó y tiene solución.</li>
     * <li>false: La matriz no se puede resolver como sudoku, no tiene solución.</li>
     * </ul>
     */
    private boolean resuelve1(int posR, int posC) {
        boolean resp = false;
        if (posR == 9 && posC == 0) {//estado base, acabó de recorrer la matriz.
            return true;
        } 
        else {
            if (sudoku[posR][posC] == 0) {//checa si la casilla está vacía, o tiene un valor dado por el usuario            
                ConjuntoADT<Integer> posibilidad = digitosPosibles(posR, posC);
                if (!posibilidad.estaVacio()) {//verifica que haya opciones para la casilla en la que se esté situado
                    Iterator<Integer> it=posibilidad.iterator();
                    while(it.hasNext()){
                        int reg = ObtenRegion(posR, posC);
                        int digitoEntrada=it.next();
                        sudoku[posR][posC] = digitoEntrada;
                        conjuntos[2][reg].Elimina(digitoEntrada);
                        conjuntos[1][posC].Elimina(digitoEntrada);
                        conjuntos[0][posR].Elimina(digitoEntrada);
                        int[] posSig = posicionSig(posR, posC);
                        if(resuelve1(posSig[0], posSig[1]))//llamada recursiva
                            return true;
                        else{//en caso de haber fallado con el digito de entrada, se borra y continúa el ciclo
                            sudoku[posR][posC] = 0;
                            conjuntos[0][posR].Inserta(digitoEntrada);
                            conjuntos[1][posC].Inserta(digitoEntrada);
                            conjuntos[2][reg].Inserta(digitoEntrada);
                        }      
                    }
                    return resp;
                } 
                else {
                    return resp;
                }
            } 
            else {
                int[] posSig = posicionSig(posR, posC);
                return resuelve1(posSig[0], posSig[1]);
            }
        }
    }
//Con fines de prueba,temporalmente los métodos probados fueron escritos como públicos, sin embargo, no todos lo son
    /*public static void main(String[] args) {
        Sku miSudok = new Sku();
        ConjuntoADT<Integer> regre=new ConjuntoA();
        int[][] panel = {{5,3,0,0,7,0,0,0,0},
                         {6,0,0,1,9,5,0,0,0},
                         {0,9,8,0,0,0,0,6,0},
                         {8,0,0,0,6,0,0,0,3},
                         {4,0,0,8,0,3,0,0,1},
                         {7,0,0,0,2,0,0,0,6},
                         {0,6,0,0,0,0,2,8,0},
                         {0,0,0,4,1,9,0,0,5},
                         {0,0,0,0,8,0,0,7,9}};
        int[] reg=miSudok.posicionSig(1, 8);
        regre=miSudok.digitosPosibles(0, 0);
        
        System.out.println(regre.toString());//debe imprimir los dígitos del 1 al 9
        System.out.print(reg[0]+" , "+reg[1]+"\t\t");//debe de dar 2 y 0
        System.out.print(miSudok.ObtenRegion(0, 7)+"\t\t");//debe ser 2
        System.out.print(miSudok.alta(5, 0, 0)+"\t\t");//debe ser true
        System.out.print(miSudok.sePuede(0, 7, 2, 5)+"\n");//debe de ser false
        System.out.println(miSudok.toString());//debe de ser una matriz con un cinco en la casilla 0,0
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                miSudok.alta(panel[i][j], i, j);
            }
        }
        System.out.println(miSudok.toString()+"\t");//debe ser la matriz igual a la descrita en panel
        System.out.println(miSudok.resuelve());//debe ser true
        System.out.println(miSudok.toString());//debe mostrar el sudoku resuelto
        
        
    }*/

   
}