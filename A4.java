/*
 * Nom du fichier : Projet_Transport.java
 * Auteur : Grp???
 * Date de création : 29 octobre 2023
 * Description : Résoud des problème de transport classic
 */

// === Section 1: Imports ===
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class A4
{
// === Section 2: Affichage ===
  public static final int MARGING = 1;

  /**
   * Renvoie la longueur en termes de chaîne de caractères d'un entier.
   * @param i L'entier dont on veut calculer la longueur.
   * @return La longueur de la chaîne représentant l'entier.
   */
  public static int lengthInt(int i)
  {
      int counter = 1;      
      if(i < 0)
        counter++;

      while((i = i/10) != 0)
        counter++;
      
      return(counter);
  }

  /**
   * Renvoie la longueur maximal en termes de chaîne de caractères d'une matrice d'entier.
   * @param mat Le tableau dont on veut trouver la longueur maximal en termes de chaîne de caractères.
   * @return La longueur maximal en termes de chaîne de caractères de la matrice.
   */
  public static int maxLengthIntMat(int[][] mat) // Renvoie la longueur max en terme de chaine de caractère d'un tableau d'entier
  {
    int maxLength = lengthInt(mat[0][0]);

    for(int[] tab : mat)
    {
      for(int i : tab)
      {
        int lengthofInt = lengthInt(i);

        if(lengthofInt > maxLength)
          maxLength = lengthofInt;
      }
    }
    return(maxLength);
  }

  /**
   * Affiche n espace dans le terminale.
   * @param n Le nombre d'espace à afficher.
   */
  public static void printSpaces(int n)
  {
      System.out.print(new String(new char[n]).replace('\0', ' '));
  }

  /**
   * Affiche la matrice avec une identation correcte
   * @param mat La matrice à afficher dans le terminale.
   */
  public static void printMatrice(int[][] mat)
  {
    int Length = maxLengthIntMat(mat)+1;
    for (int[] tab : mat)
    {
      for (int i : tab)
      {
        printSpaces(Length-lengthInt(i));
        System.out.print(i);
      }
      System.out.println();
    }
    System.out.println();
  }

  /**
   * Affiche la matrice avec certaines otions d'en-tête
   * @param mat La matrice a afficher
   * @param indice Les différentes options (0 pour juste afficher, 1 pour afficher les en-tête (C et L) sauf pour la dernière ligne/colonne, 2 pour afficher toutes les en-tête)
   */
  public static void printMatrice(int[][] mat, int indice) //0: On affiche juste la matrice, 1: On affiche la matrice avec les indice Cn et Pn sauf le dernier, 2: Comme le 1 avec les dernière lignes/colonne
  {
    int length = maxLengthIntMat(mat)+MARGING;
    if(mat.length < mat[0].length)
      length = (lengthInt(mat[0].length) +1 +MARGING > length)?lengthInt(mat[0].length) +1 +MARGING:length;
    else
      length = (lengthInt(mat.length) +1 +MARGING > length)?lengthInt(mat.length) +1 +MARGING:length;

    printSpaces(length);
    for (int i = 1; i < mat[0].length; i++)
    {
      printSpaces(length-lengthInt(i)-1);
      System.out.print("C" + i);
    }
    if (indice == 2)
    {
      printSpaces(length-lengthInt(mat[0].length)-1);
      System.out.print("C" + mat[0].length);
    }
    System.out.println();

    for (int i = 1; i < mat.length+1; i++)
    {
      printSpaces(length-lengthInt(i)-1);
      if (indice == 2)
        System.out.print("P" + i);
      else{
        if (indice == 1 && i < mat.length)
          System.out.print("P" + i);
        else
          printSpaces(lengthInt(i)+1);
      };

      for (int j : mat[i-1])
      {
        printSpaces(length-lengthInt(j));
        System.out.print(j);
      }
      System.out.println();
    }
    System.out.println();
  }

  /**
   * Pour afficher les matrice de pénalité
   * @param mat La matrice de cout
   * @param line La liste des ligne à comptabilisé
   * @param column La liste des colonne à comptabilisé
   */
  public static void printMatrice(int[][] mat, ArrayList<Integer> line, ArrayList<Integer> column) //0: On affiche juste la matrice, 1: On affiche la matrice avec les indice Cn et Pn sauf le dernier, 2: Comme le 1 avec les dernière lignes/colonne
  {
    int length = (line.get(line.size()-1) > column.get(column.size()-1))?lengthInt(line.get(line.size()-1)):lengthInt(column.get(column.size()-1));
    length = (maxLengthIntMat(mat) > length)?maxLengthIntMat(mat):length;
    length += MARGING;
    
    printSpaces(length); // On affiche les commande
    for(int i : column){
      printSpaces(length - (lengthInt(i+1) + 1));
      System.out.print("C" + (i+1));
    }
    System.out.println();

    for(int i : line){ // On affiche les provision
      printSpaces(length - (lengthInt(i+1) + 1));
      System.out.print("P" + (i+1));
      for(int j : column){
        printSpaces(length - lengthInt(mat[i][j]));
        System.out.print(mat[i][j]);
      }
      int penality = deltaMinProv(mat, column, i)[0]; // Et les pénalité
      printSpaces(length - lengthInt(penality));
      System.out.print(penality);
      System.out.println();
    }

    printSpaces(length);
    for(int i : column){
      int penality = deltaMinComm(mat, line, i)[0];
      printSpaces(length - lengthInt(penality));
      System.out.print(penality);
    }
    System.out.println();
  }

// === Section 3: Ramplissage de la matrice de transport ===
  /**
   * Remplit le tableau donné en respectant l'algorithme Nord-Ouest
   * @param matTransport La matrice de transport à remplir
   */
  public static void Nord_Ouest(int[][] matTransport)
  {
    int i = 0, j = 0;
    while(i < matTransport.length-1 && j < matTransport[0].length-1)
    {
      int maxCommande = matTransport[matTransport.length-1][j];
      int maxProvision = matTransport[i][matTransport[0].length-1];
      for (int k = 0; k < i; k++)
        maxCommande -= matTransport[k][j];

      for (int k = 0; k < j; k++)
        maxProvision -= matTransport[i][k];

      int max_P_C = (maxCommande > maxProvision)?maxProvision:maxCommande;
      matTransport[i][j] = max_P_C;
      System.out.println("On ajoute " + max_P_C + " a P" + (i+1) + ", C" + (j+1));
      if(max_P_C == maxCommande)
        j++;
      if(max_P_C == maxProvision)
        i++;
    }
  }

  /**
   * Renvoie le delta des 2 plus petit coup des provision
   * @param matCout La matrice des cout
   * @param comm La liste des comm encore en jeux
   * @param index L'index de la provision à regarder (la ligne)
   * @return Un tableau [delta, index min]
   */
  public static int[] deltaMinProv(int[][] matCout, ArrayList<Integer> comm, int index){
    int minIndex = comm.get(0);
    for(int i : comm){
      minIndex = (matCout[index][i] < matCout[index][minIndex])?i:minIndex;
    }

    int min = (comm.size() > 1)?Math.abs(matCout[index][comm.get(0)] - matCout[index][comm.get(1)]):0; // Pour éviter que si il ne reste plus que 1 comm il appel l'index 1 pour l'affichage
    for(int i : comm){
      if(i != minIndex){
        min = (matCout[index][i] - matCout[index][minIndex] < min)?matCout[index][i] - matCout[index][minIndex]:min;
      }
    }
    return new int[]{min, minIndex};
  }

  /**
   * Renvoie le delta des 2 plus petit coup des comm
   * @param matCout La matrice des cout
   * @param prov La liste des provision encore en jeux
   * @param index L'index de la comm à chercher (la colonne)
   * @return Un tableau [delta, index min]
   */
  public static int[] deltaMinComm(int[][] matCout, ArrayList<Integer> prov, int index){
    int minIndex = prov.get(0);
    for(int i : prov){
      minIndex = (matCout[i][index] < matCout[minIndex][index])?i:minIndex; // On récupére l'index du plus petit de la colonne
    }

    int min = (prov.size() > 1)?Math.abs(matCout[prov.get(0)][index] - matCout[prov.get(1)][index]):0;
    for(int i : prov){
      if(i != minIndex){
        min = (matCout[i][index] - matCout[minIndex][index] < min)?matCout[i][index] - matCout[minIndex][index]:min; // On prend celui qui à la différence la plus petite
      }
    }
    return new int[]{min, minIndex};
  }

  /**
   * Pour connaitre la capacité utiliser par les provisions DANS le tableau à l'instant t
   * @param matTransport La matrice de transport
   * @param index L'index de la provison (ligne) à regarder
   * @return Renvoie le total utiliser de la provision index
   */
  public static int useLine(int[][] matTransport, int index){
    int ttLigne = 0;
    for(int i = 0; i < matTransport[0].length-1; i++)
      ttLigne += matTransport[index][i];
    return ttLigne;
  }

  /**
   * Pour connaitre la capacité utiliser par les commandes DANS le tableau à l'instant t
   * @param matTransport La matrice de transport
   * @param index L'index de la commande (ligne) à regarder
   * @return Renvoie le total utiliser de la commande index
   */
  public static int useColumn(int[][] matTransport, int index){
    int ttColonne = 0;
    for(int i = 0; i < matTransport.length-1; i++)
      ttColonne += matTransport[i][index];
    return ttColonne;
  }

  public static int maxTransport(int[][] matTransport, int x, int y){
    int maxLine = matTransport[x][matTransport[0].length-1] - useLine(matTransport, x);
    int maxColonne = matTransport[matTransport.length-1][y] - useColumn(matTransport, y);
    int res = (maxLine < maxColonne)?maxLine:maxColonne;
    return res;
  }


  /**
   * Calcul la case de transport a maximer en suvant les régles de BalasHammer
   * @param matTransport La matrice de transport
   * @param matCout La matrice des coût
   * @param prov La liste des provision non maximiser
   * @param comm La liste des commande non maximiser
   * @return Un tableau [Quantité, x, y]
   */
  public static int[] deltaBalasHammer(int[][] matTransport, int[][] matCout, ArrayList<Integer> prov, ArrayList<Integer> comm){
    ArrayList<int[]> minProv = new ArrayList<int[]>(); // On fait les delta sur tout les lignes
    for(int i : prov)
      minProv.add(deltaMinProv(matCout, comm, i));

    ArrayList<int[]> minComm = new ArrayList<int[]>(); // On fait les delta sur tout les colonnes
    for(int i : comm)
      minComm.add(deltaMinComm(matCout, prov, i));

    ArrayList<int[]> deltaMax = new ArrayList<int[]>();
    deltaMax.add(new int[]{minProv.get(0)[0], prov.get(0), minProv.get(0)[1]}); //delta, x, y (on met une première valeur pour commencer)

    for(int i = 1; i < minProv.size(); i++){ // Pour chaque itération (chaque delta des provission, on vas comparer au delta max que l'on a trouvé pour le moment)
      if(deltaMax.get(0)[0] < minProv.get(i)[0]){ // Si on en trouve un supérieur, alors, on recrée une liste avec seulement notre deltaMax et ses position
        deltaMax = new ArrayList<int[]>();
        deltaMax.add(new int[]{minProv.get(i)[0], prov.get(i), minProv.get(i)[1]});
      }
      else if(deltaMax.get(0)[0] == minProv.get(i)[0]) // Sinon, si c'est égal, alors on l'ajoute pour pouvoir décider par la suite
        deltaMax.add(new int[]{minProv.get(i)[0], prov.get(i), minProv.get(i)[1]});
    }

    for(int i = 0; i < minComm.size(); i++){ // Même logique mais pour les commande
      if(deltaMax.get(0)[0] < minComm.get(i)[0]){
        deltaMax = new ArrayList<int[]>();
        deltaMax.add(new int[]{minComm.get(i)[0], minComm.get(i)[1], comm.get(i)});
      }
      else if(deltaMax.get(0)[0] == minComm.get(i)[0]){
        boolean doublon = false;
        for(int[] delta : deltaMax){ // Pour vérifier que l'on ne cherche pas à maximiser la même case (doublon)
          if(delta[1] == minComm.get(i)[1] && delta[2] == comm.get(i))
            doublon = true;
        }
        if(!doublon)
          deltaMax.add(new int[]{minComm.get(i)[0], minComm.get(i)[1], comm.get(i)});
      }
    }

    if(deltaMax.size() > 1){ // Si on a plussieur deltaMax égaux, il faut choisir celui qu'on peut remplir le plus (on considére initialement que la matrice est vide)
      int indexMax = 0;
      int maxTransport = maxTransport(matTransport, deltaMax.get(0)[1], deltaMax.get(0)[2]); // On garde en mémoire la quantité de transport max
      for(int i = 1; i < deltaMax.size(); i++){
        if (maxTransport(matTransport, deltaMax.get(i)[1], deltaMax.get(i)[2]) > maxTransport){ // Si on en trouve une plus grande de même pénalité, c'est elle qu'on garde
          indexMax = i;
          maxTransport = maxTransport(matTransport, deltaMax.get(i)[1], deltaMax.get(i)[2]);
        }
      }
      return new int[] {maxTransport, deltaMax.get(indexMax)[1], deltaMax.get(indexMax)[2]};
    }
    return new int[] {maxTransport(matTransport, deltaMax.get(0)[1], deltaMax.get(0)[2]), deltaMax.get(0)[1], deltaMax.get(0)[2]};
  } 

  public static void removeInt(ArrayList<Integer> list, int x){
    for(int i = 0; i < list.size(); i++){
      if(list.get(i) == x){
        list.remove(i);
        return;
      }
    }
  }

  /**
   * Boucle de calcul principale pour calculer chaque itération de Balas Hamer
   * @param mat_transport La matrice de transport
   * @param mat_cout La matrice de coup
   */
  public static void Balas_Hammer(int[][] mat_transport, int[][] mat_cout)
  {
    ArrayList<Integer> provToCheck = new ArrayList<Integer>();
    for(int i = 0; i < mat_cout.length; i++)
      provToCheck.add(i);

    ArrayList<Integer> commToCheck = new ArrayList<Integer>();
    for(int i = 0; i < mat_cout[0].length; i++)
      commToCheck.add(i);
    
    while(provToCheck.size() != 0 && commToCheck.size() != 0){
      printMatrice(mat_cout, provToCheck, commToCheck);
      int[] toMax = deltaBalasHammer(mat_transport, mat_cout, provToCheck, commToCheck);
      System.out.println("On maximise donc la case C" + (toMax[2] + 1) + " P" + (toMax[1] + 1) + " avec un \"flot\" de " + toMax[0]);
      System.out.println();

      mat_transport[toMax[1]][toMax[2]] = toMax[0];
      // On retire les lignes complétées
      if(useLine(mat_transport, toMax[1]) == mat_transport[toMax[1]][mat_transport[0].length-1])
        removeInt(provToCheck, toMax[1]);
      if(useColumn(mat_transport, toMax[2]) == mat_transport[mat_transport.length-1][toMax[2]])
        removeInt(commToCheck, toMax[2]);
    }
  }

  /**
   * Renvoie le coût total correspondant au matrice de coût et de transport
   * @param mat_cout La matrice de cout du transport
   * @param mat_transport La matrice de transport (remplit)
   * @return Le coût total du transport
   */
  public static int cout_total(int[][] mat_cout, int[][] mat_transport)
  {
    int tt = 0;
    for(int i = 0; i < mat_cout.length; i++)
    {
      for(int j = 0; j < mat_cout[0].length; j++)
        tt += mat_cout[i][j]*mat_transport[i][j];
    }
    return(tt);
  }

// === Section 4: Détection et résolution circuit, arbre non connexe et gérération de l'arbre ===

  /**
   * Liste tout les sommet lier à la provision
   * @param matTransport la matrice de transport
   * @param sommet L'id+1 du sommet
   * @return La liste des id des sommet rattacher (sous la forme -id-1 pour faire la différence avec 0 et les provision (on renvoie une liste de commande ici))
   */
  public static ArrayList<Integer> listSuccesseurProv(int[][] matTransport, int sommet){
    ArrayList<Integer> succ = new ArrayList<Integer>();
    for(int i = 0; i < matTransport[0].length-1; i++)
    {
      if(matTransport[sommet-1][i] != 0)
        succ.add(-i -1);
    }
    return succ;
  }

  /**
   * Liste tout les sommet lier à la commande
   * @param matTransport la matrice de transport
   * @param sommet L'-id-1 du sommet
   * @return La liste des id des sommet rattacher (sous la forme id+1 pour faire la différence avec 0 et les commande (on renvoie une liste de provision ici))
   */
  public static ArrayList<Integer> listSuccesseurComm(int[][] matTransport, int sommet){
    ArrayList<Integer> succ = new ArrayList<Integer>();
    for(int i = 0; i < matTransport.length-1; i++)
    {
      if(matTransport[i][-sommet-1] != 0)
        succ.add(i + 1);
    }
    return succ;
  }

  /**
   * On prend -1 + (-idCommande) pour les sommet des commandes (pour pouvoir les différencier mathématiquement et pas passer par des chaines de caractères)
   * Et +1 +idCommande pour les Provissions (pour avoir 0 comme neutre)
   * @param matTransport La matrice de transport
   */
  public static ArrayList<int[]> parcourLargeur(int[][] matTransport, int idStartingPoint, int idParentPoint){
    ArrayList<int[]> checked = new ArrayList<int[]>();
    ArrayList<int[]> toCheck = new ArrayList<int[]>(); //idSommet, idSommetParent (c'est pour ne pas se dire qu'il y a un circuit parce qu'on retrouve le sommet parent)

    int[] temp = {idStartingPoint, idParentPoint};
    toCheck.add(temp);
    while(toCheck.size() != 0){
      int[] sommet = toCheck.remove(0);
      ArrayList<Integer> discoveredPoint;
      if (sommet[0] > 0) // Choix de la méthode adapter (pour soit parcourir le tableau par colonne ou par ligne)
        discoveredPoint = listSuccesseurProv(matTransport, sommet[0]);
      else
        discoveredPoint = listSuccesseurComm(matTransport, sommet[0]);

      // On ajoute bêtement les sommets dans notre pile
      for(int i : discoveredPoint){
        if(i != sommet[1]){
          int[] temps = {i, sommet[0]};
          toCheck.add(temps);
        }
      }

      for(int[] i : checked){
        if(sommet[0] == i[0]){ //Circuit détecté
          checked.add(sommet);
          checked.add(new int[]{0, 0}); //Marqueur de circuit (pour éviter à parcourir toute la liste pour vérifier que 'l'arbre' former par la liste ne contient pas de circuit)
          return checked;
        }
      }

      checked.add(sommet);
    }

    return checked;
  }

  public static int getNextMissingCommand(ArrayList<int[]> checked, int nbCommande){
    boolean[] linkedCommand = new boolean[nbCommande];
    for(int[] parcourSommet : checked){
      if(parcourSommet[0] < 0)
        linkedCommand[-parcourSommet[0]-1] = true;
    }
    for(int i = 0; i < nbCommande; i++){
      if(!linkedCommand[i])
        return -i-1;
    }
    return 0; // On met 1 pour il ne manque rien (comme on a mis des index strictement négatif pour les commandes)
  }

  /**
   * Crée un arbre (sous forme de parcours) en prennant en compte tout les arbres non connexe (rend automatiquement un pb non connexe connexe)
   * @param matTransport La matrice de transport remplie
   * @return Renvoie l'arbre 'non connexe' sous forme de parcours en largeur
   */
  public static ArrayList<int[]> parcourLargeurForet(int[][] matTransport, int[][] matCout){
    ArrayList<int[]> checked = parcourLargeur(matTransport, -1, 0);

    if(checked.size() < matTransport.length -1 + matTransport[0].length -1)
      System.out.println("Non connexe");
    while (checked.get(checked.size()-1)[0] != 0 && checked.size() < matTransport.length -1 + matTransport[0].length -1) {
      int indexSousArbre = getNextMissingCommand(checked, matTransport[0].length);
      System.out.println("Decouverte du/des autre(s) arbre(s), decourverte a partir de C" + (-indexSousArbre));
      checked.addAll(parcourLargeur(matTransport, indexSousArbre, checked.get(1)[0])); // On ajoute les 'sous arbres' qu'on lie arbitrairement à la première provision trouver lors du premier parcour
    }
    return checked;
  }

  /**
   * Fait un parcour en largeur pour trouver le circuit détecter au préàlable
   * @param arbreList Notre parcour en largeur contenant le circuit
   * @param matTransport La matrice de transport
   * @return Le chemin du circuit
   */
  public static ArrayList<Integer> getCircuit(ArrayList<int[]> arbreList, int[][] matTransport){
    ArrayList<ArrayList<Integer>> path = new ArrayList<ArrayList<Integer>>();
    ArrayList<Integer> initPath = new ArrayList<Integer>();
    
    int tailleList = arbreList.size()-2;
    int initSommet = arbreList.get(tailleList)[0];
    arbreList.remove(tailleList+1);
    
    initPath.add(initSommet);
    initPath.add(arbreList.get(tailleList)[1]);
    path.add(initPath);

    int nbChemin = path.size();
    boolean asChanged = true;
    while (asChanged) {
      asChanged = false;
      for(int i = 0; i < nbChemin; i++){
        int sommet = path.get(i).get(path.get(i).size()-1);
        ArrayList<Integer> discoveredPoint;
        if (sommet > 0) // Choix de la méthode adapter (pour soit parcourir le tableau par colonne ou par ligne)
          discoveredPoint = listSuccesseurProv(matTransport, sommet);
        else
          discoveredPoint = listSuccesseurComm(matTransport, sommet);

        for (int j = 0; j < discoveredPoint.size(); j++) {
          if(discoveredPoint.get(j) == path.get(i).get(path.get(i).size()-2)){
            discoveredPoint.remove(j);
            j--;
          }
        }

        switch (discoveredPoint.size()) {
          case 0:
            path.remove(i);
            i--;
            nbChemin--;
            break;
          
          case 1:
            if(discoveredPoint.get(0) == initSommet)
              return path.get(i);
            path.get(i).add(discoveredPoint.get(0));
            asChanged = true;
            break;
        
          default:
            ArrayList<Integer> temp = new ArrayList<Integer>(path.get(i));
            if(discoveredPoint.get(0) == initSommet)
              return path.get(i);
            path.get(i).add(discoveredPoint.get(0));
            for(int j = 1; j < discoveredPoint.size(); j++){
              if(discoveredPoint.get(j) == initSommet)
                return path.get(i);
              ArrayList<Integer> temp_cp = new ArrayList<Integer>(temp);
              temp_cp.add(discoveredPoint.get(j));
              path.add(temp_cp);
            }
            asChanged = true;
            break;
        }
      }
      nbChemin = path.size();
    }
    return path.get(0);
  }

  /**
   * Convertir et ordonne les index du circuit (comme il ne sont pas forcement organiser)
   * @param circuit Le chemin du circuit
   * @return Un trableau d'index ordonner pour maximiser un cycle
   */
  public static int[] getPosCircuit(ArrayList<Integer> circuit){
    int[] posCircuit = new int[circuit.size()];
    int i = 0, j = 0;
    for(int sommet : circuit){
      if(sommet > 0){
        posCircuit[i*2] = sommet -1;
        i++;
      } else {
        posCircuit[j*2+1] = -sommet-1;
        j++;
      }
    }
    return posCircuit;
  }

// === Section 5: Calcul si la proposition et optimal (potentiel + marge) et maximisation de cycle ===

  public static boolean isPossible(int[][] matTransport, int[][] matMarg, int P1, int C1, int P2, int C2){
    boolean sens = matMarg[P1][C1] + matMarg[P2][C2] < matMarg[P1][C2] + matMarg[P2][C1]; // +-+- (vrai) ou -+-+ (faux)
    int max;
    if(sens) {
      max = (matTransport[P1][C2] < matTransport[P2][C1])?matTransport[P1][C2]:matTransport[P2][C1];
    } else {
      max = (matTransport[P1][C1] < matTransport[P2][C2])?matTransport[P1][C1]:matTransport[P2][C2];
    }
    return max != 0;
  }

  /**
   * Trouve si il y a potentiellement un cycle améliorant
   * @param matMarg La matrice des coûts marginaux
   * @return La position du cycle améliorant sinon [0, 0, 0, 0]
   */
  public static int[] marchePied(int[][] matTransport, int[][] matMarg){
    int delta = 0;
    int[] pos = new int[4];

    for(int P1 = 0; P1 < matMarg.length -1; P1++){
      for(int C1 = 0; C1 < matMarg[0].length -1; C1++){
        if(matMarg[P1][C1] == 0){
          for(int P2 = P1+1; P2 < matMarg.length; P2++){
            if(P2 != P1){
              for(int C2 = C1+1; C2 < matMarg[0].length; C2++){
                if(C2 != C1 && (matMarg[P1][C1] + matMarg[P1][C2] + matMarg[P2][C1] + matMarg[P2][C2]) < delta && isPossible(matTransport, matMarg, P1, C1, P2, C2)){
                  delta = matMarg[P1][C1] + matMarg[P1][C2] + matMarg[P2][C1] + matMarg[P2][C2];
                  pos = new int[]{P1, C1, P2, C2};
                }
              }
            }
          }
        }
      }
    }
    return pos;
  }

  /**
   * Maximise un cycle en prenant en compte le sens vis à vis de la chaine améliorante
   * @param matTransport La matrice de transport
   * @param matMarg La matrice des coûts marginaux
   * @param pos La liste des positions
   */
  public static void maximisationCycle(int[][] matTransport, int[][] matMarg, int[] pos){
    boolean sens = matMarg[pos[0]][pos[1]] + matMarg[pos[2]][pos[3]] < matMarg[pos[0]][pos[3]] + matMarg[pos[2]][pos[1]]; // +-+- (vrai) ou -+-+ (faux)
    if(sens) {
      int max = (matTransport[pos[0]][pos[3]] < matTransport[pos[2]][pos[1]])?matTransport[pos[0]][pos[3]]:matTransport[pos[2]][pos[1]];
      matTransport[pos[0]][pos[1]] += max;
      matTransport[pos[2]][pos[3]] += max;
      matTransport[pos[0]][pos[3]] -= max;
      matTransport[pos[2]][pos[1]] -= max;
    } else {
      int max = (matTransport[pos[0]][pos[1]] < matTransport[pos[2]][pos[3]])?matTransport[pos[0]][pos[1]]:matTransport[pos[2]][pos[3]];
      matTransport[pos[0]][pos[1]] -= max;
      matTransport[pos[2]][pos[3]] -= max;
      matTransport[pos[0]][pos[3]] += max;
      matTransport[pos[2]][pos[1]] += max;
    }
  }

  /**
   * Maximise un cycle
   * @param matTransport La matrice de transport
   * @param pos La liste des positions du cycle
   */
  public static void maximisationCycle(int[][] matTransport, int[] pos){
    boolean sens = matTransport[pos[0]][pos[1]] + matTransport[pos[2]][pos[3]] > matTransport[pos[0]][pos[3]] + matTransport[pos[2]][pos[1]]; // +-+- (vrai) ou -+-+ (faux)
    if(sens) {
      int max = (matTransport[pos[0]][pos[3]] < matTransport[pos[2]][pos[1]])?matTransport[pos[0]][pos[3]]:matTransport[pos[2]][pos[1]];
      matTransport[pos[0]][pos[1]] += max;
      matTransport[pos[2]][pos[3]] += max;
      matTransport[pos[0]][pos[3]] -= max;
      matTransport[pos[2]][pos[1]] -= max;
    } else {
      int max = (matTransport[pos[0]][pos[1]] < matTransport[pos[2]][pos[3]])?matTransport[pos[0]][pos[1]]:matTransport[pos[2]][pos[3]];
      matTransport[pos[0]][pos[1]] -= max;
      matTransport[pos[2]][pos[3]] -= max;
      matTransport[pos[0]][pos[3]] += max;
      matTransport[pos[2]][pos[1]] += max;
    }
  }

  /**
   * Calcul pour chanque sommet son potentiel
   * @param matCout La matrice des coûts
   * @param parcoursArbre La liste du parcours en largeur de l'arbre
   * @return Un liste de 2 tableau qui représente respectivement le potentiel des provissions et le potentiel des commande
   */
  public static ArrayList<int[]> potentiel(int[][] matCout, ArrayList<int[]> parcoursArbre){
    int[] potentielProv = new int[matCout.length];
    int[] potentielComm = new int[matCout[0].length];

    for(int i = 1; i < parcoursArbre.size(); i++){ // Rappel: sommet = [idSommet, idSommetParent]
      int indexSommet = parcoursArbre.get(i)[0];
      if(parcoursArbre.get(i)[1] == 0)
        potentielComm[indexSommet] = 0;
      if(indexSommet > 0){ // Si c'est une provision
        int indexComm = -parcoursArbre.get(i)[1]-1; // On remet correctement l'index
        potentielProv[indexSommet-1] = matCout[indexSommet-1][indexComm] + potentielComm[indexComm];
      } else { // Si c'est une commande
        int indexProv = parcoursArbre.get(i)[1]-1; // On remet correctement l'index
        potentielComm[-indexSommet-1] = - matCout[indexProv][-indexSommet-1] + potentielProv[indexProv];
      }
    }

    ArrayList<int[]> res = new ArrayList<int[]>();
    res.add(potentielProv);
    res.add(potentielComm);
    return res;
  }

  /**
   * Calcul la matrice des coûts potentiel
   * @param matCout La matrice des coûts
   * @param parcoursArbre La liste du parcours en largeur <int[]> [sommet, prédecesseur]
   * @return La matrice des coûts potentiel
   */
  public static int[][] matPotentiel(int[][] matCout, ArrayList<int[]> parcoursArbre){
    int[][] matPotentiel = new int[matCout.length][matCout[0].length];
    ArrayList<int[]> potentiel = potentiel(matCout, parcoursArbre);

    // On applique simplement la formule en reprenant la liste des potentiel des sommets
    for(int i = 0; i < matPotentiel.length; i++){
      for(int j = 0; j < matPotentiel[0].length; j++)
        matPotentiel[i][j] = potentiel.get(0)[i] - potentiel.get(1)[j];
    }

    return matPotentiel;
  }

  /**
   * Calcul la matrice des coûts marginaux
   * @param matCout La matrice des coûts de transport
   * @param matPot La matrice des coûts potentiel
   * @return La matrice des coûts marginaux
   */
  public static int[][] matMarginaux(int[][] matCout, int[][] matPot){
    int[][] matMarg = new int[matCout.length][matCout[0].length];
    for(int i = 0; i < matMarg.length; i++){
      for(int j = 0; j < matMarg[0].length; j++)
        matMarg[i][j] = matCout[i][j] - matPot[i][j];
    }
    return matMarg;
  }

// === Section 6: Boucle globale ===
  /**
   * Boucle global du traitement du problème de transport
   * @param file Le chemin du fichier à traiter
   * @param scanner Le scanner déjà ouvert (pour éviter d'en ouvrir un second)
   */
  public static void processTransport(String file, Scanner scanner)
  {
    try
    {
      BufferedReader br = new BufferedReader(new FileReader(new File(file)));
      String[] stringArgs = br.readLine().split(" ");

      int[][] matrice_cout = new int[Integer.parseInt(stringArgs[0])][Integer.parseInt(stringArgs[1])];
      int[][] matrice_transport = new int[Integer.parseInt(stringArgs[0])+1][Integer.parseInt(stringArgs[1])+1];

      for (int i = 0; i < matrice_cout.length; i++)
      {
        stringArgs = br.readLine().split(" ");
        for (int j = 0; j < matrice_cout[0].length; j++)
          matrice_cout[i][j] = Integer.parseInt(stringArgs[j]);
        matrice_transport[i][matrice_transport[0].length-1] = Integer.parseInt(stringArgs[stringArgs.length-1]);
      }
      stringArgs = br.readLine().split(" ");
      for (int i = 0; i < stringArgs.length; i++)
        matrice_transport[matrice_transport.length-1][i] = Integer.parseInt(stringArgs[i]);

      br.close();

      System.out.println("La matrice de transport du fichier associe (vide pour le moment):");
      printMatrice(matrice_transport, 1); // On affiche la matrice de transport
      System.out.println("La matrice de cout associe:");
      printMatrice(matrice_cout, 2);

      int algoResponse = -1;
      while (algoResponse != 0 && algoResponse != 1) {
        System.out.print("Quel algorithme voulez vous utiliser (0=Nord-Ouest, 1=Balas-Hammer): ");
        algoResponse = scanner.nextInt();
      }

      if(algoResponse == 1)
        Balas_Hammer(matrice_transport, matrice_cout);
      else
        Nord_Ouest(matrice_transport);

      int[] posOptimisation;
      do {
        System.out.println("\nOn obtient la proposition de transport suivante:");
        printMatrice(matrice_transport, 1);
        System.out.println("Avec un cout total de: " + cout_total(matrice_cout, matrice_transport) + "\n");

        ArrayList<int[]> arbrePacour = parcourLargeurForet(matrice_transport, matrice_cout);

        while(arbrePacour.get(arbrePacour.size()-1)[1] == 0){ //respectivement circuit
          int[] posCircuit = getPosCircuit(getCircuit(arbrePacour, matrice_transport));
          System.out.println("Circuit trouve en P" + (posCircuit[0] + 1) + " C" + (posCircuit[1] + 1) + " P" + (posCircuit[2] + 1) + " C" + (posCircuit[3] + 1));

          maximisationCycle(matrice_transport, posCircuit);

          System.out.println("Nouvelle proposition de transport");
          printMatrice(matrice_transport, 1);
          arbrePacour = parcourLargeurForet(matrice_transport, matrice_cout);
        }
        
        System.out.println("La matrice des couts potentiel:");
        int[][] matPotentiel = matPotentiel(matrice_cout, arbrePacour);
        printMatrice(matPotentiel, 2);
        System.out.println("La matrice des couts maginaux:");
        int[][] matMarginaux = matMarginaux(matrice_cout, matPotentiel);
        printMatrice(matMarginaux, 2);

        posOptimisation = marchePied(matrice_transport, matMarginaux);
        if(posOptimisation[2] != 0){
          System.out.println("La proposition n'est pas optimal");
          System.out.println("Optimisation de : P" + (posOptimisation[0]+1) + " C" + (posOptimisation[1]+1) + " P" + (posOptimisation[2]+1) + " C" + (posOptimisation[3]+1));
          maximisationCycle(matrice_transport, matMarginaux, posOptimisation);
        }
      } while (posOptimisation[2] != 0); // P2 != 0 (par définition P2 appartient ]P1, nbProvision], donc ne peut pas être égal à 0
      
      System.out.println("La proposition est optimal\n");

      System.out.println("Proposition optimal:");
      printMatrice(matrice_transport, 1);
      System.out.println("Avec un cout total de: " + cout_total(matrice_cout, matrice_transport));
    }
    catch(IOException e) // Dans le cas ou une erreur de lecture apparait (obligatoire)
    {
      e.printStackTrace();
    }
  }

  /**
   * Surchage pour générer plus rapidement les traces du programme
   * @param file Le chemin du fichier à lire
   * @param algo L'index de l'algorithme à utiliser
   */
  public static void processTransport(String file, int algo) //Copier coller le code à suivre
  {
    try
    {
      BufferedReader br = new BufferedReader(new FileReader(new File(file)));
      String[] stringArgs = br.readLine().split(" ");

      int[][] matrice_cout = new int[Integer.parseInt(stringArgs[0])][Integer.parseInt(stringArgs[1])];
      int[][] matrice_transport = new int[Integer.parseInt(stringArgs[0])+1][Integer.parseInt(stringArgs[1])+1];

      for (int i = 0; i < matrice_cout.length; i++)
      {
        stringArgs = br.readLine().split(" ");
        for (int j = 0; j < matrice_cout[0].length; j++)
          matrice_cout[i][j] = Integer.parseInt(stringArgs[j]);
        matrice_transport[i][matrice_transport[0].length-1] = Integer.parseInt(stringArgs[stringArgs.length-1]);
      }
      stringArgs = br.readLine().split(" ");
      for (int i = 0; i < stringArgs.length; i++)
        matrice_transport[matrice_transport.length-1][i] = Integer.parseInt(stringArgs[i]);

      br.close();

      System.out.println("La matrice de transport du fichier associe (vide pour le moment):");
      printMatrice(matrice_transport, 1); // On affiche la matrice de transport
      System.out.println("La matrice de cout associe:");
      printMatrice(matrice_cout, 2);

      if(algo == 1) {
        System.out.println("Utilisation de l'algorithme Balas Hammer");
        Balas_Hammer(matrice_transport, matrice_cout);
      } else {
        System.out.println("Utilisation de l'algorithme Nord Ouest");
        Nord_Ouest(matrice_transport);
      }

      int[] posOptimisation;
      do {
        System.out.println("\nOn obtient la proposition de transport suivante:");
        printMatrice(matrice_transport, 1);
        System.out.println("Avec un cout total de: " + cout_total(matrice_cout, matrice_transport) + "\n");

        ArrayList<int[]> arbrePacour = parcourLargeurForet(matrice_transport, matrice_cout);

        while(arbrePacour.get(arbrePacour.size()-1)[1] == 0){ //respectivement circuit
          int[] posCircuit = getPosCircuit(getCircuit(arbrePacour, matrice_transport));
          System.out.println("Circuit trouve en P" + (posCircuit[0] + 1) + " C" + (posCircuit[1] + 1) + " P" + (posCircuit[2] + 1) + " C" + (posCircuit[3] + 1));

          maximisationCycle(matrice_transport, posCircuit);

          System.out.println("Nouvelle proposition de transport");
          printMatrice(matrice_transport, 1);
          arbrePacour = parcourLargeurForet(matrice_transport, matrice_cout);
        }
        
        System.out.println("La matrice des couts potentiel:");
        int[][] matPotentiel = matPotentiel(matrice_cout, arbrePacour);
        printMatrice(matPotentiel, 2);
        System.out.println("La matrice des couts maginaux:");
        int[][] matMarginaux = matMarginaux(matrice_cout, matPotentiel);
        printMatrice(matMarginaux, 2);

        posOptimisation = marchePied(matrice_transport, matMarginaux);
        if(posOptimisation[2] != 0){
          System.out.println("La proposition n'est pas optimal");
          System.out.println("Optimisation de : P" + (posOptimisation[0]+1) + " C" + (posOptimisation[1]+1) + " P" + (posOptimisation[2]+1) + " C" + (posOptimisation[3]+1));
          maximisationCycle(matrice_transport, matMarginaux, posOptimisation);
        }
      } while (posOptimisation[2] != 0); // P2 != 0 (par définition P2 appartient ]P1, nbProvision]
      
      System.out.println("La proposition est optimal\n");

      System.out.println("Proposition optimal:");
      printMatrice(matrice_transport, 1);
      System.out.print("Avec un cout total de: " + cout_total(matrice_cout, matrice_transport));
    }
    catch(IOException e) // Dans le cas ou une erreur de lecture apparait (obligatoire)
    {
      e.printStackTrace();
    }
  }

  public static void main(String[] args)
  {
    if(args.length == 0){
      boolean again = true;
      Scanner scanner = new Scanner(System.in);
      while(again)
      {
        System.out.println("Entrer le nom/chemin du fichier à analyser: ");
        processTransport(scanner.nextLine(), scanner);

        System.out.println("Voulez vous analyser un autre fichier? (y/n)");
        if(!scanner.next().equals("y"))
          again = false;
        scanner.nextLine(); //Sinon, il prend comme fichier à traiter y
      }
      scanner.close();
      System.exit(0);
    } else {
      processTransport(args[0], Integer.parseInt(args[1]));
    }
  }
}