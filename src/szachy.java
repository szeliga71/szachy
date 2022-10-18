import java.util.Scanner;
import java.util.regex.Pattern;

public class szachy {

    public static void main(String[] args) {

       int[] deck = {0b00100011010001100101010000110010, 0b00010001000100010001000100010001,
                0b00000000000000000000000000000000, 0b00000000000000000000000000000000, 0b00000000000000000000000000000000,
                0b00000000000000000000000000000000, 0b10011001100110011001100110011001, 0b10101011110011101101110010111010};

        int tura = 0;
        boolean gra = true;

        while (gra) {

            showDeck(deck);
            System.out.println();
            System.out.println();
            int xs ;
            int ys ;
            int xc ;
            int yc ;
            int kolor = (tura + 1) % 2;
            boolean prawid = true;


            if(sprawdzSzacha(deck,xKrol(deck,kolor),yKrol(deck,kolor),kolor)){
                System.out.println(" Uwaga szach !!!");

                if(czyMat(deck,kolor)){
                    prawid=false;
                    gra=false;
                    System.out.println("  ... i mat  "  +'\n'+ "    koniec gry ! ! !");
                }
            }

            while (prawid) {

                if (kolor == 1) {
                    System.out.println("Ruch bialych ");
                } else if (kolor == 0) {
                    System.out.println("Ruch czarnych ");
                }

                int[] temp = wprowadzWspolrzedne();

                xs = startX(temp);
                ys = startY(temp);
                ys = ustawY(ys);
                xc = celX(temp);
                yc = celY(temp);
                yc = ustawY(yc);

                prawid = prawidlowo(xs, ys, xc, yc);

                if (!prawid) {
                    if ((((deck[ys] >> xs * 4) & 0b1111) > 0) && ((((deck[ys] >> xs * 4) & 0b1111) >> 3) == kolor)) {

                        if ((((deck[yc] >> xc * 4) & 0b1111) == 0) || ((((deck[yc] >> xc * 4) & 0b1111) > 0) && (((deck[yc] >> (xc) * 4) & 0b1111) >> 3 != kolor))) {

                            if (jakaFigura(deck, xs, ys, xc, yc, kolor)) {

                                 int figuraStart=zapiszRuch(deck,xs,ys,xc,yc);
                                if (sprawdzSzacha(deck, xKrol(deck, kolor), yKrol(deck, kolor), kolor)) {

                                    prawid = true;

                                    zapiszRuchOdwrotnie(deck,xc,yc,xs,ys,figuraStart);

                                } else {
                                    prawid = false;
                                    tura++;
                                }

                            } else {
                                prawid = true;
                            }
                            } else prawid = true;
                        }
                    }
                }
            }
    }
    public static void showDeck(int[] deck) {

        int field = 0;

        for (int i = 0; i < deck.length; i++) {
            System.out.print(8 - i + "  ");
            for (int j = 0; j < 8; j++) {

                if (((deck[i] >> j * 4) & 0b1111) != 0) {
                    if (((deck[i] >> j * 4) & 0b1111) == 1)
                        field = 2;
                    else if (((deck[i] >> j * 4) & 0b1111) == 2)
                        field = 3;
                    else if (((deck[i] >> j * 4) & 0b1111) == 3)
                        field = 4;
                    else if (((deck[i] >> j * 4) & 0b1111) == 4)
                        field = 5;
                    else if (((deck[i] >> j * 4) & 0b1111) == 5)
                        field = 6;
                    else if (((deck[i] >> j * 4) & 0b1111) == 6)
                        field = 7;
                    else if (((deck[i] >> j * 4) & 0b1111) == 9)
                        field = 8;
                    else if (((deck[i] >> j * 4) & 0b1111) == 10)
                        field = 9;
                    else if (((deck[i] >> j * 4) & 0b1111) == 11)
                        field = 10;
                    else if (((deck[i] >> j * 4) & 0b1111) == 12)
                        field = 11;
                    else if (((deck[i] >> j * 4) & 0b1111) == 13)
                        field = 12;
                    else if (((deck[i] >> j * 4) & 0b1111) == 14)
                        field = 13;
                } else field = (i + j) % 2;

                switch (field) {
                    case 0 -> System.out.print("\u2b1b" + " ");
                    case 1 -> System.out.print('\u2b1c' + " ");
                    case 2 -> System.out.print('\u2659' + " ");
                    case 3 -> System.out.print('\u2656' + " ");
                    case 4 -> System.out.print('\u2658' + " ");
                    case 5 -> System.out.print('\u2657' + " ");
                    case 6 -> System.out.print('\u2655' + " ");
                    case 7 -> System.out.print('\u2654' + " ");
                    case 8 -> System.out.print('\u265F' + " ");
                    case 9 -> System.out.print('\u265C' + " ");
                    case 10 -> System.out.print('\u265E' + " ");
                    case 11 -> System.out.print('\u265D' + " ");
                    case 12 -> System.out.print('\u265B' + " ");
                    case 13 -> System.out.print('\u265A' + " ");
                }
            }
            System.out.println();
            if (i == deck.length - 1) {
                System.out.println();
                System.out.print("   ");
                for (int k = 0; k < deck.length; k++) {
                    System.out.print((char) (65 + k) + "  ");
                }
            }
        }
    }

