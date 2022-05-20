public class MyTetris {
    private ProgramLogic programLogic;
    private ProgramInterface programInterface;

    public static void main(String[] args) throws InterruptedException {
        MyTetris myTetris = new MyTetris();
    }

    public MyTetris() throws InterruptedException {
        programLogic = new ProgramLogic();
        programInterface = new ProgramInterface(programLogic);
    }
}
