
### Escuela Colombiana de Ingeniería

### Arquitecturas de Software – ARSW
## Laboratorio Programación concurrente, condiciones de carrera, esquemas de sincronización, colecciones sincronizadas y concurrentes - Caso Dogs Race

### Descripción:
Este ejercicio tiene como fin que el estudiante conozca y aplique conceptos propios de la programación concurrente.

### Parte I 
Antes de terminar la clase.

Creación, puesta en marcha y coordinación de hilos.

1. Revise el programa “primos concurrentes” (en la carpeta parte1), dispuesto en el paquete edu.eci.arsw.primefinder. Este es un programa que calcula los números primos entre dos intervalos, distribuyendo la búsqueda de los mismos entre hilos independientes. Por ahora, tiene un único hilo de ejecución que busca los primos entre 0 y 30.000.000. Ejecútelo, abra el administrador de procesos del sistema operativo, y verifique cuantos núcleos son usados por el mismo.
    
    ***Después de ejecutar el Main principal de la parte uno en el administrador de tareas (como se muestra a continuación en el PID:1868) el Uso de CPU crece a medida que se ejecuta tomando cerca de la mitad de la CPU del computador.***
   
    ![](./img/media/p1-1.png)
    ![](./img/media/p1-2.png)
   
2. Modifique el programa para que en lugar de resolver el problema con un solo hilo, lo haga con tres, donde cada uno de éstos hará la tarcera parte del problema original. Verifique nuevamente el funcionamiento, y nuevamente revise el uso de los núcleos del equipo.
   
    ***Al Realizar los cambios, verificamos que con respecto al punto anterior el consumo de CPU creció aún más  debido a que usó mas recursos del computador al tiempo para poder completar todas las tareas***
   
    ![](./img/media/p2-1.png)
    ![](./img/media/p2-2.png)
   
3. Lo que se le ha pedido es: debe modificar la aplicación de manera que cuando hayan transcurrido 5 segundos desde que se inició la ejecución, se detengan todos los hilos y se muestre el número de primos encontrados hasta el momento. Luego, se debe esperar a que el usuario presione ENTER para reanudar la ejecución de los mismo.
    ```java
    public class PrimeFinderThread extends Thread {

	private boolean pausa;
	int a, b;
	Object key;

	private List<Integer> primes = new LinkedList<Integer>();

	public PrimeFinderThread(int a, int b) {
		super();
		this.a = a;
		this.b = b;
		pausa = false;
		key = 1;

	}

	public void run() {

		for (int i = a; i <= b; i++) {
			if (isPrime(i)) {
				primes.add(i);
				System.out.println(i);
			}
			if (pausa) {
				synchronized (key) {
					try {
						key.wait();
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
				}
			}

		}

	}

	boolean isPrime(int n) {
		if (n % 2 == 0)
			return false;
		for (int i = 3; i * i <= n; i += 2) {
			if (n % i == 0)
				return false;
		}
		return true;
	}

	public List<Integer> getPrimes() {
		return primes;
	}

	public void pausa() {
		pausa = true;

	}

	public void reanudar() {
		pausa = false;
		synchronized (key) {
			key.notify();
}
;
}

}
    ```

### Parte II 


Para este ejercicio se va a trabajar con un simulador de carreras de galgos (carpeta parte2), cuya representación gráfica corresponde a la siguiente figura:

![](./img/media/image1.png)

En la simulación, todos los galgos tienen la misma velocidad (a nivel de programación), por lo que el galgo ganador será aquel que (por cuestiones del azar) haya sido más beneficiado por el *scheduling* del
procesador (es decir, al que más ciclos de CPU se le haya otorgado durante la carrera). El modelo de la aplicación es el siguiente:

![](./img/media/image2.png)

Como se observa, los galgos son objetos ‘hilo’ (Thread), y el avance de los mismos es visualizado en la clase Canodromo, que es básicamente un formulario Swing. Todos los galgos (por defecto son 17 galgos corriendo en una pista de 100 metros) comparten el acceso a un objeto de tipo
RegistroLLegada. Cuando un galgo llega a la meta, accede al contador ubicado en dicho objeto (cuyo valor inicial es 1), y toma dicho valor como su posición de llegada, y luego lo incrementa en 1. El galgo que
logre tomar el ‘1’ será el ganador.

Al iniciar la aplicación, hay un primer error evidente: los resultados (total recorrido y número del galgo ganador) son mostrados antes de que finalice la carrera como tal. Sin embargo, es posible que una vez corregido esto, haya más inconsistencias causadas por la presencia de condiciones de carrera.

Parte III

1.  Corrija la aplicación para que el aviso de resultados se muestre
    sólo cuando la ejecución de todos los hilos ‘galgo’ haya finalizado.
    Para esto tenga en cuenta:

    a.  La acción de iniciar la carrera y mostrar los resultados se realiza a partir de la línea 38 de MainCanodromo.

    b.  Puede utilizarse el método join() de la clase Thread para sincronizar el hilo que inicia la carrera, con la finalización de los hilos de los galgos.
    
    **Para que el resultado sólamente muestre sólo cuando la ejecución de todos los hilos haya finalizado, usamos join() con el cual sincronizamos todos los hilos que inician la carrera con sus respectivas finalizaciones.De esta forma el aviso de resultado solamente se activa una vez  todos los hilos hayan finalizado**

2.  Una vez corregido el problema inicial, corra la aplicación varias
    veces, e identifique las inconsistencias en los resultados de las
    mismas viendo el ‘ranking’ mostrado en consola (algunas veces
    podrían salir resultados válidos, pero en otros se pueden presentar
    dichas inconsistencias). A partir de esto, identifique las regiones
    críticas () del programa.

    **Se identificó como algunas de las posiciones se repetían es allí como identificamos este problema como región crítica**
3.  Utilice un mecanismo de sincronización para garantizar que a dichas
    regiones críticas sólo acceda un hilo a la vez. Verifique los
    resultados.
    
    **Se usa synchronized  en galgo en el metodo corra()  el cual fue util al momento de corregir el problema anterior ya que evitamos que las posiciones que toma cada Galgo se repitiera**
4.  Implemente las funcionalidades de pausa y continuar. Con estas,
    cuando se haga clic en ‘Stop’, todos los hilos de los galgos
    deberían dormirse, y cuando se haga clic en ‘Continue’ los mismos
    deberían despertarse y continuar con la carrera. Diseñe una solución que permita hacer esto utilizando los mecanismos de sincronización con las primitivas de los Locks provistos por el lenguaje (wait y notifyAll).
    
    **Para implementar pausar y renaudar , en la clase MainCanodromo  corregimos la implementacion de éstos métodos, para ello usamos una condicion booleana para pausar la cual se valida dentro del metodo corre() y el renaudar usa notifyAll() para despertar todos los hilos que hayan sido pausados**



## Autores ✒️
* [Johan Damian Garrido Florez](https://github.com/anamariasalazar)
* [Posso Guevara Juan Camilo](https://github.com/RichardUG)

## Licencia 📄

Licencia bajo la [GNU General Public License](/LICENSE)