    public static int[] wprowadzWspolrzedne() {

        System.out.println();
        System.out.println();
        Scanner scan = new Scanner(System.in);
        boolean prawidlowe = true;
        int[] temp = new int[4];

        while (prawidlowe) {
            System.out.println("""
                    Podaj wpolrzedne startu i celu dla figury ,ktora chcesz wykonac ruch.
                    Dane prosze wprowadic w formacie "OSX spacja OSY spacja OSX spacja OSY"  np A 2 A 3 .
                    ( wielkosc liter nie ma znaczenia )""");

            String textWprowadzony = scan.nextLine();

            String pattern = "\\w\\s\\d\\s\\w\\s\\d";
            boolean czyPasuje = Pattern.matches(pattern, textWprowadzony);
            if (czyPasuje) {
                for (int i = textWprowadzony.length() - 1; i >= 0; i--) {
                    temp[(i / 2)] = textWprowadzony.charAt(i);
                    prawidlowe = false;
                }
            } else
                System.out.println("wprowadzona zla sekwencje znakow  ");
        }
        return temp;}

        public static int startX(int[]temp){
    int xs ;
    if (temp[0] >= 65 && temp[0] <= 72) {
        xs = temp[0] - 64;
    } else if (temp[0] >= 97 && temp[0] <= 104) {
        xs = temp[0] - 97;
    } else {
        System.out.println(" wprowadzono niepoprawna wspolrzedna startowa osi x");
           xs =1000;
    }
    return xs;}

    public static int startY(int[]temp) {
        int ys ;
        if (temp[1] >= 49 && temp[1] <= 56){
            ys = temp[1] - 49;
        }
        else
    {System.out.println(" wprowadzono niepoprawna wspolrzedna startowa osi y");
    ys =1000;
    }

    return ys;}

    public static int celX(int[]temp) {
        int xc ;
        if (temp[2] >= 65 && temp[2] <= 72) {
            xc = temp[2] - 65;
        } else if (temp[2] >= 97 && temp[2] <= 104) {
            xc = temp[2] - 97;
        } else {
            System.out.println(" wprowadzono niepoprawna wspolrzedna celu osi x ");
            xc=1000;
        }
        return xc;}

    public static int celY(int[]temp) {
        int yc ;
        if (temp[3] >= 49 && temp[3] <= 56)
            yc = temp[3] - 49;
        else{ System.out.println(" wprowadzono niepoprawna wspolrzedna startowa osi y");
        yc=1000;
        }
    return yc;
    }

    public static boolean prawidlowo(int x , int y, int x1, int y1){
        boolean ok= true;
        if(x>=0&&x<=7&&y>=0&&y<=7&&x1>=0&&x1<=7&&y1>=0&&y1<=7)
            ok =false;
        return ok;
    }

