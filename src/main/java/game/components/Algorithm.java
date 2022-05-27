package game.components;

import game.Pair;
import game.Type;

import java.util.*;

import static game.Type.*;
import static game.components.GlobalVars.*;

public class Algorithm {

    int MAX_VALUE = 255;
    int MIN_VALUE = 0;

    public AiMove wolfMoveComponent = new AiMove();

    final Pair<Integer, Integer>[] moves = new Pair[]{
            new Pair<>(+1, +1),
            new Pair<>(+1, -1),
            new Pair<>(-1, -1),
            new Pair<>(-1, +1)
    };
    Type[] wolfs = new Type[]{WOLF1, WOLF2, WOLF3, WOLF4};

    public int minMax(int entity, int recursiveLvl, int alpha, int beta) {

        if (recursiveLvl == 0) prepareField();
        int test;

        if (recursiveLvl >= 2 * DIFFICULTY) {
            int evaluation = getEvaluation();
            prepareField();
            return evaluation;
        }
        int bestMove = 255;
        boolean isWolf = (entity == 2);
        int MinMax = isWolf ? MIN_VALUE : MAX_VALUE;

        for (int i = (isWolf ? 0 : 8); i < (isWolf ? 8 : 12); i++) {
            int curEntity = isWolf ? ((i / 2) + 1) : 0;

            Pair<Integer, Integer> curEntityPosition = (curEntity == 0) ? SHEEP.getCoordinate() : wolfs[curEntity - 1].getCoordinate();
            Pair<Integer, Integer> curMove = moves[isWolf ? i % 2 : i % 4];

            if (canMove(curEntityPosition.getFirst() + curMove.getFirst(), curEntityPosition.getSecond() + curMove.getSecond())) {

                entityMove(curEntity, curMove.getFirst(), curMove.getSecond());
                test = minMax(isWolf ? 1 : 2, recursiveLvl + 1, alpha, beta);
                entityMove(curEntity, -curMove.getFirst(), -curMove.getSecond());

                if ((test > MinMax && entity == 2) || (test <= MinMax && entity == 1) || (bestMove == 255)) {
                    MinMax = test;
                    bestMove = i;
                }
                if (isWolf)
                    alpha = Math.max(alpha, test);
                else
                    beta = Math.min(beta, test);

                if (beta < alpha) break;
            }
        }

        if (bestMove == 255) {
            int evaluation = getEvaluation();
            prepareField();
            return evaluation;
        }

        if (recursiveLvl == 0) {
            if (entity == 2) {
                wolfs[bestMove / 2].setCoordinate(wolfs[bestMove / 2].getCoordinate().addTo(moves[bestMove % 2]));
                prepareField();
                wolfMoveComponent.moveEntity(wolfs[bestMove / 2]);

            } else {
                SHEEP.setCoordinate(SHEEP.getCoordinate().addTo(moves[bestMove % 4]));
                prepareField();
                wolfMoveComponent.moveEntity(SHEEP);
            }
        }
        return MinMax;
    }

    public void entityMove(int curEntity, int y, int x) {
        if (curEntity == 0) {
            array[SHEEP.getY()][SHEEP.getX()] = 0;
            array[SHEEP.getY() + y][SHEEP.getX() + x] = 1;
            SHEEP.setCoordinate(new Pair<>(SHEEP.getY() + y, SHEEP.getX() + x));
        } else {
            array[wolfs[curEntity - 1].getY()][wolfs[curEntity - 1].getX()] = 0;
            array[wolfs[curEntity - 1].getY() + y][wolfs[curEntity - 1].getX() + x] = 255;
            wolfs[curEntity - 1].setCoordinate(new Pair<>(wolfs[curEntity - 1].getY() + y, wolfs[curEntity - 1].getX() + x));
        }
    }

    public void prepareField() {

        array = new int[8][8];

        array[SHEEP.getY()][SHEEP.getX()] = 1;

        for (Type wolf : wolfs) array[wolf.getY()][wolf.getX()] = 255;
    }


        public int getEvaluation() {
            ArrayDeque<Pair<Integer, Integer>> deque = new ArrayDeque<>();
            if (SHEEP.getY() == 0) return 0;
            int minCycle = 200;
            boolean minActive = false;
            deque.push(SHEEP.getCoordinate());

            while (!deque.isEmpty()) {
                Pair<Integer, Integer> currentPosition = deque.pop();
                for (int i = 0; i < 4; i++) {
                    if (canMove(currentPosition.getFirst() + moves[i].getFirst(), currentPosition.getSecond() + moves[i].getSecond())) {
                        Pair<Integer, Integer> newPosition = currentPosition.addTo(moves[i]);
                        array[newPosition.getFirst()][newPosition.getSecond()] = array[currentPosition.getFirst()][currentPosition.getSecond()] + 1;
                        if (newPosition.getFirst() == 0) {
                            minCycle = Math.min(minCycle, array[0][newPosition.getSecond()]);
                            minActive = true;
                        }
                        if (minActive) {
                            if (array[newPosition.getFirst()][newPosition.getSecond()] < minCycle)
                                deque.push(newPosition);
                        } else
                            deque.push(newPosition);
                    }
                }
            }

            int min = MAX_VALUE;

            for (int i = 0; i < 4; i++) {
                if (array[0][i * 2 + 1] > MIN_VALUE && array[0][i * 2 + 1] < min)
                    min = array[0][i * 2 + 1];
            }
            return min - 1;
        }

    public boolean canMove(int y, int x) {
        if (!(x >= 0 && y >= 0 && x <= 7 && y <= 7)) return false;
        return array[y][x] == 0;
    }

    public boolean isGameOver() {
        if (SHEEP.getX() == 0 && SHEEP.getY() == 7 && array[6][1] != 0)
            return true;
        if (SHEEP.getX() == 0 && SHEEP.getY() < 7)
            if ((array[SHEEP.getY() + 1][SHEEP.getX() + 1] != 0 && array[SHEEP.getY() - 1][SHEEP.getX() + 1] != 0))
                return true;
        if (SHEEP.getY() == 7)
            if (array[SHEEP.getY() - 1][SHEEP.getX() + 1] != 0 && array[SHEEP.getY() - 1][SHEEP.getX() - 1] != 0)
                return true;
        if (SHEEP.getX() == 7)
            if (array[SHEEP.getY() - 1][SHEEP.getX() - 1] != 0 && array[SHEEP.getY() + 1][SHEEP.getX() - 1] != 0)
                return true;
        if (SHEEP.getX() != 0 && SHEEP.getY() != 7 && SHEEP.getX() != 7 && SHEEP.getY() != 0)
            if (array[SHEEP.getY() - 1][SHEEP.getX() - 1] != 0 && array[SHEEP.getY() + 1][SHEEP.getX() - 1] != 0
                    && array[SHEEP.getY() - 1][SHEEP.getX() + 1] != 0 && array[SHEEP.getY() + 1][SHEEP.getX() + 1] != 0)
                return true;
        int countY = 0;
        for (int i = 1; i < 5; i++) {
            if (Type.values()[i].getY() >= SHEEP.getY()) countY++;
        }
        return countY == 4;
    }
}