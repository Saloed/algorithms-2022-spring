package game.components;

import game.Pair;
import game.PlayerType;
import game.Type;

import java.util.*;

import static game.PlayerType.*;
import static game.Type.*;
import static game.Type.SHEEP;

public class Algorithm {

    private final AiMove moveComponent;

    public Algorithm(AiMove move) {
        this.moveComponent = move;

    }

    public static double DIFFICULTY;

    int MAX_VALUE = 255;
    int MIN_VALUE = 0;

    public static int[][] array = new int[8][8];

    final Pair<Integer, Integer>[] moves = new Pair[]{
            new Pair<>(+1, +1),
            new Pair<>(+1, -1),
            new Pair<>(-1, -1),
            new Pair<>(-1, +1)
    };
    Type[] wolfs = new Type[]{WOLF1, WOLF2, WOLF3, WOLF4};

    private final Map<Type, Pair<Integer, Integer>> typeCoordinate = new HashMap<>();

    Pair<Integer, Integer> getCoordinate(Type type) {
        return typeCoordinate.get(type);
    }

    public void setFirstCooridnate() {
        setCoordinate(SHEEP, new Pair<>(7, 4));
        setCoordinate(WOLF1, new Pair<>(0, 1));
        setCoordinate(WOLF2, new Pair<>(0, 3));
        setCoordinate(WOLF3, new Pair<>(0, 5));
        setCoordinate(WOLF4, new Pair<>(0, 7));
    }

    public void setUpDifficulty(int dif) {
        DIFFICULTY = dif;
    }

    public void setCoordinate(Type type, Pair<Integer, Integer> coordinate) {
        typeCoordinate.put(type, coordinate);
    }

    public int getX(Type type) {
        return typeCoordinate.get(type).getSecond();
    }

    public int getY(Type type) {
        return typeCoordinate.get(type).getFirst();
    }

    public void callMinMax(PlayerType entity) {
        minMax(entity, 0, -500, 500);
    }