    public static boolean jakaFigura(int[]temp,int xs,int ys,int xc,int yc,int kolor ){

        boolean moze=false;

        switch (((temp[ys] >> ((xs) * 4)) & 0b111)) {
            case 1 ->
                moze = pion(temp, xs, ys, xc, yc, kolor);
            case 2 ->

            moze = wieza(temp, xs, ys, xc, yc);

            case 3 ->
                moze = kon(xs, ys, xc, yc);
            case 4 ->
                moze = goniec(temp, xs, ys, xc, yc);
            case 5 ->
                moze = hetman(temp, xs, ys, xc, yc);
            case 6 ->
                moze = krol(xs, ys, xc, yc);
        }

        return moze;
    }

    public static boolean pion(int[]deck,int xs,int ys,int xc,int yc,int kolor){
       boolean ruch=false;

        switch (kolor){

            case 0:
                    if (ys == 1) {

                        if ((xs == xc) && ((ys + 2 == yc) || (ys + 1 == yc)) && (((deck[yc] >> xc * 4) & 0b1111) == 0)) {
                            ruch = true;
                        } else if((ys+1==yc)&&((xs-1==xc||xs+1==xc))&&((((deck[yc]>>xc*4)&0b1111)>0)&&((((deck[yc]>>xc*4)&0b1111)>>3)!=kolor))){

                            ruch=true;}
                    }
                       else if(ys>1){

                         if((ys+1==yc)&&((xs-1==xc||xs+1==xc))&&((((deck[yc]>>xc*4)&0b1111)>0)&&((((deck[yc]>>xc*4)&0b1111)>>3)!=kolor))){

                            ruch=true;}
                            else if((xs==xc) &&(ys + 1 == yc)&&(((deck[yc]>>xc*4)&0b1111)==0)){

                               ruch=true;
                           }
                       }
                       break;
            case 1:
        if(ys==6) {
            if ((xs == xc) && ((ys - 2 == yc) || (ys - 1 == yc)) && (((deck[yc] >> xc * 4) & 0b1111) == 0)) {
                ruch = true;
            }
            else if((ys-1==yc)&&((xs-1==xc||xs+1==xc))&&((((deck[yc]>>xc*4)&0b1111)>0)&&((((deck[yc]>>xc*4)&0b1111)>>3)!=kolor))){

                ruch=true;}
        }else if(ys<6){

             if((ys-1==yc)&&((xs-1==xc||xs+1==xc))&&((((deck[yc]>>xc*4)&0b1111)>0)&&((((deck[yc]>>xc*4)&0b1111)>>3)!=kolor))){

                ruch=true;}

                else if((xs==xc) &&(ys - 1 == yc)&&(((deck[yc]>>xc*4)&0b1111)==0)){

                    ruch=true;
                }
            }
        break;
       }
       return ruch;}

    public static boolean wieza(int[]deck,int xs,int ys,int xc,int yc) {

        boolean ruch = false;

        if ((xc == xs)&&(ys!=yc) ) {
            ruch=true;
            if (yc < ys) {
                for (int i=1;i<ys-yc;i++){
                    if((((deck[ys-i]>>xs*4)&0b1111)>0)&&((ys-i)!=yc)){
                        ruch= false;
                        break;
                    }
                }
            } else {
                 for (int i = 1 ; i <yc-ys; i++) {
                    if ((((deck[ys+i] >>(xc)*4)&0b1111) > 0) &&((ys+i)!=yc)){
                        ruch= false;
                        break;
                    }
                 }
            }
        } else if ((ys == yc)&&(xs!=xc)) {
            ruch=true;

            if (xc > xs) {

                    for (int i = 1; i < xc - xs; i++) {
                        if ((((deck[ys] >> (xs + i) * 4) & 0b1111) > 0) && ((xs + i) != xc)) {
                            ruch = false;
                            break;
                        }
                    }
                } else {

                System.out.println(xs-xc);
                    for (int i = 1; i < xs - xc; i++) {
                        if ((((deck[ys] >> (xs - i) * 4) & 0b1111) > 0) && (xs - i!= xc)) {
                            ruch = false;
                            break;
                        }
                    }
                }
            }
        return ruch;
        }

