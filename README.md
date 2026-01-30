
### Escuela Colombiana de Ingeniería
### Arquitecturas de Software - ARSW
## Ejercicio Introducción al paralelismo - Hilos - Caso BlackListSearch


### Dependencias:
####   Lecturas:
*  [Threads in Java](http://beginnersbook.com/2013/03/java-threads/)  (Hasta 'Ending Threads')
*  [Threads vs Processes]( http://cs-fundamentals.com/tech-interview/java/differences-between-thread-and-process-in-java.php)

### Descripción
  Este ejercicio contiene una introducción a la programación con hilos en Java, además de la aplicación a un caso concreto.
  
#### Integrantes: Laura Alejandra Venegas Piraban y Sergio Alejandro Idarraga Torres
**Parte I - Introducción a Hilos en Java**

1. De acuerdo con lo revisado en las lecturas, complete las clases CountThread, para que las mismas definan el ciclo de vida de un hilo que imprima por pantalla los números entre A y B.
2. Complete el método __main__ de la clase CountMainThreads para que:
	1. Cree 3 hilos de tipo CountThread, asignándole al primero el intervalo [0..99], al segundo [99..199], y al tercero [200..299].
	2. Inicie los tres hilos con 'start()'.
	3. Ejecute y revise la salida por pantalla. 
	4. Cambie el incio con 'start()' por 'run()'. Cómo cambia la salida?, por qué?.  
	**Respuesta**    
	Al inicio observamos que al usar start() la salida parecía desordenada, como si no estuviera funcionando correctamente. Sin embargo, al utilizar run(), los resultados se mostraban de manera ordenada y secuencial, lo que hacía más fácil seguir cada conjunto de números.  

	La diferencia radica en que run() ejecuta el código dentro del mismo hilo principal, por lo que las tareas se realizan una tras otra de forma secuencial. En cambio, start() crea hilos independientes que se ejecutan en paralelo.  

**Parte II - Ejercicio Black List Search**


Para un software de vigilancia automática de seguridad informática se está desarrollando un componente encargado de validar las direcciones IP en varios miles de listas negras (de host maliciosos) conocidas, y reportar aquellas que existan en al menos cinco de dichas listas. 

Dicho componente está diseñado de acuerdo con el siguiente diagrama, donde:

- HostBlackListsDataSourceFacade es una clase que ofrece una 'fachada' para realizar consultas en cualquiera de las N listas negras registradas (método 'isInBlacklistServer'), y que permite también hacer un reporte a una base de datos local de cuando una dirección IP se considera peligrosa. Esta clase NO ES MODIFICABLE, pero se sabe que es 'Thread-Safe'.

- HostBlackListsValidator es una clase que ofrece el método 'checkHost', el cual, a través de la clase 'HostBlackListDataSourceFacade', valida en cada una de las listas negras un host determinado. En dicho método está considerada la política de que al encontrarse un HOST en al menos cinco listas negras, el mismo será registrado como 'no confiable', o como 'confiable' en caso contrario. Adicionalmente, retornará la lista de los números de las 'listas negras' en donde se encontró registrado el HOST.

![](img/Model.png)

Al usarse el módulo, la evidencia de que se hizo el registro como 'confiable' o 'no confiable' se dá por lo mensajes de LOGs:

INFO: HOST 205.24.34.55 Reported as trustworthy

INFO: HOST 205.24.34.55 Reported as NOT trustworthy


Al programa de prueba provisto (Main), le toma sólo algunos segundos análizar y reportar la dirección provista (200.24.34.55), ya que la misma está registrada más de cinco veces en los primeros servidores, por lo que no requiere recorrerlos todos. Sin embargo, hacer la búsqueda en casos donde NO hay reportes, o donde los mismos están dispersos en las miles de listas negras, toma bastante tiempo.

Éste, como cualquier método de búsqueda, puede verse como un problema [vergonzosamente paralelo](https://en.wikipedia.org/wiki/Embarrassingly_parallel), ya que no existen dependencias entre una partición del problema y otra.

Para 'refactorizar' este código, y hacer que explote la capacidad multi-núcleo de la CPU del equipo, realice lo siguiente:

1. Cree una clase de tipo Thread que represente el ciclo de vida de un hilo que haga la búsqueda de un segmento del conjunto de servidores disponibles. Agregue a dicha clase un método que permita 'preguntarle' a las instancias del mismo (los hilos) cuantas ocurrencias de servidores maliciosos ha encontrado o encontró.

2. Agregue al método 'checkHost' un parámetro entero N, correspondiente al número de hilos entre los que se va a realizar la búsqueda (recuerde tener en cuenta si N es par o impar!). Modifique el código de este método para que divida el espacio de búsqueda entre las N partes indicadas, y paralelice la búsqueda a través de N hilos. Haga que dicha función espere hasta que los N hilos terminen de resolver su respectivo sub-problema, agregue las ocurrencias encontradas por cada hilo a la lista que retorna el método, y entonces calcule (sumando el total de ocurrencuas encontradas por cada hilo) si el número de ocurrencias es mayor o igual a _BLACK_LIST_ALARM_COUNT_. Si se da este caso, al final se DEBE reportar el host como confiable o no confiable, y mostrar el listado con los números de las listas negras respectivas. Para lograr este comportamiento de 'espera' revise el método [join](https://docs.oracle.com/javase/tutorial/essential/concurrency/join.html) del API de concurrencia de Java. Tenga también en cuenta:

	* Dentro del método checkHost Se debe mantener el LOG que informa, antes de retornar el resultado, el número de listas negras revisadas VS. el número de listas negras total (línea 60). Se debe garantizar que dicha información sea verídica bajo el nuevo esquema de procesamiento en paralelo planteado.

	* Se sabe que el HOST 202.24.34.55 está reportado en listas negras de una forma más dispersa, y que el host 212.24.24.55 NO está en ninguna lista negra.


**Parte II.I Para discutir la próxima clase (NO para implementar aún)**

La estrategia de paralelismo antes implementada es ineficiente en ciertos casos, pues la búsqueda se sigue realizando aún cuando los N hilos (en su conjunto) ya hayan encontrado el número mínimo de ocurrencias requeridas para reportar al servidor como malicioso. Cómo se podría modificar la implementación para minimizar el número de consultas en estos casos?, qué elemento nuevo traería esto al problema?

**Parte III - Evaluación de Desempeño**

A partir de lo anterior, implemente la siguiente secuencia de experimentos para realizar las validación de direcciones IP dispersas (por ejemplo 202.24.34.55), tomando los tiempos de ejecución de los mismos (asegúrese de hacerlos en la misma máquina):

1. Un solo hilo.
2. Tantos hilos como núcleos de procesamiento (haga que el programa determine esto haciendo uso del [API Runtime](https://docs.oracle.com/javase/7/docs/api/java/lang/Runtime.html)).
3. Tantos hilos como el doble de núcleos de procesamiento.
4. 50 hilos.
5. 100 hilos.

Al iniciar el programa ejecute el monitor jVisualVM, y a medida que corran las pruebas, revise y anote el consumo de CPU y de memoria en cada caso. ![](img/jvisualvm.png)

Con lo anterior, y con los tiempos de ejecución dados, haga una gráfica de tiempo de solución vs. número de hilos. Analice y plantee hipótesis con su compañero para las siguientes preguntas (puede tener en cuenta lo reportado por jVisualVM):  

Cuando realizamos lo anterior fue en un computador con 8 núcleos de procesamiento, al usar la JvisualVM obtuvimos los siguiente resultados:

**Desempeño con 1 hilo:**

<div style="text-align: center;">

![](img/desempeño1Hilo.png)

</div>

**Desempeño con 8 hilos (igual a núcleos):**

<div style="text-align: center;">

![](img/desempeño%208%20hilos.png)

</div>

**Desempeño con 16 hilos (doble de núcleos):**

<div style="text-align: center;">

![](img/desempeño%2016%20hilos.png)

</div>

**Desempeño con 50 hilos:**

<div style="text-align: center;">

![](img/desempeño%2050%20hilos.png)

</div>

**Desempeño con 100 hilos:**

<div style="text-align: center;">

![](img/desempeño%20100%20hilos.png)

</div>    

**GRÁFICA DE RENDIMIENTO**

La siguiente gráfica muestra la relación entre el número de hilos (eje X) y el tiempo de solución en milisegundos (eje Y) para la validación de la IP. Se puede observar claramente cómo el tiempo de ejecución disminuye significativamente al aumentar el número de hilos, alcanzando una aceleración mucho más grande cuando se usan 100 hilos en comparación con un solo hilo.  

<div style="text-align: center;">

![Gráfica de Tiempo vs Número de Hilos](img/graficaHilos.png)

</div>  

**Observación de la gráfica:** A primera vista, la distribución de los puntos parece formar una línea casi recta. Esto se debe a que la escala del eje Y (tiempo en milisegundos) es mucho más grande que la del eje X (número de hilos). Esta diferencia de escalas hace que las mejoras de rendimiento, aunque significativas, visualmente parezcan estar alineadas. En realidad, existe una reducción exponencial del tiempo a medida que aumentamos los hilos.

**Hipótesis**  

Con base en los resultados observados, planteamos la siguiente hipótesis: aumentar el número de hilos mejora significativamente el rendimiento hasta cierto punto. Sin embargo, existe un límite más allá del cual agregar más hilos se vuelve contraproducente.

En nuestro caso, observamos que el rendimiento mejora continuamente desde 1 hilo hasta 100 hilos. No obstante, es probable (leyendo los siguientes puntos) que si continuáramos aumentando el número de hilos (por ejemplo, a 500 o 1000), el overhead de creación, administración y cambio de contexto superaría los beneficios del paralelismo, causando una degradación del rendimiento.   



**Parte IV - Ejercicio Black List Search**

1. Según la [ley de Amdahls](https://www.pugetsystems.com/labs/articles/Estimating-CPU-Performance-using-Amdahls-Law-619/#WhatisAmdahlsLaw?):

	![](img/ahmdahls.png), donde _S(n)_ es el mejoramiento teórico del desempeño, _P_ la fracción paralelizable del algoritmo, y _n_ el número de hilos, a mayor _n_, mayor debería ser dicha mejora. Por qué el mejor desempeño no se logra con los 500 hilos?, cómo se compara este desempeño cuando se usan 200?.  

	**Respuesta:**  
	El problema de usar 500 hilos es que el overhead asociado al crear, unir y planificar una cantidad tan grande de hilos consume mucho tiempo. Además, el sistema operativo comienza a realizar un excesivo cambio de contexto (intervaling) entre hilos, lo que reduce significativamente el rendimiento. 
	
	Según la Ley de Amdahl, la parte secuencial del algoritmo (que no es paralelizable) se convierte en un cuello de botella dominante. A medida que incrementamos el número de hilos, el beneficio marginal disminuye porque la porción secuencial del código limita la aceleración máxima posible, sin importar cuántos hilos agreguemos.
	
	Comparando con 200 hilos, el desempeño con 500 hilos es peor debido al mayor overhead de administración de hilos y el aumento del intervaling. Con 200 hilos ya se alcanza un punto donde el costo de gestionar los hilos supera los beneficios de la paralelización. En sistemas con pocos núcleos, usar más de 100 hilos típicamente degrada el rendimiento en lugar de mejorarlo. 

2. Cómo se comporta la solución usando tantos hilos de procesamiento como núcleos comparado con el resultado de usar el doble de éste?.

	**Respuesta:**  
	Según nuestras pruebas con 8 núcleos de procesamiento, observamos una diferencia significativa entre usar 8 hilos y usar 16 hilos (el doble).
	
	El rendimiento con 16 hilos es aproximadamente dos veces mejor que con 8 hilos. Esto se debe a que, aunque tenemos solo 8 núcleos físicos, el sistema operativo puede alternar eficientemente entre 16 hilos, aprovechando los tiempos de espera y mejorando la utilización del procesador.
	


3. De acuerdo con lo anterior, si para este problema en lugar de 100 hilos en una sola CPU se pudiera usar 1 hilo en cada una de 100 máquinas hipotéticas, la ley de Amdahls se aplicaría mejor?. Si en lugar de esto se usaran c hilos en 100/c máquinas distribuidas (siendo c es el número de núcleos de dichas máquinas), se mejoraría?. Explique su respuesta.

	**Respuesta:**
	
	Escenario 1: 1 hilo en 100 máquinas
	
	Sí, la Ley de Amdahl se aplicaría significativamente mejor en este escenario distribuido. Las razones son:
	
	- Eliminación del overhead local: Cada máquina ejecuta solo 1 hilo, eliminando el overhead de context switching que sufríamos con 100 hilos en una sola CPU.
	- Mejor aplicación de la Ley de Amdahl: La parte paralelizable del algoritmo (búsqueda en listas negras) se distribuye completamente entre máquinas independientes, minimizando la fracción secuencial en cada máquina.
	- Limitación nueva: El cuello de botella se traslada a la comunicación de red y la sincronización entre máquinas, pero esto es generalmente menos costoso que manejar 100 hilos locales.
	
	Escenario 2: N hilos en 100/N máquinas (donde N=8 núcleos)
	
	Sí, esto sería óptimo y se mejoraría aún más. Tendríamos:
	- Cada máquina opera sin overhead excesivo, usa exactamente sus núcleos disponibles.
	- La escalabilidad distribuida se mantiene mientras se optimiza el rendimiento local
	




