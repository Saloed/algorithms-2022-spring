public class MyTetris {
    public static void main(String[] args) throws InterruptedException {
        MyTetris myTetris = new MyTetris();
    }
    public MyTetris() throws InterruptedException {
        ProgramLogic programLogic = new ProgramLogic();
        ProgramInterface programInterface = new ProgramInterface(programLogic);
    }
}
