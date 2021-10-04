## 모니터
모니터는 동시성 제어를 위해 필요한 것들을 high level 로 추상화하여 개발자들이 편하게 상호배제를 구현하도록 하는 도구이다. 공유자원, 공유자원을 다루는 함수를 '모니터' 라는 객체로 
캡슐화 하고 공유자원에 접근하기위해선 이 모니터안으로 진입해야 한다. 각각의 공유자원(A,B,C) 은 Condition 이라는것으로 관리한다.<br><br>
Condition 은 wait() 와 signal() 을 지원한다. 
어떤 쓰레드(Thread-1)가 공유자원(A)을 요청했는데 
다른 쓰레드(Thread-2)가 이것을 이용하고있으면 Thread-1은 A.wait() 를 호출해 A 자원을 기다린다. A.wait() 를 호출하면 CPU 제어권을 다른 스레드에 넘겨주게 된다. CPU 제어권을 넘겨받은
다른 스레드(Thread-3)가 또 A 자원을 요청할 수 있고, A 자원을 요청한 다수의 스레드는 대기큐(Thread-1, Thread-3) 에서 A자원을 기다린다. <br>
그리고 Thread-2 가 A 자원을 다 쓰고 A.signal() 를 호출하고 임계구역을 빠져나간다. A 자원의 대기큐에 있는 스레드 하나가 깨어나서 임계구역에 진입한다. 
## 자바 syncronized 
키워드를 통해 편리하게 동시성 제어를 구현할 수 있으며 이는 모니터 도구에 기반하였다. 모든 Java의 Object들은 하나의 모니터를 갖고있는데, 이 모니터들은 Lock 과 Condition Variable
를 이용한다. <br>
메서드 자체에 syncronized 를 걸 수 있고, 혹은 공유자원(Object) 에 걸 수도 있다.<br>
  
  ```
  public class Counter{
    public static int count = 0;
    
    /* 메서드에 synchronized 키워드를 붙여서 사용할 수 있다.
    메서드에 붙이면 Counter 객체 자체가 모니터가 되어 동시성을 제어하게 된다.
    */
    public syncronized static void increment(){ 
      count++;
    }
  }
  ```
메서드 자체에 syncronized 를 붙여 사용할 수 있다. 이 방식은 단순하고 편리하게 사용할 수 있지만 Counter 객체 자체가 모니터가 되어 lock 을 획득한 스레드만 접근할 수 있게된다.
만약 Counter 객체에 동시성 제어가 필요없는 메서드가 있거나, increment() 함수 내에서도 count++ 를 제외한 다른 로직이 있다면(단순 로그찍기 같은) 임계구역 외에 다른 부분까지 
모니터화 시켜버린것이다. 이는 성능저하를 야기할 수 있다..

  ```
 public class Counter{
    public static int count = 0;
    
    public static Object object = new Object();
  
    public static void increment(){
      syncronized(object){
        count++;
      }
    }
  }
  ```
위처럼 특정 구간만 임계구역으로 설정할 수 있다. 이는 동기화가 필요한 부분만 임계구역으로 설정한것이다. lock 획득을 위한 객체를 괄호안에 넣어주면 된다. 
static 메서드로 구현하였기때문에 새로운 Object 객체를 생성했지만, static 이 아니라면 일반적으로는 this 를 통해 자기자신을 넣어준다.