        public static boolean kon(int xs,int ys,int xc,int yc){
        boolean ruch = false;

        if((ys+2==yc) && (xs -1)==xc){
            ruch=true;
        }
             else if((ys-2==yc) && (xs-1)==xc){
            ruch= true;
        }
                 else if((ys+2==yc) && (xs+1)==xc){
            ruch= true;
        }
                     else  if((ys-2==yc) && (xs+1)==xc){
            ruch= true;
        }
                         else if((ys+1==yc) && (xs-2)==xc){
            ruch= true;
        }
        else if((ys-1==yc) && (xs-2)==xc){
            ruch= true;
        }
        else if((ys+1==yc) && (xs+2)==xc){
            ruch= true;
        }
        else if((ys-1==yc) && (xs+2)==xc){
            ruch= true;
        }
        return ruch;
    }

  public static boolean goniec(int[]deck,int xs,int ys,int xc,int yc) {
        boolean ruch = false;

        boolean x = false;
        boolean y = false;

        if (xs < xc && ys > yc) {

            for (int i = 0; i <= xc - xs; i++) {
                x = xs + i == xc && yc + i == ys;
            }
            for (int i = 0; i <= ys - yc; i++) {
                y = xs + i == xc && yc + i == ys;
            }
            if(x&&y){
                ruch =true;
                for (int i=1;i<=xc-xs;i++){
                    if((((deck[ys-i]>>(xs+i)*4)&0b1111)>0)&&((ys-i)!=yc&&xs+i!=xc)){
                        ruch= false;
                        break;
                    }
                }
            }

        }
            else if(xs>xc&&ys<yc){

            for (int i = 0; i <= xs - xc; i++) {
                x = xs - i == xc && ys + i == yc;
            }
            for (int i = 0; i <= yc - ys; i++) {
                y = xs - i == xc && ys + i == yc;
            }
                if(x&&y){
                    ruch =true;
                    for (int i=1;i<=xs-xc;i++) {
                        if ((((deck[ys + i] >> (xs - i) * 4) & 0b1111) > 0)&&((ys+i)!=yc&&xs-i!=xc)) {
                            ruch= false;
                            break;
                        }
                    }
                }
            }

        else if(xs>xc&&ys>yc){
            for (int i = 0; i <= xs - xc; i++) {
                x = xs - i == xc && yc + i == ys;
            }
            for (int i = 0; i <= ys - yc; i++) {
                y = xs - i == xc && yc + i == ys;
            }
                if(x&&y){
                    ruch =true;
                    for (int i=1;i<=xs-xc;i++) {
                        if ((((deck[ys - i] >> (xs - i) * 4) & 0b1111) > 0)&&((ys-i)!=yc&&xs-i!=xc)) {
                          ruch= false;
                          break;
                        }
                    }
            }
        }
        else if(xs<xc&&ys<yc){

            for (int i = 0; i <= xc - xs; i++) {
                x = xs + i == xc && ys + i == yc;
            }
            for (int i = 0; i <= yc - ys; i++) {
                y = xs + i == xc && ys + i == yc;
            }
                if(x&&y){
                    ruch =true;
                    for (int i=1;i<=xc-xs;i++){
                        if((((deck[ys+i]>>(xs+i)*4)&0b1111)>0)&&((ys+i)!=yc&&xs+i!=xc)){
                            ruch= false;
                            break;
                        }
                }

            }
        }
        return ruch;  }

        public static boolean hetman(int[]deck,int xs,int ys,int xc,int yc){
        boolean ruch=false;
        if(wieza(deck,xs,ys,xc,yc)||goniec(deck,xs,ys,xc,yc)){
            ruch=true;
        }
        return ruch;}

    public static boolean krol(int xs,int ys,int xc,int yc){
        boolean ruch=false;
        if(xs+1==xc&&ys+1==yc)
            ruch=true;
        else if(xs-1==xc&&ys+1==yc)
            ruch=true;
        else if(xs-1==xc&&ys-1==yc)
            ruch=true;
        else if(xs+1==xc&&ys==yc)
            ruch=true;
        else if(xs-1==xc&&ys==yc)
            ruch=true;
        else if(xs+1==xc&&ys-1==yc)
            ruch=true;
        else if(xs==xc&&ys+1==yc)
            ruch=true;
        else if(xs==xc&&ys-1==yc)
            ruch=true;
    return ruch;}

