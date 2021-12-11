import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//2 상속
class RecoveredPerson extends Person{
    //3 생성자
    public RecoveredPerson(String name, String location) throws Exception {
        super(name, location);
    }

    //5 오버라이딩
    @Override
    public void print() {
        System.out.println("완치자 이름 : " + this.name + " 거주위치 : " + this.location );
    }
}


/**
 * 일반적인 사람 객체
 * 이름과 거주지.
 */
//7 추상 클래스, 8 다형성
abstract class Person {
    String name;
    String location;
    //6 오버 로딩
    public Person(){

    }
    public Person(String name,String location) throws Exception {
        this.name = name;
        this.location = location;
        if(name == null || location == null){
            //10 예외 처리
            throw new Exception("올바르지 않은 생성입니다.");
        }
    }

    public abstract void print();
}


class PrintByRunnableWorker implements Runnable {
    List<Person> people;
    int thread_i;
    public PrintByRunnableWorker(List<Person> personArrayList, int i){
        this.people = personArrayList;
        this.thread_i = i;
    }

    public void run(){
        for(int i=this.thread_i;i<people.size();i=i+3){
            people.get(i).print();
        }
    }
}


/**
 * Runable을 통해 쓰레드를 사용하여 전체 사용자들을 출력하는 함수.
 */
class PrintByRunnable implements Calculator{
    List<Person> personArrayList;
    public PrintByRunnable(List<Person> personArrayList){
        this.personArrayList = personArrayList;
    }

    /**
     * 쓰레드를 나누어서 전체 사용자를 출력
     */
    @Override
    public void calculate() {
        PrintByRunnableWorker[] types = new PrintByRunnableWorker[3];
        for(int i=0;i<3;i++){
            types[i] = new PrintByRunnableWorker(this.personArrayList, i);
        }
        Thread[] ttt = new Thread[3];
        int ii = 0;
        //runable 객체를 Thread에 넣어서 실행
        for(Runnable r : types){
            Thread tt = new Thread(r);
            tt.start();
            ttt[ii++] = tt;
        }
        //각 개체가 끝나야함.
        for(int i=0;i<3;i++){
            try {
                ttt[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


class CalculateStatisticByPersonType implements Calculator{
    List<Person> personArrayList;
    public CalculateStatisticByPersonType(List<Person> personArrayList){
        this.personArrayList = personArrayList;
    }
    @Override
    public void calculate() {
        CalculateStatisticByPersonTypeThread[] types = new CalculateStatisticByPersonTypeThread[3];
        //쓰레드에 나눠서 감염자 완치자를 계산
        for(int i=0;i<3;i++){
            types[i] = new CalculateStatisticByPersonTypeThread(this.personArrayList, i);
            types[i].start();
        }
        //각 개체가 끝나야함
        for(Thread t : types){
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int infected =0 ;
        int recoverd = 0;
        //끝난 쓰레드를 합함
        for(int i=0;i<3;i++){
            infected = infected + types[i].infected;
            recoverd = recoverd + types[i].recovered;
        }
        System.out.println("감염자 : " + infected);
        System.out.println("완치자 : " + recoverd);
    }
}


class CalculateStatisticByPersonTypeThread extends Thread{
    List<Person> people;
    int i;
    int infected;
    int recovered;
    public CalculateStatisticByPersonTypeThread(List<Person> personArrayList, int i){
        this.people = personArrayList;
        this.i = i;
        //9 박싱 언박싱
        this.infected = new Integer(0);
        this.recovered = new Integer(0);
    }

    //해당 쓰레드에 할당된 사람들의 통계를 계산
    public void run(){
        for(int i=this.i;i<people.size();i=i+3){
            if(people.get(i) instanceof InfectedPerson){
                infected = infected + 1;
            }else{
                recovered = recovered + 1;
            }
        }
    }
}


// 11 인터페이스
interface Calculator {
    //계산하는 함수
    void calculate();
}


class InfectedPerson extends Person{
    //6
    int time;
    public InfectedPerson(String name, String location,int time) throws Exception {
        super(name, location);
        this.time = time;
    }
    public InfectedPerson(String name, String location) throws Exception {
        super(name, location);
        this.time = 0;
    }

    @Override
    public void print() {
        System.out.println("감염자 이름 : " + this.name + " 거주위치 : " + this.location );
    }
}


/**
 * 코로나 관리 프로그램
 * 처음 시작하면 현재 관리중인 사람의 리스트를 출력.
 * 1을 입력하면 감염자 사람 추가.
 * 2을 입력하면 완치자 사람 추가.
 * 3를 입력하면 사람 출력
 * 4을 입력하면 각 통계 계산
 * 0을 입력하면 프로그램 종료
 */
public class Main {
    //4 스태틱 메소드
    public static List<Person> createPerson() throws Exception {
        ArrayList<Person> response = new ArrayList<>();
        response.add(new InfectedPerson("도널드","미국", 3));
        response.add(new InfectedPerson("잭슨","미국"));
        response.add(new RecoveredPerson("김민수","한국"));
        return response;
    }
    public static void main(String[] args) throws Exception {
        System.out.println("코로나 관리 프로그램입니다.");
        List<Person> initialList = createPerson();
        for(Person p : initialList){
            p.print();
        }
        while(true){
            Scanner scanner = new Scanner(System.in);
            System.out.println("연산을 입력해 주세요");
            System.out.println("1 : 감염자 추가");
            System.out.println("2 : 완치자 추가");
            System.out.println("3 : 전체 출력");
            System.out.println("4 : 완치자, 감염자 수 계산");
            System.out.println("0 : 종료");
            int n = scanner.nextInt();
            scanner.nextLine();
            if(n == 0){
                return;
            }else if(n == 1){
                System.out.println("이름을 입력해 주세요 : ");
                String name = scanner.nextLine();
                System.out.println("거주지를 입력해 주세요 : ");
                String location = scanner.nextLine();
                initialList.add(new InfectedPerson(name,location));
            }else if(n == 2){
                System.out.println("이름을 입력해 주세요 : ");
                String name = scanner.nextLine();
                System.out.println("거주지를 입력해 주세요 : ");
                String location = scanner.nextLine();
                initialList.add(new RecoveredPerson(name,location));
            }else if(n == 3){
                //runable을 통해서 전체 출력
                PrintByRunnable cal = new PrintByRunnable(initialList);
                cal.calculate();
            }else if(n== 4){
                //Thread 사용하여 완치자 감염자 계산
                CalculateStatisticByPersonType cal = new CalculateStatisticByPersonType(initialList);
                cal.calculate();
            }
        }
    }
}
