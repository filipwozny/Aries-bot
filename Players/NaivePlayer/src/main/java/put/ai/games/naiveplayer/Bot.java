/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.naiveplayer;

import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Bot extends Player {

    public Move result = null;
    public int boardSize;
    public int number_of_move;
    public ArrayList<Integer> sourceX = new ArrayList<>();
    public ArrayList<Integer> sourceY = new ArrayList<>();
    public ArrayList<Integer> destinationX = new ArrayList<>();
    public ArrayList<Integer> destinationY = new ArrayList<>();
    public ArrayList<Double> score = new ArrayList<>();
    public Color ourPlayer;
    public Random random = new Random();

    public void convertToCoordinates(List<Move> moves){

        for (Move m : moves){
            String [] temp = m.toString().split(",|>|\\(|-|\\)");
            int SrcX , SrcY , DstX , DstY;
            SrcX = Integer.parseInt(temp[1]);
            SrcY = Integer.parseInt(temp[2]);
            DstX = Integer.parseInt(temp[6]);
            DstY = Integer.parseInt(temp[7]);
            sourceX.add(SrcX);
            sourceY.add(SrcY);
            destinationX.add(DstX);
            destinationY.add(DstY);
        }

    }


    public boolean chekTime(long limit , long currentTime){
        return currentTime + 500 < limit;
    }

 public int getLowerDistance(double pointx , double pointxy){

        ArrayList<Double> distances = new ArrayList<>();
        for (int i = 0 ; i < sourceX.size(); i++){
            if ((sourceX.get(i) == 0 && sourceY.get(i) ==0 && ourPlayer == Color.PLAYER1) || (sourceX.get(i) == boardSize -1 && sourceY.get(i) == boardSize - 1 && ourPlayer == Color.PLAYER2)  ){
                distances.add(100.0);
            }
            else {
                distances.add(Math.sqrt(Math.pow(destinationX.get(i) - pointx, 2) + Math.pow(destinationY.get(i) - pointxy, 2)));
            }
            }

        int index = 0;
        double minValue = 100;

        for (int i = 0; i < distances.size(); i ++){
            if (distances.get(i) <= minValue){
                minValue = distances.get(i);
                index=i;
            }
        }
        return index;
    }

    public boolean checkAtack(int XSrc , int YSrc , int XDsc , int YDsc , Board b) { //sprawdz atak w pionie
        if (YSrc != YDsc && b.getState(XDsc,YDsc) != Color.EMPTY && b.getState(XDsc,YDsc) != ourPlayer) {

            int i = 0;
            int check = b.getSize() + 1;
            if (YSrc - YDsc > 0) {
                for (i = YDsc; i >= 0; i--) {
                    if (b.getState(XSrc, i) == Color.EMPTY) {
                        return false;
                    }
                    if (b.getState(XSrc, i) != Color.EMPTY && b.getState(XSrc, i) == ourPlayer) {
                        return true;
                    }
                    check = i;
                }
                return check == 0;

            }
            if (YSrc - YDsc < 0) {
                for (i = YDsc; i < b.getSize(); i++) {
                    if (b.getState(XSrc, i) == Color.EMPTY) {
                        return false;
                    }
                    if (b.getState(XSrc, i) != Color.EMPTY && b.getState(XSrc, i) == ourPlayer) {
                        return true;
                    }
                    check = i;
                }
                return check == b.getSize() - 1;

            } else {
                return false;
            }
        }
        else return false;
    }


    public boolean checkAtack2(int XSrc , int YSrc , int XDsc , int YDsc , Board b){  //sprawdz atak w poziomie
        if (XSrc != XDsc && b.getState(XDsc,YDsc) != Color.EMPTY && b.getState(XDsc,YDsc) != ourPlayer) {

            int i = 0;
            int check = b.getSize() + 1;
            if (XSrc - XDsc > 0) {
                for (i = XDsc; i >= 0; i--) {
                    if (b.getState(i, YSrc) == Color.EMPTY) {
                        return false;
                    }
                    if (b.getState(i, YSrc) != Color.EMPTY && b.getState(i, YSrc) == ourPlayer) {
                        return true;
                    }
                    check = i;
                }
                return check == 0;
            }

            if (XSrc - XDsc < 0) {
                for (i = XDsc; i < b.getSize(); i++) {
                    if (b.getState(i,YSrc) == Color.EMPTY) {
                        return false;
                    }
                    if (b.getState(i,YSrc) != Color.EMPTY && b.getState(i,YSrc) == ourPlayer) {
                        return true;
                    }
                    check = i;
                }
                return check == b.getSize() - 1;

            } else {
                return false;
            }
        }
        else return false;
    }

    public Move checkMove(Board b){
        double chek = 0;
        int index = 0;

        for (int i =0 ; i < destinationX.size(); i++){
            boolean check1 = checkAtack(sourceX.get(i),sourceY.get(i),destinationX.get(i) , destinationY.get(i), b);
            boolean check2 = checkAtack2(sourceX.get(i),sourceY.get(i),destinationX.get(i) , destinationY.get(i), b);
             if ( (  check1|| check2 ) && ((ourPlayer == Color.PLAYER1  && destinationX.get(i) < boardSize/2 && destinationY.get(i) < boardSize/2) ||
                    ourPlayer == Color.PLAYER2  && destinationX.get(i) > boardSize/2 && destinationY.get(i) > boardSize/2)){
                if (ourPlayer == Color.PLAYER1){
                    score.add(10.0 + sourceX.get(i)/10 + sourceY.get(i)/10);
                }
                else {
                    score.add(2.0 - sourceX.get(i)/10 - sourceY.get(i)/10);
                }

            }
            else if ((checkAtack(sourceX.get(i),sourceY.get(i),destinationX.get(i) , destinationY.get(i), b) || checkAtack2(sourceX.get(i),sourceY.get(i),destinationX.get(i) , destinationY.get(i), b))){
                if (ourPlayer == Color.PLAYER1){
                    score.add(5.0 + sourceX.get(i)/10 + sourceY.get(i)/10);
                }
                else {
                    score.add(0.0 - sourceX.get(i)/10 - sourceY.get(i)/10);
                }

            }
            else {
                score.add(0.0);
            }
        }

        for(int i = 0 ; i < score.size() ; i++){
            if (score.get(i) >= chek ){
                chek = score.get(i);
                index = i;
            }
        }

        if (chek == 0){
            return b.getMovesFor(getColor()).get(random.nextInt(score.size()));
        }

        number_of_move++;
        return b.getMovesFor(getColor()).get(index);
    }




    @Override
    public Move nextMove(Board b) throws InterruptedException {
        result = null;
        long limit = getTime();
        long startTime = System.currentTimeMillis();
        boolean timelimit = chekTime(limit , System.currentTimeMillis() - startTime);

        ourPlayer = getColor();
        boardSize = b.getSize();


        List<Move> moves = b.getMovesFor(getColor());
        convertToCoordinates(moves);
        result = checkMove(b);
        if  (!timelimit) {
            result = moves.get(random.nextInt(moves.size()));
        }


        //moves.clear();
        sourceX.clear();
        sourceY.clear();
        destinationX.clear();
        destinationY.clear();
        score.clear();
        return result;

    }


    public static void main(String[] args){}


    @Override
    public String getName() {
        return "Filip Woźny 141674 Adrian Karolewski 145327";
    }
}