    public static int zapiszRuch(int[]deck,int xs,int ys,int xc,int yc) {

        int figuraStart = (deck[yc] >> (xc) * 4) & 0b1111;

        int figura = (deck[ys] >> (xs) * 4) & 0b1111;

        deck[ys] = deck[ys] & (~(0b1111 << xs * 4));
        deck[yc] = deck[yc] & (~(0b1111 << xc * 4));
        deck[yc] = deck[yc] | (figura << xc * 4);
    return figuraStart;}

    public static void zapiszRuchOdwrotnie(int[]deck,int xs,int ys,int xc,int yc,int figuraStart) {

        int figura = (deck[ys] >> (xs) * 4) & 0b1111;

        deck[ys] = deck[ys] & (~(0b1111 << xs * 4));
        deck[ys] = deck[ys] | (figuraStart << xs *4);
        deck[yc] = deck[yc] & (~(0b1111 << xc * 4));
        deck[yc] = deck[yc] | (figura << xc * 4);
    }

    public static int ustawY(int x){
        int z=0;
        for (int i=0;i<=x;i++)
            for(int j=0;j<8;j++){
                if(i==x){
                    z=j-i;
                }
                        }
return z;}

public static int xKrol(int[]deck,int kolor){
int x =0;
for (int i =0;i<deck.length;i++){
    for(int j=0;j<8;j++){
        if(((((deck[i]>>(j*4))&0b1111)==6) || (((deck[i]>>(j*4))&0b1111)==14) )  &&((((deck[i]>>(j*4))&0b1111)>>3)==kolor)){
            x=j;
        }
    }
}
return x;}

public static int yKrol(int[]deck,int kolor){
int y =0;
for (int i =0;i<deck.length;i++){
        for(int j=0;j<8;j++){
            if (((((deck[i] >> (j * 4)) & 0b111) == 6) || (((deck[i] >> (j * 4)) & 0b1111) == 14)) && ((((deck[i] >> (j * 4)) & 0b1111) >> 3) == kolor)) {
                y = i;
            }
        }
}
return y;}

public static boolean sprawdzSzacha(int[]deck,int xc,int yc,int kolor){
boolean szach=false;

for(int i=0;i<deck.length;i++){
    for(int j=0;j<8;j++){
        if((((deck[i]>>(j*4))&0b1111)>0)&&((((deck[i]>>(j*4))&0b1111)>>3)!=kolor)){
            szach=jakaFigura(deck,j,i,xc,yc,kolor);
            if(szach){
                    break;}
        }
    }
}

return szach;}

public static boolean czyMat(int[]deck,int kolor){
        boolean mat= false;
        int x=0;
    for(int i=0;i<deck.length;i++){
        for(int j=0;j<8;j++){
            if(((deck[i]>>(j*4)&0b1111)>0)&&(((deck[i]>>(j*4)&0b1111)>>3)==kolor)){
                for(int k=0;k<deck.length;k++){
                    for(int l=0;l<8;l++) {
                        if(((((deck[k]>>(l*4))&0b1111)>0)&&((((deck[k]>>(l*4))&0b1111)>>3)!=kolor))||(((deck[k]>>(l*4))&0b1111)==0)){
                            if(jakaFigura(deck,j,i,l,k,kolor)){
                                int figuraStart=zapiszRuch(deck,j,i,l,k);
                                if(sprawdzSzacha(deck,xKrol(deck,kolor),yKrol(deck,kolor),kolor)){
                                    zapiszRuchOdwrotnie(deck,l,k,j,i,figuraStart);
                                    mat=true;
                            }else{
                                zapiszRuchOdwrotnie(deck,l,k,j,i,figuraStart);
                                x++;
                                }
                        }
                    }
                }
            }
                }
    }
        if(x>0)
            mat=false; }

    return mat;
    }

}




