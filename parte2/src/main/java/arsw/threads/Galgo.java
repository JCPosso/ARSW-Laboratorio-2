package arsw.threads;

/**
 * Un galgo que puede correr en un carril
 * 
 * @author rlopez
 * 
 */
public class Galgo extends Thread {
	private int paso;
	private Carril carril;
	RegistroLlegada regl;
        private static Object cerrojo = new Object();
        private boolean pausa;

	public Galgo(Carril carril, String name, RegistroLlegada reg) {
		super(name);
		this.carril = carril;
		paso = 0;
		this.regl=reg;
                pausa = false;
	}

	public void corra() throws InterruptedException {
		while (paso < carril.size()) {
                        while(pausa){
                            synchronized(cerrojo){
                                cerrojo.wait();
                            }
                        }
			Thread.sleep(100);
			carril.setPasoOn(paso++);
			carril.displayPasos(paso);
			
			if (paso == carril.size()) {						
				carril.finish();
				int ubicacion=regl.getAndPlus(1);
				System.out.println("El galgo "+this.getName()+" llego en la posicion "+ubicacion);
				if (ubicacion==1){
					regl.setGanador(this.getName());
				}
				
			}
		}
	}


	@Override
	public void run() {
		
		try {
			corra();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
        
        public void pausa(){
            pausa = true;
        }
        public void reanudar(){
            pausa = false;
            synchronized(cerrojo){
                cerrojo.notifyAll();
            }
        }

}