    public int minMax(PlayerType entity, int recursiveLvl, int alpha, int beta) {

        if (recursiveLvl == 0) prepareField();
        int test;

        if (recursiveLvl >= 2 * DIFFICULTY) {
            int evaluation = getEvaluation();
            prepareField();
            return evaluation;
        }
        int bestMove = 255;
        boolean isWolf = (entity == WOLF);
        int MinMax = isWolf ? MIN_VALUE : MAX_VALUE;

        for (int i = (isWolf ? 0 : 8); i < (isWolf ? 8 : 12); i++) {
            int curEntity = isWolf ? ((i / 2) + 1) : 0;

            Pair<Integer, Integer> curEntityPosition = (curEntity == 0) ? getCoordinate(SHEEP) : getCoordinate(wolfs[curEntity - 1]);
            Pair<Integer, Integer> curMove = moves[isWolf ? i % 2 : i % 4];

            if (canMove(curEntityPosition.getFirst() + curMove.getFirst(), curEntityPosition.getSecond() + curMove.getSecond())) {

                entityMove(curEntity, curMove.getFirst(), curMove.getSecond());
                test = minMax(isWolf ? PlayerType.SHEEP : WOLF, recursiveLvl + 1, alpha, beta);
                entityMove(curEntity, -curMove.getFirst(), -curMove.getSecond());

                if ((test > MinMax && entity == WOLF) || (test <= MinMax && entity == PlayerType.SHEEP) || (bestMove == 255)) {
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
            System.out.println("hereeeeeeeeeeeeeee");
            if (entity == WOLF) {
                setCoordinate(wolfs[bestMove / 2], getCoordinate(wolfs[bestMove / 2]).addTo(moves[bestMove % 2]));
                prepareField();
                moveComponent.moveEntity(wolfs[bestMove / 2], getY(wolfs[bestMove / 2]), getX(wolfs[bestMove / 2]));

            } else {
                setCoordinate(SHEEP, getCoordinate(SHEEP).addTo(moves[bestMove % 4]));
                prepareField();
                moveComponent.moveEntity(SHEEP, getY(SHEEP), getX(SHEEP));
            }
        }
        return MinMax;
    }

    public void entityMove(int curEntity, int y, int x) {
        if (curEntity == 0) {
            array[getY(SHEEP)][getX(SHEEP)] = 0;
            array[getY(SHEEP) + y][getX(SHEEP) + x] = 1;
            setCoordinate(SHEEP, new Pair<>(getY(SHEEP) + y, getX(SHEEP) + x));
        } else {
            array[getY(wolfs[curEntity - 1])][getX(wolfs[curEntity - 1])] = 0;
            array[getY(wolfs[curEntity - 1]) + y][getX(wolfs[curEntity - 1]) + x] = 255;
            setCoordinate(wolfs[curEntity - 1], new Pair<>(getY(wolfs[curEntity - 1]) + y, getX(wolfs[curEntity - 1]) + x));
        }
    }

    public void prepareField() {

        array = new int[8][8];

        array[getY(SHEEP)][getX(SHEEP)] = 1;

        for (Type wolf : wolfs) array[getY(wolf)][getX(wolf)] = 255;
    }


    public int getEvaluation() {
        ArrayDeque<Pair<Integer, Integer>> deque = new ArrayDeque<>();
        if (getY(SHEEP) == 0) return 0;
        int minCycle = 200;
        boolean minActive = false;
        deque.push(getCoordinate(SHEEP));

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
        if (getX(SHEEP) == 0 && getY(SHEEP) == 7 && array[6][1] != 0)
            return true;
        if (getX(SHEEP) == 0 && getY(SHEEP) < 7)
            if ((array[getY(SHEEP) + 1][getX(SHEEP) + 1] != 0 && array[getY(SHEEP) - 1][getX(SHEEP) + 1] != 0))
                return true;
        if (getY(SHEEP) == 7)
            if (array[getY(SHEEP) - 1][getX(SHEEP) + 1] != 0 && array[getY(SHEEP) - 1][getX(SHEEP) - 1] != 0)
                return true;
        if (getX(SHEEP) == 7)
            if (array[getY(SHEEP) - 1][getX(SHEEP) - 1] != 0 && array[getY(SHEEP) + 1][getX(SHEEP) - 1] != 0)
                return true;
        if (getX(SHEEP) != 0 && getY(SHEEP) != 7 && getX(SHEEP) != 7 && getY(SHEEP) != 0)
            if (array[getY(SHEEP) - 1][getX(SHEEP) - 1] != 0 && array[getY(SHEEP) + 1][getX(SHEEP) - 1] != 0
                    && array[getY(SHEEP) - 1][getX(SHEEP) + 1] != 0 && array[getY(SHEEP) + 1][getX(SHEEP) + 1] != 0)
                return true;
        int countY = 0;
        for (int i = 1; i < 5; i++) {
            if (getY(Type.values()[i]) >= getY(SHEEP)) countY++;
        }
        return countY == 4;
    }

    public boolean canWolfStandInThatCell(int y, int x, Type wolf) {
        if (y - getY(wolf) == 1 && Math.abs(x - getX(wolf)) == 1) {
            if ((y >= 0 && x >= 0 && y <= 7 && x <= 7)) {
                if (array[y][x] == 0) {
                    setCoordinate(wolf, new Pair<>(y, x));
                    array[y][x] = 0;
                    moveComponent.moveEntity(wolf, getY(wolf), getX(wolf));
                    prepareField();
                    return true;
                }
            }
        }
        return false;
    }
    public boolean canSheepStandInThatCell(int xNew, int yNew, int xLast, int yLast) {
        if (Math.abs(xNew - xLast) == 1 && Math.abs(yNew - yLast) == 1) {
            if ((xNew >= 0 && yNew >= 0 && xNew <= 7 && yNew <= 7)) {

                if (array[yNew][xNew] == 0) {
                    setCoordinate(SHEEP, new Pair<>(yNew, xNew));
                    moveComponent.moveEntity(SHEEP,getY(SHEEP),getX(SHEEP));
                    array[yLast][xLast] = 0;
                    return true;
                }
            }
        }
        return false;
    }
}